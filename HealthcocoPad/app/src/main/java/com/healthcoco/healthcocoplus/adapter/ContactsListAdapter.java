package com.healthcoco.healthcocoplus.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.healthcoco.healthcocoplus.HealthCocoActivity;
import com.healthcoco.healthcocoplus.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocoplus.fragments.ContactsListFragment;
import com.healthcoco.healthcocoplus.listeners.ContactsItemOptionsListener;
import com.healthcoco.healthcocoplus.utilities.LogUtils;
import com.healthcoco.healthcocoplus.utilities.Util;
import com.healthcoco.healthcocoplus.viewholders.ContactsListViewHolder;

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
    private ContactsListViewHolder holder;


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
        LogUtils.LOGD(TAG, "Success getView " + position);
        RegisteredPatientDetailsUpdated patient = (RegisteredPatientDetailsUpdated) getItem(position);
        boolean loadImage;
        if (convertView == null) {
            holder = new ContactsListViewHolder(mActivity, contactsItemOptionsListener, position);
            convertView = holder.getConvertView();
            convertView.setTag(holder);
            loadImage = true;
        } else {
            holder = (ContactsListViewHolder) convertView.getTag();
            loadImage = false;
        }
        holder.setData(patient);
        holder.applyData();
        return convertView;
    }
}
