package com.healthcoco.healthcocopad.viewholders;

import android.view.View;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.BabyAchievementsResponse;
import com.healthcoco.healthcocopad.listeners.BabyAchievementsListItemListener;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoComonRecylcerViewHolder;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoRecyclerViewItemClickListener;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.Util;

public class BabyAchievementViewHolder extends HealthcocoComonRecylcerViewHolder implements View.OnClickListener {
    private static final String DATE_FORMAT_USED_IN_THIS_SCREEN = "dd MMM, yyyy";
    private BabyAchievementsListItemListener babyAchievementsListItemListener;
    private BabyAchievementsResponse objDta;
    private TextView tvDate;
    private TextView tvAchievement;
    private TextView tvAchievementsDate;
    private TextView tvDuration;
    private TextView tvNotes;
    private TextView tvUpdateAchievement;

    public BabyAchievementViewHolder(HealthCocoActivity mActivity, View convertView, HealthcocoRecyclerViewItemClickListener onItemClickListener, Object listenerObject) {
        super(mActivity, convertView, onItemClickListener, listenerObject);
        babyAchievementsListItemListener = (BabyAchievementsListItemListener) listenerObject;
    }

    @Override
    public void initViews(View view) {
        tvDate = (TextView) view.findViewById(R.id.tv_date);
        tvAchievement = (TextView) view.findViewById(R.id.tv_achievements);
        tvAchievementsDate = (TextView) view.findViewById(R.id.tv_achievement_date);
        tvDuration = (TextView) view.findViewById(R.id.tv_duration);
        tvNotes = (TextView) view.findViewById(R.id.tv_notes);
        tvUpdateAchievement = (TextView) view.findViewById(R.id.tv_update_achievement);
        tvUpdateAchievement.setOnClickListener(this);
    }

    @Override
    public void applyData(Object object) {
        objDta = (BabyAchievementsResponse) object;
        if (objDta != null) {
            if (objDta.getUpdatedTime() != null)
                tvDate.setText(DateTimeUtil.getFormatedDate(objDta.getUpdatedTime()));

            if (!Util.isNullOrBlank(objDta.getAchievement()))
                tvAchievement.setText(Util.getValidatedValue(objDta.getAchievement()));
            else tvAchievement.setText(R.string.no_text_dash);

            if (objDta.getAchievementDate() != null && objDta.getAchievementDate() > 0)
                tvAchievementsDate.setText(DateTimeUtil.getFormatedDateAndTime(DATE_FORMAT_USED_IN_THIS_SCREEN,objDta.getAchievementDate()));
            else tvAchievementsDate.setText(R.string.no_text_dash);

            if (objDta.getDuration() != null)
                tvDuration.setText(Util.getValidatedValue(objDta.getDuration().getFormattedDurationWithUnit()));
            else tvDuration.setText(R.string.no_text_dash);

            if (!Util.isNullOrBlank(objDta.getNote()))
                tvNotes.setText(Util.getValidatedValue(objDta.getNote()));
            else tvNotes.setText(R.string.no_text_dash);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.tv_update_achievement) {
            babyAchievementsListItemListener.updateBabyAchievements(objDta);
        }
    }
}
