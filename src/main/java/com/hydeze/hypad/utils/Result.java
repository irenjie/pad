package com.hydeze.hypad.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/*
 * 统一封装结果
 * code: 状态码，200表示正常
 * msg: 结果反馈，用于成功和错误提示信息
 * data: 返回的数据
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result implements Serializable {
    private int code;
    private String msg;
    private Object data;

    public static Result success(String msg) {
        return success(200, msg, null);
    }

    public static Result success(String msg, Object data) {
        return success(200, msg, data);
    }

    public static Result success(int code, String msg, Object data) {
        return new Result(code, msg, data);
    }

    public static Result fail(String msg) {
        return fail(400, msg, null);
    }

    public static Result fail(int code, String msg, Object data) {
        return new Result(code, msg, data);
    }
}
