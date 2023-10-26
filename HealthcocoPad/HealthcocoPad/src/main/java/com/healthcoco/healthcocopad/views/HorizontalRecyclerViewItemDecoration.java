package com.healthcoco.healthcocopad.views;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class HorizontalRecyclerViewItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public HorizontalRecyclerViewItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = 0;
        outRect.right = space;
        outRect.top = 0;
        outRect.bottom = 0;

    }
}

