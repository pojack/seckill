package org.seckill.service.impl;

import org.apache.commons.collections4.MapUtils;
import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dao.cache.RedisDAO;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LLPP on 2016/12/21.
 */
@Service
public class SeckillServiceImpl implements SeckillService {
    @Autowired
    private RedisDAO redisDAO;
    @Autowired
    private SeckillDao seckillDao;
    @Autowired
    private SuccessKilledDao successKilledDao;
    private Logger logger;
    private final String salt = "15sq8e9";
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0,5);
    }

    public Seckill getSeckillById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    public Exposer exposeUrl(long seckillId) {

        //优化点：redis缓存。
        //访问redis
        Seckill seckill=redisDAO.getSeckill(seckillId);

        //如果结果存在且redis中没有则放入一个
        if(seckill==null){
            //访问数据库
            seckill=seckillDao.queryById(seckillId);
            if(seckill==null){
                return new Exposer(false,seckillId);
            }
            //放入redis
            redisDAO.putSeckill(seckill);
        }

        long nowTime = new Date().getTime();
        long startTime = seckill.getStartTime().getTime();
        long endTime = seckill.getEndTime().getTime();
        if( nowTime>endTime||nowTime<startTime)
        {
            return new Exposer(false,seckillId,startTime,endTime,nowTime);
        }

        //使用方法得到一个md5值，对seckillid加密,不可逆的字符串转换过程
        String md5=getMd5(seckillId+"");
        return new Exposer(true,md5,seckillId);
    }


    @Transactional
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException, RepeatKillException, SeckillCloseException {
        //当传入md5的值与系统生成的md5不相匹配的话，抛出异常。
        if(md5==null||!md5.equals(getMd5(""+seckillId))){
            throw new SeckillException("Data rewrite");
        }

        //执行逻辑：修改库存+增加记录。当发生运行时异常，spring会对事务进行回滚
        try{
            int insertCount = successKilledDao.insertSuccessKilled(seckillId,userPhone);//插入记录行数
            if(insertCount<=0){
                //增加秒杀成功记录失败，抛出重复秒杀异常。
                throw new RepeatKillException("Repeat kill");
            }

            Date nowTime = new Date();//系统时间
            int updateCount = seckillDao.reduceNumber(seckillId,nowTime);//减库存影响行数
            if(updateCount<=0){
                //减库存失败，抛出秒杀关闭异常
                throw new SeckillCloseException("Seckill closed");
            }else{
                SuccessKilled successKilled = this.successKilledDao.queryByIdWithSeckill(seckillId,userPhone);
                return new SeckillExecution(seckillId, SeckillStateEnum.SUCCES,successKilled);
            }
        }catch (SeckillCloseException e1){
                throw e1;
        }catch (RepeatKillException e2){
            throw e2;
        }catch(Exception e){
            logger.error(e.getMessage(),e);
            throw new SeckillException("seckill inner error"+e.getMessage());
        }
    }

    /**
     * 用存储过程进行秒杀
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return
     */
    public SeckillExecution executeSeckillByProcedure(long seckillId, long userPhone, String md5) {
        //当传入md5的值与系统生成的md5不相匹配时，返回秒杀结果，告知controller数据篡改。
        if(md5==null||!md5.equals(getMd5(""+seckillId))){
            return new SeckillExecution(seckillId,SeckillStateEnum.DATA_REWRITE);
        }
        Date killtime = new Date();
        Map<String,Object> map = new HashMap<String,Object>();

        //将需传入的参数放入map
        map.put("seckillId",seckillId);
        map.put("phone",userPhone);
        map.put("killTime",killtime);
        map.put("result",null);

        try{
            //调用dao的秒杀存储过程方法,result被赋值
            seckillDao.killByProcedure(map);
            int result = MapUtils.getInteger(map,"result",-2);
            //根据返回的result值进行处理。
            if(result==1){
                //秒杀成功时，返回秒杀记录。
                SuccessKilled sk = successKilledDao.queryByIdWithSeckill(seckillId,userPhone);
                return new SeckillExecution(seckillId,SeckillStateEnum.stateOf(result),sk);
            }else{
                //秒杀失败。
                return new SeckillExecution(seckillId,SeckillStateEnum.stateOf(result));
            }
        }catch (Exception e){
            //处理存储过程发生的异常
            logger.error(e.getMessage(),e);
            return new SeckillExecution(seckillId,SeckillStateEnum.INNER_ERROR);
        }
    }

    /**
     * 传入一个字符串配合盐值生成一个md5
     * @param parameter
     * @return
     */
    private String getMd5(String parameter){
        String base= parameter+"/"+this.salt;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }
}
