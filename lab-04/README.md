# Лабораторная работа №4

Сборка из папки `lab-04`:

```bash
mvn clean package
```

Главные классы для запуска в IntelliJ IDEA:

- `ru.maga.socket.SequentialServerMain` — последовательный сервер, порт 5000.
- `ru.maga.socket.ParallelServerMain` — параллельный сервер, порт 5001.
- `ru.maga.socket.ClientMain` — клиент, по умолчанию порт 5000.

Для параллельного сервера передайте клиенту аргументы `localhost 5001`.

Результат для тестового массива: `14125.0`.
