package edu.fundatec.story.game.microservice.users.domain;

public class ExistingUserException extends Exception {

    public ExistingUserException(String message) {
        super(message);
    }
}
