package com.bitejiuyeke.biteadminservice.map.controller;

import com.bitejiuyeke.biteadminapi.map.domain.RegionVO;
import com.bitejiuyeke.biteadminapi.map.feign.MapFeignClient;
import com.bitejiuyeke.biteadminservice.map.domain.dto.RegionDTO;
import com.bitejiuyeke.biteadminservice.map.service.IMapService;
import com.bitejiuyeke.bitecommoncore.utils.BeanCopyUtil;
import com.bitejiuyeke.bitecommondomain.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
