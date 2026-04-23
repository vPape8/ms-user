## ¿Qué hace este MR?
Inicializa el proyecto ms-user con Spring Boot 3, reemplazando
el repo vacío inicial (solo tenía README).

## Cambios principales
- Estructura base de Spring Boot con Maven
- Dependencias: spring-web, spring-data-jpa, driver PostgreSQL
- Configuración en application.yml (perfil dev)
- Entidad User + Repository + Service + Controller base

## Cómo probar
1. Tener JDK 21 y PostgreSQL corriendo
2. `./mvnw spring-boot:run`
3. GET http://localhost:8080/users → debe retornar 200

## Notas
Reemplaza completamente el contenido anterior del repo.