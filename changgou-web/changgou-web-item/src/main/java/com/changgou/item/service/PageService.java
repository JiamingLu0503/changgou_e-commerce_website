package com.changgou.item.service;

public interface PageService {
    /**
     * 根据商品的ID 生成静态页
     * @param spuId
     */
    public void createPageHtml(Long spuId);

    /**
     * 根据商品的ID 删除静态页
     * @param id
     */
    void deletePageHtml(Long id);
}

