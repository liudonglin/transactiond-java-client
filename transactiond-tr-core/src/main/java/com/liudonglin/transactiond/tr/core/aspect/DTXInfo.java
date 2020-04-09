package com.liudonglin.transactiond.tr.core.aspect;

import com.liudonglin.transactiond.tr.core.annotation.LcnTransaction;
import com.liudonglin.transactiond.tr.core.annotation.Propagation;
import com.liudonglin.transactiond.tr.core.annotation.TccTransaction;
import com.liudonglin.transactiond.tr.core.transaction.TransactionModel;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.DigestUtils;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

public class DTXInfo {
    private static final Map<String, DTXInfo> dtxInfoCache = new ConcurrentReferenceHashMap<>();

    private TransactionModel transactionModel;

    private Propagation transactionPropagation;

    private TransactionInfo transactionInfo;

    /**
     * 用户实例对象的业务方法（包含注解信息）
     */
    private Method businessMethod;

    private String unitId;

    public static String APPLICATION_ID_WHEN_RUNNING = "td";

    private DTXInfo(Method method, Object[] args, Class<?> targetClass) {
        this.transactionInfo = new TransactionInfo();
        this.transactionInfo.setTargetClazz(targetClass);
        this.transactionInfo.setArgumentValues(args);
        this.transactionInfo.setMethod(method.getName());
        this.transactionInfo.setMethodStr(method.toString());
        this.transactionInfo.setParameterTypes(method.getParameterTypes());
        this.businessMethod = method;
        this.unitId = unitId(method.toString());

        LcnTransaction lcnTransaction = method.getAnnotation(LcnTransaction.class);
        if(lcnTransaction!=null) {
            this.transactionModel = TransactionModel.LCN;
            this.transactionPropagation = lcnTransaction.propagation();
            return;
        }

        TccTransaction tccTransaction = method.getAnnotation(TccTransaction.class);
        if(tccTransaction!=null) {
            this.transactionModel = TransactionModel.TCC;
            this.transactionPropagation = tccTransaction.propagation();
            return;
        }

    }

    private void reanalyseMethodArgs(Object[] args) {
        this.transactionInfo.setArgumentValues(args);
    }

    public static DTXInfo getFromCache(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String signature = proceedingJoinPoint.getSignature().toString();
        String unitId = unitId(signature);
        DTXInfo dtxInfo = dtxInfoCache.get(unitId);
        if (Objects.isNull(dtxInfo)) {
            MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
            Method method = methodSignature.getMethod();

            Class<?> targetClass = proceedingJoinPoint.getTarget().getClass();
            Method thisMethod = targetClass.getMethod(method.getName(), method.getParameterTypes());
            dtxInfo = new DTXInfo(thisMethod, proceedingJoinPoint.getArgs(), targetClass);
            dtxInfoCache.put(unitId, dtxInfo);
        }
        dtxInfo.reanalyseMethodArgs(proceedingJoinPoint.getArgs());
        return dtxInfo;
    }

    public static DTXInfo getFromCache(MethodInvocation methodInvocation) {
        String signature = methodInvocation.getMethod().toString();
        String unitId = unitId(signature);
        DTXInfo dtxInfo = dtxInfoCache.get(unitId);
        if (Objects.isNull(dtxInfo)) {
            dtxInfo = new DTXInfo(methodInvocation.getMethod(),
                    methodInvocation.getArguments(), methodInvocation.getThis().getClass());
            dtxInfoCache.put(unitId, dtxInfo);
        }
        dtxInfo.reanalyseMethodArgs(methodInvocation.getArguments());
        return dtxInfo;
    }

    public static DTXInfo getFromCache(Method method, Object[] args, Class<?> targetClass) {
        String signature = method.getName();
        String unitId = unitId(signature);
        DTXInfo dtxInfo = dtxInfoCache.get(unitId);
        if (Objects.isNull(dtxInfo)) {
            dtxInfo = new DTXInfo(method, args, targetClass);
            dtxInfoCache.put(unitId, dtxInfo);
        }
        dtxInfo.reanalyseMethodArgs(args);
        return dtxInfo;
    }

    public static String unitId(String methodSignature) {
        return DigestUtils.md5DigestAsHex((APPLICATION_ID_WHEN_RUNNING + methodSignature).getBytes());
    }

    public TransactionModel getTransactionModel() {
        return transactionModel;
    }

    public Propagation getTransactionPropagation() {
        return transactionPropagation;
    }

    public TransactionInfo getTransactionInfo() {
        return transactionInfo;
    }

    public Method getBusinessMethod() {
        return businessMethod;
    }

    public String getUnitId() {
        return unitId;
    }
}
