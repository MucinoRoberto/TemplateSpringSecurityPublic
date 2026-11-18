package dev.mucino.backend.exceptions;

public class DataConflictException extends RuntimeException {

    public DataConflictException(String message){
        super(message);
    }

}
