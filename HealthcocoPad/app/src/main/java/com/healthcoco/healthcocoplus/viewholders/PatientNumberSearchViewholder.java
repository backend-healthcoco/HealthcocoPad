package com.healthcoco.healthcocoplus.viewholders;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocoplus.HealthCocoActivity;
import com.healthcoco.healthcocoplus.HealthCocoViewHolder;
import com.healthcoco.healthcocoplus.bean.server.AlreadyRegisteredPatientsResponse;
import com.healthcoco.healthcocoplus.enums.HealthCocoFileType;
import com.healthcoco.healthcocoplus.enums.PatientProfileScreenType;
import com.healthcoco.healthcocoplus.listeners.DownloadFileFromUrlListener;
import com.healthcoco.healthcocoplus.utilities.DownloadImageFromUrlUtil;
import com.healthcoco.healthcocoplus.utilities.ImageUtil;
import com.healthcoco.healthcocoplus.utilities.LogUtils;
import com.healthcoco.healthcocoplus.utilities.Util;
import com.healthcoco.healthcocoplus.views.TextViewFontAwesome;


/**
 * Created by neha on 13/01/16.
 */
public class PatientNumberSearchViewholder extends HealthCocoViewHolder implements DownloadFileFromUrlListener {

    private static final String TAG = PatientNumberSearchViewholder.class.getSimpleName();
    private HealthCocoActivity mActivity;
    private TextView tvName;
    private AlreadyRegisteredPatientsResponse objData;
    private TextView tvInitialAlphabet;
    private ProgressBar progressLoading;
    private ImageView ivContactProfile;
    private TextViewFontAwesome ivIcon;
    private TextView tvPatientDetail;

    public PatientNumberSearchViewholder(HealthCocoActivity mActivity) {
        super(mActivity);
        this.mActivity = mActivity;
    }

    @Override
    public void setData(Object object) {
        objData = (AlreadyRegisteredPatientsResponse) object;
    }

    @Override
    public void applyData() {
        if (objData.getIsPartOfClinic()) {
            tvName.setText(Util.getValidatedValue(objData.getLocalPatientName()));
            ivIcon.setText(R.string.fa_hospital);
        } else {
            tvName.setText(Util.getValidatedValue(objData.getFirstName()));
            ivIcon.setText(R.string.fa_globe);
        }
        tvPatientDetail.setText(objData.getGender());
        if (!Util.isNullOrBlank(objData.getImageUrl()))
            objData.setImageFilePath(ImageUtil.getPathToSaveFile(HealthCocoFileType.PATIENT_PROFILE, Util.getFileNameFromUrl(objData.getImageUrl()), Util.getFileExtension(objData.getImageUrl())));
        initProfileImage();
    }

    private void initProfileImage() {
        try {
            DownloadImageFromUrlUtil.loadImageWithInitialAlphabet(mActivity, PatientProfileScreenType.IN_PATIENTS_LIST, objData, progressLoading, ivContactProfile, tvInitialAlphabet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View getContentView() {
        View convertView = inflater.inflate(R.layout.list_item_already_added_patient, null);
        tvName = (TextView) convertView.findViewById(R.id.tv_name);
        tvPatientDetail = (TextView) convertView.findViewById(R.id.tv_patient_detail);
        tvInitialAlphabet = (TextView) convertView.findViewById(R.id.tv_initial_aplhabet);
        progressLoading = (ProgressBar) convertView.findViewById(R.id.progress_loading);
        ivIcon = (TextViewFontAwesome) convertView.findViewById(R.id.iv_icon);
        ivContactProfile = (ImageView) convertView.findViewById(R.id.iv_image);
        return convertView;
    }

    @Override
    public void onPostExecute(String filePath) {
        if (!Util.isNullOrBlank(filePath) && !Util.isNullOrBlank(objData.getImageFilePath()) && objData.getImageFilePath().equalsIgnoreCase(filePath)) {
            int width = ivContactProfile.getLayoutParams().width;
            LogUtils.LOGD(TAG, "Image Size " + width);
            Bitmap bitmap = ImageUtil.getDecodedBitmapFromPath(filePath, width, width);
            if (bitmap != null) {
                ivContactProfile.setImageBitmap(bitmap);
                ivContactProfile.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onPreExecute() {

    }
}
