package com.changgou.pay.service.impl;

import com.changgou.pay.entity.MyWXPayConfig;
import com.changgou.pay.service.WeixinPayService;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import entity.HttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class WeixinPayServiceImpl implements WeixinPayService {

    @Value("${weixin.appid}")
    private String appid;

    @Value("${weixin.partner}")
    private String partner;

    @Value("${weixin.partnerkey}")
    private String partnerkey;

    @Value("${weixin.notifyurl}")
    private String notifyurl;

    /*****
     * 创建二维码
     * @param parameterMap
     * @return
     */
    @Override
    public Map<String, String> createNative(Map<String, String> parameterMap) {
        try {
            //1、封装参数
            Map<String, String> param = new HashMap<>();
            param.put("appid", appid);                                  //应用ID
            param.put("mch_id", partner);                               //商户ID号
            param.put("nonce_str", WXPayUtil.generateNonceStr());       //随机数
            param.put("body", "畅购");                                   //订单描述
            param.put("out_trade_no", parameterMap.get("outtradeno"));   //商户订单号
            param.put("total_fee", parameterMap.get("totalfee"));        //交易金额
            param.put("spbill_create_ip", "127.0.0.1");                 //终端IP
            param.put("notify_url", notifyurl);                         //回调地址
            param.put("trade_type", "NATIVE");                          //交易类型

            //2、将参数转成xml字符，并携带签名
            String paramXml = WXPayUtil.generateSignedXml(param, partnerkey);

            ///3、执行请求
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            httpClient.setHttps(true);
            httpClient.setXmlParam(paramXml);
            httpClient.post();

            //4、获取参数
            String content = httpClient.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(content);
            System.out.println("stringMap:" + resultMap);
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * 查询订单状态
     * @param outtradeno : 客户端自定义订单编号
     * @return
     */
    @Override
    public Map<String, String> queryPayStatus(String outtradeno) {
        try {
            //1.封装参数
            Map<String, String> param = new HashMap<>();
            param.put("appid", appid);                               //应用ID
            param.put("mch_id", partner);                            //商户号
            param.put("out_trade_no", outtradeno);                   //商户订单编号
            param.put("nonce_str", WXPayUtil.generateNonceStr());    //随机字符

            //2、将参数转成xml字符，并携带签名
            String paramXml = WXPayUtil.generateSignedXml(param, partnerkey);

            //3、发送请求
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            httpClient.setHttps(true);
            httpClient.setXmlParam(paramXml);
            httpClient.post();

            //4、获取返回值，并将返回值转成Map
            String content = httpClient.getContent();
            return WXPayUtil.xmlToMap(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 关闭订单
     * @param outtradeno 商户订单号
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, String> closeOrder(String outtradeno) throws Exception {
        Map<String, String> map = new HashMap<>(16);
        //商户订单号
        map.put("out_trade_no", outtradeno);
        MyWXPayConfig config = new MyWXPayConfig(appid, partner, partnerkey);
        WXPay wxpay = new WXPay(config, notifyurl);
        return wxpay.closeOrder(map);
    }

}
