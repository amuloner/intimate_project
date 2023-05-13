package com.wu.intimate.utils;


import com.alibaba.fastjson.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

//发送请求工具类
public class RestTemplateHttp {


    public static Map get(RestTemplate restTemplate, String url){
        /**
         * getForObject
         * 参数1 要请求的地址的url  必填项
         * 参数2 响应数据的类型 是String 还是 Map等 必填项
         * 参数3 请求携带参数 选填
         * getForObject 方法的返回值就是 被调用接口响应的数据
         */

        //1. getForObject()
        //先获取返回的字符串，若想获取属性，可以使用gson转化为实体后get方法获取
        String result = restTemplate.getForObject(url, String.class);
        return JSONObject.parseObject(result, Map.class);
        //2. getForEntity()
        //获取实体ResponseEntity，可以用get函数获取相关信息
//        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
//        System.out.println(responseEntity);
//        System.out.println("responseEntity.getStatusCode() = " + responseEntity.getStatusCode());
//        System.out.println("responseEntity.getStatusCodeValue() = " + responseEntity.getStatusCodeValue()); //responseEntity.getStatusCodeValue() = 200
//        System.out.println("responseEntity.getBody() = " + responseEntity.getBody());   //responseEntity.getBody() = {"code":"0","data":{"address":"北京市海淀区","id":1,"password":"123456","role":0,"sex":0,"telephone":"10086","username":"小明"},"msg":"操作成功"}
//        System.out.println("responseEntity.getHeaders() = " + responseEntity.getHeaders());//responseEntity.getHeaders() = [Content-Type:"application/json", Content-Length:"158", Server:"Werkzeug/0.14.1 Python/3.7.0", Date:"Sat, 16 Oct 2021 06:01:26 GMT"]
//        System.out.println("responseEntity.getClass() = " + responseEntity.getClass());//responseEntity.getClass() = class org.springframework.http.ResponseEntity
    }

    public static ResponseEntity<String> post(RestTemplate restTemplate, String url, Map<String, String> params){

        //LinkedMultiValueMap一个键对应多个值，对应format-data的传入类型
        LinkedMultiValueMap<String, String> request = new LinkedMultiValueMap<>();
        //入参  request.set("username","baihui");
        request.setAll(params);
        //请求
//        String result = restTemplate.postForObject(url,request,String.class);
//        System.out.println(result);
        return restTemplate.postForEntity(url, request, String.class);
    }
}
