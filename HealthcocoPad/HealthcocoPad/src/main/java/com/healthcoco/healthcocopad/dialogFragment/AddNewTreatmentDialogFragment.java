package com.healthcoco.healthcocopad.dialogFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.TreatmentService;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.AddNewDrugListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;

/**
 * Created by Shreshtha on 17-07-2017.
 */

public class AddNewTreatmentDialogFragment extends HealthCocoDialogFragment implements View.OnClickListener, Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener {
    private EditText editTreatmentName;
    private EditText editTreatmentCost;
    private TextView titleTextView;
    private LoginResponse doctor;
    private AddNewDrugListener addNewDrugListener;
    private User user;
    private String uniqueId;
    private Bundle bundle;
    private TreatmentService treatmentService;

    public AddNewTreatmentDialogFragment() {
    }

    public AddNewTreatmentDialogFragment(AddNewDrugListener addNewDrugListener) {
        this.addNewDrugListener = addNewDrugListener;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_new_treatment, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        bundle = getArguments();
        uniqueId = bundle.getString(HealthCocoConstants.TAG_UNIQUE_ID);
        if (!Util.isNullOrBlank(uniqueId))
            treatmentService = LocalDataServiceImpl.getInstance(mApp).getTreatmentService(uniqueId);

        init();
    }

    @Override
    public void init() {
        doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
        if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
            user = doctor.getUser();
            initViews();
            initListeners();
        }
    }

    @Override
    public void initViews() {
        editTreatmentName = (EditText) view.findViewById(R.id.edit_treatment_name);
        editTreatmentCost = (EditText) view.findViewById(R.id.edit_treatment_cost);
        titleTextView = (TextView) view.findViewById(R.id.tv_title);

        titleTextView.setText(R.string.new_treatment);

        if (treatmentService != null) {
            editTreatmentName.setText(treatmentService.getName());
            editTreatmentCost.setText(Util.getFormattedDoubleNumber(treatmentService.getCost()));
        }
    }

    @Override
    public void initListeners() {
        initSaveCancelButton(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_save:
                validateData();
                break;
        }
    }

    private void validateData() {
        String msg = null;
        String treatmentName = String.valueOf(editTreatmentName.getText());
        if (Util.isNullOrBlank(treatmentName))
            msg = getResources().getString((R.string.please_enter_service_name));

        if (Util.isNullOrBlank(msg)) {
            addTreatment(treatmentName);
        } else
            Util.showToast(mActivity, msg);
    }

    private void addTreatment(String treatmentName) {
        TreatmentService treatmentListSolrResponse = new TreatmentService();
        if (!Util.isNullOrBlank(uniqueId))
            treatmentListSolrResponse.setUniqueId(uniqueId);
        treatmentListSolrResponse.setName(treatmentName);
        treatmentListSolrResponse.setDoctorId(user.getUniqueId());
        treatmentListSolrResponse.setHospitalId(user.getForeignHospitalId());
        treatmentListSolrResponse.setLocationId(user.getForeignLocationId());
        String treatmentCost = String.valueOf(editTreatmentCost.getText());
        if (!Util.isNullOrBlank(treatmentCost)) {
            double parseDouble = Double.parseDouble(treatmentCost);
            treatmentListSolrResponse.setCost(parseDouble);
        } else treatmentListSolrResponse.setCost(0);
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addTreatmentListSolr(TreatmentService.class, treatmentListSolrResponse, this, this);
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        String errorMsg = null;
        if (volleyResponseBean != null && !Util.isNullOrBlank(volleyResponseBean.getErrMsg())) {
            errorMsg = volleyResponseBean.getErrMsg();
        } else {
            errorMsg = getResources().getString(R.string.error);
        }
        Util.showToast(mActivity, errorMsg);
        mActivity.hideLoading();
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onResponse(VolleyResponseBean response) {
        switch (response.getWebServiceType()) {
            case ADD_TREATMENT_SERVICE:
                if (response.getData() != null && response.getData() instanceof TreatmentService) {
                    TreatmentService treatmentListSolrResponse = (TreatmentService) response.getData();
                    Util.showToast(mActivity, String.format(getResources().getString(R.string.success_treatment_added), Util.getValidatedValueOrBlankWithoutTrimming(editTreatmentName)));
//                    mActivity.setResult(HealthCocoConstants.RESULT_CODE_ADD_NEW_TREATMENT_DETAIL, null);
                    if (addNewDrugListener != null)
                        addNewDrugListener.onSaveClicked(treatmentListSolrResponse);
                    else {
                        LocalDataServiceImpl.getInstance(mApp).addTreatmentSerice((TreatmentService) response.getData());
                        getTargetFragment().onActivityResult(HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST, HealthCocoConstants.RESULT_CODE_REFERENCE_LIST, null);
                    }
                    dismiss();
                }
                break;
            default:
                break;
        }
        mActivity.hideLoading();
    }


}
