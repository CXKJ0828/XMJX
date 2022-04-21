package com.zhjh.mapper;


import com.zhjh.entity.ProgressSearchFormMap;
import com.zhjh.mapper.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProgressSearchMapper extends BaseMapper {
    public void updateNowDistributionAmountById(@Param("waitDistributionAmount")String waitDistributionAmount,
                                                @Param("nowDistributionAmount")String nowDistributionAmount,
                                                @Param("id")String id);
    public void deleteByIds(String ids);
    public List<ProgressSearchFormMap> findByAllLike(ProgressSearchFormMap progressSearchFormMap);
    public int findCountByAllLike(ProgressSearchFormMap progressSearchFormMap);
}
