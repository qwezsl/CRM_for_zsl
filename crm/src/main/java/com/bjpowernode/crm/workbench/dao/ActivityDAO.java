package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityDAO {

    int add(Activity activity);



    int selectCount(Map<String, Object> map);
    List<Activity> selectdate(Map<String, Object> map);

    int delete(String[] ids);


    Activity getActivity(String id);

    int updateByActivity(Activity activity);

    Activity detailGetActivity(String id);





    List<Activity> Get_Clue_and_activity(String id);

    List<Activity> GetActivityByName_And_NotByClueId(Map<String, String> map);

    List<Activity> GetActivityByName(String sname);
}
