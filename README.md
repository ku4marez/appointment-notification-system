# 📅 Appointment Notification Service

The **Appointment Notification Service** is a microservice designed to send notifications for scheduled medical appointments. It supports **email** and **SMS** reminders to improve patient engagement and reduce missed appointments.

---

## 📌 Features

- **Automated Appointment Reminders**  
  Sends reminders via **email** or **SMS** before scheduled appointments.

- **Doctor & Patient Notifications**  
  Notifies both patients and doctors about upcoming appointments.

- **Customizable Reminder Intervals**  
  Configure notification timing (e.g., **1 day, 1 hour** before appointment).

- **Retry Mechanism**  
  Ensures reliable delivery of messages in case of failures.

- **Integration with Clinic Management System**  
  Fetches appointment details via **REST API**.

- **Dockerized Deployment**  
  Easily deployable using **Docker Compose**.

---

## 🚀 Technologies Used

### **Backend (Spring Boot + MongoDB)**
- 🟢 **Java 21**
- 🟢 **Spring Boot 3.1.4**
- 🟢 **Spring Data MongoDB**
- 🟢 **Spring Boot Mail (for email notifications)**
- 🟢 **Twilio API (for SMS notifications)**
- 🟢 **Lombok**
- 🟢 **ModelMapper**
- 🟢 **Maven**

### **Infrastructure**
- 🟡 **MongoDB** (Stores appointment notifications)
- 🟡 **Docker & Docker Compose**
- 🟡 **Nexus Repository** (for dependency management)
- 🟡 **Jenkins/GitHub Actions** (CI/CD)

---

## 🛠️ Getting Started

### 🔽 Clone the Repository
```sh
git clone https://github.com/your-username/appointment-notification-service.git
cd appointment-notification-service
```

### 🚀 Run Backend (Spring Boot Application)
```sh
mvn clean package
java -jar target/appointment-notification-service.jar
```
- The backend will be available at http://localhost:8080.