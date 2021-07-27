package com.bjpowernode.crm.workbench.service.imp;

import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.workbench.dao.CustomerDao;
import com.bjpowernode.crm.workbench.service.CustomerService;

import java.util.List;

public class CustomerServiceimp  implements CustomerService {
    @Override
    public List<String> getCustomerName(String name) {

        CustomerDao customerDao= SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
        List<String> list=customerDao.getCustomerName(name);

        return list;
    }
}
