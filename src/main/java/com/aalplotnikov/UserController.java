package com.aalplotnikov;

import com.aalplotnikov.DTO.UserRequestDTO;
import com.aalplotnikov.DTO.UserResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/save")
    public ResponseEntity<UserResponseDTO> save(
            @RequestBody
            UserRequestDTO userRequestDTO
            ) {
        UserResponseDTO  userResponseDTO = userService.save(userRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userResponseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> get(
            @PathVariable
            Long id
    ) {
        UserResponseDTO  userResponseDTO = userService.findById(id);
        return ResponseEntity.ok(userResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(
            @PathVariable
            Long id,
            @RequestBody
            UserRequestDTO userRequestDTO
    ) {
        UserResponseDTO  userResponseDTO = userService.update(id, userRequestDTO);
        return ResponseEntity.ok(userResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable
            Long id
    ) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
