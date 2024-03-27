package ui;

public class ResponseException extends Exception {

        private int statusCode;

        public ResponseException(int statusCode, String message) {
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
