/*
 * SPDX-License-Identifier: MIT
 *
 * Copyright (c) 2021 SemVerTools team and contributors
 */

package de.eldoria.semvertools;

import org.jetbrains.annotations.ApiStatus;

import java.util.List;

/**
 * This represents the pre-release part of a semantic version.
 *
 * @apiNote This interface is not meant to be implemented and might become {@code sealed} in future releases.
 */
@ApiStatus.NonExtendable
public interface PreRelease extends Comparable<PreRelease> {

  /**
   * Creates a new pre-release with the given identifiers.
   *
   * @param identifiers the identifiers of the pre-release.
   * @return a new pre-release.
   */
  static PreRelease of(List<Identifier> identifiers) {
    return new PreReleaseImpl(identifiers);
  }

  /**
   * Returns a string of the dot-separated identifiers of this pre-release.
   *
   * @return a formatted pre-release string.
   */
  String asString();

  /**
   * Returns the list of identifiers represented by this pre-release.
   * The returned list is unmodifiable.
   *
   * @return the list of identifiers
   */
  List<Identifier> identifiers();
}
