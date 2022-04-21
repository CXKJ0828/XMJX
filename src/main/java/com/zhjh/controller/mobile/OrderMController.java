package com.zhjh.controller.mobile;

import com.zhjh.annotation.SystemLog;
import com.zhjh.bean.LoginBean;
import com.zhjh.controller.index.BaseController;
import com.zhjh.entity.*;
import com.zhjh.mapper.*;
import com.zhjh.tool.ToolCommon;
import com.zhjh.util.Common;
import com.zhjh.util.PasswordHelper;
import com.zhjh.util.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 进行管理后台框架界面的类
 * 
 * @author lanyuan 2015-04-05
 * @Email: mmm333zzz520@163.com
 * @version 3.0v
 * @mod  Ekko 2015-09-07
 */
@Controller
@RequestMapping("/order")
public class OrderMController extends BaseController {


	@Inject
	private OrderDetailsProcessMapper orderDetailsProcessMapper;

	@Inject
	private BlankProcessMapper blankProcessMapper;

	@Inject
	private UnqualifiedMapper unqualifiedMapper;

	@Inject
	private ScrapMapper scrapMapper;

	@Inject
	private LeaveMapper leaveMapper;

	@Inject
	private ToolOutMapper toolOutMapper;

	@Inject
	private BlankMapper blankMapper;

	@Inject
	private OrderDetailsMapper orderDetailsMapper;

	@Inject
	private AttendanceMapper attendanceMapper;

	@Inject
	private ScanCodeMapper scanCodeMapper;

	@Inject
	private OrderMapper orderMapper;

	@Inject
	private OrderGoodCodeMapper orderGoodCodeMapper;

	@Inject
	private WorkersubmitMapper workersubmitMapper;

	@Inject
	private WorkersubmitHeatTreatMapper workersubmitHeatTreatMapper;

	@Inject
	private GoodProcessMapper goodProcessMapper;

	@Inject
	private HeatTreatMapper heatTreatMapper;

    @Inject
    private HeattreatcheckMapper heattreatcheckMapper;

	@Inject
	private GoodMapper goodMapper;

	@Inject
	private SystemconfigMapper systemconfigMapper;

	@Inject
	private WorkoverwageMapper workoverwageMapper;

	@Inject
	private TeamMapper teamMapper;

	@Inject
	private UserMapper userMapper;
	@ResponseBody
	@RequestMapping(value = "getDetailsByBatchNumberAndRoleIdM", method = RequestMethod.GET, produces = "text/html; charset=utf-8")
	public OrderDetailsProcessFormMap getDetailsByBatchNumberAndRoleIdM(HttpServletRequest request, String batchNumber, String roleId) {
		OrderDetailsProcessFormMap orderDetailsProcessFormMap=new OrderDetailsProcessFormMap();
		orderDetailsProcessFormMap.set("batchNumber",batchNumber);
		orderDetailsProcessFormMap.set("roleId",roleId);
		List<OrderDetailsProcessFormMap> orderDetailsProcessFormMaps=orderDetailsProcessMapper.getDetailsByBatchNumberAndRoleId(orderDetailsProcessFormMap);
		if(orderDetailsProcessFormMaps.size()>0){
			orderDetailsProcessFormMap.set("list",orderDetailsProcessFormMaps);
			orderDetailsProcessFormMap.set("id","1");
		}else{
			orderDetailsProcessFormMap.set("result","暂无相关内容");
			orderDetailsProcessFormMap.set("id","0");
		}

		return orderDetailsProcessFormMap;
	}
	@ResponseBody
	@RequestMapping(value = "scanAttendanceByCode", method = RequestMethod.GET, produces = "text/html; charset=utf-8")
	public AttendanceFormMap startAttendanceByCode(HttpServletRequest request, String code) throws Exception {
		AttendanceFormMap attendanceFormMap=new AttendanceFormMap();
		UserFormMap userFormMap=getNowUserMessage();
		String userId=userFormMap.get("id")+"";
		if(ToolCommon.isContain(code,"leave")){
			LeaveFormMap leaveFormMap=new LeaveFormMap();
			leaveFormMap.set("userId",userId);
			String days=code.replace("leave:","");
			leaveFormMap.set("days",days);
			leaveFormMap.set("modifyTime",ToolCommon.getNowTime());
			leaveMapper.addEntity(leaveFormMap);
		}else if(ToolCommon.isContain(code,"workoverwage")){
			WorkoverwageFormMap workoverwageFormMap=new WorkoverwageFormMap();
			workoverwageFormMap.set("userId",userId);
			String duration=code.replace("workoverwage:","");
			workoverwageFormMap.set("duration",duration);
			workoverwageFormMap.set("modifyTime",ToolCommon.getNowTime());
			SystemconfigFormMap systemconfigFormMapWages=systemconfigMapper.findByAttribute("name","workoverwage",SystemconfigFormMap.class).get(0);
			String workoverwage=systemconfigFormMapWages.get("content")+"";
			float workoverwageF=ToolCommon.StringToFloat(workoverwage);
			float wages=workoverwageF*ToolCommon.StringToFloat(duration);
			wages=ToolCommon.FloatToMoney(wages);
			workoverwageFormMap.set("wages",wages);
			workoverwageMapper.addEntity(workoverwageFormMap);
		}else{
			attendanceFormMap.set("userId",userId);
			String day="";
			if(ToolCommon.isContain(code,"start")){
				day=code.replace("start:","");
				attendanceFormMap.set("day",day);
				AttendanceFormMap attendanceFormMapS=attendanceMapper.findByWorkerSubmitIdAndDay(attendanceFormMap);
				if(attendanceFormMapS==null){
					AttendanceFormMap attendanceFormMap2=new AttendanceFormMap();
					attendanceFormMap2.set("day",day);
					attendanceFormMap2.set("startTime",ToolCommon.getNowTimeNoDay());
					attendanceFormMap2.set("userId",userId);
					attendanceMapper.addEntity(attendanceFormMap2);
				}else{
					attendanceFormMapS.set("startTime",ToolCommon.getNowTimeNoDay());
					attendanceMapper.editEntity(attendanceFormMapS);
				}
			}else{
				day=code.replace("end:","");
				attendanceFormMap.set("day",day);
				AttendanceFormMap attendanceFormMapS=attendanceMapper.findByWorkerSubmitIdAndDay(attendanceFormMap);
				if(attendanceFormMapS==null){
					AttendanceFormMap attendanceFormMap2=new AttendanceFormMap();
					attendanceFormMap2.set("endTime",ToolCommon.getNowTimeNoDay());
					attendanceFormMap2.set("userId",userId);
					attendanceFormMap2.set("day",day);
					attendanceMapper.addEntity(attendanceFormMap2);
				}else{
					attendanceFormMapS.set("endTime",ToolCommon.getNowTimeNoDay());
					attendanceFormMapS.set("day",day);
					attendanceMapper.editEntity(attendanceFormMapS);
				}
			}
		}


		attendanceFormMap.set("result","扫描成功");
		attendanceFormMap.set("id","1");
		return attendanceFormMap;
	}

