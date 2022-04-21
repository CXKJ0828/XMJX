package com.zhjh.mapper;

import com.zhjh.entity.BlankProcessFormMap;
import com.zhjh.entity.MaterialbuyorderdetailsBlankFormMap;
import com.zhjh.mapper.base.BaseMapper;

import java.util.List;

public interface MaterialbuyorderdetailsBlankMapper extends BaseMapper{
    public void deleteByOrderId(String orderId);
    public MaterialbuyorderdetailsBlankFormMap findBankLengthAndWeightByMaterialBuyOrderDetailsId(MaterialbuyorderdetailsBlankFormMap materialbuyorderdetailsBlankFormMap);
    public List<MaterialbuyorderdetailsBlankFormMap> findBankByMaterialBuyOrderDetailsId(MaterialbuyorderdetailsBlankFormMap materialbuyorderdetailsBlankFormMap);
}
