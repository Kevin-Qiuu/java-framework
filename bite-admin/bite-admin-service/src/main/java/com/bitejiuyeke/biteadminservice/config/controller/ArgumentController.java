package com.bitejiuyeke.biteadminservice.config.controller;

import com.bitejiuyeke.biteadminapi.config.domain.dto.ArgDTO;
import com.bitejiuyeke.biteadminapi.config.domain.dto.ArgReadReqDTO;
import com.bitejiuyeke.biteadminapi.config.domain.dto.ArgWriteReqDTO;
import com.bitejiuyeke.biteadminapi.config.domain.vo.ArgVO;
import com.bitejiuyeke.biteadminapi.config.feign.ArgumentFeignClient;
import com.bitejiuyeke.biteadminservice.config.service.ISysArgumentService;
import com.bitejiuyeke.bitecommoncore.utils.BeanCopyUtil;
import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitecommondomain.domain.dto.BasePageDTO;
import com.bitejiuyeke.bitecommondomain.domain.vo.BasePageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArgumentController implements ArgumentFeignClient {

    @Autowired
    private ISysArgumentService sysArgumentService;

    /**
     * 添加参数
     *
     * @param argWriteReqDTO 参数写请求
     * @return 参数的 id
     */
    @Override
    @PostMapping("/argument/add")
    public R<Long> argumentAdd(@RequestBody @Validated ArgWriteReqDTO argWriteReqDTO) {
        return R.ok(sysArgumentService.argumentAdd(argWriteReqDTO));
    }

    /**
     * 获取参数列表
     *
     * @param argReadReqDTO 参数读请求
     * @return 参数列表
     */
    @Override
    @GetMapping("/argument/list")
    public R<BasePageVO<ArgVO>> argumentList(@RequestBody ArgReadReqDTO argReadReqDTO) {
        BasePageDTO<ArgDTO> basePageDTO = sysArgumentService.argumentList(argReadReqDTO);
        BasePageVO<ArgVO> basePageVO = new BasePageVO<>();
        BeanCopyUtil.copyProperties(basePageDTO, basePageVO);
        basePageVO.setList(BeanCopyUtil.copyListProperties(basePageDTO.getList(), ArgVO::new));
        return R.ok(basePageVO);
    }

    /**
     * 编辑参数
     *
     * @param argWriteReqDTO 参数写请求
     * @return 参数的 id
     */
    @Override
    @PostMapping("/argument/edit")
    public R<Long> argumentEdit(@RequestBody @Validated ArgWriteReqDTO argWriteReqDTO) {
        return R.ok(sysArgumentService.argumentEdit(argWriteReqDTO));
    }

}
