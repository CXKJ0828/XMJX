package com.zhjh.mapper;

import com.zhjh.entity.MaterialBackExcelDetailsFormMap;
import com.zhjh.mapper.base.BaseMapper;

import java.util.List;

public interface MaterialBackExcelDetailsMapper extends BaseMapper{
    public List<MaterialBackExcelDetailsFormMap> findByMaterialbackExcelId(String materialbackExcelId);
    public void deleteByMaterialbackExcelIds(String ids);
}
