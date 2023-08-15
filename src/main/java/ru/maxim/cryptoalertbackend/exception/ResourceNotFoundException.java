package ru.maxim.cryptoalertbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.maxim.cryptoalertbackend.dto.ApiResponse;


@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    private final transient ApiResponse apiResponse;

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super();

        String message = String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue);

        apiResponse = new ApiResponse(Boolean.FALSE, message);
    }


    public ResourceNotFoundException(String message) {
        super();

        apiResponse = new ApiResponse(Boolean.FALSE, message);
    }

    public ApiResponse getApiResponse() {
        return apiResponse;
    }

}