package com.example.route

import com.example.dto.response.ErrorResponse
import com.example.model.DataRepository
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.sneakersRoute() {
    get("/allSneakers") {
        call.respond(DataRepository.sneakerList)
    }

    get("/sneakers/popular") {
        val popularSneakers = DataRepository.sneakerList.filter { it.isPopular }
        call.respond(popularSneakers)
    }

    get("/sneakers/{category}") {
        val category = call.parameters["category"] ?: return@get call.respond(
            HttpStatusCode.BadRequest,
            ErrorResponse("Category parameter is required", 400)
        )

        val filtered = DataRepository.sneakerList.filter {
            it.category.equals(category, ignoreCase = true)
        }
        call.respond(filtered)
    }
}