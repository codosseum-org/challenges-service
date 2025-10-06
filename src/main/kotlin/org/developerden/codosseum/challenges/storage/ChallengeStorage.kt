package org.developerden.codosseum.challenges.storage

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import com.charleskorn.kaml.decodeFromStream
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.EmptySerializersModule
import org.developerden.codosseum.challenges.Challenge
import org.developerden.codosseum.challenges.indexing.Indexed
import java.util.concurrent.ConcurrentHashMap
import kotlin.io.path.inputStream
import kotlin.io.path.name


class ChallengeStorage(val json: Json) {
    private val challenges = ConcurrentHashMap<String, Challenge>()
    private val yaml = Yaml(
        EmptySerializersModule(),
        YamlConfiguration()
            .copy(decodeEnumCaseInsensitive = true)
    )
    private val logger = KotlinLogging.logger { }

    fun readChallenge(indexed: Indexed): Challenge {
        logger.info { "Reading Challenge from ${indexed.challengeSourceID.id}" }
        val definitionFile =
            indexed.challengeDefinitionFile ?: throw IllegalArgumentException("Challenge definition not found")
        val info = yaml
            .decodeFromStream<Challenge.Info>(definitionFile.inputStream())

        val challenge = Challenge(
            indexed.challengeDefinitionFile.parent.name,
            info,
            indexed.challengeDefinitionFile,
        )

        logger.info { "Read Challenge '${challenge.name}'" }
        return challenge
    }

    fun addChallenge(challenge: Challenge) {
        logger.info { "Adding Challenge '${challenge.name}'" }
        challenges[challenge.name] = challenge
    }

    fun getAllChallenges() = challenges.values.toSet()
}