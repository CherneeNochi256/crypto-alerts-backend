package ru.maxim.cryptoalertbackend.exception;


import ru.maxim.cryptoalertbackend.dto.ApiResponse;

public class CanNotPerformActionException extends RuntimeException {

    private final transient ApiResponse apiResponse;

    public CanNotPerformActionException(String message) {
        super();
        apiResponse = new ApiResponse(Boolean.FALSE, message);
    }

    public ApiResponse getApiResponse() {
        return apiResponse;
    }

}
