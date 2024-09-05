package org.developerden.codosseum.files

import org.developerden.codosseum.model.Challenge
import org.developerden.codosseum.validation.*
import java.nio.file.Path
import kotlin.io.path.readText

interface StoredChallenges {

	val schema: Path
	val challenges: Collection<Challenge>

	fun findChallenge(name: String) = challenges.find { it.name == name }

}