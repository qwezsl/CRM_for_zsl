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


        //????????????????????????????????????????????????  ???????????????????????????????????????name???a value???b  ???"b".equals()??????
        //??????????????????????????????????????????????????????domain Tran???????????????????????????

        //?????????????????????????????? ???????????????????????????
        //??????????????? ???????????????????????? ?????????Tran????????????

        //(?????????????????????????????????????????????????????????????????????Tran????????????,????????????if Tran!=null ?????????)
        String a = request.getParameter("a");
        String createBy = ((User) request.getSession().getAttribute("user")).getName();

        Tran tran=null;//??????t??????null ??????????????? ??????????????????(????????????????????????)

        if ("b".equals(a)){ //???????????????????????????????????????Tran????????????
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


        //??????convert???????????????true??????????????????(???????????????????????????,???????????????????????????)
        ClueService cs= (ClueService) ServiceFactory.getService(new ClueServiceimp());
        boolean b=cs.convert(clueId,tran,createBy);  //??????????????????
                                      // 1.clueId,?????????????????????????????????????????????
                                      //2.Tran??????,????????????????????????????????????????????????tran???????????????
                                       //3.createBy,?????????????????????????????????request???session?????????user??????
                                             //???createTime ??? ??????id ??????????????????????????????????????????

        if (b){
            response.sendRedirect(request.getContextPath()+"workbench/clue/index.jsp");
            //??????????????????????????????????????????
        }

    }

}
