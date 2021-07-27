package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranService {


    boolean insertTranT(Tran t, String customerName);

    List<Tran> showTran();

    Tran selectTran(String tranid);

    Map<String, Object> changeStage(Tran t);

    Map<String, Object> TranECharts();
}
