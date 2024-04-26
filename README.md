# Microservicio de Control de Credenciales (ms-credential-control)

Este microservicio es responsable de gestionar credenciales y tokens de autenticación para la comunicación entre diferentes microservicios. Permite la generación de tokens JWT y la validación de credenciales.

## Requisitos Previos
Para ejecutar el microservicio, necesitarás:
- Java 17 o superior
- Apache Maven para compilación

## Collection Postman
- [PROJECT02.postman_collection.json](PROJECT02.postman_collection.json)
- [Desarrollo.postman_environment.json](Desarrollo.postman_environment.json)

## Configuración
El microservicio utiliza valores de entorno para la configuración. Asegúrate de definir los siguientes valores antes de ejecutar el microservicio:
- `JWT_SECRET`: Clave secreta para la firma de tokens JWT.
- `JWT_EXPIRATION`: Duración de los tokens JWT en milisegundos.


## Instrucciones de Ejecución
Para ejecutar el microservicio localmente, sigue estos pasos:

1. **Clona el repositorio**:
```
mvn clean package
```

2. **Ejecuta el microservicio**:
```
mvn spring-boot:run
```

