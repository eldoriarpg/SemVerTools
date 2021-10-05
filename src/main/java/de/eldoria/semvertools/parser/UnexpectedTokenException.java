/*
 * SPDX-License-Identifier: MIT
 *
 * Copyright (c) 2021 SemVerTools team and contributors
 */

package de.eldoria.semvertools.parser;

import de.eldoria.semvertools.VersionParseException;

import java.util.Arrays;

public class UnexpectedTokenException extends VersionParseException {
  private final Token token;

  public UnexpectedTokenException(Token token, TokenType... expected) {
    super("Unexpected token " + token + " at position " + token.start() + ":" + token.end()
        + ". Expected any of" + Arrays.toString(expected) + " instead.");
    this.token = token;
  }

  public UnexpectedTokenException(Token token) {
    super("Unexpected token " + token + " at position " + token.start() + ":" + token.end() + ".");
    this.token = token;
  }

  public Token getToken() {
    return token;
  }
}
