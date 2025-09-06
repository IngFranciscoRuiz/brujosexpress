package com.fjapps.brujosexpress.data.repository

import com.fjapps.brujosexpress.data.models.Product
import com.fjapps.brujosexpress.data.models.ProductType

class ProductRepository {
    
    fun getRestaurantProducts(): List<Product> {
        return listOf(
            Product(
                id = "r1",
                name = "Hamburguesa Clásica",
                price = 12.99,
                imageUrl = "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=400",
                description = "Hamburguesa con carne, lechuga, tomate y queso",
                category = "Hamburguesas",
                type = ProductType.RESTAURANT
            ),
            Product(
                id = "r2",
                name = "Pizza Margherita",
                price = 18.50,
                imageUrl = "https://images.unsplash.com/photo-1604382354936-07c5d9983bd3?w=400",
                description = "Pizza tradicional con tomate, mozzarella y albahaca",
                category = "Pizzas",
                type = ProductType.RESTAURANT
            ),
            Product(
                id = "r3",
                name = "Ensalada César",
                price = 9.99,
                imageUrl = "https://images.unsplash.com/photo-1546793665-c74683f339c1?w=400",
                description = "Ensalada fresca con lechuga, crutones y aderezo César",
                category = "Ensaladas",
                type = ProductType.RESTAURANT
            ),
            Product(
                id = "r4",
                name = "Pasta Carbonara",
                price = 15.99,
                imageUrl = "https://images.unsplash.com/photo-1621996346565-e3dbc353d2e5?w=400",
                description = "Pasta con salsa cremosa, panceta y queso parmesano",
                category = "Pastas",
                type = ProductType.RESTAURANT
            ),
            Product(
                id = "r5",
                name = "Sushi Roll California",
                price = 22.99,
                imageUrl = "https://images.unsplash.com/photo-1579584425555-c3ce17fd4351?w=400",
                description = "Roll de sushi con aguacate, cangrejo y pepino",
                category = "Sushi",
                type = ProductType.RESTAURANT
            )
        )
    }
    
    fun getGroceryProducts(): List<Product> {
        return listOf(
            Product(
                id = "g1",
                name = "Huevos Orgánicos",
                price = 4.99,
                imageUrl = "https://images.unsplash.com/photo-1582722872445-44dc5f7e3c8f?w=400",
                description = "Docena de huevos orgánicos de gallinas libres",
                category = "Huevos",
                stock = 50,
                type = ProductType.GROCERY
            ),
            Product(
                id = "g2",
                name = "Leche Entera",
                price = 3.49,
                imageUrl = "https://images.unsplash.com/photo-1550583724-b2692b85b150?w=400",
                description = "Leche entera fresca 1 litro",
                category = "Lácteos",
                stock = 30,
                type = ProductType.GROCERY
            ),
            Product(
                id = "g3",
                name = "Pan Integral",
                price = 2.99,
                imageUrl = "https://images.unsplash.com/photo-1509440159596-0249088772ff?w=400",
                description = "Pan integral fresco recién horneado",
                category = "Panadería",
                stock = 25,
                type = ProductType.GROCERY
            ),
            Product(
                id = "g4",
                name = "Manzanas Rojas",
                price = 1.99,
                imageUrl = "https://images.unsplash.com/photo-1560806887-1e4cd0b6cbd6?w=400",
                description = "Manzanas rojas frescas por kilo",
                category = "Frutas",
                stock = 40,
                type = ProductType.GROCERY
            ),
            Product(
                id = "g5",
                name = "Pollo Entero",
                price = 8.99,
                imageUrl = "https://images.unsplash.com/photo-1604503468506-a8da13d82791?w=400",
                description = "Pollo entero fresco de granja",
                category = "Carne",
                stock = 15,
                type = ProductType.GROCERY
            ),
            Product(
                id = "g6",
                name = "Arroz Basmati",
                price = 5.99,
                imageUrl = "https://images.unsplash.com/photo-1586201375761-83865001e31c?w=400",
                description = "Arroz basmati premium 1kg",
                category = "Granos",
                stock = 35,
                type = ProductType.GROCERY
            )
        )
    }
    
    fun getCategories(): List<String> {
        return listOf("Todos", "Huevos", "Lácteos", "Panadería", "Frutas", "Carne", "Granos")
    }
}