	@ResponseBody
	@RequestMapping(value = "startWorkByCode", method = RequestMethod.GET, produces = "text/html; charset=utf-8")
	public WorkersubmitFormMap startWorkByCode(HttpServletRequest request, String code) throws Exception {
		WorkersubmitFormMap workersubmitFormMap=new WorkersubmitFormMap();
		List<ScanCodeFormMap> scanCodeFormMaps=scanCodeMapper.findByAttribute("code",code,ScanCodeFormMap.class);
		if(scanCodeFormMaps.size()>0){
            workersubmitFormMap.set("result","二维码已失效");
            workersubmitFormMap.set("id","0");
        }else{
            ScanCodeFormMap scanCodeFormMap=new ScanCodeFormMap();
            scanCodeFormMap.set("code",code);
            scanCodeMapper.addEntity(scanCodeFormMap);
		    code=code.split("时间")[0];
            String[] codesStr=code.split(",");
            for(int i=0;i<codesStr.length;i++){
                String code1=codesStr[i];
                String[] codeStr=code1.split("amount");
                String startCode=codeStr[0];
                String amount=codeStr[1];
                if(ToolCommon.isContain(amount,"blankId")){
                	String blankId=amount.split("blankId")[1];
					amount=amount.split("blankId")[0];
                	BlankFormMap blankFormMap=blankMapper.findById(blankId);
                	blankFormMap.set("isFinish","已接收未完成");
                	blankMapper.editEntity(blankFormMap);
				}
                BlankProcessFormMap blankProcessFormMap=blankProcessMapper.findByStartQRCode(startCode);
                String unreceiveAmount=blankProcessFormMap.get("unreceiveAmount")+"";
                String state=blankProcessFormMap.get("state")+"";
                String blankId=blankProcessFormMap.get("blankId")+"";
				String processName=blankProcessFormMap.get("processName")+"";
				boolean isChangeBlankState=false;
				boolean isChangeonditioningBlankState=false;
				boolean isChangenormalizingBlankState=false;

				if(processName!=null&&processName.equals("下料")){
					isChangeBlankState=true;
				}else if(processName!=null&&processName.equals("热调质")){
					isChangeonditioningBlankState=true;
				}else if(processName!=null&&processName.equals("热正火")){
					isChangenormalizingBlankState=true;
				}


				if(isChangeBlankState){
					BlankFormMap blankFormMap=blankMapper.findById(blankId);
					blankFormMap.set("isFinish","已接收未完成");
					blankMapper.editEntity(blankFormMap);
				}
				if(isChangeonditioningBlankState){
					BlankFormMap blankFormMap=blankMapper.findById(blankId);
					blankFormMap.set("isConditioning","已接收未完成");
					blankMapper.editEntity(blankFormMap);
				}
				if(isChangenormalizingBlankState){
					BlankFormMap blankFormMap=blankMapper.findById(blankId);
					blankFormMap.set("isNormalizing","已接收未完成");
					blankMapper.editEntity(blankFormMap);
				}
                    if(ToolCommon.StringToInteger(unreceiveAmount)>0){
                        blankProcessFormMap.set("state","已接收未完成");
                        float  unreceiveAmountF=ToolCommon.StringToInteger(unreceiveAmount);
                        unreceiveAmount=(unreceiveAmountF-ToolCommon.StringToInteger(amount))+"";
                        blankProcessFormMap.set("unreceiveAmount",unreceiveAmount);
                        blankProcessMapper.editEntity(blankProcessFormMap);
                        String blankprocessId=blankProcessFormMap.get("id")+"";
                        WorkersubmitFormMap workersubmitFormMap1=new WorkersubmitFormMap();
                        workersubmitFormMap1.set("startTime",ToolCommon.getNowTime());
                        workersubmitFormMap1.set("amount",amount);
                        workersubmitFormMap1.set("blankprocessId",blankprocessId);
                        workersubmitFormMap1.set("submiterId",getNowUserMessage().get("id"));
                        float amountF=ToolCommon.StringToFloat(amount);
                        String goodprocessId=blankProcessFormMap.get("goodprocessId")+"";
                        GoodProcessFormMap goodProcessFormMap=goodProcessMapper.findbyFrist("id",goodprocessId,GoodProcessFormMap.class);
                        String artificial = goodProcessFormMap.get("artificial") + "";
                        float artificialF = ToolCommon.StringToFloat(artificial);
                        SystemconfigFormMap systemconfigFormMapWages=systemconfigMapper.findByAttribute("name","dailyWages",SystemconfigFormMap.class).get(0);
                        String dailyWages=systemconfigFormMapWages.get("content")+"";
                        float dailyWagesF=ToolCommon.StringToFloat(dailyWages);
                        double planneedDay = artificialF * amountF / dailyWagesF;
                        double neesDay = 0;
                        int planneedDayInt = (new Double(planneedDay)).intValue();
                        if (planneedDay == planneedDayInt) {
                            neesDay = planneedDay;
                        } else {
                            neesDay = (planneedDay) + 1;
                        }
                        int neesDayInt = (new Double(neesDay)).intValue();
                        workersubmitFormMap1.set("planneedDay", neesDayInt);
						double distributionWages=ToolCommon.StringToInteger(amount)*artificialF;
						workersubmitFormMap1.set("distributionWages",distributionWages);
                        workersubmitMapper.addEntity(workersubmitFormMap1);
                        workersubmitFormMap.set("result","操作成功");
                        workersubmitFormMap.set("id","1");
                    }else{
                        workersubmitFormMap.set("result","该工序已完成");
                        workersubmitFormMap.set("id","0");
                    }
            }
        }




		return workersubmitFormMap;
	}

	@ResponseBody
	@RequestMapping(value = "endWorkByCode", method = RequestMethod.GET, produces = "text/html; charset=utf-8")
	public WorkersubmitFormMap endWorkByCode(HttpServletRequest request, String code) throws Exception {
		WorkersubmitFormMap workersubmitFormMap=new WorkersubmitFormMap();
		String submiterId="";

		List<ScanCodeFormMap> scanCodeFormMaps=scanCodeMapper.findByAttribute("code",code,ScanCodeFormMap.class);
        if(scanCodeFormMaps.size()>0){
            workersubmitFormMap.set("result","二维码已失效");
            workersubmitFormMap.set("id","0");
        }else{
            ScanCodeFormMap scanCodeFormMap=new ScanCodeFormMap();
            scanCodeFormMap.set("code",code);
            scanCodeMapper.addEntity(scanCodeFormMap);
            code=code.split("时间")[0];
            if(!code.equals("")){
				String[] codesStr=code.split(",");
				UserFormMap userFormMap=getNowUserMessage();
				String userId=userFormMap.get("id")+"";
				for(int i=0;i<codesStr.length;i++) {
					boolean isChangeBlankState=false;
					boolean isChangeonditioningBlankState=false;
					boolean isChangenormalizingBlankState=false;
					String code1 = codesStr[i];
					String[] codeStr = code1.split("workersubmitId");
					String codeEnd = codeStr[0];
					String workersubmitId = codeStr[1];
					String blankId="";
					String processName="";
					BlankProcessFormMap blankProcessFormMap = blankProcessMapper.findByEndQRCode(codeEnd);
					if(blankProcessFormMap!=null){
						if(ToolCommon.isContain(workersubmitId,"blankId")){
							blankId=workersubmitId.split("blankId")[1];
							workersubmitId=workersubmitId.split("blankId")[0];
							processName=blankProcessFormMap.get("processName")+"";
							if(processName!=null&&processName.equals("下料")){
								isChangeBlankState=true;
							}else if(processName!=null&&processName.equals("热调质")){
								isChangeonditioningBlankState=true;
							}else if(processName!=null&&processName.equals("热正火")){
								isChangenormalizingBlankState=true;
							}

						}else{
							blankId=blankProcessFormMap.get("blankId")+"";
							processName=blankProcessFormMap.get("processName")+"";
							if(processName!=null&&processName.equals("下料")){
								isChangeBlankState=true;
							}else if(processName!=null&&processName.equals("热调质")){
								isChangeonditioningBlankState=true;
							}else if(processName!=null&&processName.equals("热正火")){
								isChangenormalizingBlankState=true;
							}
						}
						float taxPrice = ToolCommon.StringToFloat(blankProcessFormMap.getStr("taxPrice") + "");
						WorkersubmitFormMap workersubmitFormMap1 = workersubmitMapper.findbyFrist("id", workersubmitId, WorkersubmitFormMap.class);
						submiterId = workersubmitFormMap1.get("submiterId") + "";
						if (userId.equals(submiterId)) {
							if(isChangeBlankState){
								BlankFormMap blankFormMap=blankMapper.findById(blankId);
								blankFormMap.set("isFinish","已完成");
								blankFormMap.set("finishTime",ToolCommon.getNowTime());
								blankMapper.editEntity(blankFormMap);
							}
							if(isChangeonditioningBlankState){
								BlankFormMap blankFormMap=blankMapper.findById(blankId);
								blankFormMap.set("isConditioning","已完成");
								blankMapper.editEntity(blankFormMap);
							}
							if(isChangenormalizingBlankState){
								BlankFormMap blankFormMap=blankMapper.findById(blankId);
								blankFormMap.set("isNormalizing","已完成");
								blankMapper.editEntity(blankFormMap);
							}
							String completeTime = workersubmitFormMap1.getStr("completeTime");
							String nowTime = ToolCommon.getNowTime();
							if (completeTime == null || completeTime.equals("")) {
								int completeAmountInt = ToolCommon.StringToInteger(blankProcessFormMap.get("completeAmount") + "");
								if(completeAmountInt==0){
									completeAmountInt = ToolCommon.StringToInteger(blankProcessFormMap.get("amount") + "");
									workersubmitFormMap1.set("completeAmount", completeAmountInt);
									workersubmitFormMap1.set("badAmount", "0");
								}
								String blankprocessId = blankProcessFormMap.get("id") + "";
								int badAmountIntWorkersubmit = ToolCommon.StringToInteger(workersubmitFormMap1.get("badAmount") + "");
								int completeAmountIntWorkersubmit = ToolCommon.StringToInteger(workersubmitFormMap1.get("completeAmount") + "");
								float deductRate = ToolCommon.StringToFloat(workersubmitFormMap1.getStr("deductRate") + "");
								workersubmitFormMap1.set("completeTime", nowTime);
								workersubmitFormMap1.set("blankprocessId", blankprocessId);
								float artificial = ToolCommon.StringToFloat(blankProcessFormMap.get("artificial") + "");
								double wages = completeAmountIntWorkersubmit * artificial;
								double deductWages = taxPrice * deductRate * badAmountIntWorkersubmit;
								String wagesStr = ToolCommon.Double2(wages) + "";
								double trueWages = wages - deductWages;
								String deductWagesStr = ToolCommon.Double2(deductWages) + "";
								String trueWagesStr = ToolCommon.Double2(trueWages) + "";
								workersubmitFormMap1.set("wages", wagesStr);
								workersubmitFormMap1.set("deductWages", deductWagesStr);
								workersubmitFormMap1.set("trueWages", trueWagesStr);
								workersubmitMapper.editEntity(workersubmitFormMap1);
								String completeAmount = (completeAmountInt + completeAmountIntWorkersubmit) + "";
								String amount = blankProcessFormMap.getStr("amount");
								if (ToolCommon.StringToInteger(amount) <= ToolCommon.StringToInteger(completeAmount)) {
									blankProcessFormMap.set("completeTime", nowTime);
								}
								blankProcessFormMap.set("state", "已完成");
								blankProcessMapper.editEntity(blankProcessFormMap);

								String materialquality=blankProcessFormMap.get("materialQuality")+"";
								materialquality=materialquality.replace(" ","");
								if(processName.equals("下料")){
									String oprateUserId=workersubmitFormMap.get("submiterId")+"";
									BlankFormMap blankFormMap=blankMapper.findHeatTreatByWorkersubmitId(workersubmitId);
									if(materialquality.equals("20CrMnTiH")){
                                        workersubmitMapper.updateIsHeattreatTrueById(workersubmitId);
										addHeartTreatByBlankFormMap("正火",blankFormMap,oprateUserId);
									}else if(materialquality.equals("38CrMoAlA")||materialquality.equals("42CrMoA")||materialquality.equals("40Cr")){
                                        workersubmitMapper.updateIsHeattreatTrueById(workersubmitId);
									    addHeartTreatByBlankFormMap("调质",blankFormMap,oprateUserId);
									}

								}

								workersubmitFormMap.set("result", "操作成功");
								workersubmitFormMap.set("id", "1");
							} else {
								workersubmitFormMap.set("result", "该工序已报工");
								workersubmitFormMap.set("id", "0");
							}
						} else {
							i = codesStr.length + 1;
							workersubmitFormMap.set("result", "无法上报他人工序");
							workersubmitFormMap.set("id", "0");
						}
					}

				}
			}


		}


		return workersubmitFormMap;
	}

