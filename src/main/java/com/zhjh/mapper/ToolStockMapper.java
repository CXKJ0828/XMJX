package com.zhjh.mapper;

import com.zhjh.entity.ToolStockFormMap;
import com.zhjh.mapper.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ToolStockMapper extends BaseMapper{
	public int findSumAmountByAllLike(ToolStockFormMap toolStockFormMap);
	public ToolStockFormMap findByToolId(String toolId);
	public void addAmountByToolId(@Param("amount")float amount,@Param("toolId")String toolId);
	public void reduceAmountByToolId(@Param("amount")float amount,@Param("toolId")String toolId);
	public int findCountByAllLike(ToolStockFormMap toolFormMap);
	public List<ToolStockFormMap> findByAllLike(ToolStockFormMap toolFormMap);
	public void deleteById(String id);
}
