package org.developerden.codosseum.challenge.watching.remote

import kotlinx.serialization.Serializable

@Serializable
data class Repository(
  var url: String,
  val owner: String,
  val repo: String,
  val remote: String = "origin",
  val authentication: RepositoryAuthentication,
) {

  @Serializable
  data class RepositoryAuthentication(
    val username: String,
    val password: String,
  )
}