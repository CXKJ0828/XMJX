package com.zhjh.mapper;

import com.zhjh.bean.ComboboxEntity;
import com.zhjh.entity.OrderDetailsFormMap;
import com.zhjh.mapper.base.BaseMapper;
import javafx.scene.control.CheckBox;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderDetailsMapper extends BaseMapper{
    public float getTrackAmountSum(OrderDetailsFormMap orderDetailsFormMap);
    public List<ComboboxEntity> getClientSimpleNameTrack(OrderDetailsFormMap orderDetailsFormMap);
    public void updateTrackIsShowByIdsAndState(@Param("state")String state,
                                               @Param("ids")String ids);
    public int getTrackCount(OrderDetailsFormMap orderDetailsFormMap);
    public List<OrderDetailsFormMap> getTrackContent(OrderDetailsFormMap orderDetailsFormMap);
    public OrderDetailsFormMap getSumAmountOrderDetailsByClientId(OrderDetailsFormMap orderDetailsFormMap);
    public String getOrderDetailsIdsByClientIdAndDeliveryTime(OrderDetailsFormMap orderDetailsFormMap);
    public OrderDetailsFormMap getAllByContractNumberAndMapNumber(@Param("contractNumber")String contractNumber,
                                                                                      @Param("mapNumber")String mapNumber);
    public OrderDetailsFormMap getGoodIdAndOrderdetailsIdByContractNumberAndMapNumber(@Param("contractNumber")String contractNumber,
                                                 @Param("mapNumber")String mapNumber);
    public int getCountByContractNumberAndMapNumber(@Param("contractNumber")String contractNumber,
                                                    @Param("mapNumber")String mapNumber);
    public String findIdByContractNumberAndGoodIdAndDeliveryTime(@Param("contractNumber")String contractNumber,
                                                  @Param("goodId")String goodId,
                                                                 @Param("deliveryTime")String deliveryTime);
    public  void updateHideByIds(String ids);
    public OrderDetailsFormMap getExpectClientGoodAmountSum(OrderDetailsFormMap orderDetailsFormMap);
    public void updateStateById(String id);
    public List<OrderDetailsFormMap> getExpectClientGoodDetails(OrderDetailsFormMap orderDetailsFormMap);
    public List<OrderDetailsFormMap> getExpectClientGood(OrderDetailsFormMap orderDetailsFormMap);
    public int getExpectClientGoodCount(OrderDetailsFormMap orderDetailsFormMap);
    public List<OrderDetailsFormMap> getUnCompleteOrderDetailsByOrderId(OrderDetailsFormMap orderDetailsFormMap);
    public OrderDetailsFormMap findGoodMesageById(OrderDetailsFormMap orderDetailsFormMap);
    public int findStateCountByOrderId(String orderId);
    public int getOrderDetailsCountByClientId(OrderDetailsFormMap orderDetailsFormMap);
    public OrderDetailsFormMap getOrderAndGoodByOrderDetailsId(String orderdetailsId);
    public void deleteByOrderId(String orderId);
    public List<OrderDetailsFormMap> getOrderDetailsByClientId(OrderDetailsFormMap orderDetailsFormMap);
    public OrderDetailsFormMap findById(String id);
    public int getCountUnsendOrderDetailsByClientId(OrderDetailsFormMap OrderFormMap);
    public int getCountUnproductOrderDetailsByClientId(OrderDetailsFormMap OrderFormMap);
    List<OrderDetailsFormMap> getUnsendOrderDetailsNoSitationByClientId(OrderDetailsFormMap OrderFormMap);
    List<OrderDetailsFormMap> getUnsendOrderDetailsByClientId(OrderDetailsFormMap OrderFormMap);
    List<OrderDetailsFormMap> getUnproductOrderDetailsByClientId(OrderDetailsFormMap OrderFormMap);
    public List<OrderDetailsFormMap> getOrderDetailsByOrderId(OrderDetailsFormMap OrderFormMap);
}
