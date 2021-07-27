package com.bjpowernode.crm.web.Filter;

import com.bjpowernode.crm.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class loginFilter implements Filter {
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {

        HttpServletRequest request= (HttpServletRequest) req;
        HttpServletResponse response= (HttpServletResponse) resp;
        System.out.println(request.getContextPath());
        String servletPath = request.getServletPath();
        if ("/login.jsp".equals(servletPath) || "/settings/user/save.do".equals(servletPath)){
            chain.doFilter(req,resp);
        }else {

             User user = (User) request.getSession().getAttribute("user");

             if (user!=null){

                 chain.doFilter(req,resp);
             }else {

                 response.sendRedirect(request.getContextPath()+"/login.jsp");

             }


        }

    }

}
