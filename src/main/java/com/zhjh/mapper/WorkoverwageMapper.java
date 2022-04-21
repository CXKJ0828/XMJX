package com.zhjh.mapper;

import com.zhjh.entity.WorkoverwageFormMap;
import com.zhjh.mapper.base.BaseMapper;

import java.util.List;

public interface WorkoverwageMapper extends BaseMapper{
    public void deleteById(String id);
    public WorkoverwageFormMap findAllSumByAllLike(WorkoverwageFormMap workoverwageFormMap);
    public List<WorkoverwageFormMap> findByAllLike(WorkoverwageFormMap workoverwageFormMap);
    public int findCountByAllLike(WorkoverwageFormMap workoverwageFormMap);

}
