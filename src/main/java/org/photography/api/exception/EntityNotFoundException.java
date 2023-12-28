package org.photography.api.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String entityName, Long entityId) {
        super(entityName + " not found with ID : " + entityId);
    }

}
