package ru.maga.calculator;

public final class CalculatorEngineCheck {
    private static void check(boolean condition) {
        if (!condition) throw new AssertionError();
    }
    public static void main(String[] args) {
        CalculatorEngine engine = new CalculatorEngine();
        engine.input("3"); engine.input("+"); engine.input("5"); engine.input("*");
        check(engine.getDisplay().equals("8"));
        engine.input("2"); engine.input("-");
        check(engine.getDisplay().equals("16"));
        engine.input("4"); engine.input("=");
        check(engine.getDisplay().equals("12"));
        engine.input("C"); engine.input("2"); engine.input(","); engine.input(","); engine.input("5"); engine.input("6");
        check(engine.getDisplay().equals("2,56"));
        engine.input("C"); engine.input("8"); engine.input("/"); engine.input("0"); engine.input("=");
        check(engine.getDisplay().equals("Деление на ноль"));
        engine.input("C"); engine.input("9"); engine.input("sqrt");
        check(engine.getDisplay().equals("3"));
        engine.input("C"); engine.input("x");
        check(engine.getDisplay().equals("Ошибка ввода"));
    }
}
