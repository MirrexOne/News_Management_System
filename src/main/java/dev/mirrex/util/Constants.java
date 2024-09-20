package dev.mirrex.util;

public interface Constants {
    String AUTHORIZATION = "Authorization";
    String BEARER = "Bearer ";
    String UUID_REGEX = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
    String PATH_PATTERN = "^(.+)\\/([^\\/]+)$";
    String HEADER_VALUES = "attachment; filename=";
    String SEPARATOR = ".";
    String NO_FILE_EXTENSION = "";
}
