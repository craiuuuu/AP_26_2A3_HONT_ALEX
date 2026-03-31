package lab5.tema5;

public class CommandException extends Exception {
    public CommandException(String message, Throwable cause) {
        super(message, cause);
    }
    public CommandException(String message) {
        super(message);
    }
}