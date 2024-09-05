package org.developerden.codosseum.files

import java.nio.file.Path
import java.nio.file.Paths

interface FileUpdater {

	val directory: Path

	suspend fun trigger()

	object Ignore : FileUpdater {

		override val directory: Path
			get() = Paths.get("")

		override suspend fun trigger() {

		}
	}
}