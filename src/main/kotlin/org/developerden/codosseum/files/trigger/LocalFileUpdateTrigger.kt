package org.developerden.codosseum.files.trigger

import io.github.irgaly.kfswatch.KfsDirectoryWatcher
import io.github.oshai.kotlinlogging.withLoggingContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.developerden.codosseum.ChallengesService
import org.developerden.codosseum.config.ChallengesConfiguration
import org.developerden.codosseum.files.FileUpdateTrigger
import org.developerden.codosseum.files.updater.LocalFileUpdater
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.absolutePathString
import kotlin.io.path.createDirectories
import kotlin.io.path.isDirectory
import kotlin.io.path.notExists

class LocalFileUpdateTrigger(val path: Path) : FileUpdateTrigger {

	override val updater = LocalFileUpdater(path.apply { if(notExists()) createDirectories() })

	override suspend fun setupTrigger() {
		val watcher = KfsDirectoryWatcher(CoroutineScope(ChallengesService.coroutineContext))

		watcher.add(updater.directory.absolutePathString())

		withContext(ChallengesService.coroutineContext) {
			launch {
				watcher.onEventFlow.collect {
					val file = Paths.get(path.absolutePathString(), it.path)
					ChallengesService.logger.info { "Event: ${it.event.name} | Directory: ${file.isDirectory()} | File: /${file.absolutePathString()}" }
					updater.trigger()
				}
			}
		}
	}
}