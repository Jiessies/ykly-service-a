package com.ykly.service.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "ykly-service-b")
public interface TestService {

    @RequestMapping(value = "/test/{name}")
    public String test(@PathVariable(value = "name") String name);
}
