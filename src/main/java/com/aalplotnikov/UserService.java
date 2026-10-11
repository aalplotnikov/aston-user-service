package com.aalplotnikov;

import com.aalplotnikov.DTO.UserRequestDTO;
import com.aalplotnikov.DTO.UserResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Scanner;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDTO save (UserRequestDTO user) {
        try {
            User userEntity = userRepository.save(
                    new User(user.name(), user.email(), user.age())
            );
            log.info("Пользователь сохранен с id " + userEntity.getId());
            return new UserResponseDTO(
                    userEntity.getId(),
                    userEntity.getName(),
                    userEntity.getEmail(),
                    userEntity.getAge(),
                    userEntity.getCreatedAt()
            );
        } catch (DataAccessException e) {
            log.error("Ошибка Пользователь не сохранен", e);
            throw e;
        }
    }

    public UserResponseDTO findById (Long id) {
        try {
            User userEntity = userRepository.findById(id).orElseThrow();
            log.info("Пользователь найден с id " + userEntity.getId());
            return new UserResponseDTO(
                    userEntity.getId(),
                    userEntity.getName(),
                    userEntity.getEmail(),
                    userEntity.getAge(),
                    userEntity.getCreatedAt()
            );
        } catch (NoSuchElementException e) {
            log.error("Ошибка Пользователь не найден", e);
            throw e;
        } catch (DataAccessException e) {
            log.error("Ошибка c бд Пользователь не найден", e);
            throw e;
        }
    }

    public UserResponseDTO update(Long id, UserRequestDTO userRequestDTO) {
        try {
            User userEntity = userRepository.findById(id).orElseThrow();
            log.info("Пользователь найден с id " + userEntity.getId());

            userEntity.setName(userRequestDTO.name());
            userEntity.setEmail(userRequestDTO.email());
            userEntity.setAge(userRequestDTO.age());

            User userUpdate = userRepository.save(userEntity);

            log.info("Пользователь с id изменен " + userUpdate.getId());

            return new UserResponseDTO(
                    userUpdate.getId(),
                    userUpdate.getName(),
                    userUpdate.getEmail(),
                    userUpdate.getAge(),
                    userUpdate.getCreatedAt()
            );
        } catch (NoSuchElementException e) {
            log.error("Ошибка Пользователь не найден", e);
            throw e;
        } catch (DataAccessException e) {
            log.error("Ошибка c бд Пользователь не изменен", e);
            throw e;
        }
    }

    public void delete(Long id) {
        try {
            userRepository.deleteById(id);
            log.info("Пользователь удален с id " + id);
        } catch (DataAccessException e) {
            log.error("Ошибка Пользователь не удален", e);
            throw e;
        }
    }
}
