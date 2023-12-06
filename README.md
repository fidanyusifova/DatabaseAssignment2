# DatabaseAssignment2

---

# Bookstore Management System

This Java-based application is designed to manage a bookstore's database, enabling interactions such as CRUD operations, transaction handling, and metadata access. The system provides functionalities to manage books, authors, customers, orders, and order details efficiently.


## Features
- **Database Connectivity:** Connects to a PostgreSQL database using JDBC.
- **CRUD Operations:** Supports Create, Read, Update, and Delete operations for books, authors, customers, orders, and order details.
- **Transaction Management:** Ensures transactional integrity when placing orders, maintaining data consistency.
- **Metadata Access:** Provides methods to access metadata information, including details about tables, columns, and keys.

## Technologies Used
- Java
- PostgreSQL
- JDBC

# Bookstore Database Project

This repository contains a database project for a bookstore, focusing on creating a relational database schema, implementing CRUD operations, managing transactions, and accessing metadata using Java and PostgreSQL.

## Project Structure

### Database Schema
- **TableScripts.sql**: Contains SQL scripts to create database tables (`Books`, `Authors`, `Customers`, `Orders`, `OrderDetails`), define primary and foreign keys, and establish relationships between entities.

### Java Classes
- **BookCrud.java**: Manages CRUD operations for books, including insertion, retrieval, updating, and deletion.
- **AuthorCrud.java**: Handles CRUD operations for authors, providing functionalities to add, retrieve, update, and delete author details.
- **CustomerCrud.java**: Implements CRUD operations for customers, allowing addition, retrieval, modification, and removal of customer records.
- **OrderCrud.java**: Provides methods to handle CRUD operations for orders, including order placement, retrieval, and deletion.
- **Transaction.java**: Manages transactional operations such as placing orders and updating book quantities atomically.
- **Metadata.java**: Accesses metadata information from the database, displaying details about tables, columns, primary keys, and foreign keys.

### Supporting Classes
- **Book.java**: Represents book entities with attributes and getter/setter methods.
- **Author.java**: Represents author entities with attributes and getter/setter methods.
- **Customer.java**: Represents customer entities with attributes and getter/setter methods.
- **Order.java**: Represents order entities with attributes and getter/setter methods.
- **OrderDetail.java**: Represents order detail entities with attributes and getter/setter methods.
- **OrderRequest.java**: Represents order requests with attributes and getter/setter methods.
- **DbFunctions.java**: Provides database connectivity functionalities, including establishing connections to the PostgreSQL database.

## Instructions

1. **Database Setup**
    - Run the SQL script `TableScripts.sql` to create the necessary tables and establish relationships in your PostgreSQL database.
    - Modify the database connection details in the `DbFunctions.java` class to match your PostgreSQL configuration.

2. **Java Application**
    - Run and test the Java classes to perform CRUD operations on books, authors, customers, and orders.
    - Utilize `Transaction.java` for managing transactional operations.

3. **Metadata Access**
    - Use `Metadata.java` methods to access and display metadata information about tables, columns, and keys in the database.

## Usage
- Clone this repository to your local environment.
- Set up the database by running the SQL script.
- Run and test the Java application for interacting with the bookstore database.

Feel free to explore and contribute to this database project! If you encounter any issues, please raise them in the repository's "Issues" section.

Happy coding!

