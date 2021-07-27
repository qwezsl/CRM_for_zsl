package com.bjpowernode.crm.web.Listener;

import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.service.DicService;
import com.bjpowernode.crm.settings.service.imp.DicServiceimp;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.Resources;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSessionBindingEvent;
import java.io.IOException;
import java.util.*;

public class Sys_Init_Listener implements ServletContextListener{
    // Public constructor is required by servlet spec
    public Sys_Init_Listener() {
    }

    public void contextInitialized(ServletContextEvent sce) {
      //域对象初始化时触发
        ServletContext servletContext = sce.getServletContext();
        DicService ds= (DicService) ServiceFactory.getService(new DicServiceimp());

            Map<String, List<DicValue>> map=ds.getAllDic();
        Set<String> strings = map.keySet();
        for (String keys:strings) {

            servletContext.setAttribute(keys, map.get(keys));
                                    //Typecode s, List<DicValue> s
        }

        Map<String,String> map1=new HashMap<>();
        ResourceBundle r=ResourceBundle.getBundle("Stage2Possibility");
        Enumeration<String> keys = r.getKeys();
        while (keys.hasMoreElements()){
            String key = keys.nextElement();
            String value = r.getString(key);
            map1.put(key,value);
        }

        servletContext.setAttribute("pmap",map1);
    }

    public void contextDestroyed(ServletContextEvent sce) {
        //域对象被销毁时触发
    }

}
