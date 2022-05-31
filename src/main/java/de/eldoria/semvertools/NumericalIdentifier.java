/*
 * SPDX-License-Identifier: MIT
 *
 * Copyright (c) 2021 SemVerTools team and contributors
 */

package de.eldoria.semvertools;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

final class NumericalIdentifier implements Identifier {
  private final int number;

  NumericalIdentifier(int number) {
    this.number = number;
  }

  @Override
  public int compareTo(@NotNull Identifier o) {
    if (o instanceof NumericalIdentifier) {
      return Integer.compare(this.number, ((NumericalIdentifier) o).number);
    }
    return asString().compareTo(o.asString());
  }

  @Override
  public String asString() {
    return String.valueOf(this.number);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NumericalIdentifier that = (NumericalIdentifier) o;
    return this.number == that.number;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.number);
  }
}
