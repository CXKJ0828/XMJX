package com.zhjh.bean;

import com.zhjh.entity.OrderDetailsFormMap;
import org.apache.commons.beanutils.DynaBean;

import java.util.List;

/**
 * Created by 88888888 on 2019/11/27.
 */
public class OrderEntityChildsBean {
    public OrderDetailsFormMap parentEntity;
    public List<DynaBean> childs;

    public OrderDetailsFormMap getParentEntity() {
        return parentEntity;
    }

    public void setParentEntity(OrderDetailsFormMap parentEntity) {
        this.parentEntity = parentEntity;
    }

    public List<DynaBean> getChilds() {
        return childs;
    }

    public void setChilds(List<DynaBean> childs) {
        this.childs = childs;
    }
}
