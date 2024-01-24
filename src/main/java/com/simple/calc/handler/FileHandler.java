package com.simple.calc.handler;

import com.simple.calc.entity.Pair;
import com.simple.calc.enums.Operator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;

import static com.simple.calc.utils.ParserUtil.parseInput;

/**
 * Класс FileHandler предназначен для работы с файлами в контексте операций калькулятора.
 */
public class FileHandler {

    /**
     * Метод readFileInput считывает входные данные из указанного файла и парсит их для дальнейшего использования.
     *
     * @param filePath путь к файлу, из которого считываются входные данные
     * @param operator объект AtomicReference, в который записывается оператор из файла
     * @param numbers  список, в который записываются числа из файла
     * @throws IOException если возникает ошибка взаимодействия с файлом
     */
    public static void readFileInput(String filePath, AtomicReference<Operator> operator, List<Double> numbers) throws IOException {
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            Scanner fileScanner = new Scanner(file);
            String fileContent = fileScanner.useDelimiter("\\A").next();
            fileScanner.close();
            parseInput(fileContent, operator, numbers);
        } else {
            throw new IllegalArgumentException("File not found or invalid file path");
        }
    }

    /**
     * Метод handleFileOutput записывает результат операции в указанный файл.
     *
     * @param pairArgs пара значений, откуда считывались вводные данные и путь к файлу вывода
     * @param result   результат вычислений калькулятора
     */
    public static void handleFileOutput(Pair<String, String> pairArgs, String result) {
        try {
            if (Files.exists(Paths.get(pairArgs.second()))) {
                Files.write(Paths.get(pairArgs.second()), result.getBytes());
            } else {
                Files.createFile(Paths.get(pairArgs.second()));
                Files.write(Paths.get(pairArgs.second()), result.getBytes());
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Error writing to or creating file: " + e.getMessage());
        }
    }
}
