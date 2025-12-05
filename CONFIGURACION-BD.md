# Configuración de Base de Datos

Este microservicio soporta dos perfiles de configuración:

## Perfiles Disponibles

### 1. Desarrollo (dev) - H2 en memoria
- Base de datos en memoria
- Datos iniciales desde `import.sql`
- Consola H2 habilitada en `/h2-console`
- DDL: create-drop (se reinicia en cada ejecución)

### 2. Producción (prod) - MySQL
- Base de datos MySQL persistente
- Sin datos iniciales automáticos
- DDL: update (mantiene datos entre ejecuciones)

## Cambiar entre perfiles

### Opción 1: Modificar application.properties
Edita `src/main/resources/application.properties`:
```properties
spring.profiles.active=dev   # Para desarrollo
spring.profiles.active=prod  # Para producción
```

### Opción 2: Variable de entorno
```bash
# Windows
set SPRING_PROFILES_ACTIVE=prod

# Linux/Mac
export SPRING_PROFILES_ACTIVE=prod
```

### Opción 3: Argumento al ejecutar
```bash
java -jar microservicio-division.jar --spring.profiles.active=prod

# Con Maven
mvn spring-boot:run -Dspring-boot.run.arguments=--spring.profiles.active=prod
```

## Configuración de MySQL (Producción)

### 1. Crear la base de datos
```sql
CREATE DATABASE division_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. Configurar credenciales
Edita `src/main/resources/application-prod.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/division_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_PASSWORD
```

### 3. Verificar driver MySQL
El driver ya está incluido en `pom.xml`:
```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>
```

## URLs de acceso

### Desarrollo (H2)
- API: http://localhost:8081
- Consola H2: http://localhost:8081/h2-console
  - JDBC URL: `jdbc:h2:mem:divisiondb`
  - Username: `sa`
  - Password: (vacío)

### Producción (MySQL)
- API: http://localhost:8081
- MySQL: localhost:3306/division_db

## Datos iniciales

### En desarrollo (H2)
Los datos iniciales se cargan desde `src/main/resources/import.sql`

### En producción (MySQL)
Debes insertar los datos manualmente o mediante un script de migración.

Ejemplo:
```sql
INSERT INTO division (nombre, activo) VALUES ('DIVISION DE TECNOLOGIAS', true);
INSERT INTO programa_educativa (programa, activo, division_id)
VALUES ('Ing. Desarrollo y Gestión de Software', true, 1);
```
