package com.zhjh.mapper;

import com.zhjh.entity.FixedProcessFormMap;
import com.zhjh.entity.MaterialqualitytypeProcessFormMap;
import com.zhjh.mapper.base.BaseMapper;

import java.util.List;

public interface FixedProcessMapper extends BaseMapper{
    public List<FixedProcessFormMap> findByOrigin(String origin);
}
