package org.seckill.enums;

/**
 * 使用枚举表示我们的常量，数据字典
 * Created by LLPP on 2016/12/21.
 */
public enum SeckillStateEnum {
    SUCCES(1,"秒杀成功"),
    END(0,"秒杀结束"),
    INNER_ERROR(-2,"系统异常"),
    REPEAT_KILL(-1,"重复秒杀"),
    DATA_REWRITE(-3,"数据篡改");
    private int state;
    private String stateInfo;
    SeckillStateEnum(int state,String stateInfo){
        this.state=state;
        this.stateInfo=stateInfo;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public static SeckillStateEnum stateOf(int index){
        for (SeckillStateEnum state: values()) {
            if(state.getState()==index){
                return state;
            }
        }
        return null;
    }
}
