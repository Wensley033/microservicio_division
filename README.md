# Microservicio División

Microservicio para la gestión de divisiones académicas, programas educativos y coordinadores.

## Características

### Divisiones
- ✅ CRUD completo de divisiones
- ✅ Soft delete (desactivar en lugar de eliminar)
- ✅ Validación de nombres únicos
- ✅ Búsqueda por nombre
- ✅ Paginación en listados
- ✅ Toggle de estado activo/inactivo
- ✅ Gestión de programas educativos asociados

### Coordinadores
- ✅ CRUD completo de coordinadores
- ✅ Soft delete
- ✅ Validación de correos únicos
- ✅ Filtrado por división
- ✅ Paginación en listados
- ✅ Toggle de estado activo/inactivo

### Programas Educativos
- ✅ Asociados a divisiones (relación 1-a-muchos)
- ✅ Cascada: al desactivar división, se desactivan programas

## Tecnologías

- **Spring Boot** 3.5.6
- **Java** 21
- **Spring Data JPA**
- **H2** (desarrollo) / **MySQL** (producción)
- **Eureka Client** (registro en Service Registry)
- **Lombok**
- **JUnit 5** + **Mockito** (testing)

## Configuración

### Puerto
```
8081
```

### Nombre del servicio (Eureka)
```
microservicio-division
```

### Perfiles

#### Desarrollo (dev) - H2
```properties
spring.profiles.active=dev
```
- Base de datos en memoria
- Datos iniciales desde `import.sql`
- Consola H2: http://localhost:8081/h2-console

#### Producción (prod) - MySQL
```properties
spring.profiles.active=prod
```
- Base de datos MySQL persistente
- Configurar credenciales en `application-prod.properties`

Ver [CONFIGURACION-BD.md](CONFIGURACION-BD.md) para más detalles.

## Endpoints

### Divisiones (`/divisiones`)

#### Listar
- `GET /divisiones` - Todas las divisiones
- `GET /divisiones/activas` - Solo activas
- `GET /divisiones/search?nombre={nombre}` - Buscar por nombre

#### Paginados
- `GET /divisiones/paginated?page=0&size=10&sortBy=id` - Todas con paginación
- `GET /divisiones/activas/paginated?page=0&size=10&sortBy=id` - Activas con paginación
- `GET /divisiones/search/paginated?nombre={nombre}&page=0&size=10&sortBy=id` - Búsqueda con paginación

#### CRUD
- `GET /divisiones/{id}` - Obtener por ID
- `POST /divisiones` - Crear nueva división
- `PUT /divisiones/{id}` - Actualizar división
- `DELETE /divisiones/{id}` - Desactivar división (soft delete)
- `PATCH /divisiones/{id}/toggle-status` - Activar/Desactivar

### Coordinadores (`/coordinadores`)

#### Listar
- `GET /coordinadores` - Todos los coordinadores
- `GET /coordinadores/activos` - Solo activos
- `GET /coordinadores/division/{divisionId}` - Por división

#### Paginados
- `GET /coordinadores/paginated?page=0&size=10&sortBy=id` - Todos con paginación
- `GET /coordinadores/activos/paginated?page=0&size=10&sortBy=id` - Activos con paginación
- `GET /coordinadores/division/{divisionId}/paginated?page=0&size=10&sortBy=id` - Por división con paginación

#### CRUD
- `GET /coordinadores/{id}` - Obtener por ID
- `POST /coordinadores` - Crear nuevo coordinador
- `PUT /coordinadores/{id}` - Actualizar coordinador
- `DELETE /coordinadores/{id}` - Desactivar coordinador (soft delete)
- `PATCH /coordinadores/{id}/toggle-status` - Activar/Desactivar

## DTOs

### División
- **DivisionCreateDto**: Crear división
  - `nombre`: String (obligatorio, único)
  - `programasEducativos`: Lista de ProgramaEducativoDto (opcional)

- **DivisionUpdateDto**: Actualizar división
  - `nombre`: String (obligatorio, único)
  - `programasEducativos`: Lista de ProgramaEducativoDto (opcional)

