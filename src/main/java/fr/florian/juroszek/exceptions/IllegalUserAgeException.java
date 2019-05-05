package fr.florian.juroszek.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class IllegalUserAgeException extends RuntimeException {
    public IllegalUserAgeException() {
        super("Only adults can be registered");
    }
}
