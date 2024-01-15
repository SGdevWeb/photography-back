package org.photography.api.exception;

public class NonUniquePhotoUrlException extends RuntimeException{

    public NonUniquePhotoUrlException(String message) {
        super(message);
    }

}
