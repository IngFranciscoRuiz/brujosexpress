package com.fjapps.brujosexpress.data.models

data class Product(
    val id: String,
    val storeId: String = "",
    val name: String,
    val price: Double,
    val imageUrl: String,
    val description: String,
    val category: String,
    val stock: Int = 0,
    val type: ProductType
)

enum class ProductType {
    RESTAURANT,
    GROCERY
}

data class CartItem(
    val product: Product,
    val quantity: Int
)

data class Order(
    val id: String,
    val items: List<CartItem>,
    val total: Double,
    val status: OrderStatus,
    val deliveryAddress: String,
    val orderDate: Long,
    val estimatedDelivery: Long? = null
)

enum class OrderStatus {
    RECEIVED,
    PREPARING,
    ON_THE_WAY,
    DELIVERED
}

data class User(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val defaultAddress: String
)

