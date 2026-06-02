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

## 📸 Screenshots

<img width="526" height="645" alt="image" src="https://github.com/user-attachments/assets/c0079a6d-3a69-46bd-ade0-e5dfa6b93be4" />
<img width="823" height="613" alt="image" src="https://github.com/user-attachments/assets/c1259919-85af-49b9-aa70-6cc67ab94e5d" />
<img width="828" height="650" alt="image" src="https://github.com/user-attachments/assets/c7b58f8d-42e6-4546-b89c-2ac7fa83a023" />
<img width="780" height="613" alt="image" src="https://github.com/user-attachments/assets/ed913df6-13a5-4faa-ba06-6203ef21de64" />
<img width="780" height="579" alt="image" src="https://github.com/user-attachments/assets/d7a50ebb-9117-412a-8797-ee211e15f944" />
<img width="803" height="593" alt="image" src="https://github.com/user-attachments/assets/f468a8cd-0b4c-409d-b021-45e2ba04cb8e" />
<img width="805" height="636" alt="image" src="https://github.com/user-attachments/assets/870fa0e7-12cc-44c1-b3c0-2935dbaf023b" />
<img width="859" height="633" alt="image" src="https://github.com/user-attachments/assets/f7c01cb2-cc46-40a6-ac96-3acd2641eced" />
<img width="856" height="635" alt="image" src="https://github.com/user-attachments/assets/eecc4863-c261-4dbc-afd6-f6ef7efacb70" />
<img width="854" height="634" alt="image" src="https://github.com/user-attachments/assets/2427a8cd-aed1-4c8d-89f3-dec9ccd6adca" />
<img width="839" height="615" alt="image" src="https://github.com/user-attachments/assets/81947e45-22b0-4ab4-856f-0dd3a4ec7c1f" />
<img width="878" height="693" alt="image" src="https://github.com/user-attachments/assets/b8641054-b596-49b1-b438-e296e30e31ec" />












