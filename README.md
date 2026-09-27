# API Testing Framework — ReqRes Users API

A Java-based REST API test automation framework built with **RestAssured**, **TestNG**, and **Maven**, covering full CRUD operations against the public [ReqRes](https://reqres.in) API. The project includes data-driven testing, negative test scenarios, a fully automated CI/CD pipeline via GitHub Actions, and rich HTML reporting with Allure.

This project was built incrementally, test by test, as a hands-on learning exercise in professional API test automation practices.

---

## Table of Contents

- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Project Structure](#project-structure)
- [Setup Instructions](#setup-instructions)
- [Configuring the API Key](#configuring-the-api-key)
- [Running the Tests](#running-the-tests)
- [Test Coverage](#test-coverage)
- [CI/CD Pipeline](#cicd-pipeline)
- [Allure Reporting](#allure-reporting)
- [Design Decisions](#design-decisions)
- [Known Limitations](#known-limitations)

---

## Overview

This framework tests the following operations against ReqRes's `/api/users` endpoint:

| Operation | HTTP Method | Expected Status |
|---|---|---|
| Get a single user | `GET` | `200 OK` |
| Get a non-existent user | `GET` | `404 Not Found` |
| Create a new user | `POST` | `201 Created` |
| Update an existing user | `PUT` | `200 OK` |
| Delete a user | `DELETE` | `204 No Content` |

Tests are organized by resource operation (one class per HTTP verb), share a common request configuration through inheritance, and include both positive and negative test scenarios.

---

## Tech Stack

| Tool | Purpose |
|---|---|
| **Java 21** | Programming language |
| **Maven** | Build tool and dependency management |
| **RestAssured 5.4.0** | HTTP client for API testing |
| **TestNG 7.9.0** | Test execution framework (assertions, data providers, lifecycle hooks) |
| **Hamcrest** | Assertion matchers (bundled with RestAssured) |
| **Allure 2.27.0** | Test reporting and visualization |
| **GitHub Actions** | CI/CD — automated test execution on every push |

---

## Prerequisites

Before running this project, make sure you have the following installed:

- **JDK 21** — [Download here](https://www.oracle.com/java/technologies/downloads/)
- **Maven** (bundled with IntelliJ IDEA, or [install standalone](https://maven.apache.org/install.html))
- **IntelliJ IDEA** (Community or Ultimate) — recommended IDE
- **Git** — for version control
- A **ReqRes API key** — sign up for free at [app.reqres.in](https://app.reqres.in) to get your personal key

---

## Project Structure

```
ApiTesting/
├── .github/
│   └── workflows/
│       └── ci.yml                  # GitHub Actions CI/CD pipeline
├── src/
│   └── test/
│       ├── java/
│       │   ├── base/
│       │   │   └── BaseTest.java           # Shared request setup (inherited by all test classes)
│       │   ├── config/
│       │   │   └── ConfigManager.java      # Reads the API key from config.properties
│       │   └── tests/
│       │       ├── GetUsersTests.java      # GET tests (positive + 404 negative test)
│       │       ├── CreateUserTests.java    # POST tests (data-driven with 3 datasets)
│       │       ├── UpdateUserTests.java    # PUT test
│       │       └── DeleteUserTests.java    # DELETE test
│       └── resources/
│           ├── config.properties           # Contains the real API key — NOT committed to git
│           ├── config.properties.example   # Template showing the expected format
│           └── allure.properties           # Tells Allure where to store test results
├── .gitignore                      # Excludes config.properties and target/ from version control
├── pom.xml                         # Maven project configuration and dependencies
└── README.md
```

---

## Setup Instructions

### 1. Clone the repository

```bash
git clone <your-repository-url>
cd ApiTesting
```

### 2. Open the project in IntelliJ IDEA

- `File` → `Open` → select the project folder
- Wait for IntelliJ to detect `pom.xml` and prompt to load the Maven project
- Click **Load Maven Project** (or the refresh icon in the Maven tool window) to download all dependencies

### 3. Verify Java 21 is configured

- `File` → `Project Structure` → confirm the Project SDK is set to Java 21

---

## Configuring the API Key

This project requires a personal ReqRes API key. **The key is never committed to version control** — it's kept in a local, git-ignored file.

### For local development:

1. Sign up for a free key at [app.reqres.in](https://app.reqres.in)
2. Create a file at `src/test/resources/config.properties` (this file is git-ignored)
3. Add your key in the following format:

```properties
api.key=YOUR_ACTUAL_API_KEY_HERE
```

A template is provided at `src/test/resources/config.properties.example` showing the expected format without exposing a real key.

### For CI/CD (GitHub Actions):

The key is stored as a **GitHub Secret** named `API_KEY` and injected into the environment at runtime — see [CI/CD Pipeline](#cicd-pipeline) below for details.

---

## Running the Tests

### Option 1 — Run all tests via Maven (command line / IntelliJ Maven panel)

```bash
mvn clean test
```

### Option 2 — Run a single test class in IntelliJ

Right-click any test class (e.g. `GetUsersTests`) → **Run**

### Option 3 — Run a single test method

Click the green ▶️ arrow next to any `@Test` method in the editor gutter.

---

## Test Coverage

**7 automated test cases** across 4 test classes:

| Class | Tests | What it covers |
|---|---|---|
| `GetUsersTests` | 2 | Fetching an existing user (200) + fetching a non-existent user (404) |
| `CreateUserTests` | 3 (data-driven) | Creating users with 3 different name/job combinations (201) |
| `UpdateUserTests` | 1 | Updating an existing user's data (200) |
| `DeleteUserTests` | 1 | Deleting a user (204) |

### Key testing practices demonstrated:

- **Response body validation** using JSON path assertions (`data.id`, `data.email`, etc.), not just status codes
- **Data-driven testing** via TestNG `@DataProvider` — the same test logic runs against multiple input sets without duplicating code
- **Negative testing** — verifying the API correctly rejects invalid requests (e.g. non-existent user IDs)
- **Fresh request specifications per test** — each test builds its own isolated request spec (via `baseSpec()` in `BaseTest`) to avoid shared mutable state between tests, which previously caused intermittent test failures when run in a shared classpath
- **Externalized configuration** — the API key lives outside the codebase and is injected via a properties file (local) or a secret (CI)

---

## CI/CD Pipeline

Every push and pull request to `main`/`master` automatically triggers the test suite via **GitHub Actions**. The workflow is defined in [`.github/workflows/ci.yml`](.github/workflows/ci.yml):

1. **Checkout** — pulls the latest code
2. **Set up JDK 21** — installs the required Java version on the runner
3. **Create `config.properties` from a GitHub Secret** — the API key is injected at runtime from a repository secret named `API_KEY`, so it's never exposed in the codebase or logs
4. **Run tests** — executes `mvn clean test`

### Setting up the secret (for anyone forking this repo):

1. Go to the repository's **Settings** → **Secrets and variables** → **Actions**
2. Click **New repository secret**
3. Name: `API_KEY`
4. Value: your ReqRes API key
5. Save

Without this secret configured, the CI pipeline will fail at the test-execution step.

---

## Allure Reporting

This project integrates [Allure](https://allurereport.org/) for rich, visual test reports (pass/fail breakdown, suite organization, execution trends).

### Generate and view a report locally:

```bash
mvn clean test
mvn allure:serve
```

`allure:serve` builds the report and opens it automatically in your default browser.

### How it works

- The `AllureTestNg` listener (registered via `@Listeners(AllureTestNg.class)` in `BaseTest`) captures the result of every test run
- Results are written to `target/allure-results`, as configured in `src/test/resources/allure.properties`
- `mvn allure:serve` renders those raw results into an interactive HTML report

---

## Design Decisions

A few notes on choices made during development, for anyone reviewing the code:

- **`baseSpec()` returns a fresh `RequestSpecification` on every call**, rather than reusing a single static/shared spec set up once in a `@BeforeClass` hook. An earlier version shared one mutable spec across all tests, which caused test pollution — a test that modified a header (e.g. testing a missing API key) would corrupt the spec for tests that ran afterward, especially when running the full suite versus a single test in isolation. Building a fresh spec per test eliminates this class of bug entirely.
- **One test class per HTTP verb/resource action** (`GetUsersTests`, `CreateUserTests`, etc.) rather than one large test class, for readability and easier navigation as the suite grows.
- **The API key is never hardcoded** — it's read from a properties file locally and from a GitHub Secret in CI, following standard secret-management practice.

---

## Known Limitations

- A negative test verifying `403 Forbidden` when the API key is missing was **intentionally removed**. ReqRes's legacy `/api/users` endpoint has inconsistent enforcement of the API key requirement (confirmed via response metadata indicating A/B-tested variants on their end), making that specific scenario non-deterministic and unreliable to assert against a live external service outside this project's control.
- ReqRes enforces a rate limit of **20 requests/minute per IP** on the legacy endpoint. Running the full suite repeatedly in quick succession (e.g. during active debugging) may occasionally produce unrelated `403` responses due to this limit — this is expected behavior from the third-party service, not a defect in the test code.

---

## Author's Note

This framework was built step by step as a learning project, with deliberate emphasis on understanding *why* each piece of configuration and code exists — not just copying a working template. Bugs encountered along the way (shared mutable state, CI secret injection, YAML indentation, Allure results-directory configuration) were diagnosed and fixed as part of the learning process, and are documented above where relevant.