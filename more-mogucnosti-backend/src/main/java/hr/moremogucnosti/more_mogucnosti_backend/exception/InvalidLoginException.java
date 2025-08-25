package hr.moremogucnosti.more_mogucnosti_backend.exception;

public class InvalidLoginException extends RuntimeException {
    public InvalidLoginException(String message) {
        super(message);
    }
}
