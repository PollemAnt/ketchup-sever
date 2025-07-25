package com.example

import io.ktor.server.application.*
import io.ktor.server.http.content.resources
import io.ktor.server.http.content.static
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

fun Application.configureRouting() {
    routing {
        static("/images") {
            resources("images")
        }


        get("/ketchups") {
            call.respond(
                listOf(
                    ImageItem(1,"/images/lagodny-przod.png", "Łagodny"),
                    ImageItem(2,"/images/pikantny-przod.png", "Pikantny"),
                    ImageItem(3,"/images/60mniejlagodny-przod.png","60% mniej kalorii łagodny"),
                    ImageItem(4,"/images/60mniejpikanty-przod.png","60% mniej kalorii pikantny"),
                    ImageItem(5,"/images/zpieklarodem-przod.png","Z piekła rodem"),
                    ImageItem(6,"/images/bazyliaczosnek-przod.png","Czosnek i bazylia"),
                    ImageItem(7,"/images/premium-przod.png","Premium")
                )
            )
        }
    }
}

@Serializable
data class ImageItem(val id: Int, val url: String, val title: String)
