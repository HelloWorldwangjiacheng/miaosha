
```sql
CREATE TABLE `goods` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
	`goods_name` VARCHAR(16) DEFAULT NULL ,
	`goods_title` VARCHAR(64) DEFAULT NULL ,
	`goods_img` VARCHAR(64) DEFAULT NULL ,
	`goods_detail` LONGTEXT  ,
	`goods_price` DECIMAL(10,2) DEFAULT '0.00',
	`goods_stock` INT(11) DEFAULT '0' ,
	PRIMARY KEY(`id`)
) ENGINE=INNODB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;	


CREATE TABLE `miaosha_goods` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
	`goods_id` BIGINT(20) DEFAULT NULL,
	`miaosha_price` DECIMAL(10,2) DEFAULT '0.00', 
	`stock_count` INT(11) DEFAULT NULL ,
	`start_date` DATETIME DEFAULT NULL,
	`end_date` DATETIME DEFAULT NULL,
	PRIMARY KEY(`id`)
) ENGINE=INNODB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;	

CREATE TABLE `order_info` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
	`user_id` BIGINT(20) DEFAULT NULL ,
	`goods_id` BIGINT(20) DEFAULT NULL ,
	`delivery_addr_id` BIGINT(20) DEFAULT NULL ,
	`goods_name` VARCHAR(16) DEFAULT NULL ,
	`goods_count` INT(11) DEFAULT '0',
	`goods_price` DECIMAL(10,2) DEFAULT '0.00' ,
	`order_channel` TINYINT(4) DEFAULT '0' ,
	`status` TINYINT(4) DEFAULT '0',
	`create_date` DATETIME DEFAULT  NULL,
	`pay_date` DATETIME DEFAULT NULL,
	PRIMARY KEY(`id`)
) ENGINE=INNODB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `miaosha_order` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
	`user_id` BIGINT(20) DEFAULT NULL ,
	`order_id` BIGINT(20) DEFAULT NULL,
	`goods_id` BIGINT(20) DEFAULT NULL,
	PRIMARY KEY(`id`)
) ENGINE=INNODB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

```


```text
//查看redis服务是否启动
ps -ef | grep redis  

 //起了100个并发，100000个请求
redis-benchmark -h 127.0.0.1 -p 6379 -c 100 -n 100000   

//存取大小为100字节的数据包
redis-benchmark -h 127.0.0.1 -p 6379 -q -d 100   

//只测试某些操作的性能
redis-benchmark -t set,lpush -q -n 100000 

//只测试某些数值存取的性能
redis-benchmark -n 100000 -q script load "redis.call('set','foo','bar')"  



```