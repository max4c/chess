package dataAccess.Exception;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessException extends Exception {
    final private int statusCode;

    public DataAccessException(int statusCode, String message) {
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