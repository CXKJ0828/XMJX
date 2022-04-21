package com.zhjh.mapper;

import com.zhjh.entity.BlankFormMap;
import com.zhjh.entity.SendInputFormMap;
import com.zhjh.mapper.base.BaseMapper;

import java.util.List;

public interface SendInputMapper extends BaseMapper{
    public void deleteById(String id);
    public List<SendInputFormMap> findByLike(SendInputFormMap sendInputFormMap);
    public int findCountByLike(SendInputFormMap sendInputFormMap);
}
