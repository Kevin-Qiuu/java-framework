package com.bitejiuyeke.bitemstemplateservice.adminTest;

import com.bitejiuyeke.biteadminapi.config.domain.vo.ArgVO;
import com.bitejiuyeke.biteadminapi.config.feign.ArgumentFeignClient;
import com.bitejiuyeke.biteadminapi.map.domain.vo.RegionVO;
import com.bitejiuyeke.biteadminapi.map.feign.MapFeignClient;
import com.bitejiuyeke.bitecommondomain.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test/map")
public class TestMapController {

    @Autowired
    private MapFeignClient mapFeignClient;

    @Autowired
    private ArgumentFeignClient argumentFeignClient;

    @GetMapping("/cityList")
    public R<List<RegionVO>> getCityList() {
        return mapFeignClient.getCityList();
    }


}
