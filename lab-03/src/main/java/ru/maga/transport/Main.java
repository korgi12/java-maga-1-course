package ru.maga.transport;

public final class Main {
    public static void main(String[] args) throws Exception {
        String task = args.length == 0 ? "1" : args[0];
        switch (task) {
            case "1" -> Task1Main.main(args);
            case "2" -> Task2Main.main(args);
            case "3" -> Task3Main.main(args);
            case "4" -> Task4Main.main(args);
            case "5" -> Task5Main.main(args);
            default -> throw new IllegalArgumentException("Task must be between 1 and 5");
        }
    }
}
