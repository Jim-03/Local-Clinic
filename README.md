# Local-Clinic

Local-Clinic is a software solution designed to streamline and manage the operations of a local medical clinic. Built
primarily with Java and TypeScript, this project aims to provide an efficient, user-friendly platform for both medical
staff and administrators to oversee appointments, patient records, billing, and more.

___

## Features

- **Patient Management:** Add, edit, and manage patient records securely.
- **Appointment Scheduling:** Book, update, and track appointments with automated reminders.
- **Billing & Invoicing:** Generate invoices, track payments, and manage clinic finances.
- **Staff Management:** Assign roles, manage schedules, and track staff activity.
- **Reporting:** Generate insightful reports on clinic operations and patient statistics.
- **Secure Authentication:** Role-based access control for staff and administrators.

___

## Tech Stack

![Java](https://img.shields.io/badge/Java-ED8B00?style=flat&logo=java&logoColor=white)
![React](https://img.shields.io/badge/React-20232A?style=flat&logo=react&logoColor=61DAFB)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=flat&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=flat&logo=docker&logoColor=white)

- **Backend:** Java (Spring Boot)
- **Frontend:** TypeScript (React)
- **Styling:** CSS
- **Database:** PostgreSQL
- **Containerization:** Docker

___

## Getting Started

### Prerequisites

- Docker installed (version 24.x or later recommended)

1. **Clone the repository**

   ```bash
   git clone https://github.com/Jim-03/clinic-system.git
   cd clinic-system
   ```

2. **Create a .env file**

   Add the required environment variables to a .env file at the root of the project.
   ```text
   DB_URL=jdbc:postgresql://localhost:5432/clinic
   DB_USER=postgres
   DB_PASSWORD=yourpassword
   DB_NAME=clinic
   SPRING_PROFILE=dev
   VITE_API_URL=http://localhost:8080
   APP_CORS_ALLOWED_ORIGINS=http://localhost:3000
   ```

   > **NOTE:** For the initial setup, set `SPRING_PROFILE=dev` to auto-generate the database structure. Afterward,
   switch to `prod` for normal operation.

3. **Build and run with Docker Compose**

   ```bash
   docker compose up --build
   ```

4. **Access the application**

    * Frontend: http://localhost:3000
    * Credentials:
        * **Username**: `admin`
        * **Password**: `admin`
    * Swagger API docs: http://localhost:8080/swagger-ui

___

## Contributing

1. Fork the repository.
2. Create your feature branch:
   ``` bash
   git checkout -b feature/YourFeature
   ```
3. Commit your changes:
   ```bash
   git commit -m 'Add some feature'
   ```
4. Push to the branch:
   ```bash
   git push origin feature/YourFeature
   ```
5. Open a pull request.

___

## Live Demo

For the live app visit this [link](https://jims-local-clinic.vercel.app/)

___

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for more details.

___

## Acknowledgements

- Inspired by the need for efficient local healthcare management.
- Built with open-source technologies.

___

## Author

**Name:** Jimmy Chemuku

### Contact

[GitHub](https://github.com/Jim-03)

[Email](mailto:chemuku.jimmy@gmail.com)