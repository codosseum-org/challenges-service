package org.developerden.codosseum.files.stored

import org.developerden.codosseum.files.StoredChallenges
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.listDirectoryEntries

class LocalStoredChallenges(
	val local: String
) : StoredChallenges {

	override val schema: Path
		get() = Paths.get("$local/challenge-schema.json")

	override val challenges: Collection<Path>
		get() = Paths.get("$local/challenges").listDirectoryEntries()
}