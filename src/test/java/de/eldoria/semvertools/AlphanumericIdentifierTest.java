/*
 * SPDX-License-Identifier: MIT
 *
 * Copyright (c) 2021 Eldoria
 */

package de.eldoria.semvertools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AlphanumericIdentifierTest {

  @Test
  void test_compareAlphanumeric() {
    AlphanumericIdentifier a0 = new AlphanumericIdentifier("a0");
    AlphanumericIdentifier a1 = new AlphanumericIdentifier("a1");
    assertTrue(a0.compareTo(a1) < 0);
    assertTrue(a1.compareTo(a0) > 0);
  }

  @Test
  void test_compareAlphanumeric2() {
    AlphanumericIdentifier a0 = new AlphanumericIdentifier("a0");
    AlphanumericIdentifier b0 = new AlphanumericIdentifier("b0");
    assertTrue(a0.compareTo(b0) < 0);
    assertTrue(b0.compareTo(a0) > 0);
  }

  @Test
  void test_compareAlphanumeric3() {
    AlphanumericIdentifier a = new AlphanumericIdentifier("a");
    AlphanumericIdentifier aa = new AlphanumericIdentifier("aa");
    assertTrue(a.compareTo(aa) < 0);
    assertTrue(aa.compareTo(a) > 0);
  }

  @Test
  void test_compareAlphanumeric4() {
    AlphanumericIdentifier a = new AlphanumericIdentifier("alpha");
    AlphanumericIdentifier aa = new AlphanumericIdentifier("beta");
    assertTrue(a.compareTo(aa) < 0);
    assertTrue(aa.compareTo(a) > 0);
  }

}