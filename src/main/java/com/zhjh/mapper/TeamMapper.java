package com.zhjh.mapper;

import com.zhjh.entity.TeamFormMap;
import com.zhjh.mapper.base.BaseMapper;

import java.util.List;

public interface TeamMapper extends BaseMapper{
    public String finUserIdsdByRoleId(String roleId);
}
