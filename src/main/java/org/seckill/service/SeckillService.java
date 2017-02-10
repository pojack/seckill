package org.seckill.service;

import org.seckill.entity.Seckill;
import java.util.List;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;

/**
 * Created by LLPP on 2016/12/20.
 */
public interface SeckillService {
    /**
     * 得到所有秒杀产品
     * @return
     */
    public List<Seckill> getSeckillList();

    /**
     * 按Id获取seckill
     * @param seckillId
     * @return
     */
    public Seckill getSeckillById(long seckillId);

    /**
     * 暴露秒杀地址方法:秒杀开启时返回地址，秒杀关闭则返回当前时间与活动时间。
     * @param seckillId
     * @return
     */
    public Exposer exposeUrl(long seckillId);

    /**
     *执行秒杀的方法
     * @param seckillId
     * @param userPhone
     * @param md5 用于防范非法秒杀
     * @return
     */
    public SeckillExecution executeSeckill(long seckillId,long userPhone,String md5) throws
    SeckillException,RepeatKillException,SeckillCloseException;
}
