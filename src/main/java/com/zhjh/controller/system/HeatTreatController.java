package com.zhjh.controller.system;

import com.zhjh.annotation.SystemLog;
import com.zhjh.controller.index.BaseController;
import com.zhjh.entity.*;
import com.zhjh.file.ExcelUtils;
import com.zhjh.mapper.*;
import com.zhjh.tool.ToolCommon;
import com.zhjh.util.Common;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.tools.Tool;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lanyuan 2014-11-19
 * @Email: mmm333zzz520@163.com
 * @version 3.0v
 */
@Controller
@RequestMapping("/heattreat/")
public class HeatTreatController extends BaseController {
	@Inject
	private OrderDetailsMapper orderDetailsMapper;
	@Inject
	private HeatTreatMapper heattreatMapper;

	@Inject
	private BlankMapper blankMapper;

	@Inject
	private PostSearchMapper postSearchMapper;

	@Inject
	private UserMapper userMapper;

	@Inject
	private GoodProcessMapper goodProcessMapper;

	@Inject
	private WorkersubmitHeatTreatMapper workersubmitHeatTreatMapper;

	@Inject
	private HeattreatcheckMapper heattreatcheckMapper;


	@Inject
	private GoodMapper goodMapper;

	@Inject
	private ProcessMapper processMapper;

	@Inject
	private SystemconfigMapper systemconfigMapper;

	@Inject
	private BadnoticeMapper badnoticeMapper;

	@Inject
	private UnqualifiedMapper unqualifiedMapper;

	@Inject
	private ScrapMapper scrapMapper;

	@RequestMapping("list")
	public String list(Model model,String origin) throws Exception {
		String id = getPara("id");
		origin="";
		String remarks="";
		if(id.equals("702")){
			origin="渗碳";
			model.addAttribute("res", findByRes("692"));
		}else if(id.equals("692")){
			origin="中频";
			model.addAttribute("res", findByRes());
		}else if(id.equals("701")){
			origin="调质";
			model.addAttribute("res", findByRes("692"));
		}else if(id.equals("719")){
			origin="正火";
			model.addAttribute("res", findByRes("692"));
		}else if(id.equals("740")){
			origin="车后";
			remarks="销轴";
			model.addAttribute("res", findByRes("692"));
		}else if(id.equals("741")){
            origin="车后";
            remarks="钢套";
            model.addAttribute("res", findByRes("692"));
        }else if(id.equals("742")){
            origin="车后";
            remarks="垫";
            model.addAttribute("res", findByRes("692"));
        }else if(id.equals("721")){
			origin="火前后";
			model.addAttribute("res", findByRes("692"));
		}else if(id.equals("722")){
			origin="铣槽后";
			model.addAttribute("res", findByRes("692"));
		}else if(id.equals("723")){
			origin="钳后情况";
			model.addAttribute("res", findByRes("692"));
		}else if(id.equals("704")){
			origin="渗碳";
			remarks="外圆粗磨";
			List<ResFormMap> resFormMaps=findByRes("692");
			resFormMaps.remove(0);
			model.addAttribute("res",resFormMaps);
		}else if(id.equals("724")){
			origin="成品库";
			List<ResFormMap> resFormMaps=findByRes("692");
			resFormMaps.remove(0);
			model.addAttribute("res",resFormMaps);
		}else if(id.equals("762")){
			origin="打磨";
			List<ResFormMap> resFormMaps=findByRes("692");
			resFormMaps.remove(0);
			model.addAttribute("res",resFormMaps);
		}else if(id.equals("713")){
			origin="渗碳";
			remarks="外圆磨（轴）";
			List<ResFormMap> resFormMaps=findByRes("692");
			resFormMaps.remove(0);
			model.addAttribute("res",resFormMaps);
		}else if(id.equals("705")){
			origin="端面平磨";
			model.addAttribute("res", findByRes("692"));
		}else if(id.equals("706")){
			origin="消差磨";
			model.addAttribute("res", findByRes("692"));
		}else if(id.equals("707")){
			origin="统一尺寸";
			model.addAttribute("res", findByRes("692"));
		}else if(id.equals("708")){
			origin="内孔磨";
			model.addAttribute("res", findByRes("692"));
		}else if(id.equals("709")){
			origin="外圆精磨";
			model.addAttribute("res", findByRes("692"));
		}else if(id.equals("715")){
			origin="渗碳";
			remarks="平磨（垫片）";
			List<ResFormMap> resFormMaps=findByRes("692");
			resFormMaps.remove(0);
			model.addAttribute("res",resFormMaps);
		}else if(id.equals("716")){
			origin="外磨内磨端面";
			model.addAttribute("res", findByRes("692"));
		}
		model.addAttribute("remarks",remarks);
		model.addAttribute("origin",origin);
		String pickTimeStart=ToolCommon.getNowMonth()+"-01";
		String pickTimeEnd=ToolCommon.getNowMonth()+"-31";
		model.addAttribute("pickTimeStart",pickTimeStart);
		model.addAttribute("pickTimeEnd",pickTimeEnd);
		UserFormMap userFormMap=getNowUserMessage();
		model.addAttribute("user", userFormMap);

		String processUrl="";
		if(origin.equals("渗碳")&&!remarks.equals("外圆粗磨")&&!remarks.equals("外圆磨（轴）")){
			processUrl="/json/carburizationProcess.json";
		}
		if(origin.equals("渗碳")&&remarks.equals("外圆粗磨")){
			processUrl="/json/cylindricalroughgrindingProcess.json";
		}
		if(origin.equals("渗碳")&&remarks.equals("外圆磨（轴）")){
			processUrl="/json/cylindricalGrindingShaftProcess.json";
		}
		if(origin.equals("渗碳")&&remarks.equals("平磨（垫片）")){
			processUrl="/json/flatgrindingGgasketProcess.json";
		}

		if(origin.equals("外磨内磨端面")){
			processUrl="/json/externalgrindingProcess.json";
		}
		if(origin.equals("中频")){
			processUrl="/json/midfrequencyProcess.json";
		}
		if(origin.equals("调质")){
			String roleName=userFormMap.get("roleName")+"";
			if(roleName.equals("分管理员")){
				processUrl="/json/conditioningProcessAdmin.json";
			}else{
				processUrl="/json/conditioningProcess.json";
			}

		}
		if(origin.equals("正火")){
			processUrl="/json/normalizingProcess.json";
		}
		if(origin.equals("车后")){
		    if(remarks.equals("销轴")){
                processUrl="/json/carbehindpinshaftProcess.json";
            }else if(remarks.equals("钢套")){
                processUrl="/json/carbehindsteelsleeveProcess.json";
            }else if(remarks.equals("垫")){
                processUrl="/json/carbehindpadProcess.json";
            }
		}
		if(origin.equals("火前后")){
			processUrl="/json/fireProcess.json";
		}
		if(origin.equals("铣槽后")){
			processUrl="/json/millingGrooveProcess.json";
		}
		if(origin.equals("钳后情况")){
			processUrl="/json/preliminaryCarburizingProcess.json";
		}
		if(origin.equals("端面平磨")){
			processUrl="/json/endfaceflatgrindingProcess.json";
		}
		if(origin.equals("消差磨")){
			processUrl="/json/erroreliminationmillProcess.json";
		}
		if(origin.equals("统一尺寸")){
			processUrl="/json/uniformsizeProcess.json";
		}
		if(origin.equals("内孔磨")){
			processUrl="/json/boremillProcess.json";
		}
		if(origin.equals("外圆精磨")){
			processUrl="/json/cylindricalfinishgrindingProcess.json";
		}
		if(origin.equals("打磨")){
			processUrl="/json/polishProcess.json";
		}
		model.addAttribute("processUrl",processUrl);
		return Common.BACKGROUND_PATH + "/system/heattreat/list";
	}

	/**
	 * 验证资源是否存在
	 *
	 * @author lanyuan Email：mmm333zzz520@163.com date：2014-2-19
	 * @param processNames
	 * @return
	 */
	@RequestMapping("calculationMoney")
	@ResponseBody
	public float calculationMoney(String processNames,String heattreatIds) {
		float money=0;
		List<HeatTreatFormMap> heatTreatFormMaps=heattreatMapper.findDistributionAmountByIds(heattreatIds);
		for(int i=0;i<heatTreatFormMaps.size();i++){
			HeatTreatFormMap heatTreatFormMap=heatTreatFormMaps.get(i);
			String goodId=heatTreatFormMap.get("id")+"";
			String distributionAmount=heatTreatFormMap.get("distributionAmount")+"";
			List<GoodProcessFormMap> goodProcessFormMaps=goodProcessMapper.findArtificialByNameAndGoodId(processNames,goodId);
			for(int j=0;j<goodProcessFormMaps.size();j++){
				GoodProcessFormMap goodProcessFormMap=goodProcessFormMaps.get(j);
				String artificial=goodProcessFormMap.get("artificial")+"";
				money=money+ToolCommon.StringToFloat(distributionAmount)*ToolCommon.StringToFloat(artificial);
			}
		}
		return money;
	}

	@RequestMapping("calculationAllWeight")
	@ResponseBody
	public float calculationAllWeight(String badAmount,String heattreatId) {
		String goodId=heattreatMapper.findGoodIdById(heattreatId);
		float allWeight=getBadWeightByGoodIdAndBadAmount(goodId,badAmount);
		return allWeight;
	}

	@RequestMapping("getLatelyEstimateCompleteTimeBySubmiterId")
	@ResponseBody
	public String getLatelyEstimateCompleteTimeBySubmiterId(String submiterId) {
		String estimateCompleteTime=workersubmitHeatTreatMapper.findMaxEstimateCompleteTimeBySubmiterId(submiterId);
		return estimateCompleteTime;
	}

	@ResponseBody
	@RequestMapping("findClientByPage")
	@SystemLog(module="热处理情况",methods="获取客户列表内容")
	public List findClientByPage() throws Exception {
		String origin=getPara("origin");
		String remarks=getPara("remarks");
		String userId=getPara("userId");
		String content=getPara("content");
		String pickTimeStart=getPara("pickTimeStart");
		String pickTimeEnd=getPara("pickTimeEnd");
		String backTimeStart=getPara("backTimeStart");
		String backTimeEnd=getPara("backTimeEnd");

		String starttime=getPara("startTime");
		String endtime=getPara("endTime");
		String goodName=getPara("goodName");
		if(!ToolCommon.isNull(goodName)&&goodName.equals("不限")){
			goodName="";
		}
		String clientId=getPara("clientId");
		String state=getPara("state");
		String getstate=getPara("getstate");
		String sendstate=getPara("sendstate");
		String isDistribution=getPara("isDistribution");
		String materialQuality=getPara("materialQuality");
		String oprateUserId=getPara("oprateUserId");
		String oprateProcessId=getPara("oprateProcessId");
		HeatTreatFormMap heattreatFormMap=new HeatTreatFormMap();
		heattreatFormMap.set("isDistribution",isDistribution);
		heattreatFormMap.set("origin",origin);
		heattreatFormMap.set("remarks",remarks);
		heattreatFormMap.set("userId",userId);
		heattreatFormMap.set("content",content);
		heattreatFormMap.set("pickTimeStart",pickTimeStart);
		heattreatFormMap.set("pickTimeEnd",pickTimeEnd);
		heattreatFormMap.set("backTimeStart",backTimeStart);
		heattreatFormMap.set("backTimeEnd",backTimeEnd);
		heattreatFormMap.set("startTime",starttime);
		heattreatFormMap.set("endTime",endtime);

		heattreatFormMap.set("goodName",goodName);
		heattreatFormMap.set("clientId",clientId);
		heattreatFormMap.set("getstate",getstate);
		heattreatFormMap.set("state",state);
		heattreatFormMap.set("sendstate",sendstate);
		heattreatFormMap.set("materialQuality",materialQuality);
		heattreatFormMap.set("oprateUserId",oprateUserId);
		heattreatFormMap.set("oprateProcessId",oprateProcessId);
		List<ClientFormMap> clientFormMaps=heattreatMapper.findClientByAllLike(heattreatFormMap);
		List<TreeClientEntity> treeClientEntities=new ArrayList<>();
		boolean isAddNullClient=true;
		try{
			for(int i=0;i<clientFormMaps.size();i++){
				ClientFormMap clientFormMap=clientFormMaps.get(i);
				if(clientFormMap!=null){
					String clientIdSelect=clientFormMap.get("clientId")+"";
					String fullName=clientFormMap.getStr("fullName");
					TreeClientEntity treeClientEntity=new TreeClientEntity();
					if(ToolCommon.isNull(clientIdSelect)){
						if(isAddNullClient){
							isAddNullClient=false;
							treeClientEntity.id=clientIdSelect;
							treeClientEntity.text=fullName;
							treeClientEntities.add(treeClientEntity);
						}
					}else{
						treeClientEntity.id=clientIdSelect;
						treeClientEntity.text=fullName;
						treeClientEntities.add(treeClientEntity);
					}
				}
			}
			return treeClientEntities;
		}catch (Exception e){
			System.out.print(e);
			return treeClientEntities;
		}

	}

