package com.healthcoco.healthcocopad.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.fragments.AddVisitsFragment;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;

public class AddVisitsActivity extends HealthCocoActivity {

    private Intent intent;
    private int fragmentOrdinal;
    private CommonOpenUpFragmentType fragmentType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_visits);
        init();
    }

    private void init() {
        intent = getIntent();
        fragmentOrdinal = intent.getIntExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, -1);
        if (fragmentOrdinal != -1)
            initFragment(fragmentOrdinal);
    }

    private void initFragment(int ordinal) {
        fragmentType = CommonOpenUpFragmentType.values()[ordinal];
        switch (fragmentType) {
            case ADD_VISITS:
                openFragment(new AddVisitsFragment());
                break;
            case SELECT_DIAGRAM:
                break;
        }
    }

    private void openFragment(HealthCocoFragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.layout_fragment, fragment, fragment.getClass().getSimpleName());
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        showFinishConfirmationAlert();
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
}
