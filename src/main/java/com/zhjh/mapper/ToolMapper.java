package com.zhjh.mapper;

import com.zhjh.entity.ToolFormMap;
import com.zhjh.mapper.base.BaseMapper;

import java.util.List;

public interface ToolMapper extends BaseMapper{
	public ToolFormMap findByName(String name);
	public int findCountByAllLike(ToolFormMap toolFormMap);
	public List<ToolFormMap> findByAllLike(ToolFormMap toolFormMap);
	public void deleteById(String id);

}
