package com.example.route

import com.example.dto.response.ErrorResponse
import com.example.model.DataRepository
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.favoritesRoute() {
    authenticate("auth-jwt") {
        get("/favorites") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", Int::class) ?: return@get call.respond(
                HttpStatusCode.Unauthorized,
                ErrorResponse("Unauthorized", 401)
            )

            val user = DataRepository.userList.find { it.userId == userId }
                ?: return@get call.respond(
                    HttpStatusCode.NotFound,
                    ErrorResponse("User not found", 404)
                )

            val favorites = DataRepository.sneakerList.filter { it.id in user.favoriteSneakerIds }
            call.respond(favorites)
        }

        post("/favorites/{sneakerId}") {
            val sneakerId = call.parameters["sneakerId"]?.toIntOrNull() ?: return@post call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse("Invalid sneaker ID", 400)
            )

            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", Int::class) ?: return@post call.respond(
                HttpStatusCode.Unauthorized,
                ErrorResponse("Unauthorized", 401)
            )

            val userIndex = DataRepository.userList.indexOfFirst { it.userId == userId }
            if (userIndex == -1) return@post call.respond(
                HttpStatusCode.NotFound,
                ErrorResponse("User not found", 404)
            )


            if (!DataRepository.sneakerList.any { it.id == sneakerId }) return@post call.respond(
                HttpStatusCode.NotFound,
                ErrorResponse("Sneaker not found", 404)
            )


            val currentFavorites = DataRepository.userList[userIndex].favoriteSneakerIds
            if (currentFavorites.contains(sneakerId)) {
                call.respond(HttpStatusCode.Conflict, ErrorResponse("Sneaker already in favorites", 409))
                return@post
            }


            val updatedUser = DataRepository.userList[userIndex].copy(
                favoriteSneakerIds = currentFavorites + sneakerId
            )
            DataRepository.userList[userIndex] = updatedUser

            call.respond(HttpStatusCode.OK, mapOf("message" to "Sneaker added to favorites"))
        }
        delete("/favorites/{sneakerId}") {
            val sneakerId = call.parameters["sneakerId"]?.toIntOrNull() ?: return@delete call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse("Invalid sneaker ID", 400)
            )

            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", Int::class) ?: return@delete call.respond(
                HttpStatusCode.Unauthorized,
                ErrorResponse("Unauthorized", 401)
            )

            val userIndex = DataRepository.userList.indexOfFirst { it.userId == userId }
            if (userIndex == -1) return@delete call.respond(
                HttpStatusCode.NotFound,
                ErrorResponse("User not found", 404)
            )

            val updatedUser = DataRepository.userList[userIndex].copy(
                favoriteSneakerIds = DataRepository.userList[userIndex].favoriteSneakerIds - sneakerId
            )
            DataRepository.userList[userIndex] = updatedUser

            call.respond(mapOf("message" to "Sneaker removed from favorites"))
        }
    }
}