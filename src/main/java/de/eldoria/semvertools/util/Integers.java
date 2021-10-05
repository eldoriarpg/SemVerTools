/*
 * SPDX-License-Identifier: MIT
 *
 * Copyright (c) 2021 SemVerTools team and contributors
 */

package de.eldoria.semvertools.util;

import org.jetbrains.annotations.ApiStatus;

import java.util.Optional;

/**
 * A utility class for operations on integers.
 */
@ApiStatus.Internal
public enum Integers {
  ;

  /**
   * Parses non-negative integers.
   * <p>
   * This method does not accept strings with leading zeroes besides {@code "0"} itself.
   *
   * @param value the value to parse.
   * @return the parsed int wrapped by the OptionalInt, or {@link Optional#empty()} if
   * parsing was not successful.
   */
  public static Optional<Integer> parseNonNegativeInt(String value) {
    if (value.isEmpty()) {
      return Optional.empty();
    }
    // leading zeroes are not allowed
    if (value.charAt(0) == '0' && value.length() > 1) {
      return Optional.empty();
    }
    int result = 0;
    for (int i = 0; i < value.length(); i++) {
      int charAsInt = Character.digit(value.charAt(i), 10);
      if (charAsInt < 0 || charAsInt > 9) {
        return Optional.empty();
      }
      result = 10 * result + charAsInt;
      if (result < 0) { // overflow
        return Optional.empty();
      }
    }
    return Optional.of(result);
  }

  public static void assertNonNegative(int value) {
    if (value < 0) {
      throw new IllegalArgumentException("value must be non-negative");
    }
  }
}
