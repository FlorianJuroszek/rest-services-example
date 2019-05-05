package fr.florian.juroszek.Repository;

import fr.florian.juroszek.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * The User Repository allows to access to DB.
 * For example, to find an user by his email
 */
@RepositoryRestResource(collectionResourceRel = "user", path = "user")
public interface UserRepository extends MongoRepository<User, String> {

    /**
     * Find a user by his email
     *
     * @param email the user's email
     * @return a user if there is one with this email, else null
     */
    User findByEmail(@Param(value = "email") String email);
}
