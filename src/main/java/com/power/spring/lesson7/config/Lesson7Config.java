package com.power.spring.lesson7.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;


/**
 * Created by shenli on 2017/2/4.
 */
@Configuration
@ComponentScan("com.power.spring.lesson7")
@PropertySource("classpath:jdbc.properties")
@EnableTransactionManagement
public class Lesson7Config {

    private static final Logger LOG = LoggerFactory.getLogger(Lesson7Config.class);

    @Autowired
    private Environment env;

    /**
     * 使用DBCP2连接池
     */
    @Bean
    public DataSource dataSource(){
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(env.getProperty("conn.driver"));
        ds.setUsername(env.getProperty("conn.username"));
        ds.setPassword(env.getProperty("conn.password"));
        ds.setUrl(env.getProperty("conn.url"));
        ///设置空闲和借用的连接的最大总数量，同时可以激活。 
        ds.setMaxTotal(60);
        //设置初始大小 
        ds.setInitialSize(1);
        //最小空闲连接 
        ds.setMinIdle(1);
        //最大空闲连接 
        ds.setMaxIdle(16);
        //超时等待时间毫秒 
        ds.setMaxWaitMillis(2*10000);
        //只会发现当前连接失效，再创建一个连接供当前查询使用 
        ds.setTestOnBorrow(true);
        //removeAbandonedTimeout ：超过时间限制，回收没有用(废弃)的连接（默认为 300秒，调整为180） 
        ds.setRemoveAbandonedTimeout(180);
        //removeAbandoned ：超过removeAbandonedTimeout时间后，是否进 行没用连接（废弃）的回收（默认为false，调整为true) 
        //ds.setRemoveAbandonedOnMaintenance(removeAbandonedOnMaintenance); 
        ds.setRemoveAbandonedOnBorrow(true);
        //testWhileIdle 
        ds.setTestOnReturn(true);
        //testOnReturn 
        ds.setTestOnReturn(true);
        //setRemoveAbandonedOnMaintenance 
        ds.setRemoveAbandonedOnMaintenance(true);
        //记录日志 
        ds.setLogAbandoned(true);
        //设置自动提交 
        ds.setDefaultAutoCommit(true);
        // ds.setEnableAutoCommitOnReturn(true);
        LOG.debug("===debug===");
        LOG.info("完成设置数据库连接池ds的参数！！");
        return ds;
    }

    @Bean
    @Autowired
    public JdbcTemplate jdbcTemplate(DataSource dataSource){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate;
    }


    @Bean
    @Autowired
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    @Autowired
    public TransactionTemplate transactionTemplate(PlatformTransactionManager manager){
        TransactionTemplate template = new TransactionTemplate(manager);
        template.setIsolationLevel(TransactionDefinition.ISOLATION_DEFAULT);
        template.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        template.setReadOnly(false);
        return template;
    }

}
