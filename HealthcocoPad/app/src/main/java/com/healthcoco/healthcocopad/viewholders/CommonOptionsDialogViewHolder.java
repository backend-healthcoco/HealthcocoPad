package com.healthcoco.healthcocopad.viewholders;

import android.view.View;
import android.widget.TextView;

import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.enums.OptionsType;

public class CommonOptionsDialogViewHolder extends HealthCocoViewHolder {

    private OptionsType optionsType;
    private TextView tvItemName;
    private HealthCocoActivity mActivity;

    public CommonOptionsDialogViewHolder(HealthCocoActivity mActivity) {
        super(mActivity);
        this.mActivity = mActivity;
    }

    @Override
    public void setData(Object data) {
        this.optionsType = (OptionsType) data;
    }

    @Override
    public void applyData() {
        tvItemName.setText(mActivity.getResources().getString(optionsType.getStringId()));

    }

    @Override
    public View getContentView() {
        View view = inflater.inflate(R.layout.list_item_common_options_dialog,
                null);
        tvItemName = (TextView) view
                .findViewById(R.id.tv_option);
        return view;
    }

}
