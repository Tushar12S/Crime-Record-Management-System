# Crime-Record-Management-System
The Crime Record Management System is a Java-based application designed to help law enforcement agencies efficiently manage crime-related records. This system allows authorized users to add, modify, delete, and view crime records in a user-friendly manner. It features a simple login mechanism to ensure that only authorized personnel can access the system.

All crime records are stored persistently using file I/O, meaning the data is saved to a file and can be retrieved across multiple sessions, ensuring data is never lost after exiting the application.

Key Features:

User Authentication: A basic login feature where only the admin can access the system.
Add Records: Users can input detailed information about a crime, including the suspect, date, evidence, and witness details.
Modify Records: Crime records can be updated based on their unique serial number.
Delete Records: Allows deletion of records by their serial number.
View Records: Displays all crime records stored in the system.
Persistent Data Storage: Uses file-based storage to keep records between application restarts.
Technologies Used:

Java: For building the core logic of the system.
File I/O: Used for reading from and writing to a file for persistent storage.
ArrayList: To manage the in-memory list of crime records.
