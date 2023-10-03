package org.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

class Toy {
    private int id;
    private String name;
    private int quantity;
    private double weightPercent;

    public Toy(int id, String name, int quantity, double weightPercent) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.weightPercent = weightPercent;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getWeightPercent() {
        return weightPercent;
    }

    public void setWeightPercent(double weightPercent) {
        this.weightPercent = weightPercent;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

public class ToyStore {
    private List<Toy> toys;

    public ToyStore() {
        toys = new ArrayList<>();
    }

    public void addToy(int id, String name, int quantity, double weightPercent) {
        Toy toy = new Toy(id, name, quantity, weightPercent);
        toys.add(toy);
    }

    public void updateWeightPercent(int toyId, double weightPercent) {
        for (Toy toy : toys) {
            if (toy.getId() == toyId) {
                toy.setWeightPercent(weightPercent);
                break;
            }
        }
    }

    public void drawToys() {
        Random random = new Random();
        double totalWeight = toys.stream().mapToDouble(Toy::getWeightPercent).sum();
        double randomValue = random.nextDouble() * totalWeight;
        double cumulativeWeight = 0.0;

        for (Toy toy : toys) {
            cumulativeWeight += toy.getWeightPercent();
            if (randomValue <= cumulativeWeight) {
                if (toy.getQuantity() > 0) {
                    System.out.println("\nПоздравляем, вы выиграли: " + toy.getName() + "\n");
                    toy.setQuantity(toy.getQuantity() - 1);
                } else {
                    System.out.println("Извините, данной игрушки больше нет в наличии.");
                }
                break;
            }
        }
    }

    public static void main(String[] args) {
        ToyStore toyStore = new ToyStore();
        toyStore.addToy(1, "Плюшевый медведь", 10, 20.0);
        toyStore.addToy(2, "Машинка", 15, 30.0);
        toyStore.addToy(3, "Попрыгунчик", 20, 10.0);
        toyStore.addToy(4, "Водный пистолет", 5, 50.0);

        Scanner scanner = new Scanner(System.in);
        int choice = 0;

        do {
            System.out.println("1. Розыгрыш игрушки");
            System.out.println("2. Изменить вес (частоту выпадения) игрушки");
            System.out.println("3. Выйти");
            System.out.print("\nВыберите действие: ");
            try {
                choice = scanner.nextInt();
            } catch (java.util.InputMismatchException e) {
                System.out.println("\nНекорректный ввод! Пожалуйста, введите число.");
                scanner.nextLine();
                continue;
            }

            switch (choice) {
                case 1:
                    toyStore.drawToys();
                    break;
                case 2:
                    System.out.println("Введите ID игрушки, для которой хотите изменить вес:");
                    int toyId;
                    double newWeightPercent;

                    try {
                        toyId = scanner.nextInt();
                    } catch (java.util.InputMismatchException e) {
                        System.out.println("Некорректный ввод ID! Пожалуйста, введите число.");
                        scanner.nextLine();
                        continue;
                    }

                    System.out.println("Введите новый вес (частоту выпадения) игрушки в процентах:");

                    try {
                        newWeightPercent = scanner.nextDouble();
                    } catch (java.util.InputMismatchException e) {
                        System.out.println("Некорректный ввод веса! Пожалуйста, введите число.");
                        scanner.nextLine();
                        continue;
                    }

                    toyStore.updateWeightPercent(toyId, newWeightPercent);
                    try (PrintWriter writer = new PrintWriter("toyWarehouse.txt")) {
                        for (Toy toy : toyStore.toys) {
                            writer.println("Игрушка: " + toy.getName() + ", Количество: " + toy.getQuantity() + ", Вес:" + toy.getWeightPercent());
                        }
                        System.out.println("Информация обновлена 'toyWarehouse.txt'.");
                    } catch (IOException e) {
                        System.out.println("Произошла ошибка при записи в файл.");
                    }
                    break;
                case 3:
                    System.out.println("Выходим...");
                    break;
                default:
                    System.out.println("Некорректный выбор! Пожалуйста, повторите попытку.");
            }
        } while (choice != 3);
    }
}