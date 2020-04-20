package com.liudonglin.transactiond.tr.core.log;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class AspectLogHelper {

    private final H2DbHelper h2DbHelper;

    @Autowired
    public AspectLogHelper(H2DbHelper h2DbHelper) {
        this.h2DbHelper = h2DbHelper;
    }

    public void init() {
        h2DbHelper.update("CREATE TABLE IF NOT EXISTS TXLCN_LOG " +
                "(" +
                "ID BIGINT NOT NULL AUTO_INCREMENT, " +
                "UNIT_ID VARCHAR(32) NOT NULL," +
                "GROUP_ID VARCHAR(64) NOT NULL," +
                "METHOD_STR VARCHAR(512) NOT NULL ," +
                "BYTES BLOB NOT NULL," +
                "GROUP_ID_HASH BIGINT NOT NULL," +
                "UNIT_ID_HASH BIGINT NOT NULL," +
                "TIME BIGINT NOT NULL, " +
                "PRIMARY KEY(ID) )");
        log.info("Aspect log table finished (H2 DATABASE)");
    }


    public boolean save(AspectLog txLog) {
        String insertSql = "INSERT INTO TXLCN_LOG(UNIT_ID,GROUP_ID,BYTES,METHOD_STR,GROUP_ID_HASH,UNIT_ID_HASH,TIME) VALUES(?,?,?,?,?,?,?)";
        return h2DbHelper.update(insertSql, txLog.getUnitId(), txLog.getGroupId(), txLog.getBytes(), txLog.getMethodStr(), txLog.getGroupId().hashCode(), txLog.getUnitId().hashCode(), txLog.getTime()) > 0;
    }

    public boolean deleteAll() {
        String sql = "DELETE FROM TXLCN_LOG";
        return h2DbHelper.update(sql) > 0;
    }

    public void trancute() {
        String sql = "TRUNCATE TABLE TXLCN_LOG";
        h2DbHelper.update(sql);
    }

    public boolean delete(long id) {
        String sql = "DELETE FROM TXLCN_LOG WHERE ID = ?";
        return h2DbHelper.update(sql, id) > 0;
    }

    public boolean delete(long groupIdHash, long unitIdHash) {
        String sql = "DELETE FROM TXLCN_LOG WHERE GROUP_ID_HASH = ? and UNIT_ID_HASH = ?";
        return h2DbHelper.update(sql, groupIdHash, unitIdHash) > 0;
    }

    public boolean delete(String groupId) {
        String sql = "DELETE FROM TXLCN_LOG WHERE GROUP_ID = ?";
        return h2DbHelper.update(sql, groupId) > 0;
    }

    public List<AspectLog> findAll() {
        String sql = "SELECT * FROM TXLCN_LOG";
        return h2DbHelper.query(sql, resultSet -> {
            List<AspectLog> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(fill(resultSet));
            }
            return list;
        });
    }

    public long count() {
        String sql = "SELECT count(*) FROM TXLCN_LOG";
        return h2DbHelper.query(sql, new ScalarHandler<Long>());
    }

    public AspectLog getTxLog(String groupId, String unitId) {
        String sql = "SELECT * FROM TXLCN_LOG WHERE GROUP_ID = ? and UNIT_ID = ?";
        return h2DbHelper.query(sql, resultSetHandler, groupId, unitId);
    }

    public AspectLog getTxLog(long id) {
        String sql = "SELECT * FROM TXLCN_LOG WHERE ID = ?";
        return h2DbHelper.query(sql, resultSetHandler, id);
    }

    private final ResultSetHandler<AspectLog> resultSetHandler = resultSet -> {
        if (resultSet.next()) {
            return fill(resultSet);
        }
        return null;
    };


    private AspectLog fill(ResultSet resultSet) throws SQLException {
        AspectLog txLog = new AspectLog();
        txLog.setBytes(resultSet.getBytes("BYTES"));
        txLog.setGroupId(resultSet.getString("GROUP_ID"));
        txLog.setMethodStr(resultSet.getString("METHOD_STR"));
        txLog.setTime(resultSet.getLong("TIME"));
        txLog.setUnitId(resultSet.getString("UNIT_ID"));
        txLog.setGroupIdHash(resultSet.getLong("GROUP_ID_HASH"));
        txLog.setUnitIdHash(resultSet.getLong("UNIT_ID_HASH"));
        txLog.setId(resultSet.getLong("ID"));
        return txLog;
    }

}
