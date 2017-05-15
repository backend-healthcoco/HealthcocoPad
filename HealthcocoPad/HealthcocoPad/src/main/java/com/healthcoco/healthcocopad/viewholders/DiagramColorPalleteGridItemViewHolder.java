package com.healthcoco.healthcocopad.viewholders;

import android.view.View;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.enums.ColorType;
import com.healthcoco.healthcocopad.listeners.DiagramCanvasListener;
import com.healthcoco.healthcocopad.utilities.LogUtils;


/**
 * Created by Mohit on 29/02/16.
 */
public class DiagramColorPalleteGridItemViewHolder extends HealthCocoViewHolder implements View.OnClickListener {
    private static final String TAG = DiagramGridViewHolder.class.getSimpleName();
    private DiagramCanvasListener diagramCanvasListener;
    private ColorType colorType;
    private View convertView;
    private View btColor;

    public DiagramColorPalleteGridItemViewHolder(HealthCocoActivity activity, DiagramCanvasListener diagramCanvasListener) {
        super(activity);
        this.diagramCanvasListener = diagramCanvasListener;
    }

    @Override
    public void setData(Object object) {
        colorType = (ColorType) object;
    }

    @Override
    public void applyData() {
        LogUtils.LOGD(TAG, "Color " + colorType.getColorId());
        btColor.setBackgroundColor(mActivity.getResources().getColor(colorType.getColorId()));
    }

    @Override
    public View getContentView() {
        convertView = inflater.inflate(R.layout.grid_item_color_pallete, null);
        btColor = (View) convertView.findViewById(R.id.bt_color);
        convertView.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        diagramCanvasListener.onColorselected(colorType);
    }
}
