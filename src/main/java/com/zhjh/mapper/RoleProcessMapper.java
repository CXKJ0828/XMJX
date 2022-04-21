package com.zhjh.mapper;

import com.zhjh.entity.RoleProcessFormMap;
import com.zhjh.mapper.base.BaseMapper;

import java.util.List;

public interface RoleProcessMapper extends BaseMapper{
    List<RoleProcessFormMap> findByRoleId(RoleProcessFormMap roleProcessFormMap);
}
