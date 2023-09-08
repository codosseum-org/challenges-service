package org.developerden.codosseum.server

import io.ktor.resources.*

@Resource("/validate")
class Validate {

	@Resource("/{title}")
	class Title(val title: String)
}