	@ResponseBody
	@RequestMapping("findExecutionClientByPage")
	@SystemLog(module="热处理情况",methods="接收完成情况-获取客户列表内容")
	public List findExecutionClientByPage() throws Exception {
		String content=getPara("content");

		String starttime=getPara("startTime");
		String endtime=getPara("endTime");
		String goodName=getPara("goodName");
		String clientId=getPara("clientId");
		String state=getPara("state");
		String oprateUserId=getPara("oprateUserId");
		String materialQuality=getPara("materialQuality");
		HeatTreatFormMap heattreatFormMap=new HeatTreatFormMap();
		heattreatFormMap.set("content",content);
		heattreatFormMap.set("oprateUserId",oprateUserId);
		heattreatFormMap.set("startTime",starttime);
		heattreatFormMap.set("endTime",endtime);
		heattreatFormMap.set("goodName",goodName);
		heattreatFormMap.set("clientId",clientId);
		heattreatFormMap.set("state",state);
		heattreatFormMap.set("materialQuality",materialQuality);
		List<ClientFormMap> clientFormMaps=heattreatMapper.findExecutionClientByAllLike(heattreatFormMap);
		List<TreeClientEntity> treeClientEntities=new ArrayList<>();
		boolean isAddNullClient=true;
		try{
			for(int i=0;i<clientFormMaps.size();i++){
				ClientFormMap clientFormMap=clientFormMaps.get(i);
				if(clientFormMap!=null){
					String clientIdSelect=clientFormMap.get("clientId")+"";
					String fullName=clientFormMap.getStr("fullName");
					TreeClientEntity treeClientEntity=new TreeClientEntity();
					if(ToolCommon.isNull(clientIdSelect)){
						if(isAddNullClient){
							isAddNullClient=false;
							treeClientEntity.id=clientIdSelect;
							treeClientEntity.text=fullName;
							treeClientEntities.add(treeClientEntity);
						}
					}else{
						treeClientEntity.id=clientIdSelect;
						treeClientEntity.text=fullName;
						treeClientEntities.add(treeClientEntity);
					}
				}
			}
			return treeClientEntities;
		}catch (Exception e){
			System.out.print(e);
			return treeClientEntities;
		}

	}

	public class TreeClientEntity {
		public String id;
		public String text;
		public String state;
	}
	@RequestMapping("findGetListUI")
	@SystemLog(module="热处理情况",methods="已接收列表-界面")
	public String findGetListUI(Model model) throws Exception {
		model.addAttribute("res", findByRes());
		model.addAttribute("postSearchList",postSearchMapper.findAll());
		return Common.BACKGROUND_PATH + "/system/heattreat/getlist";
	}

	@ResponseBody
	@RequestMapping("findGetList")
	@SystemLog(module="热处理情况",methods="已接收列表-获取列表内容")
	public HeatTreatFormMap findGetList(String getMaxEstimateCompleteTime,String user,String goodName,String content,String startTime,String endTime) throws Exception {
		HeatTreatFormMap heatTreatFormMap=new HeatTreatFormMap();
		WorkersubmitHeatTreatFormMap workersubmitHeatTreatFormMap = getFormMap(WorkersubmitHeatTreatFormMap.class);
		String page=getPara("page");
		String rows=getPara("rows");
		if(content==null){
			content="";
		}
		if(user==null){
			user="";
		}
		if(startTime==null){
			startTime=ToolCommon.getNowMonth()+"-01";
		}
		if(endTime==null){
			endTime=ToolCommon.getNowDay();
		}
		endTime= ToolCommon.addDay(endTime,1);
		workersubmitHeatTreatFormMap.set("content",content);
		workersubmitHeatTreatFormMap.set("state","已接收未完成");
		workersubmitHeatTreatFormMap.set("user",user);
		workersubmitHeatTreatFormMap.set("goodName",goodName);
		workersubmitHeatTreatFormMap.set("startTime",startTime);
		workersubmitHeatTreatFormMap.set("endTime",endTime);
		WorkersubmitHeatTreatFormMap workersubmitFormMap1=new WorkersubmitHeatTreatFormMap();
		List<WorkersubmitHeatTreatFormMap> workersubmitFormMaps=new ArrayList<>();
		if(getMaxEstimateCompleteTime.equals("是")){
			WorkersubmitHeatTreatFormMap workersubmitHeatTreatFormMapMax=workersubmitHeatTreatMapper.findMaxEstimateCompleteTimeByWorkerSubmitIdByStartTimeAndEndTimeAndContentAndUser(workersubmitHeatTreatFormMap);
			String id=workersubmitHeatTreatFormMapMax.get("id")+"";
			workersubmitFormMap1=workersubmitHeatTreatMapper.findAllMoneyById(id);
			total=1;
			workersubmitFormMaps.add(workersubmitHeatTreatFormMapMax);
		}else{
			workersubmitFormMap1=workersubmitHeatTreatMapper.findAllMoneyByWorkerSubmitIdByStartTimeAndEndTimeAndContentAndUser(workersubmitHeatTreatFormMap);
			if(page.equals("1")){
				total=workersubmitHeatTreatMapper.findCountByWorkerSubmitIdByStartTimeAndEndTimeAndContentAndUser(workersubmitHeatTreatFormMap);
			}
			workersubmitHeatTreatFormMap=toFormMap(workersubmitHeatTreatFormMap, page, rows,workersubmitHeatTreatFormMap.getStr("orderby"));
			String sort=getPara("sort");
			String order=getPara("order");

			workersubmitHeatTreatFormMap.set("sort",sort);
			workersubmitHeatTreatFormMap.set("order",order);
			workersubmitFormMaps=workersubmitHeatTreatMapper.findByWorkerSubmitIdByStartTimeAndEndTimeAndContentAndUser(workersubmitHeatTreatFormMap);
		}
		if(workersubmitFormMap1==null){
			workersubmitFormMap1=new WorkersubmitHeatTreatFormMap();
		}
		heatTreatFormMap.set("money",workersubmitFormMap1);


		heatTreatFormMap.set("rows",workersubmitFormMaps);
		heatTreatFormMap.set("total",total);
		return heatTreatFormMap;
	}

	@ResponseBody
	@RequestMapping("findPostSearch")
	public List findPostSearch() throws Exception {
		List<PostSearchFormMap> postSearchFormMaps=postSearchMapper.findAll();
		String origin=getPara("origin");
		if(!ToolCommon.isNull(origin)&&origin.equals("已完成")){
			PostSearchFormMap postSearchFormMap=new PostSearchFormMap();
			postSearchFormMap.set("id",postSearchFormMaps.size()+1+"post");
			postSearchFormMap.set("text","确认完成人");
			postSearchFormMap.set("state","closed");
			postSearchFormMaps.add(postSearchFormMap);
		}
		return postSearchFormMaps;
	}

	@ResponseBody
	@RequestMapping("findCheckPostSearch")
	public List findCheckPostSearch() throws Exception {
		List<PostSearchFormMap> postSearchFormMaps=postSearchMapper.findAll();
			PostSearchFormMap postSearchFormMap=new PostSearchFormMap();
			postSearchFormMap.set("id",postSearchFormMaps.size()+1+"post");
			postSearchFormMap.set("text","检验员");
			postSearchFormMap.set("state","closed");
			postSearchFormMaps.add(postSearchFormMap);
		return postSearchFormMaps;
	}

	@ResponseBody
	@RequestMapping("findUserByPost")
	public List findUserByPost(String post,String isWarning) throws Exception {
		List<UserFormMap> userFormMapList=new ArrayList<>();
		if(post.equals("确认完成人")){
			userFormMapList=userMapper.findCompleteByWages("确认完成人");
		}else if(post.equals("检验员")){
			userFormMapList=userMapper.findCheckByWages("检验确认人");
		}else{
			userFormMapList=userMapper.findByTitle(post);
		}
		if(isWarning.equals("true")){
			List<UserFormMap> userFormMapList1=new ArrayList<>();
			for(int i=0;i<userFormMapList.size();i++){
				UserFormMap userFormMap=userFormMapList.get(i);
				String userId=userFormMap.get("id")+"";
				userFormMap.set("isWarning",isWarning(userId));
				userFormMapList1.add(userFormMap);
			}
			return userFormMapList1;
		}else{
			return userFormMapList;
		}

	}

	public boolean isWarning(String userId){
		boolean result=false;
		WorkersubmitHeatTreatFormMap workersubmitHeatTreatFormMap=workersubmitHeatTreatMapper.findMaxEstimateCompleteTimeAndIdByUserId(userId);
		if(workersubmitHeatTreatFormMap==null){//未接活儿
			result=true;
		}else{
			String estimateCompleteTime=workersubmitHeatTreatFormMap.getStr("estimateCompleteTime");
			if(estimateCompleteTime==null){//未接活儿
				result=true;
			}else{
				String nowTime=ToolCommon.getNowTime();
				String hour=ToolCommon.getHourTimeDistance(nowTime,estimateCompleteTime);
				if(ToolCommon.StringToFloat(hour)<4){
					result=true;
				}else{
					result=false;
				}
			}
		}
		return result;
	}

	public void addHeatTreat(HeatTreatFormMap entity,String origin,String goodSize){
		try{
			HeatTreatFormMap heatTreatFormMap=new HeatTreatFormMap();
			String hardnessId=entity.getStr("hardnessId")+"";
			heatTreatFormMap.set("hardnessId",hardnessId);

			if(origin.equals("成品库")){
				String heattreatId=entity.get("id")+"";
				String userNames=workersubmitHeatTreatMapper.findUserNamesByHeattreatId(heattreatId);
				heatTreatFormMap.set("userNames",userNames);
			}
			UserFormMap userFormMap=getNowUserMessage();
			String userId=userFormMap.get("id")+"";
			heatTreatFormMap.set("userId",userId);
			heatTreatFormMap.set("origin",origin);
			heatTreatFormMap.set("pickTime",ToolCommon.getNowDay());
			String contractNumber=entity.getStr("contractNumber");
			heatTreatFormMap.set("contractNumber",contractNumber);
			String goodId=entity.getStr("goodId");
			heatTreatFormMap.set("goodId",goodId);
			String goodWeight=goodWeight(goodSize);
			heatTreatFormMap.set("goodWeight",goodWeight);
			heatTreatFormMap.set("goodSize",goodSize);
			String amount=entity.get("amount")+"";
			heatTreatFormMap.set("amount",amount);
			if(origin.equals("打磨")||origin.equals("中频")||origin.equals("调质")||origin.equals("渗碳")){
				heatTreatFormMap.set("distributionAmount",amount);
			}
			String clientId=entity.getStr("clientId");
			heatTreatFormMap.set("clientId",clientId);
			String weight=ToolCommon.Double2NotIn(ToolCommon.StringToFloat(amount)*ToolCommon.StringToFloat(goodWeight));
			heatTreatFormMap.set("weight",weight);
			String materialQuality=entity.getStr("materialQuality")+"";
			heatTreatFormMap.set("materialQuality",materialQuality);

			String deliveryTime=entity.getStr("deliveryTime")+"";
			heatTreatFormMap.set("deliveryTime",deliveryTime);
			heatTreatFormMap.remove("id");
			heattreatMapper.addEntity(heatTreatFormMap);
		}catch (Exception e){

		}
	}

