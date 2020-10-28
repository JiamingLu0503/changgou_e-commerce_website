package com.changgou.goods.dao;
import com.changgou.goods.pojo.Sku;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/****
 * @Author:传智播客
 * @Description:Sku的Dao
 * @Date 2019/6/14 0:12
 *****/
public interface SkuMapper extends Mapper<Sku> {

    //需要绑定别名: id和num
    @Update("UPDATE tb_sku SET num=num-#{num} WHERE id=#{id} AND num>=#{num}")
    int decrCount(@Param(value = "id") long id, @Param(value = "num") int num);

    /**
     * 根据sku_id集合查询sku集合
     * @param skuIds
     * @return
     */
    @Select("select * from tb_sku where id in (${skuIds})")
    List<Sku> findBySkuIds(@Param("skuIds") String skuIds);
}
