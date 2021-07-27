package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkDAO {
    void deleteCount(String[] ids);

    List<ActivityRemark> getAr(String id);

    int removeRemark(String id);

    int saveRemark(ActivityRemark a);


    int updateByActivityRemark(ActivityRemark a);
}
