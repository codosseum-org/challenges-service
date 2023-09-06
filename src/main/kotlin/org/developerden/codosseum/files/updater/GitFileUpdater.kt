package org.developerden.codosseum.files.updater

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
	override suspend fun trigger(): Collection<Path> {
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

		temporaryFolder.listDirectoryEntries().filter { it.name != "challenges" || !it.isDirectory() }
			.forEach(Path::deleteRecursively)

		temporaryFolder.copyToRecursively(directory, followLinks = true, overwrite = true)

		temporaryFolder.deleteRecursively()

		return directory.listDirectoryEntries()
	}
}