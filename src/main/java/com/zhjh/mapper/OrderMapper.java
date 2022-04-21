package com.zhjh.mapper;

import com.zhjh.entity.OrderFormMap;
import com.zhjh.mapper.base.BaseMapper;

import java.util.List;

public interface OrderMapper extends BaseMapper{
    public String findAllMoneyByContentLike(OrderFormMap orderFormMap);
    public int findCountByAllLike(OrderFormMap departmentFormMap);
    public List<OrderFormMap> findByAllLike(OrderFormMap OrderFormMap);
    public List<OrderFormMap> findByContractNumber(String contractNumber);
    public List<OrderFormMap> findByClientId(String clientId);
    public List<OrderFormMap> findUnSendByClientId(OrderFormMap orderFormMap);
    public OrderFormMap findById(String id);
}
