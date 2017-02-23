package com.healthcoco.healthcocoplus.viewholders;

import android.view.View;
import android.widget.TextView;

import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocoplus.HealthCocoActivity;
import com.healthcoco.healthcocoplus.HealthCocoViewHolder;
import com.healthcoco.healthcocoplus.enums.OptionsType;

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
