package com.liudonglin.transactiond.tr.core.log;

import lombok.Data;

@Data
public class AspectLog {

    /**
     * id自增主键
     */
    private long id;
    /**
     * groupId hash值
     */
    private long groupIdHash;
    /**
     * unitId hash值
     */
    private long unitIdHash;

    /**
     * 事务单元Id
     */
    private String unitId;
    /**
     * 事务组Id
     */
    private String groupId;
    /**
     * 切面序列化数据
     */
    private byte[] bytes;

    /**
     * 切面方法名称
     */
    private String methodStr;

    /**
     * 保存时间
     */
    private long time;

}
