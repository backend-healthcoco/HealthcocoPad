package com.healthcoco.healthcocopad.popupwindow;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.ClinicDoctorProfile;
import com.healthcoco.healthcocopad.enums.AdapterType;
import com.healthcoco.healthcocopad.enums.PopupWindowType;
import com.healthcoco.healthcocopad.listeners.CBSelectedItemTypeListener;
import com.healthcoco.healthcocopad.listeners.DoctorListPopupWindowListener;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by neha on 03/05/17.
 */

public class DoctorListPopupWindow extends PopupWindow implements View.OnClickListener, AdapterView.OnItemClickListener, CBSelectedItemTypeListener {
    private final View anchorView;
    HealthcocoRecyclerViewAdapter popupViewAdapter;
    private List<Object> list;
    private DoctorListPopupWindowListener doctorListPopupWindowListener;
    private HealthCocoActivity mActivity;
    private PopupWindowType popupWindowType;
    private int dropDownLayoutId;
    private LinearLayout layoutSelectAll;
    private TextView tvClearAll;
    private TextView tvApply;
    private LinkedHashMap<String, ClinicDoctorProfile> requestLinkedHashMapForValidate = new LinkedHashMap<String, ClinicDoctorProfile>();
    private CheckBox cbSelectAll;
    private boolean selectAll;
    private boolean isInitialLaunch;

    public DoctorListPopupWindow(Context context, View view, PopupWindowType popupWindowType, List<Object> list, DoctorListPopupWindowListener doctorListPopupWindowListener) {
        this(context, view, popupWindowType, list, R.layout.spinner_drop_down_item_grey_background, doctorListPopupWindowListener);
    }

    public DoctorListPopupWindow(Context context, View view, PopupWindowType popupWindowType, List<Object> list, int dropDownLayoutId, DoctorListPopupWindowListener doctorListPopupWindowListener) {
        super(context);
        this.mActivity = (HealthCocoActivity) context;
        this.popupWindowType = popupWindowType;
        this.doctorListPopupWindowListener = doctorListPopupWindowListener;
        this.dropDownLayoutId = dropDownLayoutId;
        this.list = list;
        this.anchorView = view;
    }

    public View getPopupView() {
        LinearLayout linearLayout = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.popup_window_doctor_list, null);
        cbSelectAll = (CheckBox) linearLayout.findViewById(R.id.cb_select_all);
        layoutSelectAll = (LinearLayout) linearLayout.findViewById(R.id.layout_select_all);
        tvClearAll = (TextView) linearLayout.findViewById(R.id.tv_clear_all);
        tvApply = (TextView) linearLayout.findViewById(R.id.tv_apply);
        RecyclerView recyclerView = (RecyclerView) linearLayout.findViewById(R.id.lv_popup_options);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        popupViewAdapter = new HealthcocoRecyclerViewAdapter(mActivity, AdapterType.DOCTOR_POPUP_LIST, this);
        popupViewAdapter.setListData((ArrayList<Object>) (Object) list);
        recyclerView.setAdapter(popupViewAdapter);

//        popupListViewAdapter = new DoctorPopupListViewAdapter(mActivity, popupWindowType, dropDownLayoutId, this);
//        popupListViewAdapter.setListData(list);
//        lvList.setAdapter(popupListViewAdapter);
//        lvList.setOnItemClickListener(this);
        tvClearAll.setOnClickListener(this);
        tvApply.setOnClickListener(this);
        layoutSelectAll.setOnClickListener(this);

        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setOutsideTouchable(false);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
//        setInputMethodMode(ListPopupWindow.INPUT_METHOD_NEEDED);
        if (anchorView != null)
            anchorView.setOnClickListener(this);

        return linearLayout;
    }

    public void showOptionsWindow(View v) {
//        showAsDropDown(v, -5, 0);
        showAsDropDown(v);
        isInitialLaunch = true;
//        showAsDropDown(v, 0, 0, Gravity.NO_GRAVITY);
        update(v, 0, 0, anchorView.getWidth(), LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public void showOptionsWindowAtLeftCenter(View v) {
        showAtLocation(v, Gravity.LEFT | Gravity.CENTER, 0, 0);
        update(v, 0, 0, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object tag = view.getTag();
        if (anchorView != null && anchorView instanceof TextView && tag instanceof String) {
            TextView tvText = (TextView) anchorView;
            tvText.setText((String) tag);
        }
        if (doctorListPopupWindowListener != null)
//            doctorListPopupWindowListener.onDoctorSelected(popupWindowType, tag);
            dismiss();
    }

    @Override
    public void onClick(View v) {
        if (anchorView != null && anchorView.getId() == v.getId()) {
            if (list != null && list.size() > 0)
                showOptionsWindow(v);
            else doctorListPopupWindowListener.onEmptyListFound();
        }
        switch (v.getId()) {
            case R.id.layout_select_all:
                selectAll = (!cbSelectAll.isChecked());
//                setSelectAllSelected(!cbSelectAll.isChecked());
                popupViewAdapter.notifyDataSetChanged();
                isInitialLaunch = false;
                break;
            case R.id.tv_clear_all:
                selectAll = false;
//                setSelectAllSelected(false);
                isInitialLaunch = false;
                popupViewAdapter.notifyDataSetChanged();
                break;
            case R.id.tv_apply:
                doctorListPopupWindowListener.onDoctorSelected(new ArrayList<ClinicDoctorProfile>(requestLinkedHashMapForValidate.values()));
                isInitialLaunch = false;
                dismiss();
                break;

        }
    }

    public void notifyAdapter(ArrayList<Object> list) {
        this.list = list;
        popupViewAdapter.setListData(list);
        popupViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCBSelectedItemTypeCheckClicked(boolean isChecked, Object object) {
        if (object instanceof ClinicDoctorProfile) {
            ClinicDoctorProfile clinicDoctorProfile = ((ClinicDoctorProfile) object);
            if (isChecked)
                requestLinkedHashMapForValidate.put(clinicDoctorProfile.getUniqueId(), clinicDoctorProfile);
            else requestLinkedHashMapForValidate.remove(clinicDoctorProfile.getUniqueId());
        }
        if (requestLinkedHashMapForValidate.size() == list.size()) {
            setSelectAllSelected(true);
        } else
            cbSelectAll.setChecked(false);


    }

    @Override
    public boolean isSelectAll() {
        return selectAll;
    }

    @Override
    public void setSelectAllSelected(boolean isSelected) {
        cbSelectAll.setChecked(isSelected);
    }

    @Override
    public ArrayList<String> getDoctorProfileArrayList() {
        return new ArrayList<String>(requestLinkedHashMapForValidate.keySet());
    }

    @Override
    public boolean isInitialLaunch() {
        return isInitialLaunch;
    }
}
