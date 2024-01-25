package com.simple.calc.spring.controller;

import com.simple.calc.entity.Pair;
import com.simple.calc.enums.Operator;
import com.simple.calc.utils.CalculatorUtil;
import com.simple.calc.utils.ParserUtil;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@RestController
public class CalculateController {

    /**
     * Обрабатывает POST-запрос на /calculateUIResult для UI-интерфейса.
     * Принимает JSON-строку вида {"operator": "+", "nums": "2,3,4"}.
     *
     * @param data JSON-строка с оператором и числами
     * @return HTTP-ответ с результатом вычислений или сообщением об ошибке
     */
    @PostMapping("/calculateUIResult")
    public ResponseEntity<String> calculateUIResult(@RequestBody String data) {
        JSONObject jsonObject = new JSONObject(data);

        String inputString;
        AtomicReference<Operator> operator = new AtomicReference<>();
        List<Double> numbers = new ArrayList<>();

        if (!jsonObject.has("operator") || jsonObject.getString("operator").isEmpty() || !jsonObject.has("nums") || jsonObject.getString("nums").isEmpty()) {
            return ResponseEntity.badRequest().body("ERROR: Invalid input");
        } else {
            inputString = jsonObject.getString("operator") + " " + jsonObject.getString("nums");
        }
        return getStringResponseEntity(inputString, operator, numbers);
    }

    /**
     * Обрабатывает POST-запрос на /calculateConsoleResult для консольного интерфейса.
     * Принимает строку вида "+ 2,3,4".
     *
     * @param inputString Строка с оператором и числами
     * @return HTTP-ответ с результатом вычислений или сообщением об ошибке
     */
    @PostMapping("/calculateConsoleResult")
    public ResponseEntity<String> calculateConsoleResult(@RequestBody String inputString) {
        AtomicReference<Operator> operator = new AtomicReference<>();
        List<Double> numbers = new ArrayList<>();
        return getStringResponseEntity(inputString, operator, numbers);
    }

    /**
     * Общая логика обработки входных данных и возврата результата.
     *
     * @param inputString Строка с оператором и числами
     * @param operator    AtomicReference для оператора
     * @param numbers     Список чисел
     * @return HTTP-ответ с результатом вычислений или сообщением об ошибке
     */
    private ResponseEntity<String> getStringResponseEntity(@RequestBody String inputString, AtomicReference<Operator> operator, List<Double> numbers) {
        try {
            ParserUtil.parseInput(inputString, operator, numbers);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("ERROR: Invalid input");
        }

        try {
            Pair<Operator, List<Double>> parsedInput = new Pair<>(operator.get(), numbers);
            String result = String.valueOf(CalculatorUtil.calculate(parsedInput));
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("ERROR: " + e.getMessage());
        }
    }
}
