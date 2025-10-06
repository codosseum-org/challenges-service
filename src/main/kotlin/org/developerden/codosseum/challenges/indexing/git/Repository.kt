package org.developerden.codosseum.challenges.indexing.git

import kotlinx.serialization.Serializable

@Serializable
data class Repository(
  val name: String,
  val owner: String,
  val url: String,
  val branch: String? = "HEAD",
  val accessTokenEnv: String? = null
)