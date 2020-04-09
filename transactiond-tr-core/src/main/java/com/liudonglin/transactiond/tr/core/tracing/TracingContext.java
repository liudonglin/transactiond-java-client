package com.liudonglin.transactiond.tr.core.tracing;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.*;

@Slf4j
public class TracingContext {

    public static final String GROUP_ID = "groupId";

    public static final String APP_MAP = "appMap";

    public static final String HEADER_KEY_GROUP_ID = "X-Group-ID";

    public static final String HEADER_KEY_APP_MAP = "X-App-Map";

    private static ThreadLocal<TracingContext> tracingContextThreadLocal = new ThreadLocal<>();

    private TracingContext() {

    }

    public static TracingContext tracing() {
        if (tracingContextThreadLocal.get() == null) {
            tracingContextThreadLocal.set(new TracingContext());
        }
        return tracingContextThreadLocal.get();
    }

    private Map<String, String> fields;

    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public void beginTransactionGroup() {
        if (hasGroup()) {
            return;
        }

        Map<String,String> map = new HashMap<>();
        map.put(TracingContext.GROUP_ID, getUUID());
        map.put(TracingContext.APP_MAP, "{}");
        init(map);
    }

    public static void init(Map<String, String> initFields) {

        // return if null fields.
        if (Objects.isNull(initFields)) {
            log.warn("init tracingContext fail. null init fields.");
            return;
        }
        TracingContext tracingContext = tracing();
        if (Objects.isNull(tracingContext.fields)) {
            tracingContext.fields = new HashMap<>();
        }
        tracingContext.fields.putAll(initFields);
    }

    /**
     * 判断是否有事务组
     *
     * @return result
     */
    public boolean hasGroup() {
        return Objects.nonNull(fields) && fields.containsKey(TracingContext.GROUP_ID) &&
                StringUtils.hasText(fields.get(TracingContext.GROUP_ID));
    }

    /**
     * 获取事务组ID
     *
     * @return groupId
     */
    public String groupId() {
        if (hasGroup()) {
            return fields.get(TracingContext.GROUP_ID);
        }
        raiseNonGroupException();
        return "";
    }

    /**
     * 所有的字段
     *
     * @return fields
     */
    public Map<String, String> fields() {
        return this.fields;
    }

    /**
     * 添加App
     *
     * @param serviceId serviceId
     * @param address   address
     */
    public void addApp(String serviceId, String address) {
        if (hasGroup()) {
            JSONObject map = JSON.parseObject(this.fields.get(TracingContext.APP_MAP));
            if (map.containsKey(serviceId)) {
                return;
            }
            map.put(serviceId, address);
            this.fields.put(TracingContext.APP_MAP, JSON.toJSONString(map));
            return;
        }
        raiseNonGroupException();
    }

    /**
     * JSON Type App map.
     *
     * @return appMap
     */
    public JSONObject appMap() {
        return JSON.parseObject(appMapString());
    }

    /**
     * String Type App map.
     *
     * @return appMap
     */
    public String appMapString() {
        if (hasGroup()) {
            String appMap = Optional.ofNullable(this.fields.get(TracingContext.APP_MAP)).orElse("");
            log.debug("App map: {}", appMap);
            return appMap;
        }
        raiseNonGroupException();
        return "{}";
    }

    /**
     * 销毁当前线程Tracing信息
     */
    public void destroy() {
        if (Objects.nonNull(tracingContextThreadLocal.get())) {
            tracingContextThreadLocal.remove();
        }
    }

    private void raiseNonGroupException() {
        throw new IllegalStateException("non group id.");
    }

}
