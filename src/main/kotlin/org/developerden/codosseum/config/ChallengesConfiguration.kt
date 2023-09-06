package org.developerden.codosseum.config

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import org.developerden.codosseum.files.git.Repository
import java.nio.file.Paths
import kotlin.io.path.createFile
import kotlin.io.path.inputStream
import kotlin.io.path.notExists

@Serializable
data class ChallengesConfiguration(
	val locals: Collection<String>,
	val remoteUpdatePeriod: Long,
	val repositories: Collection<Repository> = emptyList()
) {

	companion object {
		@OptIn(ExperimentalSerializationApi::class)
		fun loadConfig(): ChallengesConfiguration {
			return Json.decodeFromStream(Paths.get("./challenges.json").apply { if (notExists()) createFile() }.inputStream())
		}
	}
}