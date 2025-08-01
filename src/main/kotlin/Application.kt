package com.example

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
    embeddedServer(Netty, port = 8080, module = Application::module).start(wait = true)
}

fun Application.module() {
    configureRouting()
    install(CORS) {
        anyHost()
        allowNonSimpleContentTypes = true
    }

    install(ContentNegotiation) {
        json()
    }


    install(Authentication) {
        jwt("auth-jwt") {
            realm = "ketchup-app"
            verifier(
                JWT
                    .require(Algorithm.HMAC256("my-super-secret"))
                    .withIssuer("ketchup-server")
                    .build()
            )
            validate { credential ->
                if (credential.payload.getClaim("username").asString() != "") JWTPrincipal(credential.payload) else null
            }
        }
    }
}
