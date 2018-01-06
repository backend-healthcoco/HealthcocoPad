package com.healthcoco.healthcocopad.listeners;


import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.enums.SuggestionType;

public interface AddNewSuggestionListener {

    public void onSaveClicked(Object object);

    public SuggestionType getSuggestionType();

}
