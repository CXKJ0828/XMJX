package com.zhjh.mapper;

import com.zhjh.entity.BadnoticeFormMap;
import com.zhjh.mapper.base.BaseMapper;

public interface BadnoticeMapper extends BaseMapper{
    public BadnoticeFormMap findByWorkersubmitHeattreatId(String workersubmitHeattreatId);
}
