# Login Básico - API Camiones y Conductores (Empresa XYZ)

API REST para que Empresa XYZ (transporte masivo de alimentos) gestione el registro de camiones,
conductores, y su asociación. Construida con **Spring Boot + Spring Security (HTTP Basic Auth) + PostgreSQL**.

## Requisitos

- Java 21
- Maven 3.9+
- PostgreSQL corriendo localmente (o accesible por red)

## Configuración de base de datos

Crear la base de datos antes de levantar la app:

```sql
CREATE DATABASE login_basico;
```

La conexión se configura por variables de entorno (con valores por defecto en `application.properties`):

| Variable      | Default        |
|---------------|----------------|
| `DB_NAME`     | `login_basico` |
| `DB_USERNAME` | `postgres`     |
| `DB_PASSWORD` | `postgres`     |
| `SERVER_PORT` | `8080`         |

## Ejecutar

```bash
mvn spring-boot:run
```

Al arrancar por primera vez (base de datos vacía de usuarios), se crean automáticamente dos usuarios:

| Username     | Password         | Rol        |
|--------------|------------------|------------|
| `admin`      | `admin123`       | ADMIN      |
| `supervisor` | `supervisor123`  | SUPERVISOR |

> Cambiar estas contraseñas antes de cualquier uso más allá de desarrollo/pruebas.

## Autenticación

Toda la API exige **HTTP Basic Auth** (`Authorization: Basic base64(usuario:password)`). No existen
endpoints públicos.

Ejemplo con `curl`:

```bash
curl -u admin:admin123 http://localhost:8080/api/camiones
```

## Roles y permisos

| Acción                              | ADMIN | SUPERVISOR |
|--------------------------------------|:-----:|:----------:|
| Registrar camiones                   |  Sí   |    No      |
| Registrar conductores                |  Sí   |    No      |
| Consultar camiones / conductores     |  Sí   |    Sí      |
| Asociar conductor a camión           |  Sí   |    Sí      |

## Endpoints implementados

### Camiones (`/api/camiones`)

| Método | Ruta               | Rol requerido |
|--------|--------------------|---------------|
| GET    | `/api/camiones`     | Autenticado   |
| GET    | `/api/camiones/{id}`| Autenticado   |
| POST   | `/api/camiones`     | ADMIN         |
| PUT    | `/api/camiones/{id}`| ADMIN         |
| DELETE | `/api/camiones/{id}`| ADMIN         |

Body de `POST`/`PUT`:

```json
{
  "placa": "ABC123",
  "tipoVehiculo": "Furgon refrigerado"
}
```

### Conductores (`/api/conductores`)

| Método | Ruta                   | Rol requerido |
|--------|------------------------|---------------|
| GET    | `/api/conductores`      | Autenticado   |
| GET    | `/api/conductores/{id}` | Autenticado   |
| POST   | `/api/conductores`      | ADMIN         |
| PUT    | `/api/conductores/{id}` | ADMIN         |
| DELETE | `/api/conductores/{id}` | ADMIN         |

Body de `POST`/`PUT`:

```json
{
  "nombre": "Juan Perez",
  "documento": "123456789",
  "licencia": "C2-998877"
}
```

## Estructura del proyecto

```
src/main/java/com/empresaxyz/loginbasico/
├── model/          Entidades JPA (Usuario, Camion, Conductor, Rol)
├── repository/     Repositorios Spring Data
├── security/       Configuración de Spring Security + UserDetailsService
├── service/        Lógica de negocio
├── controller/      Endpoints REST
├── dto/            Request/Response
├── exception/       Manejo global de errores
└── config/         Seed de usuarios iniciales
```

## Pendiente

Ver [PENDIENTE.md](PENDIENTE.md) para el detalle de lo que falta por implementar (módulo de
asociación conductor-camión, pruebas, etc.).
