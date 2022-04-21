package com.zhjh.mapper;

import com.zhjh.entity.AreaFormMap;
import com.zhjh.entity.AttendanceFormMap;
import com.zhjh.mapper.base.BaseMapper;

import java.util.List;

public interface AttendanceMapper extends BaseMapper{
    public void deleteById(String id);
    public AttendanceFormMap findByWorkerSubmitIdAndDay(AttendanceFormMap attendanceFormMap);
    public AttendanceFormMap findByWorkerSubmitIdAndEndTime(AttendanceFormMap attendanceFormMap);
    public AttendanceFormMap findByWorkerSubmitIdAndStartTime(AttendanceFormMap attendanceFormMap);
    public List<AttendanceFormMap> findByWorkerSubmitIdByStartTimeAndEndTimeAndUser(AttendanceFormMap attendanceFormMap);
    public int findCountByWorkerSubmitIdByStartTimeAndEndTimeAndUser(AttendanceFormMap attendanceFormMap);
}
