package fr.florian.juroszek.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class IllegalUserCountryException extends RuntimeException {
    public IllegalUserCountryException() {
        super("Only users living in France can register");
    }
}
