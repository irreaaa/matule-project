import com.example.config.JwtConfig
import com.example.dto.request.AuthResponse
import com.example.dto.request.CreateUserRequest
import com.example.dto.request.LoginRequest
import com.example.dto.response.ErrorResponse
import com.example.model.DataRepository
import com.example.model.DataRepository.userList
import com.example.model.User
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authRoute() {
    post("/login") {
        try {
            val request = call.receive<LoginRequest>()
            println("Received login request: email = ${request.email}, password = ${request.password}")

            val user = DataRepository.findUserByEmailAndPassword(request.email, request.password)
                ?: run {
                    call.respond(
                        HttpStatusCode.Unauthorized,
                        ErrorResponse("Invalid email or password", HttpStatusCode.Unauthorized.value)
                    )
                    return@post
                }

            val token = JwtConfig.generateToken(call.application, user)
            call.respond(
                HttpStatusCode.OK,
                AuthResponse(
                    token = token,
                    userId = user.userId,
                    userName = user.userName,
                    email = user.email
                )
            )

        } catch (e: Exception) {
            println("Error during login: ${e.message}")
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse("Invalid request format", HttpStatusCode.BadRequest.value)
            )
        }
    }

    post("/registration") {
        try {
            val request = call.receive<CreateUserRequest>()

            if (userList.any { it.email == request.email }) {
                call.respond(
                    HttpStatusCode.Conflict,
                    ErrorResponse("Email already exists", HttpStatusCode.Conflict.value)
                )
                return@post
            }

            val newUser = User(
                userId = userList.size + 1,
                userName = request.userName,
                email = request.email,
                password = request.password
            )

            userList.add(newUser)
            val token = JwtConfig.generateToken(call.application, newUser)


            call.respond(
                HttpStatusCode.Created,
                AuthResponse(
                    token = token,
                    userId = newUser.userId,
                    userName = newUser.userName,
                    email = newUser.email
                )
            )

        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse("Invalid request format", HttpStatusCode.BadRequest.value)
            )
        }
    }

    authenticate("auth-jwt") {
        get("/profile/{userId}") {
            val userId = call.parameters["userId"]?.toIntOrNull()
            val user = userId?.let { id -> userList.firstOrNull { it.userId == id } }

            if (user != null) {
                call.respond(user)
            } else {
                call.respond(
                    HttpStatusCode.NotFound,
                    ErrorResponse("User not found", HttpStatusCode.NotFound.value)
                )
            }
        }
    }
}