package com.bitejiuyeke.biteadminservice.map.service;

import com.bitejiuyeke.biteadminapi.map.domain.dto.LocationDTO;
import com.bitejiuyeke.biteadminapi.map.domain.dto.SearchPoiDTO;
import com.bitejiuyeke.biteadminapi.map.domain.dto.SearchPoiReqDTO;
import com.bitejiuyeke.biteadminservice.map.domain.dto.RegionDTO;
import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitecommondomain.domain.dto.BasePageDTO;

import java.util.List;
import java.util.Map;

public interface IMapService {

    /**
     * 获取全量城市列表
     * @return List<RegionDTO>
     */
    List<RegionDTO> getCityList();

    /**
     * 获取热点城市列表
     * @return List<RegionDTO>
     */
    List<RegionDTO> getHotCityList();

    /**
     * 获取全量城市列表（按拼音 A-Z）
     * @return Map<String, List<RegionDTO>>
     */
    Map<String, List<RegionDTO>> getCityPinyinMap();

    /**
     * 获取地区子列表
     * @param parentCode 父区域 code
     * @return List<RegionDTO>
     */
    List<RegionDTO> getRegionChildrenList(String parentCode);

    /**
     * 根据关键词在某地区搜索 poi
     * @param searchPoiDTO 方法参数
     * @return BasePageDTO<SearchPoiDTO>
     */
    BasePageDTO<SearchPoiDTO> searchSuggestOnMap(SearchPoiReqDTO searchPoiDTO);

    /**
     * 根据经纬度来逆地址解析
     * @param locationDTO 经纬度
     * @return 城市信息
     */
    RegionDTO locateCityByLocation(LocationDTO locationDTO);

}
