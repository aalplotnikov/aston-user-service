package com.aalplotnikov;


import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);

//        SessionFactory sessionFactory = new Configuration()
//                .configure()
//                .addAnnotatedClass(User.class)
//                .buildSessionFactory();
//        UserRepository userRepository = new UserRepository(sessionFactory);
//        UserService service = new UserService(userRepository);
//        service.start();
    }
}