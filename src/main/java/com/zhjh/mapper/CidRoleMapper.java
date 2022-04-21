package com.zhjh.mapper;

import com.zhjh.entity.CidRoleFormMap;
import com.zhjh.mapper.base.BaseMapper;

import java.util.List;

public interface CidRoleMapper extends BaseMapper{
    public List<CidRoleFormMap> findCidNotInRole(String role);
    public List<CidRoleFormMap> findByCid(String role);
}
