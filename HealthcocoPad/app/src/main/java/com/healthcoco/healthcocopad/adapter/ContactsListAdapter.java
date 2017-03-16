package com.healthcoco.healthcocopad.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.listeners.ContactsItemOptionsListener;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.ContactsGridViewHolder;
import com.healthcoco.healthcocopad.viewholders.ContactsListViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shreshtha on 2/28/2017.
 */
public class ContactsListAdapter extends BaseAdapter {
    private static final String TAG = ContactsListAdapter.class.getSimpleName();
    private ContactsItemOptionsListener contactsItemOptionsListener;
    private List<RegisteredPatientDetailsUpdated> mContacts;
    private HealthCocoActivity mActivity;
    private HealthCocoViewHolder healthCocoViewHolder;

    public ContactsListAdapter(HealthCocoActivity mActivity, ContactsItemOptionsListener contactsItemOptionsListener) {
        this.mActivity = mActivity;
        this.contactsItemOptionsListener = contactsItemOptionsListener;
    }

    public void setListData(final List<RegisteredPatientDetailsUpdated> contacts) {
        this.mContacts = contacts;
    }

    private String[] generateContactNames(final List<RegisteredPatientDetailsUpdated> contacts) {
        final ArrayList<String> contactNames = new ArrayList<String>();
        if (contacts != null)
            for (final RegisteredPatientDetailsUpdated contactEntity : contacts) {
                contactNames.add(contactEntity.getLocalPatientName().trim());
            }
        return contactNames.toArray(new String[contactNames.size()]);
    }

    @Override
    public int getCount() {
        if (Util.isNullOrEmptyList(mContacts))
            return 0;
        return mContacts.size();
    }

    @Override
    public Object getItem(int position) {
        return mContacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (contactsItemOptionsListener.getChangedViewType() != null) {
            LogUtils.LOGD(TAG, "Success getView " + position);
            RegisteredPatientDetailsUpdated patient = (RegisteredPatientDetailsUpdated) getItem(position);
            boolean loadImage;
            if (convertView == null) {
                switch (contactsItemOptionsListener.getChangedViewType()) {
                    case GRID_VIEW:
                        healthCocoViewHolder = new ContactsGridViewHolder(mActivity, contactsItemOptionsListener, position);
                        convertView = healthCocoViewHolder.getContentView();
                        convertView.setTag(healthCocoViewHolder);
                        break;
                    case LIST_VIEW:
                        healthCocoViewHolder = new ContactsListViewHolder(mActivity, contactsItemOptionsListener, position);
                        convertView = healthCocoViewHolder.getContentView();
                        convertView.setTag(healthCocoViewHolder);
                        break;
                }
                loadImage = true;
            } else {
                healthCocoViewHolder = (HealthCocoViewHolder) convertView.getTag();
            }
            loadImage = false;
            healthCocoViewHolder.setData(patient);
            healthCocoViewHolder.applyData();
        }
        return convertView;
    }
}
