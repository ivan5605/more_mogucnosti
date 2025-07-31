package hr.moremogucnosti.more_mogucnosti_backend.exception;

public class DuplicateException extends RuntimeException {
    public DuplicateException(String message) {
        super(message);
    }
}
