/*
 * SPDX-License-Identifier: MIT
 *
 * Copyright (c) 2021 SemVerTools team and contributors
 */

package de.eldoria.semvertools.parser;

import org.jetbrains.annotations.Range;

import java.util.Objects;
import java.util.Optional;

interface Token {

  TokenType type();

  @Range(from = 0, to = Integer.MAX_VALUE)
  int start();

  @Range(from = 0, to = Integer.MAX_VALUE)
  int end();

  Optional<String> id();

  static Token of(TokenType type, int start, int end) {
    return new SimpleToken(type, start, end);
  }

  static Token of(TokenType type, int position) {
    return of(type, position, position);
  }

  static Token of(TokenType type, String id, int start, int end) {
    return new IdToken(type, id, start, end);
  }

  final class SimpleToken extends AbstractToken {
    private final TokenType type;

    public SimpleToken(TokenType type, int start, int end) {
      super(start, end);
      this.type = type;
    }

    @Override
    public TokenType type() {
      return type;
    }

    @Override
    public Optional<String> id() {
      return Optional.empty();
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      SimpleToken that = (SimpleToken) o;
      return type == that.type;
    }

    @Override
    public int hashCode() {
      return Objects.hash(type);
    }

    @Override
    public String toString() {
      return "(" + type + ')';
    }
  }

  final class IdToken extends AbstractToken {
    private final TokenType type;
    private final String id;

    public IdToken(TokenType type, String id, int start, int end) {
      super(start, end);
      this.type = type;
      this.id = id;
    }

    @Override
    public TokenType type() {
      return type;
    }

    @Override
    public Optional<String> id() {
      return Optional.of(id);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      IdToken idToken = (IdToken) o;
      return type == idToken.type && id.equals(idToken.id);
    }

    @Override
    public int hashCode() {
      return Objects.hash(type, id);
    }

    @Override
    public String toString() {
      return "(" + type + ", " + id + ')';
    }
  }

  abstract class AbstractToken implements Token {
    final int start;
    final int end;

    protected AbstractToken(int start, int end) {
      this.start = start;
      this.end = end;
    }

    @Override
    public @Range(from = 0, to = Integer.MAX_VALUE) int start() {
      return start;
    }

    @Override
    public @Range(from = 0, to = Integer.MAX_VALUE) int end() {
      return start;
    }
  }
}
