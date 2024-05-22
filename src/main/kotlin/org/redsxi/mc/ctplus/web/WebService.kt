package org.redsxi.mc.ctplus.web

import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.redsxi.mc.ctplus.api.CardService
import org.redsxi.mc.ctplus.api.GoodResponse
import org.redsxi.mc.ctplus.api.Players
import org.redsxi.mc.ctplus.api.VersionData
import org.redsxi.mc.ctplus.data.CardContext
import org.redsxi.mc.ctplus.generated.BuildProps
import java.util.*

object WebService {
    private val server = embeddedServer(Netty, port = 7000) {
        install(plugin)
        install(ContentNegotiation) {
            gson {
                registerTypeAdapter(CardContext.Web::class.java, CardContext.Web.JsonAdapter())
            }
        }
        routing {
            get("/") {
                call.respond(VersionData())
            }

            get("/players") {
                call.respond(GoodResponse(Players.getOnlinePlayers()))
            }

            route("/player/{uuid}") {
                exCaughtGet("/createPrepaidCard") {
                    val uuidStr = call.parameters["uuid"] ?: "00000000-0000-0000-0000-000000000000"
                    val balanceStr = call.request.queryParameters["balance"] ?: throw IllegalArgumentException("Balance shouldn't be null")
                    val uuid = UUID.fromString(uuidStr)
                    val balance = balanceStr.toInt() //Integer.getInteger(balanceStr) ?: 50 // bad but this may solve the problem. 50 is default balance
                    CardService.createPrepaidCard(uuid, balance)
                }

                exCaughtGet("/createSingleJourneyCard") {
                    val uuidStr = call.parameters["uuid"] ?: "00000000-0000-0000-0000-000000000000"
                    val priceStr = call.request.queryParameters["price"] ?: throw IllegalArgumentException("Price shouldn't be null")
                    val uuid = UUID.fromString(uuidStr)
                    val price = priceStr.toInt() //Integer.getInteger(balanceStr) ?: 50 // bad but this may solve the problem. 50 is default balance
                    CardService.createSingleJourneyCard(uuid, price)
                }

                exCaughtGet("/getHoldingCard") {
                    val uuidStr = call.parameters["uuid"] ?: "00000000-0000-0000-0000-000000000000"
                    val uuid = UUID.fromString(uuidStr)
                    CardService.getHoldingCard(uuid)
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