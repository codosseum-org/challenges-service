package org.developerden.codosseum.challenges.indexing

import kotlinx.coroutines.channels.Channel
import java.nio.file.Path
import java.time.Instant
import java.util.UUID

typealias ChallengeQueueItem = Indexed

interface ChallengeQueue {
    fun offer(item: ChallengeQueueItem): Boolean
    suspend fun poll(): ChallengeQueueItem

    fun close()
}

class ChannelBasedChallengeQueue : ChallengeQueue {
    private val channel = Channel<ChallengeQueueItem>(50)
    override fun offer(item: ChallengeQueueItem): Boolean {
        return channel.trySend(item).isSuccess
    }

    override suspend fun poll(): ChallengeQueueItem {
        return channel.receive()
    }

    override fun close() {
        channel.close()
    }
}
