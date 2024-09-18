package org.developerden.codosseum.validation

import io.ktor.client.call.*
import org.developerden.codosseum.challenge.Challenge
import org.developerden.codosseum.templatespiler.api.apis.DefaultApi
import org.developerden.codosseum.templatespiler.api.models.TemplateParseRequest

suspend fun validateTemplate(api: DefaultApi, challenge: Challenge): TemplateValidationResult {
  val req = api.templateParsePost(
    TemplateParseRequest(challenge.info.inputFormat, "unknown")
  )

  if (req.success) {
    return TemplateValidationResult(true, null)
  }
  return TemplateValidationResult(false, req.response.body())


}