package org.imaginnovate.test.exceptions;

public class DuplicateEmployeeIdException extends RuntimeException {
    public DuplicateEmployeeIdException(String message){
        super(message);
    }

}
