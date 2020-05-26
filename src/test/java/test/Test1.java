package test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.wenda.model.User;
import org.junit.Test;

/**
 * @author Jin Qiuyang
 * @date 2020/5/25
 */
public class Test1 {

    @Test
    public void test(){
        User user = new User();
        user.setName("jqy");
        user.setId(1);
        user.setPassword("11-7");
        user.setSalt("121");
        user.setHeadUrl(null);
        String json= JSON.toJSONString(user, SerializerFeature.WriteNullStringAsEmpty);
        System.out.println(json);


    }
}
