package com.zhjh.mapper;

import com.zhjh.entity.BlankFormMap;
import com.zhjh.entity.ClientFormMap;
import com.zhjh.entity.OrderDetailsFormMap;
import com.zhjh.entity.ProgressSearchFormMap;
import com.zhjh.mapper.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BlankMapper extends BaseMapper{
    public List<BlankFormMap> findSmallShowByContent2(BlankFormMap blankFormMap);
    public List<BlankFormMap> findSmallShowByContent(BlankFormMap blankFormMap);
    public BlankFormMap findHeatTreatByWorkersubmitId(String id);
    public List<BlankFormMap> findIsFinishFromBlank();
    public List<BlankFormMap> findMainGoodAndOrderNoImgByIds(String ids);
    public List<BlankFormMap> findMainGoodAndOrderByIds(String ids);
    public int findCountByBlankProcessId(String blankprocessId);
    public String findIdByOrderDetailsId(String orderdetailsId);
    public BlankFormMap findHeatTreatByProgressSearchId(String id);
    public List<OrderDetailsFormMap> findAmountByContractNumberAndMapNumber(ProgressSearchFormMap progressSearchFormMap);
    public String findAmountByContractNumberAndMapNumberAndDeliveryTime(ProgressSearchFormMap progressSearchFormMap);
    public List<BlankFormMap> findByProgressSearchId(String id);
    public int findCodeCountFromBlankLike(BlankFormMap blankFormMap);
    public List<BlankFormMap> findCodeFromBlankLike(BlankFormMap blankFormMap);
    public BlankFormMap findProgressByBlankProcessId(String blankprocessId);
    public List<BlankFormMap> findProgressAndUserByBlankProcessId(String blankprocessId);
    public List<BlankFormMap> findUserNameByBlankProcessId(String blankprocessId);
    public List<BlankFormMap> findHeatTreatByIds(String ids);
    public List<BlankFormMap> findExportByAllLike(BlankFormMap blankFormMap);
    //根据blankId获取下料工序状态
    public String findBlankProductStateByBlankIdId(String blankId);
    public void updateBlankCodeByIdsAndCode(BlankFormMap blankFormMap);
    public BlankFormMap findXialiaoCodeByBlankId(String blankId);
    //获取热调质信息
    public BlankFormMap findRetiaozhiCodeByBlankId(String blankId);
    //获取热正火信息
    public BlankFormMap findRezhenghuoCodeByBlankId(String blankId);
    public BlankFormMap findXialiaoEndCodeByBlankId(String blankId);
    public List<BlankFormMap> findProgressByBlankId(String blankId);
    public List<BlankFormMap> findBigShowClientByContent(BlankFormMap blankFormMap);
    public List<BlankFormMap> findClientByAllLike(BlankFormMap blankFormMap);
    public List<BlankFormMap> findBigShowOrderByContent(BlankFormMap blankFormMap);
    public List<BlankFormMap> findBigShowOrderDetailsByContent(BlankFormMap blankFormMap);
    public List<BlankFormMap> findOrderByAllLike(BlankFormMap blankFormMap);
    public List<BlankFormMap> findOrderDetailsByAllLike(BlankFormMap blankFormMap);
    public BlankFormMap findSumByAllLike(BlankFormMap blankFormMap);
    public String findAmountBigShowByContent(BlankFormMap blankFormMap);
    public BlankFormMap findAllCountAndCompleteCountByOrderdetailsId(String orderdetailsId);
    public void updateBlankStateByIdAndOrigin(BlankFormMap blankFormMap);
    public List<BlankFormMap> findByOrderdetailsIdAndOrigin(BlankFormMap blankFormMap);
    public String findFeedBatchNumberByOrderdetailsId(String orderdetailsId);
    public int findBigShowCountByContent(BlankFormMap blankFormMap);
    public List<BlankFormMap> findBigShowByContent(BlankFormMap blankFormMap);
    public List<BlankFormMap> findPhoneShowByOrderdetailsId(BlankFormMap blankFormMap);
    public BlankFormMap findXialiaoStartCodeByBlankId(String blankId);
    public int findBadMessageGroupCountByOrderDetailsId(BlankFormMap blankFormMap);
    public int findCountTechnologyByClientOrderGoodProcessState(BlankFormMap blankFormMap);
    public List<BlankFormMap> findTechnologyByClientOrderGoodProcessState(BlankFormMap blankFormMap);
    public void deleteByOrderDetailsId(String orderdetailsId);
    public void deleteByOrderId(String orderId);
    public List<BlankFormMap> findByByOrderId(BlankFormMap blankFormMap);
    public List<BlankFormMap> findBadMessageGroupByOrderDetailsId(BlankFormMap blankFormMap);
    public void deleteById(String id);
    public int findCountByAllLike(BlankFormMap blankFormMap);
    public BlankFormMap findById(String id);
    public List<BlankFormMap> findByIds(String ids);
    public List<BlankFormMap> findPrintTimeFromBlank();
    public List<BlankFormMap> findMaterialQualityFromBlank();
    public List<BlankFormMap> findCodeFromBlank();
    public List<BlankFormMap> findByAllLike(BlankFormMap blankFormMap);
    public List<BlankFormMap> findByByOrderdetailsId(BlankFormMap blankFormMap);
    public List<BlankFormMap> findByOriginAndOrderdetailsId(BlankFormMap blankFormMap);
    public String findSumPlanneedDayByOrderDetailsId(String orderdetailsId);
}
