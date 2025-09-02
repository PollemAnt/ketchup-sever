package com.example

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.models.ImageItem
import com.example.models.User
import com.example.models.users
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.http.content.resources
import io.ktor.server.http.content.static
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Ketchup server is running!", ContentType.Text.Plain)
        }
        authRoutes()
        authenticate("auth-jwt") {
            post("/ketchups") {
                val principal = call.principal<JWTPrincipal>()
                val username = principal!!.payload.getClaim("username").asString()
                // dodaj ketchup przypisany do użytkownika
            }

            static("/images") {
                resources("images")
            }


            get("/ketchups") {
                call.respond(
                    listOf(
                        ImageItem(1, "/images/lagodny-przod.png", "Łagodny"),
                        ImageItem(2, "/images/pikantny-przod.png", "Pikantny"),
                        ImageItem(
                            3,
                            "/images/60mniejlagodny-przod.png",
                            "60% mniej kalorii łagodny"
                        ),
                        ImageItem(
                            4,
                            "/images/60mniejpikanty-przod.png",
                            "60% mniej kalorii pikantny"
                        ),
                        ImageItem(5, "/images/zpieklarodem-przod.png", "Z piekła rodem"),
                        ImageItem(6, "/images/bazyliaczosnek-przod.png", "Czosnek i bazylia"),
                        ImageItem(7, "/images/premium-przod.png", "Premium")
                    )
                )
            }
        }
    }
}


fun Route.authRoutes() {
    post("/register") {
        val creds = call.receive<User>()
        if (users.any { it.username == creds.username }) {
            call.respond(HttpStatusCode.Conflict, "Użytkownik już istnieje")
        } else {
            users.add(creds)
            call.respond(HttpStatusCode.Created, "Zarejestrowano")
        }
    }

    post("/login") {
        val creds = call.receive<User>()
        val user = users.find { it.username == creds.username && it.password == creds.password }
        if (user != null) {
            val token = JWT.create()
                .withAudience("ketchup-users") // odbiorca tokena
                .withIssuer("ketchup-server")// kto go wystawił
                .withClaim("username", user.username) // co w nim zapisujemy
                .sign(Algorithm.HMAC256("my-super-secret")) // podpisanie hasłem
            call.respond(mapOf("token" to token))
        } else {
            call.respond(HttpStatusCode.Unauthorized, "Nieprawidłowe dane")
        }
    }
}
