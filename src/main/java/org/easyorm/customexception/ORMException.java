package org.easyorm.customexception;

public class ORMException extends Exception {
    public ORMException(String message) {
        super(message);
    }

    public ORMException(String message, Throwable cause) {
        super(message, cause);
    }
}
