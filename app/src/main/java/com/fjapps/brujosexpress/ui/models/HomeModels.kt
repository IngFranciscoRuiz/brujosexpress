package com.fjapps.brujosexpress.ui.models

data class CategoryUi(
    val id: String,
    val name: String,
    val emoji: String,
    val imageUrl: String
)

data class FeaturedUi(
    val id: String,
    val name: String,
    val imageUrl: String,
    val rating: Double,
    val etaMinutes: Int,
    val deliveryFee: Int,
    val discountPercent: Int? = null
)


