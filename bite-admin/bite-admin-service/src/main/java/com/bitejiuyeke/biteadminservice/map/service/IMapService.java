package com.bitejiuyeke.biteadminservice.map.service;

import com.bitejiuyeke.biteadminservice.map.domain.dto.RegionDTO;
import com.bitejiuyeke.bitecommondomain.domain.R;

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
     * @param parentId 父区域 Id
     * @return List<RegionDTO>
     */
    List<RegionDTO> getRegionChildrenList(Long parentId);

}
