package org.developerden.codosseum.files.trigger

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.developerden.codosseum.ChallengesService
import org.developerden.codosseum.files.FileUpdateTrigger
import org.developerden.codosseum.files.FileUpdater
import org.developerden.codosseum.files.updater.GitFileUpdater
import org.developerden.codosseum.files.git.Repository
import kotlin.concurrent.fixedRateTimer

class GitFileUpdateTrigger(
	private val period: Long,
	private val repository: Repository
) : FileUpdateTrigger {

	override val updater: FileUpdater = GitFileUpdater(repository)

	override suspend fun setupTrigger() {
		fixedRateTimer("${repository.owner}/${repository.name} updater", period = period) {
			CoroutineScope(ChallengesService.coroutineContext).launch {
				ChallengesService.logger.info { "Pulling repository ${repository.owner}/${repository.name}." }
				updater.trigger()
			}
		}
	}
}