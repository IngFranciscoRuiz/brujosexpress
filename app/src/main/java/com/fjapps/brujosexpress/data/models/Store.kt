package com.fjapps.brujosexpress.data.models

data class Store(
    val id: String,
    val name: String,
    val logoUrl: String?,
    val isOpen: Boolean,
    val deliveryFeeCents: Int
)


