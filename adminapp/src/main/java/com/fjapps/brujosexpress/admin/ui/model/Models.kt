package com.fjapps.brujosexpress.admin.ui.model

data class AdminProduct(
    val id: String,
    val name: String,
    val priceCents: Int,
    val imageUrl: String?,
    val category: String,
    val available: Boolean,
    val featured: Boolean = false
)

data class AdminStore(
    val id: String,
    val name: String,
    val logoUrl: String?,
    val phone: String?,
    val deliveryFeeCents: Int,
    val isOpen: Boolean
)

data class AdminOrder(
    val id: String,
    val customer: String,
    val totalCents: Int,
    val status: String,
    val timestamp: Long
)