	@ResponseBody
	@RequestMapping(value = "findByWorkerSubmitId", method = RequestMethod.GET, produces = "text/html; charset=utf-8")
	public WorkersubmitFormMap findByWorkerSubmitId(HttpServletRequest request,String content,String startTime,String endTime,String state) throws Exception {
		WorkersubmitFormMap workersubmitFormMap=new WorkersubmitFormMap();
		String userId=getPara("userId");
		workersubmitFormMap.set("content",content);
		workersubmitFormMap.set("state",state);
		workersubmitFormMap.set("submiterId",userId);
		workersubmitFormMap.set("startTime",startTime);
		endTime=ToolCommon.addDay(endTime,1);
		workersubmitFormMap.set("endTime",endTime);
		List<WorkersubmitFormMap> workersubmitFormMaps=workersubmitMapper.findPhoneByWorkerSubmitId(workersubmitFormMap);
		workersubmitFormMap.set("listOrder",workersubmitFormMaps);
		WorkersubmitHeatTreatFormMap workersubmitHeatTreatFormMap=new WorkersubmitHeatTreatFormMap();
		workersubmitHeatTreatFormMap.set("content",content);
		workersubmitHeatTreatFormMap.set("state",state);
		workersubmitHeatTreatFormMap.set("submiterId",userId);
		workersubmitHeatTreatFormMap.set("startTime",startTime);
		endTime=ToolCommon.addDay(endTime,1);
		workersubmitHeatTreatFormMap.set("endTime",endTime);
		List<WorkersubmitHeatTreatFormMap> workersubmitHeatTreatFormMaps1=workersubmitHeatTreatMapper.findHeattreatPhoneByWorkerSubmitId(workersubmitHeatTreatFormMap);
		WorkersubmitHeatTreatFormMap workersubmitHeatTreatFormMapOverTime=workersubmitHeatTreatMapper.findSumOverTimeHeattreatPhoneByWorkerSubmitId(workersubmitHeatTreatFormMap);
		WorkersubmitHeatTreatFormMap workersubmitHeatTreatFormMapWages=workersubmitHeatTreatMapper.findSumWagesHeattreatPhoneByWorkerSubmitId(workersubmitHeatTreatFormMap);
		workersubmitFormMap.set("listHeatTreat",workersubmitHeatTreatFormMaps1);
		workersubmitFormMap.set("overTime",workersubmitHeatTreatFormMapOverTime);
		workersubmitFormMap.set("wagesSum",workersubmitHeatTreatFormMapWages);
		workersubmitFormMap.set("result","操作成功");
		workersubmitFormMap.set("id","1");
		return workersubmitFormMap;
	}
	@ResponseBody
	@RequestMapping(value = "findByWorkerSubmitIdAndOrigin", method = RequestMethod.GET, produces = "text/html; charset=utf-8")
	public WorkersubmitFormMap findByWorkerSubmitIdAndOrigin(HttpServletRequest request,String content,String startTime,String endTime,String state,String origin) throws Exception {
		WorkersubmitFormMap workersubmitFormMap=new WorkersubmitFormMap();
		String userId=getPara("userId");
		if(origin.equals("订单")){
			workersubmitFormMap.set("content",content);
			workersubmitFormMap.set("state",state);
			workersubmitFormMap.set("submiterId",userId);
			workersubmitFormMap.set("startTime",startTime);
			endTime=ToolCommon.addDay(endTime,1);
			workersubmitFormMap.set("endTime",endTime);
			List<WorkersubmitFormMap> workersubmitFormMaps=new ArrayList<>();
			workersubmitFormMaps=workersubmitMapper.findPhoneByWorkerSubmitId(workersubmitFormMap);
			workersubmitFormMap.set("listOrder",workersubmitFormMaps);
		}else{
			WorkersubmitHeatTreatFormMap workersubmitHeatTreatFormMap=new WorkersubmitHeatTreatFormMap();
			workersubmitHeatTreatFormMap.set("content",content);
			workersubmitHeatTreatFormMap.set("state",state);
			workersubmitHeatTreatFormMap.set("submiterId",userId);
			workersubmitHeatTreatFormMap.set("startTime",startTime);
			endTime=ToolCommon.addDay(endTime,1);
			workersubmitHeatTreatFormMap.set("endTime",endTime);
			List<WorkersubmitHeatTreatFormMap> workersubmitHeatTreatFormMaps1=new ArrayList<>();
			if(state.equals("已接收未完成")){
				workersubmitHeatTreatFormMaps1=workersubmitHeatTreatMapper.findGetHeattreatPhoneByWorkerSubmitId(workersubmitHeatTreatFormMap);
			}else{
				workersubmitHeatTreatFormMaps1=workersubmitHeatTreatMapper.findCompleteHeattreatPhoneByWorkerSubmitId(workersubmitHeatTreatFormMap);
				WorkersubmitHeatTreatFormMap workersubmitHeatTreatFormMapOverTime=workersubmitHeatTreatMapper.findSumOverTimeHeattreatPhoneByWorkerSubmitId(workersubmitHeatTreatFormMap);
				WorkersubmitHeatTreatFormMap workersubmitHeatTreatFormMapWages=workersubmitHeatTreatMapper.findSumWagesHeattreatPhoneByWorkerSubmitId(workersubmitHeatTreatFormMap);
				workersubmitFormMap.set("overTime",workersubmitHeatTreatFormMapOverTime);
				workersubmitFormMap.set("wagesSum",workersubmitHeatTreatFormMapWages);
			}
			workersubmitFormMap.set("listHeatTreat",workersubmitHeatTreatFormMaps1);

		}
		workersubmitFormMap.set("result","操作成功");
		workersubmitFormMap.set("id","1");
		return workersubmitFormMap;
	}

