package com.bitejiuyeke.bitemstemplateservice.test.controller;

import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitemstemplateservice.domain.MessageDTO;
import com.bitejiuyeke.bitemstemplateservice.test.component.TestRabbitProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/rabbit")
public class TestRabbitMqController {

    @Autowired
    private TestRabbitProducer testRabbitProducer;

    @PostMapping("/produce")
    public R<Void> produceMsg(MessageDTO messageDTO) {
        testRabbitProducer.produceMsg(messageDTO);
        return R.ok();
    }

    @PostMapping("/produceTest")
    public R<Void> produceTestMsg(MessageDTO messageDTO) {
        testRabbitProducer.produceMsgTestQueue(messageDTO);
        return R.ok();
    }

}
