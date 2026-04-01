# 🚀 SocialApp Backend API

A robust RESTful backend for a social media platform built using **Kotlin + Spring Boot**.  
It provides authentication, user management, posts, comments, notifications, and more.

---

## 🧰 Tech Stack

- **Language:** Kotlin  
- **Framework:** Spring Boot 3.x  
- **Security:** Spring Security + JWT  
- **Database:** PostgreSQL  
- **ORM:** Spring Data JPA / Hibernate  
- **Mapping:** MapStruct  
- **Email:** JavaMailSender  
- **Storage:** Local File System (`/uploads`)  

---

## ✨ Features

### 🔐 Authentication
- User Signup & Login (JWT-based)
- Email Verification
- Password Reset

### 👤 User Features
- Update profile & images
- Follow / Unfollow users
- Search users

### 📸 Posts
- Create / Update / Delete posts
- Like / Unlike posts
- Share posts
- Tag system

### 💬 Comments
- Add / Delete comments
- Like / Unlike comments

### 🔔 Notifications
- Like / Comment / Share notifications
- Mark as read/seen

---

## 📋 Prerequisites

Make sure you have:

- JDK 17+
- PostgreSQL
- Maven or Gradle

---

## ⚙️ Setup

### 1. Database

```sql
CREATE DATABASE socialapp;
CREATE USER socialuser WITH PASSWORD 'password';
GRANT ALL PRIVILEGES ON DATABASE socialapp TO socialuser;


### 2. Configuration

Update `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/socialapp
spring.datasource.username=socialuser
spring.datasource.password=password

spring.jpa.hibernate.ddl-auto=update

jwt.secret=your-secret-key

app.root.backend=http://localhost:8080

upload.user.images=uploads/users
upload.post.images=uploads/posts
```

---

### 3. Run Project

#### Using Maven

```bash
./mvnw spring-boot:run
```

#### Using Gradle

```bash
./gradlew bootRun
```

Server runs at:

```
http://localhost:8080
```

---

## 📁 Project Structure

```
src/main/kotlin/com/yourapp/
├── config/        # Security & App configs
├── controller/    # REST APIs
├── service/       # Business logic
├── repository/    # Database layer
├── entity/        # JPA entities
├── dto/           # Request/Response models
├── mapper/        # MapStruct mappers
├── exception/     # Custom exceptions
└── util/          # Utilities
```

---

## 🔒 Security

All endpoints require JWT except:

```
POST /api/v1/signup
POST /api/v1/login
POST /api/v1/forgot-password
POST /api/v1/reset-password/**
GET  /uploads/**
```

Use header:

```
Authorization: Bearer <token>
```

---

## 📧 Email Service

* **Dev Mode:** Logs emails in console
* **Prod Mode:** Sends real emails via SMTP

---

## 🧪 Future Improvements

* Docker support
* Unit & Integration tests
* CI/CD pipeline
* Cloud deployment

---

## 🤝 Contributing

Pull requests are welcome!
Feel free to fork and improve 🚀

---

## 📜 License

This project is licensed under the MIT License.




