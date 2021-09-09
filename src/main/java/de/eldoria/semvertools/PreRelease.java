/*
 * SPDX-License-Identifier: MIT
 *
 * Copyright (c) 2021 SemVerTools team and contributors
 */

package de.eldoria.semvertools;

import java.util.List;

public interface PreRelease extends Comparable<PreRelease> {

  static PreRelease of(List<Identifier> identifiers) {
    return new PreReleaseImpl(identifiers);
  }

  String asString();

  List<Identifier> identifiers();
}
