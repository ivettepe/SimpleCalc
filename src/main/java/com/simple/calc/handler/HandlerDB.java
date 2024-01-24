package com.simple.calc.handler;

import com.simple.calc.entity.Pair;
import com.simple.calc.enums.Operator;

import java.sql.*;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Класс HandlerDB предназначен для управления вводом и выводом данных с использованием базы данных.
 */
public class HandlerDB {
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Метод closeScanner закрывает экземпляр сканера.
     */
    public static void closeScanner() {
        scanner.close();
    }

    /**
     * Метод handleDBInput обрабатывает ввод данных из базы данных.
     *
     * @param operator ссылка на объект класса AtomicReference<Operator>, в который запишется оператор операции
     * @param numbers  список, в который запишутся числа для выполнения операции
     */
    public static void handleDBInput(AtomicReference<Operator> operator, List<Double> numbers) {
        System.out.println("Enter the database connection details:");
        String url = askForInput("Database URL: ");
        String username = askForInput("Username: ");
        String password = askForInput("Password: ");

        try (Connection conn = DriverManager.getConnection(url, username, password); Statement stmt = conn.createStatement()) {

            String chooseTable = askForTableSelection();

            String tableName = chooseTable.equals("1") ? askForInput("Enter the name of the table: ") : createAndPopulateTable(stmt);
            retrieveAndPrintData(stmt, tableName);

            retrieveDataByID(stmt, operator, numbers, tableName, askForInput("Enter ID: "));

        } catch (SQLException e) {
            throw new IllegalArgumentException("Error connecting to the database: " + e.getMessage());
        }
    }

    /**
     * Метод handleDBOutput обрабатывает вывод данных в базу данных.
     *
     * @param calculatedString пара, представляющая оператор операции и список чисел для выполнения операции
     * @param result           результат выполненной операции
     */
    public static void handleDBOutput(Pair<Operator, List<Double>> calculatedString, double result) {
        System.out.println("Enter the database connection details:");
        String url = askForInput("Database URL: ");
        String username = askForInput("Username: ");
        String password = askForInput("Password: ");

        try (Connection conn = DriverManager.getConnection(url, username, password); Statement stmt = conn.createStatement()) {

            String chooseTable = askForTableSelection();

            String tableName = chooseTable.equals("1") ? askForInput("Enter the name of the table: ") : createAndPopulateTable(stmt);

            processOutputData(stmt, calculatedString, result, tableName);

        } catch (SQLException e) {
            throw new IllegalArgumentException("Error connecting to the database: " + e.getMessage());
        }
    }

    /**
     * Метод askForInput запрашивает ввод данных от пользователя.
     *
     * @param prompt приглашение для ввода данных
     * @return введенную пользователем строку
     */
    private static String askForInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    /**
     * Метод askForTableSelection запрашивает у пользователя выбор существующей таблицы или создание новой.
     *
     * @return выбор пользователя в виде строки: "1" (существующая таблица) или "2" (создание новой таблицы)
     */
    private static String askForTableSelection() {
        System.out.println("Enter (1 or 2):");
        System.out.println("1 - Select an existing table");
        System.out.println("2 - Create new table");
        String chooseTable = askForInput("");
        while (!chooseTable.equals("1") && !chooseTable.equals("2")) {
            System.out.println("Wrong value. Try again");
            chooseTable = askForInput("");
        }
        return chooseTable;
    }

    /**
     * Метод processOutputData обрабатывает вывод данных в базу данных.
     *
     * @param stmt             объект Statement для выполнения SQL-запросов к базе данных
     * @param calculatedString пара, представляющая оператор операции и список чисел для выполнения операции
     * @param result           результат выполненной операции
     * @param tableName        имя таблицы в базе данных
     * @throws SQLException если возникает ошибка взаимодействия с базой данных
     */
    private static void processOutputData(Statement stmt, Pair<Operator, List<Double>> calculatedString, double result, String tableName) throws SQLException {
        String operator = calculatedString.first().name().toLowerCase();
        String numbers = buildNumberString(calculatedString.second());

        if (dataExists(stmt, tableName, operator, numbers)) {
            updateData(stmt, tableName, operator, numbers, result);
        } else {
            insertData(stmt, tableName, operator, numbers, result);
        }
    }

