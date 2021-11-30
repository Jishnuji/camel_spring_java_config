package exceptions;

public class IllegalFileFormatException extends RuntimeException{

    public IllegalFileFormatException() {
        super();
    }

    public IllegalFileFormatException(String message) {
        super(message);
    }
}
