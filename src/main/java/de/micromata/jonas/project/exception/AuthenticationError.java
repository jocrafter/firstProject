package de.micromata.jonas.project.exception;

public class AuthenticationError extends RuntimeException{
    public AuthenticationError (String message){
        super(message);
    }
}
