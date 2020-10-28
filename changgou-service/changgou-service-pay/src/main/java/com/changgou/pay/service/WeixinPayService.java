package com.changgou.pay.service;

import java.util.Map;

public interface WeixinPayService {
    /*****
     * 创建二维码
     * @param parameterMap
     * @return
     */
    Map<String, String> createNative(Map<String, String> parameterMap);

    /***
     * 查询订单状态
     * @param outtradeno : 客户端自定义订单编号
     * @return
     */
    Map<String, String> queryPayStatus(String outtradeno);

    /**
     * 关闭订单
     * @param outtradeno    商户订单号
     * @return
     * @throws Exception
     */
    Map<String, String> closeOrder(String outtradeno) throws Exception;
}
