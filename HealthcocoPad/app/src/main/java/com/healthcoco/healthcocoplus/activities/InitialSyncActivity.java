package com.healthcoco.healthcocoplus.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.healthcoco.healthcocoplus.HealthCocoActivity;
import com.healthcoco.healthcocoplus.HealthCocoFragment;
import com.healthcoco.healthcocoplus.fragments.InitialSyncFragment;

/**
 * Created by Shreshtha on 25-01-2017.
 */

public class InitialSyncActivity extends HealthCocoActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openFragment(new InitialSyncFragment());
    }

    private void openFragment(HealthCocoFragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(android.R.id.content, fragment, fragment.getClass().getSimpleName());
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
    }
}
