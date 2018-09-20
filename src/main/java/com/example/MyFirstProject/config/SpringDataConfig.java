package com.example.MyFirstProject.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:datasource.properties")
public class SpringDataConfig {
//
//    @Autowired
//    private Environment environment;
//
//    @Bean
//    @Primary
//    @ConfigurationProperties(prefix = "spring.datasource")
//    public DataSourceProperties dataSourceProperties() {
//
//        return new DataSourceProperties();
//    }
//
//    @Bean
//    public DataSource dataSource(DataSourceProperties dataSourceProperties) {
//
//        HikariDataSource dataSource = DataSourceBuilder.create().driverClassName(dataSourceProperties.getDriverClassName())
//                .url(dataSourceProperties.getUrl()).username(dataSourceProperties.getUsername()).password(dataSourceProperties.getPassword())
//                .type(HikariDataSource.class).build();
//        dataSource.setMaximumPoolSize(Integer.parseInt(environment.getProperty("spring.datasource.jdbc.maxPoolSize")));
//        return dataSource;
//    }
//
//    @Bean
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, JpaVendorAdapter adapter) {
//
//        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
//        System.out.println(dataSource);
//        factoryBean.setJpaVendorAdapter(adapter);
//        factoryBean.setDataSource(dataSource);
//        factoryBean.setPackagesToScan("com.example.MyFirstProject.model");
//        factoryBean.setJpaProperties(jpaProperties());
//        return factoryBean;
//    }
//
//    @Bean
//    public JpaVendorAdapter jpaVendorAdapter() {
//
//        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
//        return hibernateJpaVendorAdapter;
//    }
//
//    private Properties jpaProperties() {
//
//        Properties properties = new Properties();
//        properties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));
//        properties.put("hibernate.show-sql", environment.getRequiredProperty("hibernate.show-sql"));
//        properties.put("hibernate.ddl-auto", environment.getRequiredProperty("hibernate.ddl-auto"));
//        properties.put("generate-ddl ", true);
//        return properties;
//    }
//
//    @Bean
//    @Autowired
//    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
//
//        JpaTransactionManager txManager = new JpaTransactionManager();
//        txManager.setEntityManagerFactory(emf);
//        return txManager;
//    }
//
}
