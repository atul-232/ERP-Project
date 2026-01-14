University ERP System (Java + Swing)

A desktop-based University ERP system built using Java, Swing, JDBC, and MySQL. The system supports role-based access for Students, Instructors, and Admins.

Features

Student

Browse course catalog
Register / drop sections
View timetable
View grades
Download transcript (CSV/PDF)
Instructor

View assigned sections
Enter assessment scores
Compute final grades
View basic statistics
Export grades (CSV)
Admin

Add users (students/instructors)
Create courses and sections
Assign instructors
Toggle Maintenance Mode
Architecture

The project follows a layered architecture:

ui → Swing screens (no DB logic) api → Public APIs called by UI service → Business logic & validations data → JDBC-based DB access auth → Authentication & password hashing access → Role-based access & maintenance checks domain → Plain data models util → Utilities (export, logging, helpers)

Database Design

Two separate databases are used:

Auth DB

Stores authentication data only
Passwords are stored as secure hashes
Handles login and role management
ERP DB

Stores students, instructors, courses, sections
Manages enrollments and grades
Contains system settings (maintenance mode)
This separation ensures better security and clean responsibility boundaries.

Access Control & Maintenance Mode

All write operations pass through access checks
Role-based permissions are enforced in service layer
When Maintenance Mode is ON:
Students and instructors can only view data
All write actions are blocked with clear messages
UI displays a maintenance banner
Tech Stack

Java (Swing)
JDBC + MySQL
Password4j (password hashing)
FlatLaf (UI look & feel)
How to Run

Clone the repository
Create MySQL databases for Auth and ERP
Copy db.properties.example → db.properties
Update DB credentials
Run the main class
Author

Vivek Kumar
IIIT Delhi
