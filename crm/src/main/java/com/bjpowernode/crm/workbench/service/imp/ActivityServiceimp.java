package com.bjpowernode.crm.workbench.service.imp;

import com.bjpowernode.crm.VO.selectVO;
import com.bjpowernode.crm.settings.dao.UserDAO;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.workbench.dao.ActivityDAO;
import com.bjpowernode.crm.workbench.dao.ActivityRemarkDAO;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityServiceimp implements ActivityService {

   ActivityDAO dao= SqlSessionUtil.getSqlSession().getMapper(ActivityDAO.class);
    ActivityRemarkDAO dao2=SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDAO.class);

    @Override
    public boolean add(Activity activity) {

        int a=dao.add(activity);  //insert方法一定会返回修改条数
        //开始判断
        boolean b=true;
        if (a!=1)
        {
            b=false;
        }
        return b;
    }


    @Override
    public selectVO<Activity> selectActivity(Map<String, Object> map) {

                int count=dao.selectCount(map);//得到一个查询记录条数
                List<Activity> dataList=dao.selectdate(map);//得到一个List<Activity>


                selectVO<Activity> s=new selectVO();
                s.setCount(count);
                s.setDataList(dataList);

        return s;
    }

    @Override
    public boolean delete(String[] ids) {

            dao2.deleteCount(ids);//含外键 先删子，再删父
            dao.delete(ids);

        return true;
    }

    @Override
    public Map<String, Object> getUserListAndActivity(String id) {
        UserDAO userDAO=SqlSessionUtil.getSqlSession().getMapper(UserDAO.class);
        List<User> ulist =  userDAO.getName(); //User那边已经查询过了所有用户信息
        Activity activity=dao.getActivity(id);

        Map<String,Object> map=new HashMap<>();
        map.put("ulist",ulist);
        map.put("activity",activity);

        return map;
    }

    @Override
    public boolean updateByActivity(Activity activity) {

        int i=dao.updateByActivity(activity);
        boolean b=true;
        if (i!=1)
        {
            b=false;
        }

        return b;
    }

    @Override
    public Activity detailGetActivity(String id) {

        Activity activity=dao.detailGetActivity(id);

        return activity;
    }

    @Override
    public List<ActivityRemark> getAr(String id) {

       List<ActivityRemark>  ar=dao2.getAr(id);

        return ar;
    }

    @Override
    public boolean removeRemark(String id) {
        boolean b=true;

        int i=dao2.removeRemark(id);

        if (i!=1){
            b=false;
        }
        return b;
    }

    @Override
    public boolean saveRemark(ActivityRemark a) {

        boolean b=true;

        int i=dao2.saveRemark(a);

        if (i!=1){
            b=false;
        }
        return b;
    }

    @Override
    public boolean updateByActivityRemark(ActivityRemark a) {

        boolean b=true;

        int i=dao2.updateByActivityRemark(a);
        System.out.println("------------"+i);
        if (i!=1){
            b=false;
        }
        return b;
    }


    @Override
    public List<Activity> GetActivityByName_And_NotByClueId(Map<String, String> map) {

        List<Activity> list=dao.GetActivityByName_And_NotByClueId(map);

        return list;
    }

    @Override
    public List<Activity> GetActivityByName(String sname) {
   List<Activity> list= dao.GetActivityByName(sname);


        return list;
    }


}
