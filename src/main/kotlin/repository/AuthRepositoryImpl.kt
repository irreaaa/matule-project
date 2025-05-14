package com.example.repository

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.dto.request.AuthResponse
import com.example.dto.request.LoginRequest
import com.example.model.DataRepository
import com.example.dto.request.CreateUserRequest
import com.example.model.User
import io.ktor.server.application.*
import java.util.*

class AuthRepositoryImpl(
    private val application: Application
) : AuthRepository {

    override fun login(request: LoginRequest): AuthResponse? {
        val user = DataRepository.userList.firstOrNull {
            it.email == request.email && it.password == request.password
        } ?: return null

        val token = generateToken(user)
        return AuthResponse(
            token = token,
            userId = user.userId,
            userName = user.userName,
            email = user.email
        )
    }

    override fun register(request: CreateUserRequest): AuthResponse? {
        if (DataRepository.userList.any { it.email == request.email }) return null

        val newUser = User(
            userId = DataRepository.userList.size + 1,
            userName = request.userName,
            email = request.email,
            password = request.password
        )
        DataRepository.userList.add(newUser)

        val token = generateToken(newUser)
        return AuthResponse(
            token = token,
            userId = newUser.userId,
            userName = newUser.userName,
            email = newUser.email
        )
    }

    override fun getUserById(userId: Int): User? {
        return DataRepository.userList.firstOrNull { it.userId == userId }
    }

    private fun generateToken(user: User): String {
        val config = application.environment.config
        return JWT.create()
            .withAudience(config.property("jwt.audience").getString())
            .withIssuer(config.property("jwt.issuer").getString())
            .withClaim("userId", user.userId)
            .withClaim("userName", user.userName)
            .withClaim("email", user.email)
            .withExpiresAt(Date(System.currentTimeMillis() + 60000000))
            .sign(Algorithm.HMAC256(config.property("jwt.secret").getString()))
    }
}
