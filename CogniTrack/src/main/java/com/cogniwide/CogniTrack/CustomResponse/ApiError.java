package com.cogniwide.CogniTrack.CustomResponse;

import lombok.Data;

@Data
public class ApiError {
    private String message;
    private static ApiError instance;
    private ApiError() {}
    public static synchronized ApiError getInstance() {
        if (instance == null) {
            instance = new ApiError();
        }
        return instance;
    }
    public static ApiError setError(String message) {
        ApiError apiError = new ApiError();
        apiError.setMessage(message);
        return apiError;
    }
}
