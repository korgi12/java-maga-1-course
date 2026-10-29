package ru.maga.calculator;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public final class CalculatorApp extends Application {
    private final CalculatorEngine engine = new CalculatorEngine();
    private final TextField screen = new TextField("0");

    @Override public void start(Stage stage) {
        screen.setEditable(false);
        screen.setAlignment(Pos.CENTER_RIGHT);
        screen.setStyle("-fx-font-size: 26px; -fx-background-radius: 8px;");
        screen.setPrefHeight(65);

        GridPane grid = new GridPane();
        grid.setHgap(7);
        grid.setVgap(7);
        String[][] keys = {
            {"C", "sqrt", "^", "/"},
            {"7", "8", "9", "*"},
            {"4", "5", "6", "-"},
            {"1", "2", "3", "+"},
            {"+/-", "0", ",", "="}
        };
        for (int row = 0; row < keys.length; row++) {
            for (int column = 0; column < keys[row].length; column++) {
                String key = keys[row][column];
                Button button = new Button(key);
                button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                button.setPrefSize(78, 55);
                button.setStyle("-fx-font-size: 17px; -fx-background-radius: 7px;");
                button.setOnAction(event -> handle(key));
                grid.add(button, column, row);
                GridPane.setHgrow(button, Priority.ALWAYS);
                GridPane.setVgrow(button, Priority.ALWAYS);
            }
        }

        VBox content = new VBox(12, screen, grid);
        content.setPadding(new Insets(16));
        Scene scene = new Scene(content, 365, 410);
        scene.setOnKeyTyped(event -> {
            String value = event.getCharacter();
            if (value.length() == 1 && !Character.isISOControl(value.charAt(0))) {
                handle(value);
                event.consume();
            }
        });
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) handle("=");
            if (event.getCode() == KeyCode.ESCAPE) handle("C");
        });
        stage.setTitle("Калькулятор");
        stage.setScene(scene);
        stage.show();
    }

    private void handle(String input) {
        engine.input(input);
        screen.setText(engine.getDisplay());
    }

    public static void main(String[] args) { launch(args); }
}
