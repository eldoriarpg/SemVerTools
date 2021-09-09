/*
 * SPDX-License-Identifier: MIT
 *
 * Copyright (c) 2021 SemVerTools team and contributors
 */

package de.eldoria.semvertools.parser;

import de.eldoria.semvertools.Build;
import de.eldoria.semvertools.Identifier;
import de.eldoria.semvertools.PreRelease;
import de.eldoria.semvertools.SemanticVersion;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SemVerParserTest {

  @Test
  void test_parseVersionCore() {
    List<Token> oneDotZeroDotZero = Arrays.asList(
        Token.of(TokenType.NUMERIC, "1", 0, 0),
        Token.of(TokenType.DOT, 1),
        Token.of(TokenType.NUMERIC, "0", 2, 2),
        Token.of(TokenType.DOT, 3),
        Token.of(TokenType.NUMERIC, "0", 4, 4)
    );
    SemVerParser parser = new SemVerParser("1.0.0", oneDotZeroDotZero);
    SemanticVersion version = parser.parse();
    assertEquals(version, SemanticVersion.of(1, 0, 0));
  }

  @Test
  void test_parseVersionCoreWithBuild() {
    List<Token> coreWithBuild = Arrays.asList(
        Token.of(TokenType.NUMERIC, "1", 0, 0),
        Token.of(TokenType.DOT, 1),
        Token.of(TokenType.NUMERIC, "0", 2, 2),
        Token.of(TokenType.DOT, 3),
        Token.of(TokenType.NUMERIC, "0", 4, 4),
        Token.of(TokenType.PLUS, 5),
        Token.of(TokenType.ALPHABETIC, "hash", 6, 6),
        Token.of(TokenType.DOT, 7),
        Token.of(TokenType.ALPHANUMERIC, "a1b2c3", 8, 8)
    );
    SemVerParser parser = new SemVerParser("1.0.0+hash.a1b2c3", coreWithBuild);
    SemanticVersion version = parser.parse();
    assertEquals(version, SemanticVersion.of(1, 0, 0)
        .withBuild(Build.of(Arrays.asList(Identifier.of("hash"), Identifier.of("a1b2c3")))));
  }

  @Test
  void test_parseVersionCoreWithPreRelease() {
    List<Token> coreWithBuild = Arrays.asList(
        Token.of(TokenType.NUMERIC, "1", 0, 0),
        Token.of(TokenType.DOT, 1),
        Token.of(TokenType.NUMERIC, "0", 2, 2),
        Token.of(TokenType.DOT, 3),
        Token.of(TokenType.NUMERIC, "0", 4, 4),
        Token.of(TokenType.HYPHEN, 5),
        Token.of(TokenType.ALPHABETIC, "beta", 6, 6),
        Token.of(TokenType.DOT, 7),
        Token.of(TokenType.NUMERIC, "123", 8, 8)
    );
    SemVerParser parser = new SemVerParser("1.0.0-beta.123", coreWithBuild);
    SemanticVersion version = parser.parse();
    assertEquals(version, SemanticVersion.of(1, 0, 0)
        .withPreRelease(PreRelease.of(Arrays.asList(Identifier.of("beta"), Identifier.of(123)))));
  }

  @Test
  void test_parseVersionCoreWithPreReleaseAndBuild() {
    List<Token> coreWithBuild = Arrays.asList(
        Token.of(TokenType.NUMERIC, "1", 0, 0),
        Token.of(TokenType.DOT, 1),
        Token.of(TokenType.NUMERIC, "0", 2, 2),
        Token.of(TokenType.DOT, 3),
        Token.of(TokenType.NUMERIC, "0", 4, 4),
        Token.of(TokenType.HYPHEN, 5),
        Token.of(TokenType.ALPHABETIC, "beta", 6, 6),
        Token.of(TokenType.DOT, 7),
        Token.of(TokenType.NUMERIC, "123", 8, 8),
        Token.of(TokenType.PLUS, 9),
        Token.of(TokenType.ALPHABETIC, "hash", 10, 10),
        Token.of(TokenType.DOT, 11),
        Token.of(TokenType.ALPHANUMERIC, "a1b2c3", 12, 12)
    );
    SemVerParser parser = new SemVerParser("1.0.0-beta.123+hash.a1b2c3", coreWithBuild);
    SemanticVersion version = parser.parse();
    assertEquals(version, SemanticVersion.of(1, 0, 0)
        .withPreRelease(PreRelease.of(Arrays.asList(Identifier.of("beta"), Identifier.of(123))))
        .withBuild(Build.of(Arrays.asList(Identifier.of("hash"), Identifier.of("a1b2c3"))))
    );
  }

  @Test
  void test_parseVersionCoreWithHyphenedPreRelease() {
    List<Token> coreWithBuild = Arrays.asList(
        Token.of(TokenType.NUMERIC, "1", 0, 0),
        Token.of(TokenType.DOT, 1),
        Token.of(TokenType.NUMERIC, "0", 2, 2),
        Token.of(TokenType.DOT, 3),
        Token.of(TokenType.NUMERIC, "0", 4, 4),
        Token.of(TokenType.HYPHEN, 5),
        Token.of(TokenType.ALPHABETIC, "beta", 6, 6),
        Token.of(TokenType.DOT, 7),
        Token.of(TokenType.ALPHANUMERIC, "123-hash", 8, 8),
        Token.of(TokenType.DOT, 9),
        Token.of(TokenType.ALPHANUMERIC, "a1b2c3", 10, 10)
    );
    SemVerParser parser = new SemVerParser("1.0.0-beta,123-hash.a1b2c3", coreWithBuild);
    SemanticVersion version = parser.parse();
    assertEquals(version, SemanticVersion.of(1, 0, 0)
        .withPreRelease(PreRelease.of(Arrays.asList(
            Identifier.of("beta"),
            Identifier.of("123-hash"),
            Identifier.of("a1b2c3")
        )))
    );
  }

}