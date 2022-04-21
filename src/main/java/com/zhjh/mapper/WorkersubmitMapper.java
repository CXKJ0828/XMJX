package com.zhjh.mapper;

import com.zhjh.entity.PoFormMap;
import com.zhjh.entity.WorkersubmitFormMap;
import com.zhjh.mapper.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WorkersubmitMapper extends BaseMapper{
    public void updateIsHeattreatTrueById(String id);
    public String findProductAmountByClinetAndProcessIdsAndDeliveryTime(@Param("processIdS")String processIdS,
                                                                                           @Param("deliveryTime")String deliveryTime,
                                                                                           @Param("clientId")String clientId);
    public int findCountByBlankprocessId(String blankprocessId);
    public List<WorkersubmitFormMap> findPhoneShowBySubmiterIds(WorkersubmitFormMap workersubmitFormMap);
    public WorkersubmitFormMap findById(String id);
    public int findCountByWorkerSubmitIdByClientAndOrderAndGoodAndProcessAndUserAndState(WorkersubmitFormMap workersubmitFormMap);
    public List<WorkersubmitFormMap> findByWorkerSubmitIdByClientAndOrderAndGoodAndProcessAndUserAndState(WorkersubmitFormMap workersubmitFormMap);
    public WorkersubmitFormMap findAllMoneyByWorkerSubmitIdByStartTimeAndEndTimeAndContentAndUser(WorkersubmitFormMap workersubmitFormMap);
    public int findCountByWorkerSubmitIdByStartTimeAndEndTimeAndContentAndUser(WorkersubmitFormMap workersubmitFormMap);
    public List<WorkersubmitFormMap> findByWorkerSubmitIdByStartTimeAndEndTimeAndContentAndUser(WorkersubmitFormMap workersubmitFormMap);
    public List<WorkersubmitFormMap> findByWorkerSubmitId(WorkersubmitFormMap workersubmitFormMap);
    public List<WorkersubmitFormMap> findPhoneByWorkerSubmitId(WorkersubmitFormMap workersubmitFormMap);
    public List<WorkersubmitFormMap> findByBlankprocessId(String blankprocessId);
    public List<WorkersubmitFormMap> findUserNameAndGoodCodeByOrderdetailsprocessId(String orderdetailsprocessId);
    public List<WorkersubmitFormMap> findSumUseTimeByOrderId(WorkersubmitFormMap workersubmitFormMap);
    public List<WorkersubmitFormMap> findByOrderdetailsprocessId(WorkersubmitFormMap workersubmitFormMap);
    public int findCountByAllLike(WorkersubmitFormMap workersubmitFormMap);
    public List<WorkersubmitFormMap> findByAllLike(WorkersubmitFormMap workersubmitFormMap);
    public void deleteById(String id);

}
