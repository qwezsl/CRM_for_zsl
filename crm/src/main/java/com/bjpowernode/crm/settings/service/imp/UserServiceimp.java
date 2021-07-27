package com.bjpowernode.crm.settings.service.imp;

import com.bjpowernode.crm.settings.dao.UserDAO;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.exception.loginException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceimp  implements UserService {

    private UserDAO userdao= SqlSessionUtil.getSqlSession().getMapper(UserDAO.class);

    @Override
    public User login(String loginAct, String loginPwd, String ip) throws loginException {
        Map<String,Object> map=new HashMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);
        User user=userdao.login(map);
        System.out.println(ip);
        if (user==null) {
            throw new loginException("账号密码错误");
        }

        if (user.getExpireTime().compareTo(DateTimeUtil.getSysTime()) < 0)
        {
            throw new loginException("账号失效");
        }

        if ("0".equals(user.getLockState()))
        {
            throw new loginException("账号锁定中");
        }

        if (!user.getAllowIps().contains(ip)){
            throw new loginException("ip受限！");
        }

        return user;
    }

    @Override
    public List<User> getName() {
        List<User>list=userdao.getName();
        return list;
    }
}
