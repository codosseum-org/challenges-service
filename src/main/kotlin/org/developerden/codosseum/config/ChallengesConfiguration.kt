package org.developerden.codosseum.config

import org.developerden.codosseum.challenge.update.git.Repository
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

object ChallengesConfiguration {

	val localPaths: Collection<Path> = System.getenv("LOCAL_PATHS").split(";").map(Paths::get)
	val remoteUpdatePeriodMinutes: Duration = System.getenv("REMOTE_UPDATE_PERIOD_MINUTES").toInt().minutes
	val repositories: Collection<Repository> = System.getenv("GIT_REPOSITORIES").split(";").map(Repository::fromUrl)

}