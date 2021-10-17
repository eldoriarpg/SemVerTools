/*
 * SPDX-License-Identifier: MIT
 *
 * Copyright (c) 2021 SemVerTools team and contributors
 */

package de.eldoria.semvertools;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SemanticVersionTest {
  @ParameterizedTest
  @ValueSource(strings = {
      // don't allow totally random strings
      "1.1",
      "1",
      "1.a",
      "a.1",
      "",
      // we're not an IPv4 parser
      "1.1.1.1",
      // don't allow alphabetic characters in version core
      "1.a.1",
      // don't allow multiple pluses
      "1.0.0++",
      // don't allow leading zeroes
      "01.1.0",
      "1.01.0",
      "0.0.01",
      // leading zeroes are not allowed for numeric pre-release identifiers
      "1.0.0-001",
  })
  void test_failOnInvalid(String invalid) {
    assertThrows(VersionParseException.class, () -> SemanticVersion.parse(invalid));
  }

  @ParameterizedTest
  @ValueSource(strings = {
      // leading zeroes are allowed for build identifiers
      "0.0.1+0",
      "0.0.1+00",
      "0.0.1+001",
      // if a - comes after a +, it's part of an alphanumeric identifier
      "0.0.1+1-1",
      "0.0.1+a-b",
  })
  void test_succeedOnValid(String valid) {
    assertDoesNotThrow(() -> SemanticVersion.parse(valid));
  }


}
