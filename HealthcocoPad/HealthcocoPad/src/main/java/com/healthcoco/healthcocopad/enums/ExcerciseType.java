package com.healthcoco.healthcocopad.enums;

public enum ExcerciseType {

    GYM("GYM"), YOGA("YOGA"), WALKING("WALKING"), RUNNING("RUNNING"), BRISK_WALK("BRISK_WALK"), CYCLING("CYCLING"),
    WEIGHT_LIFTING("WEIGHT_LIFTING"), SWIMMING("SWIMMING"), OTHER("OTHER");

    private String excerciseType;

    ExcerciseType(String excerciseType) {
        this.excerciseType = excerciseType;
    }

    public String getExcerciseType() {
        return excerciseType;
    }

}
