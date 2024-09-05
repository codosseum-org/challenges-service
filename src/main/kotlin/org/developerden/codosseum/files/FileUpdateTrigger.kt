package org.developerden.codosseum.files

interface FileUpdateTrigger {

	val updater: FileUpdater

	suspend fun setupTrigger()
}