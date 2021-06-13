package de.eldoria.semvertools;

import de.eldoria.semvertools.parser.SemVerLexer;
import de.eldoria.semvertools.parser.SemVerParser;
import de.eldoria.semvertools.parser.Token;

import java.util.List;
import java.util.Optional;

public interface SemanticVersion extends Comparable<SemanticVersion> {

    static SemanticVersion of(int major, int minor, int patch) {
        return new VersionCore(major, minor, patch);
    }

    static SemanticVersion parse(String version) {
        SemVerLexer lexer = new SemVerLexer();
        List<Token> tokens = lexer.lex(version);
        return new SemVerParser(tokens).parse();
    }

    int major();
    
    int minor();
    
    int patch();
    
    Optional<PreRelease> preRelease();

    Optional<Build> build();

    SemanticVersion withMajor(int major);

    SemanticVersion withMinor(int minor);

    SemanticVersion withPatch(int patch);

    SemanticVersion withPreRelease(PreRelease preRelease);

    SemanticVersion withBuild(Build build);
}
