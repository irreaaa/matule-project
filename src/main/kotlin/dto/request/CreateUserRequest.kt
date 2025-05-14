package com.example.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class CreateUserRequest(
    val userName: String,
    val email: String,
    val password: String
)