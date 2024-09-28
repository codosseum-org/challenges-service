package org.developerden.codosseum.challenge.source

import org.koin.core.Koin

interface RemoteSource {

  suspend fun loadSource(koin: Koin)
}