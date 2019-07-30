package io.insightchain.inbwallet.mvps.model.vo;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * Created by lijilong on 04/18.
 * VO:值对象（Value Object）通常用于业务层之间的数据传递，和PO一样也是仅仅包含数据而已
 * 表现层对象，主要对应展示界面显示的数据对象，用一个VO对象来封装整个界面展示所需要的对象数据
 */

public class BaseVo implements Serializable {
    protected Long uid;//唯一标识
    protected String uuid;//唯一标识，防止通过uid知道用户数量，uuid相当于对uid进行加密
    protected Timestamp createdTime;//创建时间
    protected Timestamp updatedTime;//更新时间

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Timestamp getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Timestamp updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
