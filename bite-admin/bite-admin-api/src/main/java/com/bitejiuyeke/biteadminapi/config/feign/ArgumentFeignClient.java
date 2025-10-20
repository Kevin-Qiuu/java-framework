package com.bitejiuyeke.biteadminapi.config.feign;

import com.bitejiuyeke.biteadminapi.config.domain.dto.ArgDTO;
import com.bitejiuyeke.biteadminapi.config.domain.dto.ArgListReqDTO;
import com.bitejiuyeke.biteadminapi.config.domain.dto.ArgReadReqDTO;
import com.bitejiuyeke.biteadminapi.config.domain.dto.ArgWriteReqDTO;
import com.bitejiuyeke.biteadminapi.config.domain.vo.ArgVO;
import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitecommondomain.domain.dto.BasePageDTO;
import com.bitejiuyeke.bitecommondomain.domain.vo.BasePageVO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@ConditionalOnProperty(name = "feign.bite-admin.feignEnabled", havingValue = "true")
@FeignClient(contextId = "argumentFeignClient", name = "bite-admin")
public interface ArgumentFeignClient {

    /**
     * 获取参数列表
     *
     * @param argReadReqDTO 参数读请求
     * @return 参数列表
     */
    @PostMapping("/argument/list")
    R<BasePageVO<ArgVO>> argumentList(@RequestBody ArgReadReqDTO argReadReqDTO);

    /**
     * 编辑参数
     *
     * @param argWriteReqDTO 参数写请求
     * @return 参数的 id
     */
    @PostMapping("/argument/edit")
    R<Long> argumentEdit(@RequestBody ArgWriteReqDTO argWriteReqDTO);

    /**
     * 添加参数
     *
     * @param argWriteReqDTO 参数写请求
     * @return 参数的 id
     */
    @PostMapping("/argument/add")
    R<Long> argumentAdd(@RequestBody ArgWriteReqDTO argWriteReqDTO);

    /**
     * 根据一个参数键获取对应的参数值
     *
     * @param configKey 参数键
     * @return 参数值
     */
    @GetMapping("/argument/configKey/{configKey}")
    R<ArgVO> argumentByConfigKey(@PathVariable("configKey") @NotBlank(message = "参数键为空！") String configKey);

    /**
     * 根据多个参数键获取对应的参数值
     *
     * @param argListReqDTO 参数键列表
     * @return 参数值列表
     */
    @PostMapping("/argument/configKeys")
    R<List<ArgVO>> argumentByConfigKeys(@RequestBody @Validated ArgListReqDTO argListReqDTO);


}
