package com.apppple.demo.java11;

import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * @author fanhui.mengfh on 2021/5/20
 */
public class HttpClientTest {

    String url = "http://openjdk.java.net/";

    @Test
    public void testSendAsync() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenAccept(System.out::println)
            .join();
    }

    @Test
    public void testSend_get() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .uri(URI.create(url))
            .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Response" + response.body());
    }

    @Test
    public void put_ofString() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
            .PUT(HttpRequest.BodyPublishers.ofString("test")).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Response" + response.body());
    }

    @Test
    public void post_discarding() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String newUserString = "{\"name\": \"Mary\",  \"job\": \"leader\"}";
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
            .POST(HttpRequest.BodyPublishers.ofString(newUserString)).build();
        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        System.out.println("Response" + response.statusCode());
    }

    @Test
    public void delete_ofString() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).DELETE().build();
        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        System.out.println("Response" + response.statusCode());
    }

}
