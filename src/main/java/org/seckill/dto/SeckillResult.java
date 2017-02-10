package org.seckill.dto;

/**
 * Created by LLPP on 2016/12/22.
 */
public class SeckillResult<T> {
    private T data;
    private boolean success;
    private String message;

    public SeckillResult( boolean success,T data) {
        this.data = data;
        this.success = success;
    }

    public SeckillResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
