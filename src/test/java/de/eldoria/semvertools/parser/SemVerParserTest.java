package de.eldoria.semvertools.parser;

import de.eldoria.semvertools.Build;
import de.eldoria.semvertools.Identifier;
import de.eldoria.semvertools.PreRelease;
import de.eldoria.semvertools.SemanticVersion;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SemVerParserTest {

    @Test
    void test_parseVersionCore() {
        List<Token> oneDotZeroDotZero = Arrays.asList(
                Token.of(TokenType.NUMERIC, "1"),
                Token.of(TokenType.DOT),
                Token.of(TokenType.NUMERIC, "0"),
                Token.of(TokenType.DOT),
                Token.of(TokenType.NUMERIC, "0")
        );
        SemVerParser parser = new SemVerParser(oneDotZeroDotZero);
        SemanticVersion version = parser.parse();
        assertEquals(version, SemanticVersion.of(1, 0, 0));
    }

    @Test
    void test_parseVersionCoreWithBuild() {
        List<Token> coreWithBuild = Arrays.asList(
                Token.of(TokenType.NUMERIC, "1"),
                Token.of(TokenType.DOT),
                Token.of(TokenType.NUMERIC, "0"),
                Token.of(TokenType.DOT),
                Token.of(TokenType.NUMERIC, "0"),
                Token.of(TokenType.PLUS),
                Token.of(TokenType.ALPHABETIC, "hash"),
                Token.of(TokenType.DOT),
                Token.of(TokenType.ALPHANUMERIC, "a1b2c3")
        );
        SemVerParser parser = new SemVerParser(coreWithBuild);
        SemanticVersion version = parser.parse();
        assertEquals(version, SemanticVersion.of(1, 0, 0)
                .withBuild(Build.of(Arrays.asList(Identifier.of("hash"), Identifier.of("a1b2c3")))));
    }

    @Test
    void test_parseVersionCoreWithPreRelease() {
        List<Token> coreWithBuild = Arrays.asList(
                Token.of(TokenType.NUMERIC, "1"),
                Token.of(TokenType.DOT),
                Token.of(TokenType.NUMERIC, "0"),
                Token.of(TokenType.DOT),
                Token.of(TokenType.NUMERIC, "0"),
                Token.of(TokenType.HYPHEN),
                Token.of(TokenType.ALPHABETIC, "beta"),
                Token.of(TokenType.DOT),
                Token.of(TokenType.NUMERIC, "123")
        );
        SemVerParser parser = new SemVerParser(coreWithBuild);
        SemanticVersion version = parser.parse();
        assertEquals(version, SemanticVersion.of(1, 0, 0)
                .withPreRelease(PreRelease.of(Arrays.asList(Identifier.of("beta"), Identifier.of(123)))));
    }

    @Test
    void test_parseVersionCoreWithPreReleaseAndBuild() {
        List<Token> coreWithBuild = Arrays.asList(
                Token.of(TokenType.NUMERIC, "1"),
                Token.of(TokenType.DOT),
                Token.of(TokenType.NUMERIC, "0"),
                Token.of(TokenType.DOT),
                Token.of(TokenType.NUMERIC, "0"),
                Token.of(TokenType.HYPHEN),
                Token.of(TokenType.ALPHABETIC, "beta"),
                Token.of(TokenType.DOT),
                Token.of(TokenType.NUMERIC, "123"),
                Token.of(TokenType.PLUS),
                Token.of(TokenType.ALPHABETIC, "hash"),
                Token.of(TokenType.DOT),
                Token.of(TokenType.ALPHANUMERIC, "a1b2c3")
        );
        SemVerParser parser = new SemVerParser(coreWithBuild);
        SemanticVersion version = parser.parse();
        assertEquals(version, SemanticVersion.of(1, 0, 0)
                .withPreRelease(PreRelease.of(Arrays.asList(Identifier.of("beta"), Identifier.of(123))))
                .withBuild(Build.of(Arrays.asList(Identifier.of("hash"), Identifier.of("a1b2c3"))))
        );
    }

    @Test
    void test_parseVersionCoreWithHyphenedPreRelease() {
        List<Token> coreWithBuild = Arrays.asList(
                Token.of(TokenType.NUMERIC, "1"),
                Token.of(TokenType.DOT),
                Token.of(TokenType.NUMERIC, "0"),
                Token.of(TokenType.DOT),
                Token.of(TokenType.NUMERIC, "0"),
                Token.of(TokenType.HYPHEN),
                Token.of(TokenType.ALPHABETIC, "beta"),
                Token.of(TokenType.DOT),
                Token.of(TokenType.ALPHANUMERIC, "123-hash"),
                Token.of(TokenType.DOT),
                Token.of(TokenType.ALPHANUMERIC, "a1b2c3")
        );
        SemVerParser parser = new SemVerParser(coreWithBuild);
        SemanticVersion version = parser.parse();
        assertEquals(version, SemanticVersion.of(1, 0, 0)
                .withPreRelease(PreRelease.of(Arrays.asList(
                        Identifier.of("beta"),
                        Identifier.of("123-hash"),
                        Identifier.of("a1b2c3")
                )))
        );
    }

}