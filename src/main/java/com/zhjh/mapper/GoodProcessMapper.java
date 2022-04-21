package com.zhjh.mapper;

import com.zhjh.entity.GoodProcessFormMap;
import com.zhjh.mapper.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodProcessMapper extends BaseMapper{
    public List<GoodProcessFormMap> findArtificialByNameAndGoodId(@Param("processNames")String processNames,
                                                            @Param("goodId")String goodId);
    public String findStringByGoodId(String goodId);
    public String findSumArtificialByGoodId(String goodId);
    public void deleteById(String id);
    public void deleteByGoodIdAndProcessIdAndNumberNotNull(GoodProcessFormMap goodProcessFormMap);
    public List<GoodProcessFormMap> findByGoodIdAndProcessId(GoodProcessFormMap goodProcessFormMap);
    public GoodProcessFormMap findByGoodIdAndProcessIdAndNumber(GoodProcessFormMap goodProcessFormMap);
    public List<GoodProcessFormMap> findByGoodId(String goodId);
}
