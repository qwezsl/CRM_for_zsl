package com.bjpowernode.crm.settings.dao;

import com.bjpowernode.crm.settings.domain.User;

import java.util.List;
import java.util.Map;

public interface UserDAO {

    User login(Map<String, Object> map);

    List<User> getName();
}
