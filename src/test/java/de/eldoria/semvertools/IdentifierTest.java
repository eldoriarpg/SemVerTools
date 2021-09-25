/*
 * SPDX-License-Identifier: MIT
 *
 * Copyright (c) 2021 SemVerTools team and contributors
 */

package de.eldoria.semvertools;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class IdentifierTest {

  @ParameterizedTest
  @ValueSource(ints = {0, 1, 2, Integer.MAX_VALUE, Integer.MAX_VALUE / 2})
  void testOfIntValid(int value) {
    assertDoesNotThrow(() -> Identifier.of(value));
    assertEquals(new NumericalIdentifier(value), Identifier.of(value));
  }

  @ParameterizedTest
  @ValueSource(ints = {-1, -2, Integer.MIN_VALUE})
  void testOfIntInvalid(int value) {
    assertThrows(IllegalArgumentException.class, () -> Identifier.of(value));
  }

  @ParameterizedTest
  @ValueSource(strings = {"a1", "b", "123c"})
  void testOfStringValidAlphaNumeric(String value) {
    assertDoesNotThrow(() -> Identifier.of(value));
  }

  @ParameterizedTest
  @ValueSource(strings = {"1", "2", "123"})
  void testOfStringValidNumeric(String value) {
    assertDoesNotThrow(() -> Identifier.of(value));
  }

  @ParameterizedTest
  @ValueSource(strings = {"-1", "-2", "-123"})
  void testOfStringInvalidNumeric(String value) {
    assertDoesNotThrow(() -> Identifier.of(value));
    assertEquals(new AlphanumericIdentifier(value), Identifier.of(value));
  }
}
