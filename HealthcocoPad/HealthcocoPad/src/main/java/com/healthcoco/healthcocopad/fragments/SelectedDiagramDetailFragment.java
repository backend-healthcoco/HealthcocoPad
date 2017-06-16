package com.healthcoco.healthcocopad.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.AddVisitsActivity;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.Diagram;
import com.healthcoco.healthcocopad.bean.FileDetails;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.DownloadFileFromUrlAsyncTask;
import com.healthcoco.healthcocopad.custom.SignatureView;
import com.healthcoco.healthcocopad.enums.ColorType;
import com.healthcoco.healthcocopad.enums.HealthCocoFileType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.DiagramCanvasListener;
import com.healthcoco.healthcocopad.listeners.DownloadFileFromUrlListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.ImageUtil;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.DiagramColorPalleteLayout;

import org.parceler.Parcels;

public class SelectedDiagramDetailFragment extends HealthCocoFragment implements OnClickListener,
        DownloadFileFromUrlListener, DiagramCanvasListener, Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener {
    public static final String SELECTED_DIAGRAM_TAG = "selectedDiagramTag";
    private SignatureView ivSelectedImage;
    private ProgressBar progressLoading;
    private ImageButton btColor;
    private LinearLayout containerColorPallete;
    private ImageButton btErase;
    private Diagram selectedDiagram;
    private User user;
    private boolean toggleStateFromPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_selected_diagram_detail, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ImageUtil.DIAGRAM_SELECTED_BYTE_ARRAY = null;
        init();
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
        ivSelectedImage = (SignatureView) view.findViewById(R.id.iv_selected_image);
        progressLoading = (ProgressBar) view.findViewById(R.id.progress_loading);
        btColor = (ImageButton) view.findViewById(R.id.bt_color);
        btErase = (ImageButton) view.findViewById(R.id.bt_erase);

        //initialising color pallete
        containerColorPallete = (LinearLayout) view.findViewById(R.id.container_color_pallete);
        DiagramColorPalleteLayout colorPalleteLayout = new DiagramColorPalleteLayout(mActivity);
        colorPalleteLayout.initData(this);
        containerColorPallete.addView(colorPalleteLayout);
    }

    @Override
    public void initListeners() {
        toggleStateFromPreferences = Util.getVisitToggleStateFromPreferences(mActivity);
        if (toggleStateFromPreferences)
            ((AddVisitsActivity) mActivity).initSaveButton(this);
        else ((CommonOpenUpActivity) mActivity).initActionbarRightAction(this);
        btColor.setOnClickListener(this);
        btErase.setOnClickListener(this);
    }

    private void initData() {
        Intent intent = mActivity.getIntent();
        selectedDiagram = Parcels.unwrap(intent.getParcelableExtra(HealthCocoConstants.TAG_SELECTED_DIAGRAM));
        if (selectedDiagram != null && !Util.isNullOrBlank(selectedDiagram.getDiagramUrl())) {
            mActivity.showLoading(false);
            String url = selectedDiagram.getDiagramUrl();
            if (toggleStateFromPreferences)
                ((AddVisitsActivity) mActivity).initActionbarTitle(selectedDiagram.getTags());
            else ((CommonOpenUpActivity) mActivity).initActionbarTitle(selectedDiagram.getTags());
            new DownloadFileFromUrlAsyncTask(mActivity, this, HealthCocoFileType.DIAGRAM_IMAGE, Util.getFileNameFromUrl(url), null, null).execute(url);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.container_right_action:
                byte[] signatureArray = ivSelectedImage.captureSignature();
                try {
                    if (signatureArray != null) {
                        addDiagram(signatureArray);
                        ImageUtil.DIAGRAM_SELECTED_BYTE_ARRAY = signatureArray;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.bt_color:
                if (containerColorPallete.getVisibility() == View.GONE)
                    containerColorPallete.setVisibility(View.VISIBLE);
                else
                    containerColorPallete.setVisibility(View.GONE);
                break;
            case R.id.bt_erase:
                onColorselected(ColorType.TRANSPARENT);
                break;
            default:
                break;
        }
    }

    private void addDiagram(byte[] diagramSelectedByteArray) {
        mActivity.showLoading(false);
        Diagram diagram = new Diagram();
        diagram.setDoctorId(user.getUniqueId());
        diagram.setHospitalId(user.getForeignHospitalId());
        diagram.setLocationId(user.getForeignLocationId());
        diagram.setFileExtension(ImageUtil.DEFAULT_IMAGE_EXTENSION);
        diagram.setDiagram(getFileDetails(diagramSelectedByteArray));
        WebDataServiceImpl.getInstance(mApp).addDiagram(Diagram.class, diagram, this, this);
    }

    private FileDetails getFileDetails(byte[] diagramSelectedByteArray) {
        FileDetails fileDetails = new FileDetails();
        fileDetails.setFileEncoded(Util.getStringFromByteArray(diagramSelectedByteArray));
        fileDetails.setFileExtension(ImageUtil.DEFAULT_IMAGE_EXTENSION);
        fileDetails.setFileName(Util.getValidatedValue(ImageUtil.DEFAULT_IMAGE_NAME));
        return fileDetails;
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        mActivity.hideLoading();
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
        mActivity.hideLoading();
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response != null && response.getWebServiceType() != null) {
            switch (response.getWebServiceType()) {
                case ADD_DIAGRAM:
                    if (response.getData() != null && response.getData() instanceof Diagram) {
                        Diagram diagramResponse = (Diagram) response.getData();
                        LocalDataServiceImpl.getInstance(mApp).addDiagram(diagramResponse);
                        Intent intent = new Intent();
                        intent.putExtra(SELECTED_DIAGRAM_TAG, selectedDiagram.getTags());
                        intent.putExtra(HealthCocoConstants.TAG_UNIQUE_ID, diagramResponse.getUniqueId());
                        if (toggleStateFromPreferences) {
                            ((AddVisitsActivity) mActivity).setResult(HealthCocoConstants.RESULT_CODE_DIAGRAM_DETAIL, intent);
                            ((AddVisitsActivity) mActivity).finish();
                        } else {
                            ((CommonOpenUpActivity) mActivity).setResult(HealthCocoConstants.RESULT_CODE_DIAGRAM_DETAIL, intent);
                            ((CommonOpenUpActivity) mActivity).finish();
                        }
                    }
                    break;
            }
        }
        mActivity.hideLoading();
    }

    @Override
    public void onPostExecute(String filePath) {
        if (!Util.isNullOrBlank(filePath)) {
            int width = ivSelectedImage.getLayoutParams().width;
            LogUtils.LOGD(TAG, "Image SIze " + width);
            Bitmap bitmap = ImageUtil.getDecodedBitmapFromPath(filePath, width, width);
            if (bitmap != null) {
                ivSelectedImage.setBitmapToImageView(bitmap);
                ivSelectedImage.setVisibility(View.VISIBLE);
            }
        }
        mActivity.hideLoading();
    }

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onColorselected(ColorType colorType) {
        ivSelectedImage.setSelectedColor(colorType.getColorId());
    }
}
