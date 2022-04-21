package com.zhjh.mapper;

import com.zhjh.entity.ToolTypeFormMap;
import com.zhjh.mapper.base.BaseMapper;

import java.util.List;

public interface ToolTypeMapper extends BaseMapper{
	public int findCountByAllLike(String content);
	public List<ToolTypeFormMap> findByAllLike(String content);
	public void deleteById(String id);

}
