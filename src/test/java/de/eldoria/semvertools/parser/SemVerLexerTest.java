/*
 * SPDX-License-Identifier: MIT
 *
 * Copyright (c) 2021 SemVerTools team and contributors
 */

package de.eldoria.semvertools.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SemVerLexerTest {

  private SemVerLexer semVerLexer;

  @BeforeEach
  public void setup() {
    this.semVerLexer = new SemVerLexer();
  }

  @ParameterizedTest
  @ValueSource(strings = {"0", "9", "1", "01", "000", "123", "999"})
  void test_lexNumeric(String input) {
    List<Token> lex = this.semVerLexer.lex(input);
    assertEquals(1, lex.size());
    assertEquals(Token.of(TokenType.NUMERIC, input, 0, 0), lex.get(0));
  }

  @ParameterizedTest
  @ValueSource(strings = {"a", "A", "z", "Z", "aaa", "abc", "AAA", "ABC", "zzz", "zyx", "ZZZ", "ZYX"})
  void test_lexAlphabetic(String input) {
    List<Token> lex = this.semVerLexer.lex(input);
    assertEquals(1, lex.size());
    assertEquals(Token.of(TokenType.ALPHABETIC, input, 0, 0), lex.get(0));
  }

  @ParameterizedTest
  @ValueSource(strings = {"a0", "A9", "9z", "0Z", "a1a", "2b2", "33A", "A44", "5zz", "zy6", "Z7Z", "Z8X"})
  void test_lexAlphanumeric(String input) {
    List<Token> lex = this.semVerLexer.lex(input);
    assertEquals(1, lex.size());
    assertEquals(Token.of(TokenType.ALPHANUMERIC, input, 0, 1), lex.get(0));
  }

  @Test
  void test_lexDot() {
    String vc = ".";
    List<Token> lex = this.semVerLexer.lex(vc);
    assertEquals(1, lex.size());
    assertEquals(Token.of(TokenType.DOT, 0), lex.get(0));
  }

  @Test
  void test_lexDash() {
    String vc = "-";
    List<Token> lex = this.semVerLexer.lex(vc);
    assertEquals(1, lex.size());
    assertEquals(Token.of(TokenType.HYPHEN, 0), lex.get(0));
  }

  @Test
  void test_lexPlus() {
    String vc = "+";
    List<Token> lex = this.semVerLexer.lex(vc);
    assertEquals(1, lex.size());
    assertEquals(Token.of(TokenType.PLUS, 0), lex.get(0));
  }

  @Test
  void test_lexPlusDashDot() {
    String pdd = "+-.";
    List<Token> lex = this.semVerLexer.lex(pdd);
    assertEquals(3, lex.size());
    assertEquals(Token.of(TokenType.PLUS, 0), lex.get(0));
    assertEquals(Token.of(TokenType.HYPHEN, 1), lex.get(1));
    assertEquals(Token.of(TokenType.DOT, 2), lex.get(2));
  }

  @Test
  void test_lexDelimitedAlphanumerics() {
    String input = "a0.1b.3c4";
    List<Token> lex = this.semVerLexer.lex(input);
    assertEquals(5, lex.size());
    assertEquals(Token.of(TokenType.ALPHANUMERIC, "a0", 0, 1), lex.get(0));
    assertEquals(Token.of(TokenType.DOT, 2), lex.get(1));
    assertEquals(Token.of(TokenType.ALPHANUMERIC, "1b", 3, 4), lex.get(2));
    assertEquals(Token.of(TokenType.DOT, 5), lex.get(3));
    assertEquals(Token.of(TokenType.ALPHANUMERIC, "3c4", 6, 8), lex.get(4));
  }

  @Test
  void test_lexMultiplehyphens() {
    String input = "hello-semantic-versioning";
    List<Token> lex = this.semVerLexer.lex(input);
    assertEquals(3, lex.size());
    assertEquals(Token.of(TokenType.ALPHABETIC, "hello", 0, 4), lex.get(0));
    assertEquals(Token.of(TokenType.HYPHEN, 5), lex.get(1));
    assertEquals(Token.of(TokenType.ALPHABETIC, "semantic-versioning", 6, 25), lex.get(2));
  }

}