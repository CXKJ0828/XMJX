package com.zhjh.mapper;

import com.zhjh.entity.BlankSizeFormMap;
import com.zhjh.mapper.base.BaseMapper;

import java.util.List;

public interface BlankSizeMapper extends BaseMapper{
    public BlankSizeFormMap findByGoodId(String goodId);
    public void deleteById(String id);
    public int findCountByAllLike(BlankSizeFormMap blankFormMap);
    public List<BlankSizeFormMap> findByAllLike(BlankSizeFormMap blankFormMap);
}
