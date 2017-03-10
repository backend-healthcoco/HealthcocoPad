package com.healthcoco.healthcocoplus.utilities;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by Shreshtha on 10-03-2017.
 */

public class ListGridViewSizeHelper {

    private static final String TAG = ListGridViewSizeHelper.class.getSimpleName();

    public static int getListViewSize(ListView myListView) {
        ListAdapter myListAdapter = myListView.getAdapter();
        if (myListAdapter == null) {
            //do nothing return null
            return 0;
        }
        //set listAdapter in loop for getting final size
        int totalHeight = 0;
        for (int size = 0; size < myListAdapter.getCount(); size++) {
            View listItem = myListAdapter.getView(size, null, myListView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
            LogUtils.LOGD(TAG, "Height " + totalHeight);
        }
        //setting listview item in adapter
        ViewGroup.LayoutParams params = myListView.getLayoutParams();
        params.height = totalHeight + (myListView.getDividerHeight() * (myListAdapter.getCount() - 1));
        myListView.setLayoutParams(params);
        // print height of adapter on log
        Log.i("height of listItem:", String.valueOf(totalHeight));
        return totalHeight;
    }

    public static int getListViewSize(GridView gridView) {
        ListAdapter myListAdapter = gridView.getAdapter();
        if (myListAdapter == null) {
            //do nothing return null
            return 0;
        }
        //set listAdapter in loop for getting final size
        int totalHeight = 0;
        for (int size = 0; size < myListAdapter.getCount(); size++) {
            View listItem = myListAdapter.getView(size, null, gridView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
            LogUtils.LOGD(TAG, "Height " + totalHeight);
        }
        //setting listview item in adapter
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight + (gridView.getHorizontalSpacing() * (myListAdapter.getCount() - 1));
        gridView.setLayoutParams(params);
        // print height of adapter on log
        Log.i("height of listItem:", String.valueOf(totalHeight));
        return totalHeight;
    }
    public static int getGridViewHieght(){
       return 0;
    }
}
