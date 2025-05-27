# Movie Review App – Setup Guide

This document describes the structure of the repository, core containers/modules, and detailed instructions for setting up the project locally and using Docker.

---

## 🗂️ Repository Overview

* **src/main/java/com/example/moviereview/**

  * **controller/** — REST API controllers
  * **service/** — Business logic & transactional services
  * **domain/**

    * **entity/** — JPA/Hibernate entity classes
    * **repository/** — Spring Data JPA repositories
  * **dto/** — Request/response DTOs for API payloads
  * **security/** — JWT security and utilities
  * **exception/** — Custom/global exception handlers
  * **config/** — Application and security configuration
* **src/main/resources/**

  * **application.yml** — Main configuration file

---

## 🐳 Containers / Components Overview

* **PostgreSQL Database (DB container):**

  * Stores all persistent data (users, movies, reviews, etc.).
  * Default port: **5432**

---

## 🚀 Local Setup (No Docker)

### 1. **Clone the repository**

```bash
git clone https://github.com/your-org/moviereview.git
cd moviereview
```

### 2. **Install Java 21 (or compatible)**

* [Download from Adoptium](https://adoptium.net/temurin/releases/?version=21)

### 3. **Create Database Container (using Docker Compose)**

If you prefer to run PostgreSQL in a container (recommended for quick setup), use the provided `docker-compose.yml` file in the project root:

* Make sure Docker is installed and running.
* In the project directory, start only the database container:

```bash
docker compose up -d
```

* This will spin up a PostgreSQL container using the configuration in your `docker-compose.yml` file (e.g., user, password, database).
* The database will be available at `localhost:5432`.

> **Tip:** You can view/edit credentials in the `docker-compose.yml` or `.env` file.

Alternatively, if you wish to install PostgreSQL directly on your system:

* [Download PostgreSQL](https://www.postgresql.org/download/)
* Create a database (e.g., `moviereview`)
* Create a user and set password.

### 4. **Configure Database Connection**

* Edit `src/main/resources/application.properties` or `application.yml`:

  ```
  spring.datasource.url=jdbc:postgresql://localhost:5432/moviereview
  spring.datasource.username=YOUR_DB_USER
  spring.datasource.password=YOUR_DB_PASSWORD
  ```

### 5. **Build the Project**

```bash
./gradlew clean build
```

### 6. **Run the Application**

```bash
./gradlew bootRun
# OR
java -jar build/libs/moviereview-0.0.1-SNAPSHOT.jar
```

* The API is available at: [http://localhost:8080/api](http://localhost:8080/api)

---

## 📬 Postman Collection

- Easily test the API endpoints with the provided Postman collection:
  - [Download the Postman Collection]([https://www.getpostman.com/collections/YOUR_COLLECTION_LINK](https://web.postman.co/workspace/7d631892-256e-438f-869e-080a11140ef7))

---

## 👤 Authentication

* JWT-based authentication.
* Register a user, log in, and use the provided token as a Bearer token for protected endpoints.

---

## 📫 Support

For issues or questions, open a GitHub Issue or contact the maintainer.

---
