package com.devops.helloservice.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

/**
 * Unit tests for HelloController.
 * 
 * Uses MockMvc — tests the controller WITHOUT starting a real HTTP server.
 * This is faster and sufficient for controller-level testing.
 */
@SpringBootTest
@AutoConfigureMockMvc
class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // ==================== WEB DASHBOARD ====================

    @Test
    @DisplayName("GET /index.html should return the HTML dashboard page")
    void shouldReturnDashboardPage() throws Exception {
        mockMvc.perform(get("/index.html"))
                .andExpect(status().isOk());
    }

    // ==================== API STATUS ENDPOINT ====================

    @Test
    @DisplayName("GET /api/status should return JSON with status running")
    void shouldReturnApiStatus() throws Exception {
        mockMvc.perform(get("/api/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Hello from DevOps Pipeline!"))
                .andExpect(jsonPath("$.status").value("running"))
                .andExpect(jsonPath("$.version").exists())
                .andExpect(jsonPath("$.timestamp").exists());
    }

    // ==================== HEALTH ENDPOINT ====================

    @Test
    @DisplayName("GET /health should return status UP")
    void shouldReturnHealthStatusUp() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    // ==================== GREET ENDPOINT ====================

    @Test
    @DisplayName("GET /greet should return default greeting (World)")
    void shouldGreetWithDefaultName() throws Exception {
        mockMvc.perform(get("/greet"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Hello, World!"));
    }

    @Test
    @DisplayName("GET /greet?name=DevOps should return personalized greeting")
    void shouldGreetWithCustomName() throws Exception {
        mockMvc.perform(get("/greet?name=DevOps"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Hello, DevOps!"));
    }

    @Test
    @DisplayName("GET /greet?name=Kubernetes should return Kubernetes greeting")
    void shouldGreetKubernetes() throws Exception {
        mockMvc.perform(get("/greet?name=Kubernetes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Hello, Kubernetes!"));
    }

    // ==================== INFO ENDPOINT ====================

    @Test
    @DisplayName("GET /info should return application details")
    void shouldReturnAppInfo() throws Exception {
        mockMvc.perform(get("/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.app").value("hello-service"))
                .andExpect(jsonPath("$.version").exists())
                .andExpect(jsonPath("$.java_version").exists())
                .andExpect(jsonPath("$.hostname").exists())
                .andExpect(jsonPath("$.total_memory_mb").exists());
    }

    // ==================== ACTUATOR ENDPOINTS ====================

    @Test
    @DisplayName("GET /actuator/health should return Spring Boot health check")
    void shouldReturnActuatorHealth() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }
}
