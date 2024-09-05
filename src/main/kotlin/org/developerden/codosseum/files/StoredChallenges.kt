package org.developerden.codosseum.files

import org.developerden.codosseum.model.Challenge
import java.nio.file.Path

interface StoredChallenges {

	val schema: Path
	val challenges: Collection<Challenge>

	fun findChallenge(name: String) = challenges.find { it.name == name }

}