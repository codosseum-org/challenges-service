package org.developerden.codosseum

import kotlinx.serialization.Serializable
import org.developerden.codosseum.indexing.remote.Repository

@Serializable
data class ServiceConfiguration(
  val debug: Boolean = false,
  val indexing: Indexing,
) {

  @Serializable
  data class Indexing(
    val local: Local,
    val remote: Remote,
  ) {

    @Serializable
    data class Local(
      val paths: Collection<String>,
      val updateOnChange: Boolean = true,
      val updateTimePeriod: Long? = null
    )

    @Serializable
    data class Remote(
      val updateTimePeriod: Long,
      val repositories: Collection<Repository>,
    )
  }
}