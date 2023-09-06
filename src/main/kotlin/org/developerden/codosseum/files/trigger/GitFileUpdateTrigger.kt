package org.developerden.codosseum.files.trigger

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.developerden.codosseum.ChallengesService
import org.developerden.codosseum.files.FileUpdateTrigger
import org.developerden.codosseum.files.FileUpdater
import org.developerden.codosseum.files.updater.GitFileUpdater
import org.developerden.codosseum.git.Repository
import java.util.*
import kotlin.concurrent.fixedRateTimer
import kotlin.concurrent.scheduleAtFixedRate
import kotlin.io.path.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class GitFileUpdateTrigger(
	private val period: Long,
	private val repository: Repository
) : FileUpdateTrigger {

	override val updater: FileUpdater = GitFileUpdater(repository)

	override suspend fun setupTrigger() {
		fixedRateTimer("${repository.owner}/${repository.name} updater", period = period) {
			CoroutineScope(ChallengesService.coroutineContext).launch {
				updater.trigger()
			}
		}
	}
}