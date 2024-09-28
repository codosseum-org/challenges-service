package org.developerden.codosseum.util

import io.ktor.client.request.*
import org.developerden.codosseum.ServiceConfiguration
import org.koin.core.Koin

fun HttpRequestBuilder.applyAuthenticationHeaders(koin: Koin) {
  val githubToken = koin.get<ServiceConfiguration>().watching.remote.githubToken
  header("Authorization", githubToken)
}