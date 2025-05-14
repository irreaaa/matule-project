package com.example.repository

import com.example.dto.request.AuthResponse
import com.example.dto.request.CreateUserRequest
import com.example.dto.request.LoginRequest
import com.example.model.User

interface AuthRepository {
    fun login(request: LoginRequest): AuthResponse?
    fun register(request: CreateUserRequest): AuthResponse?
    fun getUserById(userId: Int): User?
}
