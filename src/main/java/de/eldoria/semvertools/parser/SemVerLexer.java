/*
 * SPDX-License-Identifier: MIT
 *
 * Copyright (c) 2021 SemVerTools team and contributors
 */

package de.eldoria.semvertools.parser;

import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;

@ApiStatus.Internal
public class SemVerLexer {

  public List<Token> lex(String versionString) {
    try {
      return lexInternal(versionString);
    } catch (VersionParseException e) {
      throw new VersionParseException("Could not parse version string '" + versionString + "'", e);
    }
  }

  private List<Token> lexInternal(String versionString) {
    if (versionString.isEmpty() || versionString.trim().isEmpty()) {
      throw new VersionParseException("versionString must not be blank");
    }
    List<Token> tokens = new ArrayList<>();
    int mark = 0;
    int head = 0;
    boolean alphabetic = false;
    boolean numeric = false;
    boolean foundHyphen = false; // first hyphen indicates start of pre-release
    for (; head < versionString.length(); head++) {
      switch (versionString.charAt(head)) {
        case '.':
          if (mark < head) {
            tokens.add(produceToken(versionString, alphabetic, numeric, mark, head));
          }
          mark = head + 1;
          alphabetic = false;
          numeric = false;
          tokens.add(Token.of(TokenType.DOT, mark));
          break;
        case '+':
          if (mark < head) {
            tokens.add(produceToken(versionString, alphabetic, numeric, mark, head));
          }
          mark = head + 1;
          alphabetic = false;
          numeric = false;
          tokens.add(Token.of(TokenType.PLUS, mark));
          break;
        case '-':
          if (!foundHyphen) {
            if (mark < head) {
              tokens.add(produceToken(versionString, alphabetic, numeric, mark, head));
            }
            mark = head + 1;
            alphabetic = false;
            numeric = false;
            tokens.add(Token.of(TokenType.HYPHEN, mark));
            foundHyphen = true;
            break;
          } // fallthrough otherwise, it's part of an alphanumeric string
        default:
          alphabetic |= isAlphabetic(versionString.charAt(head));
          numeric |= isNumeric(versionString.charAt(head));
          break;
      }
    }
    if (mark < head) {
      tokens.add(produceToken(versionString, alphabetic, numeric, mark, head));
    }
    return tokens;
  }

  private boolean isAlphabetic(char c) {
    return ('a' <= c && 'z' >= c) || ('A' <= c && 'Z' >= c) || c == '-';
  }

  private boolean isNumeric(char c) {
    return '0' <= c && '9' >= c;
  }

  private Token produceToken(String full, boolean alphabetic, boolean numeric, int start, int end) {
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
