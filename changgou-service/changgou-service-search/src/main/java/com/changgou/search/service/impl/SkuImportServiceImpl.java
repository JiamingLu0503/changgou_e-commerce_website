package com.changgou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.search.dao.SkuImportMapper;
import com.changgou.search.pojo.SearchResultMapperImpl;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.search.service.SkuImportService;
import entity.Result;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class SkuImportServiceImpl implements SkuImportService {

    @Autowired
    private SkuImportMapper skuImportMapper;

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private ElasticsearchTemplate esTemplate;

    @Override
    public Map<String, Object> search(Map<String, String> searchMap) {
        //1.创建查询对象 的构建对象
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //2.设置查询的条件
        if (searchMap != null && searchMap.size() > 0) {
            //keyword, category, brand, spec要实现筛选过滤
            if (!StringUtils.isEmpty(searchMap.get("keywords"))) {
                boolQueryBuilder.filter(QueryBuilders.queryStringQuery(searchMap.get("keywords")).field("name"));
            }
            if (!StringUtils.isEmpty(searchMap.get("category"))) {
                boolQueryBuilder.filter(QueryBuilders.termQuery("categoryName", searchMap.get("category")));
            }
            if (!StringUtils.isEmpty(searchMap.get("brand"))) {
                boolQueryBuilder.filter(QueryBuilders.termQuery("brandName", searchMap.get("brand")));
            }
            for (String key : searchMap.keySet()) {
                if (key.startsWith("spec_")) {
                    String value = searchMap.get(key).replace("\\", "");
                    boolQueryBuilder.filter(QueryBuilders
                            .termQuery("specMap." + key.substring(5) + ".keyword", value));
                }
            }
            //价格区间上的筛选
            String price = searchMap.get("price");
            if (!StringUtils.isEmpty(price)) {
                String[] prices = price.replace("元", "").replace("以上", "").split("-");
                if (prices != null && prices.length > 0) {
                    boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gt(Integer.parseInt(prices[0])));
                    if (prices.length > 1) {
                        boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").lte(Integer.parseInt(prices[1])));
                    }
                }
            }
        }
        nativeSearchQueryBuilder.withFilter(boolQueryBuilder);
        //构建排序查询
        if(searchMap != null) {
            String sortRule = searchMap.get("sortRule");
            String sortField = searchMap.get("sortField");
            if (!StringUtils.isEmpty(sortRule) && !StringUtils.isEmpty(sortField)) {
                nativeSearchQueryBuilder.withSort(
                        SortBuilders.fieldSort(sortField).order(sortRule.equals("DESC") ? SortOrder.DESC : SortOrder.ASC));
            }
        }
        //构建分页查询
        Integer pageNum = 1;
        if (searchMap != null && !StringUtils.isEmpty(searchMap.get("pageNum"))) {
            try {
                pageNum = Integer.valueOf(searchMap.get("pageNum"));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        Integer pageSize = 30;
        nativeSearchQueryBuilder.withPageable(PageRequest.of(pageNum - 1, pageSize));

        //设置高亮条件
        HighlightBuilder.Field field = new HighlightBuilder.Field("name");
        //前缀及后缀
        field.preTags("<em style=\"color:red\">");
        field.postTags("</em>");
        //碎片长度，关键词数据的长度
        field.fragmentSize(100);
        nativeSearchQueryBuilder.withHighlightFields(field);

        //3.创建查询对象
        NativeSearchQuery query = nativeSearchQueryBuilder.build();
        //4.执行查询
        AggregatedPage<SkuInfo> skuPage = esTemplate.queryForPage(query, SkuInfo.class, new SearchResultMapperImpl());
        //5.创建返回结果
        Map<String, Object> resultMap = new HashMap<>();
        //5.1聚合查询Category
        if (searchMap == null || StringUtils.isEmpty(searchMap.get("category"))) {
            List<String> categoryList = getSkuCategoryList(nativeSearchQueryBuilder);
            resultMap.put("categoryList", categoryList);
        }
        //5.2聚合查询Brand
        if (searchMap == null || StringUtils.isEmpty(searchMap.get("brand"))) {
            List<String> brandList = getSkuBrandList(nativeSearchQueryBuilder);
            resultMap.put("brandList", brandList);
        }
        //5.3聚合查询SpecMap
        Map<String, Set<String>> specMap = getSkuSpecMap(nativeSearchQueryBuilder);
        //6.返回结果
        //分页数据保存
        //设置当前页码
        resultMap.put("pageNum", pageNum);
        resultMap.put("pageSize", pageSize);
        resultMap.put("rows", skuPage.getContent());
        resultMap.put("total", skuPage.getTotalElements());
        resultMap.put("totalPages", skuPage.getTotalPages());
        resultMap.put("specMap", specMap);
        return resultMap;
    }

    /**
     * 聚合查询Brand
     * .addAggregation():添加一个聚合操作
     * 1)取别名
     * 2)表示根据哪个域进行聚合查询
     */
    private List<String> getSkuBrandList(NativeSearchQueryBuilder nativeSearchQueryBuilder) {
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuBrand")
                .field("brandName").size(50));
        //构建查询对象
        NativeSearchQuery query = nativeSearchQueryBuilder.build();
        //执行查询
        AggregatedPage<SkuInfo> skuPage = esTemplate.queryForPage(query, SkuInfo.class);
        //获取brand aggregation
        StringTerms stringTerms = skuPage.getAggregations().get("skuBrand");
        List<String> brandList = new ArrayList<>();
        for (StringTerms.Bucket bucket : stringTerms.getBuckets()) {
            // a single brand name
            String categoryName = bucket.getKeyAsString();
            brandList.add(categoryName);
        }
        return brandList;
    }

    /**
     * 聚合查询Category
     * .addAggregation():添加一个聚合操作
     * 1)取别名
     * 2)表示根据哪个域进行聚合查询
     */
    private List<String> getSkuCategoryList(NativeSearchQueryBuilder nativeSearchQueryBuilder) {
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuCategory")
                .field("categoryName").size(50));
        //构建查询对象
        NativeSearchQuery query = nativeSearchQueryBuilder.build();
        //执行查询
        AggregatedPage<SkuInfo> skuPage = esTemplate.queryForPage(query, SkuInfo.class);
        //获取category aggregation
        StringTerms stringTerms = skuPage.getAggregations().get("skuCategory");
        List<String> categoryList = new ArrayList<>();
        for (StringTerms.Bucket bucket : stringTerms.getBuckets()) {
            // a single category name
            String categoryName = bucket.getKeyAsString();
            categoryList.add(categoryName);
        }
        return categoryList;
    }

    /**
     * 聚合查询SpecMap
     * .addAggregation():添加一个聚合操作
     * 1)取别名
     * 2)表示根据哪个域进行聚合查询
     */
    private Map<String, Set<String>> getSkuSpecMap(NativeSearchQueryBuilder nativeSearchQueryBuilder) {
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuSpec").field("spec.keyword")
                .size(10000));
        //构建查询对象
        NativeSearchQuery query = nativeSearchQueryBuilder.build();
        //执行查询
        AggregatedPage<SkuInfo> skuPage = esTemplate.queryForPage(query, SkuInfo.class);
        //获取specMap aggregation
        StringTerms stringTerms = skuPage.getAggregations().get("skuSpec");
        Map<String, Set<String>> specMap = new HashMap<>();
        for (StringTerms.Bucket bucket : stringTerms.getBuckets()) {
            Map<String, String> oneSpecMap = JSON.parseObject(bucket.getKeyAsString(), Map.class);
            for (Map.Entry<String, String> entry : oneSpecMap.entrySet()) {
                if (!specMap.containsKey(entry.getKey())) {
                    specMap.put(entry.getKey(), new HashSet<>());
                }
                specMap.get(entry.getKey()).add(entry.getValue());
            }
        }
        return specMap;
    }

    /**
     * 导入sku数据到es
     */
    @Override
    public void importSku() {
        //Feign调用，查询List<Sku>
        Result<List<Sku>> allSkus = skuFeign.findByStatus("1");
        //将数据转成search.Sku
        List<SkuInfo> allSkuInfos = JSON.parseArray(JSON.toJSONString(allSkus.getData()), SkuInfo.class);
        //spec string -> map of specs
        for (SkuInfo skuInfo : allSkuInfos) {
            skuInfo.setSpecMap(JSON.parseObject(skuInfo.getSpec(), Map.class));
        }
        //调用Dao实现数据批量导入
        skuImportMapper.saveAll(allSkuInfos);
    }

    @Override
    public void deleteList(List<Sku> list) {
        for (Sku sku : list) {
            skuImportMapper.deleteById(sku.getId());
        }
    }
}
