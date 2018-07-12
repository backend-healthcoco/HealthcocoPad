package com.healthcoco.healthcocopad.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.enums.KioskSubItemType;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoComonRecylcerViewHolder;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoRecyclerViewItemClickListener;

/**
 * Created by Prashant on 23-06-18.
 */

public class KioskSubItemViewHolder extends HealthcocoComonRecylcerViewHolder {

    private HealthCocoActivity mActivity;
    private KioskSubItemType kioskSubItemType;
    private ImageView ivImage;
    private TextView tvTitle;


    public KioskSubItemViewHolder(HealthCocoActivity mActivity, View itemView, HealthcocoRecyclerViewItemClickListener itemClickListener) {
        super(mActivity, itemView, itemClickListener);
        this.mActivity = mActivity;
    }

    @Override
    public void initViews(View itemView) {

        tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        ivImage = (ImageView) itemView.findViewById(R.id.iv_image);


        if (onItemClickListener != null)
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClicked(kioskSubItemType.ordinal());
                }
            });

    }

    @Override
    public void applyData(Object object) {
        kioskSubItemType = (KioskSubItemType) object;
        if (kioskSubItemType != null) {
            tvTitle.setText(kioskSubItemType.getTitleId());
            ivImage.setImageResource(kioskSubItemType.getDrawableId());
        }
    }


}
