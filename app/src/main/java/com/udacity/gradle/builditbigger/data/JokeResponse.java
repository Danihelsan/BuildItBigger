package com.udacity.gradle.builditbigger.data;

/**
 * Created by Danihelsan
 */
public class JokeResponse {
    boolean isError;
    Object  response;

    public JokeResponse(String response) {
        this.isError = false;
        this.response = response;
    }

    public JokeResponse(boolean isError, Object response) {
        this.isError = isError;
        this.response = response;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }
}
