package com.healthcoco.healthcocopad.viewholders;

import android.view.View;
import android.widget.LinearLayout;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.PatientTreatment;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.listeners.CommonEMRItemClickListener;
import com.healthcoco.healthcocopad.listeners.VisitDetailCombinedItemListener;

/**
 * Created by Shreshtha on 25-03-2017.
 */
public class TreatmentListItemViewHolder extends HealthCocoViewHolder {
    private static final String TAG = TreatmentListItemViewHolder.class.getSimpleName();
    private User user;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private VisitDetailCombinedItemListener detailCombinedItemListener;
    private CommonEMRItemClickListener commonEmrClickListener;
    private PatientTreatment prescription;
    private LinearLayout layoutDiscarded;

    public TreatmentListItemViewHolder(HealthCocoActivity mActivity,
                                       Object listenerObject, boolean isInPrescriptionsList) {
        super(mActivity);
        if (!isInPrescriptionsList) {
            this.detailCombinedItemListener = (VisitDetailCombinedItemListener) listenerObject;
            this.user = detailCombinedItemListener.getUser();
            this.selectedPatient = detailCombinedItemListener.getSelectedPatient();
        } else {
            this.commonEmrClickListener = (CommonEMRItemClickListener) listenerObject;
            this.user = commonEmrClickListener.getUser();
            this.selectedPatient = commonEmrClickListener.getSelectedPatient();
        }
    }

    @Override
    public void setData(Object object) {
        this.prescription = (PatientTreatment) object;
    }

    @Override
    public void applyData() {

    }

    @Override
    public View getContentView() {
        View contentView = inflater.inflate(R.layout.layout_sub_item_treatment, null);
        initViews(contentView);
        initListeners();
        return contentView;
    }

    private void initViews(View contentView) {
        layoutDiscarded = (LinearLayout) contentView.findViewById(R.id.layout_discarded);
    }

    private void initListeners() {

    }

    private void checkIsDiscarded(boolean isDiscarded) {
        if (isDiscarded)
            layoutDiscarded.setVisibility(View.VISIBLE);
        else layoutDiscarded.setVisibility(View.GONE);
    }
}
