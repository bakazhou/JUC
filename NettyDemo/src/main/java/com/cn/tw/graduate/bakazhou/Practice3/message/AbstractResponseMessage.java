package com.cn.tw.graduate.bakazhou.Practice3.message;

import lombok.ToString;

@ToString(callSuper = true)
public abstract class AbstractResponseMessage extends Message {
    private boolean success;
    private String reason;

    public boolean isSuccess() {
        return success;
    }

    public String getReason() {
        return reason;
    }

    public AbstractResponseMessage() {
    }

    public AbstractResponseMessage(boolean success, String reason) {
        this.success = success;
        this.reason = reason;
    }
}
