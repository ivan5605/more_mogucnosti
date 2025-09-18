package hr.moremogucnosti.more_mogucnosti_backend.exception;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
