# 🗳️ Tabulation Demo
An offline tabulation system designed for critical real-time scoring and result computation.  

---

## 🛠️ Tech Stack

- **Backend**: Spring Boot (A dedicated backend is required to make the system robust and work offline)
- **Frontend**: Vite + ReactJS or NextJS (still deciding) 
- **Database**: PostgreSQL
- **Logging**: Spring Logback

---

## 🚀 Current To-Implement Features

### 1. Codebase
- Strong coding standards
- Good documentation
- Separation of concerns
- Maintainable
- Onboarding friendly

### 2. Frontend
- Beautiful UI yet seamless UX

### 3. LAN-based Connectivity  
- Works without internet, only requires local router/switch to asssign IP addresses.  
- Backend runs on one laptop, clients discover and connect within LAN.

### 4. High Reliability
- Dedicated backend built for **<10 clients**, focused on robustness over scalability.  
- **Short heartbeat intervals (2–3s)** for instant disconnect detection.
- Potential for **backup server promotion** if the main backend fails (future improvement).
- Aggressive Autosave judge-form state incase of disconnection. Like how Google Docs/Forms saves state even without submition

### 5. Admin Dashboard  
- Realtime Dashboard using websockets
- View connectivity of all clients and judges (Online, Disconnected, Offline).
- View current scores
- Contestant leaderboard
- Instant notification if judge/s disconnects

### 6. Authentication  
- Issues JWT at logins, and token is attached on every request

### 7. Logging  
- Logs for minute details of different subsystems for tracking:  
  - **Connectivity logs** (client heartbeats, disconnects, reconnects)  
  - **Input logs** (judges’ scoring inputs, corrections)  
  - **System logs** (server errors, warnings)

### 8. Data Export
- Save event details after event 
