package net.juligames.tresor.controller;

import com.google.gson.Gson;
import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Ture Bentzin
 * @since 21-02-2025
 */
public class RESTCaller {

    private RESTCaller() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    private static final @NotNull Gson gson = new Gson();

    public enum Method {
        GET, POST, PUT, DELETE, PATCH, HEAD, OPTIONS, TRACE
    }

    @Blocking
    public static @NotNull String call(@NotNull URL url, @NotNull String jwt, @NotNull Method method, @NotNull String body) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method.name());
        connection.setRequestProperty("Authorization", "Bearer " + jwt);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");

        if (method == Method.POST || method == Method.PUT || method == Method.PATCH) { //these have bodies
            connection.setDoOutput(true);
            try (OutputStream os = connection.getOutputStream()) {
                os.write(body.getBytes());
                os.flush();
            }
        }

        // handle response
        int responseCode = connection.getResponseCode();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                responseCode >= 200 && responseCode < 300
                        ? connection.getInputStream()
                        : connection.getErrorStream()
        ))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        } finally {
            connection.disconnect();
        }
    }

    public static @NotNull String call(@NotNull URL url, @NotNull String jwt, @NotNull Method method, @NotNull Object body) throws Exception {
        return call(url, jwt, method, gson.toJson(body));
    }
}
