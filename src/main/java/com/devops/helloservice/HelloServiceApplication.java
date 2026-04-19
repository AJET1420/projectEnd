package com.devops.helloservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Hello Service application.
 * 
 * @SpringBootApplication combines:
 *   @Configuration    — marks this as a config class
 *   @EnableAutoConfiguration — auto-configures Spring based on dependencies
 *   @ComponentScan    — scans this package for @Controller, @Service, etc.
 */
@SpringBootApplication
public class HelloServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelloServiceApplication.class, args);
    }
}
