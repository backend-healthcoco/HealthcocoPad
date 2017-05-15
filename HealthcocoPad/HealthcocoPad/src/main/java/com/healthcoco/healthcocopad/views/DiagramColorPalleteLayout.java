package com.healthcoco.healthcocopad.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
import android.widget.LinearLayout;


import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.adapter.DiagramColorPalleteGridListAdapter;
import com.healthcoco.healthcocopad.enums.ColorType;
import com.healthcoco.healthcocopad.listeners.DiagramCanvasListener;

import java.util.Arrays;

/**
 * Created by Mohit on 29/02/16.
 */
public class DiagramColorPalleteLayout extends LinearLayout {
    private HealthCocoActivity mActivity;
    private DiagramCanvasListener diagramCanvasListener;
    private GridView gvColors;

    public DiagramColorPalleteLayout(Context context) {
        super(context);
        init(context);
    }

    public DiagramColorPalleteLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DiagramColorPalleteLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mActivity = (HealthCocoActivity) context;
        inflate(getContext(), R.layout.layout_diagram_color_pallete, this);
        gvColors = (GridView) findViewById(R.id.gv_colors);
    }

    public void initData(DiagramCanvasListener diagramCanvasListener) {
        DiagramColorPalleteGridListAdapter adapter = new DiagramColorPalleteGridListAdapter(mActivity, diagramCanvasListener);
        adapter.setListData(Arrays.asList(ColorType.values()));
        gvColors.setAdapter(adapter);
    }
}
