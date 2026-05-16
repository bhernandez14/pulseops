# pulseops

PulseOps is a real-time system monitoring dashboard that tracks CPU, memory, and disk usage with live updates using WebSockets.
The application is fully containerized using Docker and Docker Compose, with a React frontend served via Nginx and a Spring Boot backend handling metrics and WebSocket communication.

## Features

- Real-time CPU, Memory, and Disk usage monitoring
- Live system health status (HEALTHY / WARN / CRITICAL)
- WebSocket-based updates (no refresh needed)
- Historical metrics graphing
- Dockerized full-stack setup

---

## Tech Stack

### Frontend

- React (Vite)
- Recharts
- SockJS + STOMP
- Nginx (production serving)

### Backend

- Java 21
- Spring Boot
- WebSockets (STOMP)
- REST API

### DevOps

- Docker
- Docker Compose

---

## Running the Project (Docker)

### 1. Clone the repository

```bash
git clone https://github.com/bhernandez14/pulseops.git
cd pulseops
```
