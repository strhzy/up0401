package com.strhzy.finalpr.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpService {

    private static final String BASE_URL = "http://localhost:8090/api";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    private static String jwtToken;

    private HttpService() {}

    private static String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка сериализации JSON", e);
        }
    }

    private static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка десериализации JSON", e);
        }
    }

    private static String getSessionToken() {
        try {
            ServletRequestAttributes attrs =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs == null) return null;
            HttpServletRequest request = attrs.getRequest();
            HttpSession session = request.getSession(false);
            if (session == null) return null;
            Object tokenObj = session.getAttribute("jwt");
            if (tokenObj instanceof String token && !token.isBlank()) return token;
        } catch (Exception ignored) {}
        return null;
    }

    public static <T> T get(String path, Class<T> responseType) throws IOException, InterruptedException {
        String token = getSessionToken();
        if (token == null) token = jwtToken;

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path));

        if (token != null && !token.isBlank()) {
            builder.header("Authorization", "Bearer " + token);
        }

        HttpRequest request = builder.GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return fromJson(response.body(), responseType);
    }

    public static <T, R> R post(String path, T body, Class<R> responseType) throws IOException, InterruptedException {
        String token = getSessionToken();
        if (token == null) token = jwtToken;

        String jsonBody = toJson(body);
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("Content-Type", "application/json");

        if (token != null && !token.isBlank()) {
            builder.header("Authorization", "Bearer " + token);
        }

        HttpRequest request = builder.POST(HttpRequest.BodyPublishers.ofString(jsonBody)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return fromJson(response.body(), responseType);
    }

    public static <T, R> R put(String path, T body, Class<R> responseType) throws IOException, InterruptedException {
        String token = getSessionToken();
        if (token == null) token = jwtToken;

        String jsonBody = toJson(body);
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("Content-Type", "application/json");

        if (token != null && !token.isBlank()) {
            builder.header("Authorization", "Bearer " + token);
        }

        HttpRequest request = builder.PUT(HttpRequest.BodyPublishers.ofString(jsonBody)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return fromJson(response.body(), responseType);
    }

    public static <T, R> R patch(String path, T body, Class<R> responseType) throws IOException, InterruptedException {
        String token = getSessionToken();
        if (token == null) token = jwtToken;

        String jsonBody = toJson(body);
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("Content-Type", "application/json");

        if (token != null && !token.isBlank()) {
            builder.header("Authorization", "Bearer " + token);
        }

        HttpRequest request = builder.method("PATCH", HttpRequest.BodyPublishers.ofString(jsonBody)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return fromJson(response.body(), responseType);
    }

    public static <R> R delete(String path, Class<R> responseType) throws IOException, InterruptedException {
        String token = getSessionToken();
        if (token == null) token = jwtToken;

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path));

        if (token != null && !token.isBlank()) {
            builder.header("Authorization", "Bearer " + token);
        }

        HttpRequest request = builder.DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return fromJson(response.body(), responseType);
    }

    public static void setToken(String token) {
        jwtToken = token;
    }

    public static void clearToken() {
        jwtToken = null;
    }

    public static String getToken() {
        return jwtToken;
    }

    public static boolean isAuthenticated() {
        String token = getSessionToken();
        if (token == null) token = jwtToken;
        return token != null && !token.isBlank();
    }
}
