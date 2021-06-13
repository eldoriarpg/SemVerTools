package de.eldoria.semvertools;

import java.util.Objects;

class AlphanumericIdentifier implements Identifier {
    private final String identifier;

    AlphanumericIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String asString() {
        return this.identifier;
    }

    @Override
    public int compareTo(Identifier o) {
        return asString().compareTo(o.asString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlphanumericIdentifier that = (AlphanumericIdentifier) o;
        return identifier.equals(that.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier);
    }
}
