package org.developerden.codosseum.files.updater

import org.developerden.codosseum.files.FileUpdater
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.listDirectoryEntries

class LocalFileUpdater(
	override val directory: Path
) : FileUpdater {

	override suspend fun trigger(): Collection<Path> = directory.listDirectoryEntries()
}