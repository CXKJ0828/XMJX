package com.zhjh.mapper;

import com.zhjh.entity.MaterialBackOrderDetailsFormMap;
import com.zhjh.mapper.base.BaseMapper;

import java.util.List;

public interface MaterialBackOrderDetailsMapper extends BaseMapper{
    public List<MaterialBackOrderDetailsFormMap> selectReduceLengthMessageByMaterialBackOrderId(String materialbackorderId);
    public void updateLackAmountById(String id);
    public MaterialBackOrderDetailsFormMap selectByMaterialQualityAndAndouterCircleMaterialBuyOrderIdAndMaterialBackOrderId(MaterialBackOrderDetailsFormMap materialBackOrderDetailsFormMap);
    public MaterialBackOrderDetailsFormMap findById(String id);
    public void deleteById(String id);
    public List<MaterialBackOrderDetailsFormMap> selectByMaterialbackorderId(String materialBackOrderId);
    public List<MaterialBackOrderDetailsFormMap> selectMaterialQualityBymaterialBackOrderId(String materialBackOrderId);
    public List<MaterialBackOrderDetailsFormMap> selectByMaterialBuyOrderIdAndMaterialBackOrderId(MaterialBackOrderDetailsFormMap materialBackOrderDetailsFormMap);
}
