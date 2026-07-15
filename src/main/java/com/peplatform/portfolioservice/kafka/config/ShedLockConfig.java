//package com.peplatform.portfolioservice.kafka.config;
//
//
//import net.javacrumbs.shedlock.core.LockProvider;
//import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
//import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jdbc.core.JdbcTemplate;
//
//import javax.sql.DataSource;
//
/// **
// * <p>
// * The class ShadeLockConfig implements the business logic for managing locks with Spring's ShedLock library.
// * It provides a configuration bean to configure and use locks in your application.
// * <p>
// * The lock provider uses JdbcTemplate to interact with the underlying database and ensures that lock acquisition is performed at most within 2 minutes.
// * <p>
// * This class is designed for internal use only and does not handle HTTP requests or other external interactions.
// *
// * @author Randjith
// * @version 1.0.0
// * @email randjithkumar@no-reply.github.com
// * @date 2026.07.14
// * @since 0.0.1-SNAPSHOT
// */
//@Configuration
//@EnableSchedulerLock(defaultLockAtMostFor = "PT2M")
//public class ShedLockConfig {
//
//    /**
//     * Configures the default lock provider for an application using JDBC.
//     *
//     * @param dataSource The data source to be used by the lock provider
//     * @return An instance of LockProvider configured with JDBC
//     */
//    @Bean(name = "kafkaLockProvider")
//    public LockProvider lockProvider(DataSource dataSource) {
//        return new JdbcTemplateLockProvider(
//                JdbcTemplateLockProvider.Configuration.builder()
//                        .withJdbcTemplate(new JdbcTemplate(dataSource))
//                        .usingDbTime()
//                        .build()
//        );
//    }
//}