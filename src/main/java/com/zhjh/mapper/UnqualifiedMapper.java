package com.zhjh.mapper;

import com.zhjh.entity.UnqualifiedFormMap;
import com.zhjh.mapper.base.BaseMapper;

public interface UnqualifiedMapper extends BaseMapper{
    public UnqualifiedFormMap findByWorkersubmitHeattreatId(String workersubmitHeattreatId);
}
