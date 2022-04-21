package com.zhjh.mapper;

import com.zhjh.entity.AttendanceFormMap;
import com.zhjh.entity.HeattreatcheckFormMap;
import com.zhjh.mapper.base.BaseMapper;

import java.util.List;

public interface HeattreatcheckMapper extends BaseMapper{
    public void deleteByIds(String id);
    public HeattreatcheckFormMap findByWorkersubmithearttreatrId(String workersubmithearttreatrId);
    public int findCountByAllLike(HeattreatcheckFormMap heattreatcheckFormMap);
    public List<HeattreatcheckFormMap> findByAllLike(HeattreatcheckFormMap heattreatcheckFormMap);

}
