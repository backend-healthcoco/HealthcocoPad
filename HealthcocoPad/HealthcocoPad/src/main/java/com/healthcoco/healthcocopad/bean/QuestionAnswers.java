package com.healthcoco.healthcocopad.bean;

import java.util.ArrayList;

/**
 * Created by neha on 31/01/18.
 */

public class QuestionAnswers {
    private String question;
    private String questionType;
    private ArrayList<String> answers;
    private boolean isNone;
    private boolean isAnswerNone;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }

    public boolean isNone() {
        return isNone;
    }

    public void setNone(boolean none) {
        isNone = none;
    }

    public boolean isAnswerNone() {
        return isAnswerNone;
    }

    public void setAnswerNone(boolean answerNone) {
        isAnswerNone = answerNone;
    }
}
