package org.redsxi.mc.ctplus.web

import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.redsxi.mc.ctplus.api.BadResponse
import org.redsxi.mc.ctplus.api.GoodResponse
import org.redsxi.mc.ctplus.api.Players
import org.redsxi.mc.ctplus.api.VersionData
import org.redsxi.mc.ctplus.generated.BuildProps
import java.util.UUID

object WebService {
    private val server = embeddedServer(Netty, port = 7000) {
        install(plugin)
        install(ContentNegotiation) {
            gson()
        }
        routing {
            get("/") {
                call.respond(VersionData())
            }

            get("/players") {
                call.respond(GoodResponse(Players.getOnlinePlayers()))
            }

            route("/player/{uuid}") {
                get("/createPrepaidCard") {
                    try {
                        val uuid = UUID.fromString(call.parameters["uuid"])

                        call.respond("Prepaid Card for $uuid | balance: ${call.request.queryParameters["balance"]}")
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, BadResponse(e))
                    }
                }

                get("/createSingleJourneyCard") {
                    try {
                        val uuid = UUID.fromString(call.parameters["uuid"])

                        call.respond("Prepaid Card for $uuid | balance: ${call.request.queryParameters["balance"]}")
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, BadResponse(e))
                    }
                }
            }

            get("/player/{uuid}/createPrepaidCard") {
                call.respond("Prepaid Card for ${call.parameters["uuid"]} balance: ${call.request.queryParameters["balance"]}")
            }
        }
    }

    private val plugin = createApplicationPlugin("CTPlusWebPlugin") {
        onCallRespond { call, _ ->
            call.response.headers.append("Server", "CTPlus/${BuildProps.VERSION} (ktor)")
        }
    }

    fun start() {
        server.start()
    }

    fun stop() {
        server.stop()
    }
}