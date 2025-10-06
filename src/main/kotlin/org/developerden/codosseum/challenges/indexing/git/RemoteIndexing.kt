package org.developerden.codosseum.challenges.indexing.git

import org.developerden.codosseum.challenges.indexing.ChallengeSourceID
import org.developerden.codosseum.challenges.indexing.Indexing
import org.developerden.codosseum.sandkasten.api.apis.ConfigurationApi
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.createDirectory
import kotlin.io.path.createParentDirectories
import kotlin.io.path.notExists
import org.developerden.codosseum.challenges.indexing.git.Repository as GitSource

object RemoteIndexing : Indexing<GitSource>() {

    override suspend fun index(source: GitSource): Path {
        val repositoryPath = Path("./challenges/git/${source.name}/")


        val destination = repositoryPath.apply {
            if (notExists()) {
                createParentDirectories()
                createDirectory()
            }
        }.toFile()

        if (destination.exists()) destination.deleteRecursively() // reset (probably not very efficient)

        Git.cloneRepository()
            .setURI(source.url)
            .setBranch(source.branch)
            .setDirectory(destination)
            .apply {
                val password = System.getenv(source.accessTokenEnv)
                if (password != null) {
                    setCredentialsProvider(
                        UsernamePasswordCredentialsProvider(
                            source.owner,
                            password
                        )
                    )
                }
            }
            .setProgressMonitor(GitProgressMonitor(source))
            .call()

        return repositoryPath.resolve(Path("challenges"))
    }

    override fun getSource(source: GitSource): ChallengeSourceID {
        return GithubChallengeSourceId(source)
    }

    class GithubChallengeSourceId(source: GitSource) : ChallengeSourceID {
        override val id: String = "github:${source.owner}/${source.name}"
    }
}
