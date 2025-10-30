# 💸 Payment Transfer Service

A Spring Boot REST API that simulates a simple **payment transfer system** between user accounts.  
It supports deposits, withdrawals and money transfers between accounts — all backed by **PostgreSQL** and **Liquibase** for database migrations.

## 🚀 Tech stack

| Layer | Technology |
|-------|-------------|
| Backend | [Spring Boot 3.5.7](https://spring.io/projects/spring-boot) |
| Language | Java 21 |
| Database | PostgreSQL 14 |
| Migrations | [Liquibase 4.23.2](https://www.liquibase.org/) |
| Build Tool | Maven |
| ORM | Spring Data JPA |
| Validation | Jakarta Validation |
| Containerization | Docker & Docker Compose |
| Testing | JUnit 5 + Mockito |
| Utilities | Lombok |

## 📂 Project structure
```
├───.idea
├───.mvn
│   └───wrapper
├───src
│   ├───main
│   │   ├───java
│   │   │   └───com
│   │   │       └───bojanlukic
│   │   │           └───test
│   │   │               ├───controller
│   │   │               │   ├───dto
│   │   │               │   └───mapper
│   │   │               ├───entity
│   │   │               ├───repository
│   │   │               └───service
│   │   │                   └───exception
│   │   └───resources
│   │       ├───db
│   │       │   └───changelog
│   │       ├───static
│   │       └───templates
│   └───test
│       └───java
│           └───com
│               └───bojanlukic
│                   └───test
│                       └───service
└───target
    ├───classes
    │   ├───com
    │   │   └───bojanlukic
    │   │       └───test
    │   │           ├───controller
    │   │           │   ├───dto
    │   │           │   └───mapper
    │   │           ├───entity
    │   │           ├───repository
    │   │           └───service
    │   │               └───exception
    │   └───db
    │       └───changelog
    ├───generated-sources
    │   └───annotations
    ├───generated-test-sources
    │   └───test-annotations
    ├───maven-archiver
    ├───maven-status
    │   └───maven-compiler-plugin
    │       ├───compile
    │       │   └───default-compile
    │       └───testCompile
    │           └───default-testCompile
    └───test-classes
        └───com
            └───bojanlukic
                └───test
                    └───service
```
## 🧰 Prerequisites

Before you start, make sure you have installed:
- **Java 21+**
- **Maven 3.9+**
- **Docker & Docker Compose**
- (Optional) **Postman** for testing API endpoints

## ⚙️ Local development setup

### 1️⃣ Clone the repository
https://github.com/Bojan-Lukic-994/PaymentService.git

### 2️⃣ Start PostgreSQL
If not using Docker, run PostgreSQL server locally and connect with settings:
- spring.datasource.url=jdbc:postgresql://localhost:5432/testdb
- spring.datasource.username=postgres
- spring.datasource.password=test

### 3️⃣ Run the application
./mvnw spring-boot:run

The app will start on http://localhost:8080

## 🐳 Run with Docker
Build & start containers
- docker-compose up --build

This will:
- Launch a PostgreSQL database (postgres-db)
- Build and run the Spring Boot app (payment-transfer-service)
- Automatically apply Liquibase migrations and seed initial data

Stop containers
- docker-compose down

## 🗄️ Database & Liquibase

Liquibase runs automatically on startup and applies the changelogs from
- src/main/resources/db/changelog/db.changelog-master.xml.

It creates two tables:
- account
- transactions

And seeds initial data:
| Owner | Amount |
|-------|--------|
| Boki | 1000 |
| John | 55887 |
| Tina | 3364 |
| Tom | 21500 |

## 🔌 API Endpoints
🧾 Accounts
Method endpoint	description
- GET	/account/{id}	- Get account details by ID
- POST	/account/{id}/add?amount={amount}	- Add funds to account
- POST	/account/{id}/withdraw?amount={amount}	- Withdraw funds from account

💸 Transactions
Method endpoint	description
- GET	/transaction/all -	Get all transactions
- GET	/transaction/{accountId} -	Get all transactions for an account
- POST	/transaction -	Execute a transfer between accounts

## 🧪 Running tests
This project includes JUnit 5 unit tests for AccountService and TransactionService.

## 🧾 Error handling
All exceptions are globally handled via GlobalExceptionHandler:

- AccountNotFoundException	- 404	Account not found
- WithdrawalNotSupportedException	- 406	Insufficient balance
- TransactionNotExecutedException	- 409	Transfer failed
- Validation errors	- 400	Invalid input fields

👨‍💻 Author

Bojan Lukic
📧 boka.lukic.994@gmail.com
