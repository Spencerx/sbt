### Updating remote VCS project dependencies

sbt now supports automatic updating of remote VCS project dependencies
(Git, Mercurial, Subversion) via a configurable update strategy.

By default the strategy is `Manual` — remote projects are never updated
automatically during `update`. To change this, set `repositoryUpdateStrategy`
per dependency:

```scala
lazy val dep = RootProject(uri("git:file:///path/to/repo"))

lazy val root = project.dependsOn(dep).settings(
  // Pull latest on every `update`:
  dep / repositoryUpdateStrategy := RepositoryUpdateStrategy.Always,
  // Or pull at most once per hour:
  dep / repositoryUpdateStrategy := RepositoryUpdateStrategy.Every(1.hour),
)
```

When changes are detected, sbt will warn that a `reload` is needed to pick
up the new sources.

The `updateRemoteProjects` command force-updates all remote VCS dependencies
regardless of strategy and automatically reloads the build.
