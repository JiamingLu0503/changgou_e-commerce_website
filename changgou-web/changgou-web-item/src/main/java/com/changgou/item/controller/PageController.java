package com.changgou.item.controller;

import com.changgou.item.service.PageService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/page")
public class PageController {

    @Autowired
    private PageService pageService;

    /**
     * 用于接收请求，测试生成静态页
     * @param id
     * @return
     */
    @RequestMapping("/createHtml/{id}")
    public Result createHtml(@PathVariable(name="id") Long id){
        pageService.createPageHtml(id);
        return new Result(true, StatusCode.OK,"ok");
    }

    /**
     * 删除静态页面
     * @param id
     * @return
     */
    @RequestMapping("/deleteHtml/{id}")
    public Result deleteHtml(@PathVariable(name = "id") Long id) {
        pageService.deletePageHtml(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }
}

