package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {
	@Resource
	private SeckillDao seckilldao;
	@Test
	public void testQueryById() throws Exception{
		long id = 1;
		Seckill seckill = seckilldao.queryById(id);
		System.out.println(seckill.getName());
		System.out.println(seckill);
	}

	@Test
	public void testQueryAll() throws Exception{
		List<Seckill> list = seckilldao.queryAll(0,4);
		for(Seckill seckill:list){
			System.out.println("名称"+seckill.getName());
			System.out.println(seckill);
		}
	}
	@Test
	public void testReduceNumber() throws Exception{
		Date date = new Date();
		int updatecount=seckilldao.reduceNumber(1L,date);
		System.out.println("updatecounts:"+updatecount);
	}
}
