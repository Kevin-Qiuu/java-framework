package com.bitejiuyeke.biteadminservice.map.service;

import com.bitejiuyeke.biteadminservice.map.domain.dto.RegionDTO;
import com.bitejiuyeke.bitecommondomain.domain.R;

import java.util.List;
import java.util.Map;

public interface IMapService {

    List<RegionDTO> getCityList();

    List<RegionDTO> getHotCityList();

    Map<String, List<RegionDTO>> getCityPinyinMap();

}
