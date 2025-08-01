package com.healthcoco.healthcocopad.dialogFragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.listeners.AddEditVaccineNoteListener;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.UpdateVaccineListViewHolder;

public class AddEditVaccineNoteDialogFragment extends HealthCocoDialogFragment implements View.OnClickListener {
    private String note;
    private EditText editNote;
    private TextView btSave;
    private AddEditVaccineNoteListener addEditVaccineNoteListener;

    public AddEditVaccineNoteDialogFragment(AddEditVaccineNoteListener addEditVaccineNoteListener) {
        this.addEditVaccineNoteListener = addEditVaccineNoteListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        view = inflater.inflate(R.layout.dialog_fragment_addvaccine_note, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        Bundle b = getArguments();
        if (b != null) {
            note = b.getString(UpdateVaccineListViewHolder.TAG_VACCINE_NOTE);
            init();
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
        editNote = (EditText) view.findViewById(R.id.edit_note);
        btSave = (TextView) view.findViewById(R.id.bt_save);
    }

    @Override
    public void initListeners() {
        initActionbarTitle(getResources().getString(R.string.add_note));
        initSaveCancelButton(this);
    }

    @Override
    public void initData() {
        if (!Util.isNullOrBlank(note))
            editNote.setText(note);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.bt_save) {
            addEditVaccineNoteListener.getVaccineNote(Util.getValidatedValue(editNote.getText().toString()));
            dismiss();
        }

    }
}
