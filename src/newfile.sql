--创建秒杀数据库
CREATE DATABASE seckill;
--使用秒杀数据库
USE DATABASE seckill;

--创建秒杀库存表，记录商品库存的信息
create　table seckill(
seckill_id bigint NOT NULL AUTO_INCREMENT COMMENT '商品库存id',
name varchar(100) NOT NULL COMMENT '商品名称',
number int NOT NULL COMMENT '库存数量',
start_time timestamp NOT NULL COMMENT '秒杀开始时间',
end_time timestamp NOT NULL COMMENT '秒杀结束时间',
create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',	
PRIMARY KEY (seck_id),
key idx_start_time(start_time),
key idx_end_time(end_time),
key idx_create_time(create_time)
)ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf-8 COMMENT='秒杀库存表';

--创建秒杀成功表,记录秒杀成功的明细信息
CREATE　table successkill(
	seckill_id bigint NOT NULL COMMENT '商品库存id',
	user_phone bigint NOT NULL COMMENT '用户电话',
	state tinyint NOT NULL COMMENT '订单状态1:已发货 0:已付款 -1:无效',--1:已发货 0:已付款 -1:无效
	create_time timestamp NOT NULL COMMENT '创建时间',
	
	primary key (seckill_id,user_phone),--复合主键，可以限制一个用户只能买一次同一商品
	key idx_create_time(create_time)
)ENGINE=InnoDB DEFAULT CHARSET=utf-8 COMMENT='秒杀记录明细表';

create TABLE activity_info(
act_id int NOT NULL AUTO_INCREMENT COMMENT '活动id',
act_name VARCHAR(100) NOT NULL COMMENT '活动名称',
act_type tinyint NOT NULL COMMENT '优惠类型-->0:满减；1:折扣',
discount DOUBLE NULL COMMENT '折扣率',
full_price DOUBLE NULL COMMENT '满价格',
minus_price DOUBLE NULL COMMENT '减价格',
PRIMARY KEY (act_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='活动信息表' AUTO_INCREMENT=10000;

create TABLE act_item_info(
barcode VARCHAR(20) NOT NULL COMMENT '条形码',
act_id int NOT NULL COMMENT '活动id',
PRIMARY KEY (barcode)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='参与活动商品信息';