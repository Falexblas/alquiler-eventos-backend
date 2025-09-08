# Sistema de Gestión de Reservas para Alquiler de Espacios para Eventos

## Descripción
Plataforma web backend desarrollada en Spring Boot que permite la gestión eficiente de reservas para espacios destinados a eventos (bodas, conferencias, cumpleaños, etc.), con disponibilidad en tiempo real, selección de servicios y integración de pagos digitales.

## Tecnologías Utilizadas
- **Java 21**
- **Spring Boot 3.5.5**
- **Spring Security** (Autenticación JWT)
- **Spring Data JPA** (Persistencia de datos)
- **MySQL 8** (Base de datos)
- **Maven** (Gestión de dependencias)
- **Lombok** (Reducción de código boilerplate)

## Estructura del Proyecto

```
src/main/java/com/alquileventos/backend/
├── config/          # Configuraciones de seguridad
├── controller/      # Controladores REST
├── dto/            # Objetos de transferencia de datos
├── entity/         # Entidades JPA
├── exception/      # Manejo global de excepciones
├── repository/     # Repositorios JPA
├── security/       # Configuración JWT y seguridad
└── service/        # Lógica de negocio
```

## Endpoints Principales

### Autenticación
- `POST /api/auth/login` - Iniciar sesión
- `POST /api/auth/register` - Registrar nuevo usuario

### Locales
- `GET /api/locales` - Listar todos los locales
- `GET /api/locales/disponibles` - Locales disponibles
- `GET /api/locales/filtrar?aforo=100&idTipoEvento=1` - Filtrar locales

### Reservas
- `POST /api/reservas` - Crear nueva reserva
- `GET /api/reservas/usuario/{id}` - Reservas de un usuario
- `GET /api/reservas/disponibilidad` - Verificar disponibilidad

### Usuarios (requiere autenticación)
- `GET /api/usuarios` - Listar usuarios (solo ADMIN)
- `PUT /api/usuarios/{id}` - Actualizar usuario



## Características Implementadas

**Gestión de Usuarios y Roles**
- Registro y autenticación con JWT
- Roles: ADMIN y CLIENTE
- Encriptación de contraseñas con BCrypt

**Gestión de Locales**
- CRUD completo de locales
- Filtrado por distrito, aforo, precio y tipo de evento
- Gestión de fotos de locales

**Sistema de Reservas**
- Verificación de disponibilidad en tiempo real
- Cálculo automático de costos
- Estados: Pendiente, Confirmada, Cancelada

**Gestión de Mobiliario**
- Catálogo de mobiliario disponible
- Asociación con reservas
- Control de stock

**Sistema de Pagos**
- Múltiples métodos: Tarjeta, Yape, Plin
- Estados: Pendiente, Pagado, Fallido
- Gestión de comprobantes

**Seguridad**
- Autenticación JWT
- Autorización basada en roles
- CORS configurado
- Validación de datos

## Próximos Pasos Sugeridos

1. **Frontend**: Desarrollar interfaz web con React/Angular/Vue
2. **Notificaciones**: Implementar envío de emails
3. **Reportes**: Generar reportes de reservas y pagos
4. **API de Pagos**: Integrar con pasarelas de pago reales
5. **Subida de Archivos**: Implementar carga de fotos de locales
6. **Calendario**: Vista de calendario para disponibilidad


