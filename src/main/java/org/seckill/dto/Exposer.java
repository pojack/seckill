package org.seckill.dto;

import org.seckill.entity.Seckill;

/**
 * Created by LLPP on 2016/12/21.
 */
public class Exposer {

    private String md5;

    private long seckillId;

    private boolean exposed;//是否可以暴露标识 true:可以暴露，false:不可暴露

    private long start;

    private long end;
    //系统当前时间（毫秒）
    private long now;

    public Exposer(boolean exposed,long seckillId ,long start, long end, long now) {
        this.exposed = exposed;
        this.start = start;
        this.seckillId=seckillId;
        this.end = end;
        this.now = now;
    }

    public Exposer(boolean exposed,String md5, long seckillId) {
        this.md5 = md5;
        this.seckillId = seckillId;
        this.exposed = exposed;
    }

    public Exposer( boolean exposed,long seckillId) {
        this.seckillId = seckillId;
        this.exposed = exposed;
    }

    public String getMd5() {
        return md5;
    }

    public long getSeckillId() {
        return seckillId;
    }

    public boolean isExposed() {
        return exposed;
    }

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }

    public long getNow() {
        return now;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public void setExposed(boolean exposed) {
        this.exposed = exposed;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public void setNow(long now) {
        this.now = now;
    }

    @Override
    public String toString() {
        return "Exposer{" +
                "md5='" + md5 + '\'' +
                ", seckillId=" + seckillId +
                ", exposed=" + exposed +
                ", start=" + start +
                ", end=" + end +
                ", now=" + now +
                '}';
    }
}
