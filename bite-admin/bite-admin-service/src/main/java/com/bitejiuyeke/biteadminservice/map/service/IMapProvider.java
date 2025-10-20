package com.bitejiuyeke.biteadminservice.map.service;

import com.bitejiuyeke.biteadminapi.map.domain.dto.LocationDTO;
import com.bitejiuyeke.biteadminservice.map.domain.dto.*;

public interface IMapProvider {

    PoiListDTO searchTencentMapPoiByRegion(SuggestSearchDTO suggestSearchDTO);

    GeoResultDTO geoCoderTencentMap(LocationDTO locationDTO);

}
