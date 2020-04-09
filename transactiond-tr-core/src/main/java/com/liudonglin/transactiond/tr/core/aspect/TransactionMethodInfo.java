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

public class TransactionMethodInfo {
    private static final Map<String, TransactionMethodInfo> methodInfoCache = new ConcurrentReferenceHashMap<>();

    private TransactionModel transactionModel;

    /**
     * 事务传播特性
     */
    private Propagation propagation;

    /**
     * 事务执行器
     */
    private Class targetClazz;

    /**
     *
     */
    private Object[] argumentValues;

    /**
     * 参数类型
     */
    private Class[] parameterTypes;


    /**
     * 用户实例对象的业务方法（包含注解信息）
     */
    private Method method;

    private String unitId;

    public static String APPLICATION_ID_WHEN_RUNNING = "td";

    private TransactionMethodInfo(Method method, Object[] args, Class<?> targetClass) {
        this.targetClazz = targetClass;
        this.argumentValues = args;
        this.parameterTypes = method.getParameterTypes();
        this.method = method;
        this.unitId = unitId(method.toString());

        LcnTransaction lcnTransaction = method.getAnnotation(LcnTransaction.class);
        if(lcnTransaction!=null) {
            this.transactionModel = TransactionModel.LCN;
            this.propagation = lcnTransaction.propagation();
            return;
        }

        TccTransaction tccTransaction = method.getAnnotation(TccTransaction.class);
        if(tccTransaction!=null) {
            this.transactionModel = TransactionModel.TCC;
            this.propagation = tccTransaction.propagation();
            return;
        }

    }

    private void reanalyseMethodArgs(Object[] args) {
        this.argumentValues = args;
    }

    public static TransactionMethodInfo getFromCache(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String signature = proceedingJoinPoint.getSignature().toString();
        String unitId = unitId(signature);
        TransactionMethodInfo dtxInfo = methodInfoCache.get(unitId);
        if (Objects.isNull(dtxInfo)) {
            MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
            Method method = methodSignature.getMethod();

            Class<?> targetClass = proceedingJoinPoint.getTarget().getClass();
            Method thisMethod = targetClass.getMethod(method.getName(), method.getParameterTypes());
            dtxInfo = new TransactionMethodInfo(thisMethod, proceedingJoinPoint.getArgs(), targetClass);
            methodInfoCache.put(unitId, dtxInfo);
        }
        dtxInfo.reanalyseMethodArgs(proceedingJoinPoint.getArgs());
        return dtxInfo;
    }

    public static TransactionMethodInfo getFromCache(MethodInvocation methodInvocation) {
        String signature = methodInvocation.getMethod().toString();
        String unitId = unitId(signature);
        TransactionMethodInfo dtxInfo = methodInfoCache.get(unitId);
        if (Objects.isNull(dtxInfo)) {
            dtxInfo = new TransactionMethodInfo(methodInvocation.getMethod(),
                    methodInvocation.getArguments(), methodInvocation.getThis().getClass());
            methodInfoCache.put(unitId, dtxInfo);
        }
        dtxInfo.reanalyseMethodArgs(methodInvocation.getArguments());
        return dtxInfo;
    }

    public static TransactionMethodInfo getFromCache(Method method, Object[] args, Class<?> targetClass) {
        String signature = method.getName();
        String unitId = unitId(signature);
        TransactionMethodInfo dtxInfo = methodInfoCache.get(unitId);
        if (Objects.isNull(dtxInfo)) {
            dtxInfo = new TransactionMethodInfo(method, args, targetClass);
            methodInfoCache.put(unitId, dtxInfo);
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

    public Propagation getPropagation() {
        return propagation;
    }

    public String getUnitId() {
        return unitId;
    }

    public Class getTargetClazz() {
        return targetClazz;
    }

    public Object[] getArgumentValues() {
        return argumentValues;
    }

    public Class[] getParameterTypes() {
        return parameterTypes;
    }

    public Method getMethod() {
        return method;
    }
}
