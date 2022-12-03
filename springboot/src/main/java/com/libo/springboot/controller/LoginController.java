package com.libo.springboot.controller;

import com.alibaba.fastjson.JSONObject;
import com.libo.springboot.entity.VxVo;
import com.libo.springboot.utils.GetPostUntil;
import io.swagger.annotations.ApiOperation;
import org.apache.tomcat.util.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class LoginController {

    @ApiOperation("获取session_key")
    @ResponseBody
    @PostMapping("/initLogin")
    public Map initLogin(@RequestParam(value = "js_code", required = true) String js_code) {

        //微信获取session_key接口地址
        String wxLoginUrl = "https://api.weixin.qq.com/sns/jscode2session";
        //接口参数
        String param = "appid=wxa32fffaa4bcfac50&secret=350c6b8ac9aed52c3472bee4e0b32a22&js_code=" + js_code + "&grant_type=authorization_code";
        //调用获取session_key接口 请求方式get
        String jsonString = GetPostUntil.sendGet(wxLoginUrl, param);
        System.out.println(jsonString);
        //因为json字符串是大括号包围，所以用JSONObject解析
        JSONObject json = JSONObject.parseObject(jsonString);
        //json解析session_key值
        String session_key = json.getString("session_key");
        System.out.println("session_key：" + session_key);
        //返回给前端
        Map<String, Object> map = new HashMap<>();
        map.put("session_key",session_key);
        return map;
    }

    @ApiOperation("获取手机号码")
    @PostMapping("/getPhone")
    @ResponseBody
    public Map mini_getPhone(@RequestBody VxVo vxVo){
        //解密电话号码
        JSONObject obj = getPhoneNumber(vxVo);
        //System.out.println(obj);
        String phone = obj.get("phoneNumber").toString();
        System.out.println("phone：" + phone);
        Map<String, Object> map = new HashMap<>(2);
        map.put("phone",phone);
        return map;
    }


    //解析电话号码
    public JSONObject getPhoneNumber(VxVo vxVo) {

        byte[] dataByte = Base64.decodeBase64(vxVo.getEncryptedData());
        byte[] keyByte = Base64.decodeBase64(vxVo.getSession_key());
        byte[] ivByte = Base64.decodeBase64(vxVo.getIv());
        try {
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, "UTF-8");
                return JSONObject.parseObject(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }


}
