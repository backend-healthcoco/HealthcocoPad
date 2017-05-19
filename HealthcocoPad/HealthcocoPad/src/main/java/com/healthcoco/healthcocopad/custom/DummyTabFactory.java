package com.healthcoco.healthcocopad.custom;

import android.content.Context;
import android.view.View;
import android.widget.TabHost.TabContentFactory;

public class DummyTabFactory implements TabContentFactory {
    private Context mContext;

    public DummyTabFactory(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public View createTabContent(String tag) {
        return new View(mContext);
    }

}
