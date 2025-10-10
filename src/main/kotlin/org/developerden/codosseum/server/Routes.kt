package org.developerden.codosseum.server

import io.github.perracodex.kopapi.annotation.Schema
import io.ktor.resources.*
import org.developerden.codosseum.challenges.Challenge

@Resource("/validate")
class Validate {

    @Resource("/{title}")
    class Title(
        val parent: Validate = Validate(),
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
        val difficultyFilters: List<Challenge.Info.Difficulty> = emptyList(),
        val tagFilters: List<String> = emptyList(),
    )
}

@Resource("/events")
class Events {
}