	@ResponseBody
	@RequestMapping(value = "findByWorkerSubmitIdAndOriginAndRole", method = RequestMethod.GET, produces = "text/html; charset=utf-8")
	public WorkersubmitFormMap findByWorkerSubmitIdAndOriginAndRole(HttpServletRequest request,String content,String startTime,String endTime,String state,String origin,String role) throws Exception {
		WorkersubmitFormMap workersubmitFormMap=new WorkersubmitFormMap();
		String userId=getPara("userId");
		if(origin.equals("订单")){
			workersubmitFormMap.set("content",content);
			workersubmitFormMap.set("state",state);
			workersubmitFormMap.set("submiterId",userId);
			workersubmitFormMap.set("startTime",startTime);
			endTime=ToolCommon.addDay(endTime,1);
			workersubmitFormMap.set("endTime",endTime);
			List<WorkersubmitFormMap> workersubmitFormMaps=new ArrayList<>();
			workersubmitFormMaps=workersubmitMapper.findPhoneByWorkerSubmitId(workersubmitFormMap);
			workersubmitFormMap.set("listOrder",workersubmitFormMaps);
		}else{
				WorkersubmitHeatTreatFormMap workersubmitHeatTreatFormMap=new WorkersubmitHeatTreatFormMap();
				workersubmitHeatTreatFormMap.set("content",content);
				workersubmitHeatTreatFormMap.set("state",state);
				workersubmitHeatTreatFormMap.set("submiterId",userId);
				workersubmitHeatTreatFormMap.set("startTime",startTime);
				endTime=ToolCommon.addDay(endTime,1);
				workersubmitHeatTreatFormMap.set("endTime",endTime);
				List<WorkersubmitHeatTreatFormMap> workersubmitHeatTreatFormMaps1=new ArrayList<>();
				if(state.equals("已接收未完成")){
                    if(ToolCommon.isContain(role,"检验")){
                        workersubmitHeatTreatFormMaps1=workersubmitHeatTreatMapper.findGetCheckHeattreatPhoneByWorkerSubmitId(workersubmitHeatTreatFormMap);
                    }else{
                        workersubmitHeatTreatFormMaps1=workersubmitHeatTreatMapper.findGetHeattreatPhoneByWorkerSubmitId(workersubmitHeatTreatFormMap);
                    }

				}else{
					if(ToolCommon.isContain(role,"检验")){
                       workersubmitHeatTreatFormMaps1=workersubmitHeatTreatMapper.findCheckCompleteHeattreatPhoneByWorkerSubmitId(workersubmitHeatTreatFormMap);
                        WorkersubmitHeatTreatFormMap workersubmitHeatTreatFormMapOverTime=workersubmitHeatTreatMapper.findCheckSumOverTimeHeattreatPhoneByWorkerSubmitId(workersubmitHeatTreatFormMap);
                        WorkersubmitHeatTreatFormMap workersubmitHeatTreatFormMapWages=workersubmitHeatTreatMapper.findCheckSumWagesHeattreatPhoneByWorkerSubmitId(workersubmitHeatTreatFormMap);
                        workersubmitFormMap.set("overTime",workersubmitHeatTreatFormMapOverTime);
                        workersubmitFormMap.set("wagesSum",workersubmitHeatTreatFormMapWages);
					}else{
						workersubmitHeatTreatFormMaps1=workersubmitHeatTreatMapper.findCompleteHeattreatPhoneByWorkerSubmitId(workersubmitHeatTreatFormMap);
						WorkersubmitHeatTreatFormMap workersubmitHeatTreatFormMapOverTime=workersubmitHeatTreatMapper.findSumOverTimeHeattreatPhoneByWorkerSubmitId(workersubmitHeatTreatFormMap);
						WorkersubmitHeatTreatFormMap workersubmitHeatTreatFormMapWages=workersubmitHeatTreatMapper.findSumWagesHeattreatPhoneByWorkerSubmitId(workersubmitHeatTreatFormMap);
						workersubmitFormMap.set("overTime",workersubmitHeatTreatFormMapOverTime);
						workersubmitFormMap.set("wagesSum",workersubmitHeatTreatFormMapWages);
					}
				}
				workersubmitFormMap.set("listHeatTreat",workersubmitHeatTreatFormMaps1);
		}
		workersubmitFormMap.set("result","操作成功");
		workersubmitFormMap.set("id","1");
		return workersubmitFormMap;
	}

