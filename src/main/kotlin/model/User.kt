package com.example.model

data class User(
    val userId: Int,
    val userName: String,
    val email: String,
    val password: String,
    val favoriteSneakerIds: List<Int> = mutableListOf()
)