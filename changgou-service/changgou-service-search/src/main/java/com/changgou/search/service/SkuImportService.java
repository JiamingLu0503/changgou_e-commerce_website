package com.changgou.search.service;

import com.changgou.goods.pojo.Sku;

import java.util.List;
import java.util.Map;

public interface SkuImportService {

    /***
     * 搜索
     * @param searchMap
     * @return
     */
    Map search(Map<String, String> searchMap);

    /***
     * 导入SKU数据
     */
    void importSku();

    /**
     * 删除Sku集合
     * @param list
     * @return
     */
    void deleteList(List<Sku> list);
}
