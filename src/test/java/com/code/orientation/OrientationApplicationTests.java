package com.code.orientation;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson2.JSON;
import com.code.orientation.constants.RedisConstants;
import com.code.orientation.entity.Activity;
import com.code.orientation.entity.User;
import com.code.orientation.service.ActivityService;
import com.code.orientation.service.UserService;
import com.code.orientation.utils.RedisUtil;
import com.code.orientation.utils.SequenceUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class OrientationApplicationTests {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserService userService;

    @Test
    void contextLoads() {
        for(int i = 0;i < 50;i++){
            System.out.println(IdUtil.getSnowflake(1,1).nextId());
            System.out.println(SequenceUtils.nextId());
        }
    }

    @Test
    public void descrept(){
        System.out.println(SaSecureUtil.aesDecrypt("xdzn", "/hz9CA3BPE8JBAMaGNLSbw=="));
    }

    @Test
    public void testJson(){
        String str = redisUtil.getString(RedisConstants.ACTIVITY_ENDING.getKey());
        String s = "[{\"content\":\"这是一个活动\",\"count\":0,\"createBy\":1,\"end\":1709628600000,\"gmtCreate\":1709628060610,\"gmtModified\":1709628060611,\"id\":77858152120321,\"isDeleted\":0,\"maxNumber\":200,\"name\":\"活动5\",\"points\":7,\"scoreStandard\":\"本次活动加7分\",\"start\":1709628120000,\"state\":1,\"type\":1,\"updateBy\":1},{\"content\":\"这是一个活动\",\"count\":0,\"createBy\":1,\"end\":1709628600000,\"gmtCreate\":1709628071839,\"gmtModified\":1709628071839,\"id\":77858175188993,\"isDeleted\":0,\"maxNumber\":200,\"name\":\"活动6\",\"points\":7,\"scoreStandard\":\"本次活动加7分\",\"start\":1709628180000,\"state\":1,\"type\":0,\"updateBy\":1},{\"content\":\"这是一个活动\",\"count\":0,\"createBy\":1,\"end\":1709628600000,\"gmtCreate\":1709628087107,\"gmtModified\":1709628087107,\"id\":77858208743425,\"isDeleted\":0,\"maxNumber\":200,\"name\":\"活动6\",\"points\":7,\"scoreStandard\":\"本次活动加7分\",\"start\":1709628240000,\"state\":1,\"type\":1,\"updateBy\":1}]";
//        if(s.contains(str)){

            // 解析 JSON 字符串为 JSON 数组
            List<Activity> list = JSON.parseArray(str).toList(Activity.class);
            for (Activity activity : list) {
                System.out.println(activity);
            }
        }
//    }

    @Test
    public void testMechile(){
    }
}
