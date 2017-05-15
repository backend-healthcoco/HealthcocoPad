package com.healthcoco.healthcocopad.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.enums.ColorType;
import com.healthcoco.healthcocopad.listeners.DiagramCanvasListener;
import com.healthcoco.healthcocopad.viewholders.DiagramColorPalleteGridItemViewHolder;

import java.util.List;

/**
 * Created by Mohit on 29/02/16.
 */
public class DiagramColorPalleteGridListAdapter extends BaseAdapter {
    private DiagramCanvasListener diagramCanvasListener;
    private List<ColorType> list;
    private HealthCocoActivity mActivity;
    private DiagramColorPalleteGridItemViewHolder holder;

    public DiagramColorPalleteGridListAdapter() {
    }

    public DiagramColorPalleteGridListAdapter(HealthCocoActivity activity, DiagramCanvasListener diagramCanvasListener) {
        this.mActivity = activity;
        this.diagramCanvasListener = diagramCanvasListener;
    }

    @Override
    public int getCount() {
        if (list == null)
            return 0;
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new DiagramColorPalleteGridItemViewHolder(mActivity, diagramCanvasListener);
            convertView = holder.getContentView();
            convertView.setTag(holder);
        } else
            holder = (DiagramColorPalleteGridItemViewHolder) convertView.getTag();
        holder.setData(getItem(position));
        holder.applyData();
        return convertView;
    }

    public void setListData(List<ColorType> list) {
        this.list = list;
    }
}
