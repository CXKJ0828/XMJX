package com.zhjh.mapper;

import com.zhjh.entity.OrderDetailsFormMap;
import com.zhjh.entity.OrderDetailsProcessFormMap;
import com.zhjh.mapper.base.BaseMapper;

import java.util.List;

public interface OrderDetailsProcessMapper extends BaseMapper{
    public int findCountByCompleteUserNameAndStartTimeAndEndTime(OrderDetailsProcessFormMap orderDetailsProcessFormMap);
    public void deleteByOrderId(String orderId);
    public int getStateCountByOrderdetailsprocessId(OrderDetailsProcessFormMap orderDetailsProcessFormMap);
    public float getProcessAllTimeByOrderId(OrderDetailsProcessFormMap orderDetailsProcessFormMap);
    public List<OrderDetailsProcessFormMap> findProcessStateAndProcessByOrderId(OrderDetailsProcessFormMap orderDetailsProcessFormMap);
    public List<OrderDetailsProcessFormMap> findByCompleteUserNameAndStartTimeAndEndTime(OrderDetailsProcessFormMap orderDetailsProcessFormMap);
    public List<OrderDetailsProcessFormMap> findAllUseTimeAndAllAmountByCompleteUserNameAndStartTimeAndEndTime(OrderDetailsProcessFormMap orderDetailsProcessFormMap);
    public List<OrderDetailsProcessFormMap> getSumUseTimesByOrderId(OrderDetailsProcessFormMap orderDetailsProcessFormMap);
    public List<OrderDetailsProcessFormMap> getProcessDetailsByOrderId(OrderDetailsProcessFormMap orderDetailsProcessFormMap);
    public OrderDetailsProcessFormMap findTimeAndAmountByCompleteUserIdAndStartTimeAndEndTime(OrderDetailsProcessFormMap orderDetailsProcessFormMap);
    public List<OrderDetailsProcessFormMap> findByCompleteUserIdAndStartTimeAndEndTime(OrderDetailsProcessFormMap orderDetailsProcessFormMap);
    public List<OrderDetailsProcessFormMap> getDetailsByBatchNumberAndRoleId(OrderDetailsProcessFormMap orderDetailsProcessFormMap);
    public List<OrderDetailsProcessFormMap> getDetailsByOrderDetailsId(OrderDetailsProcessFormMap orderDetailsProcessFormMap);
}
