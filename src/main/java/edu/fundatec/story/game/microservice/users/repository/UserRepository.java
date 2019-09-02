package edu.fundatec.story.game.microservice.users.repository;

import edu.fundatec.story.game.microservice.users.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByUsername(String username);
}
