package com.bitejiuyeke.biteadminservice.map.service.impl;

import com.bitejiuyeke.biteadminservice.map.constants.MapConstants;
import com.bitejiuyeke.biteadminservice.map.domain.entity.SysRegion;
import com.bitejiuyeke.biteadminservice.map.mapper.RegionMapper;
import com.bitejiuyeke.biteadminservice.map.service.IMapService;
import com.bitejiuyeke.biteadminservice.map.domain.dto.RegionDTO;
import com.bitejiuyeke.bitecommoncore.utils.BeanCopyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MapService implements IMapService {

    @Autowired
    private RegionMapper regionMapper;

    @Override
    public List<RegionDTO> getCityList() {
        List<SysRegion> sysRegionList = regionMapper.selectAllRegion().stream()
                .filter(sysRegion -> sysRegion.getLevel().equals(MapConstants.CITY_LEVEL))
                .toList();
        return BeanCopyUtil.copyListProperties(sysRegionList, RegionDTO::new);
    }
}
