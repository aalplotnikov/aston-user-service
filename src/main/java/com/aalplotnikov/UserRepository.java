package com.aalplotnikov;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class UserRepository {
    private SessionFactory sessionFactory;
    private Session session;

    public UserRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public User save(String name, String email, int age) {
        try {
            User user = new User(name, email, age);
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
            return user;
        } catch (HibernateException e) {
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            System.out.println("Ошибка при сохранение " + e.getMessage());
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public User save(User user) {
        try{
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
            return user;
        } catch (HibernateException e) {
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            System.out.println("Ошибка при сохранение " + e.getMessage());
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public User findById(long id) {
        try {
            session = sessionFactory.openSession();
            User user = session.find(User.class, id);
            if (user == null) {
                System.out.println("Пользователь с " + id + " не найден");
                return null;
            }
            return user;
        } catch (HibernateException e) {
            System.out.println("Ошибка при поиске " + e.getMessage());
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public User update (Long id, String name, String email, int age) {
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            User user = session.find(User.class, id);
            if (user == null) {
                System.out.println("Пользователь с " + id + " не найден");
                session.getTransaction().rollback();
                return null;
            }
            user.setName(name);
            user.setEmail(email);
            user.setAge(age);
            session.getTransaction().commit();
            return user;
        } catch (HibernateException e) {
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            System.out.println("Ошибка при обновлении " + e.getMessage());
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public User delete (Long id) {
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            User user = session.find(User.class, id);
            if (user == null) {
                System.out.println("Пользователь с " + id + " не найден");
                session.getTransaction().rollback();
                return null;
            }
            session.remove(user);
            session.getTransaction().commit();
            return user;
        } catch (HibernateException e) {
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            System.out.println("Ошибка при удалении " + e.getMessage());
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public List<User> findAll() {
        try {
            session = sessionFactory.openSession();
            List<User> users = session
                    .createQuery("FROM User", User.class)
                    .getResultList();
            return users;
        } catch (HibernateException e) {
            System.out.println("Ошибка при поиске " + e.getMessage());
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
