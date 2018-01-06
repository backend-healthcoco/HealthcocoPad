package com.healthcoco.healthcocopad.viewholders;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoApplication;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.SyncAll;
import com.healthcoco.healthcocopad.fragments.SyncFragment;
import com.healthcoco.healthcocopad.listeners.SyncAllItemListener;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;

import java.util.Date;

/**
 * Created by neha on 08/02/16.
 */
public class SyncAllViewholder extends HealthCocoViewHolder {
    private HealthCocoActivity mActivity;
    private TextView tvItemName;
    private TextView tvSyncedDate;
    private TextView tvRefresh;
    private SyncAllItemListener syncAllItemListener;
    private SyncAll syncAll;
    private Animation refreshAnimation;
    private HealthCocoApplication mApp;


    public SyncAllViewholder(HealthCocoActivity mActivity, SyncAllItemListener syncAllItemListener) {
        super(mActivity);
        this.mActivity = mActivity;
        this.syncAllItemListener = syncAllItemListener;
    }

    @Override
    public void setData(Object object) {
        this.syncAll = (SyncAll) object;

    }

    @Override
    public void applyData() {
        tvItemName.setText(syncAll.getSyncAllType().getNameId());
        if (syncAll.getLastSyncedTime() != null)
            tvSyncedDate.setText(DateTimeUtil.getFormatedDateAndTime(SyncFragment.DATE_FORMAT, syncAll.getLastSyncedTime()));
        else
            tvSyncedDate.setText(R.string.not_synced);
        if (syncAll.isLoading())
            startRefreshAnimation();
        else
            stopRefreshAnimation();
    }


    @Override
    public View getContentView() {
        View view = (View) inflater.inflate(R.layout.item_sync_all, null);
        tvItemName = (TextView) view.findViewById(R.id.tv_item_name);
        tvSyncedDate = (TextView) view.findViewById(R.id.tv_synced_date);
        tvRefresh = (TextView) view.findViewById(R.id.tv_refresh);
        refreshAnimation = AnimationUtils.loadAnimation(mActivity, R.anim.rotate_inifinite);
        return view;
    }

    public void startRefreshAnimation() {
        tvRefresh.startAnimation(refreshAnimation);
    }

    public void stopRefreshAnimation() {
        refreshAnimation.cancel();
//        refreshData();
    }

    public void refreshData() {
        syncAllItemListener.removeFromSyncTypesList(syncAll.getSyncAllType());
        syncAll.setLastSyncedTime(new Date().getTime());
        applyData();
        LocalDataServiceImpl.getInstance(mApp).addSyncAllObject(syncAll);
    }

}
