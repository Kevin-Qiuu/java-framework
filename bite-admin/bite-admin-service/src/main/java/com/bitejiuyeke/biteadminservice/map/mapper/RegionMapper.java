package com.bitejiuyeke.biteadminservice.map.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bitejiuyeke.biteadminservice.map.domain.entity.SysRegion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RegionMapper extends BaseMapper<SysRegion> {

    /**
     * 获取全量区域信息
     * @return 区域列表
     */
    List<SysRegion> selectAllRegion();

    /**
     * 根据区域 id 获取对应区域信息
     * @param regionIds 区域 Id 列表
     * @return 指定区域列表
     */
    List<SysRegion> selectRegionByIds(@Param("regionIds") List<Integer> regionIds);

}
