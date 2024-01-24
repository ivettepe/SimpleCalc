package com.simple.calc.utils;

import com.simple.calc.enums.Operator;
import com.simple.calc.entity.Pair;
import com.simple.calc.handler.FileHandler;
import com.simple.calc.handler.HandlerDB;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Класс ParserUtil предоставляет утилиты для разбора входных данных калькулятора.
 */
public class ParserUtil {

    /**
     * Метод parseArguments разбирает входные аргументы и возвращает их в виде пары значений.
     *
     * @param args массив строк с входными аргументами
     * @return пара значений, содержащая разобранные аргументы
     */
    public static Pair<String, String> parseArguments(String[] args) {
        if (args.length < 1 || args.length > 2) {
            throw new IllegalArgumentException("One or two arguments are required");
        }
        return new Pair<>(args[0], args.length == 2 ? args[1] : "");
    }

    /**
     * Метод parseCalculatedString разбирает строку с оператором и числами для вычисления.
     *
     * @param pairArgs пара значений, содержащая информацию о вводе данных
     * @return пара значений, содержащая разобранный оператор и числа
     * @throws IllegalArgumentException если входные данные имеют недопустимый формат
     */
    public static Pair<Operator, List<Double>> parseCalculatedString(Pair<String, String> pairArgs) {
        AtomicReference<Operator> operator = new AtomicReference<>();
        List<Double> numbers = new ArrayList<>();

        try {
            if (pairArgs.first().equals("-")) {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Enter the expression in the format {operator} {numbers}:");
                String input = scanner.nextLine();
                parseInput(input, operator, numbers);
            } else if (pairArgs.first().length() > 1) {
                if (pairArgs.first().equals("db")) {
                    HandlerDB.handleDBInput(operator, numbers);
                } else {
                    FileHandler.readFileInput(pairArgs.first(), operator, numbers);
                }
            } else {
                throw new IllegalArgumentException("Invalid input format");
            }
        } catch (IOException | IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        return new Pair<>(operator.get(), numbers);
    }

    /**
     * Метод parseInput разбирает строку ввода и заполняет оператор и список чисел.
     *
     * @param input    строка ввода, содержащая оператор и числа
     * @param operator ссылка на атомарное значение оператора
     * @param numbers  список чисел для заполнения
     * @throws IllegalArgumentException если строка имеет недопустимый формат
     */
    public static void parseInput(String input, AtomicReference<Operator> operator, List<Double> numbers) {
        String[] inputParts = input.split(" ", 2);
        if (inputParts.length != 2) {
            throw new IllegalArgumentException("Invalid input format");
        }
        String operatorString = inputParts[0];
        String numbersString = inputParts[1];
        parseOperator(operatorString, operator);
        parseNumbers(numbersString, numbers);
    }

    /**
     * Метод parseOperator определяет оператор на основе строки и устанавливает значение атомарной ссылки.
     *
     * @param operatorString строка, представляющая оператор
     * @param operator       ссылка на атомарное значение оператора
     * @throws IllegalArgumentException если оператор не поддерживается
     */
    private static void parseOperator(String operatorString, AtomicReference<Operator> operator) {
        operator.set(switch (operatorString) {
            case "add", "+", "sum" -> Operator.ADD;
            case "mul", "*", "multiply" -> Operator.MULTIPLY;
            case "spec_mul", "special_multiply" -> Operator.SPECIAL_MULTIPLY;
            case "pow", "^" -> Operator.POW;
            default -> throw new IllegalArgumentException("Unsupported operator: " + operatorString);
        });
    }

    /**
     * Метод parseNumbers парсит строку чисел и добавляет их в список.
     *
     * @param numbersString строка, представляющая числа, разделенные пробелами
     * @param numbers       список для заполнения числами
     * @throws IllegalArgumentException если формат чисел недопустим
     */
    private static void parseNumbers(String numbersString, List<Double> numbers) {
        try {
            String[] numbersArray = numbersString.trim().split("\\s+");
            for (String num : numbersArray) {
                numbers.add(Double.parseDouble(num));
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format");
        }
    }
}
