/*
 * SPDX-License-Identifier: MIT
 *
 * Copyright (c) 2021 SemVerTools team and contributors
 */

package de.eldoria.semvertools;

import de.eldoria.semvertools.parser.VersionParseException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class SemanticVersionTest {
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
      // don't allow build before pre-release
      "1.0.0+a-b",
      // don't allow leading zeroes
      "01.1.0",
      "1.01.0",
      "0.0.01",
  })
  void test_failOnInvalid(String invalid) {
    assertThrows(VersionParseException.class, () -> SemanticVersion.parse(invalid));
  }


}
