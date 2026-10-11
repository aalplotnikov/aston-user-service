package com.aalplotnikov.DTO;

import java.time.LocalDateTime;

public record UserResponseDTO(
        Long id,
        String name,
        String email,
        int age,
        LocalDateTime createdAt
) {
}
