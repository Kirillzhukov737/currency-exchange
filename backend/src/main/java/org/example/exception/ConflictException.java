package src.main.java.org.example.exception;

public class ConflictException extends ApiException{

    public ConflictException(String message) {
        super(message);
    }
}