package com.bjpowernode.crm.workbench.service.imp;

import com.bjpowernode.crm.settings.dao.UserDAO;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.dao.*;
import com.bjpowernode.crm.workbench.domain.*;
import com.bjpowernode.crm.workbench.service.ClueService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueServiceimp implements ClueService {
private ClueDao cd= SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
private UserDAO ud=SqlSessionUtil.getSqlSession().getMapper(UserDAO.class);
    @Override
    public List<User> getUserList() {

        List<User> userList = ud.getName();

        return userList;
    }

    @Override
    public boolean saveclue(Clue clue) {

        boolean b=true;
            int i=cd.saveclue(clue);
        if (i!=1){
            b=false;
        }

        return b;
    }

    @Override
    public Clue getClue(String id) {

        Clue clue=cd.getClue(id);

        return clue;
    }

    @Override
    public List<Activity> Get_Clue_and_activity(String id) {

        ActivityDAO ad=SqlSessionUtil.getSqlSession().getMapper(ActivityDAO.class);

        List<Activity> list=ad.Get_Clue_and_activity(id);

        return list;
    }




    @Override
    public boolean removeActivity(String id) {
        boolean b=true;
        int i=cd.removeActivity(id);
        if (i!=1){
            b=false;
        }


        return b;
    }

    @Override
    public boolean inputRelation(String cid, String[] aid) {
        boolean b=true;
        ClueActivityRelationDao card=SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);


        Map<String,String> map=new HashMap<>();
        for (String a:aid) {
            String id = UUIDUtil.getUUID();
            map.put("id",id);
            map.put("clueId",cid);
            map.put("activityId",a);
            int i=card.inputRelation(map);
            if (i!=1){
                b=false;
            }
        }



        return b;
    }

    /*SqlSessionUtil.getSqlSession().getMapper();*/
    ClueRemarkDao crd=SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);
    ClueActivityRelationDao car=SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
    CustomerDao cud=SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    CustomerRemarkDao curd=SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);
ContactsDao cod=SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
ContactsRemarkDao cord=SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    ContactsActivityRelationDao coaRelation=SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);


    TranDao tranDao=SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
