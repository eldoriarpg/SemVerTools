/*
 * SPDX-License-Identifier: MIT
 *
 * Copyright (c) 2021 SemVerTools team and contributors
 */

package de.eldoria.semvertools;

import de.eldoria.semvertools.parser.SemVerLexer;
import de.eldoria.semvertools.parser.SemVerParser;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

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

  SemanticVersion withMinor(int minor);

  SemanticVersion withPatch(int patch);

  SemanticVersion withPreRelease(@Nullable PreRelease preRelease);

  SemanticVersion withBuild(@Nullable Build build);
}
