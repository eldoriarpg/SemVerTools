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
import de.eldoria.semvertools.VersionParseException;
import de.eldoria.semvertools.util.Integers;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

@ApiStatus.Internal
public class SemVerParser {

  private final String rawVersionString;
  private final Queue<Token> tokens;

  public SemVerParser(String rawVersionString, Collection<Token> tokens) {
    this.rawVersionString = rawVersionString;
    this.tokens = new ArrayDeque<>(tokens);
  }

  public SemanticVersion parse() {
    try {
      return parseOptional(parseVersionCore());
    } catch (VersionParseException e) {
      throw new VersionParseException("Failed to parse version string '" + rawVersionString + "'", e);
    }
  }

  private SemanticVersion parseOptional(SemanticVersion core) {
    if (this.tokens.isEmpty()) {
      return core;
    }
    Token token;
    switch ((token = tokens.remove()).type()) {
      case HYPHEN:
        return parsePreRelease(core);
      case PLUS:
        return parseBuild(core);
      default:
        throw new UnexpectedTokenException(token, TokenType.HYPHEN, TokenType.PLUS);
    }
  }

  private SemanticVersion parsePreRelease(SemanticVersion version) {
    List<Identifier> identifiers = new ArrayList<>();
    while (true) {
      parsePreReleaseIdentifier(identifiers);
      if (tokens.isEmpty()) {
        return version.withPreRelease(PreRelease.of(identifiers));
      }
      Token next = this.tokens.remove();
      if (next.type() == TokenType.DOT) continue;
      if (next.type() == TokenType.PLUS) return parseBuild(version.withPreRelease(PreRelease.of(identifiers)));
      throw new UnexpectedTokenException(tokens.element(), TokenType.DOT, TokenType.PLUS);
    }
  }

  private void parsePreReleaseIdentifier(List<Identifier> identifiers) {
    Token token = this.tokens.element();
    switch (token.type()) {
      case NUMERIC:
        var id = token.id().orElseThrow(() -> new IllegalStateException("NUMERIC without id"));
        int i = Integers.parseNonNegativeInt(id)
            .orElseThrow(() -> new VersionParseException("pre-release must not include leading zeroes"));
        identifiers.add(Identifier.of(i));
        this.tokens.remove();
        break;
      case ALPHABETIC:
      case ALPHANUMERIC:
        String alphanumeric = token.id()
            .orElseThrow(() -> new IllegalStateException(token.type() + " without id"));
        identifiers.add(Identifier.of(alphanumeric));
        this.tokens.remove();
        break;
      case HYPHEN: // hyphens are allowed

      default:
        throw new UnexpectedTokenException(token);
    }
  }

  private SemanticVersion parseBuild(SemanticVersion version) {
    // TODO allow dash
    List<Identifier> identifiers = new ArrayList<>();
    while (true) {
      parsePreReleaseIdentifier(identifiers);
      if (tokens.isEmpty()) {
        return version.withBuild(Build.of(identifiers));
      }
      Token next = this.tokens.remove();
      if (next.type() == TokenType.DOT) continue;
      throw new UnexpectedTokenException(tokens.element(), TokenType.DOT);
    }
  }

  private SemanticVersion parseVersionCore() {
    int major = parseNumeric();
    expect(TokenType.DOT);
    int minor = parseNumeric();
    expect(TokenType.DOT);
    int patch = parseNumeric();
    return SemanticVersion.of(major, minor, patch);
  }

  private Token expect(TokenType type) {
    expectNotEmpty(tokens.isEmpty());
    Token token;
    if ((token = tokens.remove()).type() != type) {
      throw new UnexpectedTokenException(token, type);
    }
    return token;
  }

  private int parseNumeric() {
    Token expect = expect(TokenType.NUMERIC);
    String id = expect.id()
        .flatMap(s -> s.trim().isEmpty() ? Optional.empty() : Optional.of(s))
        .orElseThrow(() -> new IllegalArgumentException("NUMERIC without id"));
    if (id.length() == 1 && id.charAt(0) == '0') return 0;
    if (id.charAt(0) == '0') throw new VersionParseException("No leading zero allowed");
    return parseInt(id);
  }

  private int parseInt(String raw) {
    int result = 0;
    for (int i = 0; i < raw.length(); i++) {
      int charAsInt = raw.charAt(i) - '0';
      if (charAsInt < 0 || charAsInt > 9) throw new NumberFormatException(raw);
      result = 10 * result + charAsInt;
    }
    return result;
  }

  private void expectNotEmpty(boolean empty) {
    if (empty) {
      throw new VersionParseException("Expected more tokens");
    }
  }
}
