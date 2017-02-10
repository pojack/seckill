DELIMITER $$
CREATE PROCEDURE `seckill`.`execute_seckill`
(IN v_seckill_id bigint,in v_phone bigint,in v_kill_time timestamp,out r_result int)
  BEGIN
    DECLARE insert_count int DEFAULT 0;
    DECLARE update_count int DEFAULT 0;
    START TRANSACTION;
      insert ignore into successkill
      (seckill_id,user_phone,create_time) values
      (v_seckill_id,v_phone,v_kill_time);
    SELECT row_count() into insert_count;
    if(insert_count = 0) THEN
      ROLLBACK ;
      set r_result = -1;
    ELSEIF (insert_count<0) THEN
      ROLLBACK ;
      SET r_result = -2;
    ELSE
      UPDATE seckill.seckill
        SET number=number-1
      WHERE seckill_id=v_seckill_id
      AND v_kill_time<seckill.end_time
      AND seckill.number>0
      AND v_kill_time>seckill.start_time;
      SELECT row_count() into update_count;
      IF (update_count=0) THEN
        ROLLBACK ;
        SET r_result=0;
      ELSEIF (update_count<0) THEN
        ROLLBACK ;
        SET r_result=-2;
      ELSE
        COMMIT ;
        SET r_result=1;
      END IF;
    END IF;
  END ;
$$
DELIMITER ;

