# Gatling Performance Testing Playground

Welcome to the Gatling Performance Testing Playground!  
This project is designed to help you learn, explore, and practice performance testing using Gatling with Java.

---

## Overview

This playground simulates realistic API workflows using two popular public APIs:

- **ReqRes API** (https://reqres.in) — For user authentication and user-related API calls.
- **JSONPlaceholder API** (https://jsonplaceholder.typicode.com) — For full CRUD operations on posts.

The project demonstrates key Gatling features including:

- Scenario building with realistic API flows
- Usage of feeders with JSON data
- Different user injection profiles (ramp-up, constant concurrency, at-once)
- Assertions for response time and failure rates
- Generating detailed HTML reports

---

## Project Structure

| Directory / Class          | Purpose                                                      |
|---------------------------|--------------------------------------------------------------|
| `api`                     | Contains reusable API call chains (login, getPosts, etc.)     |
| `commons`                 | Common utilities like JSON data feeders                        |
| `protocol`                | HTTP protocol configuration for different APIs               |
| `scenarios`               | Gatling scenarios combining API calls with feeders and pauses |
| `simulations`             | Entry points defining user load and injection profiles         |
| `data/`                   | JSON files containing sample data for feeders                  |

---

## Running the Tests

To run a specific simulation, use Maven commands like this:

```bash
mvn gatling:test -Dgatling.simulationClass=simulations.JsonPlaceholderSimulation
