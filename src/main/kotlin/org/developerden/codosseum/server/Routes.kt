package org.developerden.codosseum.server

import io.github.tabilzad.ktor.annotations.KtorField
import io.ktor.resources.*
import org.developerden.codosseum.challenges.Challenge

@Resource("/validate")
class Validate {

  @Resource("/{title}")
  class Title(
    val parent: Validate = Validate(),
    @KtorField("Title of the challenge")
    val title: String,
  )

  @Resource("/summary")
  class Summary(
    val parent: Validate = Validate(),
  )
}

@Resource("/challenges")
class Challenges {

  @Resource("/random")
  class Random(
    val parent: Challenges = Challenges(),
    @KtorField("Filter for challenge difficulties. A list of difficulties to _include_ in the search. If empty, all difficulties will be included.")
    val difficultyFilters: List<Challenge.Info.Difficulty> = emptyList(),
    @KtorField("Filter for challenge tags. A list of tags to _include_ in the search. If empty, all tags will be included.")
    val tagFilters: List<String> = emptyList(),
  )
}

@Resource("/events")
class Events