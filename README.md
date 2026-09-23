# Servicio de consulta de precios
Aplicación Spring Boot para consultar el precio aplicable a un producto de una cadena en una fecha y hora determinadas.
La aplicación utiliza H2 en memoria y aplica la tarifa con mayor prioridad cuando existen varias tarifas vigentes simultáneamente.
## Tecnologías
- Java 21
- Spring Boot 4.1.1
- Spring Web MVC
- Spring Data JPA
- H2 Database
- Maven
- JUnit
- MockMvc
- AssertJ
## Requisitos
Para ejecutar el proyecto es necesario tener instalado:
- JDK 21
- Git, opcional
- No es necesario instalar Maven porque el proyecto incluye Maven Wrapper
  Comprobar la versión de Java:
```bash
java -version
```
## Ejecución de la aplicación
Desde la raíz del proyecto:
```bash
./mvnw spring-boot:run
```
La aplicación se iniciará en:
```text
http://localhost:8080
```
También se puede ejecutar desde IntelliJ IDEA mediante la clase:
```text
PruebaTecnicaPreciosApplication
```
## Ejecución de los tests
Ejecutar todos los tests:
```bash
./mvnw clean test
```
Ejecutar la verificación completa:
```bash
./mvnw clean verify
```
La fase `verify` compila el proyecto, ejecuta los tests y genera el JAR ejecutable.
## Generación del JAR
```bash
./mvnw clean package
```
El artefacto se generará en:
```text
target/pricing-service-0.0.1-SNAPSHOT.jar
```
Para ejecutarlo:
```bash
java -jar target/pricing-service-0.0.1-SNAPSHOT.jar
```
## Endpoint REST
### Consultar el precio aplicable
```http
GET /api/v1/prices
```
### Parámetros
| Parámetro | Tipo | Obligatorio | Descripción |
|---|---|---:|---|
| `applicationDate` | `LocalDateTime` | Sí | Fecha y hora de aplicación |
| `productId` | `Long` | Sí | Identificador del producto |
| `brandId` | `Long` | Sí | Identificador de la cadena o marca |
Formato de la fecha:
```text
yyyy-MM-dd'T'HH:mm:ss
```
### Ejemplo de petición
```bash
curl "http://localhost:8080/api/v1/prices?applicationDate=2020-06-14T16:00:00&productId=35455&brandId=1"
```
### Ejemplo de respuesta
```json
{
  "productId": 35455,
  "brandId": 1,
  "priceList": 2,
  "startDate": "2020-06-14T15:00:00",
  "endDate": "2020-06-14T18:30:00",
  "price": 25.45,
  "currency": "EUR"
}
```
## Códigos HTTP
| Código | Descripción |
|---:|---|
| `200 OK` | Se ha encontrado una tarifa aplicable |
| `400 Bad Request` | Los parámetros son incorrectos o incompletos |
| `404 Not Found` | No existe ninguna tarifa aplicable |
| `500 Internal Server Error` | Error inesperado del servidor |
## Reglas de negocio
Una tarifa es aplicable cuando la fecha consultada está dentro de su intervalo de vigencia, incluyendo ambos extremos:
```text
startDate <= applicationDate <= endDate
```
Si existen varias tarifas aplicables para el mismo producto y marca, se selecciona la de mayor prioridad.
En caso de empate de prioridad, se utiliza como criterio secundario el identificador de tarifa menor.
## Datos iniciales
La base de datos H2 se inicializa con los siguientes datos:
| Marca | Inicio | Fin | Tarifa | Producto | Prioridad | Precio | Moneda |
|---:|---|---|---:|---:|---:|---:|---|
| 1 | 2020-06-14 00:00:00 | 2020-12-31 23:59:59 | 1 | 35455 | 0 | 35.50 | EUR |
| 1 | 2020-06-14 15:00:00 | 2020-06-14 18:30:00 | 2 | 35455 | 1 | 25.45 | EUR |
| 1 | 2020-06-15 00:00:00 | 2020-06-15 11:00:00 | 3 | 35455 | 1 | 30.50 | EUR |
| 1 | 2020-06-15 16:00:00 | 2020-12-31 23:59:59 | 4 | 35455 | 1 | 38.95 | EUR |
## Casos funcionales cubiertos
Los tests de integración validan las peticiones exigidas por el enunciado:
| Fecha de consulta | Tarifa esperada | Precio esperado |
|---|---:|---:|
| 2020-06-14 10:00 | 1 | 35.50 EUR |
| 2020-06-14 16:00 | 2 | 25.45 EUR |
| 2020-06-14 21:00 | 1 | 35.50 EUR |
| 2020-06-15 10:00 | 3 | 30.50 EUR |
| 2020-06-16 21:00 | 4 | 38.95 EUR |
También se cubren:
- Fechas exactamente iguales al inicio de una tarifa.
- Fechas exactamente iguales al final de una tarifa.
- Producto inexistente.
- Marca inexistente.
- Fecha sin ninguna tarifa aplicable.
- Ausencia de parámetros.
- Identificadores no positivos.
- Fechas con formato inválido.
- Aplicación de la tarifa general cuando no existe una tarifa específica.
## Consola H2
La consola H2 está habilitada durante el desarrollo en:
```text
http://localhost:8080/h2-console
```
Datos de conexión:
```text
JDBC URL: jdbc:h2:mem:pricingdb
User Name: sa
Password:
```
La base de datos se mantiene en memoria y se reinicia al detener la aplicación.
## Arquitectura
El proyecto utiliza una arquitectura hexagonal basada en puertos y adaptadores:
```text
HTTP
 ↓
PriceController
 ↓
FindApplicablePriceUseCase
 ↓
FindApplicablePriceService
 ↓
PriceQueryPort
 ↓
PricePersistenceAdapter
 ↓
PriceJpaRepository
 ↓
H2
```
### Estructura principal
```text
src/main/java/com/dario/pricingservice
├── adapter
│   ├── input
│   │   └── rest
│   │       ├── dto
│   │       ├── PriceController.java
│   │       ├── PriceResponseMapper.java
│   │       └── RestExceptionHandler.java
│   └── output
│       └── persistence
│           ├── adapter
│           ├── entity
│           ├── mapper
│           └── repository
├── application
│   └── service
├── configuration
├── domain
│   ├── exception
│   ├── model
│   └── port
│       ├── input
│       └── output
└── PruebaTecnicaPreciosApplication.java
```
## Principios de diseño aplicados
- Separación entre dominio, aplicación e infraestructura.
- Inversión de dependencias.
- Puertos y adaptadores.
- DTOs independientes de las entidades JPA.
- Entidades JPA independientes del modelo de dominio.
- Inyección de dependencias por constructor.
- Uso de `BigDecimal` para importes monetarios.
- Uso de `LocalDateTime` para fechas con hora.
- Consultas limitadas al primer resultado aplicable.
- Tratamiento centralizado de errores REST.
- Tests unitarios y de integración.
## Configuración de tests
Los tests utilizan una base de datos H2 independiente por contexto:
```properties
spring.datasource.url=jdbc:h2:mem:pricingdb-${random.uuid}
```
Esto evita conflictos entre distintos contextos de Spring durante la ejecución de la suite.
La configuración normal de la aplicación utiliza:
```properties
spring.datasource.url=jdbc:h2:mem:pricingdb
```
## Estado de verificación
La aplicación ha sido verificada mediante:
```bash
./mvnw clean test
./mvnw clean verify
```
La verificación incluye:
- Compilación con Java 21.
- Tests unitarios.
- Tests de integración.
- Inicialización de H2.
- Generación del JAR ejecutable.
- Reempaquetado mediante Spring Boot Maven Plugin.