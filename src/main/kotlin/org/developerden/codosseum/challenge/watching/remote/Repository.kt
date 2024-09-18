package org.developerden.codosseum.challenge.watching.remote

import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.Serializable
import org.developerden.codosseum.ChallengesService
import java.io.File

@Serializable
data class Repository(
  val owner: String,
  val repo: String,
  val remote: String = "origin",
  val branch: String = "main",
  var url: String = "https://github.com/$owner/$repo/tree/$branch",
  val authentication: RepositoryAuthentication? = null,
) {

  @Serializable
  data class RepositoryAuthentication(
    val username: String,
    val password: String,
  )

  suspend fun download(path: String): Collection<File> {
    val challenges = getFile("challenges")

    println(challenges)

    return emptyList()
  }

  private suspend fun getFile(path: String): String {
    return ChallengesService.httpClient
      .get(" https://api.github.com/repos/$owner/$repo/contents/$path")
      .bodyAsText()
  }
}