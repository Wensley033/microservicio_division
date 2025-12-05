# Endpoints - Microservicio División

Base URL: `http://localhost:8081`

## 🗂️ DIVISIONES

### 1. Listar todas las divisiones
```http
GET /divisiones
```

**Respuesta:**
```json
[
  {
    "divisionId": 1,
    "nombre": "DIVISION DE TECNOLOGIAS",
    "activo": true,
    "programaEducativa": [
      "Ing. Desarrollo y Gestión de Software",
      "Ing. Redes y Ciberseguridad"
    ],
    "numeroProgramas": 2
  }
]
```

### 2. Listar divisiones activas
```http
GET /divisiones/activas
```

### 3. Buscar división por ID
```http
GET /divisiones/{id}
```

Ejemplo: `GET /divisiones/1`

### 4. Buscar divisiones por nombre
```http
GET /divisiones/search?nombre=tecnologia
```

### 5. Listar divisiones con paginación
```http
GET /divisiones/paginated?page=0&size=10&sortBy=nombre
```

**Parámetros:**
- `page`: Número de página (default: 0)
- `size`: Tamaño de página (default: 10)
- `sortBy`: Campo de ordenamiento (default: id)

### 6. Listar divisiones activas con paginación
```http
GET /divisiones/activas/paginated?page=0&size=10&sortBy=nombre
```

### 7. Buscar divisiones por nombre con paginación
```http
GET /divisiones/search/paginated?nombre=tecnologia&page=0&size=10&sortBy=nombre
```

### 8. Crear nueva división
```http
POST /divisiones
Content-Type: application/json

{
  "nombre": "DIVISION DE SALUD",
  "programasEducativos": [
    {
      "nombre": "Enfermería"
    },
    {
      "nombre": "Terapia Física"
    }
  ]
}
```

### 9. Actualizar división
```http
PUT /divisiones/{id}
Content-Type: application/json

{
  "nombre": "DIVISION DE TECNOLOGIAS AVANZADAS",
  "programasEducativos": [
    {
      "id": 1,
      "nombre": "Ing. Desarrollo y Gestión de Software",
      "activo": true
    },
    {
      "id": 2,
      "nombre": "Ing. Redes y Ciberseguridad",
      "activo": true
    },
    {
      "nombre": "Ing. Inteligencia Artificial"
    }
  ]
}
```

### 10. Eliminar división (soft delete)
```http
DELETE /divisiones/{id}
```

### 11. Activar/Desactivar división
```http
PATCH /divisiones/{id}/toggle-status
```

---

## 👤 COORDINADORES

### 1. Listar todos los coordinadores
```http
GET /coordinadores
```

**Respuesta:**
```json
[
  {
    "id": 1,
    "nombre": "Juan",
    "apellido": "Pérez",
    "correo": "juan.perez@uteq.edu.mx",
    "telefono": "4421234567",
    "divisionId": 1,
    "divisionNombre": "DIVISION DE TECNOLOGIAS",
    "activo": true
  }
]
```

### 2. Listar coordinadores activos
```http
GET /coordinadores/activos
```

### 3. Listar coordinadores por división
```http
GET /coordinadores/division/{divisionId}
```

Ejemplo: `GET /coordinadores/division/1`

### 4. Buscar coordinador por ID
```http
GET /coordinadores/{id}
```

### 5. Listar coordinadores con paginación
```http
GET /coordinadores/paginated?page=0&size=10&sortBy=apellido
```

### 6. Listar coordinadores activos con paginación
```http
GET /coordinadores/activos/paginated?page=0&size=10&sortBy=apellido
```

### 7. Listar coordinadores por división con paginación
```http
GET /coordinadores/division/{divisionId}/paginated?page=0&size=10&sortBy=apellido
```

### 8. Crear nuevo coordinador
```http
POST /coordinadores
Content-Type: application/json

{
  "nombre": "María",
  "apellido": "González",
  "correo": "maria.gonzalez@uteq.edu.mx",
  "telefono": "4421234568",
  "divisionId": 1
}
```

### 9. Actualizar coordinador
```http
PUT /coordinadores/{id}
Content-Type: application/json

{
  "nombre": "María Elena",
  "apellido": "González López",
  "correo": "maria.gonzalez@uteq.edu.mx",
  "telefono": "4421234568",
  "divisionId": 1,
  "activo": true
}
```

### 10. Eliminar coordinador (soft delete)
```http
DELETE /coordinadores/{id}
```

### 11. Activar/Desactivar coordinador
```http
PATCH /coordinadores/{id}/toggle-status
```

---

## ⚠️ Errores Comunes

### División con nombre duplicado
```http
POST /divisiones
{
  "nombre": "DIVISION DE TECNOLOGIAS"
}
```

**Respuesta 400:**
```json
{
  "timestamp": "2025-12-04T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Ya existe una división con el nombre: DIVISION DE TECNOLOGIAS"
}
```

### Coordinador con correo duplicado
```http
POST /coordinadores
{
  "nombre": "Pedro",
  "apellido": "Ramírez",
  "correo": "juan.perez@uteq.edu.mx",
  "divisionId": 1
}
```

**Respuesta 400:**
```json
{
  "timestamp": "2025-12-04T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Ya existe un coordinador con el correo: juan.perez@uteq.edu.mx"
}
```

### División no encontrada
```http
GET /divisiones/999
```

**Respuesta 404:**
```http
HTTP/1.1 404 Not Found
```

### Validación de campos obligatorios
```http
POST /divisiones
{
  "nombre": ""
}
```

**Respuesta 400:**
```json
{
  "timestamp": "2025-12-04T10:30:00",
  "status": 400,
  "error": "Validation Failed",
  "errors": {
    "nombre": "El nombre de la división es obligatorio"
  }
}
```

---

## 📋 Flujo de Prueba Completo

### 1. Crear una división
```bash
curl -X POST http://localhost:8081/divisiones \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "DIVISION DE NEGOCIOS",
    "programasEducativos": [
      {"nombre": "Administración de Empresas"},
      {"nombre": "Contaduría Pública"}
    ]
  }'
```

### 2. Listar divisiones activas
```bash
curl http://localhost:8081/divisiones/activas
```

### 3. Crear coordinador para esa división
```bash
curl -X POST http://localhost:8081/coordinadores \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Carlos",
    "apellido": "Martínez",
    "correo": "carlos.martinez@uteq.edu.mx",
    "telefono": "4429876543",
    "divisionId": 2
  }'
```

### 4. Listar coordinadores de la división
```bash
curl http://localhost:8081/coordinadores/division/2
```

### 5. Desactivar división (soft delete)
```bash
curl -X DELETE http://localhost:8081/divisiones/2
```

### 6. Reactivar división
```bash
curl -X PATCH http://localhost:8081/divisiones/2/toggle-status
```

---

## 🔍 Acceso a H2 Console (Desarrollo)

URL: http://localhost:8081/h2-console

**Credenciales:**
- JDBC URL: `jdbc:h2:mem:divisiondb`
- Username: `sa`
- Password: (vacío)

**Consultas útiles:**
```sql
-- Ver todas las divisiones
SELECT * FROM division;

-- Ver todos los programas
SELECT * FROM programa_educativa;

-- Ver todos los coordinadores con división
SELECT c.*, d.nombre as division_nombre
FROM coordinadores c
JOIN division d ON c.division_id = d.id;
```
