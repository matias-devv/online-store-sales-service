# 🧾 Sales Service
 
 <br>
 
## 📌 Descripción General
El Servicio de Ventas es el servicio de negocio central del sistema.
Es responsable de orquestar el flujo completo de ventas coordinando múltiples microservicios y persistiendo la transacción de negocio final.
Este servicio no gestiona usuarios, carritos de compras, productos, stock o pagos directamente.
En su lugar, valida, agrega y consolida datos provenientes de otros servicios para generar un registro de venta consistente e inmutable.

<br> 

## 🏗 Arquitectura
Este servicio sigue una arquitectura MVC clásica, adaptada a un entorno de microservicios:

- **Controller**: Endpoints REST (Spring Web MVC)
- **Service**: Lógica de negocio y orquestación
- **Repository**: Capa de persistencia (JPA / MySQL)
- **DTOs**: Separación clara entre modelos internos y contratos externos

El servicio actúa como un orquestador, no como propietario de datos de dominios externos.

<br> 

## 🔄 Flujo de Orquestación (Responsabilidad Principal)
El proceso de creación de ventas sigue esta secuencia:

1. **Validación de usuario**


- Verifica que el usuario existe
- Confirma la propiedad del carrito de compras

<br> 

2. **Validación del carrito de compras**


- Asegura que el carrito existe
- Recupera el precio total (calculado exclusivamente por el Servicio de Carritos de Compras)

<br> 

3. **Validación de productos**


- Recupera información completa del producto del Servicio de Productos
- No modifica el stock
- Las cantidades de productos se toman del carrito de compras

<br> 

4. **Creación de venta**


- Persiste la entidad Venta como la transacción principal

<br> 

5. **Generación de Detalle de Venta**


- Crea una instantánea de la venta
- Copia datos del producto, cantidades y precios en el momento de la compra

Este diseño garantiza la consistencia de datos y precisión histórica, lo cual es especialmente importante para propósitos de auditoría y legales.

<br>

## 🧩 Modelo Sale & SaleDetail

**Sale (Venta)**

- Representa la transacción de negocio principal vinculada al usuario y al carrito de compras.
  

**SaleDetail (Detalle de Venta)**

- Entidad interna (mismo repositorio y base de datos) que actúa como una instantánea de la transacción.
- Duplica intencionalmente los datos del producto para evitar dependencia futura de servicios externos.

Esta es una decisión de diseño deliberada.

<br> 

## 🔗 Comunicación entre Servicios
Toda la comunicación con servicios externos se realiza a través de **OpenFeign**:

- ```users-service```
  
- ```shopping-carts-service```
  
- ```products-service```

Sin RestTemplate, sin WebClient. Los contratos son explícitos y fuertemente tipados.

<br> 

## 🛡 Resiliencia y Tolerancia a Fallos
Este servicio usa **Resilience4j** con:


- CircuitBreaker

- Estrategia de Retry

Enfoque:

- Fallo rápido para dependencias críticas (Productos, Carritos de Compras)

- Excepciones explícitas cuando un servicio dependiente no está disponible

- Comportamiento de fallback controlado durante los pasos de validación

Esto evita fallos silenciosos y previene que se creen ventas inconsistentes.

<br> 

## ▶️ Cómo Ejecutar el Proyecto

### ✅ Prerequisitos

- Java 17
  
- Maven
  
- MySQL

<br> 

## 🔗 Servicios Requeridos (Deben estar corriendo)


Este servicio depende de los siguientes microservicios, que deben estar ejecutándose antes de iniciar sales-service:

- **Servidor Eureka** - http://localhost:8761
  
- **API Gateway** - Enruta todas las peticiones externas ( opcional )
  
- **Servicio de Usuarios** (users-service) - Usado para validar la existencia del usuario y propiedad
  
- **Servicio de Productos** (products-service) - Usado para recuperar información de productos
  
- **Servicio de Carritos de Compras** (carts-service) - Usado para validar carritos de compras y calcular el precio total

⚠️ Si alguno de estos servicios no está disponible, se activarán los mecanismos de resiliencia (Circuit Breaker + Retry).

<br> 

## 🗄️ Configuración de Base de Datos
Asegúrate de que las siguientes bases de datos existan antes de iniciar la aplicación:

- sales_service
  
- products_service
  
- users_service
  
- shopping_carts_service

## ▶️ Ejecutar la Aplicación
Desde el directorio raíz del proyecto:

```
mvn spring-boot:run
```

El servicio se iniciará en:

```
http://localhost:8086
```

<br>

## 🌐 Acceso al Servicio


### Usando API Gateway (Recomendado)
```
http://localhost:8080/sales-service/sale
```

Este es el enfoque recomendado en un entorno completo de microservicios.

<br>

### Modo Independiente (Sin Gateway)
```
http://localhost:8086/sale
```

Útil para desarrollo local o pruebas aisladas.

<br>

## 📘 Documentación de la API (Swagger)

Una vez que el servicio esté en ejecución:

```
http://localhost:8086/swagger-ui.html
```

Todos los endpoints, esquemas de petición/respuesta y ejemplos son generados automáticamente usando Springdoc OpenAPI.

<br>

## 📚 Lo Que Este Servicio Demuestra

- Practical application of MVC in microservices
- Business orchestration across multiple services
- Clear separation of responsibilities
- Snapshot-based consistency model
- Resilient inter-service communication
- Clean API documentation

A pesar de ser conceptualmente simple, este servicio consolida principios fundamentales de backend que escalan correctamente en sistemas distribuidos.

<br>

## 🚀 Posibles Mejoras

- Manejo global de excepciones
- Autenticación y autorización
- Integración de gestión de stock
- Pruebas unitarias y de integración
- Mocks de Feign para pruebas aisladas

<br>

## 🔑 Posicionamiento Técnico Final

- El Servicio de Ventas es el Servicio de Negocio Central del sistema.
- Posee la lógica del dominio de ventas y coordina servicios externos sin violar los límites del dominio.
