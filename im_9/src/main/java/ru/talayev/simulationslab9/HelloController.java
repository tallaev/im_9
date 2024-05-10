package ru.vorotov.simulationslab9;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.Arrays;
import java.util.Random;

public class HelloController {
    @FXML
    private TextField prob1Field;
    @FXML
    private TextField prob2Field;
    @FXML
    private TextField prob3Field;
    @FXML
    private TextField prob4Field;
    @FXML
    private TextField trialsField;

    @FXML
    private BarChart<String, Double> barChart;

    @FXML
    private Label averageLabel;
    @FXML
    private Label varLabel;
    @FXML
    private Label chiLabel;
    double[] empProbs = {0, 0, 0, 0};
    double[] counter = {0, 0, 0, 0};
    int N;

    public void onStartButtonClick(ActionEvent actionEvent) {
        N = Integer.parseInt(trialsField.getText());
        // инициализация графика
        XYChart.Series<String, Double> series = new XYChart.Series<>();
        barChart.getData().add(series);

        final double[] probabilities = {
                Double.parseDouble(prob1Field.getText()),
                Double.parseDouble(prob2Field.getText()),
                Double.parseDouble(prob3Field.getText()),
                1 - Double.parseDouble(prob1Field.getText()) - Double.parseDouble(prob2Field.getText()) - Double.parseDouble(prob3Field.getText())
        };
        int trials = Integer.parseInt(trialsField.getText());


        Random random = new Random();

        for (int i = 0; i < trials; i++) {
            empProbs[Integer.parseInt(generateEvent(random, probabilities)) - 1]++;
        }
        System.arraycopy(empProbs, 0, counter, 0, counter.length);
        for (int i = 0; i < empProbs.length; i++) {
            empProbs[i] /= trials;
        }

        series.getData().add(new XYChart.Data<>("1", empProbs[0]));
        series.getData().add(new XYChart.Data<>("2", empProbs[1]));
        series.getData().add(new XYChart.Data<>("3", empProbs[2]));
        series.getData().add(new XYChart.Data<>("4", empProbs[3]));

        System.out.println(Arrays.toString(empProbs));

        averageLabel.setText("Среднее: " + empAverage() + " Ошибка: " + (Math.abs(empAverage() - countAverage(probabilities))) / (countAverage(probabilities)));
        varLabel.setText("Дисперсия: " + empVar() + " Ошибка: " + (Math.abs(empVar() - countVar(probabilities))) / (countVar(probabilities)));
        chiLabel.setText("Хи квадрат: " + chi_square(probabilities));

        if (chi_square(probabilities) < 7.815) {
            chiLabel.setText(chiLabel.getText() + " Распределения совпадают");
        } else {
            chiLabel.setText(chiLabel.getText() + " Распределения не совпадают");
        }
    }

    private String generateEvent(Random r, double[] probabilities) {
        var a = r.nextDouble();

        if (a < probabilities[0]) {
            return "1";
        } else if (probabilities[0] <= a && a < probabilities[0] + probabilities[1]) {
            return "2";
        } else if (probabilities[0] + probabilities[1] <= a && a < probabilities[0] + probabilities[1] + probabilities[2]) {
            return "3";
        } else {
            return "4";
        }
    }

    private double countAverage(double[] probs) {
        double res = 0;

        for (int i = 0; i < probs.length; i++) {
            res += (i + 1) * probs[i];
        }

        return res;
    }

    private double empAverage() {
        double res = 0;

        for (int i = 0; i < empProbs.length; i++) {
            res += (i + 1) * empProbs[i];
        }

        return res;
    }

    private double countVar(double[] probs) {
        double res = 0;

        for (int i = 0; i < probs.length; i++) {
            res += probs[i] * Math.pow(i + 1, 2);
        }
        res -= Math.pow(countAverage(probs), 2);

        return res;
    }

    private double empVar() {
        double res = 0;

        for (int i = 0; i < empProbs.length; i++) {
            res += empProbs[i] * Math.pow(i + 1, 2);
        }
        res -= Math.pow(empAverage(), 2);

        return res;
    }

    private double chi_square(double[] probs) {
        double res = 0;

        for (int i = 0; i < probs.length; i++) {
            res += (counter[i] * counter[i]) / (N * probs[i]);
        }
        res -= N;

        return res;
    }
}