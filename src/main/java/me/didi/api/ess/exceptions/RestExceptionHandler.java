package me.didi.api.ess.exceptions;

import java.time.Instant;

import jakarta.servlet.http.HttpServletRequest;
import me.didi.api.ess.dtos.ApiErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler{

    @ExceptionHandler( { EssException.class } )
    public ResponseEntity<ApiErrorDTO> handleLojaException(EssException e, HttpServletRequest request){

        ApiErrorDTO err = new ApiErrorDTO();
        err.setTimestamp(Instant.now());
        err.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        err.setError("Erro no sistema :(");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(err.getStatus()).body(err);

    }

    @ExceptionHandler( { EntityNotFoundException.class } )
    public ResponseEntity<ApiErrorDTO> handleEntityNotFoundException(EntityNotFoundException e, HttpServletRequest request){

        ApiErrorDTO err = new ApiErrorDTO();
        err.setTimestamp(Instant.now());
        err.setStatus(HttpStatus.NOT_FOUND.value());
        err.setError("Recurso não encontrado");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(err.getStatus()).body(err);

    }

    @ExceptionHandler( { DataIntegrityViolationException.class } )
    public ResponseEntity<ApiErrorDTO> handleDataIntegrityViolationException(DataIntegrityViolationException e, HttpServletRequest request){

        ApiErrorDTO err = new ApiErrorDTO();
        err.setTimestamp(Instant.now());
        err.setStatus(HttpStatus.CONFLICT.value());
        err.setError("Data Integrity Violation");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(err.getStatus()).body(err);

    }

    @ExceptionHandler( { BadRequestBodyException.class } )
    public ResponseEntity<ApiErrorDTO> handleBadRequestBodyException(BadRequestBodyException e, HttpServletRequest request){

        ApiErrorDTO err = new ApiErrorDTO();
        err.setTimestamp(Instant.now());
        err.setStatus(HttpStatus.BAD_REQUEST.value());
        err.setError("Bad Request Body");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(err.getStatus()).body(err);

    }

}
