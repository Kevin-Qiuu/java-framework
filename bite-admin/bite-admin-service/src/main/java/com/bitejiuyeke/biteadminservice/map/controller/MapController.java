package com.bitejiuyeke.biteadminservice.map.controller;

import com.bitejiuyeke.biteadminapi.map.domain.RegionVO;
import com.bitejiuyeke.biteadminapi.map.feign.MapFeignClient;
import com.bitejiuyeke.biteadminservice.map.domain.dto.PoiListDTO;
import com.bitejiuyeke.biteadminservice.map.domain.dto.RegionDTO;
import com.bitejiuyeke.biteadminservice.map.domain.dto.SuggestSearchDTO;
import com.bitejiuyeke.biteadminservice.map.service.IMapProvider;
import com.bitejiuyeke.biteadminservice.map.service.IMapService;
import com.bitejiuyeke.bitecommoncore.utils.BeanCopyUtil;
import com.bitejiuyeke.bitecommondomain.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class MapController implements MapFeignClient {

    @Autowired
    private IMapService mapService;
    @Autowired
    private IMapProvider mapProvider;

    @Override
    @GetMapping("/map/cityList")
    public R<List<RegionVO>> getCityList() {
        List<RegionDTO> regionDTOList = mapService.getCityList();
        List<RegionVO> regionVOList = BeanCopyUtil.copyListProperties(regionDTOList, RegionVO::new);
        return R.ok(regionVOList);
    }

    @Override
    @GetMapping("/map/hotCityList")
    public R<List<RegionVO>> getHotCityList() {
        List<RegionDTO> hotRegionDTOList = mapService.getHotCityList();
        List<RegionVO> hotRegionVOList = BeanCopyUtil.copyListProperties(hotRegionDTOList, RegionVO::new);
        return R.ok(hotRegionVOList);
    }

    @Override
    @GetMapping("/map/cityPinyinList")
    public R<Map<String, List<RegionVO>>> getCityPinyinList() {
        Map<String, List<RegionDTO>> cityPinyinDTOMap = mapService.getCityPinyinMap();
        Map<String, List<RegionVO>> cityPinyinVOMap = new TreeMap<>();
        for (Map.Entry<String, List<RegionDTO>> entry : cityPinyinDTOMap.entrySet()) {
            cityPinyinVOMap.put(entry.getKey(), BeanCopyUtil.copyListProperties(entry.getValue(), RegionVO::new));
        }
        return R.ok(cityPinyinVOMap);
    }

    @Override
    @GetMapping("/map/regionChildrenList")
    public R<List<RegionVO>> getRegionChildrenList(Long parentId) {
        List<RegionDTO> regionChildrenDTOList = mapService.getRegionChildrenList(parentId);
        List<RegionVO> regionChildrenVOList = BeanCopyUtil.copyListProperties(regionChildrenDTOList, RegionVO::new);
        return R.ok(regionChildrenVOList);
    }

    @GetMapping("/map/suggestRegionTest")
    public R<PoiListDTO> getSuggestRegionTest(SuggestSearchDTO suggestSearchDTO) {
        return R.ok(mapProvider.searchTencentMapPoiByRegion(suggestSearchDTO));
    }

}
