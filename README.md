# Hello Service — DevOps Pipeline Demo

A Spring Boot REST API used for learning end-to-end DevOps deployment.

## Tech Stack
- Java 17 + Spring Boot 3.2
- Maven (build)
- Docker (containerization)
- Kubernetes/EKS (orchestration)
- Prometheus + Grafana (monitoring)

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/` | App info (version, status) |
| GET | `/health` | Health check |
| GET | `/greet?name=X` | Greeting message |
| GET | `/info` | Pod/container details |
| GET | `/actuator/health` | Spring Boot health |
| GET | `/actuator/prometheus` | Prometheus metrics |

## Quick Start

### Run with Maven
```bash
mvn clean package
java -jar target/hello-service.jar
```

### Run with Docker
```bash
docker build -t hello-service:1.0.0 .
docker run -d -p 8080:8080 hello-service:1.0.0
```

### Run with Docker Compose
```bash
docker-compose up -d --build
```

### Test
```bash
curl http://localhost:8080/
curl http://localhost:8080/health
curl http://localhost:8080/greet?name=DevOps
curl http://localhost:8080/info
curl http://localhost:8080/actuator/prometheus
```
