package com.example.command.request

import com.example.model.AuthEntity

data class UpdateAuthByEmail (
    val email: String,
    val password: String?,
    val userName: String?
)