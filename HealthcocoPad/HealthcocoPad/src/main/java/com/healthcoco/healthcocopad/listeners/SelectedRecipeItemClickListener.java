package com.healthcoco.healthcocopad.listeners;

import com.healthcoco.healthcocopad.bean.server.User;

/**
 * Created by Prashant on 05-10-2018.
 */

public interface SelectedRecipeItemClickListener {

    public void onDeleteIteClicked(Object object);

    public void onQuantityChanged(Object object);

    public void onRecipeItemClicked(Object object);

}
