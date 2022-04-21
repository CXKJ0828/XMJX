package com.zhjh.mapper;

import java.util.List;

import com.zhjh.entity.UserFormMap;
import com.zhjh.mapper.base.BaseMapper;


public interface UserMapper extends BaseMapper{
	public List<UserFormMap> findByWagesNameAndAccountName(UserFormMap userFormMap);
	public List<UserFormMap> findCompleteByWages(String wages);
	public List<UserFormMap> findCheckByWages(String wages);
	public List<UserFormMap> findByTitle(String title);
	public List<UserFormMap> findByUserIds(UserFormMap userFormMap);
	public List<UserFormMap> findByUserNameAndAccountName(UserFormMap userFormMap);
	public List<UserFormMap> findUserPage(UserFormMap userFormMap);
	public void deleteByAccountName(String accountName);
	
}
