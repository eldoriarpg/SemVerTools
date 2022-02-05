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

/**
 * Represents either an alphanumeric or a numerical identifier.
 * Identifiers are {@link Comparable} as described in <a href="https://semver.org/#spec-item-11">Item 11</a> of the spec.
 *
 * @apiNote This interface is not meant to be implemented and might become {@code sealed} in future releases.
 */
@ApiStatus.NonExtendable
public interface Identifier extends Comparable<Identifier> {

  /**
   * Returns a numerical identifier representing the given non-negative integer.
   *
   * @param num a non-negative integer value.
   * @return a numerical identifier for the given integer.
   */
  static Identifier of(int num) {
    Integers.assertNonNegative(num);
    return new NumericalIdentifier(num);
  }

  /**
   * Returns an alphanumeric identifier for the given string.
   *
   * @param alphanumeric a string only containing alphanumeric characters.
   * @return a new alphanumeric identifier.
   */
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

  /**
   * Returns a {@link String} representation of this identifier. This normally equals
   * the string it was parsed from.
   *
   * @return a string representation of this identifier.
   */
  String asString();
}
