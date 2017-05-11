package com.healthcoco.healthcocopad.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.adapter.ClinicImageGridAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.request.ClinicImageToSend;
import com.healthcoco.healthcocopad.bean.server.ClinicImage;
import com.healthcoco.healthcocopad.bean.server.FileDetails;
import com.healthcoco.healthcocopad.bean.server.Location;
import com.healthcoco.healthcocopad.enums.DialogType;
import com.healthcoco.healthcocopad.enums.OptionsType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.AddEditClinicImageListener;
import com.healthcoco.healthcocopad.listeners.CommonOptionsDialogItemClickListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.ImageUtil;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shreshtha on 24-02-2017.
 */
public class AddEditClinicImageFragment extends HealthCocoFragment implements AddEditClinicImageListener, CommonOptionsDialogItemClickListener, Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener {
    public static String DEFAULT_ADD_IMAGE_BUTTON = "100";
    private GridView gvClinicImages;
    private ClinicImageGridAdapter adapter;
    private List<Object> clinicImagesList;
    private Location clinicDetail;
    private Uri cliniImageUri;
    private ClinicImage selectedImageToDelete;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_edit_clinic_image, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void init() {
        Intent intent = mActivity.getIntent();
        String uniqueId = Parcels.unwrap(intent.getParcelableExtra(HealthCocoConstants.TAG_UNIQUE_ID));
        if (!Util.isNullOrBlank(uniqueId))
            clinicDetail = LocalDataServiceImpl.getInstance(mApp).getLocation(uniqueId);
        initViews();
        initListeners();
        initAdapter();
        initData();
    }

    @Override
    public void initViews() {
        gvClinicImages = (GridView) view.findViewById(R.id.gv_clinic_image);
    }

    @Override
    public void initListeners() {

    }

    private void initAdapter() {
        adapter = new ClinicImageGridAdapter(mActivity, this);
        gvClinicImages.setAdapter(adapter);
    }

    private void initData() {
        if (clinicDetail != null && !Util.isNullOrEmptyList(clinicDetail.getImages()))
            clinicImagesList = (ArrayList<Object>) (ArrayList<?>) clinicDetail.getImages();
        notifyAdapter(clinicImagesList);
    }

    /**
     * notify {@link #gvClinicImages}
     * also removing {@link #DEFAULT_ADD_IMAGE_BUTTON} and then adding again inorder to position it at end
     *
     * @param list : list that is to be populated. Show only {@link #DEFAULT_ADD_IMAGE_BUTTON} if list is null or empty
     */
    private void notifyAdapter(List<Object> list) {
        if (Util.isNullOrEmptyList(list))
            list = new ArrayList<>();

        if (list.contains(DEFAULT_ADD_IMAGE_BUTTON)) {
            list.remove(DEFAULT_ADD_IMAGE_BUTTON);
        }
        list.add(DEFAULT_ADD_IMAGE_BUTTON);
        gvClinicImages.setVisibility(View.VISIBLE);
        adapter.setListData(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAddClinicImageClicked() {
        Util.checkNetworkStatus(mActivity);
        if (HealthCocoConstants.isNetworkOnline)
            openDialogFragment(DialogType.SELECT_IMAGE, this);
        else onNetworkUnavailable(null);
    }

    @Override
    public void onClinicImageClicked(ClinicImage clinicImage) {

    }

    @Override
    public void onDeleteImageClicked(ClinicImage clinicImage) {
        Util.checkNetworkStatus(mActivity);
        if (HealthCocoConstants.isNetworkOnline)
            showConfirmationAlert(clinicImage);
        else
            onNetworkUnavailable(null);
    }

    private void showConfirmationAlert(final ClinicImage clinicImage) {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mActivity);
        alertBuilder.setTitle(R.string.confirm_delete_this_image);
        alertBuilder.setMessage(getResources().getString(
                R.string.this_cannot_be_undone));
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteImageFromServer(clinicImage);
            }
        });
        alertBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertBuilder.create();
        alertBuilder.show();
    }

    private void deleteImageFromServer(ClinicImage clinicImage) {
        try {
            mActivity.showLoading(false);
            selectedImageToDelete = clinicImage;
            WebDataServiceImpl.getInstance(mApp).deleteCLinicImage(ClinicImage.class, clinicImage.getCounter(), clinicDetail.getUniqueId(), this, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onOptionsItemSelected(Object object) {
        OptionsType optionsType = (OptionsType) object;
        if (optionsType != null) {
            LogUtils.LOGD(TAG, getResources().getString(optionsType.getStringId()));
            switch (optionsType) {
                case CAMERA:
                    cliniImageUri = mActivity.openCamera(this, "clinicImage");
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
                if (requestCode == HealthCocoConstants.REQUEST_CODE_CAMERA && cliniImageUri != null) {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(mActivity.getContentResolver(), cliniImageUri);
                    if (bitmap != null) {
                        addClinicImage(cliniImageUri.getPath(), bitmap);
                    }
                } else if (requestCode == HealthCocoConstants.REQUEST_CODE_GALLERY && data.getData() != null) {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(mActivity.getContentResolver(), data.getData());
                    if (bitmap != null) {
                        addClinicImage(data.getData().getPath(), bitmap);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void addClinicImage(String filePath, Bitmap originalBitmap) {
        originalBitmap = ImageUtil.getRotatedBitmapIfRequiredFromPath(filePath, originalBitmap);
        mActivity.showLoading(false);
        ClinicImageToSend imageToSend = new ClinicImageToSend();
        imageToSend.setId(clinicDetail.getUniqueId());
        imageToSend.setImages(getImages(originalBitmap));
        WebDataServiceImpl.getInstance(mApp).addClinicImages(ClinicImage.class, imageToSend, this, this);
    }

    private ArrayList<FileDetails> getImages(Bitmap bitmap) {
        ArrayList<FileDetails> fileDetailsList = new ArrayList<>();
        fileDetailsList.add(getFileDetails(bitmap));
        return fileDetailsList;
    }

    private FileDetails getFileDetails(Bitmap bitmap) {
        FileDetails fileDetails = new FileDetails();
        fileDetails.setFileEncoded(getEncodeByteArray(bitmap));
        fileDetails.setFileExtension(ImageUtil.DEFAULT_IMAGE_EXTENSION);
        fileDetails.setFileName(ImageUtil.DEFAULT_CLINIC_IMAGE_NAME);
        fileDetails.setBitmap(bitmap);
        return fileDetails;
    }

    private String getEncodeByteArray(Bitmap bitmap) {
        return ImageUtil.encodeTobase64(bitmap);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cliniImageUri != null)
            ImageUtil.deleteFileFrom(cliniImageUri);
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
                case ADD_CLINIC_IMAGE:
                    Util.showToast(mActivity, R.string.clinic_image_uploaded_successfully);
                    if (!Util.isNullOrEmptyList(response.getDataList()) && response.getDataList().get(0) instanceof ClinicImage) {
                        clinicImagesList = response.getDataList();
                        notifyAdapter(clinicImagesList);
                    }
                    break;
                case DELETE_CLINIC_IMAGE:
                    if (selectedImageToDelete != null) {
                        try {
                            int position = clinicImagesList.indexOf(selectedImageToDelete);
                            clinicImagesList.remove(position);
                            notifyAdapter(clinicImagesList);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
        mActivity.hideLoading();
    }
}
