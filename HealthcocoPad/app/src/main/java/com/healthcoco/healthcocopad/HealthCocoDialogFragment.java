package com.healthcoco.healthcocopad;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.healthcoco.healthcocopad.bean.server.Specialities;
import com.healthcoco.healthcocopad.dialogFragment.CommonListDialogFragment;
import com.healthcoco.healthcocopad.dialogFragment.CommonListSolarDialogFragment;
import com.healthcoco.healthcocopad.dialogFragment.CommonOptionsDialogListFragment;
import com.healthcoco.healthcocopad.enums.CommonListDialogType;
import com.healthcoco.healthcocopad.enums.DialogType;
import com.healthcoco.healthcocopad.listeners.CommonListDialogItemClickListener;
import com.healthcoco.healthcocopad.listeners.CommonOptionsDialogItemClickListener;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.ScreenDimensions;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.List;

/**
 * Created by Shreshtha on 23-01-2017.
 */

public abstract class HealthCocoDialogFragment extends DialogFragment implements CommonListDialogItemClickListener {
    protected String TAG;
    protected View view;
    protected HealthCocoActivity mActivity;
    protected FragmentManager mFragmentManager;
    protected HealthCocoApplication mApp;
    protected boolean isBottomOptionsDialog;
    private CommonListDialogFragment commonListDialogFragment;
    private Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(false);
        TAG = getClass().getSimpleName();
        mActivity = (HealthCocoActivity) getActivity();
        mApp = (HealthCocoApplication) mActivity.getApplication();
        mFragmentManager = getFragmentManager();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected void setWidth(double widthWeight) {
        int width = ScreenDimensions.SCREEN_WIDTH;
        int height = ScreenDimensions.SCREEN_HEIGHT;
        width = (int) (width * widthWeight);
        getDialog().getWindow().setLayout(width, height);
    }

    protected void openDialogFragment(DialogType dialogTypeTitle, CommonOptionsDialogItemClickListener listener) {
        CommonOptionsDialogListFragment mDialogFragment = new CommonOptionsDialogListFragment(dialogTypeTitle, listener);
        mDialogFragment.show(this.mFragmentManager, CommonOptionsDialogItemClickListener.class.getSimpleName());
    }

    protected void openListPopUp(CommonListDialogType popupType, List<Specialities> list) {
        commonListDialogFragment = new CommonListDialogFragment(this, popupType, list);
        commonListDialogFragment.show(mFragmentManager, CommonListDialogFragment.class.getSimpleName());
    }

    protected void setHeight(double heightWeight) {
        int width = ScreenDimensions.SCREEN_WIDTH;
        int height = ScreenDimensions.SCREEN_HEIGHT;
        height = (int) (height * heightWeight);
        getDialog().getWindow().setLayout(width, height);
    }

    protected void setWidthHeight(double widthWeight, double heightWeight) {
        int width = ScreenDimensions.SCREEN_WIDTH;
        int height = ScreenDimensions.SCREEN_HEIGHT;
        width = (int) (width * widthWeight);
        height = (int) (height * heightWeight);
        getDialog().getWindow().setLayout(width, height);
    }

    public abstract void init();

    public abstract void initViews();

    public abstract void initListeners();

    public abstract void initData();


    protected void setDialogToMatchParent() {
        try {
            Window dialogWindow = getDialog().getWindow();
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialogWindow.getAttributes());
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            dialogWindow.setAttributes(lp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected EditText initEditSearchView(int hintId, TextWatcher textWatcher, boolean setPaddingTop) {
        EditText editText = initEditSearchView(hintId, textWatcher, null, null, setPaddingTop);
        return editText;
    }

    protected EditText initEditSearchView(int hintId, TextWatcher textWatcher, View.OnKeyListener onKeyListener, TextView.OnEditorActionListener onEditorActionListener, boolean setPaddingTop) {
        LinearLayout layoutEditSearch = (LinearLayout) view.findViewById(R.id.parent_edit_search);
        if (setPaddingTop)
            layoutEditSearch.setPadding(layoutEditSearch.getPaddingLeft(), mActivity.getResources().getDimensionPixelOffset(R.dimen.layout_edit_search_padding), layoutEditSearch.getPaddingRight(), layoutEditSearch.getPaddingBottom());
        layoutEditSearch.setVisibility(View.VISIBLE);
        final EditText editSearch = (EditText) layoutEditSearch.findViewById(R.id.edit_search);
        editSearch.setHint(hintId);
        editSearch.addTextChangedListener(textWatcher);
        if (onKeyListener != null)
            editSearch.setOnKeyListener(onKeyListener);
        if (onEditorActionListener != null)
            editSearch.setOnEditorActionListener(onEditorActionListener);
        ImageButton btClear = (ImageButton) layoutEditSearch.findViewById(R.id.bt_clear);
        btClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editSearch.setText("");
            }
        });
        return editSearch;
    }

    @Override
    public void onDialogItemClicked(CommonListDialogType commonListDialogType, Object object) {

    }

    protected void initActionbarTitle() {
        ImageButton btBack = (ImageButton) view.findViewById(R.id.bt_back);
        if (btBack != null) {
            btBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDialog().cancel();
                }
            });
        }
    }

    protected void initSaveCancelButton(View.OnClickListener onClickListener) {
        Button btSave = (Button) view.findViewById(R.id.bt_save);
        ImageButton containerLeftAction = (ImageButton) view.findViewById(R.id.bt_cross);
        btSave.setOnClickListener(onClickListener);
        containerLeftAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    protected void initCrossButton() {
        ImageButton btSave = (ImageButton) view.findViewById(R.id.bt_cross);
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    protected void initCancelButton() {
        LinearLayout containerLeftAction = (LinearLayout) view.findViewById(R.id.container_left_action);
        containerLeftAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    protected CommonListDialogFragment openCommonListDialogFragment(CommonListDialogItemClickListener listener, CommonListDialogType popupType, List<?> list) {
        CommonListDialogFragment commonListDialogFragment = new CommonListDialogFragment(listener, popupType, list);
        commonListDialogFragment.show(mFragmentManager, CommonListDialogFragment.class.getSimpleName());
        return commonListDialogFragment;
    }

    protected EditText getSearchEditText() {
        EditText editSearch = (EditText) view.findViewById(R.id.edit_search);
        return editSearch;
    }

    protected String getSearchEditTextValue() {
        EditText editSearch = (EditText) view.findViewById(R.id.edit_search);
        if (editSearch != null)
            return Util.getValidatedValue(String.valueOf(editSearch.getText()));
        return "";
    }

    public void initActionbarTitle(String titleId) {
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        if (tvTitle != null) tvTitle.setText(titleId);
    }

    protected CommonListSolarDialogFragment openCommonListSolarDialogFragment(CommonListDialogItemClickListener listener, CommonListDialogType popupType) {
        Util.checkNetworkStatus(mActivity);
        if (HealthCocoConstants.isNetworkOnline) {
            CommonListSolarDialogFragment commonListSolrDialogFragment = new CommonListSolarDialogFragment(listener, popupType);
            commonListSolrDialogFragment.show(mFragmentManager, CommonListSolarDialogFragment.class.getSimpleName());
            return commonListSolrDialogFragment;
        } else
            Util.showToast(mActivity, R.string.user_offline);
        return null;
    }

    /**
     * Hides virtual keyboard
     *
     * @author nehas
     */
    protected void hideKeyboard(View view) {
        InputMethodManager in = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
