package com.healthcoco.healthcocopad.viewholders;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoApplication;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.Achievement;
import com.healthcoco.healthcocopad.custom.AutoCompleteTextViewAdapter;
import com.healthcoco.healthcocopad.enums.AutoCompleteTextViewType;
import com.healthcoco.healthcocopad.listeners.AchievementDetailItemListner;
import com.healthcoco.healthcocopad.listeners.AutoCompleteTextViewListener;
import com.healthcoco.healthcocopad.utilities.EditTextTextViewErrorUtil;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Shreshtha on 20-02-2017.
 */
public class AchievementDetailListViewHolder extends LinearLayout implements View.OnClickListener, AdapterView.OnItemClickListener, AutoCompleteTextViewListener {
    private HealthCocoActivity mActivity;
    private HealthCocoApplication mApp;
    private TextView tvAwardsAndPublication;
    private ImageButton btDelete;
    private AutoCompleteTextView autoTvYearOfAchievement;
    private Achievement objData;
    private AchievementDetailItemListner itemClickListener;

    public AchievementDetailListViewHolder(Context context) {
        super(context);
        init(context);
    }

    public AchievementDetailListViewHolder(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AchievementDetailListViewHolder(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mActivity = (HealthCocoActivity) context;
        this.mApp = (HealthCocoApplication) mActivity.getApplicationContext();
        inflate(context, R.layout.list_item_achievements_detail, this);
        EditTextTextViewErrorUtil.setUpUI(mActivity, this);
        initViews();
        initListeners();
    }

    private List<?> getYearsList() {
        ArrayList<String> yearsList = new ArrayList<String>();
        for (int i = 1900; i <= Calendar.getInstance().get(Calendar.YEAR); i++) {
            yearsList.add(String.valueOf(i));
        }
        return yearsList;
    }

    private void initViews() {
        tvAwardsAndPublication = (TextView) findViewById(R.id.tv_awards_and_publication);
        btDelete = (ImageButton) findViewById(R.id.bt_delete);
        autoTvYearOfAchievement = (AutoCompleteTextView) findViewById(R.id.autotv_year_of_passing);
    }

    private void initListeners() {
        btDelete.setOnClickListener(this);
        autoTvYearOfAchievement.setOnItemClickListener(this);
        autoTvYearOfAchievement.setOnClickListener(this);
    }

    public void setData(Achievement achievement, AchievementDetailItemListner itemClickListener, int position) {
        this.itemClickListener = itemClickListener;
        this.objData = achievement;
        autoTvYearOfAchievement.setId(position);
        if (objData != null) {
            tvAwardsAndPublication.setText(Util.getValidatedValue(objData.getAchievementName()));
            autoTvYearOfAchievement.setText(Util.getValidatedValue(objData.getYear()));
        }
        initAutoTvAdapter();
    }

    private void initAutoTvAdapter() {
        try {
            ArrayList<Object> list = (ArrayList<Object>) (ArrayList<?>) getYearsList();
            if (!Util.isNullOrEmptyList(list)) {
                AutoCompleteTextViewAdapter adapter = new AutoCompleteTextViewAdapter(mActivity, this, R.layout.spinner_drop_down_item_grey_background,
                        list, AutoCompleteTextViewType.YEAR_OF_PASSING);
                autoTvYearOfAchievement.setThreshold(0);
                autoTvYearOfAchievement.setAdapter(adapter);
                autoTvYearOfAchievement.setDropDownAnchor(autoTvYearOfAchievement.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_delete:
                itemClickListener.onDeleteAchievementDetailClicked(this, objData);
                break;
        }
    }

    private void clearPreviousAlerts() {
        tvAwardsAndPublication.setActivated(false);
        autoTvYearOfAchievement.setActivated(false);
    }

    public Object getErrorMessageOrTrueIfValidated(boolean isOnSaveClick) {
        clearPreviousAlerts();
        ArrayList<View> errorViewList = new ArrayList<>();
        String msg = null;
        String achievements = Util.getValidatedValueOrNull(tvAwardsAndPublication);
        String yearOfPassing = Util.getValidatedValueOrNull(autoTvYearOfAchievement);
        if (isOnSaveClick && Util.isNullOrBlank(achievements) && Util.isNullOrBlank(yearOfPassing)) {
            return true;
        } else if (Util.isNullOrBlank(achievements) && Util.isNullOrBlank(yearOfPassing)) {
            msg = mActivity.getResources().getString(R.string.all_fields_are_mandatory);
            errorViewList.add(tvAwardsAndPublication);
            errorViewList.add(autoTvYearOfAchievement);
        } else if (Util.isNullOrBlank(achievements)) {
            msg = mActivity.getResources().getString(R.string.all_fields_are_mandatory);
            errorViewList.add(tvAwardsAndPublication);
        } else if (Util.isNullOrBlank(yearOfPassing)) {
            msg = mActivity.getResources().getString(R.string.all_fields_are_mandatory);
            errorViewList.add(autoTvYearOfAchievement);
        }
        if (Util.isNullOrBlank(msg)) {
            itemClickListener.addAchievementDetailToList(getAchievements());
            return true;
        }
        EditTextTextViewErrorUtil.showErrorOnEditText(mActivity, errorViewList, msg);
        return msg;
    }

    private Achievement getAchievements() {
        Achievement achievement = null;
        String qualification = Util.getValidatedValueOrNull(tvAwardsAndPublication);
        Integer yearOfPassing = Util.getValidatedIntegerValue(autoTvYearOfAchievement);
        if (!Util.isNullOrBlank(qualification)
                || yearOfPassing != null) {
            achievement = new Achievement();
            achievement.setAchievementName(qualification);
            achievement.setYear(yearOfPassing);
        }
        return achievement;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onEmptyListFound(AutoCompleteTextView autoCompleteTextView) {

    }

    @Override
    public void scrollToPosition(int position) {

    }
}
