package dev.mirrex.util;

public interface Constants {
    String AUTHORIZATION = "Authorization";
    String BEARER = "Bearer ";
    String UUID_REGEX = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
    String PATH_PATTERN = "^(.+)\\/([^\\/]+)$";
    String HEADER_VALUES = "attachment; filename=\"%s\"";
    String SEPARATOR = ".";
    String NO_FILE_EXTENSION = "";
    String ANONYMOUS_USER = "anonymous";
    String ERROR_OCCURRED = "Exception occurred: %s, Handler: %s, Method: %s, User: %s, URL: %s, RequestMethod: %s, Status: %d";
    String REQUEST_COMPLETED = "Request completed: Handler: %s, Method: %s, User: %s, URL: %s, RequestMethod: %s, Status: %d";
    String INFO = "INFO";
    String ERROR = "ERROR";
}
