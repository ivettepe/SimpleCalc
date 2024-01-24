# Реалезация простого консольного калькулятора SimpleCalc
## Описание
- Проект написан с использованием Java 17.
### Сборка
- Для сборки приложения необходимо воспользоваться командой `mvn install`, 
после чего в корне проекта создастся директория `app` и файл `test-app.jar`

### Запуск
- Для запуска программы необходимо в терминале перейти в директорию `app`
и ввести команду `java -jar test-app.jar {first} {second}`. После test-app.jar следует 
два аргумента командной строки. `{first}` отвечает за режим ввода, 
`{second}` за режим вывода.
#### Доступные режимы ввода/вывода
- `-` Означает работу с консолью (ввод/вывод будет осуществлен 
через стандартные потоки ввода/вывода напрямую через/в консоль)
- `/path/to/some/file.txt` Указывается относительный/абсолютный 
путь до файла из которого будут считываться или в который будут 
выводиться данные. Если файл для вывода не будет найден, в таком 
случае такой файл будет создан. 
- `db` Означает то, что считывание информации или запись результата 
будет произведена из/в таблицу БД, данные для подключения к которой
вам будет предложено ввести через терминал вручную или ввести путь 
до файла, в котором уже записаны все эти данные. Пример такого файла
находится в `/app/connection_info`. 
  - **!!!ВАЖНО!!!**
    1. **В первой строке вводится Database URL**
    2. **Во второй строке вводится USERNAME**
    3. **В третьей строке вводится PASSWORD**
  - **Вводятся только данные без описаний лишних символов и тд**
- Данные аргументы можно комбинировать между собой
#### Примеры команд для запуска
- `java -jar test-app.jar - -`
- `java -jar test-app.jar ./input.txt ./output.txt`
- `java -jar test-app.jar db db`
- `java -jar test-app.jar db -`
- `java -jar test-app.jar - /User/mac/Desktop/asdas`
- `java -jar test-app.jar text.txt db`
- И другие возможные комбинации

##### Консоль
После запуска при использовании ввода через консоль (`-`) программа потребует
ввести строку в формате `{operator} {numbers}`. Все числа и оператор 
должны быть разделены пробелом. Пример:
````
java -jar test-app.jar - -
Enter the expression in the format {operator} {numbers}:
add 2 3
Result: 5.0
````
##### База данных
После запуска при использовании ввода/вывода с ипользованием
базы данных программа потребует ввести данные для подключения 
к запущенной БД и выбрать текущую таблицу, подходящую под условия
или создать новую. При создании новой, будет создана таблица с 
именем `test_table`, в которую сразу будет добавлено 4 тестовые 
записи. Примеры:
- ````
    java -jar test-app.jar text.txt db
    Enter the database connection details:
    Database URL: jdbc:postgresql://localhost:5432/postgres
    Username: postgres
    Password: root
    Enter (1 or 2):
    1 - Select an existing table
    2 - Create new table
    1
    Enter the name of the table: test_table
    ````
- ````
    java -jar test-app.jar db db
    Enter the database connection details:
    Database URL: jdbc:postgresql://localhost:5432/postgres
    Username: postgres
    Password: root
    Enter (1 or 2):
    1 - Select an existing table
    2 - Create new table
    1
    Enter the name of the table: test_table
    ID: 1, Operator: add, Numbers: 1 2 3 4, Result: null
    ID: 2, Operator: mul, Numbers: 1 2 3 4, Result: null
    ID: 3, Operator: spec_mul, Numbers: 2 3 4, Result: null
    ID: 4, Operator: pow, Numbers: 2 3, Result: null
    Enter ID: 4
    Enter the database connection details:
    Database URL: jdbc:postgresql://localhost:5432/postgres
    Username: postgres
    Password: root
    Enter (1 or 2):
    1 - Select an existing table
    2 - Create new table
    1
    Enter the name of the table: test_table
    ````
- ````
  java -jar test-app.jar db db
  Choose how to provide database connection details:
  1 - Enter details manually
  2 - Read details from a file
  Enter your choice (1 or 2): 2
  Enter the file name: connection_info
  Enter (1 or 2):
  1 - Select an existing table
  2 - Create new table
  1
  Enter the name of the table: test_table
  ID: 1, Operator: add, Numbers: 1 2 3 4, Result: null
  ID: 2, Operator: mul, Numbers: 1 2 3 4, Result: null
  ID: 3, Operator: spec_mul, Numbers: 2 3 4, Result: null
  ID: 4, Operator: pow, Numbers: 2 3, Result: null
  ID: 5, Operator: pow, Numbers: 2.0 3.0, Result: 8.0
  ID: 6, Operator: multiply, Numbers: 2.0 2.0 10.0 3.0, Result: 120.0
  Enter ID: 1
  Choose how to provide database connection details:
  1 - Enter details manually
  2 - Read details from a file
  Enter your choice (1 or 2): 1
  Database URL: jdbc:postgresql://localhost:5432/postgres
  Username: postgres
  Password: root
  Enter (1 or 2):
  1 - Select an existing table
  2 - Create new table
  1
  Enter the name of the table: test_table
    ````
### Реализованные сущности
- `record Pair<T, U>` - Класс Pair представляет собой обобщенную пару 
значений с типами T и U.
- `enum Operator` - Перечисление Operator определяет различные
операторы, которые могут использоваться в калькуляторе.
- `class FileHandler` - Класс FileHandler предназначен для 
работы с файлами в контексте операций калькулятора.
- `class HandlerDB` - Класс HandlerDB предназначен для управления
вводом и выводом данных с использованием базы данных.
- `class CalculatorUtil` - Класс CalculatorUtil предоставляет 
утилиты для выполнения операций калькулятора и обработки результатов.
- `class ParserUtil` - Класс ParserUtil предоставляет утилиты для 
разбора входных данных калькулятора.
- `class Main` - Класс Main содержит метод main для запуска 
приложения калькулятора.

### Основные методы
- `Pair<String, String> parseArguments(String[] args)` - Данный метод
разбирает входные аргументы и возвращает их в виде пары значений.
- `Pair<Operator, List<Double>> parseCalculatedString(Pair<String, String> pairArgs)` - 
Данный метод разбирает строку с оператором и числами для последующих
вычислений.
- `void calculateAndShow(Pair<Operator, List<Double>> calculatedString, Pair<String, String> pairArgs)` -
Метод calculateAndShow выполняет операцию калькулятора, 
форматирует результат и направляет вывод в указанное место. 
  

### Доступные операции
- Сложение двух и более чисел. Для вычисления можно исользовать 
операторы:
    - `add`
    - `sum`
    - `+`
- Перемножение двух и более чисел. Для вычисления можно исользовать 
операторы:
    - `mul`
    - `multiply`
    - `*`
- Перемножение первых двух чисел и сложение с третьим. Для
  вычисления можно исользовать операторы:
    - `spec_mul`
    - `special_multiply`
- Возведение числа в произвольную степень. Для вычисления 
можно исользовать операторы:
    - `pow`
    - `^`
