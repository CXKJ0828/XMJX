package com.zhjh.mapper;

import com.zhjh.entity.MaterialQualityTypeFormMap;
import com.zhjh.entity.RoleFormMap;
import com.zhjh.mapper.base.BaseMapper;

import java.util.List;

public interface MaterialQualityTypeMapper extends BaseMapper{
	public int findCountByAllLike(String name);
	public List<MaterialQualityTypeFormMap> findByAllLike(String name);
	public void deleteById(String id);

}
