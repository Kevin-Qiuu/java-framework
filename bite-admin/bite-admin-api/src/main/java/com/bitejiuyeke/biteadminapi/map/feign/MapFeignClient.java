package com.bitejiuyeke.biteadminapi.map.feign;


import com.bitejiuyeke.biteadminapi.map.domain.RegionVO;
import com.bitejiuyeke.bitecommondomain.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
public interface MapFeignClient {

    /**
     * @return 获取城市相关列表
     */
    @GetMapping("/map/cityList")
    R<List<RegionVO>> getCityList();

    @GetMapping("/map/hotCityList")
    R<List<RegionVO>> getHotCityList();


}
