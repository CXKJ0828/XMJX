package com.zhjh.mapper;

import com.zhjh.entity.AttendanceFormMap;
import com.zhjh.entity.ClientFormMap;
import com.zhjh.entity.GoodproductCategoryFormMap;
import com.zhjh.mapper.base.BaseMapper;

import java.util.List;

public interface GoodproductCategoryMapper extends BaseMapper{
    public GoodproductCategoryFormMap findStatisticsByAllLike(GoodproductCategoryFormMap goodproductCategoryFormMap);
    public List<GoodproductCategoryFormMap> findByIds(String ids);
    public List<GoodproductCategoryFormMap> findDistinctLeader(String content);
    public void deleteByIds(String ids);
    public List<ClientFormMap> findClientByAllLike(GoodproductCategoryFormMap goodproductCategoryFormMap);
    public int findCountByAllLike(GoodproductCategoryFormMap goodproductCategoryFormMap);
    public List<GoodproductCategoryFormMap> findByAllLike(GoodproductCategoryFormMap goodproductCategoryFormMap);
}
