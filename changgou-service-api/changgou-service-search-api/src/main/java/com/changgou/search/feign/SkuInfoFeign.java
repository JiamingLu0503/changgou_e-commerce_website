package com.changgou.search.feign;

import com.changgou.goods.pojo.Sku;
import entity.Result;
import entity.StatusCode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name="search")
@RequestMapping("/search")
public interface SkuInfoFeign {
    /**
     * 搜索
     * @param searchMap
     * @return
     */
    @GetMapping
    Map search(@RequestParam(required = false) Map<String, String> searchMap);

    /**
     * 删除Sku集合
     * @param list
     * @return
     */
    @DeleteMapping
    Result deleteList(@RequestBody List<Sku> list);
}
