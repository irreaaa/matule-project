package com.example

import authRoute
import com.example.route.favoritesRoute
import com.example.route.sneakersRoute
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        authRoute()
        sneakersRoute()
        favoritesRoute()
    }
}
