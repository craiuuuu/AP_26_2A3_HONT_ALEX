package lab5.compulsory5.exception;


public class InvalidResourceException extends Exception
{
    public InvalidResourceException(String message)
    {
        super(message);
    }

    public InvalidResourceException(String message, Throwable cause)
    {
        super(message, cause);
    }
}