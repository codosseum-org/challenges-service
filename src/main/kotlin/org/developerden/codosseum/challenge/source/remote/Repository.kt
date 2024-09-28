package org.developerden.codosseum.challenge.source.remote

import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.Serializable
import org.developerden.codosseum.ChallengesService
import org.developerden.codosseum.challenge.source.RemoteSource
import org.developerden.codosseum.util.applyAuthenticationHeaders
import org.koin.core.Koin

@Serializable
data class Repository(
  val owner: String,
  val repo: String,
  val remote: String = "origin",
  val branch: String = "main",
  val url: String = "https://github.com/$owner/$repo/tree/$branch",
  private val baseContentsUrl: String = "https://api.github.com/repos/$owner/$repo/contents/",
  private val basePath: String = "./challenges/git/$owner/$repo/",
) : RemoteSource {

  override suspend fun loadSource(koin: Koin) {
    val httpClient = ChallengesService.httpClient

    httpClient.get(baseContentsUrl) {
      applyAuthenticationHeaders(koin)
    }.body<List<GitRemoteContent>>()
      .forEach { content -> content.loadContent(httpClient, baseContentsUrl, basePath, koin) }
  }
}