package com.th;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.th.utils.TimeUtil;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

/**
 * @author TanHaooo
 * @date 2021/3/16 15:17
 */
public class TestGenerator {

    @Test
    public void testGenerator() {
        AutoGenerator autoGenerator = new AutoGenerator();
        //全局配置
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setAuthor("TanHaooo")
                .setOutputDir("D:\\Project\\Movie\\movie_api\\src\\main\\java")//设置输出路径
                .setFileOverride(true)//设置文件覆盖
                .setIdType(IdType.AUTO)//设置主键生成策略
                .setServiceName("%sService")//service接口的名称
                .setBaseResultMap(true)//基本结果集合
                .setBaseColumnList(true)//设置基本的列
                .setControllerName("%sController");
        //配置数据源
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDriverName("com.mysql.cj.jdbc.Driver").setUrl("jdbc:mysql://localhost:3306/moviesys?serverTimezone=UTC")
                .setUsername("root").setPassword("123456");
        //策略配置
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.setCapitalMode(true)//设置全局大写命名
                .setNaming(NamingStrategy.underline_to_camel)//数据库表映射到实体的命名策略
                //.setTablePrefix("tb1_")//设置表名前缀
                .setInclude();
        //包名配置
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent("com.th").setMapper("mapper")
                .setService("service").setController("controller")
                .setEntity("bean").setXml("mapper");

        autoGenerator.setGlobalConfig(globalConfig).setDataSource(dataSourceConfig)
                .setStrategy(strategyConfig).setPackageInfo(packageConfig);

        autoGenerator.execute();
    }

    @Test
    public void testTimeStamp() {
        String time = String.valueOf((int) (System.currentTimeMillis() / 1000));
        System.out.println(time);
        //long转date

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long current = Long.parseLong(time);
        Timestamp ts = new Timestamp(current * 1000);// 不除就是精确到ms 这个是到s
        System.out.println(current);
        String dateString = format.format(ts);
        System.out.println(dateString);
    }

    @Test
    public void test() {
        System.out.println(TimeUtil.TimeStampToTimeFormat(
                TimeUtil.TimeFormatToTimeStamp(System.currentTimeMillis())));
    }

}
