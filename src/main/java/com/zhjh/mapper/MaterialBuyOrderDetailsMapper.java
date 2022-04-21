package com.zhjh.mapper;

import com.zhjh.entity.MaterialBuyOrderDetailsFormMap;
import com.zhjh.entity.MaterialQualityTypeFormMap;
import com.zhjh.mapper.base.BaseMapper;

import java.util.List;

public interface MaterialBuyOrderDetailsMapper extends BaseMapper{
    public List<MaterialBuyOrderDetailsFormMap> selectBuyExcelByMaterialbuyorderIdAndSearchAndBlankIds(MaterialBuyOrderDetailsFormMap materialBuyOrderDetailsFormMap);
    public List<MaterialBuyOrderDetailsFormMap> selectByMaterialbuyorderIdAndSearchAndBlankIds(MaterialBuyOrderDetailsFormMap materialBuyOrderDetailsFormMap);
    public List<MaterialQualityTypeFormMap> selectDistinctMaterialQuality();
    public MaterialBuyOrderDetailsFormMap findById(String id);
    public List<MaterialBuyOrderDetailsFormMap> selectByMaterialbuyorderIdAndSearch(MaterialBuyOrderDetailsFormMap materialBuyOrderDetailsFormMap);
    public List<MaterialBuyOrderDetailsFormMap> selectByMaterialbuyorderId(String materialbuyorderId);
    public MaterialBuyOrderDetailsFormMap selectByMaterialQualityAndOuterCircleAndMaterialbuyorderId(MaterialBuyOrderDetailsFormMap materialBuyOrderDetailsFormMap);
    public List<MaterialBuyOrderDetailsFormMap> selectMaterialQualityBymaterialBuyOrderId(String materialBuyOrderId);
    public List<MaterialBuyOrderDetailsFormMap> selectByMaterialQualityAndMaterialbuyorderId(MaterialBuyOrderDetailsFormMap materialBuyOrderDetailsFormMap);
}
