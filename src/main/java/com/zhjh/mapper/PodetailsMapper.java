package com.zhjh.mapper;

import com.zhjh.entity.PodetailsFormMap;
import com.zhjh.mapper.base.BaseMapper;

import java.util.List;

public interface PodetailsMapper extends BaseMapper{
    public List<PodetailsFormMap> findByPoId(PodetailsFormMap podetailsFormMap);
}
