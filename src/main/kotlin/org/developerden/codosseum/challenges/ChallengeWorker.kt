package org.developerden.codosseum.challenges

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.developerden.codosseum.challenges.indexing.ChallengeQueue
import org.developerden.codosseum.challenges.storage.ChallengeStorage
import org.developerden.codosseum.validation.ChallengeValidationService

class ChallengeWorker(
    val challengeQueue: ChallengeQueue,
    val challengeStorage: ChallengeStorage,
    val validationService: ChallengeValidationService
) {
    private val logger = KotlinLogging.logger {}
    private var job: Job? = null
    fun start(scope: CoroutineScope) {
        if (job != null) return

        job = scope.launch {
            while (isActive) {
                try {
                    logger.debug { "Polling challenge queue" }
                    val msg = challengeQueue.poll()

                    logger.debug { "Received challenge: $msg" }
                    val challenge = challengeStorage.readChallenge(
                        msg
                    )
                    logger.debug { "Loaded challenge: ${challenge.name}" }
                    val validation = validationService.validate(challenge)

                    if (validation.success.not()) {
                        logger.error { "Failed to validate challenge: $validation" }
                    } else {
                        challengeStorage.addChallenge(challenge)
                    }

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