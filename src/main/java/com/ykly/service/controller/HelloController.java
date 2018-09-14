package com.ykly.service.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.ykly.service.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private final static String lHystrixToutMs_120 = "120000"; // Histrix 长熔断时间,数据交易可能需要比较长的时间
    private final static String lHystrixToutMs_60 = "60000"; // Histrix 长熔断时间,数据交易可能需要比较长的时间
    private final static String sHystrixToutMs_6 = "6000"; // Histrix 短熔断时间
    private final static String coreSize = "200";

    @Autowired
    private DiscoveryClient client;

    @Autowired
    private TestService testService;

    @RequestMapping("/hello")
    @HystrixCommand(groupKey = "dataGroup", fallbackMethod = "findOrderFallback", commandProperties = {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = lHystrixToutMs_60),}, threadPoolProperties = {@HystrixProperty(name = "coreSize", value = coreSize),})
    public String index(@RequestParam String name) {
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(name.equals("uuu")){
            throw new RuntimeException();
        }
        for(ServiceInstance serviceInstance : client.getInstances("ykly-service-a")){
            System.out.println(serviceInstance.getHost() + ":" + serviceInstance.getPort());
        }
        return "hello "+name+"，this is first messge";
    }

    public String findOrderFallback(String name) {
        return "订单查找失败！";
    }

    @RequestMapping("/test/{name}")
    public String test(@PathVariable("name") String name) {
        return testService.test(name);
    }

    @RequestMapping("/test1")
    public String test1() {
        return "我是需要认证的接口";
    }
}