	@ResponseBody
	@RequestMapping("completeReceiveEntity")
	@SystemLog(module="手机班长审核",methods="录入审核结果")//凡需要处理业务逻辑的.都需要记录操作日志
	public WorkersubmitFormMap completeReceiveEntity(String blankprocessId,
										String completeAmount,
										String badAmount,
										String deductRate,
													 String isMake,
													 String makeUser,
										String workersubmitId) throws Exception {
		WorkersubmitFormMap workersubmitFormMapResult=new WorkersubmitFormMap();
		String origin=getPara("origin");
		String badReason=getPara("badReason");
		SystemconfigFormMap systemconfigFormMapWages=systemconfigMapper.findByAttribute("name","timeoutHour",SystemconfigFormMap.class).get(0);
		String timeoutHour=systemconfigFormMapWages.get("content")+"";
		float timeoutHourF=ToolCommon.StringToFloat(timeoutHour);
		if(ToolCommon.StringToFloat(badAmount)>0){
			UnqualifiedFormMap unqualifiedFormMap=new UnqualifiedFormMap();
			unqualifiedFormMap.set("unqualifiedReason",badReason);
			if(origin.equals("订单")){
				unqualifiedFormMap.set("workersubmitId",workersubmitId);
			}else{
				unqualifiedFormMap.set("workersubmitHeattreatId",workersubmitId);
			}
			unqualifiedMapper.addEntity(unqualifiedFormMap);
			ScrapFormMap scrapFormMap=new ScrapFormMap();
			scrapFormMap.set("badReason",badReason);
			if(origin.equals("订单")){
				scrapFormMap.set("workersubmitId",workersubmitId);
			}else{
				scrapFormMap.set("workersubmitHeattreatId",workersubmitId);
			}
			scrapMapper.addEntity(scrapFormMap);
		}
		if(origin.equals("订单")){
			BlankProcessFormMap blankProcessFormMap=blankProcessMapper.findById(blankprocessId);
			String blankId=blankProcessFormMap.get("blankId")+"";
			String processName=blankProcessFormMap.get("processName")+"";
			if(processName!=null&&processName.equals("下料")){
				BlankFormMap blankFormMap=blankMapper.findById(blankId);
				blankFormMap.set("isFinish","已完成");
				blankFormMap.set("finishTime",ToolCommon.getNowTime());
				blankMapper.editEntity(blankFormMap);
			}
			String completeAmountBP=blankProcessFormMap.getStr("completeAmount");
			String badAmountBP=blankProcessFormMap.getStr("badAmount");
			blankProcessFormMap.set("completeAmount",ToolCommon.StringToInteger(completeAmount)+ToolCommon.StringToInteger(completeAmountBP));
			blankProcessFormMap.set("badAmount",ToolCommon.StringToInteger(badAmount)+ToolCommon.StringToInteger(badAmountBP));
			blankProcessFormMap.set("nowreceiveAmount","");
			blankProcessFormMap.set("deductRate",deductRate);


			WorkersubmitFormMap workersubmitFormMap=workersubmitMapper.findById(workersubmitId);
			float taxPrice=ToolCommon.StringToFloat(blankProcessFormMap.getStr("taxPrice")+"");
			String completeTime=workersubmitFormMap.getStr("completeTime");
			String nowTime=ToolCommon.getNowTime();
			if(completeTime==null||completeTime.equals("")){
				int badAmountIntWorkersubmit=ToolCommon.StringToInteger(badAmount);
				int completeAmountIntWorkersubmit=ToolCommon.StringToInteger(completeAmount);
				float deductRateF=ToolCommon.StringToFloat(deductRate);
				workersubmitFormMap.set("completeTime",nowTime);
				workersubmitFormMap.set("blankprocessId",blankprocessId);
				float artificial=ToolCommon.StringToFloat(blankProcessFormMap.get("artificial")+"");
				double wages=completeAmountIntWorkersubmit*artificial;
				double deductWages=taxPrice*deductRateF*badAmountIntWorkersubmit;
				String wagesStr=ToolCommon.Double2(wages)+"";
				double trueWages=wages-deductWages;
				String deductWagesStr=ToolCommon.Double2(deductWages)+"";
				String trueWagesStr=ToolCommon.Double2(trueWages)+"";
				workersubmitFormMap.set("wages",wagesStr);
				workersubmitFormMap.set("badAmount",badAmount);
				workersubmitFormMap.set("completeAmount",completeAmount);
				workersubmitFormMap.set("deductRate",deductRate);
				workersubmitFormMap.set("deductWages",deductWagesStr);
				workersubmitFormMap.set("trueWages",trueWagesStr);
				workersubmitMapper.editEntity(workersubmitFormMap);
				blankProcessFormMap.set("state","已完成");
				blankProcessFormMap.set("completeTime",nowTime);
				blankProcessMapper.editEntity(blankProcessFormMap);
				String processId=workersubmitFormMap.get("processId")+"";
				String materialquality=workersubmitFormMap.get("materialquality")+"";
				materialquality=materialquality.replace(" ","");
				if(processId.equals("46605")){
					String oprateUserId=workersubmitFormMap.get("submiterId")+"";
					BlankFormMap blankFormMap=blankMapper.findHeatTreatByWorkersubmitId(workersubmitId);
					workersubmitMapper.updateIsHeattreatTrueById(workersubmitId);
					if(materialquality.equals("20CrMnTiH")){
						addHeartTreatByBlankFormMap("正火",blankFormMap,oprateUserId);
					}else if(materialquality.equals("38CrMoAlA")||materialquality.equals("42CrMoA")||materialquality.equals("40Cr")){
						addHeartTreatByBlankFormMap("调质",blankFormMap,oprateUserId);
					}

				}
				workersubmitFormMapResult.set("result","操作成功");
				workersubmitFormMapResult.set("id","1");
			}else{
				workersubmitFormMapResult.set("result","该工序已完成");
				workersubmitFormMapResult.set("id","0");
			}
		}else{//热处理情况完成
			WorkersubmitHeatTreatFormMap workersubmitHeatTreatFormMap=workersubmitHeatTreatMapper.findById(workersubmitId);
			String hearttreatId=workersubmitHeatTreatFormMap.get("hearttreatId")+"";
			String processId=workersubmitHeatTreatFormMap.get("processId")+"";
			String overTimeLimit=workersubmitHeatTreatFormMap.get("overTimeLimit")+"";
			float overTimeLimitFloat=ToolCommon.StringToFloat(overTimeLimit);
			HeatTreatFormMap heatTreatFormMap=heatTreatMapper.findById(hearttreatId);
			if(processId.equals("46602")){//车
				int count=workersubmitHeatTreatMapper.findUnCompleteCountByHeattreatIdAndProcessId(hearttreatId,processId);
				if(count==1){
					heatTreatFormMap.set("turnerIsFinish","已完成");
					heatTreatFormMap.set("turnerCompleteTime",ToolCommon.getNowTime());
				}

			}else if(processId.equals("46742")){//渗碳
				int count=workersubmitHeatTreatMapper.findUnCompleteCountByHeattreatIdAndProcessId(hearttreatId,processId);
				if(count==1){
					heatTreatFormMap.set("carburizationIsFinish","已完成");
				}
			}else if(processId.equals("46645")){//热渗氮
				int count=workersubmitHeatTreatMapper.findUnCompleteCountByHeattreatIdAndProcessId(hearttreatId,processId);
				if(count==1){
					heatTreatFormMap.set("feedIsFinish","已完成");
				}
			}
			String originHeattreat=heatTreatFormMap.get("origin")+"";
			float taxPrice=ToolCommon.StringToFloat(heatTreatFormMap.getStr("taxPrice")+"");
			String completeTime=workersubmitHeatTreatFormMap.getStr("completeTime");
			String nowTime=ToolCommon.getNowTime();
			if(completeTime==null||completeTime.equals("")){
				int badAmountIntWorkersubmit=ToolCommon.StringToInteger(badAmount);
				int completeAmountIntWorkersubmit=ToolCommon.StringToInteger(completeAmount);
				float deductRateF=ToolCommon.StringToFloat(deductRate);
				workersubmitHeatTreatFormMap.set("completeTime",nowTime);
				workersubmitHeatTreatFormMap.set("blankprocessId",blankprocessId);
				float artificial=ToolCommon.StringToFloat(workersubmitHeatTreatFormMap.get("artificial")+"");
				double wages=completeAmountIntWorkersubmit*artificial;
				double deductWages=taxPrice*deductRateF*badAmountIntWorkersubmit;
				String wagesStr=ToolCommon.Double2(wages)+"";
				double trueWages=wages-deductWages;
				String deductWagesStr=ToolCommon.Double2(deductWages)+"";
				String trueWagesStr=ToolCommon.Double2(trueWages)+"";
				workersubmitHeatTreatFormMap.set("wages",wagesStr);
				workersubmitHeatTreatFormMap.set("badAmount",badAmount);
				workersubmitHeatTreatFormMap.set("completeAmount",completeAmount);
				workersubmitHeatTreatFormMap.set("deductRate",deductRate);
				workersubmitHeatTreatFormMap.set("deductWages",deductWagesStr);
				workersubmitHeatTreatFormMap.set("trueWages",trueWagesStr);
			 	String startTime=workersubmitHeatTreatFormMap.getStr("startTime")+"";
				String estimateCompleteTime=workersubmitHeatTreatFormMap.getStr("estimateCompleteTime")+"";
				//应完成时间和当前时间做对比
				String useTimeStr=ToolCommon.getHourTimeDistance(startTime,nowTime);
				float useTime=ToolCommon.StringToFloat(useTimeStr);
				String isOverTime="";
				if(ToolCommon.compareTimeSize(estimateCompleteTime,nowTime)){
						isOverTime="否";
				}else{
					isOverTime="是";
				}
				workersubmitHeatTreatFormMap.set("useTime",useTime+"");
				workersubmitHeatTreatFormMap.set("isOverTime",isOverTime);
				workersubmitHeatTreatFormMap.set("sureCompleteUser",makeUser);
				workersubmitHeatTreatMapper.editEntity(workersubmitHeatTreatFormMap);
				String oprateUserId=workersubmitHeatTreatFormMap.get("submiterId")+"";
				String oprateProcessId=workersubmitHeatTreatFormMap.get("processId")+"";
				heatTreatFormMap.set("oprateUserId",oprateUserId);
				heatTreatFormMap.set("oprateProcessId",oprateProcessId);
				heatTreatFormMap.set("oprateState","已完成:"+completeAmount);
				if(makeUser.equals("窦师傅")){
					if(processId.equals("46742")||processId.equals("46730")||processId.equals("46619")){//渗碳 中频 调质
						String backTime=heatTreatFormMap.get("backTime")+"";
						String backAmount=heatTreatFormMap.get("backAmount")+"";
						if(ToolCommon.isNull(backTime)&&ToolCommon.isNull(backAmount)){
							heatTreatFormMap.set("backTime",ToolCommon.getNowDay());
							heatTreatFormMap.set("backAmount",completeAmount);
						}
					}
				}
				heatTreatMapper.editEntity(heatTreatFormMap);
				workersubmitFormMapResult.set("result","操作成功");
				workersubmitFormMapResult.set("id","1");
				if(processId.equals("46602")||processId.equals("46629")||processId.equals("46608")){
					addHeatTreat(heatTreatFormMap,"车后",completeAmount,oprateUserId);
				}
				if(originHeattreat.equals("渗碳")){
					String str=heatTreatMapper.findIsCylindricalroughgrindingById(hearttreatId);
					if(str.equals("true")){
						if(processId.equals("46609")){
							String goodId=heatTreatFormMap.get("goodId")+"";
							String goodSize=goodMapper.findGoodSizeById(goodId);
							addHeatTreat(heatTreatFormMap,"端面平磨",completeAmount,oprateUserId,goodSize);
						}

					}
				}
				if(originHeattreat.equals("渗碳")){
					String str=heatTreatMapper.findIsPinShaftById(hearttreatId);
					if(str.equals("true")){
						if(processId.equals("46632")){
							String goodId=heatTreatFormMap.get("goodId")+"";
							String goodSize=goodMapper.findGoodSizeById(goodId);
							addHeatTreat(heatTreatFormMap,"成品库",completeAmount,oprateUserId,goodSize);
							addHeatTreat(heatTreatFormMap,"打磨",completeAmount,oprateUserId,goodSize);
						}
					}
				}
				if(originHeattreat.equals("渗碳")){
					String str=heatTreatMapper.findIsPadById(hearttreatId);
					if(str.equals("true")){
						if(processId.equals("46626")){
							String goodId=heatTreatFormMap.get("goodId")+"";
							String goodSize=goodMapper.findGoodSizeById(goodId);
							addHeatTreat(heatTreatFormMap,"外磨内磨端面",completeAmount,oprateUserId,goodSize);
						}
					}
				}
				if(originHeattreat.equals("端面平磨")){
						String goodId=heatTreatFormMap.get("goodId")+"";
						String goodSize=goodMapper.findGoodSizeById(goodId);
						addHeatTreat(heatTreatFormMap,"消差磨",completeAmount,oprateUserId,goodSize);
				}
				if(originHeattreat.equals("消差磨")){
					String goodId=heatTreatFormMap.get("goodId")+"";
					String goodSize=goodMapper.findGoodSizeById(goodId);
					addHeatTreat(heatTreatFormMap,"统一尺寸",completeAmount,oprateUserId,goodSize);
				}
				if(originHeattreat.equals("统一尺寸")){
					String goodId=heatTreatFormMap.get("goodId")+"";
					String goodSize=goodMapper.findGoodSizeById(goodId);
					addHeatTreat(heatTreatFormMap,"内孔磨",completeAmount,oprateUserId,goodSize);
				}
				if(originHeattreat.equals("内孔磨")&&processId.equals("46726")){
					String goodId=heatTreatFormMap.get("goodId")+"";
					String goodSize=goodMapper.findGoodSizeById(goodId);
					addHeatTreat(heatTreatFormMap,"外圆精磨",completeAmount,oprateUserId,goodSize);
				}
				if(originHeattreat.equals("外圆精磨")){
					if(processId.equals("46632")){
						String goodId=heatTreatFormMap.get("goodId")+"";
						String goodSize=goodMapper.findGoodSizeById(goodId);
						addHeatTreat(heatTreatFormMap,"成品库",completeAmount,oprateUserId,goodSize);
						addHeatTreat(heatTreatFormMap,"打磨",completeAmount,oprateUserId,goodSize);
					}
				}
				String goodName=heatTreatFormMap.get("goodName")+"";
				String goodId=heatTreatFormMap.get("goodId")+"";
				String goodSize=goodMapper.findGoodSizeById(goodId);
				if(isMake.equals("渗碳")){
					//生成渗碳开始
					addHeatTreat(heatTreatFormMap,"渗碳",completeAmount,oprateUserId,goodSize,makeUser);
					heatTreatMapper.updateIsMakeCarburizationTrueById(hearttreatId);
					//生成渗碳结束
				}
				if(isMake.equals("中频")){
					addHeatTreat(heatTreatFormMap,"中频",completeAmount,oprateUserId,goodSize,makeUser);
				}
				if(isMake.equals("成品")){
					addHeatTreat(heatTreatFormMap,"成品库",completeAmount,oprateUserId,goodSize,makeUser);
					addHeatTreat(heatTreatFormMap,"打磨",completeAmount,oprateUserId,goodSize,makeUser);
				}
				if(originHeattreat.equals("车后")){
					//生成火前后情况开始
					if(processId.equals("46617")){//火前
							addHeatTreat(heatTreatFormMap,"火前后",completeAmount,oprateUserId,goodSize);
						}
					//生成火前后情况结束
				}
				if(originHeattreat.equals("车后")||originHeattreat.equals("火前后")||originHeattreat.equals("钳后情况")){
					//生成铣槽后情况开始
					if(processId.equals("46618")||processId.equals("46614")){//铣槽 铣
						addHeatTreat(heatTreatFormMap,"铣槽后",completeAmount,oprateUserId,goodSize);
					}
					//生成铣槽后情况结束
				}
				if(originHeattreat.equals("车后")||originHeattreat.equals("铣槽后")){
					//生成钳后情况开始
					if(processId.equals("46607")){//钳
						addHeatTreat(heatTreatFormMap,"钳后情况",completeAmount,oprateUserId,goodSize);
					}
					//生成钳后情况结束
				}
			}else{
				workersubmitFormMapResult.set("result","该工序已完成");
				workersubmitFormMapResult.set("id","0");
			}
		}
		return workersubmitFormMapResult;
	}

