/*
 * SPDX-License-Identifier: MIT
 *
 * Copyright (c) 2021 SemVerTools team and contributors
 */

package de.eldoria.semvertools;

import org.jetbrains.annotations.ApiStatus;

import java.util.List;

/**
 * This represents the build part of a semantic version.
 *
 * @apiNote This interface is not meant to be implemented and might become {@code sealed} in future releases.
 */
@ApiStatus.NonExtendable
public interface Build {

  static Build of(List<Identifier> identifiers) {
    return new BuildImpl(identifiers);
  }

  /**
   * Returns a string of the dot-separated identifiers of this build.
   *
   * @return a formatted build string.
   */
  String asString();

  /**
   * Returns the list of identifiers represented by this build.
   * The returned list is unmodifiable.
   *
   * @return the list of identifiers.
   */
  List<Identifier> identifiers();
}
