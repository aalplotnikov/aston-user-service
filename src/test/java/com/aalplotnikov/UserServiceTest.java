package com.aalplotnikov;

import com.aalplotnikov.DTO.UserRequestDTO;
import com.aalplotnikov.DTO.UserResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void saveTest() {
        String name = "TestName";
        String email = "test@mail.ru";
        int age = 30;

        UserRequestDTO request = new UserRequestDTO(name, email, age);
        User user = new User(name, email, age);

        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO result = userService.save(request);

        assertEquals(name, result.name());
        assertEquals(email, result.email());
        assertEquals(age, result.age());

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(captor.capture());

        User savedUser = captor.getValue();

        assertEquals(name, savedUser.getName());
        assertEquals(email, savedUser.getEmail());
        assertEquals(age, savedUser.getAge());
    }

    @Test
    void findByIdTest() {
        Long id = 1L;
        String name = "TestName";
        String email = "test@mail.ru";
        int age = 30;

        User user = new User(name, email, age);

        when(userRepository.findById(id))
                .thenReturn(Optional.of(user));

        UserResponseDTO result = userService.findById(id);

        assertEquals(name, result.name());
        assertEquals(email, result.email());
        assertEquals(age, result.age());

        verify(userRepository).findById(id);
    }

    @Test
    void updateTest() {
        Long id = 1L;

        String name = "UpdatedName";
        String email = "updated@mail.ru";
        int age = 40;

        User existingUser = new User("OldName", "old@mail.ru", 25);

        UserRequestDTO request = new UserRequestDTO(name, email, age);

        when(userRepository.findById(id))
                .thenReturn(Optional.of(existingUser));

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserResponseDTO result = userService.update(id, request);

        assertEquals(name, result.name());
        assertEquals(email, result.email());
        assertEquals(age, result.age());

        verify(userRepository).findById(id);
        verify(userRepository).save(existingUser);

        assertEquals(name, existingUser.getName());
        assertEquals(email, existingUser.getEmail());
        assertEquals(age, existingUser.getAge());
    }

    @Test
    void deleteTest() {
        Long id = 1L;

        userService.delete(id);

        verify(userRepository).deleteById(id);
    }
}