    @ResponseBody
    @RequestMapping("checkReceiveEntity")
    @SystemLog(module="手机班长检验",methods="录入检验结果")//凡需要处理业务逻辑的.都需要记录操作日志
    public HeattreatcheckFormMap checkReceiveEntity(String workersubmithearttreatrId,
                                                     String amount,
                                                     String badAmount,
                                                     String badReason,
                                                     String checkUserId) throws Exception {
        HeattreatcheckFormMap heattreatcheckFormMap=heattreatcheckMapper.findByWorkersubmithearttreatrId(workersubmithearttreatrId);
        heattreatcheckFormMap.set("amount",amount);
        heattreatcheckFormMap.set("badAmount",badAmount);
        heattreatcheckFormMap.set("badReason",badReason);
        heattreatcheckFormMap.set("checkUserId",checkUserId);
        heattreatcheckFormMap.set("checkTime",ToolCommon.getNowTime());
        heattreatcheckMapper.editEntity(heattreatcheckFormMap);
        heattreatcheckFormMap.set("result","操作成功");
        heattreatcheckFormMap.set("id","1");
        return heattreatcheckFormMap;
    }


    public void addHeatTreat(HeatTreatFormMap entity,String origin,String amount,String oprateUserId){
		try{
			HeatTreatFormMap heatTreatFormMap=new HeatTreatFormMap();
			String hardnessId=entity.getStr("hardnessId")+"";
			heatTreatFormMap.set("hardnessId",hardnessId);
			heatTreatFormMap.set("userId",oprateUserId);
			heatTreatFormMap.set("origin",origin);
			heatTreatFormMap.set("pickTime",ToolCommon.getNowDay());
			String contractNumber=entity.getStr("contractNumber");
			heatTreatFormMap.set("contractNumber",contractNumber);
			String goodId=entity.getStr("goodId");
			heatTreatFormMap.set("goodId",goodId);
			String goodSize=entity.getStr("goodSize");
			String goodWeight="";
			if(!ToolCommon.isNull(goodSize)){
				goodWeight=goodWeight(goodSize);
			}
			heatTreatFormMap.set("goodWeight",goodWeight);
			heatTreatFormMap.set("goodSize",goodSize);
			heatTreatFormMap.set("amount",amount);
			String clientId=entity.getStr("clientId");
			heatTreatFormMap.set("clientId",clientId);
			String weight=ToolCommon.Double2NotIn(ToolCommon.StringToFloat(amount)*ToolCommon.StringToFloat(goodWeight));
			heatTreatFormMap.set("weight",weight);
			heatTreatFormMap.set("distributionAmount",amount);
			String materialQuality=entity.getStr("materialQuality")+"";
			heatTreatFormMap.set("materialQuality",materialQuality);

			String deliveryTime=entity.getStr("deliveryTime")+"";
			heatTreatFormMap.set("deliveryTime",deliveryTime);
			heatTreatFormMap.remove("id");
			heatTreatMapper.addEntity(heatTreatFormMap);
		}catch (Exception e){

		}
	}
	public void addHeatTreat(HeatTreatFormMap entity,String origin,String amount,String oprateUserId,String goodSize){
		try{
			HeatTreatFormMap heatTreatFormMap=new HeatTreatFormMap();
			String hardnessId=entity.getStr("hardnessId")+"";
			if(origin.equals("成品库")){
				String heattreatId=entity.get("id")+"";
				String userNames=workersubmitHeatTreatMapper.findUserNamesByHeattreatId(heattreatId);
				heatTreatFormMap.set("userNames",userNames);
			}
			heatTreatFormMap.set("hardnessId",hardnessId);
			heatTreatFormMap.set("userId",oprateUserId);
			heatTreatFormMap.set("origin",origin);
			heatTreatFormMap.set("pickTime",ToolCommon.getNowDay());
			String contractNumber=entity.getStr("contractNumber");
			heatTreatFormMap.set("contractNumber",contractNumber);
			String goodId=entity.getStr("goodId");
			heatTreatFormMap.set("goodId",goodId);
			String goodWeight="";
			if(!ToolCommon.isNull(goodSize)){
				goodWeight=goodWeight(goodSize);
			}
			heatTreatFormMap.set("goodWeight",goodWeight);
			heatTreatFormMap.set("goodSize",goodSize);
			heatTreatFormMap.set("amount",amount);
			String clientId=entity.getStr("clientId");
			heatTreatFormMap.set("clientId",clientId);
			String weight=ToolCommon.Double2NotIn(ToolCommon.StringToFloat(amount)*ToolCommon.StringToFloat(goodWeight));
			heatTreatFormMap.set("weight",weight);
			if(!origin.equals("渗碳")){
				heatTreatFormMap.set("distributionAmount",amount);
			}
			if(origin.equals("中频")||origin.equals("调质")||origin.equals("渗碳")){
				heatTreatFormMap.set("distributionAmount",amount);
			}
			String materialQuality=entity.getStr("materialQuality")+"";
			heatTreatFormMap.set("materialQuality",materialQuality);
			String deliveryTime=entity.getStr("deliveryTime")+"";
			heatTreatFormMap.set("deliveryTime",deliveryTime);
			heatTreatFormMap.remove("id");
			heatTreatMapper.addEntity(heatTreatFormMap);
		}catch (Exception e){

		}
	}
	public void addHeatTreat(HeatTreatFormMap entity,String origin,String amount,String oprateUserId,String goodSize,String makeUser){
		try{
			HeatTreatFormMap heatTreatFormMap=new HeatTreatFormMap();
			String hardnessId=entity.getStr("hardnessId")+"";
			if(origin.equals("成品库")){
				String heattreatId=entity.get("id")+"";
				String userNames=workersubmitHeatTreatMapper.findUserNamesByHeattreatId(heattreatId);
				heatTreatFormMap.set("userNames",userNames);
			}
			heatTreatFormMap.set("hardnessId",hardnessId);
			heatTreatFormMap.set("userId",oprateUserId);
			heatTreatFormMap.set("origin",origin);
			heatTreatFormMap.set("pickTime",ToolCommon.getNowDay());
			String contractNumber=entity.getStr("contractNumber");
			heatTreatFormMap.set("contractNumber",contractNumber);
			String goodId=entity.getStr("goodId");
			heatTreatFormMap.set("goodId",goodId);
			String goodWeight="";
			if(!ToolCommon.isNull(goodSize)){
				goodWeight=goodWeight(goodSize);
			}
			heatTreatFormMap.set("goodWeight",goodWeight);
			heatTreatFormMap.set("goodSize",goodSize);
			heatTreatFormMap.set("amount",amount);
			String clientId=entity.getStr("clientId");
			heatTreatFormMap.set("clientId",clientId);
			String weight=ToolCommon.Double2NotIn(ToolCommon.StringToFloat(amount)*ToolCommon.StringToFloat(goodWeight));
			heatTreatFormMap.set("weight",weight);
			if(!origin.equals("渗碳")){
				heatTreatFormMap.set("distributionAmount",amount);
			}
			if(origin.equals("中频")||origin.equals("调质")||origin.equals("渗碳")){
				heatTreatFormMap.set("distributionAmount",amount);
			}
			String materialQuality=entity.getStr("materialQuality")+"";
			heatTreatFormMap.set("materialQuality",materialQuality);
			String deliveryTime=entity.getStr("deliveryTime")+"";
			heatTreatFormMap.set("deliveryTime",deliveryTime);
			heatTreatFormMap.set("makeUser",makeUser);
			heatTreatFormMap.remove("id");
			heatTreatMapper.addEntity(heatTreatFormMap);
		}catch (Exception e){

		}
	}

