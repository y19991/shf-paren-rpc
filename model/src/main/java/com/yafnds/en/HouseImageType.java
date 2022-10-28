package com.yafnds.en;

/**
 * 包名:com.yafnds.en
 *
 * @author yafnds
 * 日期2022-03-26  22:51
 */
public enum HouseImageType {

    HOUSE_SOURCE(1,"房源图片"),
    HOUSE_PROPERTY(2,"房产图片");

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    HouseImageType(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
