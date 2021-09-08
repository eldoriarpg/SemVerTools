package de.eldoria.semvertools.parser;

public class VersionParseException extends RuntimeException {

  public VersionParseException(String message) {
    super(message);
  }

  public VersionParseException(String message, Throwable cause) {
    super(message, cause);
  }
}
