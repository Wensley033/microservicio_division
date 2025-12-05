# Changelog - Microservicio División

## Mejoras Implementadas

### ✅ 1. Validación de Nombre Único para Divisiones

**Archivos modificados:**
- [DivisionRepository.java](src/main/java/mx/edu/uteq/idgs12/microservio_division/repository/DivisionRepository.java)
  - Agregado: `existsByNombreIgnoreCaseAndIdNot(String nombre, Long id)`

- [DivisionService.java](src/main/java/mx/edu/uteq/idgs12/microservio_division/service/DivisionService.java:49-52)
  - Validación en `create()`: Verifica nombre único antes de crear
  - Validación en `update()`: Verifica nombre único excluyendo ID actual

**Comportamiento:**
- Al intentar crear una división con nombre duplicado → `400 Bad Request`
- Al intentar actualizar con nombre que ya existe → `400 Bad Request`
- Validación case-insensitive (ignora mayúsculas/minúsculas)

---

### ✅ 2. Búsqueda de Divisiones por Nombre

**Archivos creados/modificados:**
- [DivisionService.java](src/main/java/mx/edu/uteq/idgs12/microservio_division/service/DivisionService.java:44-50)
  - Método: `findByNombre(String nombre)`

- [DivisionController.java](src/main/java/mx/edu/uteq/idgs12/microservio_division/controller/DivisionController.java:51-56)
  - Endpoint: `GET /divisiones/search?nombre={nombre}`

**Características:**
- Búsqueda parcial (contiene)
- Case-insensitive
- Devuelve lista de coincidencias

---

### ✅ 3. Paginación en Listados

**Archivos modificados:**
- [DivisionRepository.java](src/main/java/mx/edu/uteq/idgs12/microservio_division/repository/DivisionRepository.java)
  - Agregados métodos paginados con `Pageable`

- [DivisionService.java](src/main/java/mx/edu/uteq/idgs12/microservio_division/service/DivisionService.java:52-71)
  - `findAllPaginated(int page, int size, String sortBy)`
  - `findAllActivasPaginated(int page, int size, String sortBy)`
  - `findByNombrePaginated(String nombre, int page, int size, String sortBy)`

- [DivisionController.java](src/main/java/mx/edu/uteq/idgs12/microservio_division/controller/DivisionController.java:58-87)
  - `GET /divisiones/paginated`
  - `GET /divisiones/activas/paginated`
  - `GET /divisiones/search/paginated`

**Parámetros:**
- `page`: Número de página (default: 0)
- `size`: Tamaño (default: 10)
- `sortBy`: Campo de orden (default: "id")

---

### ✅ 4. Integración de CoordinadorEntity

**Archivos creados:**
- [CoordinadorRepository.java](src/main/java/mx/edu/uteq/idgs12/microservio_division/repository/CoordinadorRepository.java)
- [CoordinadorService.java](src/main/java/mx/edu/uteq/idgs12/microservio_division/service/CoordinadorService.java)
- [CoordinadorController.java](src/main/java/mx/edu/uteq/idgs12/microservio_division/controller/CoordinadorController.java)
- [CoordinadorCreateDto.java](src/main/java/mx/edu/uteq/idgs12/microservio_division/dto/CoordinadorCreateDto.java)
- [CoordinadorUpdateDto.java](src/main/java/mx/edu/uteq/idgs12/microservio_division/dto/CoordinadorUpdateDto.java)
- [CoordinadorViewDto.java](src/main/java/mx/edu/uteq/idgs12/microservio_division/dto/CoordinadorViewDto.java)

**Endpoints implementados:**
- `GET /coordinadores` - Listar todos
- `GET /coordinadores/activos` - Listar activos
- `GET /coordinadores/division/{divisionId}` - Por división
- `GET /coordinadores/{id}` - Por ID
- `GET /coordinadores/paginated` - Con paginación
- `GET /coordinadores/activos/paginated` - Activos con paginación
- `GET /coordinadores/division/{divisionId}/paginated` - Por división con paginación
- `POST /coordinadores` - Crear
- `PUT /coordinadores/{id}` - Actualizar
- `DELETE /coordinadores/{id}` - Soft delete
- `PATCH /coordinadores/{id}/toggle-status` - Toggle estado

**Validaciones:**
- Correo único (case-insensitive)
- Formato de email válido
- División debe existir
- Campos obligatorios: nombre, apellido, correo, divisionId

---

### ✅ 5. Configuración MySQL para Producción

**Archivos creados:**
- [application-dev.properties](src/main/resources/application-dev.properties)
  - Perfil de desarrollo con H2

- [application-prod.properties](src/main/resources/application-prod.properties)
  - Perfil de producción con MySQL

- [CONFIGURACION-BD.md](CONFIGURACION-BD.md)
  - Guía completa de configuración de base de datos

