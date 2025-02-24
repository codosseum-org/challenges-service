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

class RemoteIndexing : Indexing<GitSource>() {

  override suspend fun index(source: GitSource): Path {
    val repositoryPath = Path("./challenges/git/${source.name}/")
    Git.cloneRepository()
      .setURI(source.url)
      .setBranch(source.branch)
      .setDirectory(repositoryPath.apply {
        if (notExists()) {
          createParentDirectories()
          createDirectory()
        }
      }.toFile())
      .setCredentialsProvider(UsernamePasswordCredentialsProvider(source.owner, System.getenv(source.accessTokenEnv)))
      .setProgressMonitor(GitProgressMonitor(source))
      .call()

    return repositoryPath.resolve(Path("challenges"))
  }
}
