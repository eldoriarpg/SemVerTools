/*
 * SPDX-License-Identifier: MIT
 *
 * Copyright (c) 2021 Eldoria
 */

package de.eldoria.semvertools;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

final class BuildImpl implements Build {
  private final List<Identifier> identifiers;

  BuildImpl(List<Identifier> identifiers) {
    this.identifiers = identifiers;
  }

  @Override
  public String asString() {
    return this.identifiers.stream().map(Identifier::asString).collect(Collectors.joining("."));
  }

  @Override
  public List<Identifier> identifiers() {
    return Collections.unmodifiableList(this.identifiers);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BuildImpl that = (BuildImpl) o;
    if (this.identifiers.size() != that.identifiers.size()) return false;
    for (Iterator<Identifier> thisIter = this.identifiers.iterator(),
         thatIter = that.identifiers.iterator();
         thisIter.hasNext() && thatIter.hasNext(); ) {
      if (!thisIter.next().equals(thatIter.next())) return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hash(identifiers);
  }
}
