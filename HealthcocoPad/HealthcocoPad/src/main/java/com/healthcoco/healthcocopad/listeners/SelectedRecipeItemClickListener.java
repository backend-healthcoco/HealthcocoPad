package com.healthcoco.healthcocopad.listeners;

import com.healthcoco.healthcocopad.bean.server.User;

/**
 * Created by Prashant on 05-10-2018.
 */

public interface SelectedRecipeItemClickListener {

    public void onDeleteItemClicked(Object object);

    public void onQuantityChanged(Object object);

    public void onRecipeItemClicked(Object object);

    public void onIngredientItemClicked(Object object);

    public void onAnalyseItemClicked(Object object);

}
