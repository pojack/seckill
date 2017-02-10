package org.seckill.dao.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dao.SeckillDao;
import org.seckill.entity.Seckill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by lp-deepin on 17-2-5.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class RedisDAOTest {
    @Autowired
    RedisDAO redisDAO;

    @Autowired
    SeckillDao seckillDao;

    @Test
    public void getTestRedisDAO()throws Exception{
        long id = 1;
        Seckill seckill = redisDAO.getSeckill(id);
        if(seckill==null){
            seckill=seckillDao.queryById(id);
            String result = redisDAO.putSeckill(seckill);
            System.out.println(result);
        }
        System.out.println(seckill);
    }
}