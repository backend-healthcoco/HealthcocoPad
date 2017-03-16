package com.healthcoco.healthcocopad.dialogFragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ListView;
import android.widget.TextView;

import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.adapter.CommonListDialogAdapter;
import com.healthcoco.healthcocopad.bean.server.Specialities;
import com.healthcoco.healthcocopad.enums.CommonListDialogType;
import com.healthcoco.healthcocopad.listeners.CommonListDialogItemClickListener;
import com.healthcoco.healthcocopad.utilities.ComparatorUtil;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by Shreshtha on 24-01-2017.
 */

public class CommonListDialogFragment extends HealthCocoDialogFragment implements
        TextWatcher, View.OnKeyListener, TextView.OnEditorActionListener {
    private List<?> list;
    private CommonListDialogType commonListDialogType;
    private CommonListDialogItemClickListener commonListDialogItemClickListener;
    private ListView listView;
    private CommonListDialogAdapter mAdapter;
    private TextView tvNoResultFound;

    public CommonListDialogFragment() {
    }

    public CommonListDialogFragment(CommonListDialogItemClickListener commonListDialogItemClickListener, CommonListDialogType commonListDialogType, List<?> list) {
        this.commonListDialogItemClickListener = commonListDialogItemClickListener;
        this.commonListDialogType = commonListDialogType;
        this.list = list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_common_list, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
//        setHeight(0.95);
        init();
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        initAdapter();
        initData();
    }

    @Override
    public void initViews() {
        listView = (ListView) view.findViewById(R.id.lv_list);
        tvNoResultFound = (TextView) view.findViewById(R.id.tv_no_result_found);
        if (isBottomOptionsDialog) {
//            hideEditSearchView();
        } else if (commonListDialogType.isAddCustomAllowed()) {
//            getSearchEditText().setImeOptions(EditorInfo.IME_ACTION_DONE);
//            initEditSearchView(commonListDialogType.getHint(), this, null, this, false);
        } else
            initEditSearchView(commonListDialogType.getHint(), this, false);
        tvNoResultFound.setText(commonListDialogType.getNoResultFoundTextId());
    }


    @Override
    public void initListeners() {
    }

    @Override
    public void initData() {
    }

    private void initAdapter() {
        sortList(list);
        mAdapter = new CommonListDialogAdapter(mActivity, commonListDialogItemClickListener);
        mAdapter.setListData(commonListDialogType, list);
        listView.setAdapter(mAdapter);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        ArrayList tempList = new ArrayList<>();
        if (editable != null) {
            String search = String.valueOf(editable).toLowerCase(Locale.ENGLISH);
            if (!Util.isNullOrEmptyList(list)) {
                if (search.length() == 0) {
                    tempList.addAll(list);
                } else {
                    for (Object object : list) {
                        switch (commonListDialogType) {
                            case SPECIALITY:
                                Specialities speciality = (Specialities) object;
                                if (!Util.isNullOrBlank(speciality.getSuperSpeciality()) && speciality.getSuperSpeciality().toLowerCase(Locale.ENGLISH)
                                        .contains(search)) {
                                    tempList.add(object);
                                }
                                break;
                        }
                    }
                }
            }
        }
        notifyAdapter(tempList);
    }

    private void notifyAdapter(List<?> tempList) {
        if (!Util.isNullOrEmptyList(tempList)) {
            sortList(tempList);
            listView.setVisibility(View.VISIBLE);
            tvNoResultFound.setVisibility(View.GONE);
            mAdapter.setListData(commonListDialogType, tempList);
            mAdapter.notifyDataSetChanged();
        } else {
            listView.setVisibility(View.GONE);
            tvNoResultFound.setVisibility(View.VISIBLE);
        }
    }

    private void sortList(List<?> list) {
        switch (commonListDialogType) {
            case SPECIALITY:
                Collections.sort(list, ComparatorUtil.specialityListComparator);
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            Object object = null;
            if (object != null) {
                commonListDialogItemClickListener.onDialogItemClicked(commonListDialogType, object);
                dismiss();
            }
        }
        return false;
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        return false;
    }
}
