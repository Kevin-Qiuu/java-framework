package com.bitejiuyeke.biteadminapi.map.feign;


import com.bitejiuyeke.biteadminapi.map.domain.dto.LocationDTO;
import com.bitejiuyeke.biteadminapi.map.domain.dto.SearchPoiReqDTO;
import com.bitejiuyeke.biteadminapi.map.domain.vo.RegionCityVO;
import com.bitejiuyeke.biteadminapi.map.domain.vo.RegionVO;
import com.bitejiuyeke.biteadminapi.map.domain.vo.SearchPoiVO;
import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitecommondomain.domain.vo.BasePageVO;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@ConditionalOnProperty(name = "feign.bite-admin.feignEnabled", havingValue = "true")
@FeignClient(contextId = "mapFeignClient", name = "bite-admin")
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
     * @param parentCode 父区域 Id
     * @return List<RegionVO>
     */
    @GetMapping("/map/regionChildrenList/{parentCode}")
    R<List<RegionVO>> getRegionChildrenList(@PathVariable("parentCode") String parentCode);

    /**
     * 根据关键词查询某一地点的相关 poi
     * @param SearchPoiReqDTO 查询 dto
     * @return 分页列表
     */
    @PostMapping("/map/search")
    R<BasePageVO<SearchPoiVO>> searchSuggestOnMap(@Validated @RequestBody SearchPoiReqDTO SearchPoiReqDTO);

    /**
     * 根据经纬度来逆地址解析
     * @param locationDTO 经纬度
     * @return 城市信息
     */
    @PostMapping("/map/locateCityByLocation")
    R<RegionCityVO> locateCityByLocation(LocationDTO locationDTO);

}
