package com.healthcoco.healthcocopad.dialogFragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.request.AddVideoRequestMultipart;
import com.healthcoco.healthcocopad.bean.request.ReportDetailsToSend;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.PatientEducationVideo;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.enums.DialogType;
import com.healthcoco.healthcocopad.enums.HealthCocoFileType;
import com.healthcoco.healthcocopad.enums.OptionsType;
import com.healthcoco.healthcocopad.enums.ReportFileType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.fragments.PatientReportsDetailFragment;
import com.healthcoco.healthcocopad.listeners.CommonOptionsDialogItemClickListener;
import com.healthcoco.healthcocopad.multipart.MultipartUploadRequestAsynTask;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.EditTextTextViewErrorUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.ImageUtil;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import static com.healthcoco.healthcocopad.dialogFragment.SelectCategoryDialogFragment.TAG_SELECTED_CATEGORY;
import static com.healthcoco.healthcocopad.utilities.FileChooser.getPath;

/**
 * Created by Prashant on 02-07-2018.
 */

public class UploadVideoDialogFragment extends HealthCocoDialogFragment implements View.OnClickListener,
        CommonOptionsDialogItemClickListener, GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean> {
    public static final String TAG_REPORT_ID = "reportId";
    private static final String[] ACCEPT_MIME_TYPES = {
            "image/*", "application/pdf", "text/*",
            "application/rtf", "application/msword", "application/vnd.ms-powerpoint"
    };
    String selectedVideoPath;
    String pathToSaveAndGet;
    private EditText editTitle;
    private EditText editDescription;
    private TextView tvCategory;
    private CheckBox cbLocal;
    private CheckBox cbServer;
    private Uri imageUri;
    private ReportDetailsToSend reportDetailsToSend;
    private User user;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private Bitmap originalBitmap;
    private TextView tvSelectFile;
    private TextView tvFileName;
    private LinearLayout containerFileDetails;
    private ArrayList<String> categoryList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_upload_video, container, false);
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
            initViews();
            initListeners();
            initData();
        }
    }

    @Override
    public void initViews() {
        tvSelectFile = (TextView) view.findViewById(R.id.tv_select_file);
        editTitle = (EditText) view.findViewById(R.id.edit_title);
        editDescription = (EditText) view.findViewById(R.id.edit_description);
        tvFileName = (TextView) view.findViewById(R.id.tv_file_name);
        tvCategory = (TextView) view.findViewById(R.id.tv_category);
        cbLocal = (CheckBox) view.findViewById(R.id.cb_local);
        cbServer = (CheckBox) view.findViewById(R.id.cb_server);
        containerFileDetails = (LinearLayout) view.findViewById(R.id.container_file_details);

        containerFileDetails.setVisibility(View.GONE);
        cbLocal.setSelected(true);
        cbLocal.setClickable(false);
    }

    @Override
    public void initListeners() {
        tvSelectFile.setOnClickListener(this);
        initSaveCancelButton(this);
        initActionbarTitle(getResources().getString(R.string.upload_video));
        tvCategory.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_select_file:
                openDialogFragment(DialogType.SELECT_IMAGE, this);
                break;
            case R.id.bt_save:
                Util.checkNetworkStatus(mActivity);
                validateData();
                break;
            case R.id.tv_category:
                openSelectCategoryFragment();
                break;
        }
    }

    private void openSelectCategoryFragment() {

        mActivity.openCategoryFragment(TAG_SELECTED_CATEGORY, categoryList, this, HealthCocoConstants.REQUEST_CODE_CATEGORY);
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
//        } else if (reportDetailsToSend == null) {
//            msg = getResources().getString(R.string.please_select_image_or_file_to_upload);
        }
        if (Util.isNullOrBlank(msg)) {
            addRecord();
        } else {
            EditTextTextViewErrorUtil.showErrorOnEditText(mActivity, view, errorViewList, msg);
        }
    }

    private void addRecord() {
        mActivity.showLoading(false);

        PatientEducationVideo patientEducationVideo = new PatientEducationVideo();
//        patientEducationVideo.setCreatedBy(user.getFirstName());
        patientEducationVideo.setName(Util.getValidatedValueOrNull(editTitle));
        patientEducationVideo.setUniqueId(String.valueOf(DateTimeUtil.getCalendarInstance().getTimeInMillis()));
        patientEducationVideo.setDiscarded(false);
        patientEducationVideo.setDescription(Util.getValidatedValue(String.valueOf(editDescription.getText())));
        patientEducationVideo.setDoctorId(user.getUniqueId());
        patientEducationVideo.setHospitalId(user.getForeignHospitalId());
        patientEducationVideo.setLocationId(user.getForeignLocationId());
        if (Util.isNullOrEmptyList(categoryList))
            patientEducationVideo.setTags(categoryList);

        boolean isSaved = saveVideoToFolder();

        if (cbServer.isChecked()) {
            AddVideoRequestMultipart addVideoRequestMultipart = new AddVideoRequestMultipart();
            addVideoRequestMultipart.setDescription(Util.getValidatedValue(String.valueOf(editDescription.getText())));
            addVideoRequestMultipart.setName(Util.getValidatedValue(String.valueOf(editTitle.getText())));
            addVideoRequestMultipart.setDoctorId(user.getUniqueId());
            addVideoRequestMultipart.setHospitalId(user.getForeignHospitalId());
            addVideoRequestMultipart.setLocationId(user.getForeignLocationId());

            new MultipartUploadRequestAsynTask(mActivity, PatientEducationVideo.class, WebServiceType.ADD_VIDEO, addVideoRequestMultipart, reportDetailsToSend.getRecordsPath(), this, this).execute();
        } else {
            if (isSaved) {
                patientEducationVideo.setVideoUrl(pathToSaveAndGet + "." + Util.getFileExtension(selectedVideoPath));
                LocalDataServiceImpl.getInstance(mApp).addEducationVideo(patientEducationVideo, user.getUniqueId());
                mActivity.hideLoading();
                getDialog().dismiss();
            }
        }
    }


    @Override
    public void onOptionsItemSelected(Object object) {
        OptionsType optionsType = (OptionsType) object;
        if (optionsType != null) {
            LogUtils.LOGD(TAG, getResources().getString(optionsType.getStringId()));
            switch (optionsType) {
                case CAMERA:
//                    imageUri = mActivity.openCamera(this, "reportImage");
                    break;
                case GALLERY:
                    mActivity.selectVideoFromGallery(this);
                    break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            try {
                if (requestCode == HealthCocoConstants.REQUEST_CODE_VIDEO_GALLERY) {
                    if (data.getData() != null) {
                        selectedVideoPath = getPath(mActivity, data.getData());
                        showImage(selectedVideoPath, data.getData());
                    } else {
                        Util.showToast(mActivity, R.string.failed_to_select_videos);
                    }
                } else if (requestCode == HealthCocoConstants.REQUEST_CODE_CATEGORY) {
                    categoryList = Parcels.unwrap(data.getParcelableExtra(TAG_SELECTED_CATEGORY));
                    if (!Util.isNullOrEmptyList(categoryList)) {
                        setSelectedCategory(categoryList);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setSelectedCategory(ArrayList<String> categoryList) {
        String categoty = "";
        for (String s : categoryList) {
            categoty = categoty + s + ", ";
        }
        categoty = categoty.substring(0, categoty.length() - 2);
        tvCategory.setText(categoty);
    }

    private void showImage(String filePath, Uri imageUri) {
        reportDetailsToSend = new ReportDetailsToSend();
        reportDetailsToSend.setReportUri(imageUri);
        reportDetailsToSend.setFileExtension(ImageUtil.DEFAULT_IMAGE_EXTENSION);
        reportDetailsToSend.setRecordsPath(filePath);
        showFileNamePath(filePath, true);
        reportDetailsToSend.setReportFileType(ReportFileType.OTHER_TYPE);

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
        } else {
            containerFileDetails.setVisibility(View.GONE);
        }
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
        LogUtils.LOGD(TAG, "ADD_VIDEO Success");
        switch (response.getWebServiceType()) {
            case ADD_VIDEO:
                if (response.isValidData(response)) {
                    PatientEducationVideo patientEducationVideo = (PatientEducationVideo) response.getData();
//                    LocalDataServiceImpl.getInstance(mApp).addRecord(record);
//                    sendBroadcast(record.getUniqueId());
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

    private boolean saveVideoToFolder() {

        pathToSaveAndGet = ImageUtil.getPathToSaveFile(HealthCocoFileType.DOCTOR_VIDEO, Util.getValidatedValueOrNull(editTitle), Util.getFileExtension(selectedVideoPath));

        // the file to be moved or copied
        File sourceLocation = new File(selectedVideoPath);

        // make sure your target location folder exists!
        File targetLocation = new File(pathToSaveAndGet + "." + Util.getFileExtension(selectedVideoPath));

        // just to take note of the location sources
        LogUtils.LOGD(TAG, "sourceLocation: " + sourceLocation);
        LogUtils.LOGD(TAG, "targetLocation: " + targetLocation);

        try {

            // 1 = move the file, 2 = copy the file
            int actionChoice = 2;

            // moving the file to another directory
            if (actionChoice == 1) {
                if (sourceLocation.renameTo(targetLocation)) {
                    Log.v(TAG, "Move file successful.");
                } else {
                    Log.v(TAG, "Move file failed.");
                }
            }
            // we will copy the file
            else {

                // make sure the target file exists
                if (sourceLocation.exists()) {

                    InputStream in = new FileInputStream(sourceLocation);
                    OutputStream out = new FileOutputStream(targetLocation);

                    // Copy the bits from instream to outstream
                    byte[] buf = new byte[1024];
                    int len;

                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }

                    in.close();
                    out.close();

                    LogUtils.LOGD(TAG, "Copy file successful.");

                    return true;
                } else {
                    LogUtils.LOGD(TAG, "Copy file failed. Source file missing.");
                    return false;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }
}
