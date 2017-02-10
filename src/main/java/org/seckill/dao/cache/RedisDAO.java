package org.seckill.dao.cache;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.runtime.RuntimeSchema;
import org.seckill.entity.Seckill;
import org.seckill.service.SeckillService;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.logging.Logger;

/**
 * Created by lp-deepin on 17-2-1.
 */
public class RedisDAO {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
    private final JedisPool jedisPool;
    //protostuff进行序列操作时需要某类的模式，而模式根据字节码文件生成，并且类有一定规范（pojo）
    RuntimeSchema<Seckill> schema=RuntimeSchema.createFrom(Seckill.class);

    public RedisDAO(String ip,int port){
        jedisPool=new JedisPool(ip,port);
    }

    /**
     *根据id得到字节数组--》》反序列化--》》得到对象
     * @param id
     * @return null：redis中没有该对象；
     */
    public Seckill getSeckill(long id){
        Jedis jedis=null;
        try {
            jedis = jedisPool.getResource();
            String key = "seckill:"+id;
            byte[] bytes=jedis.get(key.getBytes());

            if (bytes!=null){
                //得到空对象
                Seckill seckill = schema.newMessage();
                //将字节数组反序列化为对象（按照模式）
                ProtostuffIOUtil.mergeFrom(bytes,seckill,schema);
                return seckill;
            }
        }catch (Exception e){
            logger.error("redis错误！",e);
        }
        finally{
            if(jedis!=null)
            jedis.close();
        }
        return null;
    }


    public String putSeckill(Seckill seckill){
        Jedis jedis=null;
        try {
            jedis=jedisPool.getResource();
            String key = "seckill:"+seckill.getSeckillId();
            //序列化
            byte[] bytes = ProtostuffIOUtil.toByteArray(seckill,schema,
                    LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
            //存入redis
            String result = jedis.setex(key.getBytes(),60*60,bytes);
            return result;
        }catch (Exception e){
            logger.error("redis错误！",e);
        } finally {
            if(jedis!=null)
            jedis.close();
        }
        return null;
    }
}
