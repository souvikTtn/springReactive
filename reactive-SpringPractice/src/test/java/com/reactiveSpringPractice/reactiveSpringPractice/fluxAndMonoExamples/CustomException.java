package com.reactiveSpringPractice.reactiveSpringPractice.fluxAndMonoExamples;

public class CustomException extends Throwable {
    private String message;

    public CustomException(String error) {
        this.message = error;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
