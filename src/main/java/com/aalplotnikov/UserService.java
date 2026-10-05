package com.aalplotnikov;

import java.util.Scanner;

public class UserService {
    private UserRepository userRepository;
    private Scanner scanner;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        scanner = new Scanner(System.in);
    }

    public void start() {


        String comand = "";

        while (!comand.equalsIgnoreCase("Q")) {
            System.out.println("Введите команду");
            System.out.println("Create что бы создать нового пользователя");
            System.out.println("Read что бы найти пользователя по id");
            System.out.println("Update что бы изменить пользователя");
            System.out.println("Delete что бы удалить пользователя по id");
            System.out.println("или Q для выхода из программы");
            comand = scanner.nextLine();

            switch (comand) {
                case "Create" -> save();
                case "Read" -> findById();
                case "Update" -> update();
                case "Delete" -> delete();
                case "Q", "q" -> {}
                default -> System.out.println("Команда не распознана");
            }
        }
    }

    private void save () {
        System.out.println("Введите имя нового пользователя");
        String name = scanner.nextLine();
        System.out.println("Введите email нового пользователя");
        String email = scanner.nextLine();

        Integer age = null;
        do {
            try {
                System.out.println("Введите возраст нового пользователя");
                age = Integer.parseInt(scanner.nextLine());
                if (age < 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                System.out.println("Возраст должен быть не отрицательным числом");
                 age = null;
            }
        } while (age == null);

        User user = userRepository.save(name, email, age);

        if (user != null) {
            System.out.println("Пользователь " + user + " сохранен");
        }
    }

    private void findById() {

        Long id = null;
        do {
            try {
                System.out.println("Введите id пользователя");
                id = Long.parseLong(scanner.nextLine());
                if (id <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                System.out.println("ID должен быть положительным числом");
                id = null;
            }
        } while (id == null);

        User user = userRepository.findById(id);

        if (user != null) {
            System.out.println(user);
        }
    }

    private void update() {
        Long id = null;
        do {
            try {
                System.out.println("Введите id пользователя которого хотите изменить");
                id = Long.parseLong(scanner.nextLine());
                if (id <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                System.out.println("ID должен быть положительным числом");
                id = null;
            }
        } while (id == null);

        System.out.println("Введите имя пользователя");
        String name = scanner.nextLine();
        System.out.println("Введите email пользователя");
        String email = scanner.nextLine();

        Integer age = null;
        do {
            try {
                System.out.println("Введите возраст пользователя");
                age = Integer.parseInt(scanner.nextLine());
                if (age < 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                System.out.println("Возраст должен быть не отрицательным числом");
                age = null;
            }
        } while (age == null);

        User user = userRepository.update(id, name, email, age);
        if (user != null) {
            System.out.println("Пользователь " + user + " обновлен");
        }
    }

    private void delete() {
        Long id = null;
        do {
            try {
                System.out.println("Введите id пользователя которого хотите удалить");
                id = Long.parseLong(scanner.nextLine());
                if (id <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                System.out.println("ID должен быть положительным числом");
                id = null;
            }
        } while (id == null);

        User user = userRepository.delete(id);
        if (user != null) {
            System.out.println("Пользователь " + user + " удален");
        }
    }
}
