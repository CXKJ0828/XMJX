package com.zhjh.mapper;

import com.zhjh.entity.GoodFormMap;
import com.zhjh.mapper.base.BaseMapper;

import java.util.List;

public interface GoodMapper extends BaseMapper{
    public String findGoodSizeById(String id);
    public String findTaxPriceById(String id);
    public String findGoodWeightById(String id);
    /**
     * 从产品表里面获取物料编码
     * @return
     */
    public List<GoodFormMap> findDistinctMaterialCodeLike(GoodFormMap goodFormMap);
    public int findDistinctMaterialCodeCountLike(GoodFormMap goodFormMap);
    public List<GoodFormMap> findByMapNumberLike(String mapNumber);
    public int findCountByMapNumberLike(String mapNumber);
    public List<GoodFormMap> findByClientId(String clientId);
    public void deleteById(String id);
    public int findCountByAllLike(GoodFormMap goodFormMap);
    public int findCountByMapNumberAndClientIdLike(GoodFormMap goodFormMap);
    public List<GoodFormMap> findByMapNumberAndClientIdLike(GoodFormMap goodFormMap);
    public List<GoodFormMap> findByAllLike(GoodFormMap goodFormMap);
    public int findCountByNameLike(GoodFormMap goodFormMap);
    public List<GoodFormMap> findByNameLike(GoodFormMap goodFormMap);
    public List<GoodFormMap> findByMapNumber(String mapNumber);
    public GoodFormMap findByOrderDetailsId(String orderdetailsId);
    public GoodFormMap findFirstByMapNumber(String mapNumber);
}
