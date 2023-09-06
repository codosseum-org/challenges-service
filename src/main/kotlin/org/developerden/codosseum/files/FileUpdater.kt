package org.developerden.codosseum.files

import java.nio.file.Path

interface FileUpdater {

	val directory: Path

	suspend fun trigger(): Collection<Path>
}