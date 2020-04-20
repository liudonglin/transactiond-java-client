package com.liudonglin.transactiond.tr.core.log;

import com.liudonglin.transactiond.tr.core.transaction.DTXTransactionInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class AspectLogger {

    private static final ExecutorService executorService =
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());


    private final AspectLogHelper txLogHelper;

    @Autowired
    public AspectLogger(AspectLogHelper txLogHelper) {
        this.txLogHelper = txLogHelper;

        // 等待线程池任务完成
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            executorService.shutdown();
            try {
                executorService.awaitTermination(6, TimeUnit.SECONDS);
            } catch (InterruptedException ignored) {
            }
        }));
    }

    public void trace(String groupId, String unitId, DTXTransactionInfo transactionInfo) {
        executorService.submit(() -> {
            long t1 = System.currentTimeMillis();
            AspectLog txLog = new AspectLog();
            txLog.setGroupId(groupId);
            txLog.setUnitId(unitId);
            txLog.setMethodStr(transactionInfo.getMethodStr());
            txLog.setTime(System.currentTimeMillis());
            txLog.setUnitIdHash(groupId.hashCode());
            txLog.setUnitIdHash(unitId.hashCode());

            boolean res = txLogHelper.save(txLog);
            long t2 = System.currentTimeMillis();
            log.debug("async save aspect log. result: {} groupId: {}, used time: {}ms", res, groupId, (t2 - t1));
        });
    }

    public void clearLog(String groupId, String unitId) {
        executorService.submit(() -> {
            long t1 = System.currentTimeMillis();
            boolean res = txLogHelper.delete(groupId.hashCode(), unitId.hashCode());
            long t2 = System.currentTimeMillis();
            log.debug("async clear aspect log. result:{}, groupId: {}, used time: {}ms", res, groupId, (t2 - t1));
        });
    }

}
