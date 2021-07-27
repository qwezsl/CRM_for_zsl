package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.VO.selectVO;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

public interface ActivityService {

    boolean add(Activity activity);

    selectVO<Activity> selectActivity(Map<String, Object> map);

    boolean delete(String[] ids);


    Map<String, Object> getUserListAndActivity(String id);


    boolean updateByActivity(Activity activity);

    Activity detailGetActivity(String id);

    List<ActivityRemark> getAr(String id);


    boolean removeRemark(String id);

    boolean saveRemark(ActivityRemark a);


    boolean updateByActivityRemark(ActivityRemark a);

    List<Activity> GetActivityByName_And_NotByClueId(Map<String, String> map);

    List<Activity> GetActivityByName(String sname);
}
