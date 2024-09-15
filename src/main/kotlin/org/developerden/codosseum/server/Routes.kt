package org.developerden.codosseum.server

import io.github.tabilzad.ktor.annotations.KtorFieldDescription
import io.ktor.resources.*
import org.developerden.codosseum.model.ChallengeDifficulty

@Resource("/validate")
class Validate {

	@Resource("/{title}")
  class Title(
    val parent: Validate = Validate(),
    @KtorFieldDescription("Title of the challenge")
    val title: String,
  )
}

@Resource("/challenges")
class Challenges {

  @Resource("/random")
  class Random(
    val parent: Challenges = Challenges(),
    @KtorFieldDescription("Filter for challenge difficulties. A list of difficulties to _include_ in the search. If empty, all difficulties will be included.")
    val difficultyFilters: List<ChallengeDifficulty> = emptyList(),
    @KtorFieldDescription("Filter for challenge tags. A list of tags to _include_ in the search. If empty, all tags will be included.")
    val tagFilters: List<String> = emptyList(),
  )
}

@Resource("/events")
class Events