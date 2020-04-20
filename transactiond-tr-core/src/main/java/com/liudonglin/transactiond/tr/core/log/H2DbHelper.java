package com.liudonglin.transactiond.tr.core.log;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Slf4j
@Component
public class H2DbHelper implements DisposableBean {

    private final HikariDataSource hikariDataSource;

    private final QueryRunner queryRunner;

    @Autowired
    public H2DbHelper(H2DbProperties h2DbProperties) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(org.h2.Driver.class.getName());
        hikariConfig.setJdbcUrl(String.format("jdbc:h2:%s", h2DbProperties.getFilePath()));
        hikariDataSource = new HikariDataSource(hikariConfig);
        queryRunner = new QueryRunner(hikariDataSource);
        log.info("Init H2 DATABASE at {}", h2DbProperties.getFilePath());
    }

    public QueryRunner queryRunner() {
        return queryRunner;
    }

    public int update(String sql, Object... params) {
        try {
            return queryRunner.update(sql, params);
        } catch (SQLException e) {
            log.error("update error", e);
            return 0;
        }
    }


    public <T> T query(String sql, ResultSetHandler<T> rsh, Object... params) {
        try {
            return queryRunner.query(sql, rsh, params);
        } catch (SQLException e) {
            log.error("query error", e);
            return null;
        }
    }

    public <T> T query(String sql, ScalarHandler<T> scalarHandler, Object... params) {
        try {
            return queryRunner.query(sql, scalarHandler, params);
        } catch (SQLException e) {
            log.error("query error", e);
            return null;
        }
    }

    @Override
    public void destroy() throws Exception {
        hikariDataSource.close();
        log.info("log hikariDataSource close.");
    }
}
