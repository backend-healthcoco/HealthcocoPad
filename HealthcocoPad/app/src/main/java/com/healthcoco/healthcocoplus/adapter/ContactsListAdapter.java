package com.healthcoco.healthcocoplus.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.BaseAdapter;

import com.healthcoco.healthcocoplus.HealthCocoActivity;
import com.healthcoco.healthcocoplus.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocoplus.enums.ChangeViewType;
import com.healthcoco.healthcocoplus.listeners.ContactsItemOptionsListener;
import com.healthcoco.healthcocoplus.utilities.LogUtils;
import com.healthcoco.healthcocoplus.utilities.Util;
import com.healthcoco.healthcocoplus.viewholders.ContactsGridViewHolder;
import com.healthcoco.healthcocoplus.viewholders.ContactsListViewHolder;
import com.healthcoco.healthcocoplus.views.GridViewLoadMore;

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
    private ContactsGridViewHolder gridViewHolder;
    private ContactsListViewHolder listViewHolder;
    private ChangeViewType changeViewType;

    public ContactsListAdapter(HealthCocoActivity mActivity, ContactsItemOptionsListener contactsItemOptionsListener, ChangeViewType changeViewType) {
        this.mActivity = mActivity;
        this.contactsItemOptionsListener = contactsItemOptionsListener;
        this.changeViewType = changeViewType;
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
            switch (changeViewType) {
                case GRID_VIEW:
                    gridViewHolder = new ContactsGridViewHolder(mActivity, contactsItemOptionsListener, position);
                    convertView = gridViewHolder.getConvertView();
                    convertView.setTag(gridViewHolder);
                    break;
                case LIST_VIEW:
                    listViewHolder = new ContactsListViewHolder(mActivity, contactsItemOptionsListener, position);
                    convertView = listViewHolder.getConvertView();
                    convertView.setTag(listViewHolder);
                    break;
            }
            loadImage = true;
        } else {
            switch (changeViewType) {
                case GRID_VIEW:
                    gridViewHolder = (ContactsGridViewHolder) convertView.getTag();
                    break;
                case LIST_VIEW:
                    listViewHolder = (ContactsListViewHolder) convertView.getTag();
                    break;
            }
        }
        loadImage = false;
        switch (changeViewType) {
            case GRID_VIEW:
                gridViewHolder.setData(patient);
                gridViewHolder.applyData();
                break;
            case LIST_VIEW:
                listViewHolder.setData(patient);
                listViewHolder.applyData();
                break;
        }
        return convertView;
    }
}
