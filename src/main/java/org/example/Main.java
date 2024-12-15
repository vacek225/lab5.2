package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

@Data
class Visitor {
    String name;                  // Имя посетителя
    String surname;               // Фамилия посетителя
    List<Book> favoriteBooks;     // Список любимых книг
}

@Data
@EqualsAndHashCode
class Book {
    String name;                  // Название книги
    String author;                // Автор книги
    int publishingYear;           // Год публикации
}

public class Main {
    private static final String VISITORS_JSON_PATH = "src/main/resources/books.json";
    private static final String MESSAGE_JANE_AUSTEN = "Is there any book by Jane Austen in favorites? ";

    public static void main(String[] args) {
        var visitors = parseJson(VISITORS_JSON_PATH);

        printVisitors(visitors);
        printUniqueFavoriteBooks(visitors);
        printBooksSortedByYear(visitors);
        checkJaneAustenFavorite(visitors);
        printMaxFavoriteBooks(visitors);
    }

    // Задание 1: Вывод посетителей
    private static void printVisitors(List<Visitor> visitors) {
        System.out.println("Visitors and their count:");
        visitors.forEach(visitor -> System.out.println(visitor.name + " " + visitor.surname));
        System.out.println("Total visitors: " + visitors.size());
    }

    // Задание 2: Уникальные книги
    private static void printUniqueFavoriteBooks(List<Visitor> visitors) {
        System.out.println("\nUnique favorite books and their count:");
        var uniqueBooks = visitors.stream()
                .flatMap(visitor -> visitor.favoriteBooks.stream())
                .collect(Collectors.toSet());

        uniqueBooks.forEach(book -> System.out.println(book.name + " by " + book.author));
        System.out.println("Total unique books: " + uniqueBooks.size());
    }

    // Задание 3: Сортировка книг по году
    private static void printBooksSortedByYear(List<Visitor> visitors) {
        System.out.println("\nBooks sorted by year of publication:");
        visitors.stream()
                .flatMap(visitor -> visitor.favoriteBooks.stream())
                .distinct()
                .sorted(Comparator.comparingInt(book -> book.publishingYear))
                .forEach(book -> System.out.println(book.name + " (" + book.publishingYear + ")"));
    }

    // Задание 4: Проверка книг Jane Austen
    private static void checkJaneAustenFavorite(List<Visitor> visitors) {
        var hasJaneAusten = visitors.stream()
                .flatMap(visitor -> visitor.favoriteBooks.stream())
                .anyMatch(book -> "Jane Austen".equals(book.author));
        System.out.println("\n" + MESSAGE_JANE_AUSTEN + hasJaneAusten);
    }

    // Задание 5: Максимальное число книг в избранном
    private static void printMaxFavoriteBooks(List<Visitor> visitors) {
        var maxFavorites = visitors.stream()
                .mapToInt(visitor -> visitor.favoriteBooks.size())
                .max()
                .orElse(0);
        System.out.println("\nMaximum number of favorite books by a visitor: " + maxFavorites);
    }

    // Метод для парсинга JSON
    private static List<Visitor> parseJson(String filePath) {
        try (var reader = new FileReader(filePath)) {
            Type visitorListType = new TypeToken<List<Visitor>>() {}.getType();
            return new Gson().fromJson(reader, visitorListType);
        } catch (IOException e) {
            System.err.println("Error reading JSON file: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
