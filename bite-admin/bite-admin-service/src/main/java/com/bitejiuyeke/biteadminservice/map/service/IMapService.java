package com.bitejiuyeke.biteadminservice.map.service;

import com.bitejiuyeke.biteadminservice.map.domain.dto.RegionDTO;

import java.util.List;

public interface IMapService {

    List<RegionDTO> getCityList();

    List<RegionDTO> getHotCityList();

}
