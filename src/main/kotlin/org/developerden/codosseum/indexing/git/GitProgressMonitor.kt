package org.developerden.codosseum.indexing.git

import org.developerden.codosseum.ChallengesService
import org.eclipse.jgit.lib.ProgressMonitor

class GitProgressMonitor(private val repo: Repository) : ProgressMonitor {

  override fun start(totalTasks: Int) {
    ChallengesService.logger.debug { "Cloning '${repo.name}' from '${repo.url}'. Total tasks: '$totalTasks'." }
  }

  override fun beginTask(title: String?, totalWork: Int) {
    ChallengesService.logger.debug { "Cloning '${repo.name}' ($title). Total work: '$totalWork'." }
  }

  override fun update(completed: Int) {
    ChallengesService.logger.debug { "Cloning '${repo.name}'. Finished: '$completed' tasks." }
  }

  override fun endTask() {
    ChallengesService.logger.debug { "Cloning '${repo.name}' is finished." }
  }

  override fun isCancelled(): Boolean = false

  override fun showDuration(enabled: Boolean) {}

}