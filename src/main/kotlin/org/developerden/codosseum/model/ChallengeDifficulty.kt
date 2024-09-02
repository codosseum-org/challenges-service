package org.developerden.codosseum.model

import kotlinx.serialization.SerialName

enum class ChallengeDifficulty {
	@SerialName("easy")
	easy,

	@SerialName("medium")
	medium,

	@SerialName("hard")
	hard
}