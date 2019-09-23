package edu.fundatec.story.game.microservice.users.service;

import edu.fundatec.story.game.microservice.users.domain.User;
import edu.fundatec.story.game.microservice.users.dto.UserDto;
import edu.fundatec.story.game.microservice.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save(UserDto userDto) {
        User user = new User(userDto.getUsername(), userDto.getPassword());
        userRepository.save(user);
    }

    public UserDto verifyCredentials(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            UserDto userDto = new UserDto();
            if (user.get().getPassword().equals(password)) {
                userDto.setId(user.get().getId());
                userDto.setUsername(user.get().getUsername());
            }
            return userDto;
        }
        throw new IllegalArgumentException("User not found");
    }
}
