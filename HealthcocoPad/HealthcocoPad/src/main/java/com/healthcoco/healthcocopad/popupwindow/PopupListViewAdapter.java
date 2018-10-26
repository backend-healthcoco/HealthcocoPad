package com.healthcoco.healthcocopad.popupwindow;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.EquivalentQuantities;
import com.healthcoco.healthcocopad.bean.server.AppointmentSlot;
import com.healthcoco.healthcocopad.bean.server.AvailableTimeSlots;
import com.healthcoco.healthcocopad.bean.server.ClinicDoctorProfile;
import com.healthcoco.healthcocopad.bean.server.DoctorClinicProfile;
import com.healthcoco.healthcocopad.bean.server.DoctorProfile;
import com.healthcoco.healthcocopad.bean.server.RegisteredDoctorProfile;
import com.healthcoco.healthcocopad.dialogFragment.BookAppointmentDialogFragment;
import com.healthcoco.healthcocopad.enums.AppointmentSlotsType;
import com.healthcoco.healthcocopad.enums.PatientProfileScreenType;
import com.healthcoco.healthcocopad.enums.PatientTreatmentStatus;
import com.healthcoco.healthcocopad.enums.PopupWindowType;
import com.healthcoco.healthcocopad.enums.QuantityType;
import com.healthcoco.healthcocopad.enums.UnitType;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.DownloadImageFromUrlUtil;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.TextViewFontAwesome;

import java.util.List;

/**
 * Created by neha on 11/05/17.
 */

public class PopupListViewAdapter extends BaseAdapter {
    private int dropDownLayoutId;
    private PopupWindowType popupWindowType;
    private HealthCocoActivity mActivity;
    private List<Object> list;
    private TextViewFontAwesome tvBullet;

    public PopupListViewAdapter(HealthCocoActivity mActivity, PopupWindowType popupWindowType, int dropDownLayoutId) {
        this.mActivity = mActivity;
        this.dropDownLayoutId = dropDownLayoutId;
        this.popupWindowType = popupWindowType;

    }

    @Override
    public int getCount() {
        if (list == null)
            return 0;
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            switch (popupWindowType) {
                case DOCTOR_LIST:
                    convertView = mActivity.getLayoutInflater().inflate(R.layout.item_clinic_doctor_list, null);
                    break;
                default:
                    convertView = mActivity.getLayoutInflater().inflate(dropDownLayoutId, null);
                    break;
            }
        }
        try {
            applyData(convertView, getItem(position));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    private void applyData(View convertView, Object objData) {
        convertView.setTag(objData);
        switch (popupWindowType) {
            case DOCTOR_LIST:
                try {
                    LinearLayout layoutImage = (LinearLayout) convertView.findViewById(R.id.layout_image);
                    TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
                    TextView tvInitialAlphabet = (TextView) convertView.findViewById(R.id.tv_initial_aplhabet);
                    ProgressBar progressLoading = (ProgressBar) convertView.findViewById(R.id.progress_loading);
                    ImageView ivContactProfile = (ImageView) convertView.findViewById(R.id.iv_image);
                    switch (popupWindowType) {
                        case DOCTOR_LIST:
                            RegisteredDoctorProfile clinicDoctorProfile = null;
                            if (objData instanceof RegisteredDoctorProfile)
                                clinicDoctorProfile = (RegisteredDoctorProfile) objData;
                            if (clinicDoctorProfile != null) {
                                tvName.setText(Util.getValidatedValue(clinicDoctorProfile.getFirstNameWithTitle()));
                                DownloadImageFromUrlUtil.loadImageWithInitialAlphabet(mActivity, PatientProfileScreenType.IN_EMR_HEADER, clinicDoctorProfile, progressLoading, ivContactProfile, tvInitialAlphabet);
                            }
                            layoutImage.setVisibility(View.VISIBLE);
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                TextView textView;
                if (convertView instanceof TextView)
                    textView = (TextView) convertView;
                else
                    textView = (TextView) convertView.findViewById(R.id.tv_text);
                String text = getText(mActivity, objData);
                if (!Util.isNullOrBlank(text)) {
                    textView.setText(text);
                }
                break;
        }
    }

    private String getText(HealthCocoActivity context, Object object) {
        String text = "";
        switch (popupWindowType) {
            case DISCOUNT_TYPE:
                if (object instanceof UnitType) {
                    UnitType unitType = (UnitType) object;
                    text = context.getResources().getString(unitType.getSymbolId());
                }
                break;
            case DOCTOR_CLINIC_PROFILE:
                if (object instanceof DoctorClinicProfile) {
                    DoctorClinicProfile doctorClinicProfile = (DoctorClinicProfile) object;
                    text = Util.getValidatedValue(doctorClinicProfile.getLocationName());
                }
                break;
            case STATUS_TYPE:
                if (object instanceof PatientTreatmentStatus) {
                    PatientTreatmentStatus treatmentStatus = (PatientTreatmentStatus) object;
                    text = treatmentStatus.getTreamentStatus();
                }
                break;
            case SERVING_TYPE:
                if (object instanceof EquivalentQuantities) {
                    EquivalentQuantities equivalentQuantities = (EquivalentQuantities) object;
                    text = equivalentQuantities.getServingType().getUnit();
                }
                break;
            case APPOINTMENT_SLOT:
                if (object instanceof AppointmentSlotsType) {
                    AppointmentSlotsType appointmentSlotsType = (AppointmentSlotsType) object;
                    text = Math.round(appointmentSlotsType.getTime()) + " " + Util.getValidatedValue(appointmentSlotsType.getUnits().getValueToDisplay());
                }
                break;
            case NUTRIENT_TYPE:
                if (object instanceof QuantityType) {
                    QuantityType quantityType = (QuantityType) object;
                    text = quantityType.getUnit();
                }
                break;
            case QUANTITY_TYPE:
                if (object instanceof EquivalentQuantities) {
                    EquivalentQuantities quantityType = (EquivalentQuantities) object;
                    text = quantityType.getServingType().getUnit();
                }
                break;
            case TIME_SLOTS:
                if (object instanceof AvailableTimeSlots) {
                    AvailableTimeSlots availableTimeSlots = (AvailableTimeSlots) object;
                    text = DateTimeUtil.convertFormattedDate(DateTimeUtil.TIME_FORMAT_24_HOUR, BookAppointmentDialogFragment.TIME_SLOT_FORMAT_USED_IN_THIS_SCREEN, availableTimeSlots.getTime());
                    if (availableTimeSlots.getIsAvailable() != null && availableTimeSlots.getIsAvailable()) {
                        tvBullet.setSelected(false);
                    } else
                        tvBullet.setSelected(true);
                }
                break;
            default:
                if (object instanceof String)
                    text = (String) object;
                break;
        }
        return text;

    }

    public void setListData(List<Object> listData) {
        this.list = listData;
    }

}




