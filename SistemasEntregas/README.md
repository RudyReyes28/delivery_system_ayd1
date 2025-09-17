# 1. Limpiar y compilar todo
./mvnw clean install

# 2. Ejecutar la aplicación (para desarrollo)
./mvnw spring-boot:run

# O alternativamente, durante desarrollo:
## Solo compilar
```bash
./mvnw compile
```        
## Solo ejecutar tests

```bash
./mvnw test
```           

# Opciones de la propiedad de gestión del esquema de base de datos de JPA.
## spring.jpa.hibernate.ddl-auto=opcion
- Donde opcion puede ser:
    - **validate**: valida la estructura de la base de datos
    - **update**: actualiza la estructura de la base de datos
    - **create**: crea la base de datos
    - **create-drop**: crea la base de datos y la elimina al finalizar la sesión
