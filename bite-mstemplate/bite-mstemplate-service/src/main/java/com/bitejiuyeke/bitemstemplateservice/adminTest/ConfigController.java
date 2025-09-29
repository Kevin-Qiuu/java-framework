package com.bitejiuyeke.bitemstemplateservice.adminTest;

import com.bitejiuyeke.biteadminapi.config.domain.dto.ArgDTO;
import com.bitejiuyeke.biteadminapi.config.domain.dto.ArgReadReqDTO;
import com.bitejiuyeke.biteadminapi.config.domain.dto.ArgWriteReqDTO;
import com.bitejiuyeke.biteadminapi.config.domain.vo.ArgVO;
import com.bitejiuyeke.biteadminapi.config.feign.ArgumentFeignClient;
import com.bitejiuyeke.bitecommoncore.utils.JsonUtil;
import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitecommondomain.domain.vo.BasePageVO;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class ConfigController {

    @Autowired
    private ArgumentFeignClient argumentFeignClient;

    @PostMapping("/config/add")
    public R<Long> configAddTest(@RequestBody ArgWriteReqDTO argWriteReqDTO) {
        R<Long> longR = argumentFeignClient.argumentAdd(argWriteReqDTO);
        log.info(JsonUtil.obj2StringPretty(longR));
        return longR;
    }

    @GetMapping("/config/key/{configKey}")
    public R<ArgVO> argumentByConfigKey(@PathVariable("configKey") @NotBlank(message = "参数键为空！") String configKey){
       return argumentFeignClient.argumentByConfigKey(configKey);
    }

    @PostMapping("/config/list")
    public R<BasePageVO<ArgVO>> configEditTest(@RequestBody ArgReadReqDTO argWriteReqDTO) {
        R<BasePageVO<ArgVO>> basePageVOR = argumentFeignClient.argumentList(argWriteReqDTO);
        return basePageVOR;
    }

}
