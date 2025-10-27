package com.bitejiuyeke.biteadminservice.config.controller;

import com.bitejiuyeke.biteadminapi.config.domain.dto.ArgDTO;
import com.bitejiuyeke.biteadminapi.config.domain.dto.ArgListReqDTO;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @PostMapping("/argument/list")
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
    @PostMapping("/argument/edit")
    public R<Long> argumentEdit(@RequestBody @Validated ArgWriteReqDTO argWriteReqDTO) {
        return R.ok(sysArgumentService.argumentEdit(argWriteReqDTO));
    }


    /**
     * 根据一个参数键获取对应的参数值
     *
     * @param configKey 参数键
     * @return 参数值
     */
    @Override
    @GetMapping("/argument/config_key/{configKey}")
    public R<ArgVO> argumentByConfigKey(@PathVariable("configKey") String configKey) {
        ArgDTO argDTO = sysArgumentService.argumentByConfigKey(configKey);
        if (argDTO == null) {
            return R.ok(null);
        }
        ArgVO argVO = new ArgVO();
        BeanCopyUtil.copyProperties(argDTO, argVO);
        return R.ok(argVO);
    }

    /**
     * 根据多个参数键获取对应的参数值
     *
     * @param argListReqDTO 参数键列表
     * @return 参数值列表
     */
    @Override
    @PostMapping("/argument/config_keys")
    public R<List<ArgVO>> argumentByConfigKeys(@RequestBody ArgListReqDTO argListReqDTO) {
        List<ArgDTO> argDTOs = sysArgumentService.argumentByConfigKeys(argListReqDTO);
        return R.ok(BeanCopyUtil.copyListProperties(argDTOs, ArgVO::new));
    }


}
