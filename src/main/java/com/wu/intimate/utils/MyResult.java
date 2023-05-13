package com.wu.intimate.utils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 全局统一返回结果类
 *
 */
@Data
@ApiModel(value = "全局统一返回结果")
public class MyResult<T> {

    @ApiModelProperty(value = "返回码")
    private Integer code;

    @ApiModelProperty(value = "返回消息")
    private String message;

    @ApiModelProperty(value = "返回数据")
    private T data;

    public MyResult(){}

    // 返回数据
    protected static <T> MyResult<T> build(T data) {
        MyResult<T> myResult = new MyResult<T>();
        if (data != null)
            myResult.setData(data);
        return myResult;
    }

    public static <T> MyResult<T> build(T body, ResultCodeEnum resultCodeEnum) {
        MyResult<T> myResult = build(body);
        myResult.setCode(resultCodeEnum.getCode());
        myResult.setMessage(resultCodeEnum.getMessage());
        return myResult;
    }

    public static<T> MyResult<T> ok(){
        return MyResult.ok(null);
    }

    /**
     * 操作成功
     * @param data
     * @param <T>
     * @return
     */
    public static<T> MyResult<T> ok(T data){
        MyResult<T> myResult = build(data);
        return build(data, ResultCodeEnum.SUCCESS);
    }

    public static<T> MyResult<T> fail(){
        return MyResult.fail(null);
    }

    /**
     * 操作失败
     * @param data
     * @param <T>
     * @return
     */
    public static<T> MyResult<T> fail(T data){
        MyResult<T> myResult = build(data);
        return build(data, ResultCodeEnum.FAIL);
    }

    public MyResult<T> message(String msg){
        this.setMessage(msg);
        return this;
    }

    public MyResult<T> code(Integer code){
        this.setCode(code);
        return this;
    }

    public boolean isOk() {
        if(this.getCode().intValue() == ResultCodeEnum.SUCCESS.getCode().intValue()) {
            return true;
        }
        return false;
    }
}
