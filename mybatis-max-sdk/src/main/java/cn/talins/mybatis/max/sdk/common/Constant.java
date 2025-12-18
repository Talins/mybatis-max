package cn.talins.mybatis.max.sdk.common;

import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;

import java.util.HashMap;
import java.util.Map;


/**
 * 常量定义类 - 存储框架使用的常量和模板
 * <p>
 * 该类定义了动态生成Entity和Mapper所需的模板，以及表与数据源的映射关系。
 * </p>
 * 
 * @author talins
 * @see cn.talins.mybatis.max.sdk.DynamicMapperUtil 使用这些模板的工具类
 */
public class Constant {

    /**
     * 模板引擎实例（使用Hutool的Enjoy模板引擎）
     */
    public static final TemplateEngine TEMPLATE_ENGINE = TemplateUtil.createEngine();
    
    /**
     * Entity类模板
     * <p>
     * 生成的Entity类继承自BaseEntity，包含：
     * <ul>
     *     <li>serialVersionUID字段</li>
     *     <li>业务字段（private修饰）</li>
     *     <li>getter/setter方法</li>
     * </ul>
     * </p>
     * 
     * <p>
     * 模板变量：
     * <ul>
     *     <li>entityName: 实体类名（如User）</li>
     *     <li>fieldList: 字段列表，每个字段包含name、upperName、type</li>
     * </ul>
     * </p>
     */
    public static final Template ENTITY_TEMPLATE = TEMPLATE_ENGINE.getTemplate("package entity;\npublic class #(entityName) extends cn.talins.mybatis.max.api.pojo.BaseEntity {\nprivate static final long serialVersionUID = 1;\n#for(field : fieldList)private #(field.type) #(field.name);public #(field.type) get#(field.upperName)() {return #(field.name);}\npublic void set#(field.upperName)(#(field.type) #(field.name)) {this.#(field.name) = #(field.name);}\n#end}");
    
    /**
     * Mapper接口模板
     * <p>
     * 生成的Mapper接口继承自MyBatis-Plus的BaseMapper，
     * 自动获得基础的CRUD方法。
     * </p>
     * 
     * <p>
     * 模板变量：
     * <ul>
     *     <li>entityName: 实体类名（如User），Mapper名为UserMapper</li>
     * </ul>
     * </p>
     */
    public static final Template MAPPER_TEMPLATE = TEMPLATE_ENGINE.getTemplate("package mapper;\npublic interface #(entityName)Mapper extends com.baomidou.mybatisplus.core.mapper.BaseMapper<entity.#(entityName)> {}");
    
    /**
     * 表名到数据源名称的映射
     * <p>
     * 在多数据源场景下，记录每个表属于哪个数据源。
     * 执行SQL时根据表名自动切换到对应的数据源。
     * </p>
     */
    public static final Map<String, String> TABLE_DATASOURCE_MAP = new HashMap<>();
}
