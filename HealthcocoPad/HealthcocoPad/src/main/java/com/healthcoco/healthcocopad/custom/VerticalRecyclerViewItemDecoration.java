package com.healthcoco.healthcocopad.custom;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class VerticalRecyclerViewItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public VerticalRecyclerViewItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = 0;
        outRect.right = space;
        outRect.top = 0;
        outRect.bottom = space;

    }
}
