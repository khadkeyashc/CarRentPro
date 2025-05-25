# CarRentPro
CarRentPro is a Java-based commercial car rental system designed to connect individual vehicle owners with organizations seeking long-term vehicle rentals. The system enables customers to register their vehicles, which can then be leased to organizations under contract-based agreements ranging from 3 months to 5 years. Built using Java Swing for the graphical user interface (GUI) and MySQL for backend data management, CarRentPro provides a secure, scalable, and user-friendly platform that automates the rental lifecycle—from user registration to vehicle tracking and reporting.
Features

**User Management**
- Role-based authentication: Admin, Customer, and Organization Employee.
- Secure login and registration system.
- Admin dashboard for managing users, vehicles, and rental contracts.

**Vehicle Management**
- Add, edit, delete, and view vehicle information.
- Image upload and condition tracking.
- Real-time vehicle availability status.

**Rental Workflow**
- Customers list their vehicles for organizational rental.
- Organizations submit rental requests with custom durations and pricing.
- Admins approve or reject contracts, ensuring manual oversight alongside system automation.

**Contract & Billing Management**
- Contract creation with duration, status tracking, and renewal alerts.
- Auto-calculation of rental fees.
- Receipt and invoice generation (planned).

**Reports & Analytics**
- Generate rental history and user activity reports.
- Analyze vehicle usage and revenue data.
- Export options for data backups and reporting.

Technology Stack

| Layer        | Technology               |
|--------------|--------------------------|
| Language     | Java (Core Java)         |
| UI Framework | Java Swing               |
| Database     | MySQL (JDBC Integration) |
| IDE          | IntelliJ IDEA / Eclipse  |
| OS Support   | Windows, macOS, Linux    |

ystem Architecture

**Key Modules**
- **Customer Module:** Register, view rental status, and manage vehicle listings.
- **Admin Module:** Control center for approving requests, managing data, and analytics.
- **Organization Module:** Submit rental requests and track rental history.

**Included Diagrams**
- Module Hierarchy Diagram
- Entity Relationship (ER) Diagram
- Class Diagram
- Use Case Diagram
- Activity Diagram
- Sequence Diagram
- 
### Functional Requirements

- Secure, role-based login system.
- Car filtering and advanced search functionality.
- Contract creation and rental status updates.
- Notifications for rental events and expirations.
- Reporting dashboard for system-wide metrics.

---

### Non-Functional Requirements

- Fast system performance with low response latency.
- Data security through encryption and access control.
- High availability with a 99.9% uptime target.
- Platform-independent deployment (Java runtime).
- Modular, maintainable, and scalable codebase.

### Database Structure

- **Admin Table:** Stores admin credentials.
- **Customer Table:** Records customer information.
- **Car Table:** Details for each listed vehicle.
- **Organization Table:** Information about renting organizations.
- **Rental Table:** Tracks contract data and durations.
- **Car Images Table (Optional):** Stores uploaded images.

- ### Sample Interface Modules

- Admin login and dashboard
- Customer registration and vehicle listing
- Car registration with search and filter
- Organization rental request form
- Rental contract approval/rejection system

  
CarRentPro offers a comprehensive solution for managing commercial vehicle rentals within a peer-to-peer model. The system streamlines the interaction between vehicle owners and organizations through secure, automated workflows. Built with scalable Java architecture and a relational database, it is designed to meet the needs of organizations seeking transparency, data security, and long-term rental efficiency.
Developed as part of an academic project, CarRentPro demonstrates full-stack Java application development with real-world utility.

