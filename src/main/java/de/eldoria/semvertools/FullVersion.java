/*
 * SPDX-License-Identifier: MIT
 *
 * Copyright (c) 2021 SemVerTools team and contributors
 */

package de.eldoria.semvertools;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

final class FullVersion implements SemanticVersion {
  private final VersionCore versionCore;
  private final @Nullable PreRelease preRelease;
  private final @Nullable Build build;

  FullVersion(
      VersionCore versionCore,
      @Nullable PreRelease preRelease,
      @Nullable Build build
  ) {
    this.versionCore = versionCore;
    if (preRelease == null && build == null) {
      throw new IllegalStateException("Should be VersionCore");
    }
    this.preRelease = preRelease;
    this.build = build;
  }

  @Override
  public int major() {
    return this.versionCore.major();
  }

  @Override
  public int minor() {
    return this.versionCore.minor();
  }

  @Override
  public int patch() {
    return this.versionCore.patch();
  }

  @Override
  public Optional<PreRelease> preRelease() {
    return Optional.ofNullable(this.preRelease);
  }

  @Override
  public Optional<Build> build() {
    return Optional.ofNullable(this.build);
  }

  @Override
  public SemanticVersion withMajor(int major) {
    return new FullVersion(new VersionCore(major, minor(), patch()), this.preRelease, this.build);
  }

  @Override
  public SemanticVersion withMinor(int minor) {
    return new FullVersion(new VersionCore(major(), minor, patch()), this.preRelease, this.build);
  }

  @Override
  public SemanticVersion withPatch(int patch) {
    return new FullVersion(new VersionCore(major(), minor(), patch), this.preRelease, this.build);
  }

  @Override
  public SemanticVersion withPreRelease(@Nullable PreRelease preRelease) {
    if (preRelease == null && this.build == null) {
      return this.versionCore;
    }
    return new FullVersion(this.versionCore, preRelease, this.build);
  }

  @Override
  public SemanticVersion withBuild(@Nullable Build build) {
    if (build == null && this.preRelease == null) {
      return this.versionCore;
    }
    return new FullVersion(this.versionCore, this.preRelease, build);
  }

  @Override
  public int compareTo(SemanticVersion o) {
    int cmp = this.versionCore.compareTo(o.withBuild(null).withPreRelease(null)); // produce a VersionCore
    if (cmp == 0 && o instanceof VersionCore) {
      return -1; // this is definitely smaller if version-core is equal but only this is a pre-release
    }
    if (cmp != 0) {
      return cmp; // version-core already differs
    }
    FullVersion fullVersion = (FullVersion) o;
    if (this.preRelease == null && fullVersion.preRelease != null) {
      return 1;
    }
    if (this.preRelease != null && fullVersion.preRelease == null) {
      return -1;
    }
    if (this.preRelease == null) { // both are null, but build not part of the comparison
      return 0;
    }
    return this.preRelease.compareTo(fullVersion.preRelease);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FullVersion that = (FullVersion) o;
    return this.versionCore.equals(that.versionCore)
        && Objects.equals(this.preRelease, that.preRelease)
        && Objects.equals(this.build, that.build);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.versionCore, this.preRelease, this.build);
  }

  @Override
  public String toString() {
    String version = this.versionCore.toString();
    if (this.preRelease != null) {
      version += "-" + this.preRelease.asString();
    }
    if (this.build != null) {
      version += "+" + this.build.asString();
    }
    return version;
  }
}
