# CloudExtra-HusseinKhateeb

# Employee Service

A Spring Boot–based RESTful Employee Service implementing HATEOAS and communicating with an external Department service via Feign. This repository follows a **Trunk-Based Development** model for source control and supports rapid, continuous integration and deployment.

---

## Branching Strategy (Trunk-Based Development)

This project follows a **Trunk-Based Development** workflow. The `main` branch is always deployable. All feature and hotfix work occurs on short-lived branches that merge back into `main` via Pull Request (PR) once CI and review pass.

---

### Branch Naming Conventions

- **`main`**  
  - The only long-lived branch. Always contains production-ready code.  
  - Every push to `main` triggers the CI/CD pipeline.

- **Feature branches**  
  - Prefix: `feature/`  
  - Format: `feature/<ticket-ID>-<short-description>`  
    - Example: `feature/EMP-123-add-employee-search`  
  - If no ticket system is used, use a concise name:  
    - Example: `feature/add-hateoas-links`

- **Hotfix branches** (for urgent production fixes)  
  - Prefix: `hotfix/`  
  - Format: `hotfix/<short-description>`  
    - Example: `hotfix/fix-auth-nullpointer`

---

## CI/CD Pipeline

This project uses **GitHub Actions** for continuous integration and deployment.

### Trigger
- On every `push` or `pull_request` to the `main` branch.

### Pipeline Features
- Builds the project using Maven and JDK 17
- Runs unit tests
- Builds a Docker image and pushes it to AWS ECR
- Deploys the updated image to ECS by registering a new task definition and updating the ECS service

### Secrets
The following secrets must be set in the repository:
- `AWS_ACCESS_KEY_ID`
- `AWS_SECRET_ACCESS_KEY`

### AWS Services Used
- ECR (Elastic Container Registry)
- ECS (Elastic Container Service)

---

### Pull Request & Merge Policy

1. **Open a PR Early**  
   - Create a draft PR as soon as work begins to share progress and gather feedback.

2. **CI Checks Must Pass**  
   - The GitHub Actions pipeline (build → test → Docker → deploy) must succeed before merging.  
   - Any failing check blocks the PR from merging.

3. **Code Review Requirements**  
   - Assign at least one reviewer.  
   - Reviewer should verify:  
     - Correctness of code changes and tests  
     - Adherence to coding standards (e.g., logging, error handling)  
     - No sensitive data (API keys, passwords) in commits

4. **Merge Method: Squash & Merge**  
   - Use **“Squash and merge”** to keep `main`’s history linear.  
   - Squashed commit message format:  
     ```
     <ticket-ID>: <Short description>

     - Bullet-point summary of key changes
     ```
     - Example:
       ```
       EMP-123: Add employee search endpoint

       - Implemented `searchByName` in `EmployeeRepository`
       - Added `EmployeeController.searchByName()`
       - Updated unit tests for search functionality
       ```

5. **Delete Branch After Merge**  
   ```bash
   git push origin --delete feature/EMP-123-add-employee-search

---

## Table of Contents

1. [Project Overview](#project-overview)  
2. [Features & Technologies](#features--technologies)  
3. [Prerequisites](#prerequisites)  
4. [Getting Started](#getting-started)  
   - [Clone the Repository](#clone-the-repository)  
   - [Build & Run Locally](#build--run-locally)  
   - [Docker Build & Run](#docker-build--run)  
5. [API Endpoints](#api-endpoints)  
   - [List All Employees](#list-all-employees)  
   - [Create New Employee](#create-new-employee)  
   - [Get Employee by ID](#get-employee-by-id)  
   - [Get Employee by Email](#get-employee-by-email)  
   - [Update (or Upsert) Employee](#update-or-upsert-employee)  
   - [Delete Employee](#delete-employee)  
6. [Branching Strategy (Trunk-Based)](#branching-strategy-trunk-based)  
   - [Overview](#overview)  
   - [Branch Naming Conventions](#branch-naming-conventions)  
   - [Pull Request & Merge Policy](#pull-request--merge-policy)  
   - [Release Tags](#release-tags)  
7. [Contributing](#contributing)  
8. [License](#license)  

---

## Project Overview

The **Employee Service** provides a REST API for managing employee data, enriched with HATEOAS links for discoverability. It also fetches department details by making Feign-based calls to an external Department Service. Key components:

- **`EmployeeServiceImpl`** handles business logic (CRUD + Feign integration).  
- **`EmployeeModelAssembler`** converts `EmployeeDTO` objects into `EntityModel<EmployeeDTO>` with HATEOAS links.  
- **`EmployeeMapper`** maps between `Employee` entities and `EmployeeDTO` objects.  
- **Feign `DepartmentClient`** fetches department information for a given `departmentId`.  
- **Spring Data JPA** persists `Employee` entities to the database.  
- **Exception Handling** via `ResourceNotFoundException` for 404 errors.  

This service is packaged as a Docker container and can be deployed via CI/CD (GitHub Actions → AWS ECR → ECS Fargate).

---

## Features & Technologies

- **Spring Boot (Java 17)**  
- **Spring Data JPA** (Repository: `EmployeeRepository`)  
- **Spring HATEOAS** (Assembler: `EmployeeModelAssembler`)  
- **Feign Client** (`DepartmentClient`) to query Department Service  
- **DTO Layer** (`EmployeeDTO` ↔ `Employee` via `EmployeeMapper`)  
- **Exception Handling** (`ResourceNotFoundException` → HTTP 404)  
- **Logging** with SLF4J/Logback  
- **Dockerfile** for containerization  
- **Trunk-Based Development** Git workflow  

Dependencies (in `pom.xml` or `build.gradle`):
- `spring-boot-starter-web`  
- `spring-boot-starter-data-jpa`  
- `spring-boot-starter-hateoas`  
- `spring-cloud-starter-openfeign`  
- Database driver (H2 by default, configurable to PostgreSQL/MySQL)  
- Lombok (optional)  

---

## Prerequisites

- **Java 17+**  
- **Maven 3.6+** (or Gradle)  
- **Docker Engine** (to build/run container)  
- **Git** (source control)  
- **Access to Department Service** endpoint (for Feign)  


