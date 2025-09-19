package com.bitejiuyeke.bitemstemplateservice.mapTest;

import com.bitejiuyeke.biteadminapi.map.domain.RegionVO;
import com.bitejiuyeke.biteadminapi.map.feign.MapFeignClient;
import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitemstemplateservice.feign.RemoteMapClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test/map")
public class TestMapController {

    @Autowired
    private RemoteMapClient mapFeignClient;

    @GetMapping("/cityList")
    public R<List<RegionVO>> getCityList() {
        return mapFeignClient.getCityList();
    }


}
