/*
 * SPDX-License-Identifier: MIT
 *
 * Copyright (c) 2021 SemVerTools team and contributors
 */

package de.eldoria.semvertools.parser;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Range;

import java.util.Objects;
import java.util.Optional;

@ApiStatus.NonExtendable
public interface Token {

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

    private SimpleToken(TokenType type, int start, int end) {
      super(start, end);
      this.type = type;
    }

    @Override
    public TokenType type() {
      return this.type;
    }

    @Override
    public Optional<String> id() {
      return Optional.empty();
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      SimpleToken that = (SimpleToken) o;
      return this.type == that.type;
    }

    @Override
    public int hashCode() {
      return Objects.hash(this.type);
    }

    @Override
    public String toString() {
      return "(" + this.type + ')';
    }
  }

  final class IdToken extends AbstractToken {
    private final TokenType type;
    private final String id;

    private IdToken(TokenType type, String id, int start, int end) {
      super(start, end);
      this.type = type;
      this.id = id;
    }

    @Override
    public TokenType type() {
      return this.type;
    }

    @Override
    public Optional<String> id() {
      return Optional.of(this.id);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      IdToken idToken = (IdToken) o;
      return this.type == idToken.type && this.id.equals(idToken.id);
    }

    @Override
    public int hashCode() {
      return Objects.hash(this.type, this.id);
    }

    @Override
    public String toString() {
      return "(" + this.type + ", " + this.id + ')';
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
      return this.start;
    }

    @Override
    public @Range(from = 0, to = Integer.MAX_VALUE) int end() {
      return this.start;
    }
  }
}
