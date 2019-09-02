package edu.fundatec.story.game.microservice.users.domain;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Document
@Getter
public class User {

    @Id
    private String id;

    @NotNull
    @Size(min = 3, max = 16)
    @Indexed(unique = true)
    private String username;

    @NotNull
    @Size(min = 6, max = 30)
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
