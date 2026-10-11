package com.aalplotnikov;

import com.aalplotnikov.DTO.UserRequestDTO;
import com.aalplotnikov.DTO.UserResponseDTO;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void saveTest() throws Exception {

        UserResponseDTO response = new UserResponseDTO(
                1L,
                "TestName",
                "test@mail.ru",
                30,
                LocalDateTime.now()
        );

        when(userService.save(any(UserRequestDTO.class)))
                .thenReturn(response);

        mockMvc.perform(post("/save")
                        .contentType("application/json")
                        .content("""
                                {
                                    "name": "TestName",
                                    "email": "test@mail.ru",
                                    "age": 30
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("TestName"))
                .andExpect(jsonPath("$.email").value("test@mail.ru"))
                .andExpect(jsonPath("$.age").value(30))
                .andExpect(jsonPath("$.createdAt").exists());

        verify(userService).save(any(UserRequestDTO.class));
    }

    @Test
    void findByIdTest() throws Exception {

        Long id = 1L;

        UserResponseDTO response = new UserResponseDTO(
                id,
                "TestName",
                "test@mail.ru",
                30,
                LocalDateTime.now()
        );

        when(userService.findById(id))
                .thenReturn(response);

        mockMvc.perform(get("/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("TestName"))
                .andExpect(jsonPath("$.email").value("test@mail.ru"))
                .andExpect(jsonPath("$.age").value(30));

        verify(userService).findById(id);
    }

    @Test
    void updateTest() throws Exception {

        Long id = 1L;

        UserResponseDTO response = new UserResponseDTO(
                id,
                "UpdatedName",
                "updated@mail.ru",
                40,
                LocalDateTime.now()
        );

        when(userService.update(
                eq(id),
                any(UserRequestDTO.class)
        )).thenReturn(response);

        mockMvc.perform(put("/{id}", id)
                        .contentType("application/json")
                        .content("""
                                {
                                    "name": "UpdatedName",
                                    "email": "updated@mail.ru",
                                    "age": 40
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("UpdatedName"))
                .andExpect(jsonPath("$.email").value("updated@mail.ru"))
                .andExpect(jsonPath("$.age").value(40));

        verify(userService).update(
                eq(id),
                any(UserRequestDTO.class)
        );
    }

    @Test
    void deleteTest() throws Exception {

        Long id = 1L;

        mockMvc.perform(delete("/{id}", id))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(userService).delete(id);
    }
}