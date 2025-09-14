package hr.moremogucnosti.more_mogucnosti_backend.exception;

import hr.moremogucnosti.more_mogucnosti_backend.api.ErrorResponse;
import hr.moremogucnosti.more_mogucnosti_backend.api.ValidationErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest req){
        var status = HttpStatus.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<>(
                ErrorResponse.of(
                        status,
                        "Dogodila se neočekivana greška.",
                        req.getRequestURI()),
                status
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest req){
        var status = HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(
                ErrorResponse.of(
                        status,
                        ex.getMessage(),
                        req.getRequestURI()),
                status
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest req){

        var status = HttpStatus.BAD_REQUEST;

        //ako @Valid padne onda spring ubaci sve greške u jedan objekt - BindingResult
        //baca MethodArgumentNotValidException, dobim tu iznimku i vadim greske van
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()){
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return new ResponseEntity<>(
                ValidationErrorResponse.of(
                        status,
                        "Neispravni podaci",
                        req.getRequestURI(),
                        errors),
                status
        );
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateException(DuplicateException ex, HttpServletRequest req){
        var status = HttpStatus.CONFLICT;
        return new ResponseEntity<>(
                ErrorResponse.of(
                        status,
                        ex.getMessage(),
                        req.getRequestURI()),
                status
        );
    }

    @ExceptionHandler(LozinkaException.class)
    public ResponseEntity<ErrorResponse> handleLozinke(LozinkaException ex, HttpServletRequest req){
        var status = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(
                ErrorResponse.of(
                        status,
                        ex.getMessage(),
                        req.getRequestURI()),
                status
        );
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex, HttpServletRequest req){
        var status = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(
                ErrorResponse.of(
                        status,
                        ex.getMessage(),
                        req.getRequestURI()),
                status
        );
    }

    @ExceptionHandler(InvalidLoginException.class)
    public ResponseEntity<ErrorResponse> handleInvalidLoginException(InvalidLoginException ex, HttpServletRequest req){
        var status = HttpStatus.UNAUTHORIZED;
        return new ResponseEntity<>(
                ErrorResponse.of(
                        status,
                        ex.getMessage(),
                        req.getRequestURI()),
                status
        );
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException ex, HttpServletRequest req) {
        var status = HttpStatus.FORBIDDEN;
        return new ResponseEntity<>(
                ErrorResponse.of(
                        status,
                        ex.getMessage(),
                        req.getRequestURI()),
                status
        );
    }
}
