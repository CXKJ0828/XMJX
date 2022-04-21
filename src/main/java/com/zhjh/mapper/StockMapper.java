package com.zhjh.mapper;

import com.zhjh.entity.StockFormMap;
import com.zhjh.mapper.base.BaseMapper;

import java.util.List;

public interface StockMapper extends BaseMapper{
    public void deleteById(String id);
    public StockFormMap findByGoodId(String goodId);
    public int findCountByAllLike(StockFormMap stockFormMap);
    public List<StockFormMap> findByAllLike(StockFormMap stockFormMap);
}
