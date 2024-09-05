package org.developerden.codosseum.validation

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.toSet
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.developerden.codosseum.model.Challenge
import org.developerden.codosseum.sandkasten.api.apis.ProgramsApi
import org.developerden.codosseum.sandkasten.api.models.*

suspend fun validateSolutions(api: ProgramsApi, challenge: Challenge): SolutionValidationResult {

	val compiledSolution = api.compile(
		BuildRequest(
			challenge.info.solution.language,
			BuildRequestMainFile(challenge.solutionInputStream.readBytes().decodeToString())
		)
	).body()


	val results =
		challenge.info.publicTests.asFlow().concurrentMap {
			val body = api.runProgram(
				compiledSolution.programId, RunRequest(
					it.input.joinToString("\n")
				)
			).body()
			it to body
		}.toSet().toMap()


	val failed = results
		.filterNot { (k, v) -> v.stdout.trim() == k.output.joinToString("\n") }
		.map { (k, v) ->
			FailedTest(k.name, k.output.joinToString("\n"), v.stdout.trim(), v.stderr)
		}.toSet()
	val success = failed.isEmpty()
	return SolutionValidationResult(challenge.info.title, success, failed)
}

@Serializable
data class SolutionValidationResult(
	val challengeName: String,
	val success: Boolean,
	val failedTests: Set<FailedTest>
)

@Serializable
data class FailedTest(
	val testName: String,
	val expectedOutput: String,
	val actualOutput: String,
	val stderr: String = ""
)

suspend fun validateCode(language: String, code: String) {

	val body = ProgramsApi().run(
		BuildRunRequest(
			BuildRunRequestBuild(
				language,
				BuildRequestMainFile(code)
			)
		)
	).body()

	println(body)
}

inline fun <T, R> Flow<T>.concurrentMap(crossinline transform: suspend (T) -> R): Flow<R> = channelFlow {
	collect {
		launch { send(transform(it)) }
	}
}