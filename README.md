# 🏥 Turnos Médicos — Backend - Spring Boot + Auth0

Sistema de gestión de turnos médicos con autenticación y autorización basada en roles mediante Auth0.

**Integrante:** Magali Cobos

---

## 📋 Descripción

API REST desarrollada con Spring Boot que implementa un sistema de turnos médicos con tres roles:

- **Médico (admin):** puede crear nuevos turnos.
- **Recepcionista:** puede ver todos los turnos y cancelarlos.
- **Paciente (usuario):** puede ver la grilla pública y reservar un turno.

---

## 🛠️ Tecnologías utilizadas

| Tecnología | Versión |
|---|---|
| Java | 17 |
| Spring Boot | 3.x |
| Spring Security (OAuth2 Resource Server) | 3.x |
| Spring Data JPA | 3.x |
| MySQL | 8.x |
| Auth0 (JWT / JWKS) | — |
| Maven | 3.x |

---

## ✅ Requisitos previos

Antes de ejecutar el proyecto, asegurate de tener instalado:

- Java 17+
- Maven 3.8+
- MySQL 8+
- Una cuenta en [Auth0](https://auth0.com/)

---

## 🚀 Instalación y ejecución

### 1. Clonar el repositorio

```bash
git clone https://github.com/MaguiiCobos/Backend-TurnosMedicos.git
cd backend-turnos-medicos
```

### 2. Configurar las variables de entorno

Crear el archivo `src/main/resources/application.properties` con el siguiente contenido:

```
spring.application.name=turnos_medicos

# Configuración de MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/turnosmedicos?zeroDateTimeBehavior=convertToNull&serverTimezone=America/Argentina/Mendoza
spring.datasource.username=TU_USUARIO_MYSQL
spring.datasource.password=TU_PASSWORD_MYSQL
spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver

#  Configuración de JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# Configuración de Auth0
auth0.issuer=https://TU_DOMINIO.us.auth0.com/
auth0.domain=TU_DOMINIO.us.auth0.com
auth0.audience=https://api.turnosmedicos.com
auth0.clientId=TU_CLIENT_ID
auth0.clientSecret=TU_CLIENT_SECRET
auth0.recepcionistaRoleId=TU_ROLE_ID_DE_RECEPCIONISTA

# CORS
cors.allowed-origins=http://localhost:3000
```

### 3. Instala dependencias

```bash
./mvnw clean install
```

### 4. Ejecutar el proyecto

```bash
./mvnw spring-boot:run
```

El servidor queda disponible en `http://localhost:8080`.

---

## 🗄️ Configuración de la base de datos

Crear la base de datos en MySQL:

```sql
CREATE DATABASE turnosmedicos;
```

Las tablas se crean automáticamente al iniciar la aplicación gracias a Hibernate (`ddl-auto=update`).

---

## ⚙️ Configuración de Auth0

### 1. Crear una API en Auth0
1. Ir a **Auth0 Dashboard > Applications > APIs > Create API**
2. Completar:
   - **Name:** `Turnos Medicos API`
   - **Identifier:** `https://api.turnosmedicos.com`
3. Guardar y copiar el **Identifier** (es el `audience`)

### 2. Crear una Application en Auth0
1. Ir a **Applications > Create Application > Single Page Application**
2. En **Allowed Callback URLs:** `http://localhost:3000`
3. En **Allowed Logout URLs:** `http://localhost:3000`
4. En **Allowed Web Origins:** `http://localhost:3000`
5. Copiar el **Domain** y el **Client ID**

### 3. Crear los roles
1. Ir a **User Management > Roles > Create Role**
2. Crear tres roles: `admin`, `recepcionista`, `usuario`
3. Asignar el rol correspondiente a cada usuario desde **User Management > Users**

### 4. Configurar la Action para agregar roles al token
1. Ir a **Actions > Tiggers > Post Login > Add Action > Build Custom**
2. Pegar el siguiente código y hacer Deploy:

```javascript
exports.onExecutePostLogin = async (event, api) => {
  const namespace = 'https://turnos-medicos.com/roles';
  const assignedRoles = event.authorization?.roles || ['usuario'];

  api.idToken.setCustomClaim(namespace, assignedRoles);
  api.accessToken.setCustomClaim(namespace, assignedRoles);

  api.idToken.setCustomClaim('email', event.user.email);
  api.accessToken.setCustomClaim('email', event.user.email);
};
```

### 5. Crear un Machine-to-Machine Application (para gestión de recepcionistas)
1. Ir a **Applications > Create Application > Machine to Machine**
2. Autorizarla para la **Auth0 Management API** con los permisos:
   - `create:users`
   - `update:users`
   - `create:role_members`
3. Copiar el **Client ID** y **Client Secret**

---

## 🔐 Rutas de la API

### Públicas (sin autenticación)

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/public/turnos` | Lista los turnos disponibles |
| GET | `/api/public/health` | Health check del servidor |

### Protegidas (requieren token JWT válido)

| Método | Endpoint | Rol requerido | Descripción |
|---|---|---|---|
| GET | `/api/me` | Cualquier rol | Sincroniza el usuario con la BD |
| POST | `/api/private/turnos` | `admin` | Crea un nuevo turno |
| GET | `/api/private/turnos` | `admin`, `recepcionista` | Lista todos los turnos |
| PUT | `/api/private/turnos/{id}/reservar` | `usuario` | Reserva un turno |
| GET | `/api/private/turnos/mis-citas` | `usuario` | Lista los turnos del paciente |
| PUT | `/api/private/turnos/{id}/estado` | `admin`, `recepcionista` | Cambia el estado de un turno |

---

## 📁 Estructura del proyecto

```
src/main/java/com/example/turnos_medicos/
├── config/          # SecurityConfig, Auth0Properties
├── controller/      # ApiController, TurnoController, UsuarioController, RecepcionistaController
├── dto/             # TurnoDTO, EstadoTurnoUpdateDTO, CreateRecepcionistaRequestDTO, CreateTurnoRequestDTO, UserRecepcionista
├── entity/          # Turno, Persona, Medico
├── exceptions/      # GlobalExceptionHandler
├── repository/      # TurnoRepository, PersonaRepository, MedicoRepository
└── service/         # TurnoService, PersonaSyncService, Auth0Service, MedicoService
```
