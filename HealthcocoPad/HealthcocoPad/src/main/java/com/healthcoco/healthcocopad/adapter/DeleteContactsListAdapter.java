package com.healthcoco.healthcocopad.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsNew;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.listeners.ContactsItemOptionsForDeletedListener;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.AppointmentTimeSlotViewHolder;
import com.healthcoco.healthcocopad.viewholders.ContactsGridViewHolder;
import com.healthcoco.healthcocopad.viewholders.ContactsListViewForDeletedPatientHolder;

import java.util.ArrayList;
import java.util.List;

public class DeleteContactsListAdapter extends BaseAdapter {
    private static final String TAG = ContactsListAdapter.class.getSimpleName();
    private ContactsItemOptionsForDeletedListener contactsItemOptionsListener;
    private List<RegisteredPatientDetailsNew> mContacts;
    private HealthCocoActivity mActivity;
    private ContactsListViewForDeletedPatientHolder healthCocoViewHolder;

    public DeleteContactsListAdapter(HealthCocoActivity mActivity, ContactsItemOptionsForDeletedListener contactsItemOptionsListener) {
        this.mActivity = mActivity;
        this.contactsItemOptionsListener = contactsItemOptionsListener;
    }

    public void setListData(final List<RegisteredPatientDetailsNew> contacts) {
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
        if (convertView == null) {
            healthCocoViewHolder = new ContactsListViewForDeletedPatientHolder(mActivity,contactsItemOptionsListener);
            convertView = healthCocoViewHolder.getContentView();
            convertView.setTag(healthCocoViewHolder);
        } else
            healthCocoViewHolder = (ContactsListViewForDeletedPatientHolder) convertView.getTag();
        healthCocoViewHolder.setData(mContacts.get(position));
        healthCocoViewHolder.applyData();
        return convertView;
    }
}