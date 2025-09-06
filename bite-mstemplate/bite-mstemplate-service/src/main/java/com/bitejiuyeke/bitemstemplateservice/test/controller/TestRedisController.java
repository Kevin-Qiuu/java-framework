package com.bitejiuyeke.bitemstemplateservice.test.controller;

import com.bitejiuyeke.bitecommoncore.utils.JsonUtil;
import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitemstemplateservice.domain.Car;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.bitejiuyeke.bitecommonredis.service.RedisService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/test/redis")
public class TestRedisController {

    @Autowired
    private RedisService redisService;

    @PostMapping("/string/setCacheObject")
    public R<Void> setCarCacheObject(String key,
                                     @RequestParam("brand") String brand,
                                     @RequestParam("price") Long price,
                                     @RequestParam("ownerName") String ownerName) {
        Car car = new Car();
        car.setBrand(brand);
        car.setPrice(price);
        car.setOwnerName(ownerName);
        redisService.setCacheObject(key, car);
        return R.ok();
    }

    @PostMapping("/string/setCacheObjectIfAbsent")
    public R<Void> testSetCacheObjectIfAbsent(String key) {
        Car car = new Car();
        car.setBrand("BYD");
        car.setPrice(1000L);
        car.setOwnerName("KevinQiu");
        redisService.setCacheObjectIfAbsent(key, car, 1 * 60, TimeUnit.SECONDS);
        return R.ok();
    }

    @PostMapping("/list/setCacheList")
    public R<Void> testSetCacheList() {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        list.add("e");
        list.add("f");
        list.add("c");
        list.add("g");
        list.add("c");
        list.add("h");
        list.add("c");
        long ret = redisService.setCacheList("testList", list);
        return R.ok();
    }

    @PostMapping("/list/push")
    public R<Void> testPushForList() {
        redisService.leftPushForList("testList", "leftPush");
        redisService.rightPushForList("testList", "rightPush");
        return R.ok();
    }

    @PostMapping("/list/pop")
    public R<Void> testPopForList() {
        redisService.leftPopForList("testList");
        redisService.rightPopForList("testList");
        return R.ok();
    }

    @SneakyThrows
    @PostMapping("/list/remove")
    public R<Void> testRemoveForList() {
        redisService.removeForList("testList", "c");
        Thread.sleep(15000);
        redisService.removeAllForList("testList", "c");
        return R.ok();
    }

    @PostMapping("/list/getValue")
    public R<List<Car>> testGetValue() {
        List<Car> carList = new ArrayList<>();
        carList.add(new Car());
        carList.add(new Car());
        carList.add(new Car());
        carList.add(new Car());
        for (int i = 0; i < carList.size(); i++) {
            Car car = carList.get(i);
            if (i % 2 == 0) {
                car.setBrand("极氪汽车");
                car.setPrice(10000L);
                car.setElementInfo(new HashMap<>());
                car.getElementInfo().put("Wheel", 4L);
            } else {
                car.setBrand("BYD");
                car.setPrice(15000L);
                car.setElementInfo(new HashMap<>());
                car.getElementInfo().put("Door", 3L);
            }
        }

        redisService.setCacheList("testCarList", carList);
        List<Car> rangeList = redisService.getCacheListByRange("testCarList", 1, 2,
                new TypeReference<>() {});
        return R.ok(rangeList);

    }

}
