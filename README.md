# Gym Management System

A console-based Gym Management System built using Java, MySQL, and JDBC.

## Features
- Add Member
- View Members
- Update Member
- Delete Member
- Search Member
- Membership Plans (1, 6, 12 Months)
- Membership Renewal
- Fee Tracking

## Technologies Used
- Java
- MySQL
- JDBC
- VS Code

## Database
MySQL database is used to store:
- Member details
- Membership dates
- Fee information

## Concepts Used
- JDBC Connectivity
- PreparedStatement
- ResultSet
- CRUD Operations
- Date Functions

## How to Run
1. Open project in VS Code
2. Connect MySQL database
3. Add JDBC connector jar
4. Compile:

```bash
javac -cp ".;mysql-connector-j-9.7.0.jar" Main.java