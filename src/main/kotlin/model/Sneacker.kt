package com.example.model

data class Sneacker (
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val category: String,
    var isPopular: Boolean
)
