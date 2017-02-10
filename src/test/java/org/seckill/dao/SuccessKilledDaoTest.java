package org.seckill.dao;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * 配置spring与junit整合。junit运行时将自动加载ioc容器
 * @author LLPP
 *
 */
//junit运行时加载soringioc容器
@RunWith(SpringJUnit4ClassRunner.class)
//告知junit，spring的配置文件位置
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {
	//从容器中获得successKilledDao
	@Resource
	SuccessKilledDao sucdao;
	@Test
	public void testInsertSuccessKilled() {
		int insertCount;
		sucdao.insertSuccessKilled(1,13121312324L);
	}
	@Test
	public void testQueryByIdWithSeckill() {
		SuccessKilled suc = sucdao.queryByIdWithSeckill(1L,13121312324L);
		System.out.println("秒杀成功信息："+suc.toString());
		System.out.println("秒杀商品信息："+suc.getSeckill().toString());
	}

}
