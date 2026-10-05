package com.aalplotnikov;


import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Main {
    public static void main(String[] args) {
        SessionFactory sessionFactory = new Configuration()
                .configure()
                .addAnnotatedClass(User.class)
                .buildSessionFactory();
        UserRepository userRepository = new UserRepository(sessionFactory);
        UserService service = new UserService(userRepository);
        service.start();
    }
}