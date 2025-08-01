package com.healthcoco.healthcocopad.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.bean.AppointmentFeedback;
import com.healthcoco.healthcocopad.bean.QuestionAnswers;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.DoctorClinicProfile;
import com.healthcoco.healthcocopad.bean.server.DoctorProfile;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.dialogFragment.OtpVarificationFragment;
import com.healthcoco.healthcocopad.enums.FeedbackType;
import com.healthcoco.healthcocopad.enums.PopupWindowType;
import com.healthcoco.healthcocopad.listeners.PatientRegistrationDetailsListener;
import com.healthcoco.healthcocopad.popupwindow.PopupWindowListener;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;

import java.util.ArrayList;

import static com.healthcoco.healthcocopad.enums.PopupWindowType.DOCTOR_CLINIC_PROFILE;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;


/**
 * Created by neha on 13/01/18.
 */

public class AppointmentFeedbackFragment extends HealthCocoFragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, Response.Listener<VolleyResponseBean>,
        RatingBar.OnRatingBarChangeListener, PopupWindowListener {
    public static final String TAG_FEEDBACK_TYPE = "feedbackType";
    public static final String TAG_OBJECT = "object";
    public static final int DEFAULT_EXPERIENCE_LENGTH = 50;
    private TextView tvNameForFeedback;
    private RadioGroup rgLikeUnlike;
    private TextView tvClinicName;
    private TextView tvTitleQuestionImprovementRequired;
    private EditText editExperience;
    private EditText editHealthProblemTreatment;
    private FeedbackType feedbackType;
    private LinearLayout containerAnswersList;
    private RatingBar ratingBarFeedback;
    private double MINIMUM_RATING_VALUE = 2.5;
    private QuestionAnswers questionAnswer;
    private User user;
    private DoctorProfile doctorProfile;
    private String patientId;
    private PatientRegistrationDetailsListener registrationDetailsListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_feedback_appointment, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getExtra();
        init();
    }

    private void getExtra() {
        Intent intent = mActivity.getIntent();
        if (intent != null) {
            int feedbackTypeOrdinal = Parcels.unwrap(intent.getParcelableExtra(TAG_FEEDBACK_TYPE));
            feedbackType = FeedbackType.values()[feedbackTypeOrdinal];
        }
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        initData();
    }

    @Override
    public void initViews() {
        tvNameForFeedback = (TextView) view.findViewById(R.id.tv_name_for_feedback);
        rgLikeUnlike = (RadioGroup) view.findViewById(R.id.rg_like_unlike);
        tvClinicName = (TextView) view.findViewById(R.id.tv_clinic_name);
        tvTitleQuestionImprovementRequired = (TextView) view.findViewById(R.id.tv_title_what_can_be_improved);
        containerAnswersList = (LinearLayout) view.findViewById(R.id.container_answers_list);
        editExperience = (EditText) view.findViewById(R.id.edit_experience_text);
        editHealthProblemTreatment = (EditText) view.findViewById(R.id.edit_health_problem_detail);
        ratingBarFeedback = (RatingBar) view.findViewById(R.id.rating_star_feedback);
        resetEditExperienceHint();
        hideAllParents();
        if (!Util.isNullOrEmptyList(feedbackType.getVisibleViewIdsHashMap())) {
            for (Integer visibleViewId :
                    feedbackType.getVisibleViewIdsHashMap().keySet()) {
                View visibleView = view.findViewById(visibleViewId);
                visibleView.setVisibility(View.VISIBLE);
                if (visibleViewId == R.id.parent_clinic_name) {
                    if (feedbackType == FeedbackType.DOCTOR) {
                        tvClinicName.setHint(R.string.select);
                        tvClinicName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down_drug, 0);
                        tvClinicName.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.grey_background_updated));
                    } else {
                        tvClinicName.setHint(R.string.clinic_name_visited);
                        tvClinicName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        tvClinicName.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.white));
                    }
                } else if (visibleViewId == R.id.parent_health_problem_treatment) {
                    // Only default case present
                    editHealthProblemTreatment.setHint(R.string.eg_stomach_body_pain);
                }
            }
        }
    }

    private void hideAllParents() {
        hideView(R.id.tv_name_for_feedback);
//        hideView(R.id.layout_patient_header);
        hideView(R.id.parent_rating_five_star);
        hideView(R.id.parent_like_unlike);
        hideView(R.id.parent_clinic_name);
        hideView(R.id.parent_health_problem_treatment);
        hideView(R.id.parent_what_can_be_improved);
        hideView(R.id.parent_experience);
    }

    private void hideView(int viewId) {
        View parent = view.findViewById(viewId);
        parent.setVisibility(View.GONE);
    }

    @Override
    public void initListeners() {
        rgLikeUnlike.setOnCheckedChangeListener(this);
        ratingBarFeedback.setOnRatingBarChangeListener(this);
    }

    private void initData() {
        LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
        user = doctor.getUser();
        doctorProfile = LocalDataServiceImpl.getInstance(mApp).getDoctorProfileObject(user.getUniqueId());

        String nameForFeedback = "";
        String clinicPharmacyName = "";
        switch (feedbackType) {
            case DOCTOR:
                nameForFeedback = doctorProfile.getFirstName();
                initPopupWindows(tvClinicName, DOCTOR_CLINIC_PROFILE, (ArrayList<Object>) (ArrayList<?>) doctorProfile.getClinicProfile(), this);

                break;
        }

        initTitleTexts(nameForFeedback, clinicPharmacyName);
        tvNameForFeedback.setText(nameForFeedback);
        tvClinicName.setText(clinicPharmacyName);
        ratingBarFeedback.setRating(0);
    }

    private void initTitleTexts(String nameForFeedback, String clinicPharmacyName) {
        if (!Util.isNullOrEmptyList(feedbackType.getVisibleViewIdsHashMap())) {
            for (Integer visibleParentId :
                    feedbackType.getVisibleViewIdsHashMap().keySet()) {
                Integer titleTextId = feedbackType.getVisibleViewIdsHashMap().get(visibleParentId);
                if (visibleParentId == R.id.parent_rating_five_star) {
                    TextView tvTitleOverallExperienceStarRating = (TextView) view.findViewById(R.id.tv_title_overall_experience);
                    tvTitleOverallExperienceStarRating.setText(String.format(getResources().getString(titleTextId), nameForFeedback));
                } else if (visibleParentId == R.id.parent_clinic_name) {
                    TextView tvTitleClinicPharmacyName = (TextView) view.findViewById(R.id.tv_title_clinic_name);
                    tvTitleClinicPharmacyName.setText(String.format(getResources().getString(titleTextId), nameForFeedback));
                } else if (visibleParentId == R.id.parent_health_problem_treatment) {
                    TextView tvTitleHealthProblem = (TextView) view.findViewById(R.id.tv_title_for_which_health_problem);
                    tvTitleHealthProblem.setText(titleTextId);
                } else if (visibleParentId == R.id.parent_experience) {
                    TextView tvTitleExperience = (TextView) view.findViewById(R.id.tv_title_experience);
                    tvTitleExperience.setText(String.format(getResources().getString(titleTextId), nameForFeedback));
                }
            }
        }
    }

    private void resetEditExperienceHint() {
        switch (feedbackType) {
            case DOCTOR:
                editExperience.setHint(R.string.hint_recommendation_experience_message_clinic);
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.rb_like) {
            refreshQuestionAnswersList(LikeUnlikeType.LIKE);
        } else if (checkedId == R.id.rb_unlike) {
            refreshQuestionAnswersList(LikeUnlikeType.UNLIKE);
        }
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        if (rating < MINIMUM_RATING_VALUE)
            refreshQuestionAnswersList(LikeUnlikeType.UNLIKE);
        else
            refreshQuestionAnswersList(LikeUnlikeType.LIKE);

    }

    private void refreshQuestionAnswersList(LikeUnlikeType type) {
        containerAnswersList.removeAllViews();
        tvTitleQuestionImprovementRequired.setText(type.getQuestionId());
        for (final Integer answerId :
                type.getAnswersList()) {
            LinearLayout answerLayout = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.item_question_answer_feedback, null);
            CheckBox cbAnswer = (CheckBox) answerLayout.findViewById(R.id.cb_answer);
            cbAnswer.setText(answerId);
            cbAnswer.setTag(type);
            cbAnswer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (questionAnswer == null)
                        questionAnswer = new QuestionAnswers();
                    questionAnswer.setQuestion(Util.getValidatedValueOrBlankTrimming(tvTitleQuestionImprovementRequired));
                    questionAnswer.setAnswers(getSelectedAnswersList(isChecked, questionAnswer, getResources().getString(answerId)));
                    if (Util.isNullOrEmptyList(questionAnswer.getAnswers()))
                        questionAnswer.setAnswerNone(true);
                }

                private ArrayList<String> getSelectedAnswersList(boolean isChecked, QuestionAnswers questionAnswer, String answerString) {
                    ArrayList<String> answersList = questionAnswer.getAnswers();
                    if (answersList == null)
                        answersList = new ArrayList<String>();
                    if (isChecked && !answersList.contains(answerString))
                        answersList.add(answerString);
                    else if (answersList.contains(answerString))
                        answersList.remove(answerString);
                    return answersList;
                }
            });
            containerAnswersList.addView(answerLayout);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.container_right_action) {
            validateData();
        }
    }

    public void validateData() {
        Object errorMsg = getValidatedMessageForVisibleViews();
//
//        if (rgLikeUnlike.getCheckedRadioButtonId() < 0)
//            errorMsg = R.string.please_like_unlike;
//        else if (Util.isNullOrBlank(Util.getValidatedValueOrNull(editHealthProblemTreatment)))
//            errorMsg = R.string.please_tell_us_the_problem_for_which_you_visited;
//        else if (Util.isNullOrEmptyList(improvedRequiredAnswersList))
//            errorMsg = R.string.please_select_one_or_more_reasons_for_improvement;
//        else if (Util.isNullOrBlank(Util.getValidatedValueOrNull(editExperience)))
//            errorMsg = R.string.msg_share_some_experience_text;
//        else if (Util.getValidatedValueOrNull(editExperience).length() < DoctorFeedbackFragment.DEFAULT_EXPERIENCE_LENGTH)
//            errorMsg = R.string.hint_recommendation_experience_message;
//        else if (ratingBarFeedback.getRating() <= 0)
//            errorMsg = String.format(getResources().getString(R.string.please_rate_your_experience_with), Util.getValidatedValueOrBlank(tvNameForFeedback));

        if (errorMsg == null) {
            submitFeedback();
        } else {
            if (errorMsg instanceof Integer)
                Util.showToast(mActivity, (int) errorMsg);
            else
                Util.showToast(mActivity, (String) errorMsg);
        }
    }

    private Object getValidatedMessageForVisibleViews() {
        for (Integer visibleViewId :
                feedbackType.getVisibleViewIdsHashMap().keySet()) {
            if (visibleViewId == R.id.parent_rating_five_star) {
                if (ratingBarFeedback.getRating() <= 0) {
                    return String.format(
                            getResources().getString(R.string.please_rate_your_experience_with),
                            Util.getValidatedValueOrBlankTrimming(tvNameForFeedback)
                    );
                }
            } else if (visibleViewId == R.id.parent_clinic_name) {
                if (Util.isNullOrBlank(Util.getValidatedValueOrNull(tvClinicName))) {
                    return R.string.please_select_clinic_name_you_visited;
                }
            } else if (visibleViewId == R.id.parent_health_problem_treatment) {
                if (Util.isNullOrBlank(Util.getValidatedValueOrNull(editHealthProblemTreatment))) {
                    return R.string.please_tell_us_the_problem_for_which_you_visited;
                }
            } else if (visibleViewId == R.id.parent_what_can_be_improved) {
                if (questionAnswer == null || Util.isNullOrEmptyList(questionAnswer.getAnswers())) {
                    if (ratingBarFeedback.getRating() < MINIMUM_RATING_VALUE) {
                        return R.string.please_select_one_or_more_reasons_for_improvement;
                    } else {
                        return R.string.please_select_reasons_for_happy;
                    }
                }
            } else if (visibleViewId == R.id.parent_experience) {
                if (Util.isNullOrBlank(Util.getValidatedValueOrNull(editExperience))) {
                    return R.string.msg_share_some_experience;
                } else if (Util.getValidatedValueOrNull(editExperience).length() < DEFAULT_EXPERIENCE_LENGTH) {
                    return R.string.hint_recommendation_experience_message;
                }
            }
        }
        return null;
    }

    private void submitFeedback() {
        mActivity.showLoading(false);
        AppointmentFeedback appointmentFeedback = new AppointmentFeedback();
        switch (feedbackType) {
            case DOCTOR:
                appointmentFeedback.setDoctorId(doctorProfile.getUniqueId());
                Object tag = tvClinicName.getTag();
                if (tag != null && tag instanceof DoctorClinicProfile) {
                    DoctorClinicProfile doctorClinicProfile = (DoctorClinicProfile) tag;
                    appointmentFeedback.setLocationId(doctorClinicProfile.getLocationId());
                    appointmentFeedback.setHospitalId(doctorClinicProfile.getHospitalId());

                }
                break;
        }
        appointmentFeedback.setQuestionAnswers(getQuestionAnswersList(questionAnswer));
        if (Util.isNullOrBlank(patientId))
            appointmentFeedback.setPatientId(patientId);
        appointmentFeedback.setFeedbackType(feedbackType);
        appointmentFeedback.setRecommended(getRecommendedFlag());
        appointmentFeedback.setOverallExperience((int) ratingBarFeedback.getRating());
        appointmentFeedback.setAnonymous(false);
        appointmentFeedback.setExperience(Util.getValidatedValueOrBlankTrimming(editExperience));
        WebDataServiceImpl.getInstance(mApp).addAppointmentFeedback(AppointmentFeedback.class, appointmentFeedback, this, this);
    }

    private ArrayList<QuestionAnswers> getQuestionAnswersList(QuestionAnswers questionAnswer) {
        if (questionAnswer != null) {
            ArrayList<QuestionAnswers> questionAnswersesList = new ArrayList<>();
            questionAnswersesList.add(questionAnswer);
            return questionAnswersesList;
        }
        return null;
    }

    private boolean getRecommendedFlag() {
//        if (rgLikeUnlike.getCheckedRadioButtonId() == R.id.rb_like)
//            return true;
        if (ratingBarFeedback.getRating() > MINIMUM_RATING_VALUE)
            return true;
        return false;
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response != null && response.getWebServiceType() != null) {
            switch (response.getWebServiceType()) {
                case ADD_PRESCRIPTION_APPOINTMENT_FEEDBACK:
                    Util.showToast(mActivity, R.string.feedback_sent_successfully);
                    ((CommonOpenUpActivity) mActivity).openHomeActivity();
                    break;
            }
        }
        mActivity.hideLoading();
    }

    @Override
    public void onItemSelected(PopupWindowType popupWindowType, Object object) {
        if (popupWindowType != null)
            switch (popupWindowType) {
                case DOCTOR_CLINIC_PROFILE:
                    if (object != null && object instanceof DoctorClinicProfile) {
                        DoctorClinicProfile selectedClinicProfile = (DoctorClinicProfile) object;
                        tvClinicName.setText(selectedClinicProfile.getLocationName());
                        tvClinicName.setTag(selectedClinicProfile);
                    }
                    break;
            }
    }

    @Override
    public void onEmptyListFound() {

    }

    public void initDataFromPreviousFragment(String patientId) {
        this.patientId = patientId;
    }

    enum LikeUnlikeType {
        LIKE(R.string.what_were_you_happy_with,
                new ArrayList<Integer>() {{
                    add(R.string.doctor_friendliness);
                    add(R.string.explanation_of_health_issue);
                    add(R.string.treatment_satisfaction);
                    add(R.string.value_of_money);
                    add(R.string.wait_time);
                }}),
        UNLIKE(R.string.what_can_be_improved,
                new ArrayList<Integer>() {{
                    add(R.string.doctor_friendliness);
                    add(R.string.explanation_of_health_issue);
                    add(R.string.treatment_satisfaction);
                    add(R.string.value_of_money);
                    add(R.string.wait_time);
                }});

        private final int questionId;
        private final ArrayList<Integer> answersList;

        LikeUnlikeType(int questionId, ArrayList<Integer> answersList) {
            this.questionId = questionId;
            this.answersList = answersList;
        }

        public int getQuestionId() {
            return questionId;
        }

        public ArrayList<Integer> getAnswersList() {
            return answersList;
        }
    }

}
