package com.bitejiuyeke.biteadminservice.config.service;

import com.bitejiuyeke.biteadminapi.config.domain.dto.ArgDTO;
import com.bitejiuyeke.biteadminapi.config.domain.dto.ArgReadReqDTO;
import com.bitejiuyeke.biteadminapi.config.domain.dto.ArgWriteReqDTO;
import com.bitejiuyeke.bitecommondomain.domain.dto.BasePageDTO;

public interface ISysArgumentService {

    /**
     * 添加参数
     * @param argWriteReqDTO 参数写请求
     * @return 参数的 id
     */
    Long argumentAdd(ArgWriteReqDTO argWriteReqDTO);

     /**
     * 获取参数列表
     * @param argReadReqDTO 参数读请求
     * @return 参数列表
     */
    BasePageDTO<ArgDTO> argumentList(ArgReadReqDTO argReadReqDTO);

    /**
     * 编辑参数
     * @param argWriteReqDTO 参数写请求
     * @return 参数的 id
     */
    Long argumentEdit(ArgWriteReqDTO argWriteReqDTO);

}
