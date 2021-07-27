import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.MD5Util;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.service.TranHistoryService;
import com.bjpowernode.crm.workbench.service.imp.TranHistoryServiceimp;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;


public class Test  {
    public static void main(String[] args) {
        String md5 = MD5Util.getMD5("123");
        System.out.println(md5);
        int i = "2021-07-18 10:43:17".compareTo(DateTimeUtil.getSysTime());
        System.out.println(i);
    }


    @org.junit.Test
    public  void qwe() {
        Map<String, String> map1 = new HashMap<>();
        ResourceBundle r = ResourceBundle.getBundle("Stage2Possibility");
        Enumeration<String> keys = r.getKeys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            String value = r.getString(key);
            map1.put(key, value);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String s = objectMapper.writeValueAsString(map1);

            System.out.println(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
