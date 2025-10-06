package org.developerden.codosseum.challenges

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.developerden.codosseum.challenges.indexing.ChallengeQueue
import org.developerden.codosseum.challenges.indexing.Indexed
import org.developerden.codosseum.challenges.storage.ChallengeStorage
import org.developerden.codosseum.validation.validate

class ChallengeWorker(val challengeQueue: ChallengeQueue, val challengeStorage: ChallengeStorage) {
    private val logger = KotlinLogging.logger {}
    private var job: Job? = null
    fun start(scope: CoroutineScope) {
        if (job != null) return

        job = scope.launch {
            while (isActive) {
                try {
                    val msg = challengeQueue.poll()
                    val challenge = challengeStorage.loadChallenge(
                        msg
                    )

                    val schemaResponse = challenge.validate()

                } catch (t: Exception) {
                    logger.error(t) { "Failed to validate challenge" }
                }
            }
        }
    }

    suspend fun stop() {
        job?.let {
            it.cancel()
            runCatching { it.join() }.getOrNull()
        }
        job = null
        challengeQueue.close()
    }
}