	@ResponseBody
	@RequestMapping(value = "findTeamByRoleId", method = RequestMethod.GET, produces = "text/html; charset=utf-8")
	public TeamFormMap findTeamByRoleId(String roleId) throws Exception {
		TeamFormMap teamFormMap=new TeamFormMap();
		TeamFormMap teamFormMap1=new TeamFormMap();
		teamFormMap1.set("roleId",roleId);
		String userIds=teamMapper.finUserIdsdByRoleId(roleId);
		List<UserFormMap> userFormMaps=new ArrayList<>();
		if(userIds==null||userIds.equals("")){

		}else{
			UserFormMap userFormMap=new UserFormMap();
			userFormMap.set("userIds",userIds);
			userFormMaps=userMapper.findByUserIds(userFormMap);
		}

		teamFormMap.set("list",userFormMaps);
		teamFormMap.set("result","操作成功");
		teamFormMap.set("id","1");
		return teamFormMap;
	}

	@ResponseBody
	@RequestMapping(value = "findProcessByUserIds", method = RequestMethod.GET, produces = "text/html; charset=utf-8")
	public TeamFormMap findProcessByUserIds(String userIds) throws Exception {
		TeamFormMap teamFormMap=new TeamFormMap();
		String startTime=getPara("startTime");
		String endTime=getPara("endTime");
		if(endTime!=null&&!endTime.equals("")){
			endTime=ToolCommon.addDay(endTime,1);
		}
		String content=getPara("content");
		WorkersubmitFormMap workersubmitFormMap=new WorkersubmitFormMap();
		workersubmitFormMap.set("submiterIds",userIds);
		workersubmitFormMap.set("startTime",startTime);
		workersubmitFormMap.set("endTime",endTime);
		workersubmitFormMap.set("content",content);
		List<WorkersubmitFormMap> workersubmitFormMaps=workersubmitMapper.findPhoneShowBySubmiterIds(workersubmitFormMap);
		List<WorkersubmitFormMap> workersubmitFormMapsResult=new ArrayList<>();
		for(int i=0;i<workersubmitFormMaps.size();i++){
			WorkersubmitFormMap workersubmitFormMap1=workersubmitFormMaps.get(i);
			String orderdetailsId=workersubmitFormMap1.get("orderdetailsId")+"";
			boolean isComplete=isCompleteByOrderdetailsId(orderdetailsId);
			if(!isComplete){
				BlankFormMap blankFormMap = getFormMap(BlankFormMap.class);
				blankFormMap.set("orderdetailsId",orderdetailsId);
				blankFormMap.set("origin","订单");
				List<BlankFormMap> blankFormMaps=blankMapper.findPhoneShowByOrderdetailsId(blankFormMap);
				workersubmitFormMap1.set("process",blankFormMaps);
				workersubmitFormMapsResult.add(workersubmitFormMap1);
			}
		}
		teamFormMap.set("list",workersubmitFormMapsResult);
		teamFormMap.set("result","操作成功");
		teamFormMap.set("id","1");
		return teamFormMap;
	}

	public boolean isCompleteByOrderdetailsId(String orderdetailsId){
		BlankProcessFormMap blankProcessFormMap=blankProcessMapper.findMaxProcessByOrderDetailsId(orderdetailsId);
		if(blankProcessFormMap==null){
			return false;
		}else{
			String number=blankProcessFormMap.get("number")+"";
			String maxNumber=blankProcessFormMap.get("maxNumber")+"";
			String state=blankProcessFormMap.get("state")+"";
			if(state.equals("已完成")){
				if(number.equals(maxNumber)){
					return true;
				}else{
					return false;
				}

			}else{
				return false;
			}

		}
	}

