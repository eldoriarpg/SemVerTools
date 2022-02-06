/*
 * SPDX-License-Identifier: MIT
 *
 * Copyright (c) 2021 SemVerTools team and contributors
 */

package de.eldoria.semvertools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

final class PreReleaseImpl implements PreRelease {
  private final List<Identifier> identifiers;

  PreReleaseImpl(List<Identifier> identifiers) {
    this.identifiers = new ArrayList<>(identifiers);
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
  public int compareTo(PreRelease o) {
    List<Identifier> identifiers = o.identifiers();
    int smaller = Math.min(this.identifiers.size(), identifiers.size());
    for (int i = 0; i < smaller; i++) {
      int cmp = this.identifiers.get(i).compareTo(identifiers.get(i));
      if (cmp != 0) {
        return cmp;
      }
    }
    return Integer.compare(this.identifiers.size(), identifiers.size());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PreReleaseImpl that = (PreReleaseImpl) o;
    if (this.identifiers.size() != that.identifiers.size()) {
      return false;
    }
    for (Iterator<Identifier> thisIter = this.identifiers.iterator(), thatIter = that.identifiers.iterator();
         thisIter.hasNext() && thatIter.hasNext(); ) {
      if (!thisIter.next().equals(thatIter.next())) {
        return false;
      }
    }
    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.identifiers);
  }
}