	@ResponseBody
	@RequestMapping("makeHeartTreatEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="热处理情况",methods="车后情况-生成渗碳")//凡需要处理业务逻辑的.都需要记录操作日志
	public String makeHeartTreatEntity() throws Exception {
		String ids = getPara("ids");
		ids=ids.substring(0,ids.length()-1);
		List<HeatTreatFormMap> heatTreatFormMaps=heattreatMapper.findByIds(ids);
		for(int i=0;i<heatTreatFormMaps.size();i++){
			HeatTreatFormMap heatTreatFormMap=heatTreatFormMaps.get(i);
			String id=heatTreatFormMap.get("id")+"";
			heattreatMapper.updateIsMakeCarburizationTrueById(id);
			String goodId=heatTreatFormMap.get("goodId")+"";
			String goodSize=goodMapper.findGoodSizeById(goodId);
			addHeatTreat(heatTreatFormMap,"渗碳",goodSize);
		}
		return "success";

	}

	@ResponseBody
	@RequestMapping("makeGoodEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="热处理情况",methods="外磨内磨端面-生成成品")//凡需要处理业务逻辑的.都需要记录操作日志
	public String makeGoodEntity() throws Exception {
		String ids = getPara("ids");
		ids=ids.substring(0,ids.length()-1);
		List<HeatTreatFormMap> heatTreatFormMaps=heattreatMapper.findByIds(ids);
		for(int i=0;i<heatTreatFormMaps.size();i++){
			HeatTreatFormMap heatTreatFormMap=heatTreatFormMaps.get(i);
			String id=heatTreatFormMap.get("id")+"";
			String goodId=heatTreatFormMap.get("goodId")+"";
			String goodSize=goodMapper.findGoodSizeById(goodId);
			addHeatTreat(heatTreatFormMap,"成品库",goodSize);
			addHeatTreat(heatTreatFormMap,"打磨",goodSize);
		}
		return "success";

	}

	@ResponseBody
	@RequestMapping("makeMidfrequencyEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="热处理情况",methods="调质情况-生成中频")//凡需要处理业务逻辑的.都需要记录操作日志
	public String makeMidfrequencyEntity() throws Exception {
		String ids = getPara("ids");
		ids=ids.substring(0,ids.length()-1);
		List<HeatTreatFormMap> heatTreatFormMaps=heattreatMapper.findByIds(ids);
		for(int i=0;i<heatTreatFormMaps.size();i++){
			HeatTreatFormMap heatTreatFormMap=heatTreatFormMaps.get(i);
			String id=heatTreatFormMap.get("id")+"";
			heattreatMapper.updateIsMakeMidfrequencyTrueById(id);
			String goodId=heatTreatFormMap.get("goodId")+"";
			String goodSize=goodMapper.findGoodSizeById(goodId);
			addHeatTreat(heatTreatFormMap,"中频",goodSize);
		}
		return "success";

	}
	@RequestMapping("wagesPrintUI")
	public String wagesPrintUI(Model model,String user,
							   String startTime,String endTime) throws Exception {
		WorkersubmitHeatTreatFormMap workersubmitFormMap=new WorkersubmitHeatTreatFormMap();
		if(user==null){
			user="";
		}
		if(startTime==null){
			startTime=ToolCommon.getNowMonth()+"-01";
		}
		if(endTime==null){
			endTime=ToolCommon.getNowDay();
		}
		endTime= ToolCommon.addDay(endTime,1);
		workersubmitFormMap.set("user",user);
		workersubmitFormMap.set("startTime",startTime);
		workersubmitFormMap.set("state","已完成");
		workersubmitFormMap.set("endTime",endTime);
		WorkersubmitHeatTreatFormMap workersubmitFormMap1=workersubmitHeatTreatMapper.findAllMoneyByWorkerSubmitIdByStartTimeAndEndTimeAndContentAndUser(workersubmitFormMap);
		if(workersubmitFormMap1==null){
			workersubmitFormMap1=new WorkersubmitHeatTreatFormMap();
			workersubmitFormMap1.set("wages",0);
			workersubmitFormMap1.set("deductWages",0);
			workersubmitFormMap1.set("trueWages",0);
		}
		WorkersubmitHeatTreatFormMap workersubmitFormMapOverTime=workersubmitHeatTreatMapper.findSumOverTimeByWorkerSubmitIdByStartTimeAndEndTimeAndContentAndUser(workersubmitFormMap);
		String trueWages=ToolCommon.FloatToMoney(ToolCommon.StringToFloat(workersubmitFormMap1.get("trueWages")+"")-ToolCommon.StringToFloat(workersubmitFormMapOverTime.get("overTimeMoney")+""))+"";
		workersubmitFormMap1.set("trueWages",trueWages);
		model.addAttribute("money",workersubmitFormMap1);
		model.addAttribute("overtime",workersubmitFormMapOverTime);
		model.addAttribute("user",user);
		endTime= ToolCommon.addDay(endTime,-1);
		model.addAttribute("time",startTime+"至"+endTime);
		return Common.BACKGROUND_PATH + "/system/heattreat/wagesOrderPrintShow";
	}
	@ResponseBody
	@RequestMapping("deleteEntityGetList")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="热处理情况",methods="已接收-删除组")//凡需要处理业务逻辑的.都需要记录操作日志
	public String deleteEntityGetList() throws Exception {
		SystemconfigFormMap systemconfigFormMap=new SystemconfigFormMap();
		systemconfigFormMap.set("name","deletepassword");
		String content=getPara("password");
		systemconfigFormMap.set("content",content);
		List<SystemconfigFormMap> systemconfigFormMaps=systemconfigMapper.findByNames(systemconfigFormMap);
		if(systemconfigFormMaps.size()>0){
			String ids = getPara("ids");
			ids=ids.substring(0,ids.length()-1);
			String[] idsStr=ids.split(",");
			for (String id : idsStr) {
				workersubmitHeatTreatMapper.deleteById(id);
			}
			return "success";
		}else{
			return "删除订单密码错误";
		}
	}

	@RequestMapping(value = "exportGetlist")
	@ResponseBody
	@SystemLog(module="生产管理",methods="已接收列表-导出表格")//凡需要处理业务逻辑的.都需要记录操作日志
	public void exportGetlist(HttpServletResponse response,String entity) throws Exception {
		String user=ToolCommon.json2Object(entity).getString("user");
		String contentS=ToolCommon.json2Object(entity).getString("content");
		String startTime=ToolCommon.json2Object(entity).getString("startTime");
		String endTime=ToolCommon.json2Object(entity).getString("endTime");
		WorkersubmitHeatTreatFormMap workersubmitHeatTreatFormMap=new WorkersubmitHeatTreatFormMap();
		if(contentS==null){
			contentS="";
		}
		if(user==null){
			user="";
		}
		if(startTime==null){
			startTime="";
		}
		if(endTime==null){
			endTime="";
		}else{
			endTime=ToolCommon.addDay(endTime,1);
		}
		workersubmitHeatTreatFormMap.set("content",contentS);
		workersubmitHeatTreatFormMap.set("state","已接收未完成");
		workersubmitHeatTreatFormMap.set("user",user);
		workersubmitHeatTreatFormMap.set("startTime",startTime);
		workersubmitHeatTreatFormMap.set("endTime",endTime);
		List<WorkersubmitHeatTreatFormMap> workersubmitFormMaps=workersubmitHeatTreatMapper.findByWorkerSubmitIdByStartTimeAndEndTimeAndContentAndUser(workersubmitHeatTreatFormMap);

		//excel标题
		String[] title = {"订单编号",
				"图号",
				"产品名称",
				"产品尺寸",
				"材质",
				"交货日期",
				"工序",
				"接收人",
				"接收数量",
				"接收时间"};

		//excel文件名
		String fileName = "已接收列表" + System.currentTimeMillis() + ".xls";

		//sheet名
		String sheetName = "sheet1";

		String [][] content = new String[workersubmitFormMaps.size()][title.length];

		for (int i = 0; i < workersubmitFormMaps.size(); i++) {
			content[i] = new String[title.length];
			WorkersubmitHeatTreatFormMap obj = workersubmitFormMaps.get(i);
			content[i][0] = obj.get("contractNumber")+"";
			content[i][1] = obj.get("goodCode")+"";
			content[i][2] = obj.get("goodName")+"";
			content[i][3] = obj.get("goodSize")+"";
			content[i][4] = obj.get("materialQualityName")+"";
			content[i][5] = obj.get("deliveryTime")+"";
			content[i][6] = obj.get("processName")+"";
			content[i][7] = obj.get("submiterShow")+"";
			content[i][8] = obj.get("amount")+"";
			content[i][9] = obj.get("startTime")+"";

		}

		//创建HSSFWorkbook
		HSSFWorkbook wb = ExcelUtils.getHSSFWorkbook("已接收列表",sheetName, title, content, null);

		//响应到客户端
		try {
			ExcelUtils.setResponseHeader(response, fileName);
			OutputStream os = response.getOutputStream();
			wb.write(os);
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@ResponseBody
	@RequestMapping("editWagesEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="生产管理",methods="已完成列表-修改保存")//凡需要处理业务逻辑的.都需要记录操作日志
	public String editWagesEntity(String entity) throws Exception {
		List<WorkersubmitHeatTreatFormMap> list = (List) ToolCommon.json2ObjectList(entity, WorkersubmitHeatTreatFormMap.class);
		for(int ii=0;ii<list.size();ii++){
			WorkersubmitHeatTreatFormMap workersubmitFormMap=list.get(ii);
//			String id=workersubmitFormMap.get("id")+"";
//			String deductWages=workersubmitFormMap.get("deductWages")+"";
//			String trueWages=workersubmitFormMap.get("trueWages")+"";
//			String wages=workersubmitFormMap.get("wages")+"";
//			String deductRate=workersubmitFormMap.get("deductRate")+"";
//			WorkersubmitHeatTreatFormMap workersubmitFormMap1=workersubmitHeatTreatMapper.findById(id);
//			workersubmitFormMap1.set("deductWages",deductWages);
//			workersubmitFormMap1.set("trueWages",trueWages);
//			workersubmitFormMap1.set("wages",wages);
//			workersubmitFormMap1.set("deductRate",deductRate);
			workersubmitHeatTreatMapper.editEntity(workersubmitFormMap);
		}
		return "success";
	}

	@ResponseBody
	@RequestMapping("editOverTimeLimitEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="生产管理",methods="已接收列表-修改保存")//凡需要处理业务逻辑的.都需要记录操作日志
	public String editOverTimeLimitEntity(String entity) throws Exception {
		List<WorkersubmitHeatTreatFormMap> list = (List) ToolCommon.json2ObjectList(entity, WorkersubmitHeatTreatFormMap.class);
		for(int ii=0;ii<list.size();ii++){
			WorkersubmitHeatTreatFormMap workersubmitFormMap=list.get(ii);
			String id=workersubmitFormMap.get("id")+"";
			String overTimeLimit=workersubmitFormMap.get("overTimeLimit")+"";
			WorkersubmitHeatTreatFormMap workersubmitFormMap1=workersubmitHeatTreatMapper.findById(id);
			workersubmitFormMap1.set("overTimeLimit",overTimeLimit);
			workersubmitHeatTreatMapper.editEntity(workersubmitFormMap1);
		}
		return "success";
	}

	@RequestMapping(value = "exportCompletelist")
	@ResponseBody
	@SystemLog(module="生产管理",methods="已完成列表-导出表格")//凡需要处理业务逻辑的.都需要记录操作日志
	public void exportCompletelist(HttpServletResponse response,String entity) throws Exception {
		String user=ToolCommon.json2Object(entity).getString("user");
		String contentS=ToolCommon.json2Object(entity).getString("content");
		String startTime=ToolCommon.json2Object(entity).getString("startTime");
		String endTime=ToolCommon.json2Object(entity).getString("endTime");
		String startTimeGET=ToolCommon.json2Object(entity).getString("startTimeGET");
		String endTimeGET=ToolCommon.json2Object(entity).getString("endTimeGET");
		WorkersubmitHeatTreatFormMap workersubmitHeatTreatFormMap=new WorkersubmitHeatTreatFormMap();
		if(contentS==null){
			contentS="";
		}
		if(user==null){
			user="";
		}
		if(startTime==null){
			startTime="";
		}
		if(endTime==null){
			endTime="";
		}else{
			endTime=ToolCommon.addDay(endTime,1);
		}
		workersubmitHeatTreatFormMap.set("content",contentS);
		workersubmitHeatTreatFormMap.set("state","已完成");
		workersubmitHeatTreatFormMap.set("user",user);
		workersubmitHeatTreatFormMap.set("startTime",startTime);
		workersubmitHeatTreatFormMap.set("endTime",endTime);
		workersubmitHeatTreatFormMap.set("startTimeGET",startTimeGET);
		if(!ToolCommon.isNull(endTimeGET)){
			endTimeGET=ToolCommon.addDay(endTimeGET,1);
		}
		workersubmitHeatTreatFormMap.set("endTimeGET",endTimeGET);
		List<WorkersubmitHeatTreatFormMap> workersubmitFormMaps=workersubmitHeatTreatMapper.findByWorkerSubmitIdByStartTimeAndEndTimeAndContentAndUser(workersubmitHeatTreatFormMap);

		//excel标题
		String[] title = {"订单编号",
				"图号",
				"产品名称",
				"产品尺寸",
				"成品尺寸",
				"材质",
				"交货日期",
				"工序",
				"完成人",
				"完成数量",
				"废品数量",
				"完成时间",
				"应得工资",
				"应扣工资",
				"实得工资",
				"接收时间"};

		//excel文件名
		String fileName = "已完成列表" + System.currentTimeMillis() + ".xls";

		//sheet名
		String sheetName = "sheet1";

		String [][] content = new String[workersubmitFormMaps.size()][title.length];

		for (int i = 0; i < workersubmitFormMaps.size(); i++) {
			content[i] = new String[title.length];
			WorkersubmitHeatTreatFormMap obj = workersubmitFormMaps.get(i);
			content[i][0] = obj.get("contractNumber")+"";
			content[i][1] = obj.get("goodCode")+"";
			content[i][2] = obj.get("goodName")+"";
			content[i][3] = obj.get("goodSize")+"";
			content[i][4] = obj.get("goodSizeGoodTable")+"";
			content[i][5] = obj.get("materialQualityName")+"";
			content[i][6] = obj.get("deliveryTime")+"";
			content[i][7] = obj.get("processName")+"";
			content[i][8] = obj.get("submiterShow")+"";
			content[i][9] = obj.get("completeAmount")+"";
			content[i][10] = obj.get("badAmount")+"";
			content[i][11] = obj.get("completeTime")+"";
			content[i][12] = obj.get("wages")+"";
			content[i][13] = obj.get("deductWages")+"";
			content[i][14] = obj.get("trueWages")+"";
			content[i][15] = obj.get("startTime")+"";

		}

		//创建HSSFWorkbook
		HSSFWorkbook wb = ExcelUtils.getHSSFWorkbook("已完成列表",sheetName, title, content, null);

		//响应到客户端
		try {
			ExcelUtils.setResponseHeader(response, fileName);
			OutputStream os = response.getOutputStream();
			wb.write(os);
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@ResponseBody
	@RequestMapping("findByPage")
	public HeatTreatFormMap findByPage(String content) throws Exception {
		String page=getPara("page");
		String rows=getPara("rows");
		String origin=getPara("origin");
		String remarks=getPara("remarks");
		String userId=getPara("userId");
		String isMakeCarburization=getPara("isMakeCarburization");
		String pickTimeStart=getPara("pickTimeStart");
		String pickTimeEnd=getPara("pickTimeEnd");
		String backTimeStart=getPara("backTimeStart");
		String backTimeEnd=getPara("backTimeEnd");

		String starttime=getPara("startTime");
		String endtime=getPara("endTime");
		String goodName=getPara("goodName");
		if(!ToolCommon.isNull(goodName)&&goodName.equals("不限")){
			goodName="";
		}
		String clientId=getPara("clientId");
		String state=getPara("state");
		String getstate=getPara("getstate");
		String sendstate=getPara("sendstate");
		String isDistribution=getPara("isDistribution");
		String materialQuality=getPara("materialQuality");
		String oprateUserId=getPara("oprateUserId");
		String oprateProcessId=getPara("oprateProcessId");
		HeatTreatFormMap heattreatFormMap=new HeatTreatFormMap();
		heattreatFormMap.set("isDistribution",isDistribution);
		heattreatFormMap.set("origin",origin);
		heattreatFormMap.set("userId",userId);
		heattreatFormMap.set("isMakeCarburization",isMakeCarburization);
		heattreatFormMap.set("content",content);

		heattreatFormMap.set("remarks",remarks);
		heattreatFormMap.set("pickTimeStart",pickTimeStart);
		heattreatFormMap.set("pickTimeEnd",pickTimeEnd);
		heattreatFormMap.set("backTimeStart",backTimeStart);
		heattreatFormMap.set("backTimeEnd",backTimeEnd);

		heattreatFormMap.set("startTime",starttime);
		heattreatFormMap.set("endTime",endtime);

		heattreatFormMap.set("goodName",goodName);
		heattreatFormMap.set("clientId",clientId);
		heattreatFormMap.set("state",state);
		heattreatFormMap.set("sendstate",sendstate);
		heattreatFormMap.set("getstate",getstate);
		heattreatFormMap.set("materialQuality",materialQuality);
		heattreatFormMap.set("oprateUserId",oprateUserId);
		heattreatFormMap.set("oprateProcessId",oprateProcessId);
		HeatTreatFormMap sum=heattreatMapper.findSumWeightAndAmountByAllLike(heattreatFormMap);
		if(sum==null){
			sum=new HeatTreatFormMap();
			sum.set("sumAmount",0);
			sum.set("sumWeight",0);
			sum.set("sumBackAmount",0);
			sum.set("sumUnBackAmount",0);
			sum.set("unDistributionAmount",0);
			sum.set("alDistributionAmount",0);
		}
		if(page.equals("1")){
			total=heattreatMapper.findCountByAllLike(heattreatFormMap);
		}
		heattreatFormMap=toFormMap(heattreatFormMap, page, rows,heattreatFormMap.getStr("orderby"));
		String sort=getPara("sort");
		String order=getPara("order");
		if(ToolCommon.isNull(sort)){
			sort="pickTime";
			order="asc";
		}
		heattreatFormMap.set("sort",sort);
		heattreatFormMap.set("order",order);

		List<HeatTreatFormMap> heattreatFormMaps=heattreatMapper.findByAllLike(heattreatFormMap);
		List<HeatTreatFormMap> heattreatFormMaps1=new ArrayList<>();
		for(int i=0;i<heattreatFormMaps.size();i++){
			HeatTreatFormMap heatTreatFormMap=heattreatFormMaps.get(i);
			String heattreatId=heatTreatFormMap.get("id")+"";
			List<WorkersubmitHeatTreatFormMap> workersubmitHeatTreatFormMaps=workersubmitHeatTreatMapper.findProgressByHeattreatId(heattreatId);
			result="";
			for(int j=0;j<workersubmitHeatTreatFormMaps.size();j++){
				WorkersubmitHeatTreatFormMap workersubmitHeatTreatFormMap=workersubmitHeatTreatFormMaps.get(j);
				String completeTime=workersubmitHeatTreatFormMap.getStr("completeTime");
				String startTime=workersubmitHeatTreatFormMap.getStr("startTime");
				String userName=workersubmitHeatTreatFormMap.getStr("userName");
				String processName=workersubmitHeatTreatFormMap.getStr("processName");
				String amount=workersubmitHeatTreatFormMap.getStr("amount");
				String completeAmount=workersubmitHeatTreatFormMap.getStr("completeAmount");
				if(ToolCommon.isNull(completeTime)){
					result=result+"<span style='color:#ff0030;font-weight:bold'>"+processName+"("+userName+startTime+"已接收"+amount+")</span>→";
				}else if(!ToolCommon.isNull(completeTime)){
					result=result+"<span style='color:#048133;font-weight:bold'>"+processName+"("+userName+completeTime+"已完成"+completeAmount+")</span>→";
				}
			}
			if(result.equals("")){

			}else{
				result= result.substring(0, result.length() - 1);
			}

			heatTreatFormMap.set("progress",result);
			heattreatFormMaps1.add(heatTreatFormMap);
		}
		heattreatFormMap.set("rows",heattreatFormMaps1);
		heattreatFormMap.set("sum",sum);
		heattreatFormMap.set("total",total);
		return heattreatFormMap;
	}

	@ResponseBody
	@RequestMapping("findExecutionByPage")
	@SystemLog(module="热处理情况",methods="接收完成情况列表")
	public HeatTreatFormMap findExecutionByPage(String content) throws Exception {
		String page=getPara("page");
		String rows=getPara("rows");
		String starttime=getPara("startTime");
		String endtime=getPara("endTime");
		String startTimeGet=getPara("startTimeGet");
		String endTimeGet=getPara("endTimeGet");
		String startTimeComplete=getPara("startTimeComplete");
		String endTimeComplete=getPara("endTimeComplete");
		String oprateUserId=getPara("oprateUserId");
		String goodName=getPara("goodName");
		if(!ToolCommon.isNull(goodName)&&goodName.equals("不限")){
			goodName="";
		}
		String clientId=getPara("clientId");
		String state=getPara("state");
		String materialQuality=getPara("materialQuality");
		HeatTreatFormMap heattreatFormMap=new HeatTreatFormMap();
		heattreatFormMap.set("oprateUserId",oprateUserId);
		heattreatFormMap.set("content",content);
		heattreatFormMap.set("startTime",starttime);
		if(!ToolCommon.isNull(endtime)){
			endtime=ToolCommon.addDay(endtime,1);
		}
		heattreatFormMap.set("endTime",endtime);
		heattreatFormMap.set("startTimeGet",startTimeGet);
		if(!ToolCommon.isNull(endTimeGet)){
			endTimeGet=ToolCommon.addDay(endTimeGet,1);
		}
		heattreatFormMap.set("endTimeGet",endTimeGet);
		heattreatFormMap.set("startTimeComplete",startTimeComplete);
		if(!ToolCommon.isNull(endTimeComplete)){
			endTimeComplete=ToolCommon.addDay(endTimeComplete,1);
		}
		heattreatFormMap.set("endTimeComplete",endTimeComplete);

		heattreatFormMap.set("goodName",goodName);
		heattreatFormMap.set("clientId",clientId);
		heattreatFormMap.set("state",state);
		heattreatFormMap.set("materialQuality",materialQuality);
		HeatTreatFormMap sum=heattreatMapper.findSumExecutionByAllLike(heattreatFormMap);
		if(sum==null){
			sum=new HeatTreatFormMap();
			sum.set("getAmount",0);
			sum.set("completeAmount",0);
			sum.set("badAmount",0);
			sum.set("unComplete",0);
		}
		if(page.equals("1")){
			total=heattreatMapper.findExecutionCountByAllLike(heattreatFormMap);
		}
		heattreatFormMap=toFormMap(heattreatFormMap, page, rows,heattreatFormMap.getStr("orderby"));
		String sort=getPara("sort");
		String order=getPara("order");

		heattreatFormMap.set("sort",sort);
		heattreatFormMap.set("order",order);
		List<HeatTreatFormMap> heattreatFormMaps=heattreatMapper.findExecutionByAllLike(heattreatFormMap);
		heattreatFormMap.set("rows",heattreatFormMaps);
		heattreatFormMap.set("sum",sum);
		heattreatFormMap.set("total",total);
		return heattreatFormMap;
	}

	@SystemLog(module="热处理情况",methods="废品信息")
	@RequestMapping("badgoodStatistics")
	public String badgoodStatistics(Model model) throws Exception {
		model.addAttribute("res", findByRes());
		return Common.BACKGROUND_PATH + "/system/heattreat/badgoodStatistics";
	}

	@ResponseBody
	@SystemLog(module="热处理情况",methods="废品信息-获取列表内容")
	@RequestMapping("findBadgoodStatisticsByPage")
	public WorkersubmitHeatTreatFormMap findBadgoodStatisticsByPage(String content,String state,String starttime,String endtime,String userId) throws Exception {
		String page=getPara("page");
		String rows=getPara("rows");
		if(state==null||state.equals("")||state.equals("不限")){
			state="";
		}
		if(content==null){
			content="";
		}
		if(starttime==null){
			starttime="";
		}
		if(endtime==null){
			endtime="";
		}else{
			endtime=ToolCommon.addDay(endtime,1);
		}

		WorkersubmitHeatTreatFormMap blankFormMap = getFormMap(WorkersubmitHeatTreatFormMap.class);
		blankFormMap.set("content",content);
		blankFormMap.set("userId",userId);
		blankFormMap.set("startTime",starttime);
		blankFormMap.set("endTime",endtime);
		blankFormMap.set("state",state);
		if(page.equals("1")){
			total=workersubmitHeatTreatMapper.findBadMessageGroupCountByOrderDetailsId(blankFormMap);
		}
		int sumAmount=workersubmitHeatTreatMapper.findBadSumAcountByOrderDetailsId(blankFormMap);
		blankFormMap=toFormMap(blankFormMap, page, rows,blankFormMap.getStr("orderby"));
		List<WorkersubmitHeatTreatFormMap> blankFormMaps=workersubmitHeatTreatMapper.findBadMessageGroupByOrderDetailsId(blankFormMap);
		blankFormMap.set("rows",blankFormMaps);
		blankFormMap.set("total",total);
		blankFormMap.set("sumAmount",sumAmount);
		return blankFormMap;
	}

	@RequestMapping("badprintorderShowUI")
	public String badprintorderShowUI(Model model,String origin,String workersubmitHeattreatId) throws Exception {
		model.addAttribute("workersubmitHeattreatId",workersubmitHeattreatId);
		if(origin.equals("废品通知单")){
			BadnoticeFormMap entity=badnoticeMapper.findByWorkersubmitHeattreatId(workersubmitHeattreatId);
			model.addAttribute("entity",entity);
			return Common.BACKGROUND_PATH + "/system/heattreat/badnoticeOrderPrintInput";
		}else if(origin.equals("产品不合格单")){
			String goodId=getPara("goodId");
			String badAmount=getPara("badAmount");
			float allWeight=getBadWeightByGoodIdAndBadAmount(goodId,badAmount);
			model.addAttribute("allWeight",allWeight);
			UnqualifiedFormMap entity=unqualifiedMapper.findByWorkersubmitHeattreatId(workersubmitHeattreatId);
			model.addAttribute("entity",entity);
			return Common.BACKGROUND_PATH + "/system/heattreat/unqualifiedOrderPrintInput";
		}else if(origin.equals("外协废品单")){
			ScrapFormMap entity=scrapMapper.getByWorkersubmitHeattreatId(workersubmitHeattreatId);
			model.addAttribute("entity",entity);
			return Common.BACKGROUND_PATH + "/system/heattreat/scrapOrderPrintInput";
		}else{
			String heattreatId=getPara("heattreatId");
			model.addAttribute("heattreatId",heattreatId);
			if(ToolCommon.isContain(origin,"渗碳")){
				return Common.BACKGROUND_PATH + "/system/heattreat/badnoticeOrderPrintInputNoIdInputEmployee";
			}else{
				return Common.BACKGROUND_PATH + "/system/heattreat/badnoticeOrderPrintInputNoId";
			}

		}
	}

	public float getBadWeightByGoodIdAndBadAmount(String goodId,String badAmount){
		String goodWeight=goodMapper.findGoodWeightById(goodId);
		float allWeight=ToolCommon.FloatTo4Float(ToolCommon.StringToFloat(badAmount)*ToolCommon.StringToFloat(goodWeight));
		return allWeight;
	}

	@RequestMapping("unqualifiedOrderUI")
	public String unqualifiedOrderUI(Model model,String workersubmitHeattreatId) throws Exception {
		UnqualifiedFormMap unqualifiedFormMap=workersubmitHeatTreatMapper.findUnqualifiedShowById(workersubmitHeattreatId);
		UserFormMap userFormMap=getNowUserMessage();
		model.addAttribute("unqualifiedFormMap",unqualifiedFormMap);
		model.addAttribute("userFormMap",userFormMap);
		model.addAttribute("nowDay",ToolCommon.getNowDay());
		return Common.BACKGROUND_PATH + "/system/heattreat/unqualifiedOrderPrintShow";
	}

	@RequestMapping("badnoticeOrderUI")
	public String badnoticeOrderUI(Model model,String workersubmitHeattreatId) throws Exception {
		BadnoticeFormMap badnoticeFormMap=workersubmitHeatTreatMapper.findBadNoticeShowById(workersubmitHeattreatId);
		UserFormMap userFormMap=getNowUserMessage();
		model.addAttribute("badnoticeFormMap",badnoticeFormMap);
		model.addAttribute("userFormMap",userFormMap);
		model.addAttribute("nowDay",ToolCommon.getNowDay());
		return Common.BACKGROUND_PATH + "/system/heattreat/badnoticeOrderPrintShow";
	}
	@RequestMapping("badnoticeOrderNoIdUI")
	public String badnoticeOrderNoIdUI(Model model) throws Exception {
		String heattreatId=getPara("heattreatId");
		HeatTreatFormMap heatTreatFormMap=heattreatMapper.findBadOrderPrintContentById(heattreatId);
		String taxPrice="7.8";
		String amount=getPara("amount");
		String allWeight=getPara("allWeight");
		String ratio="0.9";
		float money=ToolCommon.StringToFloat(ratio)*ToolCommon.StringToFloat(allWeight)*ToolCommon.StringToFloat(taxPrice);
		String reason=getPara("reason");
		BadnoticeFormMap badnoticeFormMap=new BadnoticeFormMap();
		badnoticeFormMap.set("amount",amount);
		badnoticeFormMap.set("money",ToolCommon.FloatToMoney(money));
		badnoticeFormMap.set("reason",reason);
		HeatTreatFormMap heatTreatFormMapEntity=heattreatMapper.findById(heattreatId);
		String employees=getEmployeeByContractNumberAndGoodId(heatTreatFormMapEntity.get("contractNumber")+"",
				heatTreatFormMapEntity.get("goodId")+"",
				heatTreatFormMapEntity.get("deliveryTime")+"");
		if(!ToolCommon.isNull(employees)){
			int employeesCount=employees.split(",").length;
			float employeeMoney=ToolCommon.FloatToMoney(money/employeesCount);
			employees=employees+"(每人扣款"+employeeMoney+")";
		}
		badnoticeFormMap.set("employee",employees);
		UserFormMap userFormMap=getNowUserMessage();
		model.addAttribute("userFormMap",userFormMap);
		model.addAttribute("nowDay",ToolCommon.getNowDay());
		model.addAttribute("heatTreatFormMap",heatTreatFormMap);
		model.addAttribute("badnoticeFormMap",badnoticeFormMap);
		return Common.BACKGROUND_PATH + "/system/heattreat/badnoticeOrderPrintNoIdShow";
	}

	@RequestMapping("badnoticeOrderNoIdInputEmployeeUI")
	public String badnoticeOrderNoIdInputEmployeeUI(Model model) throws Exception {
		String heattreatId=getPara("heattreatId");
		HeatTreatFormMap heatTreatFormMap=heattreatMapper.findBadOrderPrintContentById(heattreatId);
		String employee=getPara("employee");
		String amount=getPara("amount");
		String allWeight=getPara("allWeight");
		String price="7.8";
		String ratio="0.9";
		float money= ToolCommon.FloatToMoney(ToolCommon.StringToFloat(allWeight)*ToolCommon.StringToFloat(price)*ToolCommon.StringToFloat(ratio));
		String reason=getPara("reason");
		String directorOpinion=getPara("directorOpinion");
		String bossOpinion=getPara("bossOpinion");
		BadnoticeFormMap badnoticeFormMap=new BadnoticeFormMap();
		badnoticeFormMap.set("employee",employee);
		badnoticeFormMap.set("money",money);
		badnoticeFormMap.set("amount",amount);
		badnoticeFormMap.set("allWeight",allWeight);
		badnoticeFormMap.set("reason",reason);
		badnoticeFormMap.set("directorOpinion",directorOpinion);
		badnoticeFormMap.set("bossOpinion",bossOpinion);
		model.addAttribute("nowDay",ToolCommon.getNowDay());
		model.addAttribute("heatTreatFormMap",heatTreatFormMap);
		model.addAttribute("badnoticeFormMap",badnoticeFormMap);
		return Common.BACKGROUND_PATH + "/system/heattreat/badnoticeOrderPrintNoIdInputEmployeeShow";
	}
	public String  getEmployeeByContractNumberAndGoodId(String contractNumber,String goodId,String deliveryTime) throws Exception {
		String employees="";
		String orderdetailsId="";
		try{
			orderdetailsId=orderDetailsMapper.findIdByContractNumberAndGoodIdAndDeliveryTime(contractNumber,goodId,deliveryTime);
		}catch (Exception e){
			orderdetailsId="";
		}
		if(ToolCommon.isNull(orderdetailsId)){
			return employees;
		}else{
			String blankId=blankMapper.findIdByOrderDetailsId(orderdetailsId);
			if(ToolCommon.isNull(blankId)){
				return employees;
			}else{
				BlankFormMap blankFormMap=new BlankFormMap();
				blankFormMap.set("blankId",blankId);
				blankFormMap.set("goodId",goodId);
				blankFormMap.set("blankId",blankId);
				blankFormMap.set("deliveryTime",deliveryTime);
				blankFormMap.set("contractNumber",contractNumber);
				return getEmployeesContainProgress(blankFormMap);
			}

		}
	}

	public String getEmployeesContainProgress(BlankFormMap departmentFormMapList){
		String employeeS="";
		BlankFormMap blankFormMap1=departmentFormMapList;
		String blankId=blankFormMap1.get("blankId")+"";
		String goodIdBlank=blankFormMap1.get("goodId")+"";
		String deliveryTimeBlank=blankFormMap1.get("deliveryTime")+"";
		String contractNumberBlank=blankFormMap1.get("contractNumber")+"";
		List<BlankFormMap> blankFormMaps1=blankMapper.findProgressByBlankId(blankId);
		for(int j=0;j<blankFormMaps1.size();j++){
			BlankFormMap goodProcessFormMap=blankFormMaps1.get(j);
			String blankProcessId=goodProcessFormMap.get("blankprocessId")+"";
			String processName=goodProcessFormMap.get("processName")+"";
			String processIdBlank=goodProcessFormMap.get("processId")+"";
			String planneedDay=goodProcessFormMap.get("planneedDay")+"";
			int count=workersubmitHeatTreatMapper.findCountByContractNumberAndDeliveryTimeAndGoodIdAndProcessId(
					contractNumberBlank,deliveryTimeBlank,goodIdBlank,processIdBlank
			);
			if(count>0){
				List<WorkersubmitHeatTreatFormMap> workersubmitHeatTreatFormMaps=workersubmitHeatTreatMapper.findUserNameByContractNumberAndDeliveryTimeAndGoodIdAndProcessId(
						contractNumberBlank,deliveryTimeBlank,goodIdBlank,processIdBlank
				);
				for(int jj=0;jj<workersubmitHeatTreatFormMaps.size();jj++){
					WorkersubmitHeatTreatFormMap workersubmitHeatTreatFormMap=workersubmitHeatTreatFormMaps.get(jj);
					String userName=workersubmitHeatTreatFormMap.get("userName")+"";
					if (!ToolCommon.isContain(employeeS, userName)) {
						employeeS=employeeS+userName+",";
					}

				}
			}else{
				employeeS=getProgress(employeeS,processName,blankProcessId,planneedDay);
			}
		}
		return employeeS;
	}

	public String getProgress(String employeeS,String processName,String blankProcessId,String planneedDay){
		int blankCount=blankMapper.findCountByBlankProcessId(blankProcessId);
		if(blankCount==0){
		}else{
			List<BlankFormMap> blankFormMaps2=blankMapper.findUserNameByBlankProcessId(blankProcessId);
			for(int ii=0;ii<blankFormMaps2.size();ii++){
				BlankFormMap blankFormMap2=blankFormMaps2.get(ii);
				String userName1=blankFormMap2.get("userName")+"";
				employeeS=employeeS+userName1+",";
			}
		}
		return employeeS;
	}
	@RequestMapping("scrapOrderPrintShowUI")
	public String scrapOrderPrintShowUI(Model model,String workersubmitHeattreatId) throws Exception {
		ScrapFormMap scrapFormMap=workersubmitHeatTreatMapper.findScrapShowById(workersubmitHeattreatId);
		model.addAttribute("scrapFormMap",scrapFormMap);
		UserFormMap userFormMap=getNowUserMessage();
		model.addAttribute("userFormMap",userFormMap);
		model.addAttribute("nowDay",ToolCommon.getNowDay());
		return Common.BACKGROUND_PATH + "/system/heattreat/scrapOrderPrintShow";
	}

	/**
	 * 保存废品单信息
	 *
	 * @throws Exception
	 */
	@RequestMapping("badprintorderEditEntity")
	@ResponseBody
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="废品统计",methods="保存废品单信息")//凡需要处理业务逻辑的.都需要记录操作日志
	public String badprintorderEditEntity(String origin) throws Exception {
		UserFormMap userFormMap=getNowUserMessage();
		String userId=userFormMap.get("id")+"";
		if(origin.equals("产品不合格单")){
			UnqualifiedFormMap unqualifiedFormMap=getFormMap(UnqualifiedFormMap.class);
			String id=unqualifiedFormMap.get("id")+"";
			String allWeight=unqualifiedFormMap.get("allWeight")+"";
			String price="7.8";
			String ratio="0.9";
			float money= ToolCommon.FloatToMoney(ToolCommon.StringToFloat(allWeight)*ToolCommon.StringToFloat(price)*ToolCommon.StringToFloat(ratio));
			unqualifiedFormMap.set("money",money);
			unqualifiedFormMap.set("userId",userId);
			if(ToolCommon.isNull(id)){
				unqualifiedFormMap.remove("id");
				unqualifiedMapper.addEntity(unqualifiedFormMap);
			}else{
				unqualifiedMapper.editEntity(unqualifiedFormMap);
			}
		}else if(origin.equals("外协废品单")){
			ScrapFormMap scrapFormMap=getFormMap(ScrapFormMap.class);
			String id=scrapFormMap.get("id")+"";
			scrapFormMap.set("userId",userId);
			if(ToolCommon.isNull(id)){
				scrapFormMap.remove("id");
				scrapMapper.addEntity(scrapFormMap);
			}else{
				scrapMapper.editEntity(scrapFormMap);
			}
		}else if(origin.equals("废品通知单")){
			BadnoticeFormMap badnoticeFormMap=getFormMap(BadnoticeFormMap.class);
			String id=badnoticeFormMap.get("id")+"";
			badnoticeFormMap.set("userId",userId);
			if(ToolCommon.isNull(id)){
				badnoticeFormMap.remove("id");
				badnoticeMapper.addEntity(badnoticeFormMap);
			}else{
				badnoticeMapper.editEntity(badnoticeFormMap);
			}
		}
		result="success";
		return result;
	}

	@ResponseBody
	@RequestMapping("findProgressByHeattreatId")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="热处理情况",methods="根据heattreatId获取进度")//凡需要处理业务逻辑的.都需要记录操作日志
	public List<WorkersubmitHeatTreatFormMap> findProgressByHeattreatId(String heattreatId) throws Exception {
		List<WorkersubmitHeatTreatFormMap> workersubmitHeatTreatFormMaps=workersubmitHeatTreatMapper.findProgressByHeattreatId(heattreatId);
		return workersubmitHeatTreatFormMaps;
	}

	@RequestMapping("distributionUI")
	@SystemLog(module="热处理情况",methods="分配界面")
	public String distributionUI(Model model) {
		String ids=getPara("ids");
		String remarks=getPara("remarks");
		String origin=getPara("origin");
		String source=getPara("source");
		String processUrl="";
		if(origin.equals("渗碳")&&!remarks.equals("外圆粗磨")&&!remarks.equals("外圆磨（轴）")&&!remarks.equals("平磨（垫片）")){
			processUrl="/json/carburizationProcess.json";
		}
		if(origin.equals("渗碳")&&remarks.equals("外圆粗磨")){
			processUrl="/json/cylindricalroughgrindingProcess.json";
		}
		if(origin.equals("渗碳")&&remarks.equals("外圆磨（轴）")){
			processUrl="/json/cylindricalGrindingShaftProcess.json";
		}
		if(origin.equals("渗碳")&&remarks.equals("平磨（垫片）")){
			processUrl="/json/flatgrindingGgasketProcess.json";
		}
		if(origin.equals("外磨内磨端面")){
			processUrl="/json/externalgrindingProcess.json";
		}
		if(origin.equals("中频")){
			processUrl="/json/midfrequencyProcess.json";
		}
		if(origin.equals("调质")){
			UserFormMap userFormMap=getNowUserMessage();
			String roleName=userFormMap.get("roleName")+"";
			if(roleName.equals("分管理员")){
				processUrl="/json/conditioningProcessAdmin.json";
			}else{
				processUrl="/json/conditioningProcess.json";
			}

		}
		if(origin.equals("正火")){
			processUrl="/json/normalizingProcess.json";
		}
		if(origin.equals("正火调质")){
			processUrl="/json/normalizingtemperingProcess.json";
		}
        if(origin.equals("车后")){
            if(remarks.equals("销轴")){
                processUrl="/json/carbehindpinshaftProcess.json";
            }else if(remarks.equals("钢套")){
                processUrl="/json/carbehindsteelsleeveProcess.json";
            }else if(remarks.equals("垫")){
                processUrl="/json/carbehindpadProcess.json";
            }
        }
		if(origin.equals("进度查询")){
			processUrl="/json/progressSearchProcess.json";
			ids=heattreatMapper.findIdsByProgressSearchIds(ids);

		}

		if(origin.equals("火前后")){
			processUrl="/json/fireProcess.json";
		}
		if(origin.equals("铣槽后")){
			processUrl="/json/millingGrooveProcess.json";
		}
		if(origin.equals("钳后情况")){
			processUrl="/json/preliminaryCarburizingProcess.json";
		}
		if(origin.equals("端面平磨")){
			processUrl="/json/endfaceflatgrindingProcess.json";
		}
		if(origin.equals("消差磨")){
			processUrl="/json/erroreliminationmillProcess.json";
		}
		if(origin.equals("统一尺寸")){
			processUrl="/json/uniformsizeProcess.json";
		}
		if(origin.equals("内孔磨")){
			processUrl="/json/boremillProcess.json";
		}
		if(origin.equals("外圆精磨")){
			processUrl="/json/cylindricalfinishgrindingProcess.json";
		}
		if(origin.equals("打磨")){
			processUrl="/json/polishProcess.json";
		}
		model.addAttribute("heattreatIds",ids);
		model.addAttribute("processUrl",processUrl);
		model.addAttribute("origin",origin);
		model.addAttribute("remarks",remarks);
		model.addAttribute("source",source);
		return Common.BACKGROUND_PATH + "/system/heattreat/distribution";
	}

	@RequestMapping("execution")
	@SystemLog(module="热处理情况",methods="接收完成情况界面")
	public String execution(Model model) {
		model.addAttribute("res", findByRes());
		return Common.BACKGROUND_PATH + "/system/heattreat/execution";
	}

	@ResponseBody
	@RequestMapping("distributionEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="热处理情况",methods="分配操作")//凡需要处理业务逻辑的.都需要记录操作日志
	public String distributionEntity() throws Exception {
		HeatTreatFormMap heatTreatFormMap=getFormMap(HeatTreatFormMap.class);
		String errorMessage="";
		String heattreatIds=heatTreatFormMap.get("heattreatIds")+"";
		String overTimeLimit=heatTreatFormMap.get("overTimeLimit")+"";
		String submiterId=heatTreatFormMap.get("submiterId")+"";
		String source=heatTreatFormMap.get("source")+"";
		if(ToolCommon.isNumeric(submiterId)){
			String processId=heatTreatFormMap.get("processId")+"";

			String[] processIdStr=processId.split(",");
			List<HeatTreatFormMap> heatTreatFormMaps=heattreatMapper.findByIds(heattreatIds);
			for(int i=0;i<heatTreatFormMaps.size();i++){
				for(int j=0;j<processIdStr.length;j++){
					String oprateProcessId=processIdStr[j];
					HeatTreatFormMap heatTreatFormMap1=heatTreatFormMaps.get(i);
					if(oprateProcessId.equals("46602")){//车
						heatTreatFormMap1.set("turnerIsFinish","已接收未完成");
						heattreatMapper.editEntity(heatTreatFormMap1);
					}else if(oprateProcessId.equals("46742")){//渗碳
						heatTreatFormMap1.set("carburizationIsFinish","已接收未完成");
						heattreatMapper.editEntity(heatTreatFormMap1);
					}else if(oprateProcessId.equals("46645")){//热渗氮
						heatTreatFormMap1.set("feedIsFinish","已接收未完成");
						heattreatMapper.editEntity(heatTreatFormMap1);
					}
					String origin=heatTreatFormMap1.get("origin")+"";
					String contractNumber=heatTreatFormMap1.get("contractNumber")+"";
					String mapNumber=heatTreatFormMap1.get("mapNumber")+"";
					String id=heatTreatFormMap1.get("id")+"";
					String processIds=heattreatMapper.findProcessIdsById(id);
						sureDistribution(overTimeLimit,heatTreatFormMap1,submiterId,oprateProcessId,source);
				}
			}
			if(errorMessage.equals("")){
				return "success";
			}else{
				return "success:"+errorMessage;
			}

		}else{
			return "success:不可输入员工需选中员工";
		}

	}

	public void sureDistribution(String overTimeLimit,HeatTreatFormMap heatTreatFormMap1,String submiterId,String oprateProcessId,String source){
		try {
			String amount="";
			if(source.equals("小调度")){
				amount=heatTreatFormMap1.getStr("amount")+"";
			}else{
				amount=heatTreatFormMap1.get("distributionAmount")+"";
			}
			heatTreatFormMap1.set("oprateUserId",submiterId);
			heatTreatFormMap1.set("oprateProcessId",oprateProcessId);
			heatTreatFormMap1.set("oprateState","已接收:"+amount);
			heattreatMapper.editEntity(heatTreatFormMap1);
			String hearttreatId=heatTreatFormMap1.get("id")+"";
			WorkersubmitHeatTreatFormMap workersubmitHeatTreatFormMap=new WorkersubmitHeatTreatFormMap();
			workersubmitHeatTreatFormMap.set("hearttreatId",hearttreatId);
			workersubmitHeatTreatFormMap.set("amount",amount);
			workersubmitHeatTreatFormMap.set("submiterId",submiterId);
			workersubmitHeatTreatFormMap.set("processId",oprateProcessId);
			String startTime=ToolCommon.getNowTime();
			workersubmitHeatTreatFormMap.set("startTime",startTime);
			workersubmitHeatTreatFormMap.set("overTimeLimit",overTimeLimit);
			String estimateCompleteTime=ToolCommon.computeEstimateCompleteTime(startTime,overTimeLimit);
			workersubmitHeatTreatFormMap.set("estimateCompleteTime",estimateCompleteTime);
			GoodProcessFormMap goodProcessFormMap=new GoodProcessFormMap();
			goodProcessFormMap.set("goodId",heatTreatFormMap1.get("goodId"));
			goodProcessFormMap.set("processId",oprateProcessId);
			List<GoodProcessFormMap> goodProcessFormMapList=goodProcessMapper.findByGoodIdAndProcessId(goodProcessFormMap);
			float artificial=0;
			if(goodProcessFormMapList.size()>0){
				artificial=ToolCommon.StringToFloat(goodProcessFormMapList.get(0).get("artificial")+"");
			}
			double distributionWages=ToolCommon.StringToInteger(amount)*artificial;
			workersubmitHeatTreatFormMap.set("distributionWages",distributionWages);
			workersubmitHeatTreatMapper.addEntity(workersubmitHeatTreatFormMap);
			if(oprateProcessId.equals("46679")
					||oprateProcessId.equals("46726")
					||oprateProcessId.equals("46631")
					||oprateProcessId.equals("46607")
					||oprateProcessId.equals("46741")
					||oprateProcessId.equals("46618")
					||oprateProcessId.equals("46625")
					||oprateProcessId.equals("46624")
					||oprateProcessId.equals("46609")
					||oprateProcessId.equals("46632")
					){
				HeattreatcheckFormMap heattreatcheckFormMap=new HeattreatcheckFormMap();
				heattreatcheckFormMap.set("workersubmithearttreatrId",workersubmitHeatTreatFormMap.get("id")+"");
				heattreatcheckMapper.addEntity(heattreatcheckFormMap);
			}else{
				System.out.print("test");
			}
		}catch (Exception e){

		}
	}

	@RequestMapping("findCompleteListUI")
	@SystemLog(module="热处理情况",methods="已完成-界面")
	public String findCompleteListUI(Model model) throws Exception {
		model.addAttribute("res", findByRes());
		UserFormMap userFormMap=getNowUserMessage();
		model.addAttribute("user", userFormMap);
		return Common.BACKGROUND_PATH + "/system/heattreat/completelist";
	}

	@ResponseBody
	@RequestMapping("findCompleteList")
	@SystemLog(module="热处理情况",methods="已完成-列表")
	public HeatTreatFormMap findCompleteList(String sureCompleteUserId,String user,String isOverTime,String content,String startTime,String endTime,String goodName) throws Exception {
		String page=getPara("page");

		String rows=getPara("rows");
		String sureCompleteUser=getPara("sureCompleteUser");
		String startTimeGET=getPara("startTimeGET");
		String endTimeGET=getPara("endTimeGET");
		if(!ToolCommon.isNull(endTimeGET)){
			endTimeGET=ToolCommon.addDay(endTimeGET,1);
		}
		HeatTreatFormMap blankFormMap = getFormMap(HeatTreatFormMap.class);
		WorkersubmitHeatTreatFormMap workersubmitFormMap=new WorkersubmitHeatTreatFormMap();
		if(content==null){
			content="";
		}
		if(user==null){
			user="";
		}
		if(startTime==null){
			startTime=ToolCommon.getNowMonth()+"-01";
		}
		if(endTime==null){
			endTime=ToolCommon.getNowDay();
		}
		endTime= ToolCommon.addDay(endTime,1);
		workersubmitFormMap.set("isOverTime",isOverTime);
		workersubmitFormMap.set("content",content);
		workersubmitFormMap.set("sureCompleteUser",sureCompleteUser);
		workersubmitFormMap.set("state","已完成");
		workersubmitFormMap.set("user",user);
		workersubmitFormMap.set("startTime",startTime);
		workersubmitFormMap.set("endTime",endTime);
		workersubmitFormMap.set("startTimeGET",startTimeGET);
		workersubmitFormMap.set("endTimeGET",endTimeGET);
		workersubmitFormMap.set("goodName",goodName);
		WorkersubmitHeatTreatFormMap workersubmitFormMap1=workersubmitHeatTreatMapper.findAllMoneyByWorkerSubmitIdByStartTimeAndEndTimeAndContentAndUser(workersubmitFormMap);
		if(workersubmitFormMap1==null){
			workersubmitFormMap1=new WorkersubmitHeatTreatFormMap();
			workersubmitFormMap1.set("wages",0);
			workersubmitFormMap1.set("deductWages",0);
			workersubmitFormMap1.set("trueWages",0);
		}
		blankFormMap.set("money",workersubmitFormMap1);

		WorkersubmitHeatTreatFormMap workersubmitFormMapOverTime=workersubmitHeatTreatMapper.findSumOverTimeByWorkerSubmitIdByStartTimeAndEndTimeAndContentAndUser(workersubmitFormMap);
		if(workersubmitFormMapOverTime==null){
			workersubmitFormMapOverTime=new WorkersubmitHeatTreatFormMap();
		}
		blankFormMap.set("overTime",workersubmitFormMapOverTime);
		if(page.equals("1")){
			total=workersubmitHeatTreatMapper.findCountByWorkerSubmitIdByStartTimeAndEndTimeAndContentAndUser(workersubmitFormMap);
		}
		workersubmitFormMap=toFormMap(workersubmitFormMap, page, rows,workersubmitFormMap.getStr("orderby"));
		String sort=getPara("sort");
		String order=getPara("order");

		workersubmitFormMap.set("sort",sort);
		workersubmitFormMap.set("order",order);
		List<WorkersubmitHeatTreatFormMap> workersubmitFormMaps=workersubmitHeatTreatMapper.findByWorkerSubmitIdByStartTimeAndEndTimeAndContentAndUser(workersubmitFormMap);
		blankFormMap.set("rows",workersubmitFormMaps);
		blankFormMap.set("money",workersubmitFormMap1);
		blankFormMap.set("total",total);
		return blankFormMap;
	}

	@RequestMapping("findCheckListUI")
	@SystemLog(module="检验情况",methods="界面")
	public String findCheckListUI(Model model) throws Exception {
		model.addAttribute("res", findByRes());
		UserFormMap userFormMap=getNowUserMessage();
		model.addAttribute("user", userFormMap);
		return Common.BACKGROUND_PATH + "/system/heattreat/checklist";
	}

	@ResponseBody
	@RequestMapping("findCheckList")
	@SystemLog(module="检验情况",methods="列表内容")
	public HeattreatcheckFormMap findCheckList(String checkUser,
										  String user,
										  String goodName,
										  String content,
										  String isCheck,
										  String startCheckTime,
										  String endCheckTime,
										  String startTimeGET,
										  String endTimeGET) throws Exception {
		String page=getPara("page");

		String rows=getPara("rows");
		if(!ToolCommon.isNull(endTimeGET)){
			endTimeGET=ToolCommon.addDay(endTimeGET,1);
		}
		HeattreatcheckFormMap heattreatcheckFormMap = getFormMap(HeattreatcheckFormMap.class);
		if(content==null){
			content="";
		}
		if(user==null){
			user="";
		}
		if(!ToolCommon.isNull(endCheckTime)){
			endCheckTime= ToolCommon.addDay(endCheckTime,1);
		}
		heattreatcheckFormMap.set("isCheck",isCheck);
		heattreatcheckFormMap.set("content",content);
		heattreatcheckFormMap.set("checkUser",checkUser);
		heattreatcheckFormMap.set("user",user);
		heattreatcheckFormMap.set("startCheckTime",startCheckTime);
		heattreatcheckFormMap.set("endCheckTime",endCheckTime);
		heattreatcheckFormMap.set("startTimeGET",startTimeGET);
		heattreatcheckFormMap.set("endTimeGET",endTimeGET);
		heattreatcheckFormMap.set("goodName",goodName);
		if(page.equals("1")){
			total=heattreatcheckMapper.findCountByAllLike(heattreatcheckFormMap);
		}
		heattreatcheckFormMap=toFormMap(heattreatcheckFormMap, page, rows,heattreatcheckFormMap.getStr("orderby"));
		String sort=getPara("sort");
		String order=getPara("order");

		heattreatcheckFormMap.set("sort",sort);
		heattreatcheckFormMap.set("order",order);
		List<HeattreatcheckFormMap> workersubmitFormMaps=heattreatcheckMapper.findByAllLike(heattreatcheckFormMap);
		heattreatcheckFormMap.set("rows",workersubmitFormMaps);
		heattreatcheckFormMap.set("total",total);
		return heattreatcheckFormMap;
	}

	@ResponseBody
	@RequestMapping("deleteCheckEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="热处理情况",methods="检验情况-删除组")//凡需要处理业务逻辑的.都需要记录操作日志
	public String deleteCheckEntity() throws Exception {
		SystemconfigFormMap systemconfigFormMap=new SystemconfigFormMap();
		systemconfigFormMap.set("name","deletepassword");
		String content=getPara("password");
		systemconfigFormMap.set("content",content);
		List<SystemconfigFormMap> systemconfigFormMaps=systemconfigMapper.findByNames(systemconfigFormMap);
		if(systemconfigFormMaps.size()>0){
			String ids = getPara("ids");
			ids=ids.substring(0,ids.length()-1);
			heattreatcheckMapper.deleteByIds(ids);
			return "success";
		}else{
			return "删除订单密码错误";
		}

	}

	@ResponseBody
	@RequestMapping("findCompleteListAll")
	@SystemLog(module="热处理情况",methods="已完成-列表全部")
	public HeatTreatFormMap findCompleteListAll(String user,String content,String startTime,String endTime) throws Exception {
		HeatTreatFormMap blankFormMap = getFormMap(HeatTreatFormMap.class);
		WorkersubmitHeatTreatFormMap workersubmitFormMap=new WorkersubmitHeatTreatFormMap();
		if(content==null){
			content="";
		}
		if(user==null){
			user="";
		}
		if(startTime==null){
			startTime=ToolCommon.getNowMonth()+"-01";
		}
		if(endTime!=null&&!endTime.equals("")){
			endTime=ToolCommon.getNowDay();
		}
		endTime=ToolCommon.addDay(endTime,1);
		workersubmitFormMap.set("content",content);
		workersubmitFormMap.set("state","已完成");
		workersubmitFormMap.set("user",user);
		workersubmitFormMap.set("startTime",startTime);
		workersubmitFormMap.set("endTime",endTime);
		List<WorkersubmitHeatTreatFormMap> workersubmitFormMaps=workersubmitHeatTreatMapper.findByWorkerSubmitIdByStartTimeAndEndTimeAndContentAndUser(workersubmitFormMap);
		blankFormMap.set("rows",workersubmitFormMaps);
		blankFormMap.set("total",total);
		return blankFormMap;
	}

	@RequestMapping("printUI")
	@SystemLog(module="热处理情况",methods="打印单")//凡需要处理业务逻辑的.都需要记录操作日志
	public String blankorderPrintUI(Model model,String origin,String ids) throws Exception {
		ids=ids.substring(0,ids.length()-1);
		List<HeatTreatFormMap> list=heattreatMapper.findByIds(ids);
		HeatTreatFormMap entity=heattreatMapper.findSumWeightAndAmountByIds(ids);
		model.addAttribute("origin",origin);
		model.addAttribute("ids",ids);
		model.addAttribute("entity",entity);
		model.addAttribute("rows",list);
		model.addAttribute("companyName",getCompanyName());
		model.addAttribute("nowDay",ToolCommon.getNowTime());
		return Common.BACKGROUND_PATH + "/system/heattreat/printShow";
	}

	@ResponseBody
	@RequestMapping("modifyIsPrint")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="热处理情况",methods="热处理情况-打印单子")//凡需要处理业务逻辑的.都需要记录操作日志
	public String modifyPrintTime() throws Exception {
		String ids = getPara("ids");
		heattreatMapper.updateIsPrintTrueByIds(ids);
		return "success";
	}

	@ResponseBody
	@RequestMapping("modifyIsSend")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="热处理情况",methods="成品库-发货")//凡需要处理业务逻辑的.都需要记录操作日志
	public String modifyIsSend() throws Exception {
		String ids = getPara("ids");
		heattreatMapper.updateIsSendStateByIds(ids,"是");
		return "success";
	}

	@RequestMapping("printDistributionUI")
	@SystemLog(module="热处理情况",methods="打印派工单")//凡需要处理业务逻辑的.都需要记录操作日志
	public String printDistributionUI(Model model,String origin,String ids) throws Exception {
		ids=ids.substring(0,ids.length()-1);
		List<HeatTreatFormMap> list=heattreatMapper.findByIds(ids);
		model.addAttribute("origin",origin);
		model.addAttribute("ids",ids);
		model.addAttribute("rows",list);
		model.addAttribute("companyName",getCompanyName());
		model.addAttribute("nowDay",ToolCommon.getNowTime());
		return Common.BACKGROUND_PATH + "/system/heattreat/printDistributionShow";
	}

	@RequestMapping("printDrawingUI")
	@SystemLog(module="热处理情况",methods="打印图纸")//凡需要处理业务逻辑的.都需要记录操作日志
	public String printDrawingUI(Model model,String ids,String origin) throws Exception {
		ids=ids.substring(0,ids.length()-1);
		if(origin.equals("热处理")){
			List<HeatTreatFormMap> list=heattreatMapper.findMainGoodAndOrderByIds(ids);
			model.addAttribute("rows",list);
		}else{
			List<BlankFormMap> list=blankMapper.findMainGoodAndOrderByIds(ids);
			model.addAttribute("rows",list);
		}

		return Common.BACKGROUND_PATH + "/system/heattreat/printDrawingShow";
	}

	@RequestMapping("pringTechnologyUI")
	@SystemLog(module="热处理情况",methods="打印工艺卡")//凡需要处理业务逻辑的.都需要记录操作日志
	public String pringTechnologyUI(Model model,String ids,String origin) throws Exception {
		ids=ids.substring(0,ids.length()-1);
		if(origin.equals("热处理")){
			List<HeatTreatFormMap> list=heattreatMapper.findMainGoodAndOrderNoImgByIds(ids);
			List<HeatTreatFormMap> heatTreatFormMapList=new ArrayList<>();
			for(int i=0;i<list.size();i++){
				HeatTreatFormMap heatTreatFormMap=list.get(i);
				String goodId=heatTreatFormMap.get("goodId")+"";
				List<GoodProcessFormMap> goodProcessFormMaps=goodProcessMapper.findByGoodId(goodId);
				heatTreatFormMap.set("process",goodProcessFormMaps);
				heatTreatFormMapList.add(heatTreatFormMap);
			}
			model.addAttribute("rows",heatTreatFormMapList);
		}else{
			List<BlankFormMap> list=blankMapper.findMainGoodAndOrderNoImgByIds(ids);
			List<BlankFormMap> heatTreatFormMapList=new ArrayList<>();
			for(int i=0;i<list.size();i++){
				BlankFormMap blankFormMap=list.get(i);
				String goodId=blankFormMap.get("goodId")+"";
				List<GoodProcessFormMap> goodProcessFormMaps=goodProcessMapper.findByGoodId(goodId);
				blankFormMap.set("process",goodProcessFormMaps);
				heatTreatFormMapList.add(blankFormMap);
			}
			model.addAttribute("rows",heatTreatFormMapList);
		}

		return Common.BACKGROUND_PATH + "/system/heattreat/pringTechnologyShow";
	}

	@RequestMapping(value = "exportAll")
	@ResponseBody
	@SystemLog(module="热处理情况",methods="全部导出")
	public void exportAll(HttpServletResponse response, String entity) throws Exception {

		String contentS=ToolCommon.json2Object(entity).getString("content");
		String origin=ToolCommon.json2Object(entity).getString("origin");
		String pickTimeStart=ToolCommon.json2Object(entity).getString("pickTimeStart");
		String pickTimeEnd=ToolCommon.json2Object(entity).getString("pickTimeEnd");
		String backTimeStart=ToolCommon.json2Object(entity).getString("backTimeStart");
		String backTimeEnd=ToolCommon.json2Object(entity).getString("backTimeEnd");
		HeatTreatFormMap heattreatFormMap=new HeatTreatFormMap();
		heattreatFormMap.set("origin",origin);
		heattreatFormMap.set("content",contentS);
		heattreatFormMap.set("pickTimeStart",pickTimeStart);
		heattreatFormMap.set("pickTimeEnd",pickTimeEnd);
		heattreatFormMap.set("backTimeStart",backTimeStart);
		heattreatFormMap.set("backTimeEnd",backTimeEnd);
		List<HeatTreatFormMap> heatTreatFormMaps=heattreatMapper.findByAllLike(heattreatFormMap);

		//excel标题
		String[] title = {"领料日期",
				"合同号",
				"图号",
				"产品尺寸",
				"数量",
				"单重",
				"总重量（Kg）",
				"材质",
				"硬度",
				"交货日期",
				"备注1",
				"回料日期",
				"回料数量",
				"备注2"};

		//excel文件名
		String fileName = origin+"情况" + System.currentTimeMillis() + ".xls";

		//sheet名
		String sheetName = "sheet1";

		String [][] content = new String[heatTreatFormMaps.size()][title.length];

		for (int i = 0; i < heatTreatFormMaps.size(); i++) {
			content[i] = new String[title.length];
			HeatTreatFormMap obj = heatTreatFormMaps.get(i);
			content[i][0] = obj.get("pickTime")+"";
			content[i][1] = obj.get("contractNumber")+"";
			content[i][2] = obj.get("mapNumber")+"";
			content[i][3] = obj.get("goodSize")+"";
			content[i][4] = obj.get("amount")+"";
			content[i][5] = obj.get("goodWeight")+"";
			content[i][6] = obj.get("weight")+"";
			content[i][7] = obj.get("materialQualityName")+"";
			content[i][8] = obj.get("hardnessName")+"";
			content[i][9] = obj.get("deliveryTime")+"";
			content[i][10] = obj.get("remarks1")+"";
			content[i][11] = obj.get("backTime")+"";
			content[i][12] = obj.get("backAmount")+"";
			content[i][13] = obj.get("remarks2")+"";


		}

		//创建HSSFWorkbook
		HSSFWorkbook wb = ExcelUtils.getHSSFWorkbook(origin+"情况",sheetName, title, content, null);

		//响应到客户端
		try {
			ExcelUtils.setResponseHeader(response, fileName);
			OutputStream os = response.getOutputStream();
			wb.write(os);
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "exportExecutionAll")
	@ResponseBody
	@SystemLog(module="热处理情况",methods="接收完成情况-全部导出")
	public void exportExecutionAll(HttpServletResponse response, String entity) throws Exception {

		String contentS=ToolCommon.json2Object(entity).getString("content");
		String goodName=ToolCommon.json2Object(entity).getString("goodName");
		String clientId=ToolCommon.json2Object(entity).getString("clientId");
		String state=ToolCommon.json2Object(entity).getString("state");
		String oprateUserId=ToolCommon.json2Object(entity).getString("oprateUserId");
		String startTime=ToolCommon.json2Object(entity).getString("startTime");
		String endTime=ToolCommon.json2Object(entity).getString("endTime");
		String startTimeGet=ToolCommon.json2Object(entity).getString("startTimeGet");
		String endTimeGet=ToolCommon.json2Object(entity).getString("endTimeGet");
		String startTimeComplete=ToolCommon.json2Object(entity).getString("startTimeComplete");
		String endTimeComplete=ToolCommon.json2Object(entity).getString("endTimeComplete");
		HeatTreatFormMap heattreatFormMap=new HeatTreatFormMap();
		heattreatFormMap.set("content",contentS);
		heattreatFormMap.set("goodName",goodName);
		heattreatFormMap.set("clientId",clientId);
		heattreatFormMap.set("state",state);
		heattreatFormMap.set("oprateUserId",oprateUserId);
		heattreatFormMap.set("startTime",startTime);
		heattreatFormMap.set("endTime",endTime);

		heattreatFormMap.set("startTimeGet",startTimeGet);
		if(!ToolCommon.isNull(endTimeGet)){
			endTimeGet=ToolCommon.addDay(endTimeGet,1);
		}
		heattreatFormMap.set("endTimeGet",endTimeGet);
		heattreatFormMap.set("startTimeComplete",startTimeComplete);
		if(!ToolCommon.isNull(endTimeComplete)){
			endTimeComplete=ToolCommon.addDay(endTimeComplete,1);
		}
		heattreatFormMap.set("endTimeComplete",endTimeComplete);
		List<HeatTreatFormMap> heatTreatFormMaps=heattreatMapper.findExecutionByAllLike(heattreatFormMap);

		//excel标题
		String[] title = {"客户",
				"合同号",
				"图号",
				"产品名称",
				"产品尺寸",
				"材质",
				"交货日期",
				"工序",
				"接收人",
				"接收数量",
				"接收时间",
				"完成数量",
				"废品数量",
				"完成时间"};

		//excel文件名
		String fileName ="接收完成情况" + System.currentTimeMillis() + ".xls";

		//sheet名
		String sheetName = "sheet1";

		String [][] content = new String[heatTreatFormMaps.size()][title.length];

		for (int i = 0; i < heatTreatFormMaps.size(); i++) {
			content[i] = new String[title.length];
			HeatTreatFormMap obj = heatTreatFormMaps.get(i);
			content[i][0] = obj.get("clientName")+"";
			content[i][1] = obj.get("contractNumber")+"";
			content[i][2] = obj.get("mapNumber")+"";
			content[i][3] = obj.get("goodName")+"";
			content[i][4] = obj.get("goodSize")+"";
			content[i][5] = obj.get("materialQualityName")+"";
			content[i][6] = obj.get("deliveryTime")+"";
			content[i][7] = obj.get("processName")+"";
			content[i][8] = obj.get("userName")+"";
			content[i][9] = obj.get("amount")+"";
			content[i][10] = obj.get("startTime")+"";
			content[i][11] = obj.get("completeAmount")+"";
			content[i][12] = obj.get("badAmount")+"";
			content[i][13] = obj.get("completeTime")+"";


		}

		//创建HSSFWorkbook
		HSSFWorkbook wb = ExcelUtils.getHSSFWorkbook("接收完成情况",sheetName, title, content, null);

		//响应到客户端
		try {
			ExcelUtils.setResponseHeader(response, fileName);
			OutputStream os = response.getOutputStream();
			wb.write(os);
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@ResponseBody
	@RequestMapping("editEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="热处理情况",methods="修改保存")//凡需要处理业务逻辑的.都需要记录操作日志
	public String editEntity(String entity) throws Exception {
		List<HeatTreatFormMap> list = (List) ToolCommon.json2ObjectList(entity, HeatTreatFormMap.class);
		String nowtime=ToolCommon.getNowTime();
		UserFormMap userFormMap=getNowUserMessage();
		String userId=String.valueOf(userFormMap.getInt("id"));
		for(int i=0;i<list.size();i++){
			HeatTreatFormMap heattreatFormMap=list.get(i);
			String id=heattreatFormMap.get("id")+"";
			heattreatFormMap.set("modifytime",nowtime);
			if(id==null||id.equals("")||id.equals("null")){
				heattreatFormMap.set("userId",userId);
				heattreatFormMap.remove("id");
				heattreatMapper.addEntity(heattreatFormMap);
			}else{
				HeatTreatFormMap heatTreatFormMap1=heatTreatMapper.findById(id);
				String backAmount=heatTreatFormMap1.get("backAmount")+"";
				heattreatMapper.editEntity(heattreatFormMap);
				String backAmountNow=heattreatFormMap.get("backAmount")+"";
				id=heattreatFormMap.get("id")+"";
				String origin=heattreatFormMap.get("origin")+"";
				if(origin.equals("渗碳")){
					String goodName=heattreatFormMap.get("goodName")+"";
					if(ToolCommon.isContain(goodName,"钢套")
							||ToolCommon.isContain(goodName,"钢司")){
						if(!backAmount.equals(backAmountNow)){
							heatTreatMapper.updateIsJumpStateById(id,"是");
						}
					}
					if(ToolCommon.isContain(goodName,"轴")
							||ToolCommon.isContain(goodName,"铰边")
							||ToolCommon.isContain(goodName,"铰销")){
						if(!backAmount.equals(backAmountNow)){
							heatTreatMapper.updateIsPinShaftStateById(id,"是");//轴或铰边或铰销
						}
					}
					if(ToolCommon.isContain(goodName,"垫")
							||ToolCommon.isContain(goodName,"片")){
						if(!backAmount.equals(backAmountNow)){
							heatTreatMapper.updateIsPadStateById(id,"是");//垫或片
						}
					}
				}
			}

		}
		return "success";
	}

	@ResponseBody
	@RequestMapping("deleteEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="热处理情况",methods="组管理-删除组")//凡需要处理业务逻辑的.都需要记录操作日志
	public String deleteEntity() throws Exception {
		SystemconfigFormMap systemconfigFormMap=new SystemconfigFormMap();
		systemconfigFormMap.set("name","deletepassword");
		String content=getPara("password");
		systemconfigFormMap.set("content",content);
		List<SystemconfigFormMap> systemconfigFormMaps=systemconfigMapper.findByNames(systemconfigFormMap);
		if(systemconfigFormMaps.size()>0){
			String ids = getPara("ids");
			ids=ids.substring(0,ids.length()-1);
			String[] idsStr=ids.split(",");
			for (String id : idsStr) {
				String state=heattreatMapper.findIsCylindricalroughgrindingById(id);
				if(state.equals("true")){
					heattreatMapper.updateIsJumpStateById(id,"");
				}else{
					heattreatMapper.deleteById(id);
				}

			}
			return "success";
		}else{
			return "删除订单密码错误";
		}

	}

}