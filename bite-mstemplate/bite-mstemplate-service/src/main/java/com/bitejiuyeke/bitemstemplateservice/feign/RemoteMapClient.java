package com.bitejiuyeke.bitemstemplateservice.feign;

import com.bitejiuyeke.biteadminapi.map.feign.MapFeignClient;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "bite-admin")
public interface RemoteMapClient extends MapFeignClient {
}
