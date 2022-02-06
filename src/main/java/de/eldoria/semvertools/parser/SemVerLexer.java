/*
 * SPDX-License-Identifier: MIT
 *
 * Copyright (c) 2021 SemVerTools team and contributors
 */

package de.eldoria.semvertools.parser;

import de.eldoria.semvertools.VersionParseException;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;

@ApiStatus.Internal
public final class SemVerLexer {
  private final List<Token> tokens;

  private SemVerLexer() {
    this.tokens = new ArrayList<>();
  }

  public static SemVerLexer create() {
    return new SemVerLexer();
  }

  public List<Token> lex(String versionString) {
    try {
      lexInternal(versionString);
    } catch (VersionParseException e) {
      throw new VersionParseException("Could not parse version string '" + versionString + "'", e);
    }
    return List.copyOf(this.tokens);
  }

  private void lexInternal(String versionString) {
    if (versionString.isEmpty() || versionString.trim().isEmpty()) {
      throw new VersionParseException("versionString must not be blank");
    }
    int mark = 0;
    int head = 0;
    boolean alphabetic = false;
    boolean numeric = false;
    boolean beforePreRelease = true; // whether we're before a possible pre-release part
    for (; head < versionString.length(); head++) {
      switch (versionString.charAt(head)) {
        case '.':
          if (mark < head) {
            this.tokens.add(produceToken(versionString, alphabetic, numeric, mark, head));
          }
          mark = head + 1;
          alphabetic = false;
          numeric = false;
          this.tokens.add(Token.of(TokenType.DOT, mark));
          break;
        case '+':
          if (mark < head) {
            this.tokens.add(produceToken(versionString, alphabetic, numeric, mark, head));
          }
          mark = head + 1;
          alphabetic = false;
          numeric = false;
          this.tokens.add(Token.of(TokenType.PLUS, mark));
          beforePreRelease = false; // a pre-release can't appear after a build
          break;
        case '-':
          if (beforePreRelease) {
            if (mark < head) {
              this.tokens.add(produceToken(versionString, alphabetic, numeric, mark, head));
            }
            mark = head + 1;
            alphabetic = false;
            numeric = false;
            this.tokens.add(Token.of(TokenType.HYPHEN, mark));
            beforePreRelease = false;
            break;
          }
          // fallthrough otherwise, it's part of an alphanumeric string
        default:
          alphabetic |= isAlphabetic(versionString.charAt(head));
          numeric |= isNumeric(versionString.charAt(head));
          break;
      }
    }
    if (mark < head) {
      this.tokens.add(produceToken(versionString, alphabetic, numeric, mark, head));
    }
  }

  private static boolean isAlphabetic(char c) {
    boolean isLowerCase = c >= 'a' && c <= 'z';
    boolean isUpperCase = c >= 'A' && c <= 'Z';
    return isLowerCase || isUpperCase || c == '-';
  }

  private static boolean isNumeric(char c) {
    return c >= '0' && c <= '9';
  }

  private static Token produceToken(String full, boolean alphabetic, boolean numeric, int start, int end) {
    String id = full.substring(start, end);
    if (alphabetic && numeric) {
      return Token.of(TokenType.ALPHANUMERIC, id, start, end - 1);
    }
    if (alphabetic) {
      return Token.of(TokenType.ALPHABETIC, id, start, end - 1);
    }
    if (numeric) {
      return Token.of(TokenType.NUMERIC, id, start, end - 1);
    }
    throw new IllegalArgumentException("Token with id '" + id + "' is neither alphabetic nor numeric");
  }
}
