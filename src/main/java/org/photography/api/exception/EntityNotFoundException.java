package org.photography.api.exception;

public class TagNotFoundException extends RuntimeException {

    public TagNotFoundException(Long tagId) {
        super("Tag not found with ID : " + tagId);
    }

}
