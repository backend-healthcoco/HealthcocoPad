package com.healthcoco.healthcocopad.skscustomclasses;

import java.util.LinkedHashMap;
import java.util.List;

public class CustomListData {
    private String header;
    private List<?> headerDataList;
    private LinkedHashMap<String, Object> headerDataListHashMap;
    private boolean isSelected;

    public CustomListData() {

    }

    public CustomListData(String header, List<?> listData) {
        this.header = header;
        this.headerDataList = listData;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public List<?> getHeaderDataList() {
        return headerDataList;
    }

    public void setHeaderDataList(List<?> headerDataList) {
        this.headerDataList = headerDataList;
    }


    public LinkedHashMap<String, Object> getHeaderDataListHashMap() {
        return headerDataListHashMap;
    }

    public void setHeaderDataListHashMap(LinkedHashMap<String, Object> headerDataListHashMap) {
        this.headerDataListHashMap = headerDataListHashMap;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
