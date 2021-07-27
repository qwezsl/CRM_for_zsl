package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.imp.UserServiceimp;
import com.bjpowernode.crm.utils.*;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.imp.ActivityServiceimp;

import com.bjpowernode.crm.VO.selectVO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class ActivityServlet extends HttpServlet {



    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String Path = request.getServletPath();

        if ("/workbench/Activity/getUserList.do".equals(Path)){
              getUserList(response);
        }else if ("/workbench/Activity/save.do".equals(Path)){
                    save(request,response);
        }else if("/workbench/Activity/pageList.do".equals(Path)){
                    pageList(request,response);
        }else if("/workbench/Activity/delete.do".equals(Path)){
            delete(request,response);
        }else if("/workbench/Activity/edit.do".equals(Path)){
            edit(request,response);
        }else if("/workbench/Activity/update.do".equals(Path)){
            update(request,response);
        }else if("/workbench/activity/detail.do".equals(Path)){
            detail(request,response);
        }else if("/workbench/activity/showActivityRemarkByID.do".equals(Path)){
            showArByID(request,response);
        }else if("/workbench/activity/removeRemark.do".equals(Path)){
            removeRemark(request,response);
        }else if("/workbench/activity/saveRemark.do".equals(Path)){
            saveRemark(request,response);
        }else if("/workbench/activity/editRemark.do".equals(Path)){
            editRemark(request,response);
        }




    }



    private void getUserList(HttpServletResponse response) {
        UserService u= (UserService) ServiceFactory.getService(new UserServiceimp());

        List<User> list=u.getName();
        PrintJson.printJsonObj(response,list);
    }



    private void save(HttpServletRequest request, HttpServletResponse response) {
        ActivityService  act= (ActivityService) ServiceFactory.getService(new ActivityServiceimp());
//id,owner,name,startDate,endDate,cost,description
        Activity activity=new Activity();
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");

        String createTime=DateTimeUtil.getSysTime();
        String createBy=((User) request.getSession().getAttribute("user")).getName();

        String uuid = UUIDUtil.getUUID();
        activity.setId(uuid);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setCreateTime(createTime);
        activity.setCreateBy(createBy);
        boolean b=act.add(activity);
        PrintJson.printJsonFlag(response,b);
    }


    private void pageList(HttpServletRequest request, HttpServletResponse response) {
        ActivityService  act= (ActivityService) ServiceFactory.getService(new ActivityServiceimp());
        String pageNo=request.getParameter("pageNo");
        int pageNo2=Integer.parseInt(pageNo);
        String pageSize = request.getParameter("pageSize");
        int pageSize2=Integer.parseInt(pageSize);
        //计算出当前所在页数
        int pageCount=(pageNo2-1)*pageSize2;

        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");

        System.out.println(name);

        Map<String,Object> map=new HashMap<>();
        map.put("pageCount",pageCount); //当前所在页数
        map.put("pageSize2",pageSize2);//当前页条数
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);


         selectVO<Activity> vo=act.selectActivity(map);
         //传map,返回一个VO,一个是查到的记录条，一个是前端需要的数据
         PrintJson.printJsonObj(response,vo);

    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {
        String[] ids = request.getParameterValues("id");


        ActivityService  act= (ActivityService) ServiceFactory.getService(new ActivityServiceimp());
        boolean b=act.delete(ids);

        PrintJson.printJsonFlag(response,b);
    }

    private void edit(HttpServletRequest request, HttpServletResponse response) {
        ActivityService  act= (ActivityService) ServiceFactory.getService(new ActivityServiceimp());
      String id=request.getParameter("id");

        // [ ulist:{xx:...}, activity:{xxxxxxxx} ]
        Map<String,Object> map=act.getUserListAndActivity(id);
        PrintJson.printJsonObj(response,map);

    }

    private void update(HttpServletRequest request, HttpServletResponse response) {
        ActivityService  act= (ActivityService) ServiceFactory.getService(new ActivityServiceimp());

//id,owner,name,startDate,endDate,cost,description
        Activity activity=new Activity();
        String id = request.getParameter("id");
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");




        activity.setId(id);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setEditTime(DateTimeUtil.getSysTime());//修改时间
        String editBy=((User)request.getSession().getAttribute("user")).getName();
        activity.setEditBy(editBy);

        boolean b=act.updateByActivity(activity);
        PrintJson.printJsonFlag(response,b);

    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActivityService  act= (ActivityService) ServiceFactory.getService(new ActivityServiceimp());

        String id = request.getParameter("id");
            Activity activity=act.detailGetActivity(id);

            request.setAttribute("activity",activity);

            request.getRequestDispatcher("/workbench/activity/detail.jsp").forward(request,response);
    }


    private void showArByID(HttpServletRequest request, HttpServletResponse response) {
        ActivityService  act= (ActivityService) ServiceFactory.getService(new ActivityServiceimp());
        String id = request.getParameter("id");
       List<ActivityRemark>  ar =act.getAr(id);

        PrintJson.printJsonObj(response,ar);

    }

    private void removeRemark(HttpServletRequest request, HttpServletResponse response) {
        ActivityService  act= (ActivityService) ServiceFactory.getService(new ActivityServiceimp());
        String id=request.getParameter("id");

        boolean b=act.removeRemark(id);
        PrintJson.printJsonFlag(response,b);
    }

    private void saveRemark(HttpServletRequest request, HttpServletResponse response) {
        ActivityService  act= (ActivityService) ServiceFactory.getService(new ActivityServiceimp());
        String noteContent = request.getParameter("noteContent");
        String activityId = request.getParameter("activityId");


        ActivityRemark a=new ActivityRemark();

        String uuid = UUIDUtil.getUUID();

        a.setId(uuid);
        a.setNoteContent(noteContent);
        a.setCreateTime(DateTimeUtil.getSysTime());
        String user = ((User) request.getSession().getAttribute("user")).getName();
        a.setCreateBy(user);
        a.setEditFlag("0");
        a.setActivityId(activityId);


        boolean b=act.saveRemark(a);


        Map<String,Object> map=new HashMap<>();
        map.put("success",b);
        map.put("ar",a);
        PrintJson.printJsonObj(response,map);
    }

    private void editRemark(HttpServletRequest request, HttpServletResponse response) {
        ActivityService  act= (ActivityService) ServiceFactory.getService(new ActivityServiceimp());
        String id = request.getParameter("id");
        String noteContent = request.getParameter("noteContent");
        System.out.println(id);
        ActivityRemark a=new ActivityRemark();

        a.setId(id);
        a.setNoteContent(noteContent);
        a.setEditFlag("1");
        a.setEditTime(DateTimeUtil.getSysTime());
        String user = ((User) request.getSession().getAttribute("user")).getName();
        a.setEditBy(user);

        boolean b=act.updateByActivityRemark(a);

        Map<String,Object> map = new HashMap<>();
        map.put("ar",a);
        map.put("success",b);
        PrintJson.printJsonObj(response,map);
    }

}
