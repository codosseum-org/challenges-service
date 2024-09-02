package org.developerden.codosseum.model

import java.nio.file.Path
import kotlin.io.path.inputStream

data class Challenge(
	/**
	 * Internal name/id of the challenge
	 */
	val name: String,
	val info: ChallengeInfo,
	private val path: Path
) {
	val inputStream get() = path.inputStream()
}