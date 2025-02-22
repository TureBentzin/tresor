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
    public static final @NotNull String RESPONSE_ELEMENT_BODY = "quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto";
    public static final @NotNull String RESPONSE_ELEMENT_TITLE = "sunt aut facere repellat provident occaecati excepturi optio reprehenderit";


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
        @NotNull ResponseContainer<ExampleResponse> response = RESTCaller.call(url, null, RESTCaller.Method.GET, "", ExampleResponse.class);
        log.info("Response: {}", response);
        assertNotNull(response);

        if (response.isSuccessful()) {
            ExampleResponse exampleResponse = response.getResponse();
            assertNotNull(exampleResponse);
            assertEquals(1, exampleResponse.userId());
            assertEquals(1, exampleResponse.id());
            assertEquals(RESPONSE_ELEMENT_TITLE, exampleResponse.title());
            assertEquals(RESPONSE_ELEMENT_BODY, exampleResponse.body());
        } else {
            fail("Response was not successful");
        }
    }

    @Test
    void post() throws MalformedURLException {
        String urlString = "https://jsonplaceholder.typicode.com/posts";
        URL url = URI.create(urlString).toURL();
        log.info("URL: {}", url);
        ExampleRequest exampleRequest = new ExampleRequest("foo", "bar", 1);
        ResponseContainer<ExampleResponse> response = RESTCaller.call(url, null, RESTCaller.Method.POST, exampleRequest, ExampleResponse.class);
        log.info("Response: {}", response);
        assertNotNull(response);

        if (response.isSuccessful()) {
            ExampleResponse exampleResponse = response.getResponse();
            assertNotNull(exampleResponse);
            assertEquals(1, exampleResponse.userId());
            assertEquals(101, exampleResponse.id());
            assertEquals("foo", exampleResponse.title());
            assertEquals("bar", exampleResponse.body());
        } else {
            fail("Response was not successful");
        }
    }

    @Test
    void put() throws MalformedURLException {
        String urlString = "https://jsonplaceholder.typicode.com/posts/1";
        URL url = URI.create(urlString).toURL();
        log.info("URL: {}", url);
        ExampleRequest exampleRequest = new ExampleRequest("foo", "bar", 1);

        ResponseContainer<ExampleResponse> response = RESTCaller.call(url, null, RESTCaller.Method.PUT, exampleRequest, ExampleResponse.class);

        log.info("Response: {}", response);
        assertNotNull(response);

        if (response.isSuccessful()) {
            ExampleResponse exampleResponse = response.getResponse();
            assertNotNull(exampleResponse);
            assertEquals(1, exampleResponse.userId());
            assertEquals(1, exampleResponse.id());
            assertEquals("foo", exampleResponse.title());
            assertEquals("bar", exampleResponse.body());
        } else {
            fail("Response was not successful");
        }
    }

    @Test
    void delete() throws MalformedURLException {
        String urlString = "https://jsonplaceholder.typicode.com/posts/1";
        URL url = URI.create(urlString).toURL();
        log.info("URL: {}", url);

        ResponseContainer<EmptyBody> response = RESTCaller.call(url, null, RESTCaller.Method.DELETE, "", EmptyBody.class);
        log.info("Response: {}", response);
        assertNotNull(response);
        assertTrue(response.isSuccessful());


    }

    @Test
    void getWithHeaders() throws MalformedURLException {
        String urlString = "https://jsonplaceholder.typicode.com/posts/1";
        URL url = URI.create(urlString).toURL();
        log.info("URL: {}", url);

        ResponseContainer<ExampleResponse> response = RESTCaller.call(url, null, RESTCaller.Method.GET, "", ExampleResponse.class);
        log.info("Response: {}", response);
        assertNotNull(response);

        if (response.isSuccessful()) {
            ExampleResponse exampleResponse = response.getResponse();
            assertNotNull(exampleResponse);
            assertEquals(1, exampleResponse.userId());
            assertEquals(1, exampleResponse.id());
            assertEquals(RESPONSE_ELEMENT_TITLE, exampleResponse.title());
            assertEquals(RESPONSE_ELEMENT_BODY, exampleResponse.body());
        } else {
            fail("Response was not successful");
        }
    }
}
