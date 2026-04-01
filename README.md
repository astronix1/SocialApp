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
