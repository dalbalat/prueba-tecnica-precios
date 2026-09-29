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

Una tarifa es aplicable cuando corresponde al producto y a la marca solicitados y la fecha consultada pertenece a su intervalo de vigencia. Ambos extremos del intervalo son inclusivos:

```text
startDate <= applicationDate <= endDate
```

Entre las tarifas aplicables se selecciona la de mayor prioridad.

Cuando varias tarifas tienen la misma prioridad, se selecciona la de menor `priceList`, identificador de la tarifa. Este criterio secundario es una decisión de la implementación, ya que el enunciado no define el desempate.

Si varios registros comparten tanto la prioridad como `priceList`, no se establece un criterio adicional entre ellos.

La consulta devuelve como máximo una tarifa. Si no existe ninguna aplicable, el endpoint responde con `404 Not Found`.

El contrato de selección se define en `PriceQueryPort`. El adaptador de persistencia lo implementa mediante filtrado, ordenación y limitación del resultado en base de datos, sin cargar todas las tarifas para seleccionarlas en memoria.

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
- Selección del menor `priceList` cuando las prioridades son iguales.

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

El proyecto utiliza arquitectura hexagonal, con separación entre dominio, aplicación y adaptadores. Las dependencias del código apuntan hacia las capas internas.

### Responsabilidades

- **Dominio:** contiene el modelo `Price` y sus invariantes. No depende de Spring, JPA, HTTP ni de las capas de aplicación o adaptadores.
- **Aplicación:** define los puertos de entrada y salida e implementa el caso de uso de consulta de precios. Depende del dominio, pero no conoce los detalles de REST ni de persistencia.
- **Adaptador REST:** recibe y valida los parámetros HTTP, invoca el puerto de entrada y transforma el resultado en un DTO de respuesta.
- **Adaptador de persistencia:** implementa el puerto de salida utilizando Spring Data JPA y transforma las entidades persistidas en modelos de dominio.
- **Configuración:** ensambla explícitamente el caso de uso, el adaptador de persistencia y los mappers mediante beans de Spring.

### Estructura principal

```text
com.dario.pricingservice
├── domain
│   └── model
│       └── Price.java
├── application
│   ├── port
│   │   ├── in
│   │   │   └── FindApplicablePriceUseCase.java
│   │   └── out
│   │       └── PriceQueryPort.java
│   └── service
│       └── FindApplicablePriceService.java
├── adapter
│   ├── input
│   │   └── rest
│   │       ├── dto
│   │       │   └── PriceResponse.java
│   │       ├── PriceController.java
│   │       ├── PriceResponseMapper.java
│   │       └── RestExceptionHandler.java
│   └── output
│       └── persistence
│           ├── adapter
│           │   └── PricePersistenceAdapter.java
│           ├── entity
│           │   └── PriceEntity.java
│           ├── mapper
│           │   └── PricePersistenceMapper.java
│           └── repository
│               └── PriceJpaRepository.java
├── configuration
│   └── ApplicationConfiguration.java
└── PruebaTecnicaPreciosApplication.java
```

### Dirección de dependencias

```text
Aplicación → Dominio
Adaptadores → Puertos de aplicación y modelo de dominio
Configuración → Aplicación y adaptadores
```

`FindApplicablePriceService` depende de `PriceQueryPort`, no de su implementación JPA. `ApplicationConfiguration` conecta ese contrato con `PricePersistenceAdapter`.

El flujo de ejecución puede llegar desde el caso de uso hasta la base de datos, pero eso no introduce una dependencia de código desde la aplicación hacia el adaptador de persistencia.

### Fronteras entre capas

- `PriceEntity` se utiliza exclusivamente en el adaptador de persistencia.
- `PriceResponse` pertenece al adaptador REST.
- Los puertos intercambian `Price` y tipos estándar de Java, sin exponer entidades JPA ni tipos HTTP.
- Los mappers realizan las conversiones en los adaptadores.

### Integración con Spring

El caso de uso, el adaptador de persistencia y los mappers se registran explícitamente en `ApplicationConfiguration`.

Los controladores y el manejador de errores mantienen sus anotaciones Spring MVC. Spring Data registra el repositorio JPA. Estas dependencias tecnológicas permanecen en las capas externas.

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

La última ejecución completa de `./mvnw clean verify` con Java 21 finalizó con:

```text
Tests run: 30, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

Para reproducir la verificación desde una compilación limpia:

```bash
./mvnw clean verify
```

La verificación completa incluye:

- Compilación limpia con Java 21.
- Tests unitarios.
- Tests de integración.
- Inicialización de H2.
- Generación del JAR ejecutable.
- Reempaquetado mediante Spring Boot Maven Plugin.
