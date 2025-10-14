package hr.moremogucnosti.more_mogucnosti_backend.exception;

import hr.moremogucnosti.more_mogucnosti_backend.exception.response.ErrorResponse;
import hr.moremogucnosti.more_mogucnosti_backend.exception.response.ValidationErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

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

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleNotReadableException(HttpMessageNotReadableException ex, HttpServletRequest req) {
        var status = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(
                ErrorResponse.of(
                        status,
                        "Neispravan ili prazan request body.",
                        req.getRequestURI()),
                status
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatchException(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        var status = HttpStatus.BAD_REQUEST;
        var msg = "Neispravan tip parametra '" + ex.getName() + "'" +
                (ex.getValue() != null ? (" (vrijednost: " + ex.getValue() + ")") : "") + ".";
        return new ResponseEntity<>(
                ErrorResponse.of(
                        status,
                        msg,
                        req.getRequestURI()),
                status
        );
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParamException(MissingServletRequestParameterException ex, HttpServletRequest req) {
        var status = HttpStatus.BAD_REQUEST;
        var msg = "Nedostaje obavezni parametar: " + ex.getParameterName();
        return new ResponseEntity<>(
                ErrorResponse.of(
                        status,
                        msg,
                        req.getRequestURI()),
                status
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataViolationException(DataIntegrityViolationException ex, HttpServletRequest req) {
        var status = HttpStatus.CONFLICT;
        return new ResponseEntity<>(
                ErrorResponse.of(
                        status,
                        "Greška kod zapisa u bazu. Zapis krši neki constraint baze.",
                        req.getRequestURI()),
                status
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationErrorResponse> handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest req) {
        var status = HttpStatus.BAD_REQUEST;
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(e -> {
            var path = e.getPropertyPath().toString();
            var polje = path.contains(".") ? path.substring(path.lastIndexOf('.') + 1) : path;
            errors.put(polje, e.getMessage());
        });
        return new ResponseEntity<>(ValidationErrorResponse.of(
                status,
                "Neispravni parametri",
                req.getRequestURI(),
                errors),
                status
        );
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerException(NoHandlerFoundException ex, HttpServletRequest req) {
        var status = HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(
                ErrorResponse.of(
                        status,
                        "Ruta nije pronadjena.",
                        req.getRequestURI()),
                status
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(Exception ex, HttpServletRequest req) {
        var status = HttpStatus.FORBIDDEN;
        return new ResponseEntity<>(
                ErrorResponse.of(
                        status,
                        "Zabranjen pristup.",
                        req.getRequestURI()),
                status);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuth(Exception ex, HttpServletRequest req) {
        var status = HttpStatus.UNAUTHORIZED;
        return new ResponseEntity<>(
                ErrorResponse.of(
                        status,
                        "Autentikacija neuspješna.",
                        req.getRequestURI()),
                status);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest req) {
        var status = HttpStatus.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<>(
                ErrorResponse.of(
                        status,
                        "Dogodila se neočekivana greška.",
                        req.getRequestURI()),
                status
        );
    }
}
