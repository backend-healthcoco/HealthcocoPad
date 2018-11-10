package com.healthcoco.healthcocopad.dialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.Exercise;
import com.healthcoco.healthcocopad.enums.ExerciseType;
import com.healthcoco.healthcocopad.enums.PopupWindowType;
import com.healthcoco.healthcocopad.popupwindow.PopupWindowListener;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Prashant on 19-10-2018.
 */

public class AddExerciseDialogFragment extends HealthCocoDialogFragment implements View.OnClickListener, PopupWindowListener {


    public ArrayList<Object> excerciseTypes = new ArrayList<Object>() {{
        addAll(Arrays.asList(ExerciseType.values()));
    }};
    private TextView titleTextView;
    private TextView tvExerciseType;
    private EditText editMinPerDay;
    private EditText editTimesPerWeek;
    private Bundle bundle;
    private Exercise exercise;

    public AddExerciseDialogFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_add_exercise, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        bundle = getArguments();
        if (bundle != null)
            exercise = Parcels.unwrap(bundle.getParcelable(HealthCocoConstants.TAG_INTENT_DATA));

        setWidthHeight(0.80, 0.60);
        init();
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        initData();
    }

    @Override
    public void initViews() {
        titleTextView = view.findViewById(R.id.tv_title);
        tvExerciseType = view.findViewById(R.id.tv_exercise_type);
        editMinPerDay = view.findViewById(R.id.edit_minutes_per_day);
        editTimesPerWeek = view.findViewById(R.id.edit_time_per_week);

        titleTextView.setText(R.string.add);
    }

    @Override
    public void initListeners() {
        initSaveCancelButton(this);

        mActivity.initPopupWindows(tvExerciseType, PopupWindowType.EXERCISE_TYPE, excerciseTypes, this);

    }

    @Override
    public void initData() {
        if (exercise != null) {
            if (!Util.isNullOrZeroNumber(exercise.getMinPerDay()))
                editMinPerDay.setText(Util.getValidatedValue(exercise.getMinPerDay()));
            if (exercise.getType() != null) {
                tvExerciseType.setText(exercise.getType().getExcerciseType());
                tvExerciseType.setTag(exercise.getType());
            }
            if (!Util.isNullOrZeroNumber(exercise.getTimePerWeek()))
                editTimesPerWeek.setText(Util.getValidatedValue(exercise.getTimePerWeek()));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_save:
                validateData();
                break;
        }
    }

    private void validateData() {
        String msg = null;

        String type = String.valueOf(tvExerciseType.getText());
        if (Util.isNullOrBlank(type))
            msg = getResources().getString((R.string.please_enter_serving_type));
        if (Util.isNullOrZeroNumber(Util.getValidatedDoubleValue(editMinPerDay)))
            msg = getResources().getString((R.string.please_enter_value));
        if (Util.isNullOrZeroNumber(Util.getValidatedDoubleValue(editTimesPerWeek)))
            msg = getResources().getString((R.string.please_enter_value));

        if (Util.isNullOrBlank(msg)) {
            addEquivalentMeasurement();
        } else
            Util.showToast(mActivity, msg);
    }

    private void addEquivalentMeasurement() {
        Exercise exercise = new Exercise();
        exercise.setType((ExerciseType) tvExerciseType.getTag());
        exercise.setMinPerDay((int) Util.getValidatedDoubleValue(editMinPerDay));
        exercise.setTimePerWeek((int) Util.getValidatedDoubleValue(editTimesPerWeek));

        Intent data = new Intent();
        data.putExtra(HealthCocoConstants.TAG_INTENT_DATA, Parcels.wrap(exercise));
        getTargetFragment().onActivityResult(HealthCocoConstants.REQUEST_CODE_ADD_EXERCISE, mActivity.RESULT_OK, data);
        dismiss();
    }

    @Override
    public void onItemSelected(PopupWindowType popupWindowType, Object object) {
        if (object != null && object instanceof ExerciseType) {
            ExerciseType exerciseType = (ExerciseType) object;
            tvExerciseType.setText(exerciseType.getExcerciseType());
            tvExerciseType.setTag(exerciseType);
        }
    }

    @Override
    public void onEmptyListFound() {

    }
}