	@ResponseBody
	@RequestMapping(value = "findAttendanceByWorkerSubmitId", method = RequestMethod.GET, produces = "text/html; charset=utf-8")
	public AttendanceFormMap findAttendanceByWorkerSubmitId(HttpServletRequest request,String startTime,String endTime) throws Exception {
		AttendanceFormMap workersubmitFormMap=new AttendanceFormMap();
		String userId=getNowUserMessage().get("id")+"";
		workersubmitFormMap.set("submiterId",userId);
		workersubmitFormMap.set("startTime",startTime);
		endTime=ToolCommon.addDay(endTime,1);
		workersubmitFormMap.set("endTime",endTime);
		List<AttendanceFormMap> workersubmitFormMaps=attendanceMapper.findByWorkerSubmitIdByStartTimeAndEndTimeAndUser(workersubmitFormMap);
		workersubmitFormMap.set("list",workersubmitFormMaps);
		workersubmitFormMap.set("result","操作成功");
		workersubmitFormMap.set("id","1");
		return workersubmitFormMap;
	}

	@ResponseBody
	@RequestMapping(value = "findTabAttendanceByWorkerSubmitId", method = RequestMethod.GET, produces = "text/html; charset=utf-8")
	public WorkoverwageFormMap findTabAttendanceByWorkerSubmitId(HttpServletRequest request, String startTime, String endTime) throws Exception {
		WorkoverwageFormMap workoverwageFormMap=new WorkoverwageFormMap();
		String userId=getNowUserMessage().get("id")+"";
		workoverwageFormMap.set("userId",userId);
		workoverwageFormMap.set("startTime",startTime);
		endTime=ToolCommon.addDay(endTime,1);
		workoverwageFormMap.set("endTime",endTime);
		workoverwageFormMap.set("content","");
		List<WorkoverwageFormMap> workoverwageFormMaps=workoverwageMapper.findByAllLike(workoverwageFormMap);
		workoverwageFormMap.set("workoverwagelist",workoverwageFormMaps);
		WorkoverwageFormMap workoverwageFormMapSum=workoverwageMapper.findAllSumByAllLike(workoverwageFormMap);
		workoverwageFormMap.set("workoverwageFormMapSum",workoverwageFormMapSum);
		LeaveFormMap leaveFormMap=new LeaveFormMap();
		leaveFormMap.set("userId",userId);
		leaveFormMap.set("startTime",startTime);
		leaveFormMap.set("endTime",endTime);
		leaveFormMap.set("content","");
		List<LeaveFormMap> leaveFormMaps=leaveMapper.findByAllLike(leaveFormMap);
		workoverwageFormMap.set("leavelist",leaveFormMaps);
		String leavedays=leaveMapper.findAllDaysByAllLike(leaveFormMap);
		workoverwageFormMap.set("leavedays",leavedays);
		workoverwageFormMap.set("result","操作成功");
		workoverwageFormMap.set("id","1");
		return workoverwageFormMap;
	}

	@ResponseBody
	@RequestMapping(value = "findWorkoverwageByWorkerSubmitId", method = RequestMethod.GET, produces = "text/html; charset=utf-8")
	public WorkoverwageFormMap findWorkoverwageByWorkerSubmitId(HttpServletRequest request,String startTime,String endTime) throws Exception {
		WorkoverwageFormMap workoverwageFormMap=new WorkoverwageFormMap();
		String userId=getNowUserMessage().get("id")+"";
		workoverwageFormMap.set("userId",userId);
		workoverwageFormMap.set("startTime",startTime);
		endTime=ToolCommon.addDay(endTime,1);
		workoverwageFormMap.set("endTime",endTime);
		List<WorkoverwageFormMap> workoverwageFormMaps=workoverwageMapper.findByAllLike(workoverwageFormMap);
		workoverwageFormMap.set("list",workoverwageFormMaps);
		workoverwageFormMap.set("result","操作成功");
		workoverwageFormMap.set("id","1");
		return workoverwageFormMap;
	}



	@ResponseBody
	@RequestMapping(value = "findLeaveByWorkerSubmitId", method = RequestMethod.GET, produces = "text/html; charset=utf-8")
	@SystemLog(module="手机我的",methods="请假列表-获取列表内容")//凡需要处理业务逻辑的.都需要记录操作日志
	public LeaveFormMap findLeaveByWorkerSubmitId(HttpServletRequest request,String startTime,String endTime) throws Exception {
		LeaveFormMap leaveFormMap=new LeaveFormMap();
		String userId=getNowUserMessage().get("id")+"";
		leaveFormMap.set("userId",userId);
		leaveFormMap.set("startTime",startTime);
		endTime=ToolCommon.addDay(endTime,1);
		leaveFormMap.set("endTime",endTime);
		List<LeaveFormMap> leaveFormMaps=leaveMapper.findByAllLike(leaveFormMap);
		leaveFormMap.set("list",leaveFormMaps);
		leaveFormMap.set("result","操作成功");
		leaveFormMap.set("id","1");
		return leaveFormMap;
	}

	@ResponseBody
	@RequestMapping(value = "findToolOutByWorkerSubmitId", method = RequestMethod.GET, produces = "text/html; charset=utf-8")
	@SystemLog(module="手机我的",methods="工具出库统计-获取列表内容")//凡需要处理业务逻辑的.都需要记录操作日志
	public ToolOutFormMap findToolOutByWorkerSubmitId(HttpServletRequest request,String content,String startTime,String endTime) throws Exception {
		ToolOutFormMap toolOutFormMap=new ToolOutFormMap();
		String userId=getNowUserMessage().get("id")+"";
		toolOutFormMap.set("userId",userId);
		toolOutFormMap.set("startTime",startTime);
		endTime=ToolCommon.addDay(endTime,1);
		toolOutFormMap.set("endTime",endTime);
		List<ToolOutFormMap> toolOutFormMaps=toolOutMapper.findByUserId(toolOutFormMap);
		int sumAmount=toolOutMapper.findSumAmountByUserId(toolOutFormMap);
		toolOutFormMap.set("sumAmount",sumAmount);
		toolOutFormMap.set("list",toolOutFormMaps);
		toolOutFormMap.set("result","操作成功");
		toolOutFormMap.set("id","1");
		return toolOutFormMap;
	}


	@ResponseBody
	@RequestMapping("searchProcessTimeM")
	public OrderDetailsProcessFormMap searchProcessTimeM(String completeUserId,String startTime,String endTime) throws Exception {
		OrderDetailsProcessFormMap orderDetailsProcessFormMap = getFormMap(OrderDetailsProcessFormMap.class);
		OrderDetailsProcessFormMap orderDetailsProcessFormMap1=new OrderDetailsProcessFormMap();
		if(startTime==null){
			startTime="";
		}
		if(endTime==null){
			endTime="";
		}else{
			endTime=ToolCommon.addDay(endTime,1);
		}
		orderDetailsProcessFormMap1.set("completeUserId",completeUserId);
		orderDetailsProcessFormMap1.set("startTime",startTime);
		orderDetailsProcessFormMap1.set("endTime",endTime);
		List<OrderDetailsProcessFormMap> makeorderdetailsFormMaps=orderDetailsProcessMapper.findByCompleteUserIdAndStartTimeAndEndTime(orderDetailsProcessFormMap1);
		OrderDetailsProcessFormMap orderDetailsProcessFormMap2=orderDetailsProcessMapper.findTimeAndAmountByCompleteUserIdAndStartTimeAndEndTime(orderDetailsProcessFormMap1);
		orderDetailsProcessFormMap.set("list",makeorderdetailsFormMaps);
		orderDetailsProcessFormMap.set("id","1");
		orderDetailsProcessFormMap.set("totalEntity",orderDetailsProcessFormMap2);
		return orderDetailsProcessFormMap;
	}

	@ResponseBody
	@RequestMapping("searchOrderGoodCodeByBatchNumberM")
	public OrderGoodCodeFormMap searchOrderGoodCodeByBatchNumberM(String batchNumber,String content) throws Exception {
		OrderGoodCodeFormMap orderGoodCodeFormMap = getFormMap(OrderGoodCodeFormMap.class);
		OrderGoodCodeFormMap orderGoodCodeFormMap1=new OrderGoodCodeFormMap();
		orderGoodCodeFormMap1.set("content",content);
		orderGoodCodeFormMap1.set("batchNumber",batchNumber);
		List<OrderGoodCodeFormMap> orderGoodCodeFormMaps=orderGoodCodeMapper.findAllByBatchNumber(orderGoodCodeFormMap1);
		orderGoodCodeFormMap.set("list",orderGoodCodeFormMaps);
		orderGoodCodeFormMap.set("id","1");
		return orderGoodCodeFormMap;
	}
}
