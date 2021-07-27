package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.dao.UserDAO;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.imp.UserServiceimp;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.dao.CustomerDao;
import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.service.CustomerService;
import com.bjpowernode.crm.workbench.service.TranHistoryService;
import com.bjpowernode.crm.workbench.service.TranService;
import com.bjpowernode.crm.workbench.service.imp.ClueServiceimp;
import com.bjpowernode.crm.workbench.service.imp.CustomerServiceimp;
import com.bjpowernode.crm.workbench.service.imp.TranHistoryServiceimp;
import com.bjpowernode.crm.workbench.service.imp.TranServiceimp;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class TranServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String Path = request.getServletPath();


        if ("/workbench/transaction/save.do".equals(Path)){
                 saveTran(request,response);

        }else if ("/workbench/transaction/getCustomerName.do".equals(Path)){
            getCustomerName(request,response);
        }else if ("/workbench/transaction/insert.do".equals(Path)){
            insertTran(request,response);
        }else if ("/workbench/transaction/showTran.do".equals(Path)){
            showTran(request,response);
        }else if ("/workbench/transaction/detail.do".equals(Path)){
            detail(request,response);
        }else if ("/workbench/transaction/showTranHistory.do".equals(Path)){
            showTranHistory(request,response);
        }else if ("/workbench/transaction/changeStage.do".equals(Path)){
            changeStage(request,response);
        }else if ("/workbench/chart/TranECharts.do".equals(Path)){
            TranECharts(request,response);
        }


    }




    private void saveTran(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserService user= (UserService) ServiceFactory.getService(new UserServiceimp());

        List<User> userList = user.getName();
        request.setAttribute("user",userList);
      request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request,response);

    }


    private void getCustomerName(HttpServletRequest request, HttpServletResponse response) {

        String name = request.getParameter("name");
        CustomerService customerService= (CustomerService) ServiceFactory.getService(new CustomerServiceimp());
        List<String> list=customerService.getCustomerName(name);

        PrintJson.printJsonObj(response,list);

    }


    private void insertTran(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = UUIDUtil.getUUID();
        String owner=request.getParameter("tran-owner");
        String money=request.getParameter("tran-money");
        String name=request.getParameter("tran-name");
        String expectedDate=request.getParameter("tran-expectedDate");
        String customerName = request.getParameter("tran-customerName");
        String stage=request.getParameter("tran-stage");
        String type=request.getParameter("tran-type");
        String source=request.getParameter("tran-source");
        String activityId=request.getParameter("tran-activity");
        String contactsId=request.getParameter("tran-contactsName");
        String createBy= ((User)request.getSession().getAttribute("user")).getName();
        String createTime=DateTimeUtil.getSysTime();
        String description=request.getParameter("tran-description");
        String contactSummary = request.getParameter("tran-contactSummary");
        String nextContactTime = request.getParameter("tran-nextTime");

        Tran t=new Tran();
        t.setId(uuid); t.setOwner(owner); t.setMoney(money); t.setName(name); t.setExpectedDate(expectedDate);
        t.setStage(stage); t.setType(type); t.setSource(source); t.setActivityId(activityId);
        t.setContactsId(contactsId); t.setCreateBy(createBy); t.setDescription(description); t.setNextContactTime(nextContactTime);
        t.setCreateTime(createTime); t.setContactSummary(contactSummary);



        TranService tranService= (TranService) ServiceFactory.getService(new TranServiceimp());
        tranService.insertTranT(t,customerName);

        response.sendRedirect(request.getContextPath()+"/workbench/transaction/index.jsp");

    }


    private void showTran(HttpServletRequest request, HttpServletResponse response) {

       TranService ts= (TranService) ServiceFactory.getService(new TranServiceimp());
        List<Tran> list=ts.showTran();
        PrintJson.printJsonObj(response,list);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String tranid = request.getParameter("tranid");
        TranService ts= (TranService) ServiceFactory.getService(new TranServiceimp());
        Tran tran=ts.selectTran(tranid);

        String stage = tran.getStage();
        ServletContext servletContext = request.getServletContext();
        Map<String,String> pmap= (Map<String, String>) servletContext.getAttribute("pmap");
        String kenengxing = pmap.get(stage);

        tran.setKenengxing(kenengxing);
        request.setAttribute("tran",tran);
        request.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(request,response);

    }

    private void showTranHistory(HttpServletRequest request, HttpServletResponse response) {

        String tranid = request.getParameter("tranid");
        TranHistoryService th= (TranHistoryService) ServiceFactory.getService(new TranHistoryServiceimp());
        List<TranHistory> tranHistorylist =th.showTranHistory(tranid);



        ServletContext servletContext = request.getServletContext();
        Map<String,String> pmap = (Map<String, String>) servletContext.getAttribute("pmap");
        for (TranHistory tranHistory:tranHistorylist) {
            String stage = tranHistory.getStage();
            tranHistory.setKenengxing(pmap.get(stage));
            System.out.println(tranHistory.getStage());

        }
        PrintJson.printJsonObj(response,tranHistorylist);

    }

    private void changeStage(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        String stage = request.getParameter("stage");
        String money = request.getParameter("money");
        String expectedDate = request.getParameter("expectedDate");

        String editBy=((User)request.getSession().getAttribute("user")).getName();
        String editTime=DateTimeUtil.getSysTime();
        Tran t=new Tran();
        t.setId(id);
        t.setStage(stage);
        t.setMoney(money);
        t.setExpectedDate(expectedDate);
        t.setEditBy(editBy);
        t.setEditTime(editTime);

        Map<String,String> pmap = (Map<String, String>) request.getServletContext().getAttribute("pmap");
        String kenengxing = pmap.get(stage);
        t.setKenengxing(kenengxing);

        TranService ts= (TranService) ServiceFactory.getService(new TranServiceimp());
        Map<String,Object> map=ts.changeStage(t);

        PrintJson.printJsonObj(response,map);

    }

    private void TranECharts(HttpServletRequest request, HttpServletResponse response) {

        TranService tranService= (TranService) ServiceFactory.getService(new TranServiceimp());

        Map<String,Object> map=tranService.TranECharts();
        PrintJson.printJsonObj(response,map);

    }

}
