package com.zhjh.mapper;

import com.zhjh.entity.CidRoleFormMap;
import com.zhjh.entity.OrderGoodCodeFormMap;
import com.zhjh.mapper.base.BaseMapper;

import java.util.List;

public interface OrderGoodCodeMapper extends BaseMapper{
    public List<OrderGoodCodeFormMap> findAllByBatchNumber(OrderGoodCodeFormMap orderGoodCodeFormMap);
}
