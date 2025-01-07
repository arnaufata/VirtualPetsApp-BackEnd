# VirtualPetsApp - Backend

**VirtualPetsApp** is a backend application built with **Spring Boot** and **MySQL** that provides the necessary APIs for the VirtualPetsApp platform. It supports user authentication, role-based authorization, and CRUD operations for managing virtual pets.

## ✨ Features
- **User Authentication**: Register, login, and token-based authentication using JWT.
- **Role-Based Authorization**:
  - **ROLE_USER**: Users can manage their own virtual pets.
  - **ROLE_ADMIN**: Admins have full access to all pets and user data.
- **Pet Management**: Create, read, update, and delete virtual pets.
- **Interactive Environment**: Provides endpoints to interact with and customize pets.
- **API Documentation**: Includes Swagger UI for easy API exploration and testing.

## 🛠️ Requirements
- **Java 17**
- **MySQL**
- **Maven**
- **Spring Boot**

## 📥 Installation

### 1. Clone the repository
```bash
git clone https://github.com/arnaufata/VirtualPetsApp-Backend.git
```
### 2. Configure the application
Modify the `application.properties` file to match your database and JWT settings:

```bash
# Database configuration
spring.datasource.url=jdbc:mysql://localhost:3306/virtual_pets
spring.datasource.username=<your_database_username>
spring.datasource.password=<your_database_password>

# JWT configuration
jwt.secret=<your_jwt_secret>
jwt.validity=86400000
```
3. Install dependencies and build the project
Navigate to the project folder and build the application:

```bash
mvn clean install
```
4. Run the application
Start the backend application:

```bash
mvn spring-boot:run
```
The application will run on `http://localhost:8080`.

## 🚀 API Endpoints
The application exposes RESTful API endpoints for the following functionalities:

### Authentication
- **POST /auth/register**: Register a new user.
- **POST /auth/login**: Login and receive a JWT token.

### Pet Management
- **GET /pets**: Retrieve all pets (Admin only).
- **POST /pets**: Create a new pet.
- **GET /pets/{id}**: Retrieve a pet by its ID.
- **PUT /pets/{id}**: Update a pet's information.
- **DELETE /pets/{id}**: Delete a pet.

### Swagger UI
Access the API documentation at:
```bash
http://localhost:8080/swagger-ui.html
```
## 📂 Project Structure
```bash
VirtualPetsApp-Backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com.virtualpets.app/
│   │   │       ├── controller/
│   │   │       ├── model/
│   │   │       ├── repository/
│   │   │       ├── service/
│   │   │       └── config/
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/
├── pom.xml
└── README.md
```
### Explanation:
- `controller/`: Contains the REST controllers for handling HTTP requests.
- `model/`: Defines the entities and domain objects.
- `repository/`: Interfaces for database operations.
- `service/`: Contains the business logic.
- `config/`: Configuration classes, including security and JWT setup.
  
## 🐾 Role-Based Authorization
The backend implements role-based authorization:

- **User Role (ROLE_USER)**:
  - Access to their own pets for reading, updating, and deleting.
- **Admin Role (ROLE_ADMIN)**:
  - Full access to all pets and system data.

## 🎨 Customization
You can customize the application by modifying:

- **Database configuration** in `application.properties`.
- **JWT settings** for token validity and secret keys.
- **Swagger settings** for API documentation.
  
## 📦 Dependencies
The project uses the following dependencies:

- **Spring Boot**: Core framework for the backend.
- **Spring Security**: For authentication and authorization.
- **Spring Data JPA**: To interact with the MySQL database.
- **JWT**: For token-based authentication.
- **Swagger**: For API documentation.
  
## 🤝 Contribution
If you'd like to contribute:

1. Fork the repository.
2. Create a new branch:
```bash
git checkout -b feature/new-feature
```
3. Commit your changes:
```bash
git commit -m "Description of changes"
```
4. Push your branch:
```bash
git push origin feature/new-feature
```
5. Open a pull request.
 
## 📄 License
This project is licensed under the MIT License.

