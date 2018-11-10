package com.healthcoco.healthcocopad.enums;

public enum ExerciseType {

    GYM("GYM", "Gym"), YOGA("YOGA", "Yoga"),
    WALKING("WALKING", "Walking"), RUNNING("RUNNING", "Running"),
    BRISK_WALK("BRISK_WALK", "Brisk Walk"), CYCLING("CYCLING", "Cycling"),
    WEIGHT_LIFTING("WEIGHT_LIFTING", "Weight Lifting"),
    SWIMMING("SWIMMING", "Swimming"), OTHER("OTHER", "Other");

    private String excerciseType;
    private String type;

    ExerciseType(String type, String excerciseType) {
        this.excerciseType = excerciseType;
        this.type = type;
    }

    public String getExcerciseType() {
        return excerciseType;
    }

    public String getType() {
        return type;
    }
}
