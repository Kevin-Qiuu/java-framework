package com.bitejiuyeke.bitecommoncore;

import com.bitejiuyeke.bitecommoncore.TestDomains.TestAnimal;
import com.bitejiuyeke.bitecommoncore.TestDomains.TestCar;
import com.bitejiuyeke.bitecommoncore.TestDomains.TestCat;
import com.bitejiuyeke.bitecommoncore.TestDomains.TestDog;
import com.bitejiuyeke.bitecommoncore.utils.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@SpringBootTest
public class JsonUtilTest {


    @Test
    void testCommonObject() {

//        TestCar testCar = new TestCar();
//        testCar.setName("宋 plus");
//        testCar.setPrice(23);
//        testCar.setBrandName("BYD");
//
//        System.out.println(JsonUtil.obj2string(testCar));

        String str = "{\"name\":\"宋 plus\",\"price\":23,\"brandName\":\"BYD\"}";
        System.out.println(JsonUtil.string2Obj(str, TestCar.class));


    }


    @Test
    void testListObj() throws InterruptedException {
//        List<TestCar> testCarList = new ArrayList<>();
//
        TestCar testCar1 = new TestCar();
        testCar1.setName("宋 plus");
        testCar1.setPrice(23);
        testCar1.setBrandName("BYD");

        TestCar testCar2 = new TestCar();
        testCar2.setName("001 GT");
        testCar2.setPrice(25);
        testCar2.setBrandName("极氪");
//
//        testCarList.add(testCar1);
//        testCarList.add(testCar2);
//
//        System.out.println(JsonUtil.obj2String(testCarList));

//        String str = "[{\"name\":\"宋 plus\",\"price\":23,\"brandName\":\"BYD\"},{\"name\":\"001 GT\",\"price\":25,\"brandName\":\"极氪\"}]";
//        List<TestCar> testCarList = JsonUtil.string2List(str, TestCar.class);
//        System.out.println(testCarList);

//        Map<Date, TestCar> testCarMap = new HashMap<>();
//        testCarMap.put(new Date(), testCar1);
//        Thread.sleep(1000);
//        testCarMap.put(new Date(), testCar2);
//        System.out.println(JsonUtil.obj2String(testCarMap));

//        String str = "{\"1753181680863\":{\"name\":\"001 GT\",\"price\":25,\"brandName\":\"极氪\"},\"1753181679858\":{\"name\":\"宋 plus\",\"price\":23,\"brandName\":\"BYD\"}}";
//        Map<String, TestCar> testCarMap = JsonUtil.string2Map(str, TestCar.class);
//        System.out.println(testCarMap);

//        List<Map<Date, TestCar>> mapList = new ArrayList<>();
//        Map<Date, TestCar> testCarMap = new HashMap<>();
//        testCarMap.put(new Date(), testCar1);
//        Thread.sleep(1000);
//        testCarMap.put(new Date(), testCar2);
//        mapList.add(testCarMap);
//        System.out.println(JsonUtil.obj2String(mapList));

        String str = "[{\"1753192199758\":{\"name\":\"001 GT\",\"price\":25,\"brandName\":\"极氪\"},\"1753192198753\":{\"name\":\"宋 plus\",\"price\":23,\"brandName\":\"BYD\"}}]";
        List<Map<String, TestCar>> mapList = JsonUtil.string2Obj(str, new TypeReference<List<Map<String, TestCar>>>() {
        });
        System.out.println(mapList);

    }

    @Test
    void testExtendObj() {

//        TestDog testDog = new TestDog();
//        testDog.setNickName("旺财");
//        testDog.setPrice(18);
//        testDog.setName("dog");
//        System.out.println(JsonUtil.obj2String(testDog));
//
//        TestCat testCat = new TestCat();
//        testCat.setName("cat");
//        testCat.setAge(5);
//        testCat.setSound("miao");
//        System.out.println(JsonUtil.obj2String(testCat));

        String dogStr = "{\"type\":\"dog\", \"name\":\"dog\",\"nickName\":\"旺财\",\"price\":18}";
        String catStr = "{\"type\":\"cat\", \"name\":\"cat\",\"sound\":\"miao\",\"age\":5}";
        System.out.println(JsonUtil.string2Obj(dogStr, TestAnimal.class).getClass());
        System.out.println(JsonUtil.string2Obj(catStr, TestAnimal.class).getClass());

    }

    @Test
    public void currentTime() {
        log.info(LocalDate.now().toString());
        LocalDate parse = LocalDate.parse("2025-07-27");
        log.info(parse.toString());
    }

}
