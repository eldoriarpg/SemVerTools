/*
 * SPDX-License-Identifier: MIT
 *
 * Copyright (c) 2021 SemVerTools team and contributors
 */

package de.eldoria.semvertools;

import org.jetbrains.annotations.ApiStatus;

import java.util.List;

@ApiStatus.NonExtendable
public interface Build {

  static Build of(List<Identifier> identifiers) {
    return new BuildImpl(identifiers);
  }

  String asString();

  List<Identifier> identifiers();
}
