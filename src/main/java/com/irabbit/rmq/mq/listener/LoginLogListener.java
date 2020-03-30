package com.irabbit.rmq.mq.listener;

import com.irabbit.rmq.config.RabbitConfig;
import com.irabbit.rmq.domain.LoginLog;
import com.irabbit.rmq.mq.MessageHelper;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
public class LoginLogListener {

    @RabbitListener(queues = RabbitConfig.LOGIN_LOG_QUEUE_NAME)
    public void consume(Message message, Channel channel) throws IOException {

        log.info("收到消息: {}", message.toString());

        String correlationId = getCorrelationId(message);

        // 消息幂等判定

        MessageProperties properties = message.getMessageProperties();
        long tag = properties.getDeliveryTag();

        // 业务处理
        LoginLog loginLog = MessageHelper.msgToObj(message, LoginLog.class);

        // 消费确认
        channel.basicAck(tag, false);

//        channel.basicNack(tag, false, true);

    }

    /**
     * 获取CorrelationId
     *
     * @param message
     * @return
     */
    private String getCorrelationId(Message message) {
        String correlationId = null;

        MessageProperties properties = message.getMessageProperties();
        Map<String, Object> headers = properties.getHeaders();
        for (Map.Entry entry : headers.entrySet()) {
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            if (key.equals("spring_returned_message_correlation")) {
                correlationId = value;
            }
        }

        return correlationId;
    }
}
