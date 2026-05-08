package org.example.aiintegratedchatbot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDTO handleValidationException(MethodArgumentNotValidException ex) {
       String errorMessage = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();

       return new ErrorResponseDTO(
               LocalDateTime.now(),
               HttpStatus.BAD_REQUEST.value(),
               "VALIDATION_ERROR",
               errorMessage
       );
    }

    // 408
    @ExceptionHandler(ResourceAccessException.class)
    @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
    public ErrorResponseDTO handleTimeout(ResourceAccessException ex) {
        return new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.REQUEST_TIMEOUT.value(),
                "Request Timeout",
               "The request timed out. Please check your connection to the AI service."
        );
    }

    // 429
    @ExceptionHandler(HttpClientErrorException.TooManyRequests.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public ErrorResponseDTO handleToManyRequests(HttpClientErrorException.TooManyRequests ex) {
        return new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.TOO_MANY_REQUESTS.value(),
                "Too Many Requests",
                "Rate limit exceeded. Please wait a moment before sending more messages."
        );
    }

    // 500
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseDTO handleInternalServerError(Exception ex){
        return new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "An unexpected error occurred."
        );
    }

    // 502
    @ExceptionHandler(HttpServerErrorException.BadGateway.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public ErrorResponseDTO handleBadGateWay(HttpServerErrorException.BadGateway ex) {
        return new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_GATEWAY.value(),
                "Bad Gateway",
                "The AI gateway is temporarily unavailable."
        );
    }

    // 503
    @ExceptionHandler(AIServiceException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ErrorResponseDTO handleAIError(AIServiceException ex) {
        return new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                "AI_CONNECTION_ERROR",
                ex.getMessage()
        );
    }

    // 504
    @ExceptionHandler(HttpServerErrorException.GatewayTimeout.class)
    @ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
    public ErrorResponseDTO handleGateWayTimeout(HttpServerErrorException.GatewayTimeout ex) {
        return new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.GATEWAY_TIMEOUT.value(),
                "Gateway Timeout",
                "The upstream AI server is taking too long to respond."
        );
    }

}
