package com.bitejiuyeke.biteadminservice.user.handler;

import com.bitejiuyeke.biteadminservice.user.domain.dto.SysUserDTO;
import com.bitejiuyeke.biteadminservice.user.service.ISysUserService;
import com.bitejiuyeke.bitecommoncore.utils.JsonUtil;
import com.bitejiuyeke.bitecommondomain.exception.ServiceException;
import com.bitejiuyeke.bitecommonrabbitmq.domain.annotation.MqTaskType;
import com.bitejiuyeke.bitecommonrabbitmq.handler.TaskHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@MqTaskType("upload")
public class UploadTaskHandler implements TaskHandler<Boolean> {

    @Autowired
    private ISysUserService sysUserService;

    @Override
    public Boolean handleTask(String payload) {
        SysUserDTO sysUserDTO = JsonUtil.string2Obj(payload, SysUserDTO.class);
        if (sysUserDTO == null) {
            return false;
        }

        try {
            Thread.sleep(1000 * 3);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // todo 处理不成功的解决方案？
        try {
            sysUserService.addOrEdit(sysUserDTO);
        } catch (ServiceException e) {
            log.error("上传用户信息失败，{}", e.getMsg());
            throw e;
        }
        return true;
    }
}
