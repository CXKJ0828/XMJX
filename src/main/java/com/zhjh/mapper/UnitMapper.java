package com.zhjh.mapper;

import com.zhjh.entity.UnitFormMap;
import com.zhjh.mapper.base.BaseMapper;

import java.util.List;

public interface UnitMapper extends BaseMapper{
	public int findCountByAllLike(String name);
	public List<UnitFormMap> findByAllLike(String name);
	public void deleteById(String id);

}
