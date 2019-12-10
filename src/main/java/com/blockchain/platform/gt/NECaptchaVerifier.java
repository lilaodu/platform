package com.blockchain.platform.gt;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 二次验证
 */
public class NECaptchaVerifier {
    public static final String VERIFY_API = "http://c.dun.163yun.com/api/v2/verify"; // verify接口地址
    public static final String REQ_VALIDATE = "NECaptchaValidate"; // 二次验证带过来的validate
    private static final String VERSION = "v2";
    private static final String captchaId = "c1e1fe6b13da4ca69e03e0977f5a6e93"; // 验证码id
    private static final String secretId = "335bdfc61365a77e77fd4f0e49f219b0";
    private static final String secretKey = "665ec863c53002d56b8ca17d1058dc31";
    
    /**
     * 二次验证
     *
     *
     * @param user     用户
     * @return
     */
    public VerifyResult verify( String user) {
        
        Map<String, String> params = new HashMap<String, String>();
        params.put("captchaId", captchaId);//验证码id 
       // params.put("validate", validate);//提交二次校验的验证数据，即NECaptchaValidate值
        params.put("user", user);//用户信息，值可为空
        // 公共参数
        params.put("secretId", secretId);//密钥对id
        params.put("version", VERSION);//版本信息，固定值v2
        params.put("timestamp", String.valueOf(System.currentTimeMillis()));//当前时间戳的毫秒值
        params.put("nonce", String.valueOf(ThreadLocalRandom.current().nextInt()));//随机字符串
        // 计算请求参数签名信息
        String signature = sign(secretKey, params);  
        params.put("signature", signature);//签名信息
        String resp = "";
        try {
            resp = HttpConnectionUtils.readContentFromPost(VERIFY_API, params);
        } catch (IOException ex) {
            System.out.println("http connect occur exception,please check !");//post发送表单
            ex.printStackTrace();
        }
        System.out.println("resp = " + resp);
        return verifyRet(resp);
    }

    /**
     * 生成签名信息
     *
     * @param secretKey 验证码私钥
     * @param params    接口请求参数名和参数值map，不包括signature参数名
     * @return
     */
    public static String sign(String secretKey, Map<String, String> params) {
        String[] keys = params.keySet().toArray(new String[0]);
        Arrays.sort(keys);
        StringBuffer sb = new StringBuffer();
        for (String key : keys) {
            sb.append(key).append(params.get(key));
        }
        sb.append(secretKey);
        try {
            return DigestUtils.md5Hex(sb.toString().getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();// 一般编码都支持的。。
        }
        return null;
    }

    /**
     * 验证返回结果
     *
     * @param resp
     * @return
     */
    private VerifyResult verifyRet(String resp) {
        if (StringUtils.isEmpty(resp)) {
            return VerifyResult.fakeNormalResult(resp);
        }
        try {
            VerifyResult verifyResult = JSONObject.parseObject(resp, VerifyResult.class);
            return verifyResult;
        } catch (Exception e) {
            return VerifyResult.fakeNormalResult(resp);
        }
    }
}
