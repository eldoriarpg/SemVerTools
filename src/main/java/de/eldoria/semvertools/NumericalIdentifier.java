package de.eldoria.semvertools;

import java.util.Objects;

class NumericalIdentifier implements Identifier {
    private final int number;

    NumericalIdentifier(int number) {
        this.number = number;
    }

    @Override
    public int compareTo(Identifier o) {
        if (o instanceof NumericalIdentifier) {
            return Integer.compare(this.number, ((NumericalIdentifier) o).number);
        }
        return asString().compareTo(o.asString());
    }

    @Override
    public String asString() {
        return String.valueOf(this.number);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NumericalIdentifier that = (NumericalIdentifier) o;
        return number == that.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }
}
