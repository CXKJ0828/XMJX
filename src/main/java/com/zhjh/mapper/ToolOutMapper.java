package com.zhjh.mapper;

import com.zhjh.entity.ToolOutFormMap;
import com.zhjh.mapper.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ToolOutMapper extends BaseMapper{
	public ToolOutFormMap findAmoutAndMoneySumByAllLike(ToolOutFormMap toolOutFormMap);
	public int findSumAmountByUserId(ToolOutFormMap toolOutFormMap);
	public List<ToolOutFormMap> findByUserId(ToolOutFormMap toolFormMap);
	public void deleteByIds(String ids);
	public int findCountByAllLike(ToolOutFormMap toolFormMap);
	public int findAmoutnSumByAllLike(ToolOutFormMap toolFormMap);
	public List<ToolOutFormMap> findByAllLike(ToolOutFormMap toolFormMap);
	public void deleteById(String id);
}
