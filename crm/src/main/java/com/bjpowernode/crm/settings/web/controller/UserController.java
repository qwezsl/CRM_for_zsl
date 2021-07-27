package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.imp.UserServiceimp;
import com.bjpowernode.crm.utils.MD5Util;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String Path = request.getServletPath();

        if ("/settings/user/save.do".equals(Path)){
            save(request,response);
        }else if ("/settings/user/xxx.do".equals(Path)){

        }
    }

    private void save(HttpServletRequest request, HttpServletResponse response) {

        String loginAct=request.getParameter("loginAct");
        System.out.println(loginAct);
        String loginPwd = request.getParameter("loginPwd");
        System.out.println(loginPwd);
         loginPwd = MD5Util.getMD5(loginPwd);
        System.out.println("sbbbb22233333");
        String ip = request.getRemoteAddr();


        UserService us= (UserService) ServiceFactory.getService(new UserServiceimp());

        try{
            User user=us.login(loginAct,loginPwd,ip);// 传进dao层做验证
            request.getSession().setAttribute("user",user);//下次登录不用输入账号密码
            PrintJson.printJsonFlag(response,true); //密码正确返回
        }catch (Exception e){
            e.printStackTrace();
            //肯定是以密码错误为基础，因为密码错误才能走到span
            //两个值没有对应的javabean, 所以定义一个map来装success和msg异常信息
            Map<String,Object> map=new HashMap<>();
            map.put("success",false);

            String msg = e.getMessage();
            map.put("msg",msg);     //具体的异常信息交给业务层处理,由他们上抛此处

            PrintJson.printJsonObj(response,map);

        }

    }
}
