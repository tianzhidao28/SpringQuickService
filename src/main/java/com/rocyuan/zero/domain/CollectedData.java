package com.rocyuan.zero.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by rocyuan on 15/12/11.
 */
@Entity
@Table(name="ss_collected_data")
public class CollectedData extends IdEntity{
    /**
     * 收集的什么类型的数据
     */
    private String type;

    /**
     * 数据
     */
    private String data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
