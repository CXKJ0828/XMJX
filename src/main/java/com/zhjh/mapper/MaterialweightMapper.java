package com.zhjh.mapper;

import com.zhjh.entity.MaterialweightFormMap;
import com.zhjh.mapper.base.BaseMapper;

import java.util.List;

public interface MaterialweightMapper extends BaseMapper{
    public MaterialweightFormMap findByMaterialId(String materialId);
    public MaterialweightFormMap findByMaterialQualityAndOuterCircle(MaterialweightFormMap materialweightFormMap);
    public void deleteById(String id);
    public int findCountByAllLike(MaterialweightFormMap materialweightFormMap);
    public List<MaterialweightFormMap> findByAllLike(MaterialweightFormMap materialweightFormMap);
}
