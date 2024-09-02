package org.developerden.codosseum.files.stored

import org.developerden.codosseum.files.StoredChallenges
import org.developerden.codosseum.files.git.Repository
import org.developerden.codosseum.model.Challenge
import java.io.FileNotFoundException
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.absolute
import kotlin.io.path.isDirectory
import kotlin.io.path.listDirectoryEntries

class GitStoredChallenges(
	val repository: Repository
) : StoredChallenges {

	override val schema: Path
		get() = Paths.get("./challenges/git/repos/${repository.owner}/${repository.name}/", "challenges-schema.json")
			?: Paths.get(
				GitStoredChallenges::class.java.getResource("/default-schema.json")?.toURI()
					?: throw FileNotFoundException("Could not find default schema in resources.")
			)

	override val challenges: Collection<Challenge>
		get() = Paths.get("./challenges/git/repos/${repository.owner}/${repository.name}/", "challenges/")
			.listDirectoryEntries()
			.toMutableList()
			.apply { removeIf { !it.isDirectory() || it.absolute() == schema.absolute() } }
			.map(::loadChallenge)
}