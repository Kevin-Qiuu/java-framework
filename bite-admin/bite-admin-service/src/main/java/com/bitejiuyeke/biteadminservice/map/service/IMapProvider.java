package com.bitejiuyeke.biteadminservice.map.service;

import com.bitejiuyeke.biteadminservice.map.domain.dto.PoiListDTO;
import com.bitejiuyeke.biteadminservice.map.domain.dto.SuggestSearchDTO;

public interface IMapProvider {

    PoiListDTO searchTencentMapPoiByRegion(SuggestSearchDTO suggestSearchDTO);

}
