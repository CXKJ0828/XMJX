package com.zhjh.mapper;

import com.zhjh.entity.MaterialBackOrderFormMap;
import com.zhjh.mapper.base.BaseMapper;

import java.util.List;

public interface MaterialBackOrderMapper extends BaseMapper{
    public List<MaterialBackOrderFormMap> selectTimeSlot();
    public MaterialBackOrderFormMap selectByStartAndEndTime(MaterialBackOrderFormMap materialBackOrderFormMap);
    public MaterialBackOrderFormMap selectByDeliveryDay(String day);
    public void deleteById(String id);

}
