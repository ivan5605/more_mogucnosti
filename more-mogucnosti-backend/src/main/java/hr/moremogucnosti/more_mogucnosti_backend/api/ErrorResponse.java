package hr.moremogucnosti.more_mogucnosti_backend.api;

import org.springframework.http.HttpStatus;

public record ErrorResponse(
        int status,
        String error,
        String message,
        String path,
        String timestamp
) {
    public static ErrorResponse of(HttpStatus status, String msg, String path) {
        return new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                msg,
                path,
                java.time.OffsetDateTime.now().toString()
        );
    }
}
