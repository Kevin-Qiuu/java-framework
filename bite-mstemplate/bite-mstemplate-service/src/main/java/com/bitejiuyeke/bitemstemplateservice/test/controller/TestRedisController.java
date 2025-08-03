package com.bitejiuyeke.bitemstemplateservice.test.controller;

import com.bitejiuyeke.bitemstemplateservice.domain.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.bitejiuyeke.bitecommonredis.service.RedisService;

@RestController
@RequestMapping("/test/redis")
public class TestRedisController {

    @Autowired
    private RedisService redisService;

    @PostMapping("/setCarCache")
    public void setCarCacheObject(String key,
                                  @RequestParam("brand") String brand,
                                  @RequestParam("price") Long price,
                                  @RequestParam("ownerName") String ownerName) {
        Car car = new Car();
        car.setBrand(brand);
        car.setPrice(price);
        car.setOwnerName(ownerName);
        redisService.setCacheObject(key, car);
    }

}
