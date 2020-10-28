package com.changgou.pay.feign;

import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@FeignClient("pay")
@RequestMapping("/weixin/pay")
public interface WexinPayFeign {
    /**
     * 关闭订单
     * @param outTradeNo    商户订单号
     * @return
     * @throws Exception
     */
    @RequestMapping("/cancel/order")
    Result<Map<String,String>> closeOrder(String outTradeNo) throws Exception;
}
