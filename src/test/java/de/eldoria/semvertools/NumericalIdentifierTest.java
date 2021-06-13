package de.eldoria.semvertools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NumericalIdentifierTest {

    @Test
    void test_compareNumeric() {
        NumericalIdentifier n0 = new NumericalIdentifier(0);
        NumericalIdentifier n1 = new NumericalIdentifier(1);
        assertTrue(n0.compareTo(n1) < 0);
        assertTrue(n1.compareTo(n0) > 0);
    }

    @Test
    void test_compareNumeric2() {
        NumericalIdentifier n0 = new NumericalIdentifier(5);
        NumericalIdentifier n1 = new NumericalIdentifier(50);
        assertTrue(n0.compareTo(n1) < 0);
        assertTrue(n1.compareTo(n0) > 0);
    }

}