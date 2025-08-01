package com.healthcoco.healthcocopad.dialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.TreatmentFields;
import com.healthcoco.healthcocopad.enums.DentalChartType;
import com.healthcoco.healthcocopad.listeners.SelectedToothNumberListner;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Shreshtha on 20-07-2017.
 */

public class SelectToothDetailDialogFragment extends HealthCocoDialogFragment implements View.OnClickListener {
    private static final String SEPARATOR_GROUP_NOTES = ", ";
    private ImageView imageviewCross;
    private LinearLayout layoutQuadrantFour;
    private LinearLayout layoutQuadrantThree;
    private LinearLayout layoutQuadrantTwo;
    private LinearLayout layoutQuadrantOne;
    private TextView tvToggleChart;
    private Button buttonDone;
    private LinearLayout mainContainerAdultChart;
    private LinearLayout layoutBabyQuadrantFour;
    private LinearLayout layoutBabyQuadrantThree;
    private LinearLayout layoutBabyQuadrantTwo;
    private LinearLayout layoutBabyQuadrantOne;
    private LinearLayout mainContainerBabyChart;
    private TreatmentFields treatmentFields;
    private int toothNo;
    private HashMap<String, TreatmentFields> fieldsList = new HashMap<>();
    private HashMap<String, String> stringHashMap = new HashMap<String, String>();
    private LinearLayout layoutToothItem;
    private SelectedToothNumberListner selectedToothNumberListner;
    private List<TreatmentFields> treatmentFieldsList;


    public SelectToothDetailDialogFragment(SelectedToothNumberListner selectedToothNumberListner, List<TreatmentFields> treatmentFieldsList) {
        this.selectedToothNumberListner = selectedToothNumberListner;
        this.treatmentFieldsList = treatmentFieldsList;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_select_tooth_detail, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        Intent intent = mActivity.getIntent();
//            List<TreatmentFields> fieldsList = Parcels .unwrap(intent.getParcelableExtra(AddTreatmentItemDetailFragment.TAG_TOOTH_LIST));
        if (!Util.isNullOrEmptyList(treatmentFieldsList)) {
            formHashMap(treatmentFieldsList);
        }
        init();
        setWidthHeight(0.65, 0.65);
    }

    private void formHashMap(List<TreatmentFields> list) {
        for (TreatmentFields fields :
                list) {
            fieldsList.put(fields.getKey(), fields);
        }
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        iniData();
    }

