package com.zhjh.mapper;

import com.zhjh.entity.MaterialFormMap;
import com.zhjh.entity.MaterialweightFormMap;
import com.zhjh.mapper.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MaterialMapper extends BaseMapper{
    public void updateTaxPriceById(@Param("taxPrice")String taxPrice,@Param("id")String id);
    public void deleteById(String id);
    public MaterialFormMap findByMaterialQualityAndOuterCircle(MaterialFormMap materialFormMap);
    public int findCountByAllLike(MaterialFormMap materialFormMap);
    public List<MaterialFormMap> findByAllLike(MaterialFormMap materialFormMap);
}
