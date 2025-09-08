# Guía de Pruebas del Backend - Alquiler de Eventos

## Preparación
1. **Ejecutar la aplicación**: `mvn spring-boot:run`
2. **Base URL**: `http://localhost:8080/api`
3. **Base de datos**: Asegúrate de tener MySQL corriendo con la BD `alquiler_eventos`

## 🔐 Pruebas de Autenticación

### 1. Login Admin
```
POST /auth/login
Content-Type: application/json

{
  "email": "admin@alquileventos.com",
  "contrasena": "123456"
}
```
**Respuesta esperada**: JWT token

### 2. Login Cliente
```
POST /auth/login
Content-Type: application/json

{
  "email": "juan.perez@email.com",
  "contrasena": "123456"
}
```

### 3. Registro nuevo usuario
```
POST /auth/register
Content-Type: application/json

{
  "nombre": "Test",
  "apellido": "Usuario",
  "email": "test@email.com",
  "telefono": "999888777",
  "contrasena": "123456"
}
```

## 🏢 Pruebas de Locales

### 1. Listar todos los locales
```
GET /locales
```

### 2. Locales disponibles
```
GET /locales/disponibles
```

### 3. Filtrar por distrito
```
GET /locales/distrito/1
```

### 4. Filtrar por aforo mínimo
```
GET /locales/aforo/100
```

### 5. Filtrar por rango de precio
```
GET /locales/precio?min=200&max=400
```

### 6. Buscar locales
```
GET /locales/buscar?termino=elegance
```

### 7. Filtrar para evento específico
```
GET /locales/filtrar?aforo=100&idTipoEvento=1&presupuestoMaximo=300
```

## 📅 Pruebas de Reservas

### 1. Verificar disponibilidad (sin autenticación)
```
GET /reservas/disponibilidad?idLocal=1&fecha=2024-12-25&horaInicio=18:00&horaFin=23:00
```

### 2. Crear reserva (requiere JWT)
```
POST /reservas
Authorization: Bearer {JWT_TOKEN}
Content-Type: application/json

{
  "usuario": {"idUsuario": 2},
  "local": {"idLocal": 1},
  "tipoEvento": {"idTipoEvento": 1},
  "fecha": "2024-12-25",
  "horaInicio": "18:00",
  "horaFin": "23:00",
  "cantidadPersonas": 120
}
```

### 3. Listar reservas de un usuario
```
GET /reservas/usuario/2
Authorization: Bearer {JWT_TOKEN}
```

### 4. Confirmar reserva
```
PUT /reservas/1/confirmar
Authorization: Bearer {JWT_TOKEN}
```

## 💳 Pruebas de Pagos

### 1. Crear pago
```
POST /pagos
Authorization: Bearer {JWT_TOKEN}
Content-Type: application/json

{
  "reserva": {"idReserva": 1},
  "metodoPago": "TARJETA",
  "monto": 1250.00
}
```

### 2. Procesar pago
```
PUT /pagos/1/procesar
Authorization: Bearer {JWT_TOKEN}
```

## 👥 Pruebas de Usuarios (Solo ADMIN)

### 1. Listar usuarios
```
GET /usuarios
Authorization: Bearer {ADMIN_JWT_TOKEN}
```

### 2. Buscar usuarios
```
GET /usuarios/buscar?termino=juan
Authorization: Bearer {ADMIN_JWT_TOKEN}
```

## 📊 Datos Maestros

### 1. Listar distritos
```
GET /distritos
```

### 2. Listar tipos de evento
```
GET /tipos-evento
```

### 3. Listar mobiliario disponible
```
GET /mobiliario/disponibles
```

## 🔍 Endpoints de Monitoreo

### 1. Health check
```
GET /actuator/health
```

### 2. Métricas
```
GET /actuator/metrics
```

## 📝 Notas Importantes

1. **JWT Token**: Guarda el token del login y úsalo en el header `Authorization: Bearer {token}`
2. **Roles**: 
   - ADMIN: Acceso completo
   - CLIENTE: Acceso limitado a sus propios datos
3. **Validaciones**: Todos los endpoints validan datos de entrada
4. **Errores**: La API devuelve errores descriptivos en español

## 🧪 Secuencia de Prueba Completa

1. **Login** → Obtener JWT
2. **Listar locales** → Ver opciones disponibles
3. **Verificar disponibilidad** → Confirmar horario libre
4. **Crear reserva** → Hacer reserva
5. **Crear pago** → Procesar pago
6. **Confirmar reserva** → Finalizar proceso

## 🚨 Troubleshooting

- **Error 401**: Token JWT inválido o expirado
- **Error 403**: Sin permisos para la operación
- **Error 400**: Datos de entrada inválidos
- **Error 500**: Error interno (revisar logs)
