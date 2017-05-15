package com.healthcoco.healthcocopad.bean;

/**
 * Created by neha on 04/02/17.
 */
public class DrugInteractions {
   private String text;
    private String reaction;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getReaction() {
        return reaction;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }
}
