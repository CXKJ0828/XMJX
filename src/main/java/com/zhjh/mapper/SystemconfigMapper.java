package com.zhjh.mapper;

import com.zhjh.entity.SystemconfigFormMap;
import com.zhjh.mapper.base.BaseMapper;

public interface SystemconfigMapper extends BaseMapper{
    public SystemconfigFormMap findByName(String name);
}
