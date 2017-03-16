package com.healthcoco.healthcocopad.bean.server;

import com.orm.SugarRecord;

/**
 * Created by Shreshtha on 16-03-2017.
 */

public class PersonalHistory extends SugarRecord {
    private String diet;
    private String addictions;
    private String bowelHabit;
    private String bladderHabit;

    public String getDiet() {
        return diet;
    }

    public void setDiet(String diet) {
        this.diet = diet;
    }

    public String getAddictions() {
        return addictions;
    }

    public void setAddictions(String addictions) {
        this.addictions = addictions;
    }

    public String getBowelHabit() {
        return bowelHabit;
    }

    public void setBowelHabit(String bowelHabit) {
        this.bowelHabit = bowelHabit;
    }

    public String getBladderHabit() {
        return bladderHabit;
    }

    public void setBladderHabit(String bladderHabit) {
        this.bladderHabit = bladderHabit;
    }
}
