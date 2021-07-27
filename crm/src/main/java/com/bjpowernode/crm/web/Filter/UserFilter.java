package com.bjpowernode.crm.web.Filter;

import javax.servlet.*;
import java.io.IOException;

public class UserFilter implements Filter {

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        chain.doFilter(req, resp);
    }

}
