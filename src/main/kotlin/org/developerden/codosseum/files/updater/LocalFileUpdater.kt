package org.developerden.codosseum.files.updater

import org.developerden.codosseum.files.FileUpdater
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.notExists

class LocalFileUpdater(
	val local: Path
) : FileUpdater {

	override val directory: Path
		get() = local.apply { if(notExists()) createDirectories() }

	override suspend fun trigger() {

	}
}