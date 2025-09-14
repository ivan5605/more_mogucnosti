package hr.moremogucnosti.more_mogucnosti_backend.api;

import org.springframework.http.HttpStatus;

import java.util.Map;

public record ValidationErrorResponse(
        int status,
        String error,
        String message,
        String path,
        Map<String, String> errors,
        String timestamp
) {
    public static ValidationErrorResponse of(HttpStatus status, String msg, String path, Map<String, String> errors){
        return new ValidationErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                msg,
                path,
                errors,
                java.time.LocalDateTime.now().toString()
        );
    }
}
