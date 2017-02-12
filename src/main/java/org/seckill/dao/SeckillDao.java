package org.seckill.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Seckill;

public interface SeckillDao {
	/**
	 * 根据传入的seckilledid减少库存，返回影响的行数。当killtime>endtime或重复秒杀返回值为零
	 * @param seckillId
	 * @param killTime
	 * @return
	 */
	public int reduceNumber(@Param("seckillId")long seckillId,@Param("killTime")Date killTime);
	/**
	 * 查询所有商品
	 * @param offset
	 * @param limit
	 * @return
	 */
	public List<Seckill> queryAll(@Param("offset") int offset, @Param("limit")int limit);
	/**
	 * 根据库存id查找商品
	 * @param seckillId
	 * @return
	 */
	public Seckill queryById(long seckillId);

	public void killByProcedure(Map<String,Object> paramMap);
}
