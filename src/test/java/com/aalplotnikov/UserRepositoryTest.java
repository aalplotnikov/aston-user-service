package com.aalplotnikov;

import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;

import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer postgresContainer =
            new PostgreSQLContainer("postgres:16")
                    .withDatabaseName("mydb")
                    .withUsername("postgres")
                    .withPassword("123");

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void saveTest() {
        String name = "TestName";
        String email = "test@mail.ru";
        int age = 30;

        User userTest = new User(name, email, age);

        User saved = userRepository.saveAndFlush(userTest);

        assertNotNull(saved);
        assertNotNull(saved.getId());

        entityManager.clear();

        User userLoaded = userRepository.findById(saved.getId())
                .orElseThrow();

        assertEquals(saved.getId(), userLoaded.getId());
        assertEquals(name, userLoaded.getName());
        assertEquals(email, userLoaded.getEmail());
        assertEquals(age, userLoaded.getAge());
    }

    @Test
    void saveFieldsTest() {
        String name = "TestName";
        String email = "test@mail.ru";
        int age = 30;

        User saved = userRepository.saveAndFlush(
                new User(name, email, age)
        );

        assertNotNull(saved.getId());

        entityManager.clear();

        User userLoaded = userRepository.findById(saved.getId())
                .orElseThrow();

        assertEquals(name, userLoaded.getName());
        assertEquals(email, userLoaded.getEmail());
        assertEquals(age, userLoaded.getAge());
        assertNotNull(userLoaded.getCreatedAt());
    }

    @Test
    void findByIdTest() {
        String name = "TestName";
        String email = "test@mail.ru";
        int age = 30;

        User userTest = new User(name, email, age);

        User saved = userRepository.saveAndFlush(userTest);

        entityManager.clear();

        User userLoaded = userRepository.findById(saved.getId())
                .orElseThrow();

        assertEquals(saved.getId(), userLoaded.getId());
        assertEquals(name, userLoaded.getName());
        assertEquals(email, userLoaded.getEmail());
        assertEquals(age, userLoaded.getAge());
    }

    @Test
    void updateTest() {
        String name = "TestName";
        String email = "test@mail.ru";
        int age = 30;

        String updateName = "UpdateTestName";
        String updateEmail = "UpdateTest@mail.ru";
        int updateAge = 40;

        User userTest = new User(name, email, age);

        User saved = userRepository.saveAndFlush(userTest);

        Long id = saved.getId();

        saved.setName(updateName);
        saved.setEmail(updateEmail);
        saved.setAge(updateAge);

        userRepository.saveAndFlush(saved);

        entityManager.clear();

        User userLoaded = userRepository.findById(id)
                .orElseThrow();

        assertEquals(id, userLoaded.getId());
        assertEquals(updateName, userLoaded.getName());
        assertEquals(updateEmail, userLoaded.getEmail());
        assertEquals(updateAge, userLoaded.getAge());
    }

    @Test
    void deleteTest() {
        String name = "TestName";
        String email = "test@mail.ru";
        int age = 30;

        User userTest = new User(name, email, age);

        User saved = userRepository.saveAndFlush(userTest);

        Long id = saved.getId();

        userRepository.deleteById(id);
        userRepository.flush();

        entityManager.clear();

        assertFalse(userRepository.existsById(id));
        assertTrue(userRepository.findById(id).isEmpty());
    }

    @Test
    void findAllTest() {
        String name = "TestName";
        String email = "test@mail.ru";
        int age = 30;

        String nameOther = "OtherTestName";
        String emailOther = "Othertest@mail.ru";
        int ageOther = 40;

        User userTest = new User(name, email, age);
        User userOtherTest = new User(nameOther, emailOther, ageOther);

        User savedFirst = userRepository.save(userTest);
        User savedSecond = userRepository.save(userOtherTest);

        userRepository.flush();
        entityManager.clear();

        List<User> userList = userRepository.findAll();

        assertNotNull(userList);
        assertEquals(2, userList.size());

        assertTrue(userList.stream()
                .anyMatch(user ->
                        user.getId().equals(savedFirst.getId())
                                && user.getName().equals(name)
                                && user.getEmail().equals(email)
                                && user.getAge() == age
                )
        );

        assertTrue(userList.stream()
                .anyMatch(user ->
                        user.getId().equals(savedSecond.getId())
                                && user.getName().equals(nameOther)
                                && user.getEmail().equals(emailOther)
                                && user.getAge() == ageOther
                )
        );
    }
}