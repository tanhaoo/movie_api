package com.th.utils;

import lombok.Data;

/**
 * @author TanHaooo
 * @date 2021/3/18 16:30
 */
@Data
public class ReturnObject {

    private Integer code = 200;
    private String message = "";
    private Object result;

    public ReturnObject() {
    }

    public ReturnObject(String message, Object result) {
        this.message = message;
        this.result = result;
    }

    public ReturnObject(Object result) {
        this.result = result;
    }
}
