
# Student Management System
## Overview

The Student Management System is a web-based application designed to simplify the management of student information. It provides a centralized way to maintain student records and perform common student management operations.

The project was developed to demonstrate database connectivity, backend development, and CRUD operations using Java-based technologies and PostgreSQL.

## Objectives
- Maintain student records in a structured database.
- Simplify student information management.Perform create, read, update, and delete operations.
- Store and retrieve data using PostgreSQL.
- Build a maintainable application using Java technologies.
## Technology Stack
- Backend: Java, Spring Boot
- Database: PostgreSQL
- Database Connectivity: Spring
- Data JPA / Hibernate, if configured
- Build Tool: Maven
- Development Environment: Eclipse IDE
## Core Features
- Student record management
- Adding new student information
- Viewing saved student
- recordsUpdating existing records
- Deleting records
- Database-backed data persistence
## Architecture
The application follows a layered backend architecture:
- Controller Layer: Handles incoming HTTP requests.
- Service Layer: Contains business logic, if separated in the project.
- Repository Layer: Communicates with the database.
- Database Layer: PostgreSQL stores student information.
## Getting Started
### Prerequisites

- Java JDK compatible with the project
- Maven
- PostgreSQL
- Eclipse IDE or another Java IDE


## Setup

- Clone or download the project.
- Open the project in Eclipse as a Maven project.
- Create a PostgreSQL database for the application.
- Configure the database URL, username, and password in application.properties.
- Allow Maven to download the required dependencies.
- Run the Spring Boot application.
- Test the available operations using the frontend, browser, or API client appropriate to your project.


## Database Configuration

#### Update the values below to match your local PostgreSQL installation.

spring.datasource.url=jdbc:postgresql://localhost:5432/your_database
spring.datasource.username=your_username
spring.datasource.password=your_password

Use the actual port and database name configured on your computer.

## Learning Outcomes

- Java application development
- Spring Boot fundamentals
- Relational database management
- CRUD operations
- Database integration and debugging
- Maven project management

## Future Enhancements

- Student search and filtering
- Pagination for large datasets
- Role-based access control
- Data export to CSV or PDF
- Improved validation and reporting

##  screenshots
login page
![1](https://github.com/user-attachments/assets/89357afc-0ec8-45c9-9ab6-34daa498955c)
dashboard page
![2](https://github.com/user-attachments/assets/2ab7d5d8-8dc5-4ce4-8bc5-47286ae6ea7e)
Add Student page
![3](https://github.com/user-attachments/assets/e9400d1e-6939-4824-81cb-c845697049d7)
teacher page
![4](https://github.com/user-attachments/assets/be8dd877-cd88-4f7f-a6f1-9fdb4ab1cb54)
course page
![5](https://github.com/user-attachments/assets/9d2347bd-ce4d-48f9-8efd-6d64d7206866)
Attendance
![6](https://github.com/user-attachments/assets/56f5ff2b-f69f-4844-8b28-56baf6538843)
Marks
![7](https://github.com/user-attachments/assets/3d3dfa31-4b68-49d0-8cc1-cbeb62877be9)


## Conclusion

The Student Management System demonstrates how Java backend technologies and PostgreSQL can be used to develop an application for managing student information efficiently. It provides a foundation for learning database-driven application development.