**Archivos modificados:**
- [application.properties](src/main/resources/application.properties)
  - Configurado para usar perfiles
  - Default: `spring.profiles.active=dev`

**Características:**
- Cambio fácil entre H2 y MySQL
- Scripts SQL documentados
- Guía de migración incluida

---

### ✅ 6. Tests Unitarios para Servicios

**Archivos creados:**
- [DivisionServiceTest.java](src/test/java/mx/edu/uteq/idgs12/microservio_division/service/DivisionServiceTest.java)
  - 12 tests unitarios
  - Cobertura completa de DivisionService

- [CoordinadorServiceTest.java](src/test/java/mx/edu/uteq/idgs12/microservio_division/service/CoordinadorServiceTest.java)
  - 11 tests unitarios
  - Cobertura completa de CoordinadorService

**Tests incluidos:**
- ✅ findAll / findAllActivos
- ✅ findById
- ✅ findByNombre / findByDivision
- ✅ create (éxito y validaciones)
- ✅ update (éxito, no encontrado, duplicados)
- ✅ delete (soft delete)
- ✅ toggleStatus
- ✅ Métodos paginados

---

### ✅ 7. Tests de Integración para Controladores

**Archivos creados:**
- [DivisionControllerTest.java](src/test/java/mx/edu/uteq/idgs12/microservio_division/controller/DivisionControllerTest.java)
  - 13 tests de integración
  - Pruebas de endpoints HTTP

- [CoordinadorControllerTest.java](src/test/java/mx/edu/uteq/idgs12/microservio_division/controller/CoordinadorControllerTest.java)
  - 11 tests de integración
  - Pruebas de endpoints HTTP

**Tecnologías:**
- `@WebMvcTest` - Testing de capa web
- `@MockitoBean` - Mocking de servicios
- MockMvc - Simulación de peticiones HTTP

**Tests incluidos:**
- ✅ GET endpoints (200, 404)
- ✅ POST (201, 400 validación)
- ✅ PUT (200, 404)
- ✅ DELETE (204, 404)
- ✅ PATCH (200, 404)
- ✅ Paginación

---

## Archivos Adicionales Creados

### Documentación
- [README.md](README.md) - Documentación principal del microservicio
- [ENDPOINTS.md](ENDPOINTS.md) - Guía completa de endpoints con ejemplos
- [CONFIGURACION-BD.md](CONFIGURACION-BD.md) - Guía de configuración de base de datos
- [CHANGELOG.md](CHANGELOG.md) - Este archivo

### Manejo de Errores
- [GlobalExceptionHandler.java](src/main/java/mx/edu/uteq/idgs12/microservio_division/exception/GlobalExceptionHandler.java)
  - Manejo centralizado de excepciones
  - Respuestas de error estandarizadas
  - Captura de validaciones

---

## Resumen de Mejoras

### Antes
- ❌ Sin validación de nombres únicos
- ❌ Sin búsqueda por nombre
- ❌ Sin paginación
- ❌ CoordinadorEntity sin exponer
- ❌ Solo H2, sin configuración MySQL
- ❌ Sin tests

### Después
- ✅ Validación completa de unicidad
- ✅ Búsqueda flexible por nombre
- ✅ Paginación en todos los listados
- ✅ CRUD completo de coordinadores
- ✅ Soporte MySQL para producción
- ✅ 23 tests unitarios
- ✅ 24 tests de integración
- ✅ Documentación completa
- ✅ Manejo profesional de errores

---

## Cómo Ejecutar

### Tests
```bash
mvn test
```

### Desarrollo (H2)
```bash
mvn spring-boot:run
```

### Producción (MySQL)
```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--spring.profiles.active=prod
```

---

## Integración con Ecosistema

Este microservicio está listo para integrarse con:
- ✅ **eureka_server** - Registro de servicios
- ✅ **api_gateway** - Enrutamiento
- ✅ **microservicio-profesor** - Validación de divisionId
- ✅ **microservicio-alumno** - Validación de programaEducativoId
- ✅ **sistema-educativo-frontend** - Consumo desde Next.js

---

## Próximos Pasos Sugeridos

1. **Seguridad**
   - Agregar Spring Security
   - Implementar JWT authentication
   - Proteger endpoints sensibles

2. **Documentación API**
   - Integrar Swagger/OpenAPI
   - Generar documentación automática

3. **Monitoreo**
   - Configurar Spring Boot Actuator endpoints
   - Implementar métricas personalizadas
   - Integrar con sistema de logs centralizado

4. **Cache**
   - Agregar Redis para cache de divisiones
   - Optimizar consultas frecuentes

5. **Auditoría**
   - Implementar JPA Auditing
   - Registrar createdAt, updatedAt, createdBy, updatedBy
