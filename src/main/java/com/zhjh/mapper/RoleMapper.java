package com.zhjh.mapper;

import java.util.List;

import com.zhjh.entity.RoleFormMap;
import com.zhjh.mapper.base.BaseMapper;

public interface RoleMapper extends BaseMapper{
	public List<RoleFormMap> seletUserRole(RoleFormMap map);
	RoleFormMap seletUserRoleByUserId(RoleFormMap map);
	public void deleteById(RoleFormMap map);
	public int seletAllCount();
}
