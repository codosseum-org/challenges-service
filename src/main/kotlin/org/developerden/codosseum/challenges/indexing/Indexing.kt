package org.developerden.codosseum.challenges.indexing

import org.developerden.codosseum.ChallengesService
import org.developerden.codosseum.ServiceConfiguration
import org.developerden.codosseum.challenges.indexing.git.RemoteIndexing
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.forEachDirectoryEntry
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.name

abstract class Indexing<F> {

    abstract suspend fun index(source: F): Path


    abstract fun getSource(source: F): ChallengeSourceID

    suspend fun collectIndexedPaths(source: F): Set<Indexed> {
        return buildSet {
            index(source).forEachDirectoryEntry { challengeDirectory ->
//                val schema = path.resolve(Path("challenge-schema.json"))

                challengeDirectory.listDirectoryEntries().forEach { challenge ->
                    if (challenge.name == "challenge.yml") {
                        add(
                            Indexed(
                                challenge,
                                getSource(source),
                            )
                        )
                    }
                }
            }
        }
    }
}

data class Indexed(
    val challengeDefinitionFile: Path? = null,
    val challengeSourceID: ChallengeSourceID
)

suspend fun indexChallenges(challengeQueue: ChallengeQueue, indexing: ServiceConfiguration.Indexing) {
    indexing.remote.repositories.map { it ->
        ChallengesService.logger.debug { "Indexing repo '${it.name}'..." }
        RemoteIndexing.collectIndexedPaths(it)
            .forEach(challengeQueue::offer)
    }

    ChallengesService.logger.debug { "Indexing local path '${indexing.local.path}'..." }
    val localIndexing = object : Indexing<Path>() {
        override suspend fun index(source: Path): Path = source
        override fun getSource(source: Path): ChallengeSourceID {
            return object : ChallengeSourceID {
                override val id: String = "local"

            }
        }
    }


    localIndexing.collectIndexedPaths(Path(indexing.local.path)).forEach(challengeQueue::offer)
}