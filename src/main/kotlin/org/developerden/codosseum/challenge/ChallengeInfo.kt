package org.developerden.codosseum.challenge

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChallengeInfo(
	@SerialName("\$schema") val schema: String,
	val author: ChallengeAuthor,
	val license: String,
	val language: String,
	val title: String,
	val difficulty: ChallengeDifficulty,
	val tags: List<String>,
	val text: String,
	val inputFormat: String,
	val examples: List<ChallengeExample>,
	val publicTests: List<ChallengeTest>,
	val solution: Solution
)

@Serializable
data class ChallengeAuthor(
	val name: String,
	val contact: List<AuthorContact>
)

@Serializable
data class AuthorContact(
	val type: String,
	val contact: String
)

@Serializable
data class ChallengeExample(
	@SerialName("in") val input: List<String>,
	@SerialName("out") val output: List<String>
)

@Serializable
data class ChallengeTest(
	val name: String,
	@SerialName("in") val input: List<String>,
	@SerialName("out") val output: List<String>
)

@Serializable
data class Solution(
	val language: String,
	val file: String
)


