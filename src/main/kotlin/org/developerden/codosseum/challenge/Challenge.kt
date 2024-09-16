package org.developerden.codosseum.challenge

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.decodeFromStream
import kotlinx.serialization.Serializable
import java.io.InputStream
import java.nio.file.Path
import kotlin.io.path.inputStream
import kotlin.io.path.nameWithoutExtension

/**
 * Data holder of a challenge
 * @param name Internal name of the challenge
 * @param info General information of the challenge
 * @param challengeFilePath Path where the files of the challenge are located
 */
@Serializable
data class Challenge(
	val name: String,
	val info: ChallengeInfo,
	private val challengeFilePath: Path
) {
	val inputStream: InputStream
		get() = challengeFilePath.inputStream()

	val solutionInputStream: InputStream
		get() = challengeFilePath.parent.resolve(info.solution.file).inputStream()
}

fun Path.loadChallenge(): Challenge {
	val challengeFile = resolve("challenge.yml")
	val info = Yaml.default.decodeFromStream<ChallengeInfo>(challengeFile.inputStream())
	return Challenge(nameWithoutExtension, info, challengeFile)
}