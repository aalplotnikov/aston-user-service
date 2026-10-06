package com.aalplotnikov;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class UserRepositoryTest {
    @Container
    private static final PostgreSQLContainer postgresContainer = new PostgreSQLContainer("postgres:16")
            .withDatabaseName("mydb")
            .withUsername("postgres")
            .withPassword("123");

    private static SessionFactory sessionFactory;
    private static UserRepository userRepository;

    @BeforeAll
    public static void setUp () {
        sessionFactory = new Configuration()
                .configure()
                .setProperty("hibernate.connection.url", postgresContainer.getJdbcUrl())
                .setProperty("hibernate.connection.username", postgresContainer.getUsername())
                .setProperty("hibernate.connection.password", postgresContainer.getPassword())
                .addAnnotatedClass(User.class)
                .buildSessionFactory();
        userRepository = new UserRepository(sessionFactory);
    }

    @AfterAll
    public static void tearDown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @AfterEach
    public void cleanDatabase() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.createMutationQuery("delete from User")
                    .executeUpdate();

            session.getTransaction().commit();
        }
    }

    @Test
    public void saveTest() {
        String name = "TestName";
        String email = "test@mail.ru";
        int age = 30;

        User userTest = new User(name, email, age);
        User saved = userRepository.save(userTest);
        assertNotNull(saved);

        try (Session session = sessionFactory.openSession()) {
            User userLoaded = session.find(User.class, saved.getId());
            assertNotNull(userLoaded);
            assertEquals(saved.getId(), userLoaded.getId());
            assertEquals(name, userLoaded.getName());
            assertEquals(email, userLoaded.getEmail());
            assertEquals(age, userLoaded.getAge());
        }
    }

    @Test
    public void saveFieldsTest() {
        String name = "TestName";
        String email = "test@mail.ru";
        int age = 30;

        User saved = userRepository.save(name, email, age);
        assertNotNull(saved);

        try (Session session = sessionFactory.openSession()) {
            User userLoaded = session.find(User.class, saved.getId());
            assertNotNull(userLoaded);
            assertEquals(saved.getId(), userLoaded.getId());
            assertEquals(name, userLoaded.getName());
            assertEquals(email, userLoaded.getEmail());
            assertEquals(age, userLoaded.getAge());
        }
    }

    @Test
    public void findByIdTest() {
        String name = "TestName";
        String email = "test@mail.ru";
        int age = 30;

        User userTest = new User(name, email, age);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(userTest);
            session.getTransaction().commit();
        }

        User userLoaded = userRepository.findById(userTest.getId());
        assertNotNull(userLoaded);
        assertEquals(userTest.getId(), userLoaded.getId());
        assertEquals(name, userLoaded.getName());
        assertEquals(email, userLoaded.getEmail());
        assertEquals(age, userLoaded.getAge());
    }

    @Test
    public void updateTest() {
        String name = "TestName";
        String email = "test@mail.ru";
        int age = 30;

        String updateName = "UpdateTestName";
        String updateEmail = "UpdateTest@mail.ru";
        int updateAge = 40;

        User userTest = new User(name, email, age);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(userTest);
            session.getTransaction().commit();
        }

        User saved = userRepository.update(userTest.getId(), updateName, updateEmail, updateAge);
        assertNotNull(saved);

        try (Session session = sessionFactory.openSession()) {
            User userLoaded = session.find(User.class, userTest.getId());
            assertNotNull(userLoaded);
            assertEquals(userTest.getId(), userLoaded.getId());
            assertEquals(updateName, userLoaded.getName());
            assertEquals(updateEmail, userLoaded.getEmail());
            assertEquals(updateAge, userLoaded.getAge());
        }
    }

    @Test
    public void deleteTest() {
        String name = "TestName";
        String email = "test@mail.ru";
        int age = 30;

        User userTest = new User(name, email, age);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(userTest);
            session.getTransaction().commit();
        }

        User deleted = userRepository.delete(userTest.getId());
        assertNotNull(deleted);
        assertEquals(userTest.getId(), deleted.getId());
        assertEquals(name, deleted.getName());
        assertEquals(email, deleted.getEmail());
        assertEquals(age, deleted.getAge());

        try (Session session = sessionFactory.openSession()) {
            User userLoaded = session.find(User.class, userTest.getId());
            assertNull(userLoaded);
        }
    }

    @Test
    public void findAllTest() {
        String name = "TestName";
        String email = "test@mail.ru";
        int age = 30;
        String nameOther = "OtherTestName";
        String emailOther = "Othertest@mail.ru";
        int ageOther = 40;

        int size = 2;

        User userTest = new User(name, email, age);
        User userOtherTest = new User(nameOther, emailOther, ageOther);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(userTest);
            session.persist(userOtherTest);
            session.getTransaction().commit();
        }

        List<User> userList = userRepository.findAll();
        assertNotNull(userList);
        assertEquals(size, userList.size());
        assertTrue(userList.stream()
                .anyMatch(user -> user.getId().equals(userTest.getId())
                        && user.getName().equals(userTest.getName())
                        && user.getEmail().equals(userTest.getEmail())
                        && user.getAge() == userTest.getAge()
                )
        );

        assertTrue(userList.stream()
                .anyMatch(user -> user.getId().equals(userOtherTest.getId())
                        && user.getName().equals(userOtherTest.getName())
                        && user.getEmail().equals(userOtherTest.getEmail())
                        && user.getAge() == userOtherTest.getAge()
                )
        );
    }
}
