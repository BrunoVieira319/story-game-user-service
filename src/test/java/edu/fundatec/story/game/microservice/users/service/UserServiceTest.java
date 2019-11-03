package edu.fundatec.story.game.microservice.users.service;

import edu.fundatec.story.game.microservice.users.domain.User;
import edu.fundatec.story.game.microservice.users.dto.UserDto;
import edu.fundatec.story.game.microservice.users.repository.UserRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @After
    public void dropDatabase() {
        userRepository.deleteAll();
    }

    @Test
    public void shouldSaveUser() {
        userService.save(createsUserDto());
        assertEquals(1, userRepository.count());
    }

    @Test
    public void shouldVerifyCredentials() {
        userRepository.save(new User("USER1", "123456"));

        UserDto authorized = userService.verifyCredentials("USER1", "123456");
        assertEquals("USER1", authorized.getUsername());
        assertNotNull(authorized.getId());
    }

    private UserDto createsUserDto() {
        UserDto userDto = new UserDto();
        userDto.setUsername("USER1");
        userDto.setPassword("123456");
        return userDto;
    }


}
