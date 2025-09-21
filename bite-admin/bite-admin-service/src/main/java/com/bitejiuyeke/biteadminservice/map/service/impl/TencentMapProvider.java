package com.bitejiuyeke.biteadminservice.map.service.impl;

import com.bitejiuyeke.biteadminservice.map.constants.MapConstants;
import com.bitejiuyeke.biteadminservice.map.domain.dto.PoiListDTO;
import com.bitejiuyeke.biteadminservice.map.domain.dto.SuggestSearchDTO;
import com.bitejiuyeke.biteadminservice.map.service.IMapProvider;
import com.bitejiuyeke.bitecommondomain.domain.ResultCode;
import com.bitejiuyeke.bitecommonsecurity.exception.ServiceException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RefreshScope
@Data
@Component
@ConditionalOnProperty(value = "map.type", havingValue = "tencent")
public class TencentMapProvider implements IMapProvider {

    /**
     * 腾讯地图开发密钥
     */
    @Value("${tencentMap.key}")
    private String key;

    /**
     * 腾讯地图 api 访问地址：<a href="https://apis.map.qq.com">https://apis.map.qq.com</a>
     */
    @Value("${tencentMap.apiServer}")
    private String apiServer;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public PoiListDTO searchTencentMapPoiByRegion(SuggestSearchDTO suggestSearchDTO) {
        /*
        https://apis.map.qq.com/ws/place/v1/suggestion?
        region=德州市&keyword=新贝&key=PW2BZ-NZHKW-3TBR2-YXR64-LPLG5-OXFJQ&region_fix=1
         */
        String url = String.format(apiServer + MapConstants.TENCENT_MAP_API_PLACE_SUGGESTION
                + "?region=%s&keyword=%s&key=%s&region_fix=%s",
                suggestSearchDTO.getRegion(), suggestSearchDTO.getKeyword(), key, suggestSearchDTO.getRegion_fix());
//        PoiListDTO poiListDTO = restTemplate.getForObject(url, PoiListDTO.class);
        ResponseEntity<PoiListDTO> response = restTemplate.getForEntity(url, PoiListDTO.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new ServiceException(ResultCode.TencentMAP_QUERY_FAILED);
        }
        return response.getBody();
    }
}
