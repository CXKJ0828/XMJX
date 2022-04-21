package com.zhjh.mapper;

import com.zhjh.entity.LeaveFormMap;
import com.zhjh.mapper.base.BaseMapper;

import java.util.List;

public interface LeaveMapper extends BaseMapper{
    public void deleteById(String id);
    public String findAllDaysByAllLike(LeaveFormMap leaveFormMap);
    public List<LeaveFormMap> findByAllLike(LeaveFormMap leaveFormMap);
    public int findCountByAllLike(LeaveFormMap leaveFormMap);

}
