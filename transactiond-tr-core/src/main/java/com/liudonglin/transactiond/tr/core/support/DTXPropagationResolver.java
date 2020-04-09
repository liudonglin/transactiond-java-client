package com.liudonglin.transactiond.tr.core.support;

import com.liudonglin.transactiond.tr.core.annotation.Propagation;
import com.liudonglin.transactiond.tr.core.context.DTXLocalContext;
import com.liudonglin.transactiond.tr.core.transaction.DTXTransactionInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DTXPropagationResolver {

    public DTXPropagationState resolvePropagationState(DTXTransactionInfo txTransactionInfo) {

        // 本地已在DTX，根据事务传播，静默加入
        if (DTXLocalContext.cur().isInGroup()) {
            log.info("SILENT_JOIN group!");
            return DTXPropagationState.SILENT_JOIN;
        }

        // 发起方之前没有事务
        if (txTransactionInfo.isTransactionStart()) {
            // 根据事务传播，对于 SUPPORTS 不参与事务
            if (Propagation.SUPPORTS.equals(txTransactionInfo.getPropagation())) {
                return DTXPropagationState.NON;
            }
            // 根据事务传播，创建事务
            return DTXPropagationState.CREATE;
        }

        // 已经存在DTX，根据事务传播，加入
        return DTXPropagationState.JOIN;
    }
}
