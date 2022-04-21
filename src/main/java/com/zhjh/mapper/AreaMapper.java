package com.zhjh.mapper;

import com.zhjh.entity.AreaFormMap;
import com.zhjh.mapper.base.BaseMapper;

import java.util.List;

public interface AreaMapper extends BaseMapper{
    public List<AreaFormMap> findByParentId(AreaFormMap areaFormMap);
}
