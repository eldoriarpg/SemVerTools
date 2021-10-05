/*
 * SPDX-License-Identifier: MIT
 *
 * Copyright (c) 2021 SemVerTools team and contributors
 */

package de.eldoria.semvertools;

import de.eldoria.semvertools.util.Integers;
import org.jetbrains.annotations.ApiStatus;

import java.util.Optional;
import java.util.function.Supplier;

@ApiStatus.NonExtendable
public interface Identifier extends Comparable<Identifier> {

  static Identifier of(int num) {
    Integers.assertNonNegative(num);
    return new NumericalIdentifier(num);
  }

  static Identifier of(String alphanumeric) {
    return Integers.parseNonNegativeInt(alphanumeric)
        .map(Identifier::of)
        .or(alphanumeric(alphanumeric))
        .orElseThrow(() -> new IllegalStateException("not a valid identifier: '" + alphanumeric + "'"));
  }

  private static Supplier<Optional<Identifier>> alphanumeric(String value) {
    return () -> {
      if (value.isBlank()) {
        return Optional.empty();
      }
      // single digit is not allowed as already covered by the int case
      // so we don't need to check it here again
      for (int i = 0; i < value.length(); i++) {
        char c = value.charAt(i);
        if (!(Character.isDigit(c) || Character.isLetter(c) || c == '-')) {
          return Optional.empty();
        }
      }
      return Optional.of(new AlphanumericIdentifier(value));
    };
  }

  String asString();
}
