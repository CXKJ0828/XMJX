package com.zhjh.mapper;

import com.zhjh.entity.BadnoticeFormMap;
import com.zhjh.entity.ScrapFormMap;
import com.zhjh.entity.UnqualifiedFormMap;
import com.zhjh.entity.WorkersubmitHeatTreatFormMap;
import com.zhjh.mapper.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WorkersubmitHeatTreatMapper extends BaseMapper{
    public List<WorkersubmitHeatTreatFormMap> findCheckCompleteHeattreatPhoneByWorkerSubmitId(WorkersubmitHeatTreatFormMap workersubmitHeatTreatFormMap);
    public WorkersubmitHeatTreatFormMap findMaxEstimateCompleteTimeAndIdByUserId(String userId);
    public WorkersubmitHeatTreatFormMap findAllMoneyById(String id);
    public WorkersubmitHeatTreatFormMap findMaxEstimateCompleteTimeByWorkerSubmitIdByStartTimeAndEndTimeAndContentAndUser(WorkersubmitHeatTreatFormMap workersubmitHeatTreatFormMap);
    public String findUserNamesByHeattreatId(String heattreatId);
    public String findMaxEstimateCompleteTimeBySubmiterId(String submiterId);
    public WorkersubmitHeatTreatFormMap findSumWagesHeattreatPhoneByWorkerSubmitId(WorkersubmitHeatTreatFormMap workersubmitHeatTreatFormMap);
    public WorkersubmitHeatTreatFormMap findCheckSumWagesHeattreatPhoneByWorkerSubmitId(WorkersubmitHeatTreatFormMap workersubmitHeatTreatFormMap);
    public WorkersubmitHeatTreatFormMap findSumOverTimeHeattreatPhoneByWorkerSubmitId(WorkersubmitHeatTreatFormMap workersubmitHeatTreatFormMap);
    public WorkersubmitHeatTreatFormMap findCheckSumOverTimeHeattreatPhoneByWorkerSubmitId(WorkersubmitHeatTreatFormMap workersubmitHeatTreatFormMap);
    public WorkersubmitHeatTreatFormMap findSumOverTimeByWorkerSubmitIdByStartTimeAndEndTimeAndContentAndUser(WorkersubmitHeatTreatFormMap workersubmitHeatTreatFormMap);
    public int findBadSumAcountByOrderDetailsId(WorkersubmitHeatTreatFormMap workersubmitHeatTreatFormMap);
    public WorkersubmitHeatTreatFormMap findUnCompleteAmountAndCompleteAmountByHeattreatIdAndProcessId(@Param("processId")String processId,
                                                                                                       @Param("hearttreatId")String hearttreatId);
    public List<WorkersubmitHeatTreatFormMap> findAmountAndUserByHeattreatIdAndProcessId(@Param("processId")String processId,
                                                                                         @Param("hearttreatId")String hearttreatId);
    public int findCountByContractNumberAndDeliveryTimeAndGoodIdAndProcessId(@Param("contractNumber")String contractNumber,
                                                                                                                    @Param("deliveryTime")String deliveryTime,
                                                                                                                    @Param("goodId")String goodId,
                                                                                                                    @Param("processId")String processId);
    public int findUnCompleteCountByHeattreatIdAndProcessId(@Param("hearttreatId")String hearttreatId,
                                                                             @Param("processId")String processId);
    public void updateStateById(@Param("state")String state,@Param("id")String id);
    public WorkersubmitHeatTreatFormMap findSumAmountByContractNumberAndDeliveryTimeAndGoodIdAndProcessId(@Param("contractNumber")String contractNumber,
                                                                                                          @Param("deliveryTime")String deliveryTime,
                                                                                                          @Param("goodId")String goodId,
                                                                                                          @Param("processId")String processId);
    public List<WorkersubmitHeatTreatFormMap> findUserNameByContractNumberAndDeliveryTimeAndGoodIdAndProcessId(@Param("contractNumber")String contractNumber,
                                                                                                                    @Param("deliveryTime")String deliveryTime,
                                                                                                                    @Param("goodId")String goodId,
                                                                                                                    @Param("processId")String processId);
    public List<WorkersubmitHeatTreatFormMap> findAmountAndUserByContractNumberAndDeliveryTimeAndGoodIdAndProcessId(@Param("contractNumber")String contractNumber,
                                                                                                          @Param("deliveryTime")String deliveryTime,
                                                                                                          @Param("goodId")String goodId,
                                                                                                          @Param("processId")String processId);
    public List<WorkersubmitHeatTreatFormMap> findProgressByContractNumberAndDeliveryTimeAndGoodId(@Param("contractNumber")String contractNumber,
                                                                                                                                  @Param("deliveryTime")String deliveryTime,
                                                                                                                                  @Param("goodId")String goodId);
    public WorkersubmitHeatTreatFormMap findUnCompleteAmountAndCompleteAmountByContractNumberAndDeliveryTimeAndGoodIdAndProcessId(@Param("contractNumber")String contractNumber,
                                                                                                                    @Param("deliveryTime")String deliveryTime,
                                                                                                                    @Param("goodId")String goodId,
                                                                                                                    @Param("processId")String processId);
    public BadnoticeFormMap findBadNoticeShowById(String id);
    public ScrapFormMap findScrapShowById(String id);
    public UnqualifiedFormMap findUnqualifiedShowById(String id);
    public List<WorkersubmitHeatTreatFormMap> findBadMessageGroupByOrderDetailsId(WorkersubmitHeatTreatFormMap workersubmitHeatTreatFormMap);
    public int findBadMessageGroupCountByOrderDetailsId(WorkersubmitHeatTreatFormMap workersubmitHeatTreatFormMap);
    public List<WorkersubmitHeatTreatFormMap> findProgressByHeattreatId(String id);
    public List<WorkersubmitHeatTreatFormMap> findPhoneShowBySubmiterIds(WorkersubmitHeatTreatFormMap WorkersubmitHeatTreatFormMap);
    public WorkersubmitHeatTreatFormMap findById(String id);
    public WorkersubmitHeatTreatFormMap findAllMoneyByWorkerSubmitIdByStartTimeAndEndTimeAndContentAndUser(WorkersubmitHeatTreatFormMap WorkersubmitHeatTreatFormMap);
    public int findCountByWorkerSubmitIdByStartTimeAndEndTimeAndContentAndUser(WorkersubmitHeatTreatFormMap WorkersubmitHeatTreatFormMap);
    public List<WorkersubmitHeatTreatFormMap> findByWorkerSubmitIdByStartTimeAndEndTimeAndContentAndUser(WorkersubmitHeatTreatFormMap WorkersubmitHeatTreatFormMap);
    public List<WorkersubmitHeatTreatFormMap> findByWorkerSubmitId(WorkersubmitHeatTreatFormMap WorkersubmitHeatTreatFormMap);
    public List<WorkersubmitHeatTreatFormMap> findHeattreatPhoneByWorkerSubmitId(WorkersubmitHeatTreatFormMap WorkersubmitHeatTreatFormMap);
    public List<WorkersubmitHeatTreatFormMap> findGetHeattreatPhoneByWorkerSubmitId(WorkersubmitHeatTreatFormMap WorkersubmitHeatTreatFormMap);
    public List<WorkersubmitHeatTreatFormMap> findGetCheckHeattreatPhoneByWorkerSubmitId(WorkersubmitHeatTreatFormMap WorkersubmitHeatTreatFormMap);
    public List<WorkersubmitHeatTreatFormMap> findCompleteHeattreatPhoneByWorkerSubmitId(WorkersubmitHeatTreatFormMap WorkersubmitHeatTreatFormMap);
    public void deleteById(String id);

}
