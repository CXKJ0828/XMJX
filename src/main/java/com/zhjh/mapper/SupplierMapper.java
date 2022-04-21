package com.zhjh.mapper;

import com.zhjh.entity.SupplierFormMap;
import com.zhjh.mapper.base.BaseMapper;

import java.util.List;

public interface SupplierMapper extends BaseMapper{
    public void deleteById(String id);
    public List<SupplierFormMap> findByAllLike(SupplierFormMap supplierFormMap);
    public int findCountByAllLike(SupplierFormMap supplierFormMap);
}
