package com.example.rout

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class User(
    val userId: Int,
    val userName: String,
    val email: String,
    val password: String, )
@Serializable
data class CreateUserRequest(
    val userName: String,
    val email: String,
    val password: String, )

val userList = mutableListOf<User>()

fun Route.authRoute(){
    post("login"){

    }
    post("registration"){
        val user = call.receive<CreateUserRequest>()
        userList.add(User(
            userId = userList.size + 1,
            userName = user.userName,
            email = user.email,
            password = user.password,
        ))
        val jwtAudience = environment.config.property("jwt.audience").getString()
        val jwtIssue = environment.config.property("jwt.issue").getString()
        val jwtRealm = environment.config.property("jwt.realm").getString()
        val jwtSecret = environment.config.property("jwt.secret").getString()
        val token = JWT.create()
            .withAudience(jwtAudience)
            .withIssuer(jwtIssue)
            .withClaim("user",user.userName)
            .withExpiresAt(Date(System.currentTimeMillis() + 60000))
            .sign(Algorithm.HMAC256(jwtSecret))
        call.respond("Token" to token)


    }
    authenticate("auth-jwt") {
        get ("/profile/{userId}")
        {
            val userId = call.pathParameters["userId"]?.toInt()
            val findUser = userList.firstOrNull{ it.userId == userId}
            if (findUser != null) call.respond(findUser)
            call.respond(HttpStatusCode.NotFound)
        }
    }
}

//eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJodHRwOi8vMC4wLjAuMDo4MDgwIiwiaXNzIjoiaHR0cDovLzAuMC4wLjA6ODA4MCIsInVzZXIiOiJmaXJzdCBwZXJzb24iLCJleHAiOjE3NDEyNDczODd9.aT0FBL2W3Ra7nb5yOdCIZp9FgP5wNv8bS6wLjRb5qx4