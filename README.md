# New School

New School es una aplicación de backend para un sistema de gestión de aprendizaje (LMS) desarrollada en Java utilizando Spring Boot. Este proyecto es parte de la materia de sistemas de información.

## Requisitos

- Java 17
- Maven 3.9.9
- PostgreSQL

## Configuración

1. Clona el repositorio:

    ```sh
    git clone https://github.com/tu-usuario/Backend-New-School.git
    cd Backend-New-School/New-School
    ```

2. Configura la base de datos PostgreSQL:

    - Crea una base de datos llamada `new-school-backend`.
    - Actualiza las credenciales de la base de datos en el archivo `src/main/resources/application.properties`:

        ```properties
        spring.datasource.url=jdbc:postgresql://localhost:5432/new-school-backend
        spring.datasource.username=postgres
        spring.datasource.password=1234
        ```

## Construcción y Ejecución

1. Construye el proyecto con Maven:

    ```sh
    ./mvnw clean install
    ```

2. Ejecuta la aplicación:

    ```sh
    ./mvnw spring-boot:run
    ```

## Endpoints

La aplicación expone varios endpoints RESTful para gestionar usuarios y estudiantes. Aquí hay algunos ejemplos:

- **Usuarios**
    - `GET /api/v1/usuario/getUsuarios`: Obtiene la lista de todos los usuarios.
    - `GET /api/v1/usuario/getUsuarios/{id}`: Obtiene un usuario por su ID.

- **Estudiantes**
    - `GET /api/v1/estudiantes/getEstudiantes/{id}`: Obtiene un estudiante por su ID.
    - `POST /api/v1/estudiantes/postEstudiantes`: Guarda un nuevo estudiante.

## Estructura del Proyecto

El proyecto sigue la estructura estándar de un proyecto Spring Boot:
