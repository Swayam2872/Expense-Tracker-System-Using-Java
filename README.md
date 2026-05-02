# Expense Tracker System
**Java Web App · JDBC + MySQL · Servlet API · HTML Frontend**

Submitted by: Himanshu Dhandre · Swayam Hull · Tanay Rangari · Aditya Shelar
Faculty: Prof. Wasim Khan | SIT Pune — Dept. AI & ML | AY 2025-26

---

## Prerequisites

Make sure these are installed before you start:

| Tool | Version | Download |
|------|---------|----------|
| JDK | 17+ | https://adoptium.net |
| Apache Tomcat | 10.1+ | https://tomcat.apache.org/download-10.cgi |
| Maven | 3.8+ | https://maven.apache.org/download.cgi |
| MySQL | 8.x | Already installed (Workbench) |

---

## Step 1 — Set up the Database (MySQL Workbench)

1. Open **MySQL Workbench**
2. Connect using:
   - Host: `localhost`
   - Port: `3306`
   - User: `root`
   - Password: `Swayam@123`
3. Open the file `src/main/resources/schema.sql`
4. Click the ⚡ **Execute** button (or press `Ctrl+Shift+Enter`)

This will:
- Create the database `expense_tracker_db`
- Create the `expenses` table
- Insert 10 sample records

---

## Step 2 — Build the Project

Open a terminal in the project root folder (where `pom.xml` is) and run:

```
mvn clean package
```

This creates the file: `target/ExpenseTracker.war`

---

## Step 3 — Deploy to Tomcat

1. Copy `target/ExpenseTracker.war` into your Tomcat's `webapps/` folder

   Example path: `C:\apache-tomcat-10.1.x\webapps\`

2. Start Tomcat:
   - Windows: double-click `bin\startup.bat`
   - Mac/Linux: run `bin/startup.sh`

3. Wait ~5 seconds for deployment

---

## Step 4 — Open the App

Open your browser and go to:

```
http://localhost:8080/ExpenseTracker/
```

You should see the Expense Tracker dashboard.

---

## Project Structure

```
ExpenseTracker/
├── pom.xml                         ← Maven config (MySQL driver included)
├── README.md
└── src/main/
    ├── java/com/expensetracker/
    │   ├── model/
    │   │   └── Expense.java        ← Data model
    │   ├── dao/
    │   │   ├── ExpenseDAO.java     ← CRUD interface
    │   │   └── ExpenseDAOImpl.java ← JDBC implementation
    │   ├── servlet/
    │   │   └── ExpenseServlet.java ← REST API (GET/POST/PUT/DELETE)
    │   └── util/
    │       └── DatabaseConnection.java  ← DB connection (your credentials)
    ├── resources/
    │   └── schema.sql              ← Run this in Workbench
    └── webapp/
        ├── index.html              ← Frontend UI
        └── WEB-INF/
            └── web.xml
```

---

## CRUD Operations — What Each Does

| Operation | Method | URL | What Happens |
|-----------|--------|-----|--------------|
| **Create** | POST | `/api/expenses` | Inserts a new expense row |
| **Read All** | GET | `/api/expenses` | Returns all rows as JSON |
| **Read One** | GET | `/api/expenses?id=3` | Returns one row by ID |
| **Read Totals** | GET | `/api/expenses?totals=1` | Returns total + breakdown |
| **Update** | PUT | `/api/expenses` | Updates a row by ID |
| **Delete** | DELETE | `/api/expenses?id=3` | Deletes a row by ID |

---

## Database Details

```
Database : expense_tracker_db
Table    : expenses
Host     : localhost:3306
User     : root
Password : Swayam@123
```

Table columns:

| Column | Type | Description |
|--------|------|-------------|
| id | INT AUTO_INCREMENT | Primary key |
| category | VARCHAR(100) | e.g. Food, Transport |
| description | VARCHAR(255) | Short note |
| amount | DECIMAL(12,2) | Rupee amount |
| date | DATE | Date of expense |
| created_at | TIMESTAMP | Auto-set on insert |

---

## Troubleshooting

**Port 8080 already in use?**
Change Tomcat's port in `conf/server.xml` — find `port="8080"` and change it.

**`Communications link failure` error?**
Make sure MySQL is running. In Workbench, check the connection.

**`Access denied for user root`?**
Verify your MySQL password is `Swayam@123`. If you changed it, update `DatabaseConnection.java`.

**WAR not deploying?**
Check Tomcat logs at `logs/catalina.out` for errors.

---

## GitHub

https://github.com/Swayam2872/Expense-Tracker-System-Using-Java
