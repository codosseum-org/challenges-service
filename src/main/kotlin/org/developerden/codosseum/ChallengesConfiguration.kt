package org.developerden.codosseum

import kotlinx.serialization.Serializable
import org.developerden.codosseum.challenge.watching.remote.Repository

@Serializable
data class ChallengesConfiguration(
  val watching: Watching,
) {

  @Serializable
  data class Watching(
    val storingDirectory: String,
    val local: Collection<String>,
    val remote: Remote,
  ) {

    @Serializable
    data class Remote(
      val updateTimePeriod: Long,
      val repositories: Collection<Repository>,
    )
  }
}