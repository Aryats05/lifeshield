🛡️ LifeShield – Health & Wellness Dashboard

LifeShield is a full-stack health and wellness management platform designed to help users monitor their health, maintain healthy habits, manage medical records, and stay updated with important health reminders.

The application provides a centralized dashboard where users can manage their personal health information securely through an account-based authentication system.

---

🚀 Key Features

- User Authentication: Secure user registration and login with BCrypt password encryption.
- Personalized Dashboard: Manage and access personal health information.
- Daily Health Tracking: Record and monitor daily health activities.
- Weekly Health History: Maintain a history of health-related activities.
- Vaccination Management: Store and manage vaccination records.
- Health Reminders: Keep track of important health-related reminders.
- Health Assessments: Maintain assessment history.
- Database Integration: Store user information and application data in PostgreSQL.
- Session Management: Login sessions with automatic expiration and logout functionality.
- Data Synchronization: Synchronize frontend application data with the backend database.
- Responsive Interface: React-based user interface.

---

🛠️ Technology Stack

Component| Technology
Frontend| React.js
Programming Language| TypeScript
Backend| Java, Spring Boot
Database| PostgreSQL
ORM| Spring Data JPA, Hibernate
Authentication| HTTP Session, BCrypt
Build Tool| Maven
Package Manager| npm
API Architecture| REST API
Development Environment| VS Code

---

📁 Project Structure

LifeShield/
│
├── frontend/
│   ├── src/
│   ├── public/
│   ├── package.json
│   └── ...
│
├── backend/
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── com/lifeshield/
│   │       │       ├── auth/
│   │       │       ├── controller/
│   │       │       ├── model/
│   │       │       ├── repository/
│   │       │       └── ...
│   │       │
│   │       └── resources/
│   │           └── application.properties
│   │
│   └── pom.xml
│
├── .gitignore
└── README.md

---

⚙️ Prerequisites

Install the following software before running the project:

- Node.js 20 or above
- npm
- Java JDK 21 or above
- Apache Maven 3.9 or above
- PostgreSQL 15 or above
- Visual Studio Code

---

💻 Installation and Setup

1. Clone the Repository

git clone https://github.com/YOUR_USERNAME/liveshield.git

Navigate into the project:

cd liveshield

2. Database Configuration

Open PostgreSQL or pgAdmin and create a database:

CREATE DATABASE lifeshield_db;

Configure your database credentials in:

"backend/src/main/resources/application.properties"

Example:

spring.datasource.url=jdbc:postgresql://localhost:5432/lifeshield_db
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

Use the actual database settings required by your project.

Important: Never upload actual database passwords or secret credentials to GitHub.

3. Run the Backend

Open a terminal:

cd backend

Build the application:

mvn clean install

Start the Spring Boot server:

mvn spring-boot:run

Backend server:

http://localhost:8080

4. Run the Frontend

Open another terminal:

cd frontend

Install dependencies:

npm install

Start the frontend:

npm run dev

Open the local URL displayed in your terminal.

---

🔐 Authentication API

Base URL:

http://localhost:8080/api/auth

Method| Endpoint| Description
POST| "/register"| Register a new user
POST| "/login"| Authenticate an existing user
GET| "/me"| Get currently logged-in user
POST| "/logout"| End the current session

Authentication Features

- Password hashing using BCrypt.
- Duplicate email validation.
- Session-based authentication.
- 30-minute session inactivity timeout.
- Session invalidation during logout.
- Login session created after successful registration.

---

🗄️ Database Management

LifeShield uses PostgreSQL to manage user information and application data.

The database supports:

- User account information.
- User-specific health records.
- Daily health logs.
- Weekly health history.
- Vaccination records.
- Health reminders.
- Health assessment history.

Each user's application data is associated with their authenticated account to support individual data management.

---

🔄 Application Workflow

          USER
           |
           v
    Registration / Login
           |
           v
   Backend Authentication
           |
           v
      PostgreSQL
           |
           v
    User Dashboard
           |
           v
     Health Tracking
           |
           v
    Backend API Sync
           |
           v
      Database

---

🔒 Security Considerations

- Passwords are stored as BCrypt hashes.
- Authentication uses HTTP sessions.
- User-specific data should be accessed only after verifying the authenticated session.
- Database credentials should be stored outside public source control.
- Production deployment should use HTTPS and secure cookie settings.

---

🔮 Future Enhancements

- AI-powered personalized health recommendations.
- Integration with wearable health devices.
- Advanced health analytics and visualization.
- Medicine and appointment reminders.
- Emergency health assistance.
- Cloud deployment.
- Enhanced authentication and account recovery.

---

🎯 Project Objective

The primary objective of LifeShield is to develop a centralized digital health management platform that simplifies personal health monitoring and encourages users to maintain healthier lifestyles.

By combining modern frontend technologies, Spring Boot backend services, and PostgreSQL database management, LifeShield aims to provide a scalable and user-friendly health management solution.

---

👨‍💻 Developer

Project: LifeShield – Health & Wellness Dashboard

Category: Full-Stack Web Development

Frontend: React + TypeScript

Backend: Java + Spring Boot

Database: PostgreSQL

Repository: "GitHub – LifeShield" (https://github.com/YOUR_USERNAME/liveshield)

---

⭐ If you find this project interesting, consider giving the repository a star!
