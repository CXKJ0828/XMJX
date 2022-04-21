package com.zhjh.mapper;

import com.zhjh.entity.BlankProcessFormMap;
import com.zhjh.mapper.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BlankProcessMapper extends BaseMapper{
    public String findIdByBlankIdAndGoodProcessId(@Param("blankId")String blankId,
                                                  @Param("goodprocessId")String goodprocessId);
    public void deleteByOrderId(String orderId);
    public List<BlankProcessFormMap> findDistributionPrintByIds(BlankProcessFormMap blankProcessFormMap);
    public List<BlankProcessFormMap> findByBlankId(String blankId);
    public int findCountByBlankprocessId(BlankProcessFormMap blankProcessFormMap);
    public List<BlankProcessFormMap> findByBlankIdAndNumber(BlankProcessFormMap blankProcessFormMap);
    public BlankProcessFormMap findBadAmountAndProcessByBlankProcessId(String blankprocessId);
    public BlankProcessFormMap findByStartQRCode(String code);
    public BlankProcessFormMap findByEndQRCode(String code);
    public BlankProcessFormMap findById(String id);
    public BlankProcessFormMap findMaxProcessByOrderDetailsId(String orderdetailsId);
    public String findSumPlanneedDayByBlankIdAndNumber(BlankProcessFormMap blankProcessFormMap);
    public String findProcessNameByBlankIdAndNumber(BlankProcessFormMap blankProcessFormMap);
}
