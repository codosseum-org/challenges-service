package org.developerden.codosseum.indexing.git

import org.developerden.codosseum.indexing.Indexing
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.createDirectory
import kotlin.io.path.createParentDirectories
import kotlin.io.path.notExists
import org.developerden.codosseum.indexing.git.Repository as GitSource

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

//            .setCredentialsProvider(
//                UsernamePasswordCredentialsProvider(
//                    source.owner,
//                    System.getenv(source.accessTokenEnv) ?: throw IllegalStateException("No access token for repository '${source.name}' provided.")
//                )
//            )
            .setProgressMonitor(GitProgressMonitor(source))
            .call()

        return repositoryPath.resolve(Path("challenges"))
    }
}
