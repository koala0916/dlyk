package com.bjpowernode;


import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.bjpowernode.entity.TUser;
import com.bjpowernode.service.UserService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SpringBootTest
class DlykServerApplicationTests {

    //密钥不能让别人知道，这个密钥放在服务器上
    public static final String secret = "0S/12d4845Whd2h3OPYs";

    @Resource
    private UserService userService;

    @Test
    void contextLoads() {
        //1、生成jwt
        Map<String, Object> header = new HashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");

        Map<String, Object> payLoad = new HashMap<>();
        payLoad.put("id", 12352);
        payLoad.put("phone", "13720202020");
        payLoad.put("birthDay", new Date());

        String jwt = JWTUtil.createToken(payLoad, secret.getBytes(StandardCharsets.UTF_8));
        System.out.println(jwt);

        String jwt2 = JWTUtil.createToken(payLoad, JWTSignerUtil.hs256(secret.getBytes(StandardCharsets.UTF_8)));
        System.out.println(jwt2);

        String jwt3 = JWTUtil.createToken(header, payLoad, secret.getBytes(StandardCharsets.UTF_8));
        System.out.println(jwt3);

        String jwt4 = JWTUtil.createToken(header, payLoad, JWTSignerUtil.hs256(secret.getBytes(StandardCharsets.UTF_8)));
        System.out.println(jwt4);

        //2、验证jwt
        boolean verify = JWTUtil.verify(jwt4, secret.getBytes(StandardCharsets.UTF_8));
        boolean verify2 = JWTUtil.verify(jwt4, JWTSignerUtil.hs256(secret.getBytes(StandardCharsets.UTF_8)));
        System.out.println(verify);
        System.out.println(verify2);

        //3、解析jwt负载中的数据
        JWT parseJWT = JWTUtil.parseToken(jwt4);
        JWTPayload payload = parseJWT.getPayload();
        Object id = payload.getClaim("id");
        String phone = (String)payload.getClaim("phone");
        Object birthDay = payload.getClaim("birthDay");
        System.out.println(id + " -- " + phone + " -- " + birthDay);

        JSONObject payloads = parseJWT.getPayloads();
        Integer id2 = payloads.get("id", Integer.class);
        String phone2 = payloads.get("phone", String.class);
        Date birthDay2 = payloads.get("birthDay", Date.class);
        System.out.println(id2 + " -- " + phone2 + " -- " + birthDay2);

        Object id3 = parseJWT.getPayload("id");
        Object phone3 = parseJWT.getPayload("phone");
        Object birthDay3 = parseJWT.getPayload("birthDay");
        System.out.println(id3 + " -- " + phone3 + " -- " + birthDay3);
    }




    @Test
    public void testPageHelpler(){
        PageInfo<TUser> userByPage = userService.getUserByPage(1, null);
        System.out.println(userByPage);
    }

    @Test
    public void testUUID() {
        String uuid = UUID.randomUUID().toString();
        System.out.println(uuid);

        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        long id = snowflake.nextId();

        Snowflake snowflake1 = IdUtil.getSnowflake();
        System.out.println(id);
    }
}
