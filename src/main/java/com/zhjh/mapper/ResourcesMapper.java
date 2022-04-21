package com.zhjh.mapper;

import java.util.List;

import com.zhjh.entity.ResFormMap;
import com.zhjh.mapper.base.BaseMapper;

public interface ResourcesMapper extends BaseMapper {
	public List<ResFormMap> findlistsContainRoleState(String roleId);
	public List<ResFormMap> findChildlists(ResFormMap map);
	public List<ResFormMap> findResByEmployee(ResFormMap map);
	public List<ResFormMap> findRes(ResFormMap map);

	public void updateSortOrder(List<ResFormMap> map);
	
	public List<ResFormMap> findUserResourcess(String userId);

	public List<ResFormMap> findButtonByRoleId(int roleId);
	
}
