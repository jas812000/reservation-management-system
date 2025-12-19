# Reservation Management System

## Overview
The Reservation Management System is a modular Java backend designed to manage
customer accounts and lodging reservations across multiple property types,
including hotels, cabins, and houses.

The project focuses on backend domain modeling, validation, persistence,
and automated testing rather than UI concerns. All data is persisted using
a structured file-based storage model instead of a database, allowing the
system to simulate real backend responsibilities while remaining portable
and easy to test.

---

## Features
- Account creation, retrieval, and modification
- Reservation lifecycle management (draft → completed / cancelled)
- Support for multiple lodging types using inheritance-based specialization
- Per-night and total cost calculations with type-specific pricing logic
- Structured file-based persistence with deterministic loading and saving
- Centralized orchestration via a manager/controller layer
- Robust validation and domain-specific exception handling

---

## Architecture Overview
The system follows a layered, object-oriented architecture:

- **Manager**
  - Central controller coordinating accounts, reservations, validation,
    and persistence.

- **Account**
  - Represents customer records and maintains associations to reservations.

- **Reservation (Abstract Base Class)**
  - Encapsulates shared reservation attributes and behavior.

- **Concrete Reservation Types**
  - `HotelReservation`
  - `CabinReservation`
  - `HouseReservation`

- **Persistence Layer**
  - Structured text files per account and per reservation.

- **Exception Framework**
  - Custom exception hierarchy enforcing parameter validation,
    state transitions, and I/O correctness.

---

## Data Persistence Model
- One directory per account
- One account file per customer
- One reservation file per reservation
- XML-like structured text format for readability and future migration

This approach simulates backend persistence while keeping storage logic
explicit, deterministic, and testable.

---

## Reservation State Management
Reservations follow a controlled lifecycle:

- `DRAFT → COMPLETED`
- `DRAFT → CANCELLED`

Invalid state transitions are explicitly blocked through domain logic
and custom exceptions to preserve system consistency.

---

## Error Handling Strategy
The system uses a comprehensive custom exception hierarchy, including:
- Invalid parameter handling
- Duplicate object detection
- Illegal state transitions
- Missing account or reservation access
- Load/save failures during file I/O

Failures are explicit, traceable, and covered by automated tests.

---

## Build & Test

### Prerequisites
- Java 17+
- Maven 3.8+

### Run tests
```bash
mvn clean test

---

## Run (CLI)

This project primarily focuses on backend logic and automated testing.  
The CLI entry point is intentionally minimal.

```bash
java -cp target/classes com.jamesstevens.rms.Main
```

---

## Tools & Technologies
- **Language:** Java 17
- **Build Tool:** Maven
- **Testing:** JUnit 5
- **Modeling:** UML
- **Persistence:** Structured text files
- **Design Artifacts:** Software Design Document (SDD), class diagrams

---

## Purpose
This project serves as a backend engineering case study demonstrating:
- Object-oriented system design
- Domain-driven validation
- Maintainable architecture
- Controlled state transitions
- Automated testing practices
- Translation of formal design documentation into working code

---

## License
This project is licensed under the MIT License.  
See the [LICENSE](LICENSE) file for details.

---
