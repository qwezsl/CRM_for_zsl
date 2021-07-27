package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.TranHistory;

import java.util.List;

public interface TranHistoryDao {

    int insertTHD(TranHistory tr);

    List<TranHistory> showTranHistory(String tranid);


}
