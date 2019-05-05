package fr.florian.juroszek.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.florian.juroszek.Repository.UserRepository;
import fr.florian.juroszek.model.User;
import fr.florian.juroszek.utils.Country;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The Registration Controller allow to create a new user or find one by his email
 */
@RestController
public class RegistrationController {

    private final UserRepository repository;

    public RegistrationController(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * Register a user
     *
     * @param email the user's email
     * @param firstName the user's first name
     * @param middleName the user's middle name
     * @param lastName the user's last name
     * @param age the user's age
     * @param country the user's country where he lives
     * @return a response entity with the registered user and an HTTP status
     */
    @RequestMapping("/register")
    public ResponseEntity<String> register(@RequestParam(value = "email") final String email,
                                           @RequestParam(value = "firstName") final String firstName,
                                           @RequestParam(value = "middleName", required = false) final String middleName,
                                           @RequestParam(value = "lastName") final String lastName,
                                           @RequestParam(value = "age") final int age,
                                           @RequestParam(value = "country") final Country country) {
        try {
            // Building a new user
            User user = new User(email, lastName, middleName, firstName, age, country);

            // Save it in DB
            repository.save(user);

            // Mapping the object to json string for return value
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(user);
            return new ResponseEntity<>(json, HttpStatus.OK);
        } catch (RuntimeException | JsonProcessingException e) {
            return new ResponseEntity<>("", HttpStatus.FORBIDDEN);
        }
    }

    /**
     * Get an existing user from the DB
     *
     * @param email the user's email
     * @return a response entity with the registered user and an HTTP status
     */
    @RequestMapping("/user/{email}")
    public ResponseEntity<String> getUser(@PathVariable(value = "email") final String email) {
        User user = repository.findByEmail(email);

        // If there is no user found
        if (user == null) {
            return new ResponseEntity<>("", HttpStatus.NOT_FOUND);
        }

        try {
            // Mapping the object to json string to return details
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(user);
            return new ResponseEntity<>(json, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("", HttpStatus.NOT_FOUND);
        }
    }
}