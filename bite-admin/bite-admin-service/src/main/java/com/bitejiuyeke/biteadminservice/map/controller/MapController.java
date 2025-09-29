package com.bitejiuyeke.biteadminservice.map.controller;

import com.bitejiuyeke.biteadminapi.map.domain.dto.LocationDTO;
import com.bitejiuyeke.biteadminapi.map.domain.dto.SearchPoiDTO;
import com.bitejiuyeke.biteadminapi.map.domain.dto.SearchPoiReqDTO;
import com.bitejiuyeke.biteadminapi.map.domain.vo.RegionCityVO;
import com.bitejiuyeke.biteadminapi.map.domain.vo.RegionVO;
import com.bitejiuyeke.biteadminapi.map.domain.vo.SearchPoiVO;
import com.bitejiuyeke.biteadminapi.map.feign.MapFeignClient;
import com.bitejiuyeke.biteadminservice.map.domain.dto.*;
import com.bitejiuyeke.biteadminservice.map.service.IMapProvider;
import com.bitejiuyeke.biteadminservice.map.service.IMapService;
import com.bitejiuyeke.bitecommoncore.utils.BeanCopyUtil;
import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitecommondomain.domain.dto.BasePageDTO;
import com.bitejiuyeke.bitecommondomain.domain.vo.BasePageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class MapController implements MapFeignClient {

    @Autowired
    private IMapService mapService;

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
    @GetMapping("/map/regionChildrenList/{parentCode}")
    public R<List<RegionVO>> getRegionChildrenList(@PathVariable("parentCode") String parentCode){
        List<RegionDTO> regionChildrenDTOList = mapService.getRegionChildrenList(parentCode);
        List<RegionVO> regionChildrenVOList = BeanCopyUtil.copyListProperties(regionChildrenDTOList, RegionVO::new);
        return R.ok(regionChildrenVOList);
    }

    @Override
    @PostMapping("/map/search")
    public R<BasePageVO<SearchPoiVO>> searchSuggestOnMap(@Validated @RequestBody SearchPoiReqDTO searchPoiDTO) {
        BasePageDTO<SearchPoiDTO> basePageDTO = mapService.searchSuggestOnMap(searchPoiDTO);
        BasePageVO<SearchPoiVO> basePageVO = new BasePageVO<>();
        BeanCopyUtil.copyProperties(basePageDTO, basePageVO);
        List<SearchPoiVO> searchPoiVOList = BeanCopyUtil.copyListProperties(basePageDTO.getList(), SearchPoiVO::new);
        basePageVO.setList(searchPoiVOList);
        return R.ok(basePageVO);
    }

    @Override
    @PostMapping("/map/locateCityByLocation")
    public R<RegionCityVO> locateCityByLocation(@Validated @RequestBody LocationDTO locationDTO) {
       RegionDTO regionDTO = mapService.locateCityByLocation(locationDTO);
       RegionCityVO regionCityVO = new RegionCityVO();
       BeanCopyUtil.copyProperties(regionDTO, regionCityVO);
       return R.ok(regionCityVO);
    }
}
