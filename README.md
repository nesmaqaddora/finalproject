# GHADS - Gaza Humanitarian Aid Distribution System 🕊️

## 📌 Project Overview
GHADS is a desktop application developed to manage and organize the distribution of humanitarian aid for displaced families in the Gaza Strip. The system ensures fair distribution by keeping track of which families received what type of aid, preventing duplicate distributions using a smart 30-day verification logic.

**Course:** Programming III Lab - CSCI 2108  
**University:** Islamic University of Gaza  

## 🚀 Features
* **Role-Based Access:** Separate dashboards for Admins and Coordinators.
* **Dynamic Dashboard:** Real-time statistics fetched directly from the database using SQL aggregations.
* **Smart Duplicate Check:** Families with a "HIGH" vulnerability level can receive aid anytime. Families with "MEDIUM" or "LOW" vulnerability are logically restricted from receiving the same aid type within a 30-day period.
* **Bonus Feature:** User profile management with profile picture upload functionality to the database.
* **UI/UX Customization:** Dark/Light mode toggle and dynamic font size adjustments applied globally.

## 💻 Technologies & Architecture
* **Language:** Java
* **GUI Framework:** JavaFX (FXML & Custom CSS)
* **Database:** MySQL (JDBC)
* **Architecture Patterns:** MVC (Model-View-Controller), DAO (Data Access Object), and Singleton (for Database Connection management).

## ⚙️ How to Run the Project
1. Clone this repository to your local machine.
2. Open the project in **IntelliJ IDEA**.
3. Ensure you have the **JavaFX SDK** configured in your project structure.
4. Import the provided database file (`ghads_final_project_db.sql`) into your local MySQL server (e.g., via XAMPP / phpMyAdmin).
5. Run the `Main.java` file to launch the application.
