package com.changgou.seckill.task;

import com.changgou.seckill.dao.SeckillGoodsMapper;
import com.changgou.seckill.dao.SeckillOrderMapper;
import com.changgou.seckill.pojo.SeckillGoods;
import com.changgou.seckill.pojo.SeckillOrder;
import entity.IdWorker;
import entity.SeckillStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MultiThreadingCreateOrder {

    @Autowired
    private SeckillOrderMapper seckillOrderMapper;

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IdWorker idWorker;

    /***
     * 多线程添加秒杀订单
     */
    @Async
    public void createOrder() {
        //从队列中获取排队信息
        SeckillStatus seckillStatus = (SeckillStatus) redisTemplate.boundListOps("SeckillOrderQueue").rightPop();

        try {
            if (seckillStatus != null) {
                //时间区间
                String time = seckillStatus.getTime();
                //用户登录名
                String username = seckillStatus.getUsername();
                //用户抢购商品
                Long id = seckillStatus.getGoodsId();
                //获取商品数据
                SeckillGoods seckillGood = (SeckillGoods) redisTemplate.boundHashOps("SeckillGoods_" + time).get(id);
                //如果没有库存，则直接抛出异常
                if (seckillGood == null || seckillGood.getStockCount() <= 0) {
                    throw new RuntimeException("已售罄!");
                }

                //如果有库存，则创建秒杀商品订单
                SeckillOrder seckillOrder = new SeckillOrder();
                seckillOrder.setId(idWorker.nextId());
                seckillOrder.setSeckillId(id);
                seckillOrder.setMoney(seckillGood.getCostPrice());
                seckillOrder.setUserId(username);
                seckillOrder.setCreateTime(new Date());
                seckillOrder.setStatus("0");

                //store the order in redis
                redisTemplate.boundHashOps("SeckillOrder").put(username, seckillOrder);

                //抢单成功，更新抢单状态,排队->等待支付
                seckillStatus.setStatus(2);  //等待支付
                seckillStatus.setOrderId(seckillOrder.getId());  //秒杀订单ID
                seckillStatus.setMoney(Float.valueOf(seckillOrder.getMoney()));  //应付金额
                redisTemplate.boundHashOps("UserQueueStatus").put(username, seckillStatus);

                //库存减少
                seckillGood.setStockCount(seckillGood.getStockCount() - 1);
                //判断当前商品是否还有库存,如果已经没有库存了
                if (seckillGood.getStockCount() <= 0) {
                    //将商品数据同步到MySQL中
                    seckillGoodsMapper.updateByPrimaryKeySelective(seckillGood);
                    //清空Redis缓存中该商品数据
                    redisTemplate.boundHashOps("SeckillGoods_" + time).delete(id);
                } else {
                    //如果有库存，则直数据重置到Reids中
                    redisTemplate.boundHashOps("SeckillGoods_" + time).put(id, seckillGood);
                }
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

}
