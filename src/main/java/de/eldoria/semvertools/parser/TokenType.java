/*
 * SPDX-License-Identifier: MIT
 *
 * Copyright (c) 2021 SemVerTools team and contributors
 */

package de.eldoria.semvertools.parser;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public enum TokenType {
  NUMERIC,
  ALPHABETIC,
  ALPHANUMERIC,
  DOT,
  HYPHEN,
  PLUS;
}
