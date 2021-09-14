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

@ApiStatus.NonExtendable
public interface SemanticVersion extends Comparable<SemanticVersion> {

  static SemanticVersion of(int major, int minor, int patch) {
    return new VersionCore(major, minor, patch);
  }

  static SemanticVersion parse(String version) {
    SemVerLexer lexer = new SemVerLexer();
    return new SemVerParser(version, lexer.lex(version)).parse();
  }

  int major();

  int minor();

  int patch();

  Optional<PreRelease> preRelease();

  Optional<Build> build();

  SemanticVersion withMajor(int major);

  default SemanticVersion increaseMajor() {
    return this.withMajor(this.major() + 1);
  }

  SemanticVersion withMinor(int minor);

  default SemanticVersion increaseMinor() {
    return this.withMinor(this.minor() + 1);
  }

  SemanticVersion withPatch(int patch);

  default SemanticVersion increasePatch() {
    return this.withPatch(this.patch() + 1);
  }

  SemanticVersion withPreRelease(@Nullable PreRelease preRelease);

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
}
