package com.changgou.search.controller;

import com.changgou.search.feign.SkuInfoFeign;
import com.changgou.search.pojo.SkuInfo;
import entity.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping(value = "/search")
public class SkuSearchController {

    @Autowired
    private SkuInfoFeign skuInfoFeign;

    /**
     * 搜索
     * @param searchMap
     * @return
     */
    @GetMapping(value = "/list")
    public String search(@RequestParam(required = false) Map<String, String> searchMap, Model model) {
        //替换特殊字符
        handleSearchMap(searchMap);
        //调用changgou-service-search微服务
        Map resultMap = skuInfoFeign.search(searchMap);
        //搜索数据结果
        model.addAttribute("result", resultMap);
        //搜索条件存储，用于页面回显数据
        model.addAttribute("searchMap", searchMap);
        //请求地址
        String[] Uris = getUri(searchMap);
        model.addAttribute("Uri", Uris[0]);
        model.addAttribute("pagerUri", Uris[1]);
        //分页计算
        Page<SkuInfo> page = new Page<>(
                Long.parseLong(resultMap.get("totalPages").toString()), //总记录数
                Integer.parseInt(resultMap.get("pageNum").toString()),  //当前页
                Integer.parseInt(resultMap.get("pageSize").toString())  //每页显示条数
        );
        model.addAttribute("page", page);
        return "search";
    }

    /**
     * Uri组装和处理
     * @param searchMap
     * @return
     */
    private String[] getUri(Map<String, String> searchMap) {
        String Uri = "/search/list", pagerUri = "/search/list";
        if (searchMap != null && searchMap.size() > 0) {
            Uri += "?";
            pagerUri += "?";
            for (Map.Entry<String, String> entry : searchMap.entrySet()) {
                //跳过排序
                if (entry.getKey().equalsIgnoreCase("sortField") ||
                        entry.getValue().equalsIgnoreCase("sortRule")) {
                    pagerUri += entry.getKey() + "=" + entry.getValue() + "&";
                    continue;
                }
                //跳过分页
                if(entry.getKey().equalsIgnoreCase("pageNum")) {
                    continue;
                }
                Uri += entry.getKey() + "=" + entry.getValue() + "&";
                pagerUri += entry.getKey() + "=" + entry.getValue() + "&";
            }
            if(Uri.lastIndexOf("&") != -1) {
                Uri = Uri.substring(0, Uri.length() - 1);
            }
            if(pagerUri.lastIndexOf("&") != -1) {
                pagerUri = pagerUri.substring(0, pagerUri.length() - 1);
            }
        }
        return new String[]{Uri, pagerUri};
    }

    /**
     * 替换特殊字符
     * @param searchMap
     */
    public void handleSearchMap(Map<String, String> searchMap) {
        if(searchMap != null) {
            for(Map.Entry<String, String> entry : searchMap.entrySet()) {
                if(entry.getKey().startsWith("spec_")) {
                    entry.setValue(entry.getValue().replace("+", "%2B"));
                }
            }
        }
    }
}

