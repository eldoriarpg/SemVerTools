/*
 * SPDX-License-Identifier: MIT
 *
 * Copyright (c) 2021 SemVerTools team and contributors
 */

package de.eldoria.semvertools.performance;

import de.eldoria.semvertools.Build;
import de.eldoria.semvertools.Identifier;
import de.eldoria.semvertools.PreRelease;
import de.eldoria.semvertools.SemanticVersion;
import org.openjdk.jmh.annotations.*;

import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@BenchmarkMode(Mode.AverageTime)
@State(Scope.Benchmark)
@Warmup(iterations = 3)
@Fork(1)
@Threads(6)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class RegexComparisonBenchmark {

  @Param(value = {"1.0.0", "1.0.0-alpha-a.b-c-somethinglong+build.1-aef.1-its-okay"})
  private String input;

  @Benchmark
  public SemanticVersion notCompiledPattern() {
    Pattern pattern = Pattern.compile("^(0|[1-9]\\d*)\\.(0|[1-9]\\d*)\\.(0|[1-9]\\d*)(?:-((?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*)(?:\\.(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*))*))?(?:\\+([0-9a-zA-Z-]+(?:\\.[0-9a-zA-Z-]+)*))?$");
    Matcher matcher = pattern.matcher(input);
    if (matcher.matches()) {
      int major = Integer.parseInt(matcher.group(1));
      int minor = Integer.parseInt(matcher.group(2));
      int patch = Integer.parseInt(matcher.group(3));
      SemanticVersion version = SemanticVersion.of(major, minor, patch);
      if (matcher.groupCount() >= 4) {
        version = version.withPreRelease(PreRelease.of(Collections.singletonList(Identifier.of(matcher.group(4)))));
      }
      if (matcher.groupCount() >= 5) {
        version = version.withBuild(Build.of(Collections.singletonList(Identifier.of(matcher.group(5)))));
      }
      return version;
    }
    return null;
  }

  @Benchmark
  public SemanticVersion semanticVersionParser() {
    return SemanticVersion.parse(input);
  }
}
