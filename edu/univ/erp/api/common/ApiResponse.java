package edu.univ.erp.api.common;

public class ApiResponse<T> {
    private final boolean success;
    private final String message;
    private final T data;
    private final String errorCode;

    private ApiResponse(boolean success, String message, T data, String errorCode) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.errorCode = errorCode;
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        System.out.println("[DEBUG] ApiResponse.success(data, message) called -> message=" + message + ", hasData=" + (data != null));
        ApiResponse<T> response = new ApiResponse<>(true, message, data, null);
        System.out.println("[DEBUG] ApiResponse.success created -> success=" + response.success);
        return response;
    }

    public static <T> ApiResponse<T> success(String message) {
        System.out.println("[DEBUG] ApiResponse.success(message) called -> message=" + message);
        ApiResponse<T> response = new ApiResponse<>(true, message, null, null);
        System.out.println("[DEBUG] ApiResponse.success created -> success=" + response.success);
        return response;
    }

    public static <T> ApiResponse<T> error(String message, String errorCode) {
        System.out.println("[DEBUG] ApiResponse.error called -> message=" + message + ", errorCode=" + errorCode);
        ApiResponse<T> response = new ApiResponse<>(false, message, null, errorCode);
        System.out.println("[DEBUG] ApiResponse.error created -> success=" + response.success + ", errorCode=" + response.errorCode);
        return response;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public T getData() { return data; }
    public String getErrorCode() { return errorCode; }
}
