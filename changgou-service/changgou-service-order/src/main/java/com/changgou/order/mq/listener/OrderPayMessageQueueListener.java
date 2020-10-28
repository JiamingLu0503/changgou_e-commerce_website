package com.changgou.order.mq.listener;

import com.alibaba.fastjson.JSON;
import com.changgou.order.service.OrderService;
import com.changgou.pay.feign.WexinPayFeign;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = {"${mq.pay.queue.order}"})
public class OrderPayMessageQueueListener {

    @Autowired
    private OrderService orderService;

    @Autowired
    private WexinPayFeign wexinPayFeign;

    /***
     * 接收消息
     */
    @RabbitHandler
    public void consumeMessage(String msg) throws Exception {
        //将数据转成Map
        Map<String, String> result = JSON.parseObject(msg, Map.class);
        //返回状态码
        String returnCode = result.get("return_code");
        //业务结果
        String resultCode = result.get("result_code");
        if (returnCode.equalsIgnoreCase("success")) {
            //获取订单号
            String outtradeno = result.get("out_trade_no");
            if (!resultCode.equalsIgnoreCase("success")) {
                //关闭订单时服务器返回的数据
                Map<String, String> closeResult = wexinPayFeign.closeOrder(outtradeno).getData();
                //如果错误代码为ORDERPAID则说明订单已经支付, 当作正常订单处理, 反之回滚库存
                if (!("FAIL".equals(closeResult.get("result_code")) &&
                        "ORDERPAID".equals(closeResult.get("err_code")))) {
                    orderService.deleteOrder(outtradeno);
                    return;
                }
            }
            String transactionId = result.get("transaction_id");   //微信支付订单号
            String timeEnd = result.get("time_end");               //支付完成时间
            orderService.updateStatus(outtradeno, timeEnd, transactionId);
        }
    }
}


