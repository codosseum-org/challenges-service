package org.developerden.codosseum.files.updater

import org.developerden.codosseum.ChallengesService
import org.developerden.codosseum.files.FileUpdater
import org.developerden.codosseum.files.git.Repository
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.*

class GitFileUpdater(
	private val repository: Repository,
	private val authentication: Repository.Authentication? = repository.authentication
) : FileUpdater {

	override val directory: Path
		get() = Paths.get("./challenges/git/repos/${repository.owner}/${repository.name}/")
			.apply { if (notExists()) createDirectories() }

	private val temporaryFolder: Path
		get() = Paths.get("./challenges/git/temporary/${repository.owner}/${repository.name}/")
			.apply { if (notExists()) createDirectories() }

	@OptIn(ExperimentalPathApi::class)
	override suspend fun trigger() {
		temporaryFolder.deleteRecursively()

		Git.cloneRepository()
			.setURI(repository.url)
			.setDirectory(temporaryFolder.toFile())
			.also {
				if (authentication != null && !repository.public) {
					it.setCredentialsProvider(
						UsernamePasswordCredentialsProvider(
							authentication.username,
							authentication.password
						)
					)
				}
			}
			.call()

		temporaryFolder.listDirectoryEntries().filter {
			!(it.name.contains("challenges") && it.isDirectory()) && !(it.name.contains("default-schema.json") && it.isRegularFile())
		}.forEach {
			ChallengesService.logger.info { "Deleting ${it.absolutePathString()}" }
			it.deleteRecursively()
		}

		temporaryFolder.copyToRecursively(directory, followLinks = true, overwrite = true)

		temporaryFolder.deleteRecursively()
	}
}