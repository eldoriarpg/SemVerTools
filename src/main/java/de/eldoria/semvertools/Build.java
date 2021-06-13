package de.eldoria.semvertools;

import java.util.List;

public interface Build {

    static Build of(List<Identifier> identifiers) {
        return new BuildImpl(identifiers);
    }

    String asString();

    List<Identifier> identifiers();
}
