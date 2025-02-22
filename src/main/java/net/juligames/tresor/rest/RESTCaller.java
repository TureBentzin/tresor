package net.juligames.tresor.rest;

import com.google.gson.Gson;
import org.jetbrains.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;

/**
 * The RESTCaller class provides static methods for sending RESTful HTTP requests.
 *
 * <p><b>Features:</b></p>
 * <ul>
 *   <li><b>URL Creation:</b> Methods {@code createURL(String)} and {@code createURL(String, String)}
 *       generate URL objects from string data.</li>
 *   <li><b>HTTP Requests:</b> Supports various HTTP methods (GET, POST, PUT, DELETE, PATCH, HEAD,
 *       OPTIONS, TRACE) using the {@code Method} enum.</li>
 *   <li><b>Authentication:</b> Optionally accepts a JSON Web Token (JWT) for authentication.</li>
 *   <li><b>Request Handling:</b> For methods that include a request body (POST, PUT, PATCH), the
 *       request body is transmitted as a JSON string. Conversion between Java objects and JSON is
 *       handled via Gson.</li>
 *   <li><b>Response Processing:</b> The response is read and, depending on the HTTP status code,
 *       either converted into the desired object or returned as an error/unauthorized response
 *       encapsulated within a {@code ResponseContainer}.</li>
 * </ul>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>
 *     URL url = RESTCaller.createURL("http://example.com", "/api/data");
 *     ResponseContainer&lt;MyResponse&gt; response = RESTCaller.call(url, "myJWT", RESTCaller.Method.GET, MyResponse.class);
 * </pre>
 *
 * @author Ture Bentzin
 * @since 21-02-2025
 */
@SuppressWarnings("UnusedReturnValue")
public class RESTCaller {

    private static final Logger log = LoggerFactory.getLogger(RESTCaller.class);

    private RESTCaller() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    private static final @NotNull Gson gson = new Gson();

    public enum Method {
        GET, POST, PUT, DELETE, PATCH, HEAD, OPTIONS, TRACE
    }

    @Contract("_, _ -> new")
    public static @NotNull URL createURL(@NotNull String host, @NotNull String path) {
        return createURL(host + path);
    }

    @Contract("_ -> new")
    public static @NotNull URL createURL(@NotNull String urlString) {
        try {
            return URI.create(urlString).toURL();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static @NotNull <R> ResponseContainer<R> handleResponse(@NotNull RawResponse response, @NotNull Class<R> responseClass) {
        log.debug("Response JSON: {}", response.getResponse());
        if (response.isSuccessful()) {
            R responseObj = gson.fromJson(response.getResponse(), responseClass);
            return ResponseContainer.successful(responseObj);
        } else if (response.isUnauthorized()) {
            UnauthorizedResponse unauthorizedResponse = gson.fromJson(response.getResponse(), UnauthorizedResponse.class);
            return ResponseContainer.unauthorized(unauthorizedResponse);
        } else if (response.getStatusCode() == 422) {
            return ResponseContainer.unprocessableEntity(gson.fromJson(response.getResponse(), UnprocessableEntity.class));
        } else {
            return ResponseContainer.differentJson(response.getResponse());
        }
    }


    @ApiStatus.Internal
    @Blocking
    public static @NotNull RawResponse call(@NotNull URL url, @Nullable String jwt, @NotNull Method method) {
        return call(url, jwt, method, (String) null);
    }

    @ApiStatus.Internal
    @Blocking
    public static @NotNull RawResponse call(@NotNull URL url, @Nullable String jwt, @NotNull Method method, @Nullable String body) {
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

            return new RawResponse(response.toString(), responseCode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            connection.disconnect();
        }
    }


    @Blocking
    public static @NotNull <R, T> ResponseContainer<R> call(@NotNull URL url, @Nullable String jwt, @NotNull Method method, @NotNull T body, @NotNull Class<R> responseClass) {
        String json = gson.toJson(body);
        log.debug("Request JSON (BODY): {}", json);
        RawResponse response = call(url, jwt, method, json);
        return handleResponse(response, responseClass);
    }


    @Blocking
    public static @NotNull <R> ResponseContainer<R> call(@NotNull URL url, @Nullable String jwt, @NotNull Method method, @NotNull Class<R> responseClass) {
        RawResponse response = call(url, jwt, method);
        return handleResponse(response, responseClass);
    }

    @Blocking
    public static @NotNull <R> ResponseContainer<R> callPublic(@NotNull URL url, @NotNull Method method, @NotNull Class<R> responseClass) {
        return call(url, null, method, responseClass);
    }

    @Blocking
    public static @NotNull <R> ResponseContainer<R> callPublic(@NotNull URL url, @NotNull Method method, @NotNull Object body, @NotNull Class<R> responseClass) {
        return call(url, null, method, body, responseClass);
    }

    @TestOnly
    @Blocking
    public static @NotNull <R> ResponseContainer<R> get(@NotNull URL url, @NotNull Class<R> responseClass) {
        return callPublic(url, Method.GET, responseClass);
    }

}
