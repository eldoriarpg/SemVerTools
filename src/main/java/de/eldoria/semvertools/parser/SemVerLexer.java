package de.eldoria.semvertools.parser;

import java.util.ArrayList;
import java.util.List;

public class SemVerLexer {

    public List<Token> lex(String versionString) {
        if (versionString == null) {
            throw new NullPointerException("versionString must not be null");
        }
        if (versionString.isEmpty() || versionString.trim().isEmpty()) {
            throw new IllegalArgumentException("versionString must not be blank");
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
                        tokens.add(produceToken(versionString.substring(mark, head), alphabetic, numeric));
                    }
                    mark = head + 1;
                    alphabetic = false;
                    numeric = false;
                    tokens.add(Token.of(TokenType.DOT));
                    break;
                case '+':
                    if (mark < head) {
                        tokens.add(produceToken(versionString.substring(mark, head), alphabetic, numeric));
                    }
                    mark = head + 1;
                    alphabetic = false;
                    numeric = false;
                    tokens.add(Token.of(TokenType.PLUS));
                    break;
                case '-':
                    if (!foundHyphen) {
                        if (mark < head) {
                            tokens.add(produceToken(versionString.substring(mark, head), alphabetic, numeric));
                        }
                        mark = head + 1;
                        alphabetic = false;
                        numeric = false;
                        tokens.add(Token.of(TokenType.HYPHEN));
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
            tokens.add(produceToken(versionString.substring(mark, head), alphabetic, numeric));
        }
        return tokens;
    }

    private boolean isAlphabetic(char c) {
        return 'a' <= c && 'z' >= c || 'A' <= c && 'Z' >= c || c == '-';
    }

    private boolean isNumeric(char c) {
        return '0' <= c && '9' >= c;
    }

    private Token produceToken(String id, boolean alphabetic, boolean numeric) {
        if (alphabetic && numeric) {
            return Token.of(TokenType.ALPHANUMERIC, id);
        }
        if (alphabetic) {
            return Token.of(TokenType.ALPHABETIC, id);
        }
        if (numeric) {
            return Token.of(TokenType.NUMERIC, id);
        }
        throw new IllegalArgumentException("Token with id '" + id + "' is neither alphabetic nor numeric");
    }
}