    private void iniData() {
        if (!Util.isNullOrEmptyList(fieldsList)) {
            for (TreatmentFields fields :
                    fieldsList.values()) {
                if (fields.getKey().equals(HealthCocoConstants.TAG_TOOTH_NUMBER)) {
                    String value = fields.getValue();
                    String[] split = value.split("\\s*,\\s*");
                    for (String toothNumber : split) {
                        if (!Util.isNullOrBlank(toothNumber)) {
                            int toothNumberInt = Integer.parseInt(toothNumber);
                            if (toothNumberInt >= DentalChartType.MIN_ADULT_TOOTH_NUMBER && toothNumberInt <= DentalChartType.MAX_ADULT_TOOTH_NUMBER) {
                                DentalChartType.Q1 q1 = DentalChartType.Q1.getValue(Integer.parseInt(toothNumber));
                                if (q1 != null && layoutQuadrantOne.findViewWithTag(q1) != null) {
                                    addToothNumberToList(toothNumberInt, layoutQuadrantOne.findViewWithTag(q1));
                                    continue;
                                }
                                DentalChartType.Q2 q2 = DentalChartType.Q2.getValue(Integer.parseInt(toothNumber));
                                if (q2 != null && layoutQuadrantTwo.findViewWithTag(q2) != null) {
                                    addToothNumberToList(toothNumberInt, layoutQuadrantTwo.findViewWithTag(q2));
                                    continue;
                                }
                                DentalChartType.Q3 q3 = DentalChartType.Q3.getValue(Integer.parseInt(toothNumber));
                                if (q3 != null && layoutQuadrantThree.findViewWithTag(q3) != null) {
                                    addToothNumberToList(toothNumberInt, layoutQuadrantThree.findViewWithTag(q3));
                                    continue;
                                }
                                DentalChartType.Q4 q4 = DentalChartType.Q4.getValue(Integer.parseInt(toothNumber));
                                if (q4 != null && layoutQuadrantFour.findViewWithTag(q4) != null) {
                                    addToothNumberToList(toothNumberInt, layoutQuadrantFour.findViewWithTag(q4));
                                    continue;
                                }
                            } else if (toothNumberInt >= DentalChartType.MIN_CHILD_TOOTH_NUMBER || toothNumberInt <= DentalChartType.MAX_CHILD_TOOTH_NUMBER) {
                                if (mainContainerAdultChart.getVisibility() == View.VISIBLE) {
                                    mainContainerBabyChart.setVisibility(View.VISIBLE);
                                    mainContainerAdultChart.setVisibility(View.GONE);
                                    tvToggleChart.setText(getResources().getString(R.string.toggle_to_adult_chart));
                                }
                                //repest conditions same as above for babyQuadrants
                                DentalChartType.QB1 qb1 = DentalChartType.QB1.getValue(Integer.parseInt(toothNumber));
                                if (qb1 != null && layoutBabyQuadrantOne.findViewWithTag(qb1) != null) {
                                    addToothNumberToList(toothNumberInt, layoutBabyQuadrantOne.findViewWithTag(qb1));
                                    continue;
                                }
                                DentalChartType.QB2 qb2 = DentalChartType.QB2.getValue(Integer.parseInt(toothNumber));
                                if (qb2 != null && layoutBabyQuadrantTwo.findViewWithTag(qb2) != null) {
                                    addToothNumberToList(toothNumberInt, layoutBabyQuadrantTwo.findViewWithTag(qb2));
                                    continue;
                                }
                                DentalChartType.QB3 qb3 = DentalChartType.QB3.getValue(Integer.parseInt(toothNumber));
                                if (qb3 != null && layoutBabyQuadrantThree.findViewWithTag(qb3) != null) {
                                    addToothNumberToList(toothNumberInt, layoutBabyQuadrantThree.findViewWithTag(qb3));
                                    continue;
                                }
                                DentalChartType.QB4 qb4 = DentalChartType.QB4.getValue(Integer.parseInt(toothNumber));
                                if (qb4 != null && layoutBabyQuadrantFour.findViewWithTag(qb4) != null) {
                                    addToothNumberToList(toothNumberInt, layoutBabyQuadrantFour.findViewWithTag(qb4));
                                    continue;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void addToothLayout(DentalChartType quadrantOneAdult, LinearLayout containerToothLayout) {
        ArrayList<Object> arrayList = quadrantOneAdult.getQuadrantType();
        containerToothLayout.removeAllViews();
        for (final Object object : arrayList) {
            addToothContainer(object, containerToothLayout);
        }
    }

    private void addToothContainer(Object object, LinearLayout containerToothLayout) {
        LinearLayout linearLayout = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.sub_item_treatment_tooth, null);
        layoutToothItem = (LinearLayout) linearLayout.findViewById(R.id.layout_tooth_item);
        ImageView imageViewTooth = (ImageView) linearLayout.findViewById(R.id.iv_tooth);
        TextView tvToothNo = (TextView) linearLayout.findViewById(R.id.tv_tooth_no);
        if (object instanceof DentalChartType.Q1) {
            DentalChartType.Q1 toothDrawable1 = (DentalChartType.Q1) object;
            imageViewTooth.setImageResource(toothDrawable1.getResourceId());
            tvToothNo.setText(String.valueOf(toothDrawable1.getToothNo()));
        } else if (object instanceof DentalChartType.Q2) {
            DentalChartType.Q2 toothDrawable1 = (DentalChartType.Q2) object;
            imageViewTooth.setImageResource(toothDrawable1.getResourceId());
            tvToothNo.setText(String.valueOf(toothDrawable1.getToothNo()));
        } else if (object instanceof DentalChartType.Q3) {
            DentalChartType.Q3 toothDrawable1 = (DentalChartType.Q3) object;
            imageViewTooth.setImageResource(toothDrawable1.getResourceId());
            tvToothNo.setText(String.valueOf(toothDrawable1.getToothNo()));
        } else if (object instanceof DentalChartType.Q4) {
            DentalChartType.Q4 toothDrawable1 = (DentalChartType.Q4) object;
            imageViewTooth.setImageResource(toothDrawable1.getResourceId());
            tvToothNo.setText(String.valueOf(toothDrawable1.getToothNo()));
        } else if (object instanceof DentalChartType.QB1) {
            DentalChartType.QB1 toothDrawable1 = (DentalChartType.QB1) object;
            imageViewTooth.setImageResource(toothDrawable1.getResourceId());
            tvToothNo.setText(String.valueOf(toothDrawable1.getToothNo()));
        } else if (object instanceof DentalChartType.QB2) {
            DentalChartType.QB2 toothDrawable1 = (DentalChartType.QB2) object;
            imageViewTooth.setImageResource(toothDrawable1.getResourceId());
            tvToothNo.setText(String.valueOf(toothDrawable1.getToothNo()));
        } else if (object instanceof DentalChartType.QB3) {
            DentalChartType.QB3 toothDrawable1 = (DentalChartType.QB3) object;
            imageViewTooth.setImageResource(toothDrawable1.getResourceId());
            tvToothNo.setText(String.valueOf(toothDrawable1.getToothNo()));
        } else if (object instanceof DentalChartType.QB4) {
            DentalChartType.QB4 toothDrawable1 = (DentalChartType.QB4) object;
            imageViewTooth.setImageResource(toothDrawable1.getResourceId());
            tvToothNo.setText(String.valueOf(toothDrawable1.getToothNo()));
        }
        layoutToothItem.setTag(object);
        layoutToothItem.setOnClickListener(this);
        containerToothLayout.addView(linearLayout);
    }

    @Override
    public void initViews() {
        imageviewCross = (ImageView) view.findViewById(R.id.imageview_cross);
        buttonDone = (Button) view.findViewById(R.id.button_done);
        tvToggleChart = (TextView) view.findViewById(R.id.tv_toggle_chart);
        layoutQuadrantOne = (LinearLayout) view.findViewById(R.id.layout_quadrant_one);
        layoutQuadrantTwo = (LinearLayout) view.findViewById(R.id.layout_quadrant_two);
        layoutQuadrantThree = (LinearLayout) view.findViewById(R.id.layout_quadrant_three);
        layoutQuadrantFour = (LinearLayout) view.findViewById(R.id.layout_quadrant_four);

        mainContainerAdultChart = (LinearLayout) view.findViewById(R.id.main_container_adult_chart);
        mainContainerBabyChart = (LinearLayout) view.findViewById(R.id.main_container_baby_chart);
        layoutBabyQuadrantOne = (LinearLayout) view.findViewById(R.id.layout_baby_quadrant_one);
        layoutBabyQuadrantTwo = (LinearLayout) view.findViewById(R.id.layout_baby_quadrant_two);
        layoutBabyQuadrantThree = (LinearLayout) view.findViewById(R.id.layout_baby_quadrant_three);
        layoutBabyQuadrantFour = (LinearLayout) view.findViewById(R.id.layout_baby_quadrant_four);

        addToothLayout(DentalChartType.QUADRANT_ONE_ADULT, layoutQuadrantOne);
        addToothLayout(DentalChartType.QUADRANT_TWO_ADULT, layoutQuadrantTwo);
        addToothLayout(DentalChartType.QUADRANT_THREE_ADULT, layoutQuadrantThree);
        addToothLayout(DentalChartType.QUADRANT_FOUR_ADULT, layoutQuadrantFour);
        addToothLayout(DentalChartType.QUADRANT_ONE_BABY, layoutBabyQuadrantOne);
        addToothLayout(DentalChartType.QUADRANT_TWO_BABY, layoutBabyQuadrantTwo);
        addToothLayout(DentalChartType.QUADRANT_THREE_BABY, layoutBabyQuadrantThree);
        addToothLayout(DentalChartType.QUADRANT_FOUR_BABY, layoutBabyQuadrantFour);
    }

    @Override
    public void initListeners() {
        imageviewCross.setOnClickListener(this);
        buttonDone.setOnClickListener(this);
        tvToggleChart.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.imageview_cross) {
            dismiss();
        } else if (id == R.id.button_done) {
            setResultData();
        } else if (id == R.id.tv_toggle_chart) {
            initViews();
            if (mainContainerAdultChart.getVisibility() == View.VISIBLE) {
                mainContainerBabyChart.setVisibility(View.VISIBLE);
                mainContainerAdultChart.setVisibility(View.GONE);
                tvToggleChart.setText(getResources().getString(R.string.toggle_to_adult_chart));
                stringHashMap.clear();
            } else {
                mainContainerBabyChart.setVisibility(View.GONE);
                mainContainerAdultChart.setVisibility(View.VISIBLE);
                tvToggleChart.setText(getResources().getString(R.string.toggle_to_baby_chart));
                stringHashMap.clear();
            }
        } else if (id == R.id.layout_tooth_item) {
            setToothItemInLayout(v);
        }
    }

    private void setToothItemInLayout(View v) {
        Object tag = v.getTag();
        if (tag instanceof DentalChartType.Q1) {
            DentalChartType.Q1 toothDrawable1 = (DentalChartType.Q1) tag;
            toothNo = toothDrawable1.getToothNo();
        } else if (tag instanceof DentalChartType.Q2) {
            DentalChartType.Q2 toothDrawable1 = (DentalChartType.Q2) tag;
            toothNo = toothDrawable1.getToothNo();
        } else if (tag instanceof DentalChartType.Q3) {
            DentalChartType.Q3 toothDrawable1 = (DentalChartType.Q3) tag;
            toothNo = toothDrawable1.getToothNo();
        } else if (tag instanceof DentalChartType.Q4) {
            DentalChartType.Q4 toothDrawable1 = (DentalChartType.Q4) tag;
            toothNo = toothDrawable1.getToothNo();
        } else if (tag instanceof DentalChartType.QB1) {
            DentalChartType.QB1 toothDrawable1 = (DentalChartType.QB1) tag;
            toothNo = toothDrawable1.getToothNo();
        } else if (tag instanceof DentalChartType.QB2) {
            DentalChartType.QB2 toothDrawable1 = (DentalChartType.QB2) tag;
            toothNo = toothDrawable1.getToothNo();
        } else if (tag instanceof DentalChartType.QB3) {
            DentalChartType.QB3 toothDrawable1 = (DentalChartType.QB3) tag;
            toothNo = toothDrawable1.getToothNo();
        } else if (tag instanceof DentalChartType.QB4) {
            DentalChartType.QB4 toothDrawable1 = (DentalChartType.QB4) tag;
            toothNo = toothDrawable1.getToothNo();
        }
        addToothNumberToList(toothNo, v);
    }

    private void addToothNumberToList(int toothNo, View v) {
        if (!Util.isNullOrBlank(String.valueOf(toothNo))) {
            String key = String.valueOf(toothNo);
            v.setSelected(!v.isSelected());
            if (stringHashMap.containsKey(key)) {
                stringHashMap.remove(key);
            } else
                stringHashMap.put(key, String.valueOf(toothNo));
        }
    }

    private void setResultData() {
        if (treatmentFields == null) {
            treatmentFields = new TreatmentFields();
            treatmentFields.setKey(HealthCocoConstants.TAG_TOOTH_NUMBER);
            treatmentFields.setValue(getToothNumbers(getToothNumberList()));
        }
        fieldsList.put(treatmentFields.getKey(), treatmentFields);
        selectedToothNumberListner.onDoneClicked(fieldsList);
        dismiss();
    }

    private List<String> getToothNumberList() {
        if (!Util.isNullOrEmptyList(stringHashMap)) {
            List<String> itemArrayList = new ArrayList<String>(stringHashMap.values());
            return itemArrayList;
        }
        return null;
    }

    private String getToothNumbers(List<String> toothNumberList) {
        String notesText = "";
        if (!Util.isNullOrEmptyList(toothNumberList)) {
            for (String note :
                    toothNumberList) {
                int index = toothNumberList.indexOf(note);
                if (index == toothNumberList.size() - 1)
                    notesText = notesText + note;
                else
                    notesText = notesText + note + SEPARATOR_GROUP_NOTES;
            }
        }
        return notesText;
    }
}
