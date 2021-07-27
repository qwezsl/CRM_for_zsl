package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranDao {


    int insertTran(Tran t);

    List<Tran> showTran();

    Tran selectTran(String tranid);

    int changeStage(Tran t);

    int getTotal();

    List<Map<String, Object>> TranECharts();
}
