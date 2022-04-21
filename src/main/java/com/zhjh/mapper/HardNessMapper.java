package com.zhjh.mapper;

import com.zhjh.entity.HardNessFormMap;
import com.zhjh.mapper.base.BaseMapper;

import java.util.List;

public interface HardNessMapper extends BaseMapper{
	public int findCountByAllLike(String name);
	public List<HardNessFormMap> findByAllLike(String name);
	public void deleteById(String id);
}
