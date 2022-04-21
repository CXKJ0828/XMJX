package com.zhjh.mapper;

import com.zhjh.entity.MaterialBackOrderDetailsDetailsFormMap;
import com.zhjh.entity.MaterialBackOrderDetailsFormMap;
import com.zhjh.mapper.base.BaseMapper;

import java.util.List;

public interface MaterialBackOrderDetailsDetailsMapper extends BaseMapper{
    public List<MaterialBackOrderDetailsDetailsFormMap> selectByMaterialbackorderdetailsId(String materialBackOrderDetailsId);
}
