package org.developerden.codosseum.challenge

enum class ChallengeDifficulty {
	EASY,
	MEDIUM,
	HARD;

	override fun toString(): String {
		return name.lowercase()
	}
}