/*
 * SPDX-License-Identifier: MIT
 *
 * Copyright (c) 2021 Eldoria
 */

package de.eldoria.semvertools;

public interface Identifier extends Comparable<Identifier> {

  static Identifier of(int num) {
    return new NumericalIdentifier(num);
  }

  static Identifier of(String alphanumeric) {
    return new AlphanumericIdentifier(alphanumeric);
  }

  String asString();
}
