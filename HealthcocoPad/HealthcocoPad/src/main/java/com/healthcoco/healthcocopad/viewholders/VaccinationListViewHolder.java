package com.healthcoco.healthcocopad.viewholders;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.bean.server.VaccineCustomResponse;
import com.healthcoco.healthcocopad.bean.server.VaccineResponse;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.VaccineStatus;
import com.healthcoco.healthcocopad.listeners.VaccinationListener;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoComonRecylcerViewHolder;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoRecyclerViewItemClickListener;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VaccinationListViewHolder extends HealthcocoComonRecylcerViewHolder implements View.OnClickListener {

    public static final String TAG_VACCINE_DATA = "vaccineData";
    private static final String DATE_FORMAT_USED_IN_THIS_SCREEN = "dd MMM yyyy";
    private TextView tvVaccineStatus;
    private TextView tvDateOfVaccine;
    private TextView tvAgeForVaccine;
    private TextView tvVaccineName;
    private TextView tvUpdateVaccine;
    private TextView tvUpdateBy;
    private LinearLayout layoutUpdatedBy;
    private VaccineCustomResponse obj;
    private int count = 0;
    private TextView tvVaccineCount;
    private ArrayList<Boolean> booleans = new ArrayList<>();
    private VaccinationListener vaccinationListener;

    public VaccinationListViewHolder(HealthCocoActivity activity, View itemView, HealthcocoRecyclerViewItemClickListener onItemClickListener, Object listenerObject) {
        super(activity, itemView, onItemClickListener, listenerObject);
        vaccinationListener = (VaccinationListener) listenerObject;
    }

    @Override
    public void initViews(View itemView) {
        tvVaccineStatus = itemView.findViewById(R.id.tv_vaccine_status);
        tvDateOfVaccine = itemView.findViewById(R.id.tv_date_of_vaccine);
        tvAgeForVaccine = itemView.findViewById(R.id.tv_age_for_vaccine);
        tvVaccineName = itemView.findViewById(R.id.tv_vaccine_name);
        tvUpdateVaccine = itemView.findViewById(R.id.tv_update_vaccine);
        tvVaccineCount = itemView.findViewById(R.id.tv_vaccine_count);
        tvUpdateBy = itemView.findViewById(R.id.tv_updated_by);
        layoutUpdatedBy = itemView.findViewById(R.id.layout_updated_by);
        tvUpdateVaccine.setOnClickListener(this);
        layoutUpdatedBy.setVisibility(View.GONE);
    }

    @Override
    public void applyData(Object object) {
        obj = (VaccineCustomResponse) object;
        if (obj != null) {
            List<VaccineResponse> responseList = obj.getVaccineResponse();
            count = 0;
            for (VaccineResponse vaccineResponse :
                    responseList) {
                VaccineStatus status = vaccineResponse.getStatus();
                if (status == VaccineStatus.GIVEN) {
                    count++;
                }
            }
            String formattedName = "";
            for (VaccineResponse vaccineResponse :
                    responseList) {
                int index = responseList.indexOf(vaccineResponse);
                if (index == responseList.size() - 1) {
                    formattedName = formattedName + vaccineResponse.getName();
                } else {
                    formattedName = formattedName + vaccineResponse.getName() + ", ";
                }
            }

            if (!Util.isNullOrBlank(formattedName))
                tvVaccineName.setText(formattedName);
            else tvVaccineName.setText("");

            if (obj.getDueDate() != null && obj.getDueDate() > 0)
                tvDateOfVaccine.setText(DateTimeUtil.getFormatedDateAndTime(DATE_FORMAT_USED_IN_THIS_SCREEN, obj.getDueDate()));
            else {
                tvDateOfVaccine.setText(DateTimeUtil.getCurrentFormattedDate(DATE_FORMAT_USED_IN_THIS_SCREEN));
            }

            if (obj.getDuration() != null) {
                tvAgeForVaccine.setVisibility(View.VISIBLE);
                tvAgeForVaccine.setText(obj.getDuration());
            } else {
                tvAgeForVaccine.setVisibility(View.GONE);
                tvAgeForVaccine.setText("");
            }

            Drawable background = tvVaccineStatus.getBackground();
            try {
                if (background instanceof ShapeDrawable) {
                    ((ShapeDrawable) background).getPaint().setColor(getColor());
                } else if (background instanceof GradientDrawable) {
                    ((GradientDrawable) background).setColor(getColor());
                } else if (background instanceof ColorDrawable) {
                    ((ColorDrawable) background).setColor(getColor());
                }
            } catch (IllegalArgumentException iae) {
                // This color string is not valid
            }


            if (count > 0)
                tvVaccineCount.setText(count + " of " + obj.getVaccineResponse().size() + " given ");
            else tvVaccineCount.setText("");

            User user = vaccinationListener.getUser();

            for (VaccineResponse vaccineResponse : responseList) {
                int index = responseList.lastIndexOf(vaccineResponse);
                if (count > 0) {
                    layoutUpdatedBy.setVisibility(View.VISIBLE);
                    if (!Util.isNullOrBlank(vaccineResponse.getCreatedBy())) {
                        if (!Util.isNullOrBlank(vaccineResponse.getDoctorId())
                                && vaccineResponse.getDoctorId().equals(user.getUniqueId())) {
                            tvUpdateBy.setText(mActivity.getString(R.string.dr) + vaccineResponse.getCreatedBy());
                            break;
                        } else {
                            tvUpdateBy.setText(mActivity.getString(R.string.patient));
                        }
                    } else {
                        tvUpdateBy.setText(mActivity.getString(R.string.patient));
                    }
                } else {
                    layoutUpdatedBy.setVisibility(View.GONE);
                }
            }
        }
    }

    private boolean getStatusValue() {
        count = 0;
        List<VaccineResponse> responseList = obj.getVaccineResponse();
        for (VaccineResponse vaccineResponse :
                responseList) {
            VaccineStatus status = vaccineResponse.getStatus();
            if (status == VaccineStatus.GIVEN) {
                count++;
            }
        }
        VaccineStatus compare = responseList.get(0).getStatus();
        for (int i = 1; i < responseList.size(); i++) {
            if (responseList.get(i).getStatus().equals(VaccineStatus.GIVEN) != compare.equals(VaccineStatus.GIVEN))
                return false;
        }
        return true;
    }

    private int getColor() {
        int color = mActivity.getResources().getColor(R.color.grey_light_text);
        String formatedDueDate = DateTimeUtil.getFormatedDateAndTime(DATE_FORMAT_USED_IN_THIS_SCREEN, obj.getDueDate());
        String currentFormattedDate = DateTimeUtil.getCurrentFormattedDate(DATE_FORMAT_USED_IN_THIS_SCREEN);
        if (obj.getDueDate() != null) {
            if (!Util.isNullOrBlank(currentFormattedDate)) {
                SimpleDateFormat originalFormat = new SimpleDateFormat(DATE_FORMAT_USED_IN_THIS_SCREEN, Locale.ENGLISH);
                try {
                    java.util.Date toDate = originalFormat.parse(formatedDueDate);
                    java.util.Date fromDate = originalFormat.parse(currentFormattedDate);
                    int comparision = toDate.compareTo(fromDate);
                    if ((toDate.equals(fromDate))) {
                        color = mActivity.getResources().getColor(R.color.yellow);
                        tvVaccineStatus.setText(mActivity.getResources().getString(R.string.due_on));
                    } else if (comparision < 0) {
                        color = mActivity.getResources().getColor(R.color.red_error);
                        tvVaccineStatus.setText(mActivity.getResources().getString(R.string.overdue));
                    } else if (comparision > 0) {
                        color = mActivity.getResources().getColor(R.color.grey_light_text);
                        tvVaccineStatus.setText(mActivity.getResources().getString(R.string.due_on));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
//            if (obj.getDueDate() > DateTimeUtil.getCurrentDateLong()) {
//                color = mActivity.getResources().getColor(R.color.grey_light_text);
//                tvVaccineStatus.setText(mActivity.getResources().getString(R.string.due_on));
//            } else if (obj.getDueDate() < DateTimeUtil.getCurrentDateLong()) {
//                color = mActivity.getResources().getColor(R.color.red_error);
//                tvVaccineStatus.setText(mActivity.getResources().getString(R.string.overdue));
//            } else if (formatedDueDate.equals(currentFormattedDate)) {
//                color = mActivity.getResources().getColor(R.color.yellow);
//                tvVaccineStatus.setText(mActivity.getResources().getString(R.string.due_on));
//            }
            if (count == obj.getVaccineResponse().size()) {
                color = mActivity.getResources().getColor(R.color.green_logo);
                tvVaccineStatus.setText(mActivity.getResources().getString(R.string.given));
            }
        } else if (count == obj.getVaccineResponse().size()) {
            color = mActivity.getResources().getColor(R.color.green_logo);
            tvVaccineStatus.setText(mActivity.getResources().getString(R.string.given));
        }
        return color;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_update_vaccine) {
            mActivity.openCommonOpenUpActivityUsingParcel(CommonOpenUpFragmentType.UPDATE_VACCINE, HealthCocoConstants.REQUEST_CODE_VACCINATION_DATA,
                    new String[]{TAG_VACCINE_DATA}, new Object[]{obj});
        }
    }
}
