package com.zhjh.mapper;

import com.zhjh.entity.ScrapFormMap;
import com.zhjh.mapper.base.BaseMapper;

public interface ScrapMapper extends BaseMapper{
    public ScrapFormMap getByWorkersubmitId(String workersubmitId);
    public ScrapFormMap getByWorkersubmitHeattreatId(String workersubmitHeattreatId);
}
