package org.developerden.codosseum.challenge.source.remote

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.developerden.codosseum.util.applyAuthenticationHeaders
import org.koin.core.Koin
import java.nio.file.Paths
import kotlin.io.path.createDirectories
import kotlin.io.path.createFile
import kotlin.io.path.notExists
import kotlin.io.path.writeBytes

@Serializable
data class GitRemoteContent(
  val name: String,
  val path: String,
  @SerialName("download_url") val download: String? = null,
  val type: String,
) {

  suspend fun loadContent(httpClient: HttpClient, baseUrl: String, basePath: String, koin: Koin) {
    if (type == "dir") httpClient.get("$baseUrl$path") {
      applyAuthenticationHeaders(koin)
    }.body<List<GitRemoteContent>>()
      .forEach { content ->
        content.loadContent(httpClient, baseUrl, basePath, koin)
      }
    else with(httpClient.get("$baseUrl$path") {
      applyAuthenticationHeaders(koin)
    }.readBytes()) {
      Paths.get(basePath + path).apply {
        if (parent.notExists()) parent.createDirectories()
        if (notExists()) createFile()

        writeBytes(this@with)
      }
    }
  }
}