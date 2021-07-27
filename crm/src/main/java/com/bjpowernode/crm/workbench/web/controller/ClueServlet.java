package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.utils.*;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.ClueService;
import com.bjpowernode.crm.workbench.service.imp.ActivityServiceimp;
import com.bjpowernode.crm.workbench.service.imp.ClueServiceimp;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueServlet extends HttpServlet {



    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String Path = request.getServletPath();

        if ("/workbench/clue/getUserList.do".equals(Path)) {
            getUserList(request,response);
        }else if("/workbench/clue/save.do".equals(Path)) {
            saveClue(request,response);
        }else if("/workbench/clue/detail.do".equals(Path)) {
            getClue(request,response);
        }else if("/workbench/clue/Get_Clue_and_activity.do".equals(Path)) {
            Get_Clue_and_activity(request,response);
        }else if("/workbench/clue/removeActivity.do".equals(Path)) {
            removeActivity(request,response);
        }else if("/workbench/clue/GetActivityByName_And_NotByClueId.do".equals(Path)) {
            GetActivityByName_And_NotByClueId(request,response);
        }else if("/workbench/clue/inputRelation.do".equals(Path)) {
            inputRelation(request,response);
        }else if("/workbench/clue/GetActivityByName.do".equals(Path)) {
            GetActivityByName(request,response);
        }else if("/workbench/clue/convert.do".equals(Path)) {
            convert(request,response);
        }
    }




    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        ClueService cs= (ClueService) ServiceFactory.getService(new ClueServiceimp());

        List<User> userList=cs.getUserList();
        PrintJson.printJsonObj(response,userList);

    }


    private void saveClue(HttpServletRequest request, HttpServletResponse response) {

        ClueService cs= (ClueService) ServiceFactory.getService(new ClueServiceimp());
        String fullname = request.getParameter("fullname");
        String appellation = request.getParameter("appellation");
        String owner = request.getParameter("owner");
        String company = request.getParameter("company");
        String job = request.getParameter("job");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        String source = request.getParameter("source");
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");

        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String  createBy=((User)request.getSession().getAttribute("user")).getName();

        Clue clue=new Clue();
        clue.setFullname    (fullname);
        clue.setAppellation (appellation);
        clue.setOwner       (owner);
        clue.setCompany     (company);
        clue.setJob         (job);
        clue.setEmail       (email);
        clue.setPhone       (phone);
        clue.setWebsite     (website);
        clue.setState       (state);
        clue.setSource      (source);
        clue.setDescription (description);
    clue.setContactSummary  (contactSummary);
   clue.setNextContactTime  (nextContactTime);
        clue.setAddress     (address);
        clue.setId          (id);
       clue.setCreateTime   (createTime);
       clue.setCreateBy     (createBy);
        clue.setMphone(mphone);

       boolean b=cs.saveclue(clue);

    PrintJson.printJsonFlag(response,b);

    }

    private void getClue(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ClueService cs= (ClueService) ServiceFactory.getService(new ClueServiceimp());
        String id = request.getParameter("id");
        Clue clue=cs.getClue(id);


        request.setAttribute("clue",clue);
        request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request,response);

    }


    private void Get_Clue_and_activity(HttpServletRequest request, HttpServletResponse response) {
        ClueService cs= (ClueService) ServiceFactory.getService(new ClueServiceimp());


        String id = request.getParameter("id");
        List<Activity> list=cs.Get_Clue_and_activity(id);
        for (Activity l:list
             ) {
            System.out.println(l.getName());
            System.out.println(l.getOwner());
        }
        PrintJson.printJsonObj(response,list);
    }

    private void removeActivity(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");

        ClueService cs= (ClueService) ServiceFactory.getService(new ClueServiceimp());
        boolean b=cs.removeActivity(id);
    PrintJson.printJsonFlag(response,b);
    }

    private void GetActivityByName_And_NotByClueId(HttpServletRequest request, HttpServletResponse response) {
        ActivityService ac= (ActivityService) ServiceFactory.getService(new ActivityServiceimp());
        String activityName = request.getParameter("activityName");
        String Cid = request.getParameter("Cid");

        Map<String,String> map=new HashMap<>();
        map.put("activityName",activityName);
        map.put("Cid",Cid);
        List<Activity> list=ac.GetActivityByName_And_NotByClueId(map);

        PrintJson.printJsonObj(response,list);

    }

    private void inputRelation(HttpServletRequest request, HttpServletResponse response) throws IOException {

        ClueService cs= (ClueService) ServiceFactory.getService(new ClueServiceimp());
        String cid = request.getParameter("cid");
        String[] aid = request.getParameterValues("aid");



        boolean b=cs.inputRelation(cid,aid);
        System.out.println(b);
        PrintJson.printJsonFlag(response,b);
    }

    private void GetActivityByName(HttpServletRequest request, HttpServletResponse response) {
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceimp());
        String sname = request.getParameter("sname");
        List<Activity> list=as.GetActivityByName(sname);
        PrintJson.printJsonObj(response,list);

    }

    private void convert(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String clueId = request.getParameter("ClueId");


        //如何判断，进来的是表单，还是跳转  ，在表单中再加一个隐藏域，name为a value为b  用"b".equals()判断
        //如果是表单，就说明有更多参数，进行对domain Tran交易实体类进行赋值

        //前端按照复选框来判断 是否勾选创建小交易
        //控制层按照 前端的标记来判断 是否为Tran进行赋值

        //(他俩统一处理的，都是普通请求，除了表单多了一个Tran对象罢了,在业务层if Tran!=null 就是了)
        String a = request.getParameter("a");
        String createBy = ((User) request.getSession().getAttribute("user")).getName();

        Tran tran=null;//如果t不为null 在业务层中 就添加小交易(勾选了创建小交易)

        if ("b".equals(a)){ //这里用标记来判断是否应该对Tran进行赋值
            tran=new Tran();
            tran.setId(UUIDUtil.getUUID());
           tran.setMoney( request.getParameter("money"));
            tran.setName(request.getParameter("convertName"));
            tran.setExpectedDate(request.getParameter("convertDate"));
            tran.setStage(request.getParameter("convertStage"));
           tran. setActivityId( request.getParameter("activityId"));
            tran.setCreateTime(DateTimeUtil.getSysTime());
            tran.setCreateBy(createBy);

        }


        //如果convert的返回值为true，进行重定向(用重定向而不是转发,是为了防止恶意提交)
        ClueService cs= (ClueService) ServiceFactory.getService(new ClueServiceimp());
        boolean b=cs.convert(clueId,tran,createBy);  //必须传的参数
                                      // 1.clueId,业务层才知道为哪条线索进行转换
                                      //2.Tran对象,因为传进的可能是表单，而表单带有tran创建小交易
                                       //3.createBy,因为只有控制层才方便用request取session域中的user对象
                                             //而createTime 和 交易id 可以在业务层内使用工具类生成

        if (b){
            response.sendRedirect(request.getContextPath()+"workbench/clue/index.jsp");
            //当成功转换后，返回线索主页面
        }

    }

}
