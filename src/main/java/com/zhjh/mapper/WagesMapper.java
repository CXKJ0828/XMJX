package com.zhjh.mapper;

import com.zhjh.entity.AreaFormMap;
import com.zhjh.entity.WagesFormMap;
import com.zhjh.mapper.base.BaseMapper;

import java.util.List;

public interface WagesMapper extends BaseMapper{
    public void deleteById(String id);
    public String findAllMoneyByStartTimeAndEndTimeAndContentAndUser(WagesFormMap wagesFormMap);
    public int findCountByAllLike(WagesFormMap wagesFormMap);
    public List<WagesFormMap> findByAllLike(WagesFormMap wagesFormMap);

}
