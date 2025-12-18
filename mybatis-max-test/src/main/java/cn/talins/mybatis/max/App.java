package cn.talins.mybatis.max;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * MyBatis-Max测试应用启动类
 * <p>
 * 该类是mybatis-max-test模块的Spring Boot应用入口，
 * 用于测试和演示MyBatis-Max框架的功能。
 * </p>
 * 
 * <p>
 * 启动后，框架会自动：
 * <ol>
 *     <li>连接配置的数据源</li>
 *     <li>读取数据库表结构</li>
 *     <li>动态生成Entity和Mapper</li>
 *     <li>注册REST API接口</li>
 * </ol>
 * </p>
 * 
 * <p>
 * 可通过以下方式测试：
 * <ul>
 *     <li>访问 /mybatis-max/selectList/{tableName} 查询数据</li>
 *     <li>访问 /mybatis-max/insert/{tableName} 插入数据</li>
 *     <li>运行单元测试验证功能</li>
 * </ul>
 * </p>
 * 
 * @author talins
 */
@SpringBootApplication
public class App {

    /**
     * 应用程序入口
     * 
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
