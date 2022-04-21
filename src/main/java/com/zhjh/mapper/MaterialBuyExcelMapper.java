package com.zhjh.mapper;

import com.zhjh.entity.MaterialBuyExcelFormMap;
import com.zhjh.mapper.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MaterialBuyExcelMapper extends BaseMapper{
    public void deleteAll();
    public List<MaterialBuyExcelFormMap> findSumByMaterialQuality(@Param("materialQuality") String materialQuality);
    public void updateAmountByEntity(MaterialBuyExcelFormMap materialBuyExcelFormMap);
    public String findIsExistBySizeAndMaterialQuality(MaterialBuyExcelFormMap materialBuyExcelFormMap);
    public void deleteByIds(String ids);
    public List<MaterialBuyExcelFormMap> findByMaterialQuality(@Param("materialQuality") String materialQuality);
}
