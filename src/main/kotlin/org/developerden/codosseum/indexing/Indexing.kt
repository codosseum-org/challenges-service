package org.developerden.codosseum.indexing

import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.listDirectoryEntries

abstract class Indexing<F> {

  abstract suspend fun index(source: F): Path

  suspend fun collectIndexedPaths(source: F): Set<Indexed> {
    return buildSet {
      index(source).forEach { path ->
        val schema = path.resolve(Path("challenge-schema.json"))

        path.listDirectoryEntries().forEach { challenge ->
          if (challenge.contains(Path("challenge.yml"))) add(Indexed(if (schema.exists()) schema else null, challenge))
        }
      }
    }
  }
}


data class Indexed(
  val schema: Path? = null,
  val challengeDirectory: Path,
)