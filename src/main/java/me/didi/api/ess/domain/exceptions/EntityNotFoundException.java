package me.didi.api.ess.domain.exceptions;

public class EntityNotFoundException extends EssException{
    public EntityNotFoundException(String message) {
        super(message);
    }
}