TranHistoryDao TranHDao=SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    @Override
    public boolean convert(String clueId, Tran tran, String createBy) {
        boolean b=true;
//1.获取线索id 通过线索id获取线索对象 (为了分享数据给客户表 和 联系人表)
            Clue clue=cd.getClueById(clueId);
        String createTime = DateTimeUtil.getSysTime();

//2.通过线索对象提取客户信息，当前客户不存在时，新建客户  (根据客户(公司)名称 = 号精确匹配，判断该客户是否已存在  )
          String company=clue.getCompany(); //客户名称

          Customer customer=cud.getCustomerByCompany(company);
        if (customer==null){
            customer=new Customer();
             customer.setId(UUIDUtil.getUUID());
              customer.setOwner(clue.getOwner());
               customer.setName(clue.getCompany()); //公司名称
                customer.setWebsite(clue.getWebsite());
                customer.setPhone(clue.getPhone());
                  customer.setCreateBy(createBy);
                    customer.setCreateTime(createTime);
                     customer.setContactSummary(clue.getContactSummary());
                      customer.setNextContactTime(clue.getNextContactTime());
                       customer.setDescription(clue.getDescription());
                        customer.setAddress(clue.getAddress());

                    int CunstomerInt=cud.insertCustomer(customer);
            System.out.println(CunstomerInt+"-----------------cust");
                if (CunstomerInt!=1){
                    b=false;
                }
        }

//3.通过线索对象提取联系人信息，保存联系人---------------------------------------------------------
            Contacts contacts=new Contacts();
        contacts.setId(UUIDUtil.getUUID()); //id
          contacts.setOwner(clue.getOwner());//owner;
            contacts.setSource(clue.getSource()); //source;//来源
                contacts.setCustomerId(customer.getId());//customerId;
                    contacts.setFullname(clue.getFullname());//fullname;
                     contacts.setAppellation(clue.getAppellation());//appellation;
                         contacts.setEmail(clue.getEmail());//email;
                              contacts.setMphone(clue.getMphone());//mphone;
                                 contacts.setJob(clue.getJob());//job;
                  //birth;  没有，后期完善
                                    contacts.setCreateBy(createBy);//createBy;
                                          contacts.setCreateTime(createTime);//createTime;
                                              contacts.setDescription(clue.getDescription());//description;
                                                contacts.setContactSummary(clue.getContactSummary());//contactSummary;
                                                    contacts.setNextContactTime(clue.getNextContactTime());//nextContactTime;
                                                            contacts.setAddress(clue.getAddress());//address;
                              int ContactsInt=cod.insertContacts(contacts);
        System.out.println(ContactsInt+"--------------contactsInt");
                              if (ContactsInt!=1){
                                  b=false;
                              }

//4.线索备注转换到客户备注 以及联系人备注---------------------------------------------------------
        List<ClueRemark>  clueRemarkList=crd.getClueRemark(clue.getId()); //获取与此clueid对应的clueremark

        for (ClueRemark cl:clueRemarkList) {
            String noteContent=cl.getNoteContent();


            CustomerRemark customerRemark=new CustomerRemark(); //转换至 客户备注 -------------------
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setCustomerId(customer.getId());  //用上面创建好的客户id
            customerRemark.setNoteContent(noteContent);//用上面得到的备注信息
            customerRemark.setCreateTime(createTime); //上面创建好的
            customerRemark.setCreateBy(createBy);//传来的
            customerRemark.setEditFlag("0");
            int CustomerRemarkInt=curd.inserCustomerRemark(customerRemark);
            if (CustomerRemarkInt!=1){
                b=false;
            }

            ContactsRemark contactsRemark=new ContactsRemark();// 转换至 联系人备注-------------------
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setContactsId(contacts.getId());//用上面创建好的联系人id
            contactsRemark.setNoteContent(noteContent);//同样用上面得到的备注信息
            contactsRemark.setCreateTime(createTime);
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setEditFlag("0");
            int contactsRemarkInt=cord.insertContactsRemark(contactsRemark);
            System.out.println(contactsRemarkInt+"!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                if (contactsRemarkInt!=1){
                    b=false;
                }

        }

//5.tbl_clue_activity_relation 转换到 tbl_contacts_acvitvty_relation 中---------------------------------------

        //(其中的clueId和 id马上要被干掉了,因为这条clue相关的记录都要被转换   ,activityId 要转移到右边表去)
        List<ClueActivityRelation> clueActivityRelationList=car.getCar(clueId); //传clueId 来指定查询

        for (ClueActivityRelation clueActivityRelation:clueActivityRelationList ) {
            String activityId = clueActivityRelation.getActivityId();
            ContactsActivityRelation contactsActivityRelation=new ContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setContactsId(contacts.getId()); //用上面创建好的联系人id
            contactsActivityRelation.setActivityId(activityId); //循环遍历出来的
            int contactsActivityRelationInt=coaRelation.insertContactsActivityRelation(contactsActivityRelation);
            if (contactsActivityRelationInt!=1){
                b=false;
            }
        }

//6.如果有创建交易 的需求(勾选了) 创建一条交易---------------------------------------
        if (tran!=null){
            //这里是创建临时交易，往交易表中添加 tran(临时交易填写数据)
            // tran 已经在控制层封装好的字段 Id  money Name  ExpectedDate  Stage  ActivityId  CreateTime  CreateBy
                                          // 但是一切根据客户需求来定，比如客户还想要多添加几条内容

//剩余未完善的:owner customerId type source contactsId editBy editTime description contactSummary nextContactTime
                                                          // 这俩不属于创建
         //其他都可以从上面封装好的对象 取过来完善
            //这里老师选择全部完善，除了type和edit 这两个无法取到
            tran.setOwner(clue.getOwner());
            tran.setCustomerId(customer.getId());
            tran.setSource(clue.getSource());
            tran.setContactsId(contacts.getId());
            tran.setDescription(clue.getDescription());
            tran.setContactSummary(contacts.getContactSummary());
            tran.setNextContactTime(clue.getNextContactTime());
            int insertTranInt=tranDao.insertTran(tran);
            System.out.println(tran.getName());

            if (insertTranInt!=1){
                b=false;
            }
//7.要是创建了交易，就创建一条属于该交易的交易历史 tbl_tran_history----------------------------------
            TranHistory tr=new TranHistory();
                tr.setId(UUIDUtil.getUUID());
                tr.setStage(tran.getStage());  //此 交易 的阶段
                tr.setMoney(tran.getMoney()); //此交易的 钱
                tr.setExpectedDate(tran.getExpectedDate());
                tr.setCreateTime(createTime);
                tr.setCreateBy(createBy);
                tr.setTranId(tran.getId());
            int insertTHDInt=TranHDao.insertTHD(tr);
            if (insertTHDInt!=1){
                b=false;
            }

        }



//8.转换为客户 和 联系人后，删除该线索备注/该线索和市场活动的关系/该线索   Clue / Clue_Remark / Clue_Activity_Relation
        //外键关系，删除 先子后父  把两个子先删了
      int deleteClueRemarkInt=crd.deleteClueRemark(clueId);
         if (deleteClueRemarkInt!=1){
             b=false;
         }
         int deleteClueActivityInt=car.deleteClueActivity(clueId);
         if (deleteClueActivityInt!=1){
             b=false;
         }

        int deleteClueInt=cd.deleteClue(clueId);
        if (deleteClueInt!=1){
            b=false;
        }

        return b;
    }


}
