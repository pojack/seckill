package org.seckill.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by LLPP on 2016/12/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml"
})
public class SeckillServiceImplTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    SeckillService service;

    @Test
    public void getSeckillList() throws Exception {
        List<Seckill> list = service.getSeckillList();
        logger.info("list={}", list);
    }

    @Test
    public void getSeckillById() throws Exception {
        Seckill seckill = service.getSeckillById(2);
        logger.info("seckill={}", seckill);
    }

    @Test
    public void exposeUrl() throws Exception {
        Exposer exposer = service.exposeUrl(6);
        logger.info("seckill={}", exposer);
    }

    @Test
    public void executeSeckill() throws Exception {
        try {
            service.executeSeckill(6, 15520747616L, "d5e128fdd84d5019ff2333d677fd8283");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Test
    public void integrationTest() throws Exception {
        long seckillId = 6;
        long userPhone = 13124962597L;
        String md5 = "5b678ab38bdabc9eb84665771dfb8641";
        Exposer exposer = service.exposeUrl(6);
        logger.info("seckill={}", exposer);
        if (exposer.isExposed()) {
            try {
                service.executeSeckill(seckillId, userPhone, md5);
            } catch (SeckillCloseException e1) {
                logger.error(e1.getMessage());
            } catch (RepeatKillException e2) {
                logger.error(e2.getMessage());
            } catch (SeckillException e3) {
                logger.error(e3.getMessage());
            }
        }
        Seckill seckill = service.getSeckillById(6);
        logger.info("seckill={}", seckill);
    }

    /**
     * 测试通过存储过程执行秒杀
     */
    @Test
    public void procedureTest(){
        long id = 6;
        long phone=13124962597L;
        String md5 = null;

        Exposer exposer = service.exposeUrl(id);
        md5=exposer.getMd5();

        try {
            if(exposer.isExposed()){
                SeckillExecution ske= service.executeSeckillByProcedure(id,phone,md5);
                logger.info(ske.toString());
            }else{
                throw new Exception("地址暴露失败");
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }
}