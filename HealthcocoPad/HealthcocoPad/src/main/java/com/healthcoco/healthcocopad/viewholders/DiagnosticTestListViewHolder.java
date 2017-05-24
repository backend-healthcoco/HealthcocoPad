package com.healthcoco.healthcocopad.viewholders;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.DiagnosticTest;
import com.healthcoco.healthcocopad.listeners.DiagnosticTestItemListener;
import com.healthcoco.healthcocopad.utilities.Util;

/**
 * Created by neha on 13/04/16.
 */
public class DiagnosticTestListViewHolder extends HealthCocoViewHolder implements View.OnClickListener {

    private DiagnosticTestItemListener itemListener;
    private TextView tvDiagnosisName;
    private DiagnosticTest diagnosticTest;
    private Button btDelete;

    public DiagnosticTestListViewHolder(HealthCocoActivity mActivity, DiagnosticTestItemListener itemListener) {
        super(mActivity);
        this.mActivity = mActivity;
        this.itemListener = itemListener;
    }

    @Override
    public void setData(Object object) {
        this.diagnosticTest = (DiagnosticTest) object;
    }

    @Override
    public void applyData() {
        tvDiagnosisName.setText(diagnosticTest.getTestName());
    }

    @Override
    public View getContentView() {
        LinearLayout view;
        boolean visitToggleStateFromPreferences = Util.getVisitToggleStateFromPreferences(mActivity);
        if (visitToggleStateFromPreferences) {
            view = (LinearLayout) inflater.inflate(R.layout.list_item_diagnostic_test, null);
            tvDiagnosisName = (TextView) view.findViewById(R.id.tv_name);
            btDelete = (Button) view.findViewById(R.id.bt_delete_diagnostic_test);
            btDelete.setOnClickListener(this);
        } else {
            view = (LinearLayout) inflater.inflate(R.layout.list_item_lab_test, null);
            tvDiagnosisName = (TextView) view.findViewById(R.id.tv_name);
            tvDiagnosisName.setOnClickListener(this);
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_delete_diagnostic_test:
                itemListener.onDeleteItemClicked(diagnosticTest);
                break;
            case R.id.tv_name:
                itemListener.onDiagnosticTestClicked(diagnosticTest);
                break;
        }
    }
}
