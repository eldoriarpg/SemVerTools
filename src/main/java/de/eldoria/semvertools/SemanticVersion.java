/*
 * SPDX-License-Identifier: MIT
 *
 * Copyright (c) 2021 SemVerTools team and contributors
 */

package de.eldoria.semvertools;

import de.eldoria.semvertools.parser.SemVerLexer;
import de.eldoria.semvertools.parser.SemVerParser;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * A representation of a <a href="https://semver.org">Semantic Version</a>.
 * <p>
 * It contains the version core (major.minor.patch), pre-release and build.
 * <p>
 * The natural order of semantic versions is defined in <a href="https://semver.org/#spec-item-11">the spec</a>.
 * @since 1.0.0
 *
 * @apiNote This interface is not meant to be implemented and might become {@code sealed} in future releases.
 */
@ApiStatus.NonExtendable
public interface SemanticVersion extends Comparable<SemanticVersion> {

  /**
   * Creates a SemanticVersion with the given major, minor and patch versions.
   *
   * @param major the major version
   * @param minor the minor version
   * @param patch the patch version
   * @return a SemanticVersion representing the version {@code major}.{@code minor}.{@code patch}.
   */
  static SemanticVersion of(int major, int minor, int patch) {
    return new VersionCore(major, minor, patch);
  }

  /**
   * Parses a string into a full semantic version, including pre-releases and builds.
   * <p>
   * The given string must represent a valid semantic version, otherwise the parsing will fail.
   *
   * @param version the raw version string.
   * @return the parsed version.
   * @throws VersionParseException if the given string does not express
   *                                                             a valid semantic version.
   */
  static SemanticVersion parse(String version) {
    SemVerLexer lexer = SemVerLexer.create();
    return new SemVerParser(version, lexer.lex(version)).parse();
  }

  /**
   * The major version of this semantic version.
   * <p>
   * This is a non-negative integer value.
   *
   * @return the major version.
   */
  int major();

  /**
   * The minor version of this semantic version.
   * <p>
   * This is a non-negative integer value.
   *
   * @return the minor version.
   */
  int minor();

  /**
   * The patch version of this semantic version.
   * <p>
   * This is a non-negative integer value.
   *
   * @return the patch version.
   */
  int patch();

  /**
   * The pre-release version, if available.
   * <p>
   * If this semantic version does not have a pre-release version, {@link Optional#empty()} is returned.
   *
   * @return the pre-release version, wrapped into an {@link Optional}.
   */
  Optional<PreRelease> preRelease();

  /**
   * The build metadata, if available.
   * <p>
   * If this semantic version does not have build metadata, {@link Optional#empty()} is returned.
   *
   * @return the build metadata, wrapped into an {@link Optional}.
   */
  Optional<Build> build();

  /**
   * Returns a semantic version that only differs from this in its major version.
   * <p>
   * If the new major version is equal to the current major version, the returned semantic version
   * is equal to this semantic version.
   *
   * @param major the new major version.
   * @return the semantic version with the new major version.
   */
  SemanticVersion withMajor(int major);

  /**
   * Increases the major version by 1.
   * <p>
   * This is equivalent to <pre>{@code
   * SemanticVersion version = ...;
   * version = version.withMajor(version.major() + 1);
   * }</pre>
   *
   * @return the semantic version with a major version greater by 1 to this major version.
   */
  default SemanticVersion increaseMajor() {
    return this.withMajor(this.major() + 1);
  }

  /**
   * Returns a semantic version that only differs from this in its minor version.
   * <p>
   * If the new minor version is equal to the current minor version, the returned semantic version
   * is equal to this semantic version.
   *
   * @param minor the new minor version.
   * @return the semantic version with the new minor version.
   */
  SemanticVersion withMinor(int minor);

  /**
   * Increases the minor version by 1.
   * <p>
   * This is equivalent to <pre>{@code
   * SemanticVersion version = ...;
   * version = version.withMinor(version.minor() + 1);
   * }</pre>
   *
   * @return the semantic version with a minor version greater by 1 to this minor version.
   */
  default SemanticVersion increaseMinor() {
    return this.withMinor(this.minor() + 1);
  }

  /**
   * Returns a semantic version that only differs from this in its patch version.
   * <p>
   * If the new patch version is equal to the current patch version, the returned semantic version
   * is equal to this semantic version.
   *
   * @param patch the new patch version.
   * @return the semantic version with the new patch version.
   */
  SemanticVersion withPatch(int patch);

  /**
   * Increases the patch version by 1.
   * <p>
   * This is equivalent to <pre>{@code
   * SemanticVersion version = ...;
   * version = version.withPatch(version.patch() + 1);
   * }</pre>
   *
   * @return the semantic version with a minor version greater by 1 to this minor version.
   */
  default SemanticVersion increasePatch() {
    return this.withPatch(this.patch() + 1);
  }

  /**
   * Returns a semantic version that only differs from this in its pre-release version.
   * <p>
   * If the new pre-release version is equal to the current pre-release version, the returned semantic version
   * is equal to this semantic version.
   * <p>
   * If the new pre-release version is {@code null}, the returned semantic version
   * won't contain a pre-release version.
   *
   * @param preRelease the new pre-release version.
   * @return the semantic version with the new pre-release version.
   */
  SemanticVersion withPreRelease(@Nullable PreRelease preRelease);

  /**
   * Returns a semantic version that only differs from this in its build metadata.
   * <p>
   * If the new build metadata is equal to the current build metadata, the returned semantic version
   * is equal to this semantic version.
   * <p>
   * If the new build metadata is {@code null}, the returned semantic version won't contain build metadata.
   *
   * @param build the new build metadata.
   * @return the semantic version with the new build metadata.
   */
  SemanticVersion withBuild(@Nullable Build build);

  /**
   * Returns whether this version precedes ("is older than") the other version.
   * This is equivalent to checking {@code this.compareTo(other) < 0}.
   *
   * @param other the version to compare with.
   * @return {@code true} if this version is a predecessor of the other version.
   */
  default boolean precedes(SemanticVersion other) {
    return compareTo(other) < 0;
  }

  /**
   * Returns whether this version is succeeds ("is newer than") the other version.
   * This is equivalent to checking {@code this.compareTo(other) > 0}.
   *
   * @param other the version to compare with.
   * @return {@code true} if this version is a successor of the other version.
   */
  default boolean succeeds(SemanticVersion other) {
    return compareTo(other) > 0;
  }

  /**
   * Returns whether this semantic version is equal to the given object.
   * <p>
   * <b>Note:</b> Two semantic versions are considered equal if major version, minor version,
   * patch version, pre-release version and build metadata are equal. This differs from
   * {@link SemanticVersion#compareTo(Object)}, as build metadata is considered
   * for the {@code equals} method only. If you want to check if two semantic versions are equal, you can:
   * <ul>
   * <li>strip the build metadata before using this method</li>
   * <li>use {@code versionA.compareTo(versionB) == 0}</li>
   * <li>use {@code !(versionA.succeeds(versionB) || versionA.precedes(versionB))}</li>
   * </ul>
   *
   * @param o the object to compare to.
   * @return {@code true} if both semantic versions are equal.
   */
  @Override
  boolean equals(Object o);
}
