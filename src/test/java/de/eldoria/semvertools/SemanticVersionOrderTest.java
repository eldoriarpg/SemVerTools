/*
 * SPDX-License-Identifier: MIT
 *
 * Copyright (c) 2021 Eldoria
 */

package de.eldoria.semvertools;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class SemanticVersionOrderTest {
  private final List<String> versionStringsSorted = Arrays.asList(
      "1.0.0-alpha",
      "1.0.0-alpha.1",
      "1.0.0-alpha.beta",
      "1.0.0-beta",
      "1.0.0-beta.2",
      "1.0.0-beta.11",
      "1.0.0-rc.1",
      "1.0.0"
  );

  private final List<String> versionStringsShuffled = Arrays.asList(
      "1.0.0-beta",
      "1.0.0-alpha.1",
      "1.0.0",
      "1.0.0-rc.1",
      "1.0.0-alpha",
      "1.0.0-alpha.beta",
      "1.0.0-beta.11",
      "1.0.0-beta.2"
  );

  @Test
  void test_comparePreReleaseIdentifiersLengthDifference() {
    SemanticVersion less = SemanticVersion.parse("1.0.0-alpha");
    SemanticVersion greater = SemanticVersion.parse("1.0.0-alpha.1");
    assertTrue(less.compareTo(greater) < 0);
    assertTrue(greater.compareTo(less) > 0);
    List<SemanticVersion> versions = Arrays.asList(greater, less);
    assertNotEquals(Arrays.asList(less, greater), versions);
    Collections.sort(versions);
    assertEquals(Arrays.asList(less, greater), versions);
  }

  @Test
  void test_sortExamples() {
    List<SemanticVersion> unsorted = this.versionStringsShuffled.stream().map(SemanticVersion::parse)
        .collect(Collectors.toList());
    List<SemanticVersion> sorted = this.versionStringsSorted.stream().map(SemanticVersion::parse)
        .collect(Collectors.toList());
    assertNotEquals(sorted, unsorted);
    Collections.sort(unsorted);
    assertIterableEquals(sorted, unsorted);
  }

  @Test
  void test_buildDoesNotMatter() {
    SemanticVersion build123 = SemanticVersion.of(1, 2, 3)
        .withBuild(Build.of(Collections.singletonList(Identifier.of("build123"))));
    SemanticVersion build100 = SemanticVersion.of(1, 2, 3)
        .withBuild(Build.of(Collections.singletonList(Identifier.of("build100"))));
    assertEquals(0, build100.compareTo(build123));
    assertEquals(0, build123.compareTo(build100));
  }

}