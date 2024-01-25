package com.simple.calc.spring.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@EnableAutoConfiguration
public class UICalcController {

    /**
     * Обрабатывает GET-запрос на / и /index, предоставляя пользовательский интерфейс для калькулятора.
     *
     * @return Имя представления (view name), соответствующее пользовательскому интерфейсу
     */
    @GetMapping(value = {"/", "/index"})
    public String UIModPage() {
        return "ui_calc";
    }

    /**
     * Обрабатывает GET-запрос на /console_calc, предоставляя консольный интерфейс для калькулятора.
     *
     * @return Имя представления (view name), соответствующее консольному интерфейсу
     */
    @GetMapping("/console_calc")
    public String consoleModPage() {
        return "console_calc";
    }
}
