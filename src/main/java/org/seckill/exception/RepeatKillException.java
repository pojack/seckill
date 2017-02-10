package org.seckill.exception;

/**
 * Created by LLPP on 2016/12/21.
 */
public class RepeatKillException extends SeckillException {
    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }

    public RepeatKillException(String message) {

        super(message);
    }
}
