package org.developerden.codosseum.files.stored

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.decodeFromStream
import org.developerden.codosseum.model.Challenge
import org.developerden.codosseum.model.ChallengeInfo
import java.nio.file.Path
import kotlin.io.path.inputStream
import kotlin.io.path.nameWithoutExtension

fun loadChallenge(challengeDirectory: Path): Challenge {
	val name = challengeDirectory.nameWithoutExtension
	val challengeFile = challengeDirectory.resolve("challenge.yml")
	val info = Yaml.default.decodeFromStream<ChallengeInfo>(challengeFile.inputStream())
	return Challenge(name, info, challengeFile)
}