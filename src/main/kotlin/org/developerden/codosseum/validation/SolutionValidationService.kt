package org.developerden.codosseum.validation

import org.developerden.codosseum.sandkasten.api.apis.ProgramsApi
import org.developerden.codosseum.server.routes.event.EventBus
import org.developerden.codosseum.templatespiler.api.apis.DefaultApi

class SolutionValidationService(
  private val programsApi: ProgramsApi,
  private val defaultApi: DefaultApi,
  private val eventsBus: EventBus,
)