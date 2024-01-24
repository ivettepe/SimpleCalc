package com.simple.calc;

import com.simple.calc.entity.Pair;
import com.simple.calc.enums.Operator;
import com.simple.calc.utils.CalculatorUtil;
import com.simple.calc.utils.ParserUtil;

import java.util.List;

/**
 * Класс Main содержит метод main для запуска приложения калькулятора.
 */
public class Main {

    /**
     * Метод main является точкой входа в приложение.
     *
     * @param args массив строк с входными аргументами
     */
    public static void main(String[] args) {
        // Парсинг входных аргументов и строки для вычисления
        Pair<String, String> pairArgs = ParserUtil.parseArguments(args);
        Pair<Operator, List<Double>> calcString = ParserUtil.parseCalculatedString(pairArgs);
        // Вычисление и отображение результата
        CalculatorUtil.calculateAndShow(calcString, pairArgs);
    }
}