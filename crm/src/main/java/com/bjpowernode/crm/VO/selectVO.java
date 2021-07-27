package com.bjpowernode.crm.VO;

import java.util.List;

public class selectVO<T> {

    private int count; //查询到的记录条数
    private List<T> dataList;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }
}
