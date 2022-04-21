package com.zhjh.mapper;

import com.zhjh.entity.PoFormMap;
import com.zhjh.mapper.base.BaseMapper;

import java.util.List;

public interface PoMapper extends BaseMapper{
    public int findCountByAllLike(PoFormMap PoFormMap);
    public List<PoFormMap> findByAllLike(PoFormMap PoFormMap);
}
