package com.healthcoco.healthcocopad.viewholders;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.listeners.PatientRegistrationListener;

/**
 * Created by neha on 19/03/16.
 */
public class NotesListItemViewHolder extends HealthCocoViewHolder implements View.OnClickListener {

    private PatientRegistrationListener notesItemClickListener;
    private HealthCocoActivity mActivity;
    private String objData;
    private TextView tvNote;
    private ImageButton btDelete;

    public NotesListItemViewHolder(HealthCocoActivity mActivity, PatientRegistrationListener notesItemClickListener) {
        super(mActivity);
        this.mActivity = mActivity;
        this.notesItemClickListener = notesItemClickListener;
    }

    @Override
    public void setData(Object object) {
        this.objData = (String) object;
    }

    @Override
    public void applyData() {
        tvNote.setText(objData);
    }

    @Override
    public View getContentView() {
        View contentView = inflater.inflate(R.layout.list_item_notes, null);
        tvNote = (TextView) contentView.findViewById(R.id.tv_note);
        btDelete = (ImageButton) contentView.findViewById(R.id.bt_delete);
        btDelete.setOnClickListener(this);
        return contentView;
    }

    @Override
    public void onClick(View v) {
        notesItemClickListener.onDeleteNotesClicked(objData);
    }
}
