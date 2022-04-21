package com.zhjh.mapper;

		import com.zhjh.entity.UserRoleFormMap;
		import com.zhjh.mapper.base.BaseMapper;


public interface UserRoleMapper extends BaseMapper{
	public void deleteByUserAccountName(String userId);
	public void updateRoleIdByUserId(UserRoleFormMap userRoleFormMap);

}
