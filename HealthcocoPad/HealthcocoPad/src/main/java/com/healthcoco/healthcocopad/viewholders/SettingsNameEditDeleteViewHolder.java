package com.healthcoco.healthcocopad.viewholders;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.UserGroups;
import com.healthcoco.healthcocopad.enums.SettingsItemType;
import com.healthcoco.healthcocopad.listeners.NameEditDeleteListener;

/**
 * Created by neha on 15/01/16.
 */
public class SettingsNameEditDeleteViewHolder extends HealthCocoViewHolder implements View.OnClickListener {
    private SettingsItemType itemType;
    private NameEditDeleteListener editDeleteListener;
    private HealthCocoActivity mActivity;
    private Object objData;
    private TextView tvName;
    private ImageButton btEdit;
    private ImageButton btDelete;

    public SettingsNameEditDeleteViewHolder(HealthCocoActivity mActivity, NameEditDeleteListener editDeleteListener, SettingsItemType itemType) {
        super(mActivity);
        this.mActivity = mActivity;
        this.editDeleteListener = editDeleteListener;
        this.itemType = itemType;
    }

    @Override
    public void setData(Object object) {
        this.objData = object;
    }

    @Override
    public void applyData() {
        String text = "";
        String uniqueId = "";
        switch (itemType) {
            case GROUPS:
                if (objData instanceof UserGroups) {
                    UserGroups groups = (UserGroups) objData;
                    uniqueId = groups.getUniqueId();
                    text = groups.getName();
                }
                break;
        }
        tvName.setText(text);
    }

    @Override
    public View getContentView() {
        View contentView = inflater.inflate(R.layout.item_name_edit_delete, null);
        tvName = (TextView) contentView.findViewById(R.id.tv_name);
        btEdit = (ImageButton) contentView.findViewById(R.id.bt_edit);
        btDelete = (ImageButton) contentView.findViewById(R.id.bt_delete);
        btEdit.setOnClickListener(this);
        btDelete.setOnClickListener(this);
        return contentView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_edit) {
            editDeleteListener.onEditClicked(itemType, objData);
        } else if (v.getId() == R.id.bt_delete) {
            editDeleteListener.onDeleteClicked(itemType, objData);
        }
    }
}
