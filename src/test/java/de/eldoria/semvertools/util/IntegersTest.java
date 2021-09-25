package de.eldoria.semvertools.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.OptionalInt;

import static org.junit.jupiter.api.Assertions.*;

class IntegersTest {

  @Test
  void testParseEmpty() {
    assertEquals(OptionalInt.empty(), Integers.parseNonNegativeInt(""));
  }

  @ParameterizedTest
  @ValueSource(strings = {"00", "01", "09", "000", "00001"})
  void testParseLeadingZero(String value) {
    assertEquals(OptionalInt.empty(), Integers.parseNonNegativeInt(value));
  }

  @ParameterizedTest
  @ValueSource(strings = {"2147483648", "2147483648999"})
  void testOverflow(String value) {
    assertEquals(OptionalInt.empty(), Integers.parseNonNegativeInt(value));
  }

  @ParameterizedTest
  @ValueSource(strings = {"2147483647", "21474836"})
  void testDoNotOverflow(String value) {
    // use stdlib method here, assuming it works correct
    assertEquals(OptionalInt.of(Integer.parseInt(value)), Integers.parseNonNegativeInt(value));
  }

  @ParameterizedTest
  @ValueSource(strings = {"-1", "-123"})
  void testNegative(String value) {
    assertEquals(OptionalInt.empty(), Integers.parseNonNegativeInt(value));
  }

}