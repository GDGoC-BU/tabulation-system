# 🗳️ GDGoC BU Tabulation System
An offline tabulation system that manages multiple pageants, designed for real-time scoring and critical result computation. Primarly used in the annual university-wide Mr. and Ms. Bicol University, and other college wide pageants aroung the university.


## 🛠️ Tech Stack

* **Backend**
   * Spring Boot
   * Mapstruct
   * Lombok
   * Redis
   * PostgreSQL
* **Frontend**
   * ReactJS
   * TailwindCSS
   * Tanstack (Router, Query, Table)
   * ShadCN
   * Axios
   * Zod
   * Zustand

## 🚀 Features
* Multi-pageant management
* Runs completely locally with LAN
* Real-time judge scoring sheets with websockets
* Automatic scoring saves
* Admin dashboard
* Award configurations
* Dynamic formula builder with string-based expression evaluation with SpEL
* Evaluated criteria breakdown for each candidate
* Handles multi-phased pageants
* Funnel candidates per segments
* Candidate leaderboards for awards and segments
* Fast response time with caching
* Admin and judge authentication

## 💻 How to Run

### Prerequisites
* Node.js (v22+)
* Java (v21+)
* Maven (v3.9.11+)
* PostgreSQL (v17+ installed locally)
* Docker
* VSCode (Frontend)
* IntelliJ (Backend)

### 1) Clone the repository
```
git clone git@github.com:GDGoC-BU/tabulation-system.git
cd tabulation-system
```

### 2) Setup the Backend
1) Navigate to the backend folder
```
cd backend
```
2) Update `backend/src/main/resources/application.properties:` with your respective postgreSQL credentials. Ensure that the respective database (TabulationDemo) exists! You need to create it manually.
```
spring.datasource.url=jdbc:postgresql://localhost:5432/TabulationDemo
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```


3) Run Docker Desktop in the background

4) Build the backend
```
./mvnw clean install
```

5) Run the backend (or start from IntelliJ)
```
./mvnw spring-boot:run
```

### 3) Setup the Frontend
1) Navigate to the backend folder
```
cd frontend
```
2) Install the dependencies
```
npm install
```
2) Start the development server
```
npm run dev
```

### 4) Data seeding
1) Contact admins for further instructions on how to seed dummy pageant data
