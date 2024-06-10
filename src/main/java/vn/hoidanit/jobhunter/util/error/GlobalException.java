package vn.hoidanit.jobhunter.util.error;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import vn.hoidanit.jobhunter.domain.dto.RestResponse;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(value = {
            UsernameNotFoundException.class,
            BadCredentialsException.class,
            IdInvalidException.class
    })
    public ResponseEntity<RestResponse<Object>> handleIdInvalidException(Exception ex) {

        var rs = new RestResponse<>();
        rs.setStatusCode(HttpStatus.BAD_REQUEST.value());
        rs.setError(ex.getMessage());
        rs.setMessage("Call api error");

        return ResponseEntity.badRequest().body(rs);
    }

    @ExceptionHandler(value = {
            NoResourceFoundException.class
    })
    public ResponseEntity<RestResponse<Object>> handleNoResourceFoundException(NoResourceFoundException m) {
        var rs = new RestResponse<>();
        rs.setStatusCode(m.getStatusCode().value());
        rs.setMessage("404 not found. URL may not exist...");
        rs.setError(m.getMessage());
        return ResponseEntity.badRequest().body(rs);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<RestResponse<Object>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException m) {

        BindingResult result = m.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();

        var rs = new RestResponse<>();
        rs.setStatusCode(HttpStatus.BAD_REQUEST.value());
        rs.setError(m.getBody().getDetail());

        var errors = new ArrayList<String>();
        for (FieldError x : fieldErrors) {
            errors.add(x.getDefaultMessage());
        }

        rs.setMessage(errors.size() > 1 ? errors : errors.get(0));
        return ResponseEntity.badRequest().body(rs);
    }

}
