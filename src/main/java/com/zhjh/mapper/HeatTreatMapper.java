package com.zhjh.mapper;

import com.zhjh.entity.*;
import com.zhjh.mapper.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HeatTreatMapper extends BaseMapper{
	public String findGoodIdById(String id);
	public HeatTreatFormMap findBadOrderPrintContentById(String id);
	public List<HeatTreatFormMap> findDistributionAmountByIds(String ids);
	public List<HeatTreatFormMap> findByProgressSearchIdNoLimitDeliveryTime(String id);
	public void updateIsSendStateByIds(@Param("ids") String ids,@Param("state")String state);
	public String findIsCylindricalroughgrindingById(String id);//是否为外圆粗磨
	public String findIsPinShaftById(String id);//是否为销轴
	public String findIsPadById(String id);//是否为垫
	public void updateIsGoodStateById(@Param("id") String id,@Param("state")String state);
	public void updateIsJumpStateById(@Param("id") String id,@Param("state")String state);
	public void updateIsPadStateById(@Param("id") String id,@Param("state")String state);
	public void updateIsPinShaftStateById(@Param("id") String id,@Param("state")String state);
	public String findDispatchAmountByDispatch(HeatTreatFormMap heatTreatFormMap);
	public List<HeatTreatFormMap> findSmallShow2OrderDetailsByContent(HeatTreatFormMap heatTreatFormMap);
	public List<HeatTreatFormMap> findSmallShow2OrderByContent(HeatTreatFormMap heatTreatFormMap);
	public List<HeatTreatFormMap> findSmallShow2ClientByContent(HeatTreatFormMap heatTreatFormMap);
	public List<HeatTreatFormMap> findDispatchContentByDispatch(HeatTreatFormMap heatTreatFormMap);
	public int findDispatchCountByDispatch(HeatTreatFormMap heatTreatFormMap);
	public void updateIsPrintTrueByIds(String ids);
	public String findProcessIdsById(String id);
	public void updateIsMakeMidfrequencyTrueById(String id);
	public void updateIsMakeCarburizationTrueById(String id);
	public List<ClientFormMap> findExecutionClientByAllLike(HeatTreatFormMap heatTreatFormMap);
	public List<HeatTreatFormMap> findMainGoodAndOrderNoImgByIds(String ids);
	public List<HeatTreatFormMap> findMainGoodAndOrderByIds(String ids);
	public void deleteByProgressSearchId(ProgressSearchFormMap progressSearchFormMap);
	public HeatTreatFormMap findByProgressSearchId(String id);
	public HeatTreatFormMap findSumExecutionByAllLike(HeatTreatFormMap heatTreatFormMap);
	public String findIdsByProgressSearchIds(String ids);
	public List<ClientFormMap> findClientByAllLike(HeatTreatFormMap heatTreatFormMap);
	public HeatTreatFormMap findById(String id);
	public List<HeatTreatFormMap> findByIds(String ids);
	public HeatTreatFormMap findSumWeightAndAmountByIds(String ids);
	public int findCountByAllLike(HeatTreatFormMap heatTreatFormMap);
	public List<HeatTreatFormMap> findByAllLike(HeatTreatFormMap heattreatFormMap);
	public int findExecutionCountByAllLike(HeatTreatFormMap heatTreatFormMap);
	public List<HeatTreatFormMap> findExecutionByAllLike(HeatTreatFormMap heattreatFormMap);
	public void deleteById(String id);
	public HeatTreatFormMap findSumWeightAndAmountByAllLike(HeatTreatFormMap heatTreatFormMap);
}
