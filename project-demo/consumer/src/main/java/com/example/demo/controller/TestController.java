package com.example.demo.controller;

import client.RpcProxy;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.bean.TestBean;
import com.example.demo.service.TestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by lenovo on 2018/11/26.
 */
@Controller
public class TestController {

    @Autowired
    RpcProxy rpcProxy;

    @RequestMapping("test")
    @ResponseBody
    public String testBean(String name,int age){
        TestBean testBean = new TestBean();
        testBean.setName(name);
        testBean.setAge(age);
        TestService testService = rpcProxy.get(TestService.class);
        int newAge = testService.nextYearAge(testBean);
        testBean.setAge(newAge);
        return JSONObject.toJSONString(testBean);
    }
}
