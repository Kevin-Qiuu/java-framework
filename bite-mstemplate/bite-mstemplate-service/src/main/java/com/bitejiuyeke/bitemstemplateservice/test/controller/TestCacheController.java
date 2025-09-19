package com.bitejiuyeke.bitemstemplateservice.test.controller;

import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitemstemplateservice.test.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/cache")
public class TestCacheController {

    @Autowired
    private CarService carService;

    @GetMapping("/car/getValue")
    public R<Integer> getCarValue(Integer carId) {
        Integer carValue = carService.getCarValue(carId);
        return R.ok(carValue);
    }

    @PostMapping("/car/setValue")
    public R<Void> setCarValue(Integer carId, Integer carValue) {
        carService.setCarValue(carId, carValue);
        return R.ok();
    }
}
