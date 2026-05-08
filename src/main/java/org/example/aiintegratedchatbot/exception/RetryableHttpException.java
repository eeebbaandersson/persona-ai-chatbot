package org.example.aiintegratedchatbot.exception;

public class RetryableHttpException extends RuntimeException {
    public RetryableHttpException(String message) {
        super(message);
    }

}
