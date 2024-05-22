package org.redsxi.mc.ctplus.web

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import org.redsxi.mc.ctplus.api.BadResponse
import org.redsxi.mc.ctplus.api.GoodResponse

fun Route.exCaughtGet(
    path: String,
    body: suspend PipelineContext<Unit, ApplicationCall>.() -> Any?)
: Route = get(path) {
    try {
        val resp = GoodResponse(body())
        call.respond(resp)
    } catch (e: Throwable) {
        call.respond(HttpStatusCode.BadRequest, BadResponse.new(e))
    }
}