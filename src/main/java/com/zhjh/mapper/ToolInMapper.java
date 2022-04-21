package com.zhjh.mapper;

import com.zhjh.entity.ToolInFormMap;
import com.zhjh.mapper.base.BaseMapper;

import java.util.List;

public interface ToolInMapper extends BaseMapper{
	public ToolInFormMap findAmountAndMoneySumByAllLike(ToolInFormMap toolFormMap);
	public int findCountByAllLike(ToolInFormMap toolFormMap);
	public List<ToolInFormMap> findByAllLike(ToolInFormMap toolFormMap);
	public void deleteById(String id);
}
