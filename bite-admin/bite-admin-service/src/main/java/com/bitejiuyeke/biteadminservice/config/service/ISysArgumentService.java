package com.bitejiuyeke.biteadminservice.config.service;

import com.bitejiuyeke.biteadminapi.config.domain.dto.ArgDTO;
import com.bitejiuyeke.biteadminapi.config.domain.dto.ArgListReqDTO;
import com.bitejiuyeke.biteadminapi.config.domain.dto.ArgReadReqDTO;
import com.bitejiuyeke.biteadminapi.config.domain.dto.ArgWriteReqDTO;
import com.bitejiuyeke.biteadminapi.config.domain.vo.ArgVO;
import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitecommondomain.domain.dto.BasePageDTO;
import jakarta.validation.constraints.NotBlank;
import org.checkerframework.checker.units.qual.A;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface ISysArgumentService {

    /**
     * 添加参数
     *
     * @param argWriteReqDTO 参数写请求
     * @return 参数的 id
     */
    Long argumentAdd(ArgWriteReqDTO argWriteReqDTO);

    /**
     * 获取参数列表
     *
     * @param argReadReqDTO 参数读请求
     * @return 参数列表
     */
    BasePageDTO<ArgDTO> argumentList(ArgReadReqDTO argReadReqDTO);

    /**
     * 编辑参数
     *
     * @param argWriteReqDTO 参数写请求
     * @return 参数的 id
     */
    Long argumentEdit(ArgWriteReqDTO argWriteReqDTO);

    /**
     * 根据一个参数键获取对应的参数值
     *
     * @param configKey 参数键
     * @return 参数值
     */
    ArgDTO argumentByConfigKey(String configKey);

    /**
     * 根据多个参数键获取对应的参数值
     *
     * @param configKeys 参数键列表
     * @return 参数值列表
     */
    List<ArgDTO> argumentByConfigKeys(ArgListReqDTO configKeys);

}
