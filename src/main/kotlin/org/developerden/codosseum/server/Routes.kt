package org.developerden.codosseum.server

import io.github.tabilzad.ktor.annotations.KtorFieldDescription
import io.github.tabilzad.ktor.annotations.Tag
import io.ktor.resources.*
import io.micrometer.core.instrument.config.validate.Validated.Valid
import org.developerden.codosseum.indexing.challenge.Challenge
import kotlin.reflect.jvm.internal.ReflectProperties.Val

@Resource("/validate")
class Validate {

  @Resource("/{title}")
  class Title(
    val parent: Validate = Validate(),
    @KtorFieldDescription("Title of the challenge")
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
    @KtorFieldDescription("Filter for challenge difficulties. A list of difficulties to _include_ in the search. If empty, all difficulties will be included.")
    val difficultyFilters: List<Challenge.Info.Difficulty> = emptyList(),
    @KtorFieldDescription("Filter for challenge tags. A list of tags to _include_ in the search. If empty, all tags will be included.")
    val tagFilters: List<String> = emptyList(),
  )
}

@Resource("/events")
class Events