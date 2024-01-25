package com.simple.calc.utils;

import com.simple.calc.entity.Pair;
import com.simple.calc.enums.Operator;
import com.simple.calc.spring.ServerRunner;

import java.io.IOException;
import java.util.List;

public class StartUtil {

    /**
     * Запускает приложение в зависимости от входных аргументов.
     * Если аргумент "spring" указан, запускается сервер Spring Boot. Иначе происходит вычисление и отображение результата.
     *
     * @param args Входные аргументы командной строки
     */
    public static void start(String[] args) {
        Pair<String, String> pairArgs = ParserUtil.parseArguments(args);
        if (pairArgs.first().equals("spring")) {
            try {
                ServerRunner.runLocalServer(args);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            Pair<Operator, List<Double>> calcString = ParserUtil.parseCalculatedString(pairArgs);
            CalculatorUtil.calculateAndShow(calcString, pairArgs);
        }
    }
}