    /**
     * Метод createAndPopulateTable создает и заполняет таблицу в базе данных
     *
     * @param stmt объект Statement для выполнения SQL-запросов к базе данных
     * @return имя созданной таблицы в базе данных
     * @throws SQLException если возникает ошибка взаимодействия с базой данных
     */
    private static String createAndPopulateTable(Statement stmt) throws SQLException {
        String tableName = "test_table";
        String createTableQuery = "CREATE TABLE IF NOT EXISTS test_table (" + "id INTEGER PRIMARY KEY NOT NULL," + "operator VARCHAR(40) NOT NULL," + "numbers VARCHAR(40) NOT NULL," + "result VARCHAR(40)" + ")";

        String insertDataQuery1 = "INSERT INTO test_table " + "VALUES (COALESCE((SELECT max(id) + 1 FROM test_table), 1), 'add', '1 2 3 4')";
        String insertDataQuery2 = "INSERT INTO test_table " + "VALUES (COALESCE((SELECT max(id) + 1 FROM test_table), 1), 'mul', '1 2 3 4')";
        String insertDataQuery3 = "INSERT INTO test_table " + "VALUES (COALESCE((SELECT max(id) + 1 FROM test_table), 1), 'spec_mul', '2 3 4')";
        String insertDataQuery4 = "INSERT INTO test_table " + "VALUES (COALESCE((SELECT max(id) + 1 FROM test_table), 1), 'pow', '2 3')";

        stmt.executeUpdate(createTableQuery);
        stmt.executeUpdate(insertDataQuery1);
        stmt.executeUpdate(insertDataQuery2);
        stmt.executeUpdate(insertDataQuery3);
        stmt.executeUpdate(insertDataQuery4);

        return tableName;
    }

