package com.healthcoco.healthcocopad.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.enums.ActionbarLeftRightActionTypeDrawables;
import com.healthcoco.healthcocopad.enums.ActionbarType;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.fragments.AddVisitsFragment;
import com.healthcoco.healthcocopad.fragments.DiagramsListFragment;
import com.healthcoco.healthcocopad.fragments.SelectedDiagramDetailFragment;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;

public class AddVisitsActivity extends HealthCocoActivity {

    private Intent intent;
    private int fragmentOrdinal;
    private CommonOpenUpFragmentType fragmentType;
    private FragmentTransaction transaction;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_visits);
        init();
    }

    private void init() {
        intent = getIntent();
        fragmentOrdinal = intent.getIntExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, -1);
        initMembers();
        if (fragmentOrdinal != -1)
            initFragment(fragmentOrdinal);
    }

    private void initMembers() {
        LogUtils.LOGD(TAG, "initMembers");
        transaction = getSupportFragmentManager().beginTransaction();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    private void initFragment(int ordinal) {
        fragmentType = CommonOpenUpFragmentType.values()[ordinal];
        switch (fragmentType) {
            case ADD_VISITS:
                openFragment(ActionbarType.HIDDEN, R.string.add_visits, new AddVisitsFragment());
                break;
            case SELECT_DIAGRAM:
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_BACK, ActionbarLeftRightActionTypeDrawables.NO_LEFT_RIGHT_ACTION, fragmentType.getTitleId(), new DiagramsListFragment());
                break;
            case SELECTED_DIAGRAM_DETAIL:
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_CROSS, ActionbarLeftRightActionTypeDrawables.WITH_SAVE, fragmentType.getTitleId(), new SelectedDiagramDetailFragment());
                break;
        }
    }

    private void openFragment(ActionbarType actionbarType, int actionBarTitleId, HealthCocoFragment fragment) {
        openFragment(actionbarType, null, null, actionBarTitleId, fragment);
    }

    private void openFragment(ActionbarType actionbarType, ActionbarLeftRightActionTypeDrawables leftAction, ActionbarLeftRightActionTypeDrawables rightAction, int actionBarTitle, HealthCocoFragment fragment) {
        Bundle bundle = new Bundle();
        initActionBar(actionbarType, actionBarTitle, leftAction, rightAction);
        bundle.putInt(HealthCocoConstants.TAG_FRAGMENT_NAME, fragmentType.ordinal());
        fragment.setArguments(bundle);
        transaction.add(R.id.layout_fragment, fragment, fragment.getClass().getSimpleName());
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (fragmentType != null) {
            switch (fragmentType) {
                case ADD_VISITS:
                    AddVisitsFragment addVisitsFragment = (AddVisitsFragment) getSupportFragmentManager().findFragmentByTag(AddVisitsFragment.class.getSimpleName());
                    boolean wasKeyboardOrWidgetVisible = false;
                    if (addVisitsFragment != null)
                        wasKeyboardOrWidgetVisible = addVisitsFragment.hideKeyboardOrWidgetIfVisible();
                    if (!wasKeyboardOrWidgetVisible)
                        showFinishConfirmationAlert();
                    return;
                default:
                    finish();
                    break;
            }
        } else
            super.onBackPressed();
    }

    public void showFinishConfirmationAlert() {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle(R.string.alert);
        alertBuilder.setMessage(R.string.your_changes_will_not_be_saved);
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton(R.string.leave, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        alertBuilder.setNegativeButton(R.string.stay, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertBuilder.create();
        alertBuilder.show();
    }

    public void initActionBar(ActionbarType actionbarType, int title, ActionbarLeftRightActionTypeDrawables leftAction, ActionbarLeftRightActionTypeDrawables rightAction) {
        if (actionbarType == ActionbarType.HIDDEN) {
            hideActionBar();
        } else {
            View actionbar = getLayoutInflater().inflate(actionbarType.getActionBarLayoutId(), null);
            actionbar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            toolbar.setContentInsetsAbsolute(0, 0);
            toolbar.addView(actionbar);
            LinearLayout containerLeftAction = (LinearLayout) actionbar.findViewById(R.id.container_left_action);
            if (leftAction != null && leftAction != ActionbarLeftRightActionTypeDrawables.NO_LEFT_RIGHT_ACTION) {
                containerLeftAction.setVisibility(View.VISIBLE);
                View leftView = getLayoutInflater().inflate(leftAction.getLayoutId(), null);
                if (leftView != null) {
                    if (leftAction.isDrawableBackground()) {
                        ImageButton imageButton = (ImageButton) leftView;
                        imageButton.setImageResource(leftAction.getDrawableTitleId());
                    } else {
                        TextView button = (TextView) leftView;
                        button.setText(leftAction.getDrawableTitleId());
                    }
                    containerLeftAction.addView(leftView);
                }
                containerLeftAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            } else
                containerLeftAction.setVisibility(View.GONE);

            LinearLayout containerRightAction = (LinearLayout) actionbar.findViewById(R.id.container_right_action);
            if (rightAction != null && rightAction != ActionbarLeftRightActionTypeDrawables.NO_LEFT_RIGHT_ACTION) {
                containerRightAction.setVisibility(View.VISIBLE);
                View rightView = getLayoutInflater().inflate(rightAction.getLayoutId(), null);
                if (rightView != null) {
                    if (rightAction.isDrawableBackground()) {
                        ImageButton imageButton = (ImageButton) rightView;
                        imageButton.setImageResource(rightAction.getDrawableTitleId());
                    } else {
                        TextView button = (TextView) rightView;
                        button.setText(rightAction.getDrawableTitleId());
                    }
                    containerRightAction.addView(rightView);
                }
            } else {
                containerRightAction.setVisibility(View.GONE);
            }
            if (title > 0) {
                TextView tvTitle = (TextView) toolbar.findViewById(R.id.tv_title);
                tvTitle.setText(title);
            }
        }
        setupUI(toolbar);
    }

    public void setupUI(final View view) {
        try {
            //Set up touch listener for non-text box views to hide keyboard.
            if (!(view instanceof EditText)) {
                view.setOnTouchListener(new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        Util.hideKeyboard(AddVisitsActivity.this, view);
                        return false;
                    }
                });
            }
            //If a layout container, iterate over children and seed recursion.
            if (view instanceof ViewGroup) {
                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                    View innerView = ((ViewGroup) view).getChildAt(i);
                    setupUI(innerView);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideActionBar() {
        toolbar.setVisibility(View.GONE);
    }

    public void initSaveButton(View.OnClickListener listener) {
        LinearLayout btSave = (LinearLayout) findViewById(R.id.container_right_action);
        if (btSave != null)
            btSave.setOnClickListener(listener);
    }

    public void initActionbarTitle(int titleId) {
        initActionbarTitle(getResources().getString(titleId));
    }

    public void initActionbarTitle(String titleId) {
        TextView tvTitle = (TextView) toolbar.findViewById(R.id.tv_title);
        if (tvTitle != null) tvTitle.setText(titleId);
    }

    public void openCommonOpenUpVisitActivity(CommonOpenUpFragmentType fragmentType, String tag, Object intentData) {
        openCommonOpenUpVisitActivity(fragmentType, tag, intentData, 0);
    }

    public void openCommonOpenUpVisitActivity(CommonOpenUpFragmentType fragmentType, int requestCode) {
        openCommonOpenUpVisitActivity(fragmentType, null, null, requestCode);
    }

    public void openCommonOpenUpVisitActivity(CommonOpenUpFragmentType fragmentType, Object intentData, int requestCode) {
        openCommonOpenUpVisitActivity(fragmentType, HealthCocoConstants.TAG_COMMON_OPENUP_INTENT_DATA, intentData, requestCode);
    }

    public void openCommonOpenUpVisitActivity(CommonOpenUpFragmentType fragmentType, String tag, Object intentData, int requestCode) {
        Intent intent = new Intent(this, AddVisitsActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, fragmentType.ordinal());
        if (!Util.isNullOrBlank(tag) && intentData != null)
            intent.putExtra(tag, Parcels.wrap(intentData));
        if (requestCode == 0)
            startActivity(intent);
        else
            startActivityForResult(intent, requestCode);
    }
}
