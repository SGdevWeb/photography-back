package org.photography.api.exception;

public class AlreadyExists extends RuntimeException {

    public AlreadyExists(String name) {
        super(name + " existe déjà");
    }

}
