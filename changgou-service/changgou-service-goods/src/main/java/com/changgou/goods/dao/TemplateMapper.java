package com.changgou.goods.dao;
import com.changgou.goods.pojo.Template;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

/****
 * @Author:传智播客
 * @Description:Template的Dao
 * @Date 2019/6/14 0:12
 *****/
public interface TemplateMapper extends Mapper<Template> {

    /***
     * 查询分类对应的模版
     */
    @Select("SELECT tt.* FROM tb_category tc,tb_template tt WHERE tc.id=#{categoryid} AND tc.template_id=tt.id")
    Template findByCategory(Integer categoryid);
}
