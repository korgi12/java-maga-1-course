package ru.maga.calculator;

import java.math.BigDecimal;

public final class CalculatorEngine {
    private String display = "0";
    private Double left;
    private String operation;
    private boolean typing;
    private boolean error;

    public String getDisplay() { return display; }

    public void reset() {
        display = "0";
        left = null;
        operation = null;
        typing = false;
        error = false;
    }

    public void digit(char value) {
        if (value < '0' || value > '9') {
            fail("Ошибка ввода");
            return;
        }
        if (error) reset();
        if (!typing) {
            display = String.valueOf(value);
            typing = true;
        } else if (display.equals("0")) {
            display = String.valueOf(value);
        } else {
            display += value;
        }
    }

    public void decimal() {
        if (error) reset();
        if (!typing) {
            display = "0,";
            typing = true;
        } else if (!display.contains(",")) {
            display += ",";
        }
    }

    private double value() { return Double.parseDouble(display.replace(',', '.')); }

    private String format(double number) {
        if (!Double.isFinite(number)) throw new ArithmeticException("Invalid result");
        return BigDecimal.valueOf(number).stripTrailingZeros().toPlainString().replace('.', ',');
    }

    private void fail(String message) {
        display = message;
        operation = null;
        left = null;
        typing = false;
        error = true;
    }

    private double apply(double a, double b, String op) {
        return switch (op) {
            case "+" -> a + b;
            case "-" -> a - b;
            case "*" -> a * b;
            case "/" -> {
                if (b == 0) throw new ArithmeticException("Деление на ноль");
                yield a / b;
            }
            case "^" -> Math.pow(a, b);
            default -> throw new IllegalArgumentException(op);
        };
    }

    public void operator(String op) {
        if (error) return;
        try {
            if (operation != null && typing) {
                left = apply(left, value(), operation);
                display = format(left);
            } else if (left == null) {
                left = value();
            }
            operation = op;
            typing = false;
        } catch (ArithmeticException ex) {
            fail(ex.getMessage());
        }
    }

    public void equals() {
        if (error || operation == null || !typing) return;
        try {
            display = format(apply(left, value(), operation));
            left = null;
            operation = null;
            typing = false;
        } catch (ArithmeticException ex) {
            fail(ex.getMessage());
        }
    }

    public void squareRoot() {
        if (error) return;
        try {
            double number = value();
            if (number < 0) throw new ArithmeticException("Корень из отрицательного числа");
            display = format(Math.sqrt(number));
            typing = false;
        } catch (ArithmeticException ex) {
            fail(ex.getMessage());
        }
    }

    public void sign() {
        if (!error && !display.equals("0")) {
            display = display.startsWith("-") ? display.substring(1) : "-" + display;
        }
    }

    public void input(String key) {
        if (key.length() == 1 && key.charAt(0) >= '0' && key.charAt(0) <= '9') digit(key.charAt(0));
        else switch (key) {
            case ".", "," -> decimal();
            case "+", "-", "*", "/", "^" -> operator(key);
            case "=", "Enter" -> equals();
            case "sqrt", "r" -> squareRoot();
            case "C", "Escape" -> reset();
            case "+/-" -> sign();
            default -> fail("Ошибка ввода");
        }
    }
}
