package com.healthcoco.healthcocopad.dialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.fragments.AddEditNormalVisitPrescriptionFragment;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;

public class AddAdviceDialogFragment extends HealthCocoDialogFragment implements View.OnClickListener {
    private EditText editAdvice;
    private TextView btCancel;
    private TextView btSave;
    private String advice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_add_advice, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        setWidthHeight(0.60, 0.60);
        init();
    }

    @Override
    public void init() {
        Bundle bundle = getArguments();
        advice = bundle.getString(AddEditNormalVisitPrescriptionFragment.TAG_ADVICE_STRING);
        initViews();
        initListeners();
        initData();
    }

    @Override
    public void initViews() {
        initActionbarTitle("Add Advice");
        editAdvice = (EditText) view.findViewById(R.id.edit_advice);
        btCancel = (TextView) view.findViewById(R.id.bt_cancel);
        btSave = (TextView) view.findViewById(R.id.bt_save);
    }

    @Override
    public void initListeners() {
        btCancel.setOnClickListener(this);
        btSave.setOnClickListener(this);
    }

    @Override
    public void initData() {
        if (!Util.isNullOrBlank(advice))
            editAdvice.setText(Util.getValidatedValue(advice));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_cancel:
                dismiss();
                break;
            case R.id.bt_save:
                Intent intent = new Intent();
                intent.putExtra(AddEditNormalVisitPrescriptionFragment.TAG_ADVICE_STRING, editAdvice.getText().toString());
                getTargetFragment().onActivityResult(HealthCocoConstants.REQUEST_CODE_ADD_ADVICE, HealthCocoConstants.RESULT_CODE_ADD_ADVICE, intent);
                dismiss();
                break;
        }
    }
}
