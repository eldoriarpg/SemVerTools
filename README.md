![GitHub Workflow Status](https://img.shields.io/github/workflow/status/eldoriarpg/semvertools/Verify%20state?style=for-the-badge&label=Build)
![GitHub Workflow Status](https://img.shields.io/github/workflow/status/eldoriarpg/semvertools/Publish%20to%20Nexus?style=for-the-badge&label=Publish)

[![Sonatype Nexus (Releases)](https://img.shields.io/nexus/maven-releases/de.eldoria/semvertools?label=Release&logo=Release&server=https%3A%2F%2Feldonexus.de&style=for-the-badge)][nexus_releases]
[![Sonatype Nexus (Development)](https://img.shields.io/nexus/maven-dev/de.eldoria/semvertools?label=DEV&logo=Release&server=https%3A%2F%2Feldonexus.de&style=for-the-badge)][nexus_dev]
[![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/de.eldoria/semvertools?color=orange&label=Snapshot&server=https%3A%2F%2Feldonexus.de&style=for-the-badge)][nexus_releases]


# SemVerTools

This is a Java library providing tooling for semantic versioning.

Currently, it can mainly parse and compare valid semver strings.

## Example Usage

To check if an update is available for a program, library, framework, etc.,
it is a common approach to compare the versions, given as strings.

With SemVerTools, this is as easy as doing

```java
String thisVersion = ...;
String remoteVersion = ...;
SemanticVersion current = SemanticVersion.parse(thisVersion);
SemanticVersion next = SemanticVersion.parse(remoteVersion);
if (next.succeeds(current)) {
    System.out.println("New version available: " + next);
}
```

## Using this library

As this library is in an early development state, it is not deployed to any maven repository.

## Contributing to this project

If you want to contribute, please check out the [Contribution Notes](CONTRIBUTING.md)

## Issues, Ideas, Questions

If you found a bug, please report it to the [issue tracker](https://github.com/eldoriarpg/SemVerTools/issues).
Make sure you don't report a bug that was already reported before.

If you have a feature request, feel free to explain your idea in an issue too.

If you have any questions, feel free to [start a discussion](https://github.com/eldoriarpg/SemVerTools/discussions/new).

## License

SemVerTools is licenced under the MIT license.

[nexus_releases]: https://eldonexus.de/#browse/browse:maven-releases:de%2Feldoria%2Fsemvertools
[nexus_snapshots]: https://eldonexus.de/#browse/browse:maven-snapshots:de%2Feldoria%2Fsemvertools
[nexus_dev]: https://eldonexus.de/#browse/browse:maven-dev:de%2Feldoria%2Fsemvertools
