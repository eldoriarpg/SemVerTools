package de.eldoria.semvertools.parser;

import de.eldoria.semvertools.Build;
import de.eldoria.semvertools.Identifier;
import de.eldoria.semvertools.PreRelease;
import de.eldoria.semvertools.SemanticVersion;
import de.eldoria.semvertools.VersionCore;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

public class SemVerParser {

    private final Queue<Token> tokens;

    public SemVerParser(Collection<Token> tokens) {
        this.tokens = new ArrayDeque<>(tokens);
    }

    public SemanticVersion parse() {
        VersionCore versionCore = parseVersionCore();
        return parseOptional(versionCore);
    }

    private SemanticVersion parseOptional(VersionCore core) {
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
                throw new IllegalArgumentException("Unexpected token " + token);
        }
    }

    private SemanticVersion parsePreRelease(VersionCore version) {
        List<Identifier> identifiers = new ArrayList<>();
        while (true) {
            parsePreReleaseIdentifier(identifiers);
            if (tokens.isEmpty()) {
                return version.withPreRelease(PreRelease.of(identifiers));
            }
            Token next = this.tokens.remove();
            if (next.type() == TokenType.DOT) continue;
            if (next.type() == TokenType.PLUS) return parseBuild(version.withPreRelease(PreRelease.of(identifiers)));
            throw new IllegalArgumentException("Unexpected token " + tokens.element());
        }
    }

    private void parsePreReleaseIdentifier(List<Identifier> identifiers) {
        Token token = this.tokens.element();
        switch (token.type()) {
            case NUMERIC:
                int i = Integer.parseInt(token.id()
                        .orElseThrow(() -> new IllegalArgumentException("NUMERIC without id")));
                identifiers.add(Identifier.of(i));
                this.tokens.remove();
                break;
            case ALPHABETIC:
            case ALPHANUMERIC:
                String alphanumeric = token.id()
                        .orElseThrow(() -> new IllegalArgumentException(token.type() + " without id"));
                identifiers.add(Identifier.of(alphanumeric));
                this.tokens.remove();
                break;
            case HYPHEN: // hyphens are allowed

            default:
                throw new IllegalArgumentException("Unexpected token " + token);
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
            throw new IllegalArgumentException("Unexpected token " + tokens.element());
        }
    }

    private void parseBuildIdentifier(List<Identifier> identifiers) {

    }

    private VersionCore parseVersionCore() {
        int major = parseNumeric();
        expect(TokenType.DOT);
        int minor = parseNumeric();
        expect(TokenType.DOT);
        int patch = parseNumeric();
        return (VersionCore) SemanticVersion.of(major, minor, patch);
    }

    private Token expect(TokenType type) {
        expectNotEmpty(tokens.isEmpty());
        Token token;
        if ((token = tokens.remove()).type() != type) {
            throw new IllegalArgumentException("Unexpected token " + token + ", expected toke of type " + type);
        }
        return token;
    }

    private int parseNumeric() {
        Token expect = expect(TokenType.NUMERIC);
        String id = expect.id()
                .flatMap(s -> s.trim().isEmpty() ? Optional.empty() : Optional.of(s))
                .orElseThrow(() -> new IllegalArgumentException("NUMERIC without id"));
        if (id.length() == 1 && id.charAt(0) == '0') return 0;
        if (id.charAt(0) == '0') throw new IllegalArgumentException("No leading zero allowed");
        int result = 0;
        for (char c : id.toCharArray()) {
            int charAsInt = c - '0';
            if (charAsInt < 0 || charAsInt > 9) throw new NumberFormatException(id);
            result = 10 * result + charAsInt;
        }
        return result;
    }

    private void expectNotEmpty(boolean empty) {
        if (empty) {
            throw new IllegalArgumentException("Expected more tokens");
        }
    }
}
