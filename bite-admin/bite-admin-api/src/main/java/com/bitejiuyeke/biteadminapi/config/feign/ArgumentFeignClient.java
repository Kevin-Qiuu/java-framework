package com.bitejiuyeke.biteadminapi.config.feign;

import com.bitejiuyeke.biteadminapi.config.domain.dto.ArgDTO;
import com.bitejiuyeke.biteadminapi.config.domain.dto.ArgReadReqDTO;
import com.bitejiuyeke.biteadminapi.config.domain.dto.ArgWriteReqDTO;
import com.bitejiuyeke.biteadminapi.config.domain.vo.ArgVO;
import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitecommondomain.domain.vo.BasePageVO;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@ConditionalOnProperty(name = "feign.client.mapFeign.enabled", havingValue = "true")
@FeignClient(name = "bite-admin")
public interface ArgumentFeignClient {

    /**
     * 添加参数
     * @param argWriteReqDTO 参数写请求
     * @return 参数的 id
     */
    @PostMapping("/argument/add")
    R<Long> argumentAdd(@RequestBody @Validated ArgWriteReqDTO argWriteReqDTO);

    /**
     * 获取参数列表
     * @param argReadReqDTO 参数读请求
     * @return 参数列表
     */
    @GetMapping("/argument/list")
    R<BasePageVO<ArgVO>> argumentList(@RequestBody ArgReadReqDTO argReadReqDTO);

    /**
     * 编辑参数
     * @param argWriteReqDTO 参数写请求
     * @return 参数的 id
     */
    @PostMapping("/argument/edit")
    R<Long> argumentEdit(@RequestBody @Validated ArgWriteReqDTO argWriteReqDTO);
}
