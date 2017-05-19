package com.healthcoco.healthcocopad.dialogFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.adapter.CommonOptionsDialogListAdapter;
import com.healthcoco.healthcocopad.enums.DialogType;
import com.healthcoco.healthcocopad.listeners.CommonOptionsDialogItemClickListener;

public class CommonOptionsDialogListFragment extends HealthCocoDialogFragment
        implements OnItemClickListener {

    private DialogType dialogTypeTitle;
    private TextView tvTitle;
    private CommonOptionsDialogListAdapter adapter;
    private ListView lvList;
    private CommonOptionsDialogItemClickListener listener;
    private EditText editSearch;

    public CommonOptionsDialogListFragment() {

    }

    public CommonOptionsDialogListFragment(DialogType dialogTypeTitle,
                                           CommonOptionsDialogItemClickListener listener) {
        this.listener = listener;
        this.dialogTypeTitle = dialogTypeTitle;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_common_options, null);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        tvTitle.setText(getResources().getString(dialogTypeTitle.getStringId()));
        initListData();
    }

    @Override
    public void initViews() {
        lvList = (ListView) view.findViewById(R.id.lv_options);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
    }

    @Override
    public void initListeners() {
        lvList.setOnItemClickListener(this);
    }

    @Override
    public void initData() {

    }

    private void initListData() {
        adapter = new CommonOptionsDialogListAdapter(mActivity);
        adapter.setListData(dialogTypeTitle.getOptionsType());
        lvList.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        listener.onOptionsItemSelected(dialogTypeTitle.getOptionsType().get(position));
        dismiss();
    }
}
