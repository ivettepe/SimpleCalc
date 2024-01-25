package com.simple.calc.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class ServerRunner {

    /**
     * Запускает локальный сервер Spring Boot для развертывания веб-приложения.
     *
     * @param args Аргументы командной строки
     * @throws IOException Если возникла ошибка ввода-вывода при запуске сервера
     */
    public static void runLocalServer(String[] args) throws IOException {
        SpringApplication.run(ServerRunner.class, args);
    }
}
