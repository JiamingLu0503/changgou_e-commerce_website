package com.changgou.item.feign;

import entity.Result;
import entity.StatusCode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name="item-web")
@RequestMapping("/page")
public interface PageFeign {

    /***
     * 根据SpuID生成静态页
     * @param id
     * @return
     */
    @RequestMapping("/createHtml/{id}")
    Result createHtml(@PathVariable(name="id") Long id);

    /**
     * 删除静态页面
     * @param id
     * @return
     */
    @RequestMapping("/deleteHtml/{id}")
    Result deleteHtml(@PathVariable(name = "id") Long id);
}

