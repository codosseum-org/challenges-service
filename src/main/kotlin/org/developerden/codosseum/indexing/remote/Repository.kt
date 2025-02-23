package org.developerden.codosseum.indexing.remote

import kotlinx.serialization.Serializable

@Serializable
data class Repository(
  val owner: String,
  val repo: String,
  val accessToken: String? = null
)