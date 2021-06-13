package de.eldoria.semvertools.parser;

import java.util.Objects;
import java.util.Optional;

public interface Token {

    TokenType type();

    Optional<String> id();

    static Token of(TokenType type) {
        return new SimpleToken(type);
    }

    static Token of(TokenType type, String id) {
        return new IdToken(type, id);
    }

    class SimpleToken implements Token {
        private final TokenType type;

        public SimpleToken(TokenType type) {
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

    class IdToken implements Token {
        private final TokenType type;
        private final String id;

        public IdToken(TokenType type, String id) {
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
}
