package org.developerden.codosseum.indexing.challenge

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.nio.file.Path
import kotlin.io.path.Path

/**
 * Data holder of a challenge
 * @param name Internal name of the challenge
 * @param info General information of the challenge
 * @param challengePath Path where the files of the challenge are located
 */
@Serializable
data class Challenge(
  val name: String,
  val info: Info,
  val owner: ChallengesOwner,
  val challengePath: Path = owner.ownerPath.resolve(name),
) {

  val solutionFile: Path
    get() = challengePath.resolve(info.solution.file)

  @Serializable
  data class ChallengesOwner(
    val name: String,
    val ownerPath: Path = Path("./challenges/", "$name/"),
    val ownedChallenges: MutableSet<Challenge> = mutableSetOf(),
  )

  @Serializable
  data class Info(
    @SerialName("\$schema") val schema: String,
    val author: Author,
    val license: String,
    val language: String,
    val title: String,
    val difficulty: Difficulty,
    val tags: List<String>,
    val text: String,
    val inputFormat: String,
    val examples: List<Example>,
    val publicTests: List<Test>,
    val solution: Solution,
  ) {

    @Serializable
    data class Author(
      val name: String,
      val contact: List<Contact>,
    ) {
      @Serializable
      data class Contact(
        val type: String,
        val contact: String,
      )
    }

    @Serializable
    data class Example(
      @SerialName("in") val input: List<String>,
      @SerialName("out") val output: List<String>,
    )

    @Serializable
    data class Test(
      val name: String,
      @SerialName("in") val input: List<String>,
      @SerialName("out") val output: List<String>,
    )

    @Serializable
    data class Solution(
      val language: String,
      val file: String,
    )

    enum class Difficulty {
      EASY,
      MEDIUM,
      HARD;

      override fun toString(): String {
        return name.lowercase()
      }
    }
  }
}

