package com.aalplotnikov;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private InputStream originalIn;

    @BeforeEach
    void setUp() {
        originalIn = System.in;
    }

    @AfterEach
    void tearDown() {
        System.setIn(originalIn);
    }

    @Test
    void saveTest() {
        String name = "TestName";
        String email = "test@mail.ru";
        int age = 30;

        String input = String.join("\n", "Create", name, email, String.valueOf(age), "Q", "");

        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));

        UserService userService = new UserService(userRepository);

        User user = new User(name, email, age);
        when(userRepository.save(name, email, age)).thenReturn(user);

        userService.start();

        verify(userRepository).save(name, email, age);
    }

    @Test
    void findByIdTest() {
        long id = 1L;
        String name = "TestName";
        String email = "test@mail.ru";
        int age = 30;

        String input = String.join("\n", "Read", String.valueOf(id), "Q", "");

        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));

        UserService userService = new UserService(userRepository);

        User user = new User(name, email, age);
        when(userRepository.findById(id)).thenReturn(user);

        userService.start();

        verify(userRepository).findById(id);
    }

    @Test
    void updateTest() {
        long id = 1L;
        String name = "UpdatedName";
        String email = "updated@mail.ru";
        int age = 40;

        String input = String.join("\n", "Update", String.valueOf(id),
                name, email, String.valueOf(age), "Q", "");

        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));

        UserService userService = new UserService(userRepository);

        User updated = new User(name, email, age);
        when(userRepository.update(id, name, email, age)).thenReturn(updated);

        userService.start();

        verify(userRepository).update(id, name, email, age);
    }

    @Test
    void deleteTest() {
        long id = 1L;
        String name = "TestName";
        String email = "test@mail.ru";
        int age = 30;

        String input = String.join("\n", "Delete", String.valueOf(id), "Q", "");

        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));

        UserService userService = new UserService(userRepository);

        User deleted = new User(name, email, age);
        when(userRepository.delete(id)).thenReturn(deleted);

        userService.start();

        verify(userRepository).delete(id);
    }
}