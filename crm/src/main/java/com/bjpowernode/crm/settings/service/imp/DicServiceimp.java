package com.bjpowernode.crm.settings.service.imp;

import com.bjpowernode.crm.settings.dao.DicTypeDAO;
import com.bjpowernode.crm.settings.dao.DicValueDAO;
import com.bjpowernode.crm.settings.domain.DicType;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.service.DicService;
import com.bjpowernode.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicServiceimp implements DicService {

   private DicTypeDAO dtd= SqlSessionUtil.getSqlSession().getMapper(DicTypeDAO.class);
   private DicValueDAO dvd=SqlSessionUtil.getSqlSession().getMapper(DicValueDAO.class);


   @Override
   public Map<String, List<DicValue>> getAllDic() {
      Map<String,List<DicValue>> map=new HashMap<>();
      List<DicType> getDT=dtd.getDT(); //通过查询所有DicType中的code(每一种类型)


      for (DicType dt:getDT
           ) {
         List<DicValue> getDv=dvd.getDV(dt.getCode());//然后将每次查询到的code放入getDv作为条件来查询
            map.put(dt.getCode(),getDv);//最后类型放进，List<DicValue>放进
      }

      return map;
   }
}
