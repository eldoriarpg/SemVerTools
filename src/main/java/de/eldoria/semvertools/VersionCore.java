/*
 * SPDX-License-Identifier: MIT
 *
 * Copyright (c) 2021 Eldoria
 */

package de.eldoria.semvertools;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public final class VersionCore implements SemanticVersion {
  private final int major;
  private final int minor;
  private final int patch;

  VersionCore(int major, int minor, int patch) {
    this.major = major;
    this.minor = minor;
    this.patch = patch;
  }

  @Override
  public int major() {
    return this.major;
  }

  @Override
  public int minor() {
    return this.minor;
  }

  @Override
  public int patch() {
    return this.patch;
  }

  @Override
  public Optional<PreRelease> preRelease() {
    return Optional.empty();
  }

  @Override
  public Optional<Build> build() {
    return Optional.empty();
  }

  @Override
  public SemanticVersion withMajor(int major) {
    return new VersionCore(major, this.minor, this.patch);
  }

  @Override
  public SemanticVersion withMinor(int minor) {
    return new VersionCore(this.major, minor, this.patch);
  }

  @Override
  public SemanticVersion withPatch(int patch) {
    return new VersionCore(this.major, this.minor, patch);
  }

  @Override
  public int compareTo(SemanticVersion o) {
    if (this.major != o.major()) {
      return Integer.compare(this.major, o.major());
    }
    if (this.minor != o.minor()) {
      return Integer.compare(this.minor, o.minor());
    }
    if (this.patch != o.patch()) {
      return Integer.compare(this.patch, o.patch());
    }
    if (o.preRelease().isPresent()) return 1; // pre release is always less than a release
    return 0;
  }

  @Override
  public SemanticVersion withPreRelease(@Nullable PreRelease preRelease) {
    if (preRelease == null) return this;
    return new FullVersion(this, preRelease, null);
  }

  @Override
  public SemanticVersion withBuild(@Nullable Build build) {
    if (build == null) return this;
    return new FullVersion(this, null, build);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    VersionCore that = (VersionCore) o;
    return major == that.major && minor == that.minor && patch == that.patch;
  }

  @Override
  public int hashCode() {
    return Objects.hash(major, minor, patch);
  }

  @Override
  public String toString() {
    return String.format("%s.%s.%s", this.major, this.minor, this.patch);
  }
}
