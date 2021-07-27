package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Customer;

import java.util.List;

public interface CustomerDao {

    Customer getCustomerByCompany(String company);

    int insertCustomer(Customer customer);

    List<String> getCustomerName(String name);

    Customer selectCustomerName(String tranCustomerName);

    int insertCustomer2(Customer customerByCompany);
}
