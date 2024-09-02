package org.developerden.codosseum.server

import io.ktor.resources.*
import org.developerden.codosseum.model.ChallengeDifficulty

@Resource("/validate")
class Validate {

	@Resource("/{title}")
	class Title(val parent: Validate = Validate(), val title: String)
}

@Resource("/random")
class RandomChallenge(
	val difficultyFilters: List<ChallengeDifficulty> = emptyList(),
	val tagFilters: List<String> = emptyList()
) {

}