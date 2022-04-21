package com.zhjh.mapper;

import com.zhjh.entity.MaterialQualityTypeFormMap;
import com.zhjh.entity.MaterialqualitytypeProcessFormMap;
import com.zhjh.mapper.base.BaseMapper;

import java.util.List;

public interface MaterialqualitytypeProcessMapper extends BaseMapper{
    public String findStringByMaterialqualitytypeId(String materialqualitytypeId);
    public List<MaterialqualitytypeProcessFormMap> findByMaterialqualitytypeId(String materialqualitytypeId);
}
