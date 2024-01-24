package com.simple.calc.utils;

import com.simple.calc.enums.Operator;
import com.simple.calc.entity.Pair;
import com.simple.calc.handler.FileHandler;
import com.simple.calc.handler.HandlerDB;

import java.util.List;

/**
 * Класс CalculatorUtil предоставляет утилиты для выполнения операций калькулятора и обработки результатов.
 */
public class CalculatorUtil {

    /**
     * Метод calculate выполняет операцию калькулятора на основе переданных входных данных.
     *
     * @param calculatedString пара, содержащая оператор и список чисел для вычисления
     * @param pairArgs         пара, откуда считывались вводные данные и куда производится вывод
     * @return результат вычисления
     */
    public static double calculate(Pair<Operator, List<Double>> calculatedString, Pair<String, String> pairArgs) {
        if (pairArgs.first().equals("db")) {
            List<Double> numbers = calculatedString.second();
            numbers.remove(numbers.size() - 1);
            calculatedString = new Pair<>(calculatedString.first(), numbers);
        }
        if (calculatedString.first() != null) {
            return switch (calculatedString.first()) {
                case ADD -> add(calculatedString.second());
                case MULTIPLY -> multiply(calculatedString.second());
                case SPECIAL_MULTIPLY -> specialMultiply(calculatedString.second());
                case POW -> pow(calculatedString.second());
                default -> throw new IllegalArgumentException("Unknown operator");
            };
        } else {
            throw new IllegalArgumentException("Operator can`t be null");
        }
    }

    /**
     * Метод calculateAndShow выполняет операцию калькулятора, форматирует результат и выводит его.
     *
     * @param calculatedString пара, содержащая оператор и список чисел для вычисления
     * @param pairArgs         пара, откуда считывались вводные данные и куда производится вывод
     */
    public static void calculateAndShow(Pair<Operator, List<Double>> calculatedString, Pair<String, String> pairArgs) {
        double resultValue = calculate(calculatedString, pairArgs);
        String resultString = "Result: " + resultValue;
        if (pairArgs.second().equals("-")) {
            System.out.println(resultString);
        } else if (pairArgs.second().length() > 1) {
            if (pairArgs.second().equals("db")) {
                HandlerDB.handleDBOutput(calculatedString, resultValue);
            } else {
                FileHandler.handleFileOutput(pairArgs, resultString);
            }
            if (pairArgs.first().equals("db") || pairArgs.second().equals("db")) {
                HandlerDB.closeScanner();
            }
        } else {
            throw new IllegalArgumentException("Invalid output format");
        }
        System.out.println("Successful!");
    }

    /**
     * Метод add выполняет операцию сложения калькулятора и вывозвращает ее результат.
     *
     * @param numbers список чисел для выполнения операции
     * @return sum результат операции
     */
    private static double add(List<Double> numbers) {
        double sum = 0;
        for (double num : numbers) {
            sum += num;
        }
        return sum;
    }

    /**
     * Метод multiply выполняет операцию умножения калькулятора и вывозвращает ее результат.
     *
     * @param numbers список чисел для выполнения операции
     * @return product результат операции
     */
    private static double multiply(List<Double> numbers) {
        double product = 1;
        for (double num : numbers) {
            product *= num;
        }
        return product;
    }

    /**
     * Метод pow выполняет операцию возведения числа в степень и вывозвращает ее результат.
     *
     * @param numbers список чисел для выполнения операции
     * @return результат операции
     */
    private static double pow(List<Double> numbers) {
        if (numbers.size() != 2) {
            throw new IllegalArgumentException("Elevation to a degree requires exactly 2 numbers");
        }
        return Math.pow(numbers.get(0), numbers.get(1));
    }

    /**
     * Метод specialMultiply выполняет операцию умножения первых двух чисел, прибавляет к нему третье
     * и возвращает реузльтат операции.
     *
     * @param numbers список чисел для выполнения операции
     * @return результат операции
     */
    private static double specialMultiply(List<Double> numbers) {
        if (numbers.size() != 3) {
            throw new IllegalArgumentException("Special multiplication requires exactly 3 numbers");
        }
        return (numbers.get(0) * numbers.get(1)) + numbers.get(2);
    }
}
