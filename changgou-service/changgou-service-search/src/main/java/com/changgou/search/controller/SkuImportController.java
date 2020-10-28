package com.changgou.search.controller;

import com.changgou.goods.pojo.Sku;
import com.changgou.search.service.SkuImportService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/search")
@CrossOrigin
public class SkuImportController {

    @Autowired
    private SkuImportService skuImportService;

    /**
     * 搜索
     * @param searchMap
     * @return
     */
    @GetMapping
    public Map search(@RequestParam(required = false) Map<String, String> searchMap) {
        return skuImportService.search(searchMap);
    }

    /**
     * 导入sku数据到es
     */
    @GetMapping("/import")
    public Result importSku() {
        skuImportService.importSku();
        return new Result(true, StatusCode.OK, "商品数据导入ES成功");
    }

    /**
     * 删除Sku集合
     * @param list
     * @return
     */
    @DeleteMapping
    public Result deleteList(@RequestBody List<Sku> list) {
        skuImportService.deleteList(list);
        return new Result<>(true,StatusCode.OK,"删除成功");
    }
}
