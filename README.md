# Reservation Management System

## Overview
The Reservation Management System is a modular Java backend designed to manage customer accounts and lodging reservations across multiple property types, including hotels, cabins, and houses. The system emphasizes clean architectural decomposition, strict data integrity enforcement, and maintainable object-oriented design.

All data is persisted using a structured file-based storage model rather than a database, allowing the system to simulate core backend responsibilities while remaining portable and easily extensible for future database or API integration.

---

## System Capabilities
- Account creation, retrieval, and modification  
- Reservation lifecycle management (draft, completion, cancellation)  
- Support for multiple lodging types with inheritance-based specialization  
- Per-night and total cost calculations with type-specific pricing logic  
- Structured file-based persistence using standardized naming conventions  
- Centralized orchestration through a manager/controller layer  
- Robust validation and domain-specific exception handling  

---

## Architecture Overview
The system follows a layered, object-oriented architecture:

- **Manager**  
  Central controller responsible for coordinating accounts, reservations, validation, and persistence.

- **Account**  
  Represents customer records and maintains associations to reservation identifiers.

- **Reservation (Abstract Base Class)**  
  Encapsulates shared reservation attributes and behavior.

- **Concrete Reservation Types**  
  - `HotelReservation`  
  - `CabinReservation`  
  - `HouseReservation`  

- **Persistence Layer**  
  Structured text files per account and per reservation, enabling deterministic loading and saving.

- **Exception Framework**  
  Custom exception hierarchy enforcing parameter validation, state transitions, and I/O correctness.

A full UML class diagram is included in the project documentation.

---

## Data Persistence Model
- One directory per account  
- One account file per customer  
- One reservation file per reservation  
- XML-like structured text format for readability and future migration  

This approach simulates backend persistence while keeping storage logic explicit and testable.

---

## Reservation State Management
Reservations follow a controlled lifecycle:

- `DRAFT` → `COMPLETED`  
- `DRAFT` → `CANCELLED`  

Invalid state transitions are explicitly blocked through domain logic and custom exceptions to preserve system consistency.

---

## Error Handling Strategy
The system uses a comprehensive custom exception hierarchy, including:
- Invalid parameter handling  
- Duplicate object detection  
- Illegal state transitions  
- Missing account or reservation access  
- Load/save failures during file I/O  

This ensures failures are explicit, traceable, and testable.

---

## Testing
The project includes an extensive JUnit test suite covering:
- Account creation and lookup  
- Reservation creation, retrieval, updates, and cancellation  
- Pricing logic  
- Manager orchestration behavior  
- Exception and edge-case handling  

Tests are designed to validate correctness, regression safety, and boundary conditions.

---

## Tools & Technologies
- **Language:** Java  
- **Testing:** JUnit  
- **Modeling:** UML  
- **Persistence:** Structured text files  
- **Design Artifacts:** Software Design Document (SDD), UI mockups, class diagrams  

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
This project is provided for educational and portfolio demonstration purposes.
