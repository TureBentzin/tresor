package net.juligames.tresor.rest;

import com.google.gson.Gson;
import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;

/**
 * @author Ture Bentzin
 * @since 21-02-2025
 */
@SuppressWarnings("UnusedReturnValue")
public class RESTCaller {

    private RESTCaller() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    private static final @NotNull Gson gson = new Gson();

    public enum Method {
        GET, POST, PUT, DELETE, PATCH, HEAD, OPTIONS, TRACE
    }

    public static @NotNull URL createURL(@NotNull String host, @NotNull String path) {
        return createURL(host + path);
    }

    public static @NotNull URL createURL(@NotNull String urlString) {
        try {
            return URI.create(urlString).toURL();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Blocking
    public static @NotNull String call(@NotNull URL url, @Nullable String jwt, @NotNull Method method) {
        return call(url, jwt, method, (String) null);
    }

    @Blocking
    public static @NotNull String call(@NotNull URL url, @Nullable String jwt, @NotNull Method method, @Nullable String body) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            connection.setRequestMethod(method.name());
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        }
        if (jwt != null)
            connection.setRequestProperty("Authorization", "Bearer " + jwt);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");

        if (body == null) body = "";
        if (method == Method.POST || method == Method.PUT || method == Method.PATCH) { //these have bodies
            connection.setDoOutput(true);
            try (OutputStream os = connection.getOutputStream()) {
                os.write(body.getBytes());
                os.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // handle response
        int responseCode = 0;
        try {
            responseCode = connection.getResponseCode();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            connection.disconnect();
        }
    }

    @Blocking
    public static @NotNull String call(@NotNull URL url, @Nullable String jwt, @NotNull Method method, @NotNull Object body) {
        return call(url, jwt, method, gson.toJson(body));
    }

    @Blocking
    public static @NotNull <R, T> R call(@NotNull URL url, @Nullable String jwt, @NotNull Method method, @NotNull T body, @NotNull Class<R> responseClass) {
        return gson.fromJson(call(url, jwt, method, body), responseClass);
    }

    @Blocking
    public static @NotNull <R> R call(@NotNull URL url, @Nullable String jwt, @NotNull Method method, @NotNull Class<R> responseClass) {
        return gson.fromJson(call(url, jwt, method), responseClass);
    }

    @Blocking
    public static @NotNull <R> R callPublic(@NotNull URL url, @NotNull Method method, @NotNull Class<R> responseClass) {
        return gson.fromJson(call(url, null, method), responseClass);
    }

    @Blocking
    public static @NotNull <R> R callPublic(@NotNull URL url, @NotNull Method method, @NotNull Object body, @NotNull Class<R> responseClass) {
        return gson.fromJson(call(url, null, method, body), responseClass);
    }

    @TestOnly
    @Blocking
    public static @NotNull String get(@NotNull URL url) {
        return call(url, null, Method.GET);
    }

}
