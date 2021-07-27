package com.bjpowernode.crm.workbench.service.imp;

import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.dao.CustomerDao;
import com.bjpowernode.crm.workbench.dao.TranDao;
import com.bjpowernode.crm.workbench.dao.TranHistoryDao;
import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.service.TranService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranServiceimp implements TranService {
    private  TranDao tranDao= SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao=SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
   private CustomerDao cd=SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);


    @Override
    public boolean insertTranT(Tran t, String customerName) {
        boolean b=true;
        //1.先判断customerName是否存在，返回Customer

        Customer customerByCompany = cd.getCustomerByCompany(customerName);

        //2判断Customer是否为空，空表示不存在，添加该客户
        if (customerByCompany==null)
        {
            customerByCompany=new Customer();
            customerByCompany.setId(UUIDUtil.getUUID());
            customerByCompany.setName(customerName);
            customerByCompany.setCreateTime(t.getCreateTime());
            customerByCompany.setCreateBy(t.getCreateBy());             //根据需求来添加字段，这里随便加几个,但创建人时间和名字必须
            customerByCompany.setNextContactTime(t.getNextContactTime());
            customerByCompany.setOwner(t.getOwner());
            int i=cd.insertCustomer2(customerByCompany);
            if (i!=1){
                b=false;
            }
        }

        // 不管存在不存在都会添加交易 ，因为不存在会在上面创建  (先把CustomerId补上)
            String id = customerByCompany.getId();
            t.setCustomerId(id);
            int i=tranDao.insertTran(t);
             if (i!=1){
                 b=false;
             }
        //3.添加交易历史，从t中取值
             TranHistory thd=new TranHistory();
             thd.setId(UUIDUtil.getUUID());
             thd.setStage(t.getStage());
             thd.setMoney(t.getMoney());
             thd.setExpectedDate(t.getExpectedDate());
                thd.setCreateTime(t.getCreateTime());
                thd.setCreateBy(t.getCreateBy());
                thd.setTranId(t.getId());

             int i1=tranHistoryDao.insertTHD(thd);
                if (i1!=1){
                    b=false;
                }
        return b;
    }

    @Override
    public List<Tran> showTran() {

        List<Tran> list=tranDao.showTran();

        return list;
    }



    @Override
    public Tran selectTran(String tranid) {

            Tran tran=tranDao.selectTran(tranid);

        return tran;
    }

    @Override
    public Map<String, Object> changeStage(Tran t) {
        boolean b=true;
       int i=tranDao.changeStage(t);
        if (i!=1){
            b=false;
        }

        TranHistory tranHistory=new TranHistory();
        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setCreateBy(t.getEditBy());
        tranHistory.setCreateTime(t.getEditTime());
        tranHistory.setMoney(t.getMoney());
        tranHistory.setKenengxing(t.getKenengxing());
        tranHistory.setTranId(t.getId());
        tranHistory.setStage(t.getStage());
        tranHistory.setExpectedDate(t.getExpectedDate());
        int i1=tranHistoryDao.insertTHD(tranHistory);
        if (i1!=1){
           b=false;
        }

        Map<String,Object> map=new HashMap<>();
        map.put("success",b);
        map.put("t",t);

        return map;
    }

    @Override
    public Map<String, Object> TranECharts() {

        int i=tranDao.getTotal();//所有条数

       List<Map<String,Object>> mapList=tranDao.TranECharts(); //单组stage个数 和stage

       Map<String,List<Map<String,Object>>> listMap=new HashMap<>();

       Map<String,Object> map=new HashMap<>();
       map.put("total",i);
       map.put("dateList",mapList);

        return map;
    }
}
