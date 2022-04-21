package com.zhjh.mapper;

import com.zhjh.entity.MaterialBuyOrderFormMap;
import com.zhjh.mapper.base.BaseMapper;

import java.util.List;

public interface MaterialBuyOrderMapper extends BaseMapper{
    public List<MaterialBuyOrderFormMap> selectTimeSlot();
    public MaterialBuyOrderFormMap selectByDeliveryDay(String day);
    public MaterialBuyOrderFormMap selectByStartAndEndTime(MaterialBuyOrderFormMap materialBuyOrderFormMap);


}
