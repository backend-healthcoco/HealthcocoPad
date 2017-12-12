package com.healthcoco.healthcocopad.dialogFragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.FileDetails;
import com.healthcoco.healthcocopad.bean.RecordMultipartRequest;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.request.RecordsAddRequestMultipart;
import com.healthcoco.healthcocopad.bean.request.ReportDetailsToSend;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.Records;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.enums.DialogType;
import com.healthcoco.healthcocopad.enums.OptionsType;
import com.healthcoco.healthcocopad.enums.RecordState;
import com.healthcoco.healthcocopad.enums.ReportFileType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.fragments.PatientReportsDetailFragment;
import com.healthcoco.healthcocopad.listeners.CommonOptionsDialogItemClickListener;
import com.healthcoco.healthcocopad.multipart.MultipartUploadRequestAsynTask;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.EditTextTextViewErrorUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.ImageUtil;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Shreshtha on 19-06-2017.
 */

public class UploadReportDialogFragment extends HealthCocoDialogFragment implements View.OnClickListener,
        CommonOptionsDialogItemClickListener, GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean> {
    public static final String TAG_REPORT_ID = "reportId";
    private static final String[] ACCEPT_MIME_TYPES = {
            "image/*", "application/pdf", "text/*",
            "application/rtf", "application/msword", "application/vnd.ms-powerpoint"
    };
    private TextView tvSelectImage;
    private EditText editTitle;
    private EditText editDescription;
    private ImageView ivReport;
    private Uri imageUri;
    private ReportDetailsToSend reportDetailsToSend;
    private User user;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private Bitmap originalBitmap;
    private TextView tvSelectFile;
    private TextView tvFileName;
    private LinearLayout containerFileDetails;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_upload_report, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        setWidthHeight(0.60, 0.80);
    }

    @Override
    public void init() {
        LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
        if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
            user = doctor.getUser();
            selectedPatient = LocalDataServiceImpl.getInstance(mApp).getPatient(HealthCocoConstants.SELECTED_PATIENTS_USER_ID);
            if (selectedPatient != null && !Util.isNullOrBlank(selectedPatient.getUserId())) {
                initViews();
                initListeners();
                initData();
            }
        }
    }

    @Override
    public void initViews() {
        tvSelectImage = (TextView) view.findViewById(R.id.tv_select_image);
        tvSelectFile = (TextView) view.findViewById(R.id.tv_select_file);
        editTitle = (EditText) view.findViewById(R.id.edit_title);
        editDescription = (EditText) view.findViewById(R.id.edit_description);
        ivReport = (ImageView) view.findViewById(R.id.iv_report);
        tvFileName = (TextView) view.findViewById(R.id.tv_file_name);
        containerFileDetails = (LinearLayout) view.findViewById(R.id.container_file_details);
        containerFileDetails.setVisibility(View.GONE);
        ivReport.setVisibility(View.GONE);
    }

    @Override
    public void initListeners() {
        tvSelectImage.setOnClickListener(this);
        tvSelectFile.setOnClickListener(this);
        initSaveCancelButton(this);
        initActionbarTitle(getResources().getString(R.string.upload_record));
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_select_image:
                openDialogFragment(DialogType.SELECT_IMAGE, this);
                break;
            case R.id.tv_select_file:
                try {
                    String action = "";
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
                        action = Intent.ACTION_GET_CONTENT;
                    else
                        action = Intent.ACTION_OPEN_DOCUMENT;
                    Intent intent = new Intent(action);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("*/*");
                    intent.putExtra(Intent.EXTRA_MIME_TYPES, ACCEPT_MIME_TYPES);
                    startActivityForResult(intent, HealthCocoConstants.REQUEST_CODE_FILE);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.bt_save:
                Util.checkNetworkStatus(mActivity);
                validateData();
                break;
        }
    }

    private void clearPreviousAlerts() {
        editTitle.setActivated(false);
    }

    private void validateData() {
        clearPreviousAlerts();
        ArrayList<View> errorViewList = new ArrayList<>();
        String msg = null;
        String imageTitle = Util.getValidatedValueOrNull(editTitle);
        if (Util.isNullOrBlank(imageTitle)) {
            msg = getResources().getString(R.string.please_enter_image_label);
            errorViewList.add(editTitle);
        } else if (reportDetailsToSend == null) {
            msg = getResources().getString(R.string.please_select_image_or_file_to_upload);
        }
        if (Util.isNullOrBlank(msg)) {
            addRecord();
        } else {
            EditTextTextViewErrorUtil.showErrorOnEditText(mActivity, view, errorViewList, msg);
        }
    }

    private void addRecord() {
     /*   mActivity.showLoading(false);
        Records records = new Records();
        records.setExplanation(Util.getValidatedValue(String.valueOf(editDescription.getText())));
        records.setDoctorId(user.getUniqueId());
        records.setHospitalId(user.getForeignHospitalId());
        records.setLocationId(user.getForeignLocationId());
        records.setPatientId(selectedPatient.getUserId());
        records.setRecordsState(RecordState.APPROVAL_NOT_REQUIRED);
        records.setRecordsType(String.valueOf(reportDetailsToSend.getReportFileType()));
        records.setFileDetails(getFileDetails());
    WebDataServiceImpl.getInstance(mApp).addRecord(Records.class, records, this, this);

*/
        mActivity.showLoading(false);
        RecordsAddRequestMultipart requestMultipart = new RecordsAddRequestMultipart();
        requestMultipart.setExplanation(Util.getValidatedValue(String.valueOf(editDescription.getText())));
        requestMultipart.setDoctorId(user.getUniqueId());
        requestMultipart.setHospitalId(user.getForeignHospitalId());
        requestMultipart.setLocationId(user.getForeignLocationId());
        requestMultipart.setPatientId(selectedPatient.getUserId());
//        requestMultipart.setRecordsState(RecordState.APPROVAL_NOT_REQUIRED);
        requestMultipart.setRecordsType(String.valueOf(reportDetailsToSend.getReportFileType()));

        new MultipartUploadRequestAsynTask(mActivity, Records.class, WebServiceType.ADD_RECORD_MULTIPART, requestMultipart, reportDetailsToSend.getRecordsPath(), this, this).execute();

    }


    @Override
    public void onOptionsItemSelected(Object object) {
        OptionsType optionsType = (OptionsType) object;
        if (optionsType != null) {
            LogUtils.LOGD(TAG, getResources().getString(optionsType.getStringId()));
            switch (optionsType) {
                case CAMERA:
                    imageUri = mActivity.openCamera(this, "reportImage");
                    break;
                case GALLERY:
                    mActivity.openGallery(this);
                    break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            try {
                if (requestCode == HealthCocoConstants.REQUEST_CODE_CAMERA && imageUri != null) {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(mActivity.getContentResolver(), imageUri);
                    if (bitmap != null) {
                        showImage(imageUri.getPath(), imageUri);
                    }
                } else if (requestCode == HealthCocoConstants.REQUEST_CODE_GALLERY && data.getData() != null) {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(mActivity.getContentResolver(), data.getData());
                    if (bitmap != null) {
                        showImage(ImageUtil.getRealPathFromURI(mActivity, data.getData()), data.getData());
                    }
                } else if (requestCode == HealthCocoConstants.REQUEST_CODE_FILE && data.getData() != null) {
                    showImage(ImageUtil.getRealPathFromURI(mActivity, data.getData()), data.getData());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showImage(String filePath, Uri imageUri) {
        reportDetailsToSend = new ReportDetailsToSend();
        reportDetailsToSend.setReportUri(imageUri);
        reportDetailsToSend.setFileExtension(ImageUtil.DEFAULT_IMAGE_EXTENSION);
        reportDetailsToSend.setRecordsPath(filePath);
        try {
            originalBitmap = ImageUtil.getBitmapFromUri(mActivity, imageUri);
            if (originalBitmap != null) {
                ivReport.setImageBitmap(originalBitmap);
            }
            reportDetailsToSend.setReportFileType(ReportFileType.IMAGE);
            showFileNamePath(null, false);
        } catch (Exception e) {
            e.printStackTrace();
            showFileNamePath(imageUri.getPath(), true);
            reportDetailsToSend.setReportFileType(ReportFileType.OTHER_TYPE);
        }
    }

    /**
     * Displays the fileType based on isShowFileNamePath
     *
     * @param extension          : extension of file
     * @param isShowFileNamePath : if true  then prints file extension in TextView and hides ImageView.
     *                           if false,shows ImageView and hides TextView.
     */
    private void showFileNamePath(String extension, boolean isShowFileNamePath) {
        if (isShowFileNamePath) {
            tvFileName.setText(extension + " " + getResources().getString(R.string.file));
            containerFileDetails.setVisibility(View.VISIBLE);
            ivReport.setVisibility(View.GONE);
        } else {
            containerFileDetails.setVisibility(View.GONE);
            ivReport.setVisibility(View.VISIBLE);
        }
    }

    private FileDetails getFileDetails() {
        FileDetails fileDetails = new FileDetails();
        fileDetails.setFileEncoded(getEncodeButeArray());
        fileDetails.setFileExtension(reportDetailsToSend.getFileExtension());
        fileDetails.setFileName(Util.getValidatedValue(String.valueOf(editTitle.getText())));
        return fileDetails;
    }

    private String getEncodeButeArray() {
        if (reportDetailsToSend.getReportFileType() == ReportFileType.IMAGE)
            return ImageUtil.encodeTobase64(originalBitmap);
        else
            return Util.getByteArrayFromUri(mActivity, reportDetailsToSend.getReportUri());
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        LogUtils.LOGD(TAG, "ADD_RECORD Fail");
        mActivity.hideLoading();
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
        mActivity.hideLoading();
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        LogUtils.LOGD(TAG, "ADD_RECORD Success");
        switch (response.getWebServiceType()) {
            case ADD_RECORD_MULTIPART:
                if (response.isValidData(response)) {
                    Records record = (Records) response.getData();
                    LocalDataServiceImpl.getInstance(mApp).addRecord(record);
                    sendBroadcast(record.getUniqueId());
                    getDialog().dismiss();
                }
                break;
        }
        mActivity.hideLoading();
    }

    private void sendBroadcast(String recordId) {
        if (!Util.isNullOrBlank(recordId)) {
            try {
                Intent intent = new Intent(PatientReportsDetailFragment.INTENT_GET_REPORTS_LIST_USING_ID);
                intent.putExtra(TAG_REPORT_ID, recordId);
                LocalBroadcastManager.getInstance(mApp.getApplicationContext()).sendBroadcast(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