    /**
     * Метод retrieveAndPrintData извлекает и выводит данные из указанной таблицы.
     *
     * @param stmt      объект Statement для выполнения SQL-запросов к базе данных
     * @param tableName имя таблицы в базе данных
     * @throws SQLException если возникает ошибка взаимодействия с базой данных
     */
    private static void retrieveAndPrintData(Statement stmt, String tableName) throws SQLException {
        ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);
        while (rs.next()) {
            showRowFromTable(rs);
        }
        rs.close();
    }

    /**
     * Метод showRowFromTable выводит данные из текущей строки ResultSet таблицы.
     *
     * @param rs объект ResultSet, представляющий результат SQL-запроса к базе данных
     * @throws SQLException если возникает ошибка при работе с базой данных
     */
    private static void showRowFromTable(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String operatorStr = rs.getString("operator");
        String numbersStr = rs.getString("numbers");
        String result = rs.getString("result");

        System.out.println("ID: " + id + ", Operator: " + operatorStr + ", Numbers: " + numbersStr + ", Result: " + result);

    }

    /**
     * Метод retrieveDataByID извлекает данные из таблицы по указанному идентификатору и обновляет параметры оператора и чисел.
     *
     * @param stmt      объект Statement для выполнения SQL-запросов к базе данных
     * @param operator  ссылка на объект AtomicReference<Operator>, представляющий оператор операции
     * @param numbers   список чисел для выполнения операции
     * @param tableName имя таблицы в базе данных
     * @param chooseID  выбранный идентификатор
     * @throws SQLException если возникает ошибка взаимодействия с базой данных
     */
    private static void retrieveDataByID(Statement stmt, AtomicReference<Operator> operator, List<Double> numbers, String tableName, String chooseID) throws SQLException {
        ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName + " WHERE id = " + chooseID);
        while (rs.next()) {
            int id = rs.getInt("id");
            String operatorStr = rs.getString("operator");
            String numbersStr = rs.getString("numbers");

            operator.set(switch (operatorStr) {
                case "add", "+", "sum" -> Operator.ADD;
                case "mul", "*", "multiply" -> Operator.MULTIPLY;
                case "spec_mul", "special_multiply" -> Operator.SPECIAL_MULTIPLY;
                case "pow", "^" -> Operator.POW;
                default -> Operator.DEFAULT;
            });
            String[] numbersArray = numbersStr.split(" ");
            for (String currNum : numbersArray) {
                numbers.add(Double.parseDouble(currNum));
            }
            numbers.add((double) id);
        }
        rs.close();
    }

    /**
     * Метод buildNumberString формирует строку из списка чисел.
     *
     * @param numbers список чисел
     * @return строка, содержащая числа из списка, разделенные пробелами
     */
    private static String buildNumberString(List<Double> numbers) {
        return numbers.stream().map(String::valueOf).collect(Collectors.joining(" "));
    }

    /**
     * Метод dataExists проверяет, существуют ли данные с указанными параметрами в таблице базы данных.
     *
     * @param stmt      объект Statement для выполнения SQL-запросов к базе данных
     * @param tableName имя таблицы в базе данных
     * @param operator  оператор операции
     * @param numbers   строка чисел
     * @return true, если данные с указанными параметрами существуют, иначе false
     * @throws SQLException если возникает ошибка взаимодействия с базой данных
     */
    private static boolean dataExists(Statement stmt, String tableName, String operator, String numbers) throws SQLException {
        String query = "SELECT * FROM " + tableName + " WHERE operator = '" + operator + "' AND numbers = '" + numbers + "'";
        ResultSet rs = stmt.executeQuery(query);
        boolean exists = rs.next();
        rs.close();
        return exists;
    }

    /**
     * Метод updateData обновляет результат операции в базе данных, если данные о соответствующей операции уже существуют.
     *
     * @param stmt      объект Statement для выполнения SQL-запросов к базе данных
     * @param tableName имя таблицы в базе данных
     * @param operator  оператор операции
     * @param numbers   строка чисел
     * @param result    результат операции
     * @throws SQLException если возникает ошибка взаимодействия с базой данных
     */
    private static void updateData(Statement stmt, String tableName, String operator, String numbers, double result) throws SQLException {
        String query = "SELECT * FROM " + tableName + " WHERE operator LIKE '" + operator + "' AND numbers LIKE '" + numbers + "'";
        ResultSet rs = stmt.executeQuery(query);
        if (rs.next()) {
            updateResultColumn(stmt, tableName, rs, String.valueOf(result));
        }
        rs.close();
    }

    /**
     * Метод updateResultColumn обновляет значение столбца с результатом в базе данных для указанной операции.
     *
     * @param stmt      объект Statement для выполнения SQL-запросов к базе данных
     * @param tableName имя таблицы в базе данных
     * @param rs        объект ResultSet, содержащий данные для обновления
     * @param newValue  новое значение для столбца с результатом
     * @throws SQLException если возникает ошибка взаимодействия с базой данных
     */
    private static void updateResultColumn(Statement stmt, String tableName, ResultSet rs, String newValue) throws SQLException {
        int id = rs.getInt("id");
        String updateQuery = "UPDATE " + tableName + " SET result = '" + newValue + "' WHERE id = " + id;
        stmt.executeUpdate(updateQuery);
    }

    /**
     * Метод insertData вставляет новые данные об операции в базу данных.
     *
     * @param stmt      объект Statement для выполнения SQL-запросов к базе данных
     * @param tableName имя таблицы в базе данных
     * @param operator  оператор операции
     * @param numbers   строка чисел
     * @param result    результат операции
     * @throws SQLException если возникает ошибка взаимодействия с базой данных
     */
    private static void insertData(Statement stmt, String tableName, String operator, String numbers, double result) throws SQLException {
        String insertDataQuery = "INSERT INTO " + tableName + " VALUES (COALESCE((SELECT max(id) + 1 FROM " + tableName + "), 1), '" + operator + "', '" + numbers + "', '" + result + "')";
        stmt.executeUpdate(insertDataQuery);
    }
}