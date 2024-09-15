package org.developerden.codosseum.model

enum class ChallengeDifficulty {
	EASY,
	MEDIUM,
	HARD;

	override fun toString(): String {
		return name.lowercase()
	}
}