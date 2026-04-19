package com.devops.helloservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Main REST Controller for our DevOps demo application.
 *
 * WEB UI:
 *   GET /           — Dashboard (HTML page served from static/index.html)
 *
 * REST API Endpoints:
 *   GET /api/status — App status JSON
 *   GET /health     — Health check (for Kubernetes probes)
 *   GET /greet      — Greeting with name parameter
 *   GET /info       — Pod/container info (hostname, IP, memory)
 */
@RestController
public class HelloController {

    @Value("${app.version:1.0.0}")
    private String appVersion;

    @Value("${app.environment:local}")
    private String environment;

    // ==================== API STATUS (JSON) ====================
    // The web dashboard (index.html) is served automatically by Spring from /static
    // This endpoint provides JSON status for API consumers / CI pipelines
    @GetMapping("/api/status")
    public Map<String, String> status() {
        Map<String, String> response = new LinkedHashMap<>();
        response.put("message", "Hello from DevOps Pipeline!");
        response.put("version", appVersion);
        response.put("environment", environment);
        response.put("status", "running");
        response.put("timestamp", LocalDateTime.now().toString());
        return response;
    }

    // ==================== HEALTH ====================
    @GetMapping("/health")
    public Map<String, String> health() {
        Map<String, String> response = new LinkedHashMap<>();
        response.put("status", "UP");
        response.put("version", appVersion);
        return response;
    }

    // ==================== GREET ====================
    @GetMapping("/greet")
    public Map<String, String> greet(@RequestParam(defaultValue = "World") String name) {
        Map<String, String> response = new LinkedHashMap<>();
        response.put("message", "Hello, " + name + "!");
        response.put("timestamp", LocalDateTime.now().toString());
        return response;
    }

    // ==================== INFO (Useful in Kubernetes) ====================
    @GetMapping("/info")
    public Map<String, String> info() {
        Map<String, String> response = new LinkedHashMap<>();
        response.put("app", "hello-service");
        response.put("version", appVersion);
        response.put("environment", environment);
        response.put("java_version", System.getProperty("java.version"));

        // Show hostname — in Kubernetes this will be the Pod name
        try {
            response.put("hostname", InetAddress.getLocalHost().getHostName());
            response.put("ip_address", InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            response.put("hostname", "unknown");
        }

        response.put("os", System.getProperty("os.name") + " " + System.getProperty("os.arch"));
        response.put("total_memory_mb", String.valueOf(Runtime.getRuntime().totalMemory() / 1024 / 1024));
        response.put("free_memory_mb", String.valueOf(Runtime.getRuntime().freeMemory() / 1024 / 1024));
        return response;
    }
}
