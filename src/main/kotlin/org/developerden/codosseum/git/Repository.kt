package org.developerden.codosseum.git

import kotlinx.serialization.Serializable

@Serializable
data class Repository(
	val url: String,
	val owner: String,
	val name: String,
	val branch: String,
	val authentication: Authentication? = null,
	val public: Boolean = authentication == null
) {

	@Serializable
	data class Authentication(
		val username: String,
		val password: String
	)
}