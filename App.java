package com.example;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.time.Instant;

public class App {
    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        // Main endpoint
        server.createContext("/", (HttpExchange exchange) -> {
            long start = System.currentTimeMillis();

            String version = App.class.getPackage().getImplementationVersion();
            if (version == null) version = "dev";

            String response = "Hello from pipeline v" + version;
            byte[] bytes = response.getBytes();

            exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }

            long duration = System.currentTimeMillis() - start;

            // Simple structured log
            System.out.println("{"
                    + "\"timestamp\":\"" + Instant.now() + "\","
                    + "\"level\":\"INFO\","
                    + "\"path\":\"/\","
                    + "\"status\":" + 200 + ","
                    + "\"durationMs\":" + duration
                    + "}");
        });

        // Health endpoint for monitoring
        server.createContext("/health", (HttpExchange exchange) -> {
            String response = "OK";
            byte[] bytes = response.getBytes();

            exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }

            System.out.println("{"
                    + "\"timestamp\":\"" + Instant.now() + "\","
                    + "\"level\":\"INFO\","
                    + "\"path\":\"/health\","
                    + "\"status\":" + 200
                    + "}");
        });

        server.setExecutor(null);
        server.start();
        System.out.println("Server started at http://localhost:" + port);
    }
}
