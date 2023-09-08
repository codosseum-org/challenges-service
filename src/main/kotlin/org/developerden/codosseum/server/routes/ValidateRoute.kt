package org.developerden.codosseum.server.routes

import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.developerden.codosseum.ChallengesService
import org.developerden.codosseum.server.Validate
import kotlin.io.path.name

fun Routing.validate() {
	get<Validate.Title> { route ->

	}
}