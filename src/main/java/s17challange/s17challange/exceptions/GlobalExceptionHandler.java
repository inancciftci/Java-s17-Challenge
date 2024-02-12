package s17challange.s17challange.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ApiErrorResponse> handleApiException(ApiException exception){
        log.error("API exception occurred! Exception details: ");
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(exception.getHttpStatus().value(), exception.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(apiErrorResponse,exception.getHttpStatus());
    }

    @ExceptionHandler
    public ResponseEntity<ApiErrorResponse> handleAllException(Exception exception){
        log.error("Exception occurred!", exception.getMessage());
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(apiErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
