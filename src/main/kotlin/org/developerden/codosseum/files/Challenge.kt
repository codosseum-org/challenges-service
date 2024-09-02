package org.developerden.codosseum.files

import java.nio.file.Path
import kotlin.io.path.inputStream
import kotlin.io.path.readText

data class Challenge(
	val name: String,
	private val path: Path
) {
	val inputStream get() = path.inputStream()
	val content get() = path.readText()
}
