# 🧙‍♂️ Brujos Express - App de Delivery

Una aplicación móvil completa de delivery que maneja tanto restaurantes como tienda de abarrotes, construida con **Kotlin** y **Jetpack Compose**.

## 🚀 Características Principales

### ✨ Funcionalidades del Usuario
- **Splash Screen** con logo animado
- **Pantalla de Inicio** con opciones de restaurante y tienda
- **Catálogos dinámicos** con filtros por categoría
- **Carrito global** con gestión de cantidades
- **Resumen de pedido** con información de entrega
- **Seguimiento en tiempo real** del estado del pedido
- **Sistema de pagos** preparado para integración futura

### 🏪 Tipos de Productos
- **Restaurantes**: Platos preparados con descripciones detalladas
- **Tienda**: Productos de abarrotes organizados por categorías
- **Filtros inteligentes** para búsqueda rápida
- **Gestión de stock** para productos de tienda

### 🎨 Diseño y UX
- **Material Design 3** con tema personalizado
- **Colores morados** (#6C63FF) como identidad visual
- **Tipografía moderna** y legible
- **Componentes reutilizables** para consistencia
- **Navegación fluida** entre pantallas

## 🏗️ Arquitectura del Proyecto

### Estructura de Carpetas
```
app/src/main/java/com/fjapps/brujosexpress/
├── ui/
│   ├── screens/          # Pantallas principales
│   ├── components/       # Componentes reutilizables
│   └── theme/           # Temas y estilos
├── navigation/           # Navegación con Compose
├── viewmodel/           # ViewModels por pantalla
├── data/
│   ├── models/          # Modelos de datos
│   └── repository/      # Repositorios simulados
└── MainActivity.kt      # Punto de entrada
```

### Tecnologías Utilizadas
- **Kotlin** como lenguaje principal
- **Jetpack Compose** para la UI
- **Navigation Compose** para navegación
- **ViewModel** para gestión de estado
- **Coil** para carga de imágenes
- **Material 3** para componentes

## 📱 Pantallas de la Aplicación

### 1. SplashScreen
- Logo centrado con animación
- Navegación automática después de 2 segundos

### 2. HomeScreen
- Opciones principales: Restaurante 🍽️ y Tienda 🛒
- Diseño de tarjetas grandes y atractivas

### 3. RestaurantCatalogScreen
- Lista de platos disponibles
- ProductCard con imagen, nombre, precio y descripción
- Botón de agregar al carrito

### 4. GroceryCatalogScreen
- Productos organizados por categorías
- Filtros con chips interactivos
- Mismo ProductCard reutilizable

### 5. CartScreen
- Lista de productos seleccionados
- Controles de cantidad (+/-)
- Opción de eliminar productos
- Total calculado automáticamente

### 6. OrderSummaryScreen
- Formulario de información de entrega
- Resumen final de productos
- Cálculo de total con envío
- Botón de confirmar pedido

### 7. OrderTrackingScreen
- Stepper visual del progreso del pedido
- Estados: Recibido → Preparando → En Camino → Entregado
- Información del repartidor y tiempo estimado

## 🛠️ Módulo Admin

### Características
- **App separada** para administradores
- **Dashboard de pedidos** con lista completa
- **Gestión de estados** con botones interactivos
- **Información detallada** de cada pedido
- **Interfaz intuitiva** para operadores

### Funcionalidades
- Ver todos los pedidos activos
- Cambiar estado de pedidos
- Información de clientes y direcciones
- Totales y detalles de productos

## 🔮 Escalabilidad Futura

### Firebase Integration
- **Firestore** para catálogos y pedidos
- **Authentication** para usuarios
- **Cloud Messaging** para notificaciones
- **Analytics** para métricas

### Sistemas de Pago
- **Stripe** para pagos con tarjeta
- **Mercado Pago** para Latinoamérica
- **Pagos en efectivo** para entrega
- **Billeteras digitales** nativas

### Funcionalidades Avanzadas
- **App de repartidor** independiente
- **Panel web** para administración
- **Notificaciones push** en tiempo real
- **Sistema de calificaciones** y reseñas

## 🚀 Instalación y Configuración

### Requisitos
- Android Studio Arctic Fox o superior
- Kotlin 1.8+
- Android SDK 23+
- Gradle 7.0+

### Pasos de Instalación
1. Clona el repositorio
2. Abre el proyecto en Android Studio
3. Sincroniza las dependencias de Gradle
4. Ejecuta la aplicación en un dispositivo o emulador

### Dependencias Principales
```gradle
implementation "androidx.navigation:navigation-compose:2.7.7"
implementation "androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0"
implementation "io.coil-kt:coil-compose:2.5.0"
implementation "androidx.compose.material:material-icons-extended:1.6.1"
```

## 📊 Estado del Proyecto

### ✅ Completado
- [x] Estructura base del proyecto
- [x] Navegación entre pantallas
- [x] Todas las pantallas principales
- [x] Componentes reutilizables
- [x] ViewModels y gestión de estado
- [x] Módulo admin separado
- [x] Temas y estilos personalizados
- [x] Estructura base para Firebase
- [x] Sistema de pagos simulado
- [x] Notificaciones base

### 🔄 En Desarrollo
- [ ] Integración con Firebase
- [ ] Sistema de pagos real
- [ ] Notificaciones push
- [ ] App de repartidor

### 📋 Pendiente
- [ ] Panel web administrativo
- [ ] Sistema de calificaciones
- [ ] Múltiples restaurantes/tiendas
- [ ] Geolocalización y mapas

## 🤝 Contribución

### Cómo Contribuir
1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

### Estándares de Código
- Usa **Kotlin** para todo el código
- Sigue las **convenciones de Android**
- Mantén la **arquitectura MVVM**
- Escribe **tests unitarios** para ViewModels
- Documenta funciones complejas

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

## 👥 Equipo

- **Desarrollador Principal**: [Tu Nombre]
- **Diseño UX/UI**: [Diseñador]
- **Testing**: [QA]

## 📞 Contacto

- **Email**: [tu-email@ejemplo.com]
- **Proyecto**: [https://github.com/tu-usuario/brujos-express]
- **Issues**: [https://github.com/tu-usuario/brujos-express/issues]

---

**¡Gracias por usar Brujos Express! 🧙‍♂️✨**

*Una experiencia mágica de delivery a tu alcance.*

