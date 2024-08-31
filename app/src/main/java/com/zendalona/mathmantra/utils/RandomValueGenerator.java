package com.zendalona.mathmantra.utils;

import com.zendalona.mathmantra.enums.Difficulty;

import java.util.Random;

public class RandomValueGenerator {
    private Random random;
    private final int NO_OF_TOPICS = 4;

    public RandomValueGenerator() {
        this.random = new Random();
    }

    public boolean generateNumberLineQuestion(){
        return random.nextBoolean();
    }

    public int generateQuestionTopic(){
        return random.nextInt(NO_OF_TOPICS);
    }

    public int generateNumberForCountGame() {
        return random.nextInt(16) + 2;
    }

    public int[] generateNumberRangeForCount(int upperBound){
        int start = random.nextInt(upperBound - 10);
        int end = random.nextInt(11) + start + 6;
        return new int[]{start, end};
    }

    public int[] generateAdditionValues(Difficulty difficulty) {
        int[] values = new int[3];
        switch (difficulty) {
            case EASY:
                values[0] = random.nextInt(10) + 1;
                values[1] = random.nextInt(10) + 1;
                break;
            case MEDIUM:
                values[0] = random.nextInt(50) + 10;
                values[1] = random.nextInt(50) + 10;
                break;
            case HARD:
                values[0] = random.nextInt(500) + 17;
                values[1] = random.nextInt(500) + 17;
                break;
        }
        values[2] = values[0] + values[1];
        return values;
    }

    public int[] generateSubtractionValues(Difficulty difficulty) {
        int[] values = new int[3];
        switch (difficulty) {
            case EASY:
                values[0] = random.nextInt(10) + 1;
                values[1] = random.nextInt(10) + 1;
                if (values[1] > values[0]) {
                    int temp = values[0];
                    values[0] = values[1];
                    values[1] = temp;
                }
                break;
            case MEDIUM:
                values[0] = random.nextInt(50) + 10;
                values[1] = random.nextInt(50) + 10;
                if (values[1] > values[0]) {
                    int temp = values[0];
                    values[0] = values[1];
                    values[1] = temp;
                }
                break;
            case HARD:
                values[0] = random.nextInt(500) + 17;
                values[1] = random.nextInt(500) + 17;
                if (values[1] > values[0]) {
                    int temp = values[0];
                    values[0] = values[1];
                    values[1] = temp;
                }
                break;
        }
        values[2] = values[0] - values[1];
        return values;
    }

    public int[] generateMultiplicationValues(Difficulty difficulty) {
        int[] values = new int[3];
        switch (difficulty) {
            case EASY:
                values[0] = random.nextInt(10) + 1;
                values[1] = random.nextInt(10) + 1;
                break;
            case MEDIUM:
                values[0] = random.nextInt(20) + 1;
                values[1] = random.nextInt(20) + 1;
                break;
            case HARD:
                values[0] = random.nextInt(50) + 1;
                values[1] = random.nextInt(50) + 1;
                break;
        }
        values[2] = values[0] * values[1];
        return values;
    }

    public int[] generateDivisionValues(Difficulty difficulty) {
        int[] values = new int[3];
        switch (difficulty) {
            case EASY:
                values[1] = random.nextInt(9) + 1; // Avoid division by zero
                values[0] = values[1] * (random.nextInt(10) + 1);
                break;
            case MEDIUM:
                values[1] = random.nextInt(19) + 1; // Avoid division by zero
                values[0] = values[1] * (random.nextInt(20) + 1);
                break;
            case HARD:
                values[1] = random.nextInt(49) + 1; // Avoid division by zero
                values[0] = values[1] * (random.nextInt(50) + 1); //TODO : Scope for decimal values?
                break;
        }
        values[2] = values[0] / values[1];
        return values;
    }

}