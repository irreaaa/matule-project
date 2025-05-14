package com.example.command.request

data class UpdateAuthByEmail (
    val email: String,
    val password: String?,
    val userName: String?
)