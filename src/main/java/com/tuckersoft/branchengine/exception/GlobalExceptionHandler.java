package com.tuckersoft.branchengine.exception;
import com.tuckersoft.branchengine.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.stream.Collectors;
@RestControllerAdvice
public class GlobalExceptionHandler {
    private ResponseEntity<ErrorResponse> b(HttpStatus s, String c, String m, HttpServletRequest r) {
        return ResponseEntity.status(s).body(ErrorResponse.of(c, m, r.getRequestURI()));
    }
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> nf(NotFoundException e, HttpServletRequest r){return b(HttpStatus.NOT_FOUND,"NOT_FOUND",e.getMessage(),r);}
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> cf(ConflictException e, HttpServletRequest r){return b(HttpStatus.CONFLICT,"CONFLICT",e.getMessage(),r);}
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> fb(ForbiddenException e, HttpServletRequest r){return b(HttpStatus.FORBIDDEN,"FORBIDDEN",e.getMessage(),r);}
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> ua(UnauthorizedException e, HttpServletRequest r){return b(HttpStatus.UNAUTHORIZED,"UNAUTHORIZED",e.getMessage(),r);}
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> br(BadRequestException e, HttpServletRequest r){return b(HttpStatus.BAD_REQUEST,"BAD_REQUEST",e.getMessage(),r);}
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> v(MethodArgumentNotValidException e, HttpServletRequest r){
        String m = e.getBindingResult().getFieldErrors().stream().map(f->f.getField()+": "+f.getDefaultMessage()).collect(Collectors.joining("; "));
        return b(HttpStatus.BAD_REQUEST,"VALIDATION_ERROR",m,r);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> ad(AccessDeniedException e, HttpServletRequest r){return b(HttpStatus.FORBIDDEN,"FORBIDDEN","Acceso denegado",r);}
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> au(AuthenticationException e, HttpServletRequest r){return b(HttpStatus.UNAUTHORIZED,"UNAUTHORIZED","Credenciales invalidas",r);}
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> g(Exception e, HttpServletRequest r){return b(HttpStatus.INTERNAL_SERVER_ERROR,"INTERNAL_ERROR",e.getMessage(),r);}
}
