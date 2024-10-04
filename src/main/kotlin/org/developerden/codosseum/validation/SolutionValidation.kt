package org.developerden.codosseum.validation

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.toSet
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import org.developerden.codosseum.ChallengesService
import org.developerden.codosseum.challenge.Challenge
import org.developerden.codosseum.sandkasten.api.apis.ProgramsApi
import org.developerden.codosseum.sandkasten.api.models.BuildRequest
import org.developerden.codosseum.sandkasten.api.models.BuildRequestMainFile
import org.developerden.codosseum.sandkasten.api.models.RunRequest
import org.developerden.codosseum.server.routes.event.EventBus
import org.developerden.codosseum.server.routes.event.TestCompleteEvent

suspend fun Challenge.validateSolutions(
  eventsBus: EventBus,
  api: ProgramsApi,
): SolutionValidationResult {
  val compiledSolution = api.compile(
    BuildRequest(
      info.solution.language,
      BuildRequestMainFile(solutionInputStream.readBytes().decodeToString())
    )
  ).body()


  val results = info.publicTests.asFlow().concurrentMap { test ->
    val body = api.runProgram(
      compiledSolution.programId, RunRequest(
        test.input.joinToString("\n")
      )
    ).body()
    val succeeded = body.stdout.trim() == test.output.joinToString("\n")
    eventsBus.publish(
      TestCompleteEvent(
        name, test.name, succeeded,
        if (succeeded) null else FailedTest(
          test.name,
          test.output.joinToString("\n"),
          body.stdout.trim(),
          body.stderr
        )
      )
    )
    test to body
  }.toSet().toMap()


  val failed = results
    .filterNot { (k, v) -> v.stdout.trim() == k.output.joinToString("\n") }
    .map { (k, v) ->
      FailedTest(k.name, k.output.joinToString("\n"), v.stdout.trim(), v.stderr)
    }.toSet()
  val success = failed.isEmpty()
  return SolutionValidationResult(info.title, success, failed)
}

@Serializable
data class SolutionValidationResult(
  val challengeName: String,
  val success: Boolean,
  val failedTests: Set<FailedTest>,
)

@Serializable
data class FailedTest(
  val testName: String,
  val expectedOutput: String,
  val actualOutput: String,
  val stderr: String = "",
)

@Serializable
data class TemplateValidationResult(
  val success: Boolean,
  val errors: String?,
)


inline fun <T, R> Flow<T>.concurrentMap(crossinline transform: suspend (T) -> R): Flow<R> = channelFlow {
  withContext(ChallengesService.coroutineContext) {
    collect {
      launch { send(transform(it)) }
    }
  }
}