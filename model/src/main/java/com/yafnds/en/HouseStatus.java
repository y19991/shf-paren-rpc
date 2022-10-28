package com.yafnds.en;

/**
 * 类描述：房源发布状态 枚举类
 * <p>创建时间：2022/10/7 9:39
 *
 * @author yafnds
 * @version 1.0
 */
public enum HouseStatus {

    PUBLISHED(1,"已发布"),
    UNPUBLISHED(0,"未发布");

    public int code;
    public String message;

    HouseStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
