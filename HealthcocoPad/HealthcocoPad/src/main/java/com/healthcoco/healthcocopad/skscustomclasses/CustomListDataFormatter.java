package com.healthcoco.healthcocopad.skscustomclasses;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;


public class CustomListDataFormatter {

    static int headerLength;
    static List<CustomListData> dataList;

//    public static List<Pair<String, List<?>>> getAllData(
//            List<CustomListData> listData) {
//        dataList = listData;
//        List<Pair<String, List<?>>> res = new ArrayList<Pair<String, List<?>>>();
//
//        for (int i = 0; i < dataList.size(); i++) {
//            res.add(getOneSectionFromHeaderListHashMap(i));
//        }
//        return res;
//    }

    public static List<Pair<String, List<?>>> getAllDataFromHeaderDataList(
            List<CustomListData> listData) {
        dataList = listData;
        List<Pair<String, List<?>>> res = new ArrayList<Pair<String, List<?>>>();

        for (int i = 0; i < dataList.size(); i++) {
            res.add(getOneSectionFromHeaderList(i));
        }
        return res;
    }

    public static List<Pair<CustomListData, List<?>>> getAllDataFromHeaderDataListHashMap(
            List<CustomListData> listData) {
        dataList = listData;
        List<Pair<CustomListData, List<?>>> res = new ArrayList<Pair<CustomListData, List<?>>>();

        for (int i = 0; i < dataList.size(); i++) {
            res.add(getOneSectionFromHeaderListHashMap(i));
        }
        return res;
    }

    public static Pair<String, List<?>> getOneSectionFromHeaderList(int index) {
        return new Pair<String, List<?>>(dataList.get(index).getHeader(),
                dataList.get(index).getHeaderDataList());
    }

    public static Pair<CustomListData, List<?>> getOneSectionFromHeaderListHashMap(int index) {
        return new Pair<CustomListData, List<?>>(dataList.get(index),
                new ArrayList<>(dataList.get(index).getHeaderDataListHashMap().values()));
    }
}
