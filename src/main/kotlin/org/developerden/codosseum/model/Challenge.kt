package org.developerden.codosseum.model

import kotlinx.serialization.Serializable
import java.io.InputStream
import java.nio.file.Path
import kotlin.io.path.inputStream

@Serializable
data class Challenge(
	/**
	 * Internal name/id of the challenge
	 */
	val name: String,
	val info: ChallengeInfo,
	private val challengeFilePath: Path
) {
	val inputStream get() = challengeFilePath.inputStream()

	val solutionInputStream: InputStream
		get() = challengeFilePath.parent.resolve(info.solution.file).inputStream()
}