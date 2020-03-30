package com.irabbit.rmq.controller;

import com.irabbit.rmq.common.Constant;
import com.irabbit.rmq.config.RabbitConfig;
import com.irabbit.rmq.domain.LoginLog;
import com.irabbit.rmq.domain.MsgLog;
import com.irabbit.rmq.mapper.MsgLogMapper;
import com.irabbit.rmq.mq.MessageHelper;
import com.irabbit.rmq.utils.JodaTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;


@Slf4j
@RestController
@RequestMapping("/mq")
public class MqController {

    @Autowired
    private MsgLogMapper msgLogMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping("/startup")
    public String startUpTest() {
        log.info("started~~~~~~~~~~~~~~");

        sendAndSaveMsg();

        return "startup";
    }

    private void sendAndSaveMsg() {

        String msgId = UUID.randomUUID().toString().replace("-","");

        LoginLog loginLog = new LoginLog();
        loginLog.setUserId(1);
        loginLog.setType(Constant.LogType.LOGIN);
        Date date = new Date();
        loginLog.setDescription("小笑" + "在" + JodaTimeUtil.dateToStr(date) + "登录系统");
        loginLog.setCreateTime(date);
        loginLog.setUpdateTime(date);
        loginLog.setMsgId(msgId);

        CorrelationData correlationData = new CorrelationData(msgId);
        rabbitTemplate.convertAndSend(RabbitConfig.LOGIN_LOG_EXCHANGE_NAME, RabbitConfig.LOGIN_LOG_ROUTING_KEY_NAME, MessageHelper.objToMsg(loginLog), correlationData);

        MsgLog msgLog = new MsgLog(msgId, loginLog, RabbitConfig.LOGIN_LOG_EXCHANGE_NAME, RabbitConfig.LOGIN_LOG_ROUTING_KEY_NAME);
        msgLogMapper.insert(msgLog);
    }
}
