package net.juligames.tresor.rest;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

/**
 * @author Ture Bentzin
 * @since 22-02-2025
 */
public class RestCallerTest {

    private static final @NotNull Logger log = LoggerFactory.getLogger(RestCallerTest.class);


    public record ExampleResponse(int userId, int id, String title, String body) {
    }

    public record ExampleRequest(String title, String body, int userId) {
    }

    public @NotNull Gson gson = new Gson();

    //before all check internet connection via ping

    @BeforeAll
    static void checkInternetConnection() throws MalformedURLException {
        URL url = URI.create("https://jsonplaceholder.typicode.com/").toURL();
        boolean reachable = false;

        try {
            reachable = Inet4Address.getByName(url.getHost()).isReachable(1000);
        } catch (IOException e) {
            //SKIP tests - dont fail
        }

        assumeTrue(reachable, "No internet connection");

    }

    @Test
    void get() throws MalformedURLException {
        String urlString = "https://jsonplaceholder.typicode.com/posts/1";
        URL url = URI.create(urlString).toURL();
        log.info("URL: {}", url);
        String response = RESTCaller.call(url, null, RESTCaller.Method.GET, "");
        log.info("Response: {}", response);
        assertNotNull(response);
        assertFalse(response.isEmpty());
        ExampleResponse exampleResponse = gson.fromJson(response, ExampleResponse.class);
        assertNotNull(exampleResponse);
        assertEquals(1, exampleResponse.userId());
        assertEquals(1, exampleResponse.id());
        assertEquals("sunt aut facere repellat provident occaecati excepturi optio reprehenderit", exampleResponse.title());
        assertEquals("quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto", exampleResponse.body());
    }

    @Test
    void post() throws MalformedURLException {
        String urlString = "https://jsonplaceholder.typicode.com/posts";
        URL url = URI.create(urlString).toURL();
        log.info("URL: {}", url);
        ExampleRequest exampleRequest = new ExampleRequest("foo", "bar", 1);
        String response = RESTCaller.call(url, null, RESTCaller.Method.POST, gson.toJson(exampleRequest));
        log.info("Response: {}", response);
        assertNotNull(response);
        assertFalse(response.isEmpty());
        ExampleResponse exampleResponse = gson.fromJson(response, ExampleResponse.class);
        assertNotNull(exampleResponse);
        assertEquals(1, exampleResponse.userId());
        assertEquals(101, exampleResponse.id());
        assertEquals("foo", exampleResponse.title());
        assertEquals("bar", exampleResponse.body());
    }

    @Test
    void put() throws MalformedURLException {
        String urlString = "https://jsonplaceholder.typicode.com/posts/1";
        URL url = URI.create(urlString).toURL();
        log.info("URL: {}", url);
        ExampleRequest exampleRequest = new ExampleRequest("foo", "bar", 1);
        String response = RESTCaller.call(url, null, RESTCaller.Method.PUT, gson.toJson(exampleRequest));
        log.info("Response: {}", response);
        assertNotNull(response);
        assertFalse(response.isEmpty());
        ExampleResponse exampleResponse = gson.fromJson(response, ExampleResponse.class);
        assertNotNull(exampleResponse);
        assertEquals(1, exampleResponse.userId());
        assertEquals(1, exampleResponse.id());
        assertEquals("foo", exampleResponse.title());
        assertEquals("bar", exampleResponse.body());
    }

    @Test
    void delete() throws MalformedURLException {
        String urlString = "https://jsonplaceholder.typicode.com/posts/1";
        URL url = URI.create(urlString).toURL();
        log.info("URL: {}", url);
        String response = RESTCaller.call(url, null, RESTCaller.Method.DELETE, "");
        log.info("Response: {}", response);
        assertNotNull(response);
        assertEquals("{}", response);
    }

    @Test
    void getWithHeaders() throws MalformedURLException {
        String urlString = "https://jsonplaceholder.typicode.com/posts/1";
        URL url = URI.create(urlString).toURL();
        log.info("URL: {}", url);
        String response = RESTCaller.call(url, "eyJhbGciOiJIUzI1NiJ9.eyJzY29wZSI6IlRlc3RpbmcifQ.GSUxiQ-EYtewCn6270V1-F1DWTF0Orfg2-S3Dt9S3xA", RESTCaller.Method.GET, "");
        log.info("Response: {}", response);
        assertNotNull(response);
        assertFalse(response.isEmpty());
        ExampleResponse exampleResponse = gson.fromJson(response, ExampleResponse.class);
        assertNotNull(exampleResponse);
        assertEquals(1, exampleResponse.userId());
        assertEquals(1, exampleResponse.id());

        assertEquals("sunt aut facere repellat provident occaecati excepturi optio reprehenderit", exampleResponse.title());
        assertEquals("quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto", exampleResponse.body());
    }

    //other paths

    @Test
    void getWithGson() throws MalformedURLException {
        String urlString = "https://jsonplaceholder.typicode.com/posts/1";
        URL url = URI.create(urlString).toURL();

        ExampleResponse exampleResponse = RESTCaller.call(url, null, RESTCaller.Method.GET, ExampleResponse.class);
        assertNotNull(exampleResponse);
        assertEquals(1, exampleResponse.userId());
        assertEquals(1, exampleResponse.id());
        assertEquals("sunt aut facere repellat provident occaecati excepturi optio reprehenderit", exampleResponse.title());
        assertEquals("quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto", exampleResponse.body());
    }
}
