/*
 * SPDX-License-Identifier: MIT
 *
 * Copyright (c) 2021 SemVerTools team and contributors
 */

package de.eldoria.semvertools;

/**
 * This exception is thrown to indicate that parsing a semantic version was not successful.
 */
public class VersionParseException extends RuntimeException {

  /**
   * Creates a new VersionParseException with the specified message.
   *
   * @param message the message describing the exception details.
   */
  public VersionParseException(String message) {
    super(message);
  }

  /**
   * Creates a new VersionParseException with the specified message and the specified cause.
   *
   * @param message the message describing the exception details.
   * @param cause   the Throwable causing this exception.
   */
  public VersionParseException(String message, Throwable cause) {
    super(message, cause);
  }
}
