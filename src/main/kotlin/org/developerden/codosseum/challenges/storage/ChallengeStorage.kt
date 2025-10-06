package org.developerden.codosseum.challenges.storage

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import com.charleskorn.kaml.decodeFromStream
import io.github.oshai.kotlinlogging.KotlinLogging
import it.krzeminski.snakeyaml.engine.kmp.api.Load
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.EmptySerializersModule
import org.developerden.codosseum.challenges.Challenge
import org.developerden.codosseum.challenges.indexing.Indexed
import org.koin.core.component.getScopeName
import java.util.concurrent.ConcurrentHashMap
import kotlin.io.path.inputStream
import kotlin.io.path.name
import kotlin.io.path.readText


class ChallengeStorage(val json: Json) {
    private val challenges = ConcurrentHashMap<String, Challenge>()
    private val yaml = Yaml(
        EmptySerializersModule(),
        YamlConfiguration()
            .copy(decodeEnumCaseInsensitive = true)
    )
    private val logger = KotlinLogging.logger { }

    fun loadChallenge(indexed: Indexed): Challenge {
        logger.info { "Loading Challenge from ${indexed.challengeSourceID.id}" }
        val definitionFile =
            indexed.challengeDefinitionFile ?: throw IllegalArgumentException("Challenge definition not found")
        val info = yaml
            .decodeFromStream<Challenge.Info>(definitionFile.inputStream())

        val challenge = Challenge(
            indexed.challengeDefinitionFile.parent.name,
            info,
            indexed.challengeDefinitionFile,
        )

        challenges[challenge.name] = challenge
        logger.info { "Loaded Challenge '${challenge.name}'" }
        return challenge
    }

    fun getAllChallenges() = challenges.values.toSet()
}