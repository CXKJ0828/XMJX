package com.zhjh.mapper;

import com.zhjh.entity.MaterialBackExcelDetailsFormMap;
import com.zhjh.entity.MaterialBackExcelFormMap;
import com.zhjh.mapper.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MaterialBackExcelMapper extends BaseMapper{
    public List<MaterialBackExcelFormMap> findSumMaterialQualityByRemarks(@Param("remarks")String remarks,
                                                                          @Param("state")String state);
    public void updateAmountByEntity(MaterialBackExcelFormMap materialBackExcelFormMap);
    public void updateAmountByBackEntity(MaterialBackExcelDetailsFormMap materialBackExcelDetailsFormMap);
    public String findIsExistByEntity(MaterialBackExcelFormMap materialBackExcelFormMap);
    public void deleteByIds(String ids);
    public List<MaterialBackExcelFormMap> findDifferentRemarksTreeShow();
    public List<MaterialBackExcelFormMap> findByRemarks(@Param("remarks")String remarks,
                                                        @Param("state")String state);
}
