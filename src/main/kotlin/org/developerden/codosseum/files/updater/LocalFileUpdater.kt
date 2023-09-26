package org.developerden.codosseum.files.updater

import org.developerden.codosseum.files.FileUpdater
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.createDirectories
import kotlin.io.path.notExists

class LocalFileUpdater(
	val locale: Path
) : FileUpdater {

	override val directory: Path
		get() = locale.apply { if(notExists()) createDirectories() }

	override suspend fun trigger() {

	}
}