package edu.fundatec.story.game.microservice.users.controller;

import edu.fundatec.story.game.microservice.users.dto.UserDto;
import edu.fundatec.story.game.microservice.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.xml.ws.Response;

@CrossOrigin
@RestController
@RequestMapping("/v1/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity saveUser(@RequestBody UserDto userDto) {
        try {
            userService.save(userDto);
        } catch (DuplicateKeyException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This username has already been taken");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> doLogin(@RequestBody UserDto userDto) {
        UserDto userReturned;
        try {
            userReturned = userService.verifyCredentials(userDto.getUsername(), userDto.getPassword());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }

        if (userReturned.getId() == null) {
            return new ResponseEntity<>(userReturned, HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(userReturned, HttpStatus.ACCEPTED);
    }

    @GetMapping("/health")
    public ResponseEntity health() {
        return new ResponseEntity(HttpStatus.OK);
    }
}
