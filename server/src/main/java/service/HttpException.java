package service;

public class HttpException extends Exception {

    private int statusCode;

    public HttpException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
