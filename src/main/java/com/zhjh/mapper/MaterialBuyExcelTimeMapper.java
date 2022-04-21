package com.zhjh.mapper;

import com.zhjh.entity.MaterialBuyExcelTimeFormMap;
import com.zhjh.mapper.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MaterialBuyExcelTimeMapper extends BaseMapper{
    public void deleteAll();
    public void deleteByMaterialbuyexcelIds(String ids);
    public List<MaterialBuyExcelTimeFormMap> findByMaterialQuality(@Param("materialQuality") String materialQuality);
}
