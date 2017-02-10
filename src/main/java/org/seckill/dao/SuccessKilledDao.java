package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.SuccessKilled;

import java.util.Date;

public interface SuccessKilledDao {
	/**
	 * 插入购买成功记录
	 * @param seckillId
	 * @param userPhone
	 * @return 影响行数
	 */
	public int insertSuccessKilled(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);
	/**
	 * 根据产品id查找秒杀记录，并返回携带产品实体的successkilled
	 * @param seckillId
	 * @return
	 */
	SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);
}
