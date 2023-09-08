package org.developerden.codosseum.files.stored

import org.developerden.codosseum.files.StoredChallenges
import org.developerden.codosseum.files.git.Repository
import java.io.FileNotFoundException
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.exists
import kotlin.io.path.listDirectoryEntries

class GitStoredChallenges(
	val repository: Repository
) : StoredChallenges {

	override val schema: Path
		get() = Paths.get("./challenges/git/repos/${repository.owner}/${repository.name}/", "challenges-schema.json") ?: Paths.get(
			GitStoredChallenges::class.java.getResource("/default-schema.json")?.toURI() ?: throw FileNotFoundException("Could not find default schema in resources.")
		)

	override val challenges: Collection<Path>
		get() = Paths.get("./challenges/git/repos/${repository.owner}/${repository.name}/", "challenges/").listDirectoryEntries()
}