package com.irabbit.rmq.common;

public class Constant {

    public interface MsgLogStatus {
        Integer DELIVERING = 0; // 消息投递中
        Integer DELIVER_SUCCESS = 1; // 消息投递成功
        Integer DELIVER_FAIL = 2; // 消息投递失败
        Integer CONSUMED_SUCCESS = 3; //  消息已消费
    }

    public interface LogType {
        Integer LOGIN = 1;// 登录
        Integer LOGOUT = 2;// 登出
    }
}
