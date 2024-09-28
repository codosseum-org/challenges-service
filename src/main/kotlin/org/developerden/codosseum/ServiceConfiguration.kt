package org.developerden.codosseum

import kotlinx.serialization.Serializable
import org.developerden.codosseum.challenge.source.remote.Repository

@Serializable
data class ServiceConfiguration(
  val watching: Watching,
) {

  @Serializable
  data class Watching(
    val local: Collection<String>,
    val remote: Remote,
  ) {

    @Serializable
    data class Remote(
      val updateTimePeriod: Long,
      val repositories: Collection<Repository>,
      val githubToken: String
    )
  }
}