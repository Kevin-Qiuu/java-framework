package com.bitejiuyeke.biteadminapi.map.feign;


import com.bitejiuyeke.biteadminapi.map.domain.RegionVO;
import com.bitejiuyeke.bitecommondomain.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

public interface MapFeignClient {

    /**
     * 获取全量城市列表
     * @return List<RegionVO>
     */
    @GetMapping("/map/cityList")
    R<List<RegionVO>> getCityList();

    /**
     * 获取热点城市列表
     * @return List<RegionVO>
     */
    @GetMapping("/map/hotCityList")
    R<List<RegionVO>> getHotCityList();

    /**
     * 获取全量城市列表Map（按拼音 A-Z）
     * @return List<RegionVO>
     */
    @GetMapping("/map/cityPinyinList")
    R<Map<String, List<RegionVO>>> getCityPinyinList();

    /**
     * 获取地区子列表
     * @param parentId 父区域 Id
     * @return List<RegionVO>
     */
    @GetMapping("/map/regionChildrenList")
    R<List<RegionVO>> getRegionChildrenList(Long parentId);


}
