package com.bjpowernode.crm.workbench.service.imp;

import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.workbench.dao.TranHistoryDao;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.service.TranHistoryService;

import java.util.List;

public class TranHistoryServiceimp implements TranHistoryService {
    TranHistoryDao thd= SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);

    @Override
    public List<TranHistory> showTranHistory(String tranid) {
       List<TranHistory> tranHistory= thd.showTranHistory(tranid);

        return tranHistory;
    }
}
