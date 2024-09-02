package org.developerden.codosseum.files.stored

import org.developerden.codosseum.files.Challenge
import org.developerden.codosseum.files.StoredChallenges
import java.io.FileNotFoundException
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.isDirectory
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.name

class LocalStoredChallenges(
	val local: String
) : StoredChallenges {

	override val schema: Path
		get() = Paths.get("$local/challenges/", "challenge-schema.json") ?: Paths.get(
			GitStoredChallenges::class.java.getResource("/default-schema.json")?.toURI()
				?: throw FileNotFoundException("Could not find default schema in resources.")
		)

	override val challenges: Collection<Challenge>
		get() = (Paths.get("$local/", "challenges/")
			.takeIf { it.isDirectory() }
			?.listDirectoryEntries()
			?.toMutableList()
			?.apply {
				removeIf { it.name == schema.name }
			}
			?: emptyList())
			.map { Challenge(it.name, it.resolve("challenge.yml")) }
}