- **DivisionToViewListDto**: Respuesta
  - `divisionId`: Long
  - `nombre`: String
  - `activo`: boolean
  - `programaEducativa`: Lista de Strings
  - `numeroProgramas`: int

### Coordinador
- **CoordinadorCreateDto**: Crear coordinador
  - `nombre`: String (obligatorio)
  - `apellido`: String (obligatorio)
  - `correo`: String (obligatorio, único, email válido)
  - `telefono`: String (opcional)
  - `divisionId`: Long (obligatorio)

- **CoordinadorUpdateDto**: Actualizar coordinador
  - `nombre`: String (obligatorio)
  - `apellido`: String (obligatorio)
  - `correo`: String (obligatorio, único, email válido)
  - `telefono`: String (opcional)
  - `divisionId`: Long (obligatorio)
  - `activo`: boolean

- **CoordinadorViewDto**: Respuesta
  - `id`: Long
  - `nombre`: String
  - `apellido`: String
  - `correo`: String
  - `telefono`: String
  - `divisionId`: Long
  - `divisionNombre`: String
  - `activo`: boolean

## Validaciones

### División
- ✅ Nombre no vacío
- ✅ Nombre único (case insensitive)
- ✅ Cascada: al desactivar división, desactiva programas

### Coordinador
- ✅ Nombre y apellido no vacíos
- ✅ Correo no vacío y formato válido
- ✅ Correo único (case insensitive)
- ✅ División debe existir

## Manejo de Errores

El microservicio incluye un `GlobalExceptionHandler` que captura:
- `IllegalArgumentException` → 400 Bad Request
- `MethodArgumentNotValidException` → 400 Bad Request (errores de validación)
- `Exception` genérica → 500 Internal Server Error

Formato de respuesta de error:
```json
{
  "timestamp": "2025-12-04T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Ya existe una división con el nombre: División de Tecnologías"
}
```

## Ejecución

### Con Maven
```bash
# Desarrollo (H2)
mvn spring-boot:run

# Producción (MySQL)
mvn spring-boot:run -Dspring-boot.run.arguments=--spring.profiles.active=prod
```

### Con JAR
```bash
# Compilar
mvn clean package

# Ejecutar en desarrollo
java -jar target/microservio_division-0.0.1-SNAPSHOT.jar

# Ejecutar en producción
java -jar target/microservio_division-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

## Testing

### Ejecutar todos los tests
```bash
mvn test
```

### Tests incluidos
- **Tests unitarios** (servicios):
  - `DivisionServiceTest`
  - `CoordinadorServiceTest`

- **Tests de integración** (controladores):
  - `DivisionControllerTest`
  - `CoordinadorControllerTest`

## Datos Iniciales (Desarrollo)

El archivo `import.sql` contiene datos de ejemplo:
- 1 División: "DIVISION DE TECNOLOGIAS"
- 2 Programas: "Ing. Desarrollo y Gestión de Software", "Ing. Redes y Ciberseguridad"

## Integración con otros microservicios

Este microservicio es consumido por:
- **microservicio-profesor**: Valida `divisionId` al crear/actualizar profesores
- **microservicio-alumno**: Valida `programaEducativoId` al asignar programas a alumnos

## Estructura del Proyecto

```
src/main/java/
├── controller/
│   ├── DivisionController.java
│   └── CoordinadorController.java
├── service/
│   ├── DivisionService.java
│   └── CoordinadorService.java
├── repository/
│   ├── DivisionRepository.java
│   └── CoordinadorRepository.java
├── entity/
│   ├── Division.java
│   ├── ProgramaEducativa.java
│   └── CoordinadorEntity.java
├── dto/
│   ├── DivisionCreateDto.java
│   ├── DivisionUpdateDto.java
│   ├── DivisionToViewListDto.java
│   ├── ProgramaEducativoDto.java
│   ├── CoordinadorCreateDto.java
│   ├── CoordinadorUpdateDto.java
│   └── CoordinadorViewDto.java
└── exception/
    └── GlobalExceptionHandler.java
```

## Autor

Universidad Tecnológica de Querétaro - IDGS 12
