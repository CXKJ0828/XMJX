package com.zhjh.controller.system;

import com.zhjh.annotation.SystemLog;
import com.zhjh.bean.ComboboxEntity;
import com.zhjh.bean.FileBean;
import com.zhjh.controller.index.BaseController;
import com.zhjh.entity.*;
import com.zhjh.file.ExcelUtils;
import com.zhjh.mapper.*;
import com.zhjh.tool.ToolCommon;
import com.zhjh.tool.ToolExcel;
import com.zhjh.util.Common;
import com.zhjh.util.ExcelUtil;
import com.zhjh.util.QrCodeUtils;
import com.zhjh.util.StringUtil;
import net.sf.json.JSONArray;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

/**
 *
 * @author lanyuan 2014-11-19
 * @Email: mmm333zzz520@163.com
 * @version 3.0v
 */
@Controller
@RequestMapping("/blank/")
public class BlankController extends BaseController {
	@Inject
	private GoodMapper goodMapper;

	@Inject
	private BlankMapper blankMapper;

	@Inject
	private MaterialqualitytypeProcessMapper materialqualitytypeProcessMapper;
	@Inject
	private FixedProcessMapper fixedProcessMapper;

	@Inject
	private ProgressSearchMapper progressSearchMapper;

	@Inject
	private HeatTreatMapper heatTreatMapper;

	@Inject
	private MaterialBuyOrderMapper materialBuyOrderMapper;

	@Inject
	private MaterialBuyOrderDetailsMapper materialBuyOrderDetailsMapper;


	@Inject
	private BlankSizeMapper blankSizeMapper;

	@Inject
	private ProcessMapper processMapper;

	@Inject
	private WorkersubmitMapper workersubmitMapper;

	@Inject
	private SystemconfigMapper systemconfigMapper;

	@Inject
	private GoodProcessMapper goodProcessMapper;

	@Inject
	private WorkersubmitHeatTreatMapper workersubmitHeatTreatMapper;

	@Inject
	private ClientMapper clientMapper;

	@Inject
	private OrderMapper orderMapper;

	@Inject
	private OrderDetailsMapper orderDetailsMapper;

	@Inject
	private UnqualifiedMapper unqualifiedMapper;

	@Inject
	private ScrapMapper scrapMapper;

	@Inject
	private BadnoticeMapper badnoticeMapper;

	@Inject
	private SendInputMapper sendInputMapper;

	@Inject
	private StockMapper stockMapper;

	@Inject
	private BlankProcessMapper blankProcessMapper;

	@Inject
	private UserMapper userMapper;

	String blankExportAllsort="";
	String blankExportAllorder="";

	@RequestMapping("list")
	public String listUI(Model model) throws Exception {
		model.addAttribute("res", findByRes());
		return Common.BACKGROUND_PATH + "/system/blank/list";
	}

	@RequestMapping("feedlist")
	public String feedlist(Model model) throws Exception {
		model.addAttribute("res", findByRes());
		return Common.BACKGROUND_PATH + "/system/blank/feedlist";
	}

	@RequestMapping("printCodeShow")
	@SystemLog(module="生产管理",methods="进入生成接收码列表")//凡需要处理业务逻辑的.都需要记录操作日志
	public String printCodeShow(Model model) throws Exception {
		model.addAttribute("res", findByRes());
		return Common.BACKGROUND_PATH + "/system/blank/printCodeShow";
	}

	@RequestMapping("printCompleteCodeShow")
	public String printCompleteCodeShow(Model model) throws Exception {
		model.addAttribute("res", findByRes());
		return Common.BACKGROUND_PATH + "/system/blank/printCompleteCodeShow";
	}

	@RequestMapping("technologylist")
	public String technologylistUI(Model model) throws Exception {
		SystemconfigFormMap systemconfigFormMapWages=systemconfigMapper.findByAttribute("name","dailyWages",SystemconfigFormMap.class).get(0);
		String dailyWages=systemconfigFormMapWages.get("content")+"";
		float dailyWagesF=ToolCommon.StringToFloat(dailyWages);
		model.addAttribute("dailyWages",dailyWagesF);
		model.addAttribute("res", findByRes());
		return Common.BACKGROUND_PATH + "/system/blank/technologylist";
	}

	public class TreeBigShowClientEntity {
		public String id;
		public String text;
		public String state;
	}

	public class TreeClientEntity {
		public String id;
		public String text;
		public String state;
		public List<TreeOrderFormMap> children;
	}

	public class TreeOrderFormMap {
		public String id;
		public String text;
		public String state;
		public List<TreeOrderDetailsFormMap> children;
	}

	public class TreeOrderDetailsFormMap {
		public String id;
		public String text;
	}

	@ResponseBody
	@RequestMapping("findTechnologylist")
	@SystemLog(module="生产管理",methods="工艺卡-根据图号和合同号查询对应客户信息")//凡需要处理业务逻辑的.都需要记录操作日志
	public List findTechnologylist(String content,String contractNumber,String startTime,String endTime) throws Exception {
		if(content==null){
			content="";
		}
		if(contractNumber==null){
			contractNumber="";
		}
		if(endTime!=null&&!endTime.equals("")){
			endTime=ToolCommon.addDay(endTime,1);
		}
		ClientFormMap clientFormMap1=new ClientFormMap();
		clientFormMap1.set("mapNumber",content);
		clientFormMap1.set("contractNumber",contractNumber);
		clientFormMap1.set("startTime",startTime);
		clientFormMap1.set("endTime",endTime);
		List<ClientFormMap> clientFormMaps=clientMapper.findUnCompleteOrderClientGroupByClientId(clientFormMap1);
		List<TreeClientEntity> treeClientEntities=new ArrayList<>();
		for(int i=0;i<clientFormMaps.size();i++){
			ClientFormMap clientFormMap=clientFormMaps.get(i);
			String clientId=clientFormMap.get("id")+"";
			String fullName=clientFormMap.getStr("fullName");
			TreeClientEntity treeClientEntity=new TreeClientEntity();
			treeClientEntity.id=clientId+":客户";
			treeClientEntity.text=fullName;
			treeClientEntity.state="closed";
			List<TreeOrderFormMap> treeOrderFormMaps=new ArrayList<>();
//			OrderFormMap orderFormMap1=new OrderFormMap();
//			orderFormMap1.set("clientId",clientId);
//			orderFormMap1.set("mapNumber",content);
//			List<OrderFormMap> orderFormMaps=orderMapper.findUnSendByClientId(orderFormMap1);
//			for(int j=0;j<orderFormMaps.size();j++){
//				OrderFormMap orderFormMap=orderFormMaps.get(j);
//				String orderId=orderFormMap.get("id")+"";
//				String contractNumber=orderFormMap.getStr("contractNumber");
//				TreeOrderFormMap treeOrderFormMap=new TreeOrderFormMap();
//				treeOrderFormMap.id=orderId;
//				treeOrderFormMap.text=contractNumber;
//				treeOrderFormMap.state="closed";
//				OrderDetailsFormMap orderDetailsFormMapS=new OrderDetailsFormMap();
//				orderDetailsFormMapS.set("orderId",orderId);
//				List<OrderDetailsFormMap> orderDetailsFormMaps=orderDetailsMapper.getOrderDetailsByOrderId(orderDetailsFormMapS);
//				List<TreeOrderDetailsFormMap> treeOrderDetailsFormMaps=new ArrayList<>();
//				for(int n=0;n<orderDetailsFormMaps.size();n++){
//					OrderDetailsFormMap orderDetailsFormMap=orderDetailsFormMaps.get(n);
//					String orderdetailsId=orderDetailsFormMap.get("id")+"";
//					String mapNumber=orderDetailsFormMap.getStr("mapNumber");
//					TreeOrderDetailsFormMap treeOrderDetailsFormMap=new TreeOrderDetailsFormMap();
//					treeOrderDetailsFormMap.id="订单明细"+orderdetailsId;
//					treeOrderDetailsFormMap.text=mapNumber;
//					treeOrderDetailsFormMaps.add(treeOrderDetailsFormMap);
//				}
//				treeOrderFormMap.children=treeOrderDetailsFormMaps;
//				treeOrderFormMaps.add(treeOrderFormMap);
//			}
			treeClientEntity.children=treeOrderFormMaps;
			treeClientEntities.add(treeClientEntity);
		}
		return treeClientEntities;
	}

	@ResponseBody
	@RequestMapping("findTechnologylistByClientId")
	@SystemLog(module="生产管理",methods="工艺卡-根据客户id获取订单信息")//凡需要处理业务逻辑的.都需要记录操作日志
	public List findTechnologylistByClientId(String content,String clientId,String contractNumber,String startTime,String endTime) throws Exception {
		if(content==null){
			content="";
		}
		if(contractNumber==null){
			contractNumber="";
		}
		if(endTime!=null&&!endTime.equals("")){
			endTime=ToolCommon.addDay(endTime,1);
		}
		List<TreeOrderFormMap> treeOrderFormMaps=new ArrayList<>();
			OrderFormMap orderFormMap1=new OrderFormMap();
			orderFormMap1.set("clientId",clientId);
		orderFormMap1.set("startTime",startTime);
		orderFormMap1.set("endTime",endTime);
		orderFormMap1.set("contractNumber",contractNumber);
			orderFormMap1.set("mapNumber",content);
			List<OrderFormMap> orderFormMaps=orderMapper.findUnSendByClientId(orderFormMap1);
			for(int j=0;j<orderFormMaps.size();j++){
				OrderFormMap orderFormMap=orderFormMaps.get(j);
				String orderId=orderFormMap.get("id")+"";
				String contractNumber1=orderFormMap.getStr("contractNumber");
				TreeOrderFormMap treeOrderFormMap=new TreeOrderFormMap();
				treeOrderFormMap.id=orderId+":订单";
				treeOrderFormMap.text=contractNumber1;
				treeOrderFormMap.state="closed";
				List<TreeOrderDetailsFormMap> treeOrderDetailsFormMaps=new ArrayList<>();
				treeOrderFormMap.children=treeOrderDetailsFormMaps;
				treeOrderFormMaps.add(treeOrderFormMap);
			}

		return treeOrderFormMaps;
	}

	@ResponseBody
	@RequestMapping("findTechnologylistByOrderId")
	@SystemLog(module="生产管理",methods="工艺卡-根据订单获取未完成的订单明细")//凡需要处理业务逻辑的.都需要记录操作日志
	public List findTechnologylistByOrderId(String content,String orderId,String startTime,String endTime) throws Exception {
				if(content==null){
					content="";
				}
				if(endTime!=null&&!endTime.equals("")){
					endTime=ToolCommon.addDay(endTime,1);
				}
				OrderDetailsFormMap orderDetailsFormMapS=new OrderDetailsFormMap();
				orderDetailsFormMapS.set("orderId",orderId);
				orderDetailsFormMapS.set("mapNumber",content);
				orderDetailsFormMapS.set("startTime",startTime);
				orderDetailsFormMapS.set("endTime",endTime);
				List<OrderDetailsFormMap> orderDetailsFormMaps=orderDetailsMapper.getUnCompleteOrderDetailsByOrderId(orderDetailsFormMapS);
				List<TreeOrderDetailsFormMap> treeOrderDetailsFormMaps=new ArrayList<>();
				for(int n=0;n<orderDetailsFormMaps.size();n++){
					OrderDetailsFormMap orderDetailsFormMap=orderDetailsFormMaps.get(n);
					String orderdetailsId=orderDetailsFormMap.get("id")+"";
					String mapNumber=orderDetailsFormMap.getStr("mapNumber");
					TreeOrderDetailsFormMap treeOrderDetailsFormMap=new TreeOrderDetailsFormMap();
					treeOrderDetailsFormMap.id="订单明细"+orderdetailsId;
					treeOrderDetailsFormMap.text=mapNumber;
					treeOrderDetailsFormMaps.add(treeOrderDetailsFormMap);
				}
		return treeOrderDetailsFormMaps;
	}

	@RequestMapping("findGetListUI")
	@SystemLog(module="生产管理",methods="已接收列表-界面")
	public String findGetListUI(Model model) throws Exception {
		model.addAttribute("res", findByRes());
		return Common.BACKGROUND_PATH + "/system/blank/getlist";
	}

	@ResponseBody
	@RequestMapping("findGetList")
	@SystemLog(module="生产管理",methods="已接收列表-获取列表内容")
	public BlankFormMap findGetList(String user,String content,String startTime,String endTime) throws Exception {
		BlankFormMap blankFormMap = getFormMap(BlankFormMap.class);
		WorkersubmitFormMap workersubmitFormMap=new WorkersubmitFormMap();
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
		workersubmitFormMap.set("content",content);
		workersubmitFormMap.set("state","已接收未完成");
		workersubmitFormMap.set("user",user);
		workersubmitFormMap.set("startTime",startTime);
		workersubmitFormMap.set("endTime",endTime);
		WorkersubmitFormMap workersubmitFormMap1=workersubmitMapper.findAllMoneyByWorkerSubmitIdByStartTimeAndEndTimeAndContentAndUser(workersubmitFormMap);
		if(workersubmitFormMap1==null){
			workersubmitFormMap1=new WorkersubmitFormMap();
		}
		blankFormMap.set("money",workersubmitFormMap1);
		if(page.equals("1")){
			total=workersubmitMapper.findCountByWorkerSubmitIdByStartTimeAndEndTimeAndContentAndUser(workersubmitFormMap);
		}
		workersubmitFormMap=toFormMap(workersubmitFormMap, page, rows,workersubmitFormMap.getStr("orderby"));
		String sort=getPara("sort");
		String order=getPara("order");

		workersubmitFormMap.set("sort",sort);
		workersubmitFormMap.set("order",order);
		List<WorkersubmitFormMap> workersubmitFormMaps=workersubmitMapper.findByWorkerSubmitIdByStartTimeAndEndTimeAndContentAndUser(workersubmitFormMap);
		blankFormMap.set("rows",workersubmitFormMaps);
		blankFormMap.set("total",total);
		return blankFormMap;
	}


	@ResponseBody
	@RequestMapping("findGetListAll")
	public BlankFormMap findGetListAll(String user,String content,String startTime,String endTime) throws Exception {
		BlankFormMap blankFormMap = getFormMap(BlankFormMap.class);
		WorkersubmitFormMap workersubmitFormMap=new WorkersubmitFormMap();
		if(content==null){
			content="";
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
		workersubmitFormMap.set("content",content);
		workersubmitFormMap.set("state","已接收未完成");
		workersubmitFormMap.set("user",user);
		workersubmitFormMap.set("startTime",startTime);
		workersubmitFormMap.set("endTime",endTime);
		List<WorkersubmitFormMap> workersubmitFormMaps=workersubmitMapper.findByWorkerSubmitIdByStartTimeAndEndTimeAndContentAndUser(workersubmitFormMap);
		blankFormMap.set("rows",workersubmitFormMaps);
		blankFormMap.set("total",total);
		return blankFormMap;
	}

	@ResponseBody
	@RequestMapping("findByBlankId")
	public BlankFormMap findGetList(String blankId) throws Exception {
		BlankFormMap blankFormMap = getFormMap(BlankFormMap.class);
//		List<BlankFormMap> workersubmitFormMaps=blankMapper.findById(blankId);
//		blankFormMap.set("rows",workersubmitFormMaps);
//		blankFormMap.set("total",total);
		return blankFormMap;
	}

	@RequestMapping("findCompleteListUI")
	public String findCompleteListUI(Model model) throws Exception {
		model.addAttribute("res", findByRes());
		UserFormMap userFormMap=getNowUserMessage();
		model.addAttribute("user", userFormMap);
		return Common.BACKGROUND_PATH + "/system/blank/completelist";
	}


	@ResponseBody
	@RequestMapping("findCompleteList")
	public BlankFormMap findCompleteList(String user,String content,String startTime,String endTime) throws Exception {
		String page=getPara("page");

		String rows=getPara("rows");
		BlankFormMap blankFormMap = getFormMap(BlankFormMap.class);
		WorkersubmitFormMap workersubmitFormMap=new WorkersubmitFormMap();
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
		workersubmitFormMap.set("content",content);
		workersubmitFormMap.set("state","已完成");
		workersubmitFormMap.set("user",user);
		workersubmitFormMap.set("startTime",startTime);
		workersubmitFormMap.set("endTime",endTime);
		WorkersubmitFormMap workersubmitFormMap1=workersubmitMapper.findAllMoneyByWorkerSubmitIdByStartTimeAndEndTimeAndContentAndUser(workersubmitFormMap);
		if(workersubmitFormMap1==null){
			workersubmitFormMap1=new WorkersubmitFormMap();
		}
		blankFormMap.set("money",workersubmitFormMap1);
		if(page.equals("1")){
			total=workersubmitMapper.findCountByWorkerSubmitIdByStartTimeAndEndTimeAndContentAndUser(workersubmitFormMap);
		}
		workersubmitFormMap=toFormMap(workersubmitFormMap, page, rows,workersubmitFormMap.getStr("orderby"));
		String sort=getPara("sort");
		String order=getPara("order");

		workersubmitFormMap.set("sort",sort);
		workersubmitFormMap.set("order",order);
		List<WorkersubmitFormMap> workersubmitFormMaps=workersubmitMapper.findByWorkerSubmitIdByStartTimeAndEndTimeAndContentAndUser(workersubmitFormMap);
		blankFormMap.set("rows",workersubmitFormMaps);
		blankFormMap.set("money",workersubmitFormMap1);
		blankFormMap.set("total",total);
		return blankFormMap;
	}

	@RequestMapping("wagesPrintUI")
	public String wagesPrintUI(Model model,String user,
							   String startTime,String endTime) throws Exception {
		WorkersubmitFormMap workersubmitFormMap=new WorkersubmitFormMap();
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
		WorkersubmitFormMap workersubmitFormMap1=workersubmitMapper.findAllMoneyByWorkerSubmitIdByStartTimeAndEndTimeAndContentAndUser(workersubmitFormMap);
		if(workersubmitFormMap1==null){
			workersubmitFormMap1=new WorkersubmitFormMap();
			workersubmitFormMap1.set("wages",0);
			workersubmitFormMap1.set("deductWages",0);
			workersubmitFormMap1.set("trueWages",0);
		}
		model.addAttribute("money",workersubmitFormMap1);
		model.addAttribute("user",user);
		endTime= ToolCommon.addDay(endTime,-1);
		model.addAttribute("time",startTime+"至"+endTime);
		return Common.BACKGROUND_PATH + "/system/blank/wagesOrderPrintShow";
	}


	@ResponseBody
	@RequestMapping("findCompleteListAll")
	public BlankFormMap findCompleteListAll(String user,String content,String startTime,String endTime) throws Exception {
		BlankFormMap blankFormMap = getFormMap(BlankFormMap.class);
		WorkersubmitFormMap workersubmitFormMap=new WorkersubmitFormMap();
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
		List<WorkersubmitFormMap> workersubmitFormMaps=workersubmitMapper.findByWorkerSubmitIdByStartTimeAndEndTimeAndContentAndUser(workersubmitFormMap);
		blankFormMap.set("rows",workersubmitFormMaps);
		blankFormMap.set("total",total);
		return blankFormMap;
	}


	@ResponseBody
	@RequestMapping("findTechnologyGoodByOrderdetailsId")
	public OrderDetailsFormMap findTechnologyByOrderdetailsIdAndGoodId(String orderdetailsId,String origin) throws Exception {
		OrderDetailsFormMap orderDetailsFormMap1=new OrderDetailsFormMap();
		orderDetailsFormMap1.set("id",orderdetailsId);
		orderDetailsFormMap1.set("origin",origin);
		OrderDetailsFormMap orderDetailsFormMap=orderDetailsMapper.findGoodMesageById(orderDetailsFormMap1);
		if(orderDetailsFormMap==null){
			orderDetailsFormMap=new OrderDetailsFormMap();
		}
		return orderDetailsFormMap;
	}


	@RequestMapping("badgoodStatistics")
	public String badgoodStatistics(Model model) throws Exception {
		model.addAttribute("res", findByRes());
		return Common.BACKGROUND_PATH + "/system/blank/badgoodStatistics";
	}

	@ResponseBody
	@RequestMapping("findBadgoodStatisticsByPage")
	public BlankFormMap findBadgoodStatisticsByPage(String content,String state,String starttime,String endtime,String userId) throws Exception {
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

		BlankFormMap blankFormMap = getFormMap(BlankFormMap.class);
		blankFormMap.set("content",content);
		blankFormMap.set("userId",userId);
		blankFormMap.set("startTime",starttime);
		blankFormMap.set("endTime",endtime);
		blankFormMap.set("state",state);
		if(page.equals("1")){
			total=blankMapper.findBadMessageGroupCountByOrderDetailsId(blankFormMap);
		}
		blankFormMap=toFormMap(blankFormMap, page, rows,blankFormMap.getStr("orderby"));
		List<BlankFormMap> blankFormMaps=blankMapper.findBadMessageGroupByOrderDetailsId(blankFormMap);
		blankFormMap.set("rows",blankFormMaps);
		blankFormMap.set("total",total);
		return blankFormMap;
	}

	@ResponseBody
	@RequestMapping("findTechnologyByOrderdetailsId")
	@SystemLog(module="生产管理",methods="工艺卡-根据订单明细id获取工艺卡信息")//凡需要处理业务逻辑的.都需要记录操作日志
	public BlankFormMap findTechnologyByOrderdetailsId(String orderdetailsId) throws Exception {
		String origin=getPara("origin");
		BlankFormMap blankFormMap = getFormMap(BlankFormMap.class);
		blankFormMap.set("orderdetailsId",orderdetailsId);
		blankFormMap.set("origin",origin);
		List<BlankFormMap> departmentFormMapList=blankMapper.findByByOrderdetailsId(blankFormMap);
		blankFormMap.set("rows",departmentFormMapList);
		return blankFormMap;
	}

	@ResponseBody
	@RequestMapping("findUnCompleteProcess")
	public BlankFormMap findUnCompleteProcess(String clientId,
											  String contractNumber,
											  String mapNumber,
											  String processId,
											  String userName,
											  String state) throws Exception {
		String page=getPara("page");
		String rows=getPara("rows");
		BlankFormMap blankFormMap = getFormMap(BlankFormMap.class);
		WorkersubmitFormMap workersubmitFormMap=new WorkersubmitFormMap();

		workersubmitFormMap.set("state",state);

		if(clientId.equals("不限")){
			clientId="";
		}
		workersubmitFormMap.set("clientId",clientId);


		workersubmitFormMap.set("contractNumber",contractNumber);
		workersubmitFormMap.set("userName",userName);
		workersubmitFormMap.set("mapNumber",mapNumber);
		if(processId.equals("不限")){
			processId="";
		}
		workersubmitFormMap.set("processId",processId);
		if(page.equals("1")){
			total=workersubmitMapper.findCountByWorkerSubmitIdByClientAndOrderAndGoodAndProcessAndUserAndState(workersubmitFormMap);
		}
		workersubmitFormMap=toFormMap(workersubmitFormMap, page, rows,workersubmitFormMap.getStr("orderby"));
		List<WorkersubmitFormMap> workersubmitFormMaps=workersubmitMapper.findByWorkerSubmitIdByClientAndOrderAndGoodAndProcessAndUserAndState(workersubmitFormMap);
		blankFormMap.set("rows",workersubmitFormMaps);
		blankFormMap.set("total",total);
		return blankFormMap;
	}



	@ResponseBody
	@RequestMapping("findTechnologyByClientOrderGoodProcess")
	@SystemLog(module="生产管理",methods="接收码打印-获取列表内容")//凡需要处理业务逻辑的.都需要记录操作日志
	public BlankFormMap findTechnologyByClientOrderGoodProcess(String clientId,
															   String code,
															   String contractNumber,
															   String mapNumber,
															   String startTime,
															   String endTime,
															   String blankSize,
															   String materialQuality,
															   String goodName,
															   String processId,String state) throws Exception {
		String page=getPara("page");
		String rows=getPara("rows");

		String origin=getPara("origin");
		BlankFormMap blankFormMap = getFormMap(BlankFormMap.class);
		blankFormMap.set("state",state);
		if(endTime!=null&&!endTime.equals("")){
			endTime=ToolCommon.addDay(endTime,1);
		}
		if(clientId.equals("不限")){
			clientId="";
		}
		if(goodName==null||goodName.equals("")||goodName.equals("不限")){
			goodName="";
		}

		if(code==null||code.equals("")||code.equals("不限")){
			code="";
		}
		if(materialQuality==null||materialQuality.equals("")){
			materialQuality="";
		}
		blankFormMap.set("code",code);
		blankFormMap.set("materialQuality",materialQuality);
		blankFormMap.set("blankSize",blankSize);
		blankFormMap.set("goodName",goodName);

		blankFormMap.set("clientId",clientId);
		blankFormMap.set("startTime",startTime);
		blankFormMap.set("endTime",endTime);

		blankFormMap.set("origin",origin);
		blankFormMap.set("contractNumber",contractNumber);
		blankFormMap.set("mapNumber",mapNumber);
		if(processId.equals("不限")){
			processId="";
		}
		blankFormMap.set("processId",processId);
		if(page.equals("1")){
			total=blankMapper.findCountTechnologyByClientOrderGoodProcessState(blankFormMap);
		}
		blankFormMap=toFormMap(blankFormMap, page, rows,blankFormMap.getStr("orderby"));
		List<BlankFormMap> departmentFormMapList=blankMapper.findTechnologyByClientOrderGoodProcessState(blankFormMap);
		blankFormMap.set("rows",departmentFormMapList);
		blankFormMap.set("total",total);
		return blankFormMap;
	}

	@ResponseBody
	@RequestMapping("findWorkersubmitByBlankprocessId")
	public WorkersubmitFormMap findWorkersubmitByBlankprocessId(String blankprocessId) throws Exception {
		WorkersubmitFormMap blankFormMap = getFormMap(WorkersubmitFormMap.class);
		List<WorkersubmitFormMap> departmentFormMapList=workersubmitMapper.findByBlankprocessId(blankprocessId);
		blankFormMap.set("rows",departmentFormMapList);
		return blankFormMap;
	}

	@ResponseBody
	@RequestMapping("materialQualitySelect")
	public List<ComboboxEntity>  materialQualitySelect() throws Exception {
		List<BlankFormMap> blankFormMaps=blankMapper.findMaterialQualityFromBlank();
		List<ComboboxEntity> comboboxEntityList=new ArrayList<>();
		for(int i=0;i<blankFormMaps.size();i++){
			BlankFormMap roleFormMap1=blankFormMaps.get(i);
			if(roleFormMap1!=null){
				ComboboxEntity entity=new ComboboxEntity();
				entity.id=roleFormMap1.get("materialQuality")+"";
				entity.text=roleFormMap1.getStr("materialQuality");
				comboboxEntityList.add(entity);
			}

		}
		ComboboxEntity entity=new ComboboxEntity();
		entity.id="不限";
		entity.text="不限";
		comboboxEntityList.add(entity);
		return comboboxEntityList;
	}

	@ResponseBody
	@RequestMapping("materialQualityContainIdSelect")
	public List<ComboboxEntity>  materialQualityContainIdSelect() throws Exception {
		List<BlankFormMap> blankFormMaps=blankMapper.findMaterialQualityFromBlank();
		List<ComboboxEntity> comboboxEntityList=new ArrayList<>();
		for(int i=0;i<blankFormMaps.size();i++){
			BlankFormMap roleFormMap1=blankFormMaps.get(i);
			if(roleFormMap1!=null){
				ComboboxEntity entity=new ComboboxEntity();
				entity.id=roleFormMap1.get("materialQualityId")+"";
				entity.text=roleFormMap1.getStr("materialQuality");
				comboboxEntityList.add(entity);
			}

		}
		ComboboxEntity entity=new ComboboxEntity();
		entity.id="不限";
		entity.text="不限";
		comboboxEntityList.add(entity);
		return comboboxEntityList;
	}

	@ResponseBody
	@RequestMapping("isFinishSelect")
	public List<ComboboxEntity>  isFinishSelect() throws Exception {
		List<BlankFormMap> blankFormMaps=blankMapper.findIsFinishFromBlank();
		List<ComboboxEntity> comboboxEntityList=new ArrayList<>();
		for(int i=0;i<blankFormMaps.size();i++){
			BlankFormMap roleFormMap1=blankFormMaps.get(i);
			if(roleFormMap1!=null){
				ComboboxEntity entity=new ComboboxEntity();
				entity.id=roleFormMap1.get("isFinish")+"";
				entity.text=roleFormMap1.getStr("isFinish");
				comboboxEntityList.add(entity);
			}

		}
		ComboboxEntity entity=new ComboboxEntity();
		entity.id="不限";
		entity.text="不限";
		comboboxEntityList.add(entity);
		return comboboxEntityList;
	}

	@ResponseBody
	@RequestMapping("codeSelect")
	public List<ComboboxEntity>  codeSelect() throws Exception {
		List<BlankFormMap> blankFormMaps=blankMapper.findCodeFromBlank();
		List<ComboboxEntity> comboboxEntityList=new ArrayList<>();
		for(int i=0;i<blankFormMaps.size();i++){
			BlankFormMap roleFormMap1=blankFormMaps.get(i);
			if(roleFormMap1!=null){
				ComboboxEntity entity=new ComboboxEntity();
				String code=roleFormMap1.get("code")+"";
				entity.id=code;
				entity.text=code;
				comboboxEntityList.add(entity);
			}

		}
		ComboboxEntity entity=new ComboboxEntity();
		entity.id="不限";
		entity.text="不限";
		comboboxEntityList.add(entity);
		return comboboxEntityList;
	}

	@ResponseBody
	@RequestMapping("printTimeSelect")
	public List<ComboboxEntity>  printTimeSelect() throws Exception {
		List<BlankFormMap> blankFormMaps=blankMapper.findPrintTimeFromBlank();
		List<ComboboxEntity> comboboxEntityList=new ArrayList<>();
		for(int i=0;i<blankFormMaps.size();i++){
			BlankFormMap roleFormMap1=blankFormMaps.get(i);
			if(roleFormMap1!=null){
				ComboboxEntity entity=new ComboboxEntity();
				String printTime=roleFormMap1.get("printTime")+"";
				if(printTime==null||printTime.equals("")){
					printTime="未打印";
				}
				entity.id=printTime;
				entity.text=printTime;
				comboboxEntityList.add(entity);
			}

		}
		ComboboxEntity entity=new ComboboxEntity();
		entity.id="不限";
		entity.text="不限";
		comboboxEntityList.add(entity);
		return comboboxEntityList;
	}

	@RequestMapping("codeUI")
	public String codeUI(Model model,String code) throws Exception {
		code=code+"时间"+ToolCommon.getNowTime();
		String binary = QrCodeUtils.creatRrCode(code, 200,200);
		model.addAttribute("img",binary);
		return Common.BACKGROUND_PATH + "/system/blank/codeShow";
	}

	@RequestMapping("showBigUI")
	public String showBigShowUI(Model model) throws Exception {
		return Common.BACKGROUND_PATH + "/system/blank/showBig";
	}

	@RequestMapping("showSmallUI")
	public String showSmallUI(Model model) throws Exception {
		return Common.BACKGROUND_PATH + "/system/blank/showSmall";
	}

	@RequestMapping("showSmallUI2")
	public String showSmallUI2(Model model,String origin) throws Exception {
		model.addAttribute("origin",origin);
		return Common.BACKGROUND_PATH + "/system/blank/showSmall2";
	}

	@RequestMapping("codesUI")
	@SystemLog(module="生产二维码打印",methods="生产二维码打印")
	public String codesUI(Model model,String entity) throws Exception {
		String codes=getPara("codes");
		codes=codes+"时间"+ToolCommon.getNowTime();
		String binary = QrCodeUtils.creatRrCode(codes, 200,200);
		model.addAttribute("img",binary);
		return Common.BACKGROUND_PATH + "/system/blank/codesShow";
	}
	public class CodeEntity{
		public String showCode;
		public String binary;

		public String getShowCode() {
			return showCode;
		}

		public void setShowCode(String showCode) {
			this.showCode = showCode;
		}

		public String getBinary() {
			return binary;
		}

		public void setBinary(String binary) {
			this.binary = binary;
		}
	}

	@RequestMapping("unqualifiedOrderUI")
	public String unqualifiedOrderUI(Model model,String orderdetailsId,String workersubmitId) throws Exception {
		OrderDetailsFormMap orderDetailsFormMap=orderDetailsMapper.getOrderAndGoodByOrderDetailsId(orderdetailsId);
		WorkersubmitFormMap workersubmitFormMap=workersubmitMapper.findById(workersubmitId);
		model.addAttribute("orderdetails",orderDetailsFormMap);
		model.addAttribute("workersubmitFormMap",workersubmitFormMap);
		UnqualifiedFormMap unqualifiedFormMap=unqualifiedMapper.findbyFrist("workersubmitId",workersubmitId,UnqualifiedFormMap.class);
		UserFormMap userFormMap=getNowUserMessage();
		model.addAttribute("unqualifiedFormMap",unqualifiedFormMap);
		model.addAttribute("userFormMap",userFormMap);
		model.addAttribute("nowDay",ToolCommon.getNowDay());
		return Common.BACKGROUND_PATH + "/system/blank/unqualifiedOrderPrintShow";
	}

	@RequestMapping("badnoticeOrderUI")
	public String badnoticeOrderUI(Model model,String orderdetailsId,String workersubmitId) throws Exception {
		OrderDetailsFormMap orderDetailsFormMap=orderDetailsMapper.getOrderAndGoodByOrderDetailsId(orderdetailsId);
		WorkersubmitFormMap workersubmitFormMap=workersubmitMapper.findById(workersubmitId);
		model.addAttribute("orderdetails",orderDetailsFormMap);
		model.addAttribute("workersubmitFormMap",workersubmitFormMap);
		BadnoticeFormMap badnoticeFormMap=badnoticeMapper.findbyFrist("workersubmitId",workersubmitId,BadnoticeFormMap.class);
		UserFormMap userFormMap=getNowUserMessage();
		model.addAttribute("badnoticeFormMap",badnoticeFormMap);
		model.addAttribute("userFormMap",userFormMap);
		model.addAttribute("nowDay",ToolCommon.getNowDay());
		return Common.BACKGROUND_PATH + "/system/blank/badnoticeOrderPrintShow";
	}

	@RequestMapping("badprintorderShowUI")
	public String badprintorderShowUI(Model model,String origin,String orderdetailsId,String workersubmitId) throws Exception {
		model.addAttribute("orderdetailsId",orderdetailsId);
		model.addAttribute("workersubmitId",workersubmitId);
		if(origin.equals("废品通知单")){
			BadnoticeFormMap entity=badnoticeMapper.findbyFrist("workersubmitId",workersubmitId,BadnoticeFormMap.class);
			model.addAttribute("entity",entity);
			return Common.BACKGROUND_PATH + "/system/blank/badnoticeOrderPrintInput";
		}else if(origin.equals("产品不合格单")){
			UnqualifiedFormMap entity=unqualifiedMapper.findbyFrist("workersubmitId",workersubmitId,UnqualifiedFormMap.class);
			model.addAttribute("entity",entity);
			return Common.BACKGROUND_PATH + "/system/blank/unqualifiedOrderPrintInput";
		}else{
			ScrapFormMap entity=scrapMapper.findbyFrist("workersubmitId",workersubmitId,ScrapFormMap.class);
			model.addAttribute("entity",entity);
			return Common.BACKGROUND_PATH + "/system/blank/scrapOrderPrintInput";
		}
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
			unqualifiedFormMap.set("userId",userId);
			if(id==null||id.equals("null")){
				unqualifiedMapper.addEntity(unqualifiedFormMap);
			}else{
				unqualifiedMapper.editEntity(unqualifiedFormMap);
			}
		}else if(origin.equals("外协废品单")){
			ScrapFormMap scrapFormMap=getFormMap(ScrapFormMap.class);
			String id=scrapFormMap.get("id")+"";
			scrapFormMap.set("userId",userId);
			if(id==null||id.equals("null")){
				scrapMapper.addEntity(scrapFormMap);
			}else{
				scrapMapper.editEntity(scrapFormMap);
			}
		}else if(origin.equals("废品通知单")){
			BadnoticeFormMap badnoticeFormMap=getFormMap(BadnoticeFormMap.class);
			String id=badnoticeFormMap.get("id")+"";
			badnoticeFormMap.set("userId",userId);
			if(id==null||id.equals("null")){
				badnoticeMapper.addEntity(badnoticeFormMap);
			}else{
				badnoticeMapper.editEntity(badnoticeFormMap);
			}
		}
		result="success";
		return result;
	}

	@RequestMapping("blankorderPrintUI")
	@SystemLog(module="生产管理",methods="打印下料单")//凡需要处理业务逻辑的.都需要记录操作日志
	public String blankorderPrintUI(Model model,String rows,String ids) throws Exception {
		String submiterId="438";
		List<BlankFormMap> list=new ArrayList<>();
		ids=ids.substring(0,ids.length()-1);
       String[] idsStr=ids.split(",");
       String codes="";
       String error="";
		String code="";
       for(int i=0;i<idsStr.length;i++){
       	String id=idsStr[i];
		   BlankFormMap entity = blankMapper.findById(id);
		   if(i==0){
			   code=entity.get("code")+"";
		   }
		   String isFinish=entity.get("isFinish")+"";
		   if(isFinish==null||isFinish.equals("")||(!ToolCommon.isContain(isFinish,"已接收未完成")&&!ToolCommon.isContain(isFinish,"已完成"))){
			   String blankId=entity.get("id")+"";
			   BlankFormMap blankFormMap=blankMapper.findXialiaoCodeByBlankId(blankId);

			   if(blankFormMap!=null){
				   entity.set("isFinish","已接收未完成");
				   blankMapper.editEntity(entity);
				   String blankprocessId=blankFormMap.get("blankprocessId")+"";
				   BlankProcessFormMap blankProcessFormMap=blankProcessMapper.findById(blankprocessId);
				   blankProcessFormMap.set("state","已接收未完成");
				   blankProcessFormMap.set("unreceiveAmount","0");
				   blankProcessMapper.editEntity(blankProcessFormMap);
				   String amount=blankFormMap.get("amount")+"";
				   WorkersubmitFormMap workersubmitFormMap=new WorkersubmitFormMap();
				   workersubmitFormMap.set("blankprocessId",blankprocessId);
				   workersubmitFormMap.set("submiterId",submiterId);
				   workersubmitFormMap.set("amount",amount);
				   workersubmitFormMap.set("startTime",ToolCommon.getNowTime());
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
				   workersubmitFormMap.set("planneedDay", neesDayInt);
				   double distributionWages=ToolCommon.StringToInteger(amount)*artificialF;
				   workersubmitFormMap.set("distributionWages",distributionWages);
				   workersubmitMapper.addEntity(workersubmitFormMap);
				   String workersubmitId=workersubmitFormMap.get("id")+"";
				   codes = codes + blankFormMap.get("endQRCode") + "workersubmitId" + workersubmitId+"blankId"+blankId+",";
			   }else{
				   String mapNumber=entity.get("mapNumber")+"不存在下料工序";
				   error=error+mapNumber+",";
			   }
		   }else{
			   String mapNumber=entity.get("mapNumber")+"已下料";
			   error=error+mapNumber+",";
		   }

		   list.add(entity);

	   }
		codes=codes+"时间"+ToolCommon.getNowTime();
		String binary = QrCodeUtils.creatRrCode(codes, 120,120);
		model.addAttribute("img",binary);
		model.addAttribute("code",code);
		model.addAttribute("ids",ids);
		model.addAttribute("error",error);
        model.addAttribute("rows",list);
		model.addAttribute("companyName",getCompanyName());
        model.addAttribute("nowDay",ToolCommon.getNowTime());
		return Common.BACKGROUND_PATH + "/system/blank/blankOrderPrintShow";
	}
	@RequestMapping("distributionOrderPrintUI")
	@SystemLog(module="生产管理",methods="接收码打印-打印派工单")//凡需要处理业务逻辑的.都需要记录操作日志
	public String distributionOrderPrintUI(Model model) throws Exception {
		String ids=getPara("ids");
		String submiterId=getPara("submiterId");
		ids=ids.substring(0,ids.length()-1);
		BlankProcessFormMap blankProcessFormMap=new BlankProcessFormMap();
		blankProcessFormMap.set("ids",ids);
		List<BlankProcessFormMap> rows=blankProcessMapper.findDistributionPrintByIds(blankProcessFormMap);
		UserFormMap userFormMap=userMapper.findbyFrist("id",submiterId,UserFormMap.class);
		String userName=userFormMap.get("userName")+"";
		String codes="";
		for(int i=0;i<rows.size();i++){
			BlankProcessFormMap blankProcessFormMap1=rows.get(i);
			String blankprocessId=blankProcessFormMap1.get("id")+"";

			String amount=blankProcessFormMap1.get("nowreceiveAmount")+"";
			String endQRCode=blankProcessFormMap1.get("endQRCode")+"";
			String workersubmitId=blankProcessFormMap1.get("workersubmitId")+"";
			codes = codes + endQRCode + "workersubmitId" + workersubmitId+",";
		}
		codes=codes+"时间"+ToolCommon.getNowTime();
		String binary = QrCodeUtils.creatRrCode(codes, 120,120);
		model.addAttribute("img",binary);
		model.addAttribute("userName",userName);
		model.addAttribute("rows",rows);
		model.addAttribute("companyName",getCompanyName());
		model.addAttribute("nowDay",ToolCommon.getNowTime());
		return Common.BACKGROUND_PATH + "/system/blank/distributionOrderPrintShow";
	}

	@RequestMapping("blankconditioningOrderPrintUI")
	@SystemLog(module="生产管理",methods="打印调质单")//凡需要处理业务逻辑的.都需要记录操作日志
	public String blankconditioningOrderPrintUI(Model model,String rows,String ids) throws Exception {
		String submiterId="509";//打印调质单——热调质——0004窦师傅；
		List<BlankFormMap> list=new ArrayList<>();
		ids=ids.substring(0,ids.length()-1);
		String[] idsStr=ids.split(",");
		String codes="";
		String error="";
		String code="";
		float weightF=0;
		for(int i=0;i<idsStr.length;i++){
			String id=idsStr[i];
			BlankFormMap entity = blankMapper.findById(id);
			String weight=entity.get("weight")+"";
			weightF=weightF+ToolCommon.StringToFloat(weight);
			if(i==0){
				code=entity.get("code")+"";
			}
			String isConditioning=entity.get("isConditioning")+"";
			if(isConditioning==null||isConditioning.equals("")||(!ToolCommon.isContain(isConditioning,"已接收未完成")&&!ToolCommon.isContain(isConditioning,"已完成"))){
				String blankId=entity.get("id")+"";
				BlankFormMap blankFormMap=blankMapper.findRetiaozhiCodeByBlankId(blankId);

				if(blankFormMap!=null){
					entity.set("isConditioning","已接收未完成");
					blankMapper.editEntity(entity);
					String blankprocessId=blankFormMap.get("blankprocessId")+"";
					BlankProcessFormMap blankProcessFormMap=blankProcessMapper.findById(blankprocessId);
					blankProcessFormMap.set("state","已接收未完成");
					blankProcessFormMap.set("unreceiveAmount","0");
					blankProcessMapper.editEntity(blankProcessFormMap);
					String amount=blankFormMap.get("amount")+"";
					WorkersubmitFormMap workersubmitFormMap=new WorkersubmitFormMap();
					workersubmitFormMap.set("blankprocessId",blankprocessId);
					workersubmitFormMap.set("submiterId",submiterId);
					workersubmitFormMap.set("amount",amount);
					workersubmitFormMap.set("startTime",ToolCommon.getNowTime());
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
					workersubmitFormMap.set("planneedDay", neesDayInt);
					double distributionWages=ToolCommon.StringToInteger(amount)*artificialF;
					workersubmitFormMap.set("distributionWages",distributionWages);
					workersubmitMapper.addEntity(workersubmitFormMap);
					String workersubmitId=workersubmitFormMap.get("id")+"";
					codes = codes + blankFormMap.get("endQRCode") + "workersubmitId" + workersubmitId+"blankId"+blankId+",";
				}else{
					String mapNumber=entity.get("mapNumber")+"不存在热调质工序";
					error=error+mapNumber+",";
				}
			}else{
				String mapNumber=entity.get("mapNumber")+"已热调质";
				error=error+mapNumber+",";
			}

			list.add(entity);

		}
		codes=codes+"时间"+ToolCommon.getNowTime();
		String binary = QrCodeUtils.creatRrCode(codes, 120,120);
		model.addAttribute("img",binary);
		model.addAttribute("code",code);
		model.addAttribute("ids",ids);
		model.addAttribute("error",error);
		model.addAttribute("rows",list);
		model.addAttribute("companyName",getCompanyName());
		model.addAttribute("nowDay",ToolCommon.getNowTime());
		model.addAttribute("weightAll",ToolCommon.FloatTo4Float(weightF));
		return Common.BACKGROUND_PATH + "/system/blank/blankconditioningOrderPrintShow";
	}

	@RequestMapping("blanknormalizingOrderPrintUI")
	@SystemLog(module="生产管理",methods="打印正火单")//凡需要处理业务逻辑的.都需要记录操作日志
	public String blanknormalizingOrderPrintUI(Model model,String rows,String ids) throws Exception {
		String submiterId="437";//打印正火单——热正火——1007李建吾；
		List<BlankFormMap> list=new ArrayList<>();
		ids=ids.substring(0,ids.length()-1);
		String[] idsStr=ids.split(",");
		String codes="";
		String error="";
		String code="";
		float weightF=0;
		for(int i=0;i<idsStr.length;i++){
			String id=idsStr[i];
			BlankFormMap entity = blankMapper.findById(id);
			String weight=entity.get("weight")+"";
			weightF=weightF+ToolCommon.StringToFloat(weight);
			if(i==0){
				code=entity.get("code")+"";
			}
			String isNormalizing=entity.get("isNormalizing")+"";
			if(isNormalizing==null||isNormalizing.equals("")||(!ToolCommon.isContain(isNormalizing,"已接收未完成")&&!ToolCommon.isContain(isNormalizing,"已完成"))){
				String blankId=entity.get("id")+"";
				BlankFormMap blankFormMap=blankMapper.findRezhenghuoCodeByBlankId(blankId);

				if(blankFormMap!=null){
					entity.set("isNormalizing","已接收未完成");
					blankMapper.editEntity(entity);
					String blankprocessId=blankFormMap.get("blankprocessId")+"";
					BlankProcessFormMap blankProcessFormMap=blankProcessMapper.findById(blankprocessId);
					blankProcessFormMap.set("state","已接收未完成");
					blankProcessFormMap.set("unreceiveAmount","0");
					blankProcessMapper.editEntity(blankProcessFormMap);
					String amount=blankFormMap.get("amount")+"";
					WorkersubmitFormMap workersubmitFormMap=new WorkersubmitFormMap();
					workersubmitFormMap.set("blankprocessId",blankprocessId);
					workersubmitFormMap.set("submiterId",submiterId);
					workersubmitFormMap.set("amount",amount);
					workersubmitFormMap.set("startTime",ToolCommon.getNowTime());
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
					workersubmitFormMap.set("planneedDay", neesDayInt);
					double distributionWages=ToolCommon.StringToInteger(amount)*artificialF;
					workersubmitFormMap.set("distributionWages",distributionWages);
					workersubmitMapper.addEntity(workersubmitFormMap);
					String workersubmitId=workersubmitFormMap.get("id")+"";
					codes = codes + blankFormMap.get("endQRCode") + "workersubmitId" + workersubmitId+"blankId"+blankId+",";
				}else{
					String mapNumber=entity.get("mapNumber")+"不存在热正火工序";
					error=error+mapNumber+",";
				}
			}else{
				String mapNumber=entity.get("mapNumber")+"已热正火";
				error=error+mapNumber+",";
			}

			list.add(entity);

		}
		codes=codes+"时间"+ToolCommon.getNowTime();
		String binary = QrCodeUtils.creatRrCode(codes, 120,120);
		model.addAttribute("img",binary);
		model.addAttribute("code",code);
		model.addAttribute("ids",ids);
		model.addAttribute("error",error);
		model.addAttribute("rows",list);
		model.addAttribute("companyName",getCompanyName());
		model.addAttribute("nowDay",ToolCommon.getNowTime());
		model.addAttribute("weightAll",ToolCommon.FloatTo4Float(weightF));
		return Common.BACKGROUND_PATH + "/system/blank/blanknormalizingOrderPrintShow";
	}

	@ResponseBody
	@RequestMapping("findXialiaoStartCodeByBlankId")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="生产管理",methods="下料单-根据blankid获取下料工序startQRCode")//凡需要处理业务逻辑的.都需要记录操作日志
	public String findXialiaoStartCodeByBlankId(String blankId) throws Exception {
		BlankFormMap blankFormMap=blankMapper.findXialiaoStartCodeByBlankId(blankId);
		return blankFormMap.get("startQRCode")+"";
	}

	@RequestMapping("ticketPrintUI")
	public String ticketPrintUI(Model model,String rows,String ids) throws Exception {

		List<BlankFormMap> list=new ArrayList<>();
		List<NameListEntity> listList=new ArrayList<>();
		ids=ids.substring(0,ids.length()-1);
		String[] idsStr=ids.split(",");
		String code="";
		for(int i=0;i<idsStr.length;i++){
			String id=idsStr[i];
			BlankFormMap entity = blankMapper.findById(id);
			if(i==0){
				code=entity.get("code")+"";
			}
			list.add(entity);
		}
		//多少个一组
		int groupNum = 6;

		//当前游标
		int current = 0;
		while (current < list.size()) {
			NameListEntity nameListEntity=new NameListEntity();
			nameListEntity.name=current+"";
			nameListEntity.list=new ArrayList<>(list.subList(current, Math.min((current + groupNum), list.size())));
			listList.add(nameListEntity);
			current += groupNum;
		}

		model.addAttribute("ids",ids);
		model.addAttribute("code",code);
		model.addAttribute("rows",listList);
		model.addAttribute("nowDay",ToolCommon.getNowTime());
		return Common.BACKGROUND_PATH + "/system/blank/ticketPrintShow";
	}

	public class NameListEntity{
		public String name;
		public List<BlankFormMap> list;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public List<BlankFormMap> getList() {
			return list;
		}

		public void setList(List<BlankFormMap> list) {
			this.list = list;
		}
	}

	// 方式1: 降序排列
	public List<BlankFormMap> desc(List<BlankFormMap> logsList){
		Collections.sort(logsList, new Comparator<BlankFormMap>() {
			@Override
			public int compare(BlankFormMap o1, BlankFormMap o2) {
				if ((o1.getStr("deliveryTime").compareTo(o2.getStr("deliveryTime"))>0)){
					return -1;
				}
				if ((o1.getStr("deliveryTime").compareTo(o2.getStr("deliveryTime"))==0)){
					return 0;
				}
				return 1;
			}
		});
		return logsList;
	}

	// 方式2: 升序排列
	public List<BlankFormMap> asc(List<BlankFormMap> logsList){
		Collections.sort(logsList, new Comparator<BlankFormMap>() {
			@Override
			public int compare(BlankFormMap o1, BlankFormMap o2) {
				if ((o1.getStr("deliveryTime").compareTo(o2.getStr("deliveryTime"))>0)){
					return 1;
				}
				if ((o1.getStr("deliveryTime").equals(o2.getStr("deliveryTime")))){
					return 0;
				}
				return -1;
			}
		});
		return logsList;
	}

	@ResponseBody
	@RequestMapping("findByPage")
	@SystemLog(module="生产管理",methods="下料单-获取列表内容")
	public BlankFormMap findByPage(String content,
								   String printTime,
								   String code,
								   String goodId,
								   String materialQuality,
								   String goodName,
								   String blankSize,
								   String goodSize,
								   String clientId,
								   String makeTimestart,
								   String deliveryTimestart,
								   String makeTimeend,
								   String deliveryTimeend,
								   String orderId,
								   String isFinish,
								   String state) throws Exception {
		String page=getPara("page");
		String rows=getPara("rows");
		String sort=getPara("sort");
		String order=getPara("order");
		blankExportAllsort=sort;
		blankExportAllorder=order;

//		if(sort==null){
//			sort="blankSize";
//			order="asc";
//		}
		BlankFormMap blankFormMap = getFormMap(BlankFormMap.class);
		if(content==null){
			content="";
		}
		if(state==null||state.equals("")||state.equals("不限")){
			state="";
		}
		if(code==null||code.equals("")||code.equals("不限")){
			code="";
		}else{
			String[] codes=code.split(",");
			code="";
			for(int j=0;j<codes.length;j++){
				if(j==(codes.length-1)){
					code=code+"'"+codes[j]+"'";
				}else{
					code=code+"'"+codes[j]+"',";
				}

			}
		}
		if(goodName==null||goodName.equals("")||goodName.equals("不限")){
			goodName="";
		}
		if(clientId==null||clientId.equals("")){
			clientId="不限";
		}else{
			String[] clientIds=clientId.split(",");
			clientId="";
			for(int j=0;j<clientIds.length;j++){
				if(j==(clientIds.length-1)){
					clientId=clientId+"'"+clientIds[j]+"'";
				}else{
					clientId=clientId+"'"+clientIds[j]+"',";
				}

			}
		}
		if(blankSize==null||blankSize.equals("")){
			blankSize="不限";
		}
		if(goodSize==null||goodSize.equals("")){
			goodSize="不限";
		}
		if(printTime==null||printTime.equals("")){
			printTime="不限";
		}
		if(materialQuality==null||materialQuality.equals("")){
			materialQuality="";
		}
		blankFormMap.set("goodName",goodName);
		blankFormMap.set("isFinish",isFinish);
		blankFormMap.set("code",code);
		blankFormMap.set("blankSize",blankSize);
		blankFormMap.set("goodSize",goodSize);
		blankFormMap.set("content",content);
		blankFormMap.set("origin","订单");
		blankFormMap.set("printTime",printTime);
		blankFormMap.set("orderId",orderId);
		blankFormMap.set("state",state);
		blankFormMap.set("materialQuality",materialQuality);
		blankFormMap.set("clientId",clientId);
		blankFormMap.set("goodId",goodId);
		blankFormMap.set("makeTimestart",makeTimestart);
		blankFormMap.set("makeTimeend",makeTimeend);
		blankFormMap.set("deliveryTimestart",deliveryTimestart);
		blankFormMap.set("deliveryTimeend",deliveryTimeend);
		BlankFormMap blankFormMapSum=blankMapper.findSumByAllLike(blankFormMap);
		if(blankFormMapSum==null){
			blankFormMapSum=new BlankFormMap();
		}
		if(page.equals("1")){
			total=blankMapper.findCountByAllLike(blankFormMap);
		}
//		BlankFormMap blankFormMapSum=new BlankFormMap();
//			total=0;
		blankFormMap.set("sort",sort);
		blankFormMap.set("order",order);
		blankFormMap=toFormMap(blankFormMap, page, rows,blankFormMap.getStr("orderby"));
		List<BlankFormMap> departmentFormMapList=blankMapper.findByAllLike(blankFormMap);
//		List<BlankFormMap> departmentFormMapList=new ArrayList<>();
		blankFormMap.set("rows",departmentFormMapList);
		blankFormMap .set("sum",blankFormMapSum);
		blankFormMap.set("total",total);

		return blankFormMap;
	}


	@ResponseBody
	@RequestMapping("findProgressByBlankId")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="调度大屏",methods="根据blankId获取进度")//凡需要处理业务逻辑的.都需要记录操作日志
	public List<BlankFormMap> findProgressByBlankId(String blankId) throws Exception {
		List<BlankFormMap> blankFormMaps=blankMapper.findProgressByBlankId(blankId);
		return blankFormMaps;
	}

	public void setBlankSearchCode(BlankFormMap blankFormMap){
		String code=blankFormMap.get("code")+"";
		if(code==null||code.equals("")||code.equals("null")||code.equals("不限")){
			code="";
		}else{
			String[] codes=code.split(",");
			code="";
			for(int j=0;j<codes.length;j++){
				if(j==(codes.length-1)){
					code=code+"'"+codes[j]+"'";
				}else{
					code=code+"'"+codes[j]+"',";
				}

			}
		}
		blankFormMap.set("code",code);
	}

	@ResponseBody
	@RequestMapping("findClientByPage")
	@SystemLog(module="生产管理",methods="下料单-获取客户列表内容")
	public List findClientByPage() throws Exception {
		BlankFormMap blankFormMap = getFormMap(BlankFormMap.class);
		System.out.print(blankFormMap);
		blankFormMap.set("origin","订单");
		setBlankSearchCode(blankFormMap);
		List<BlankFormMap> clientFormMaps=blankMapper.findClientByAllLike(blankFormMap);
		return clientFormMaps;
	}

	@ResponseBody
	@RequestMapping("findOrderByPage")
	@SystemLog(module="生产管理",methods="下料单-获取订单列表内容")
	public List findOrderByPage(String clientId) throws Exception {
		BlankFormMap blankFormMap =getFormMap(BlankFormMap.class);
		BlankFormMap blankFormMapSearch=new BlankFormMap();
		blankFormMapSearch.set("origin","订单");
		blankFormMapSearch.set("clientId",blankFormMap.get("clientId"));
		setBlankSearchCode(blankFormMap);
		List<BlankFormMap> clientFormMaps=blankMapper.findOrderByAllLike(blankFormMapSearch);
		List<TreeOrderFormMap> treeClientEntities=new ArrayList<>();
		try{
			for(int i=0;i<clientFormMaps.size();i++){
				BlankFormMap clientFormMap=clientFormMaps.get(i);
				if(clientFormMap!=null){
					String orderId=clientFormMap.get("orderId")+"";
					String contractNumber=clientFormMap.getStr("contractNumber");
					TreeOrderFormMap treeOrderFormMap=new TreeOrderFormMap();
					treeOrderFormMap.id=orderId+"订单";
					treeOrderFormMap.text=contractNumber;
					treeOrderFormMap.state="closed";
					List<TreeOrderDetailsFormMap> treeOrderDetailsFormMaps=new ArrayList<>();
					treeOrderFormMap.children=treeOrderDetailsFormMaps;
					treeClientEntities.add(treeOrderFormMap);
				}
			}
			return treeClientEntities;
		}catch (Exception e){
			System.out.print(e);
			return treeClientEntities;
		}

	}

	@ResponseBody
	@RequestMapping("findOrderDetailsByPage")
	@SystemLog(module="生产管理",methods="下料单-获取订单明细内容")
	public List findOrderDetailsByPage(
								String clientId,
								String orderId) throws Exception {
		BlankFormMap blankFormMap = getFormMap(BlankFormMap.class);
		BlankFormMap blankFormMapSearch=new BlankFormMap();
		setBlankSearchCode(blankFormMap);
		blankFormMapSearch.set("origin","订单");
		blankFormMapSearch.set("clientId",blankFormMap.get("clientId")+"");
		blankFormMapSearch.set("orderId",blankFormMap.get("orderId")+"");
		List<BlankFormMap> clientFormMaps=blankMapper.findOrderDetailsByAllLike(blankFormMapSearch);
		List<TreeOrderDetailsFormMap> treeClientEntities=new ArrayList<>();
		try{
			for(int i=0;i<clientFormMaps.size();i++){
				BlankFormMap clientFormMap=clientFormMaps.get(i);
				if(clientFormMap!=null){
					String mapNumber=clientFormMap.getStr("mapNumber");
					String goodId=clientFormMap.get("goodId")+"";
					TreeOrderDetailsFormMap treeOrderFormMap=new TreeOrderDetailsFormMap();
					treeOrderFormMap.id=goodId+"明细";
					treeOrderFormMap.text=mapNumber;
					treeClientEntities.add(treeOrderFormMap);
				}
			}
//			TreeOrderDetailsFormMap treeClientEntity=new TreeOrderDetailsFormMap();
//			treeClientEntity.id="不限";
//			treeClientEntity.text="不限";
//			treeClientEntities.add(treeClientEntity);
			return treeClientEntities;
		}catch (Exception e){
			System.out.print(e);
			return treeClientEntities;
		}

	}

	@RequestMapping(value = "exportAllByContent")
	@ResponseBody
	@SystemLog(module="生产管理",methods="下料单-导出")//凡需要处理业务逻辑的.都需要记录操作日志
	public void exportAllByContent(HttpServletResponse response,String entity) throws Exception {
		String contentS=ToolCommon.json2Object(entity).getString("content");
		String printTime=ToolCommon.json2Object(entity).getString("printTime");
		String code=ToolCommon.json2Object(entity).getString("code");
		String goodId=ToolCommon.json2Object(entity).getString("goodId");
		String materialQuality=ToolCommon.json2Object(entity).getString("materialQuality");
		String goodName=ToolCommon.json2Object(entity).getString("goodName");
		String blankSize=ToolCommon.json2Object(entity).getString("blankSize");
		String goodSize=ToolCommon.json2Object(entity).getString("goodSize");
		String clientId=ToolCommon.json2Object(entity).getString("clientId");
		String makeTimestart=ToolCommon.json2Object(entity).getString("makeTimestart");
		String deliveryTimestart=ToolCommon.json2Object(entity).getString("deliveryTimestart");
		String makeTimeend=ToolCommon.json2Object(entity).getString("makeTimeend");
		String deliveryTimeend=ToolCommon.json2Object(entity).getString("deliveryTimeend");
		String orderId=ToolCommon.json2Object(entity).getString("orderId");
		String state=ToolCommon.json2Object(entity).getString("state");
		BlankFormMap blankFormMap = new BlankFormMap();
		if(contentS==null){
			contentS="";
		}
		if(state==null||state.equals("")||state.equals("不限")){
			state="";
		}
		if(code==null||code.equals("")||code.equals("不限")){
			code="";
		}else{
			String[] codes=code.split(",");
			code="";
			for(int j=0;j<codes.length;j++){
				if(j==(codes.length-1)){
					code=code+"'"+codes[j]+"'";
				}else{
					code=code+"'"+codes[j]+"',";
				}

			}
		}
		if(goodName==null||goodName.equals("")||goodName.equals("不限")){
			goodName="";
		}
		if(clientId==null||clientId.equals("")){
			clientId="不限";
		}
		if(blankSize==null||blankSize.equals("")){
			blankSize="不限";
		}
		if(goodSize==null||goodSize.equals("")){
			goodSize="不限";
		}
		if(printTime==null||printTime.equals("")){
			printTime="不限";
		}
		if(materialQuality==null||materialQuality.equals("")){
			materialQuality="";
		}
		blankFormMap.set("goodName",goodName);
		blankFormMap.set("code",code);
		blankFormMap.set("blankSize",blankSize);
		blankFormMap.set("goodSize",goodSize);
		blankFormMap.set("content",contentS);
		blankFormMap.set("origin","订单");
		blankFormMap.set("printTime",printTime);
		blankFormMap.set("orderId",orderId);
		blankFormMap.set("state",state);
		blankFormMap.set("materialQuality",materialQuality);
		blankFormMap.set("clientId",clientId);
		blankFormMap.set("goodId",goodId);
		blankFormMap.set("makeTimestart",makeTimestart);
		blankFormMap.set("makeTimeend",makeTimeend);
		blankFormMap.set("deliveryTimestart",deliveryTimestart);
		blankFormMap.set("deliveryTimeend",deliveryTimeend);
		List<BlankFormMap> blankFormMaps=blankMapper.findExportByAllLike(blankFormMap);

		//excel标题
		String[] title = {"客户",
				"订单编号",
				"来单日期",
				"产品名称",
				"图号",
				"成品尺寸",
				"订货数量",
				"下料尺寸",
				"下料数量",
				"库存量",
				"交货日期",
				"材质",
				"米数",
				"吨数"};

		//excel文件名
		String fileName = "下料单" + System.currentTimeMillis() + ".xls";

		//sheet名
		String sheetName = "sheet1";

		String [][] content = new String[blankFormMaps.size()][title.length];

		for (int i = 0; i < blankFormMaps.size(); i++) {
			content[i] = new String[title.length];
			BlankFormMap obj = blankFormMaps.get(i);
			content[i][0] = obj.get("clientFullName")+"";
			content[i][1] = obj.get("contractNumber")+"";
			content[i][2] = obj.get("makeTime")+"";
			content[i][3] = obj.get("goodName")+"";
			content[i][4] = obj.get("mapNumber")+"";
			content[i][5] = obj.get("goodSize")+"";
			content[i][6] = obj.get("orderAmount")+"";
			content[i][7] = obj.get("blankSize")+"";
			content[i][8] = obj.get("amount")+"";
			content[i][9] = obj.get("stockAmount")+"";
			content[i][10] = obj.get("deliveryTime")+"";
			content[i][11] = obj.get("materialQualityName")+"";
			content[i][12] = obj.get("length")+"";
			content[i][13] = obj.get("weight")+"";
		}

		//创建HSSFWorkbook
		HSSFWorkbook wb = ExcelUtils.getHSSFWorkbook("下料单",sheetName, title, content, null);

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
	@RequestMapping("codeSelectContainPage")
	public BlankFormMap  codeSelectContainPage(String q) throws Exception {
		String page=getPara("page");

		String rows=getPara("rows");
		BlankFormMap blankFormMap=new BlankFormMap();
		blankFormMap.set("content",q);
		if(page.equals("1")){
			total=blankMapper.findCodeCountFromBlankLike(blankFormMap);
		}
		blankFormMap=toFormMap(blankFormMap, page, rows,blankFormMap.getStr("orderby"));
		List<BlankFormMap> blankFormMaps=blankMapper.findCodeFromBlankLike(blankFormMap);
		BlankFormMap blankFormMap1=new BlankFormMap();
		blankFormMap1.set("total",total);
		blankFormMap1.set("rows",blankFormMaps);
		return blankFormMap1;
	}

	@ResponseBody
	@RequestMapping("progressSearchByPage")
	public ProgressSearchFormMap  progressSearchByPage() throws Exception {
		String page=getPara("page");

		String rows=getPara("rows");
		ProgressSearchFormMap progressSearchFormMap=new ProgressSearchFormMap();
		if(page.equals("1")){
			total=progressSearchMapper.findCountByAllLike(progressSearchFormMap);
		}
		progressSearchFormMap=toFormMap(progressSearchFormMap, page, rows,progressSearchFormMap.getStr("orderby"));
		List<ProgressSearchFormMap> progressSearchFormMaps=progressSearchMapper.findByAllLike(progressSearchFormMap);
		ProgressSearchFormMap progressSearchFormMap1=new ProgressSearchFormMap();
		progressSearchFormMap1.set("total",total);
		progressSearchFormMap1.set("rows",progressSearchFormMaps);
		return progressSearchFormMap1;
	}
	@ResponseBody
	@RequestMapping("deleteProgressSearchEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="进度查询",methods="查询内容-删除组")//凡需要处理业务逻辑的.都需要记录操作日志
	public String deleteProgressSearchEntity() throws Exception {
		SystemconfigFormMap systemconfigFormMap=new SystemconfigFormMap();
		systemconfigFormMap.set("name","deletepassword");
		String content=getPara("password");
		systemconfigFormMap.set("content",content);
		List<SystemconfigFormMap> systemconfigFormMaps=systemconfigMapper.findByNames(systemconfigFormMap);
		if(systemconfigFormMaps.size()>0){
			String ids = getPara("ids");
			ids=ids.substring(0,ids.length()-1);
//			String[] idStr=ids.split(",");
//			for(int i=0;i<idStr.length;i++){
//				ProgressSearchFormMap progressSearchFormMap=new ProgressSearchFormMap();
//				progressSearchFormMap.set("id",idStr[i]);
//				heatTreatMapper.deleteByProgressSearchId(progressSearchFormMap);
//			}
			progressSearchMapper.deleteByIds(ids);
			return "success";
		}else{
			return "删除订单密码错误";
		}

	}

	@ResponseBody
	@RequestMapping("getProgressByContractNumberAndGoodId")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="热处理情况",methods="获取生产进度详细")//凡需要处理业务逻辑的.都需要记录操作日志
	public List<BlankFormMap> getProgressByContractNumberAndGoodId(String contractNumber,String goodId,String deliveryTime) throws Exception {
		List<BlankFormMap> blankFormMaps=new ArrayList<>();
		String orderdetailsId="";
		try{
			orderdetailsId=orderDetailsMapper.findIdByContractNumberAndGoodIdAndDeliveryTime(contractNumber,goodId,deliveryTime);
		}catch (Exception e){
			orderdetailsId="";
		}
		if(ToolCommon.isNull(orderdetailsId)){
			return blankFormMaps;
		}else{
			String blankId=blankMapper.findIdByOrderDetailsId(orderdetailsId);
			if(ToolCommon.isNull(blankId)){
				return blankFormMaps;
			}else{
				BlankFormMap blankFormMap=new BlankFormMap();
				blankFormMap.set("blankId",blankId);
				blankFormMap.set("goodId",goodId);
				blankFormMap.set("blankId",blankId);
				blankFormMap.set("deliveryTime",deliveryTime);
				blankFormMap.set("contractNumber",contractNumber);
				blankFormMaps.add(blankFormMap);
				List<BlankFormMap> blankFormMaps1=getBlankListContainProgress(blankFormMaps);
				return blankFormMaps1;
			}

		}


	}


	@ResponseBody
	@RequestMapping("getSmallProgress")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="小调度2或小调度渗碳",methods="获取生产进度详细")//凡需要处理业务逻辑的.都需要记录操作日志
	public String getSmallProgress(String contractNumber,
								   String materialQuality,
								   String goodId,
								   String hearttreatId,
								   String deliveryTime,
								   String origin) throws Exception {
		BlankFormMap blankFormMap=new BlankFormMap();
		blankFormMap.set("contractNumber",contractNumber);
		blankFormMap.set("materialQuality",materialQuality);
		blankFormMap.set("goodId",goodId);
		blankFormMap.set("hearttreatId",hearttreatId);
		blankFormMap.set("deliveryTime",deliveryTime);
		String process=getSmallProcessStr(blankFormMap,origin);
		return process;


	}


	@ResponseBody
	@RequestMapping("findBigShowByPage")
	@SystemLog(module="调度大屏",methods="获取列表内容")
	public BlankFormMap findBigShowByPage(String clientId,
								   String contractNumber,
										  String materialCode,
										  String code,
								   String state,
								   String goodName,
								   String processId,
								   String startTime,
									String orderId,
									String goodId,
									String endTime,
										  String materialQuality,
										  String blankSize,
										  String goodSize,
								   String mapNumber) throws Exception {
		String page=getPara("page");
		String rows=getPara("rows");
//		rows="1";
		String sort=getPara("sort");
		String order=getPara("order");
		BlankFormMap blankFormMap = getFormMap(BlankFormMap.class);
		if(ToolCommon.isNull(processId)){
			processId="46605";
		}
		blankFormMap.set("clientId",clientId);
		blankFormMap.set("materialCode",materialCode);
		blankFormMap.set("code",code);
		blankFormMap.set("goodId",goodId);
		blankFormMap.set("orderId",orderId);
		blankFormMap.set("contractNumber",contractNumber);
		blankFormMap.set("state",state);
		blankFormMap.set("processId",processId);
		blankFormMap.set("startTime",startTime);
		if(!ToolCommon.isNull(endTime)){
			endTime=ToolCommon.addDay(endTime,1);
		}
		blankFormMap.set("endTime",endTime);
		blankFormMap.set("mapNumber",mapNumber);
		blankFormMap.set("materialQuality",materialQuality);
		blankFormMap.set("blankSize",blankSize);
		blankFormMap.set("goodSize",goodSize);
		blankFormMap.set("goodName",goodName);
		String amount=blankMapper.findAmountBigShowByContent(blankFormMap);
		if(page.equals("1")){
			total=blankMapper.findBigShowCountByContent(blankFormMap);
		}
//		String amount="";
//		total=0;
		blankFormMap=toFormMap(blankFormMap, page, rows,blankFormMap.getStr("orderby"));
		blankFormMap.set("sort",sort);
		blankFormMap.set("order",order);
		List<BlankFormMap> departmentFormMapList=blankMapper.findBigShowByContent(blankFormMap);
//		List<BlankFormMap> departmentFormMapList=new ArrayList<>()
		List<BlankFormMap> blankFormMaps=getBlankListContainProgress(departmentFormMapList);
		blankFormMap.set("rows",blankFormMaps);
		blankFormMap.set("amount",amount);
		blankFormMap.set("total",total);
		return blankFormMap;
	}


	@ResponseBody
	@RequestMapping("findSmallshowByPage")
	@SystemLog(module="小调度",methods="获取列表内容")
	public BlankFormMap findSmallshowByPage(String clientId,
										  String contractNumber,
										  String materialCode,
										  String code,
										  String state,
										  String goodName,
										  String processId,
										  String startTime,
										  String orderId,
										  String goodId,
										  String endTime,
										  String materialQuality,
										  String blankSize,
										  String goodSize,
										  String mapNumber) throws Exception {
		String page=getPara("page");
		String rows=getPara("rows");
//		rows="1";
		String sort=getPara("sort");
		String order=getPara("order");
		BlankFormMap blankFormMap = getFormMap(BlankFormMap.class);
		if(ToolCommon.isNull(processId)){
			processId="46605";
		}
		blankFormMap.set("clientId",clientId);
		blankFormMap.set("materialCode",materialCode);
		blankFormMap.set("code",code);
		blankFormMap.set("goodId",goodId);
		blankFormMap.set("orderId",orderId);
		blankFormMap.set("contractNumber",contractNumber);
		blankFormMap.set("state",state);
		blankFormMap.set("processId",processId);
		blankFormMap.set("startTime",startTime);
		if(!ToolCommon.isNull(endTime)){
			endTime=ToolCommon.addDay(endTime,1);
		}
		blankFormMap.set("endTime",endTime);
		blankFormMap.set("mapNumber",mapNumber);
		blankFormMap.set("materialQuality",materialQuality);
		blankFormMap.set("blankSize",blankSize);
		blankFormMap.set("goodSize",goodSize);
		blankFormMap.set("goodName",goodName);
		String amount=blankMapper.findAmountBigShowByContent(blankFormMap);
		if(page.equals("1")){
			total=blankMapper.findBigShowCountByContent(blankFormMap);
		}
//		String amount="";
//		total=0;
		blankFormMap=toFormMap(blankFormMap, page, rows,blankFormMap.getStr("orderby"));
		blankFormMap.set("sort",sort);
		blankFormMap.set("order",order);
		List<BlankFormMap> departmentFormMapList=blankMapper.findSmallShowByContent(blankFormMap);
//		List<BlankFormMap> departmentFormMapList=new ArrayList<>()
		List<BlankFormMap> blankFormMaps=getSmallBlankListContainProgress(departmentFormMapList);
		blankFormMap.set("rows",blankFormMaps);
		blankFormMap.set("amount",amount);
		blankFormMap.set("total",total);
		return blankFormMap;
	}

	@ResponseBody
	@RequestMapping("findSmallshowByPage2")
	@SystemLog(module="小调度2",methods="获取列表内容")
	public HeatTreatFormMap findSmallshowByPage2(String clientId,
										   String contractNumber,
										   String goodName,
										   String startTime,
												String endTime,
												String startTimePick,
												String endTimePick,
										   String goodId,
										   String materialQuality,
										   String goodSize,
										   String mapNumber,String origin) throws Exception {
		String page=getPara("page");
		String rows=getPara("rows");
//		rows="1";
		String sort=getPara("sort");
		String order=getPara("order");
		HeatTreatFormMap heatTreatFormMap =new HeatTreatFormMap();
		heatTreatFormMap.set("clientId",clientId);
		heatTreatFormMap.set("goodId",goodId);
		heatTreatFormMap.set("contractNumber",contractNumber);
		heatTreatFormMap.set("startTime",startTime);
		if(!ToolCommon.isNull(endTime)){
			endTime=ToolCommon.addDay(endTime,1);
		}
		heatTreatFormMap.set("endTime",endTime);
		heatTreatFormMap.set("startTimePick",startTimePick);
		if(!ToolCommon.isNull(endTimePick)){
			endTimePick=ToolCommon.addDay(endTimePick,1);
		}
		heatTreatFormMap.set("endTimePick",endTimePick);
		heatTreatFormMap.set("mapNumber",mapNumber);
		heatTreatFormMap.set("materialQuality",materialQuality);
		heatTreatFormMap.set("goodSize",goodSize);
		heatTreatFormMap.set("goodName",goodName);
		heatTreatFormMap.set("origin",origin);
		String amount=heatTreatMapper.findDispatchAmountByDispatch(heatTreatFormMap);
		if(page.equals("1")){
			total=heatTreatMapper.findDispatchCountByDispatch(heatTreatFormMap);
		}
//		String amount="";
//		total=0;
		heatTreatFormMap=toFormMap(heatTreatFormMap, page, rows,heatTreatFormMap.getStr("orderby"));
		heatTreatFormMap.set("sort",sort);
		heatTreatFormMap.set("order",order);
		List<HeatTreatFormMap> departmentFormMapList=heatTreatMapper.findDispatchContentByDispatch(heatTreatFormMap);
//		List<HeatTreatFormMap> departmentFormMapList=new ArrayList<>();
		List<FixedProcessFormMap> fixedProcessFormMaps=fixedProcessMapper.findByOrigin(origin);
		List<HeatTreatFormMap> blankFormMaps=getSmallBlankListProgress(departmentFormMapList,fixedProcessFormMaps,origin);
		heatTreatFormMap.set("rows",blankFormMaps);
		heatTreatFormMap.set("total",total);
		heatTreatFormMap.set("amount",amount);
		return heatTreatFormMap;
	}

	@RequestMapping("progressUI")
	@SystemLog(module="进度查询",methods="进入界面")
	public String progressUI(Model model) throws Exception {
		model.addAttribute("res", findByRes());
		return Common.BACKGROUND_PATH + "/system/blank/progressSearch";
	}

	@ResponseBody
	@RequestMapping("uploadExcelSearchProgress")
	@SystemLog(module="进度查询",methods="导入数据查询")
	public String findBigShowByPage(@RequestParam(value = "file", required = false) MultipartFile file,
										  HttpServletRequest request) throws Exception {
		String errorMessage="";
		boolean isAdd=true;
		if(file!=null){
			FileBean fileBean=ToolCommon.uploadExcelFile(file,request);
			Sheet sheet1= ExcelUtil.readRowsAndColumsSheet1(fileBean.getPath());
			for(int i=1;i<=sheet1.getLastRowNum();i++){
				Row row = null;
				row = sheet1.getRow(i);
				int lastColNum=0;
				if(row!=null){
					lastColNum = row.getLastCellNum();
					String contractNumber="";
					String mapNumber="";
					if(lastColNum>0){
						for(int j=0;j<lastColNum;j++){
							Cell cell=row.getCell(j);
							if(cell!=null){
								String value=ExcelUtil.getCellValue(cell);
								if(value==null){
									value="";
								}
								if(j==0){
									if(value.equals("")){
										j=lastColNum;
										isAdd=false;
										i=sheet1.getLastRowNum()+1;
									}else if(value.equals("结束")){
										i=sheet1.getLastRowNum()+1;
										isAdd=false;
									}
								}
								value= ToolExcel.replaceBlank(value);
								switch (j){
									case 0:
										contractNumber=value.replace(" ","");
										contractNumber=contractNumber.replace(",","");
										break;
									case 1:
										mapNumber=value.replace(" ","");
										mapNumber=mapNumber.replace(",","");
										break;
								}
							}
						}
						if(isAdd){
							ProgressSearchFormMap progressSearchFormMap=new ProgressSearchFormMap();
							progressSearchFormMap.set("contractNumberInsert",contractNumber);
							progressSearchFormMap.set("contractNumber",contractNumber);
							progressSearchFormMap.set("mapNumber",mapNumber);
							progressSearchFormMap.set("deliveryTime","");
							int count=progressSearchMapper.findCountByAllLike(progressSearchFormMap);
							if(count==0){
								List<OrderDetailsFormMap> orderDetailsFormMaps=blankMapper.findAmountByContractNumberAndMapNumber(progressSearchFormMap);
								for(int k=0;k<orderDetailsFormMaps.size();k++){
									OrderDetailsFormMap orderDetailsFormMap=orderDetailsFormMaps.get(k);
									String amount=orderDetailsFormMap.get("amount")+"";
									String deliveryTime=orderDetailsFormMap.get("deliveryTime")+"";
									progressSearchFormMap.set("deliveryTime",deliveryTime);
									if(!ToolCommon.isNull(amount)){
										progressSearchFormMap.set("waitDistributionAmount",amount);
										progressSearchFormMap.set("nowDistributionAmount",amount);
									}
									progressSearchFormMap.remove("id");
									progressSearchMapper.addEntity(progressSearchFormMap);
									String id=progressSearchFormMap.get("id")+"";
									HeatTreatFormMap heatTreatFormMap=heatTreatMapper.findByProgressSearchId(id);
									if(heatTreatFormMap==null){
										BlankFormMap blankFormMap=blankMapper.findHeatTreatByProgressSearchId(id);
										addHeartTreatByBlankFormMap("进度查询",blankFormMap,getNowUserMessage().get("id")+"");
									}
								}

							}else{
								errorMessage=errorMessage+"订单号:"+contractNumber+"图号:"+mapNumber+"已存在<br>";
							}
						}
					}else{
						i=sheet1.getLastRowNum()+1;
					}
				}
			}
		}
		if(errorMessage.equals("")){
			return "success";
		}else{
			return errorMessage;
		}
	}




	@ResponseBody
	@RequestMapping("findSearchProgressByPage")
	@SystemLog(module="进度查询",methods="获取数据列表")
	public BlankFormMap findSearchProgressByPage(String goodName,String content,String clientId,String startTime,String endTime) throws Exception {
		String page=getPara("page");
		String rows=getPara("rows");
		String origin=getPara("origin");
		BlankFormMap blankFormMap=new BlankFormMap();
		BlankFormMap blankFormMapS=new BlankFormMap();
		List<BlankFormMap> blankFormMaps=new ArrayList<>();
		ProgressSearchFormMap progressSearchFormMapS=new ProgressSearchFormMap();
		if(origin.equals("检索")){
			progressSearchFormMapS.set("origin",origin);
			progressSearchFormMapS.set("goodName",goodName);
			progressSearchFormMapS.set("content",content);
			progressSearchFormMapS.set("clientId",clientId);
			progressSearchFormMapS.set("goodName",goodName);
			progressSearchFormMapS.set("startTime",startTime);
			if(!ToolCommon.isNull(endTime)){
				endTime=ToolCommon.addDay(endTime,1);
			}
			progressSearchFormMapS.set("endTime",endTime);
		}
		if(page.equals("1")){
			total=progressSearchMapper.findCountByAllLike(progressSearchFormMapS);
		}
		progressSearchFormMapS=toFormMap(progressSearchFormMapS, page, rows,progressSearchFormMapS.getStr("orderby"));
			List<ProgressSearchFormMap> progressSearchFormMaps=progressSearchMapper.findByAllLike(progressSearchFormMapS);
			for(int i=0;i<progressSearchFormMaps.size();i++){
				ProgressSearchFormMap progressSearchFormMap=progressSearchFormMaps.get(i);
				String id=progressSearchFormMap.get("id")+"";
				String contractNumber=progressSearchFormMap.get("contractNumber")+"";
				String mapNumber=progressSearchFormMap.get("mapNumber")+"";
				String deliveryTime=progressSearchFormMap.get("deliveryTime")+"";
				blankFormMapS.set("contractNumber",contractNumber);
				blankFormMapS.set("mapNumber",mapNumber);
				blankFormMapS.set("deliveryTime",deliveryTime);
				List<BlankFormMap> blankFormMaps1=blankMapper.findByProgressSearchId(id);
				if(blankFormMaps1.size()==0){
					if(origin.equals("全部")){
						String progressSearchId=progressSearchFormMap.get("id")+"";
						BlankFormMap blankFormMap1=new BlankFormMap();
						blankFormMap1.set("progressSearchId",progressSearchId);
						blankFormMap1.set("contractNumber",contractNumber);
						blankFormMap1.set("mapNumber",mapNumber);
						blankFormMap1.set("deliveryTime",deliveryTime);
						blankFormMaps1.add(blankFormMap1);
						blankFormMaps.addAll(blankFormMaps1);
					}
				}else{
					List<BlankFormMap> blankFormMaps2=getBlankListContainProgress(blankFormMaps1);
					blankFormMaps.addAll(blankFormMaps2);
				}
		}
		blankFormMap.set("rows",blankFormMaps);
		blankFormMap.set("total",total);
		return blankFormMap;
	}


	public List<BlankFormMap> getBlankListContainProgress(List<BlankFormMap> departmentFormMapList){
		List<BlankFormMap> blankFormMaps=new ArrayList<>();
		for(int i=0;i<departmentFormMapList.size();i++){
			BlankFormMap blankFormMap1=departmentFormMapList.get(i);
			String process="";
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
					List<WorkersubmitHeatTreatFormMap> workersubmitHeatTreatFormMaps=workersubmitHeatTreatMapper.findAmountAndUserByContractNumberAndDeliveryTimeAndGoodIdAndProcessId(
							contractNumberBlank,deliveryTimeBlank,goodIdBlank,processIdBlank
					);
					for(int jj=0;jj<workersubmitHeatTreatFormMaps.size();jj++){
						WorkersubmitHeatTreatFormMap workersubmitHeatTreatFormMap=workersubmitHeatTreatFormMaps.get(jj);
						String getMessage=workersubmitHeatTreatFormMap.get("getMessage")+"";
						String completeMessage=workersubmitHeatTreatFormMap.get("completeMessage")+"";
						String userName=workersubmitHeatTreatFormMap.get("userName")+"";
						if(ToolCommon.isNull(userName)){
							process=getProgress(process,processName,blankProcessId,planneedDay);
						}else{
							if(getMessage.equals("0")&&completeMessage.equals("0")){

							}else{
								if(getMessage.equals("0")){
									process=process+processName+planneedDay+"("+ToolCommon.setContentToGreen(userName+completeMessage)+")→";
								}else if(completeMessage.equals("0")){
									process=process+processName+planneedDay+"("+ToolCommon.setContentToRed(userName+getMessage)+")→";
								}else{
									process=process+processName+planneedDay+"("+ToolCommon.setContentToRed(userName+getMessage)+" "+ToolCommon.setContentToGreen(userName+completeMessage)+")→";
								}
							}
						}

					}
				}else{
					process=getProgress(process,processName,blankProcessId,planneedDay);
				}


			}
			if(process.equals("")){

			}else{
				process= process.substring(0, process.length() - 1);
			}
			blankFormMap1.set("progress",process);
			blankFormMaps.add(blankFormMap1);
		}
		return blankFormMaps;
	}

	public List<BlankFormMap> getSmallBlankListContainProgress(List<BlankFormMap> departmentFormMapList){
		List<BlankFormMap> blankFormMaps=new ArrayList<>();
		for(int i=0;i<departmentFormMapList.size();i++){
			BlankFormMap blankFormMap1=departmentFormMapList.get(i);
			String materialQuality=blankFormMap1.get("materialQuality")+"";

			String process="";
			String goodIdBlank=blankFormMap1.get("goodId")+"";
			String deliveryTimeBlank=blankFormMap1.get("deliveryTime")+"";
			String contractNumberBlank=blankFormMap1.get("contractNumber")+"";
			List<MaterialqualitytypeProcessFormMap> blankFormMaps1=materialqualitytypeProcessMapper.findByMaterialqualitytypeId(materialQuality);
			for(int j=0;j<blankFormMaps1.size();j++){
				MaterialqualitytypeProcessFormMap goodProcessFormMap=blankFormMaps1.get(j);
				String processName=goodProcessFormMap.get("processName")+"";
				String processIdBlank=goodProcessFormMap.get("processId")+"";
					List<WorkersubmitHeatTreatFormMap> workersubmitHeatTreatFormMaps=workersubmitHeatTreatMapper.findAmountAndUserByContractNumberAndDeliveryTimeAndGoodIdAndProcessId(
							contractNumberBlank,deliveryTimeBlank,goodIdBlank,processIdBlank
					);
					if(workersubmitHeatTreatFormMaps.size()==0){
						process=process+processName+"→";
					}else{
						for(int jj=0;jj<workersubmitHeatTreatFormMaps.size();jj++){
							WorkersubmitHeatTreatFormMap workersubmitHeatTreatFormMap=workersubmitHeatTreatFormMaps.get(jj);
							String getMessage=workersubmitHeatTreatFormMap.get("getMessage")+"";
							String completeMessage=workersubmitHeatTreatFormMap.get("completeMessage")+"";
							String userName=workersubmitHeatTreatFormMap.get("userName")+"";
							if(ToolCommon.isNull(userName)){
								process=process+processName;
							}else{
								if(getMessage.equals("0")&&completeMessage.equals("0")){

								}else{
									if(getMessage.equals("0")){
										process=process+processName+"("+ToolCommon.setContentToGreen(userName+completeMessage)+")→";
									}else if(completeMessage.equals("0")){
										process=process+processName+"("+ToolCommon.setContentToRed(userName+getMessage)+")→";
									}else{
										process=process+processName+"("+ToolCommon.setContentToRed(userName+getMessage)+" "+ToolCommon.setContentToGreen(userName+completeMessage)+")→";
									}
								}
							}

						}
					}

			}
			if(process.equals("")){

			}else{
				process= process.substring(0, process.length() - 1);
			}
			blankFormMap1.set("progress",process);
			blankFormMaps.add(blankFormMap1);
		}
		return blankFormMaps;
	}

	public String getSmallProcessStr(BlankFormMap blankFormMap1,String origin){
		String materialQuality=blankFormMap1.get("materialQuality")+"";

		String process="";
		String goodIdBlank=blankFormMap1.get("goodId")+"";
		String hearttreatId=blankFormMap1.get("hearttreatId")+"";
		String deliveryTimeBlank=blankFormMap1.get("deliveryTime")+"";
		String contractNumberBlank=blankFormMap1.get("contractNumber")+"";
		if(origin.equals("正火调质")){
			List<MaterialqualitytypeProcessFormMap> blankFormMaps1=materialqualitytypeProcessMapper.findByMaterialqualitytypeId(materialQuality);
			for(int j=0;j<blankFormMaps1.size();j++){
				MaterialqualitytypeProcessFormMap goodProcessFormMap=blankFormMaps1.get(j);
				String processName=goodProcessFormMap.get("processName")+"";
				String processIdBlank=goodProcessFormMap.get("processId")+"";
				List<WorkersubmitHeatTreatFormMap>workersubmitHeatTreatFormMaps=workersubmitHeatTreatMapper.findAmountAndUserByContractNumberAndDeliveryTimeAndGoodIdAndProcessId(
						contractNumberBlank,deliveryTimeBlank,goodIdBlank,processIdBlank
				);
				if(workersubmitHeatTreatFormMaps.size()==0){
					process=process+processName+"→";
				}else{
					for(int jj=0;jj<workersubmitHeatTreatFormMaps.size();jj++){
						WorkersubmitHeatTreatFormMap workersubmitHeatTreatFormMap=workersubmitHeatTreatFormMaps.get(jj);
						String getMessage=workersubmitHeatTreatFormMap.get("getMessage")+"";
						String completeMessage=workersubmitHeatTreatFormMap.get("completeMessage")+"";
						String userName=workersubmitHeatTreatFormMap.get("userName")+"";
						if(ToolCommon.isNull(userName)){
							process=process+processName;
						}else{
							if(getMessage.equals("0")&&completeMessage.equals("0")){

							}else{
								if(getMessage.equals("0")){
									process=process+processName+"("+ToolCommon.setContentToGreen(userName+completeMessage)+")→";
								}else if(completeMessage.equals("0")){
									process=process+processName+"("+ToolCommon.setContentToRed(userName+getMessage)+")→";
								}else{
									process=process+processName+"("+ToolCommon.setContentToRed(userName+getMessage)+" "+ToolCommon.setContentToGreen(userName+completeMessage)+")→";
								}
							}
						}

					}
				}

			}
		}else{
			//小调度渗碳右击详细
			List<WorkersubmitHeatTreatFormMap> workersubmitHeatTreatFormMaps=workersubmitHeatTreatMapper.findProgressByContractNumberAndDeliveryTimeAndGoodId(contractNumberBlank,deliveryTimeBlank,goodIdBlank);
			process="";
			for(int j=0;j<workersubmitHeatTreatFormMaps.size();j++){
				WorkersubmitHeatTreatFormMap workersubmitHeatTreatFormMap=workersubmitHeatTreatFormMaps.get(j);
				String completeTime=workersubmitHeatTreatFormMap.getStr("completeTime");
				String startTime=workersubmitHeatTreatFormMap.getStr("startTime");
				String userName=workersubmitHeatTreatFormMap.getStr("userName");
				String processName=workersubmitHeatTreatFormMap.getStr("processName");
				String amount=workersubmitHeatTreatFormMap.getStr("amount");
				String completeAmount=workersubmitHeatTreatFormMap.getStr("completeAmount");
				if(ToolCommon.isNull(completeTime)){
					process=process+"<span style='color:#ff0030;font-weight:bold'>"+processName+"("+userName+startTime+"已接收"+amount+")</span>→";
				}else if(!ToolCommon.isNull(completeTime)){
					process=process+"<span style='color:#048133;font-weight:bold'>"+processName+"("+userName+completeTime+"已完成"+completeAmount+")</span>→";
				}
			}
		}

		if(process.equals("")){

		}else{
			process= process.substring(0, process.length() - 1);
		}
		return process;
	}

	public List<HeatTreatFormMap> getSmallBlankListProgress(List<HeatTreatFormMap> departmentFormMapList,
														List<FixedProcessFormMap> fixedProcessFormMaps,String origin){
		List<HeatTreatFormMap> blankFormMaps=new ArrayList<>();
		for(int i=0;i<departmentFormMapList.size();i++){
			HeatTreatFormMap blankFormMap1=departmentFormMapList.get(i);
			String goodIdBlank=blankFormMap1.get("goodId")+"";
			String materialQuality=blankFormMap1.get("materialQuality")+"";
			String goodProcessIds="";
			if(!ToolCommon.isNull(materialQuality)){
				goodProcessIds=materialqualitytypeProcessMapper.findStringByMaterialqualitytypeId(materialQuality);
			}
			String deliveryTimeBlank=blankFormMap1.get("deliveryTime")+"";
			String contractNumberBlank=blankFormMap1.get("contractNumber")+"";
			for(int j=0;j<fixedProcessFormMaps.size();j++){
				FixedProcessFormMap goodProcessFormMap=fixedProcessFormMaps.get(j);
				String processName=goodProcessFormMap.get("processName")+"";
				String processIdBlank=goodProcessFormMap.get("processId")+"";
				WorkersubmitHeatTreatFormMap workersubmitHeatTreatFormMap=null;
				boolean isOprate=false;
				if(origin.equals("正火调质")){
					if(ToolCommon.isContain(goodProcessIds,processIdBlank)){
						isOprate=true;
							workersubmitHeatTreatFormMap=workersubmitHeatTreatMapper.findUnCompleteAmountAndCompleteAmountByContractNumberAndDeliveryTimeAndGoodIdAndProcessId(
									contractNumberBlank,deliveryTimeBlank,goodIdBlank,processIdBlank
							);

					}else{

					}
				}else{
					isOprate=true;
					workersubmitHeatTreatFormMap=workersubmitHeatTreatMapper.findUnCompleteAmountAndCompleteAmountByContractNumberAndDeliveryTimeAndGoodIdAndProcessId(
							contractNumberBlank,deliveryTimeBlank,goodIdBlank,processIdBlank
					);
				}
				if(isOprate){
					int unCompleteAmount=ToolCommon.StringToInteger(workersubmitHeatTreatFormMap.get("unCompleteAmount")+"");
					int completeAmount=ToolCommon.StringToInteger(workersubmitHeatTreatFormMap.get("completeAmount")+"");
					if(unCompleteAmount==0&&completeAmount>0){
						blankFormMap1.set("process"+(j+1),ToolCommon.setContentToGreenFontBig(processName));
					}else if(unCompleteAmount>0&&completeAmount==0){
						blankFormMap1.set("process"+(j+1),ToolCommon.setContentToRedFontBig(processName));
					}else{
						blankFormMap1.set("process"+(j+1),processName);
					}
				}
			}
			blankFormMaps.add(blankFormMap1);
		}
		return blankFormMaps;
	}

	public List<HeatTreatFormMap> getSmallBlankListProgressChinese(List<HeatTreatFormMap> departmentFormMapList,
															List<FixedProcessFormMap> fixedProcessFormMaps,String origin){
		List<HeatTreatFormMap> blankFormMaps=new ArrayList<>();
		for(int i=0;i<departmentFormMapList.size();i++){
			HeatTreatFormMap blankFormMap1=departmentFormMapList.get(i);
			String goodIdBlank=blankFormMap1.get("goodId")+"";
			String materialQuality=blankFormMap1.get("materialQuality")+"";
			String goodProcessIds=materialqualitytypeProcessMapper.findStringByMaterialqualitytypeId(materialQuality);
			String deliveryTimeBlank=blankFormMap1.get("deliveryTime")+"";
			String contractNumberBlank=blankFormMap1.get("contractNumber")+"";
			for(int j=0;j<fixedProcessFormMaps.size();j++){
				FixedProcessFormMap goodProcessFormMap=fixedProcessFormMaps.get(j);
				String processName=goodProcessFormMap.get("processName")+"";
				String processIdBlank=goodProcessFormMap.get("processId")+"";
				WorkersubmitHeatTreatFormMap workersubmitHeatTreatFormMap=null;
				boolean isOprate=false;
				if(origin.equals("正火调质")){
					if(ToolCommon.isContain(goodProcessIds,processIdBlank)){
						isOprate=true;
						workersubmitHeatTreatFormMap=workersubmitHeatTreatMapper.findUnCompleteAmountAndCompleteAmountByContractNumberAndDeliveryTimeAndGoodIdAndProcessId(
								contractNumberBlank,deliveryTimeBlank,goodIdBlank,processIdBlank
						);

					}else{

					}
				}else{
					isOprate=true;
					String hearttreatId=blankFormMap1.get("id")+"";
					workersubmitHeatTreatFormMap=workersubmitHeatTreatMapper.findUnCompleteAmountAndCompleteAmountByHeattreatIdAndProcessId(processIdBlank,hearttreatId);
				}
				if(isOprate){
					int unCompleteAmount=ToolCommon.StringToInteger(workersubmitHeatTreatFormMap.get("unCompleteAmount")+"");
					int completeAmount=ToolCommon.StringToInteger(workersubmitHeatTreatFormMap.get("completeAmount")+"");
					if(unCompleteAmount==0&&completeAmount>0){
						blankFormMap1.set("process"+(j+1),processName+"(已完成)");
					}else if(unCompleteAmount>0&&completeAmount==0){
						blankFormMap1.set("process"+(j+1),processName+"(已接收未完成)");
					}else{
						blankFormMap1.set("process"+(j+1),processName);
					}
				}
			}
			blankFormMaps.add(blankFormMap1);
		}
		return blankFormMaps;
	}

	public List<BlankFormMap> getBlankListContainProgressNoColor(List<BlankFormMap> departmentFormMapList){
		List<BlankFormMap> blankFormMaps=new ArrayList<>();
		for(int i=0;i<departmentFormMapList.size();i++){
			BlankFormMap blankFormMap1=departmentFormMapList.get(i);
			String process="";
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
				List<WorkersubmitHeatTreatFormMap> workersubmitHeatTreatFormMaps=workersubmitHeatTreatMapper.findAmountAndUserByContractNumberAndDeliveryTimeAndGoodIdAndProcessId(
						contractNumberBlank,deliveryTimeBlank,goodIdBlank,processIdBlank
				);
				if(workersubmitHeatTreatFormMaps.size()>0){
					for(int jj=0;jj<workersubmitHeatTreatFormMaps.size();jj++){
						WorkersubmitHeatTreatFormMap workersubmitHeatTreatFormMap=workersubmitHeatTreatFormMaps.get(jj);
						String getMessage=workersubmitHeatTreatFormMap.get("getMessage")+"";
						String completeMessage=workersubmitHeatTreatFormMap.get("completeMessage")+"";
						String userName=workersubmitHeatTreatFormMap.get("userName")+"";
						if(ToolCommon.isNull(userName)){
							process=getProgress(process,processName,blankProcessId,planneedDay);
						}else{
							if(getMessage.equals("0")&&completeMessage.equals("0")){

							}else{
								if(getMessage.equals("0")){
									process=process+processName+planneedDay+"("+(userName+completeMessage)+")→";
								}else if(completeMessage.equals("0")){
									process=process+processName+planneedDay+"("+(userName+getMessage)+")→";
								}else{
									process=process+processName+planneedDay+"("+(userName+getMessage)+" "+(userName+completeMessage)+")→";
								}
							}
						}

					}
				}else{
					process=getProgressNoColor(process,processName,blankProcessId,planneedDay);
				}


			}
			if(process.equals("")){

			}else{
				process= process.substring(0, process.length() - 1);
			}
			blankFormMap1.set("progress",process);
			blankFormMaps.add(blankFormMap1);
		}
		return blankFormMaps;
	}

	public String getProgress(String process,String processName,String blankProcessId,String planneedDay){
		int blankCount=blankMapper.findCountByBlankProcessId(blankProcessId);
		if(blankCount==0){
			process=process+processName+planneedDay+"→";
		}else{
			List<BlankFormMap> blankFormMaps2=blankMapper.findProgressAndUserByBlankProcessId(blankProcessId);
			for(int ii=0;ii<blankFormMaps2.size();ii++){
				BlankFormMap blankFormMap2=blankFormMaps2.get(ii);
				String userName1=blankFormMap2.get("userName")+"";
				String getMessage1=blankFormMap2.get("getMessage")+"";
				String completeMessage1=blankFormMap2.get("completeMessage")+"";
				if(ToolCommon.isNull(userName1)){
					process=process+processName+planneedDay+"→";
				}else{
					if(getMessage1.equals("0")&&completeMessage1.equals("0")){
						process=process+processName+planneedDay+"→";
					}else{
						if(getMessage1.equals("0")){
							process=process+processName+planneedDay+"("+ToolCommon.setContentToGreen(userName1+completeMessage1)+")→";
						}else if(completeMessage1.equals("0")){
							process=process+processName+planneedDay+"("+ToolCommon.setContentToRed(userName1+getMessage1)+")→";
						}else{
							process=process+processName+planneedDay+"("+ToolCommon.setContentToRed(userName1+completeMessage1)+" "+ToolCommon.setContentToGreen(userName1+completeMessage1)+")→";
						}
					}
				}

			}
		}
		return process;
	}
	public String getProgressNoColor(String process,String processName,String blankProcessId,String planneedDay){
		List<BlankFormMap> blankFormMaps2=blankMapper.findProgressAndUserByBlankProcessId(blankProcessId);
		if(blankFormMaps2.size()==0){
			process=process+processName+planneedDay+"→";
		}else{
			for(int ii=0;ii<blankFormMaps2.size();ii++){
				BlankFormMap blankFormMap2=blankFormMaps2.get(ii);
				String userName1=blankFormMap2.get("userName")+"";
				String getMessage1=blankFormMap2.get("getMessage")+"";
				String completeMessage1=blankFormMap2.get("completeMessage")+"";
				if(ToolCommon.isNull(userName1)){
					process=process+processName+planneedDay+"→";
				}else{
					if(getMessage1.equals("0")&&completeMessage1.equals("0")){
						process=process+processName+planneedDay+"→";
					}else{
						if(getMessage1.equals("0")){
							process=process+processName+planneedDay+"("+(userName1+completeMessage1)+")→";
						}else if(completeMessage1.equals("0")){
							process=process+processName+planneedDay+"("+(userName1+getMessage1)+")→";
						}else{
							process=process+processName+planneedDay+"("+(userName1+completeMessage1)+" "+(userName1+completeMessage1)+")→";
						}
					}
				}

			}
		}

		return process;
	}

	@ResponseBody
	@RequestMapping("findBigShowClientByContent")
	@SystemLog(module="调度大屏",methods="获取左侧客户列表内容")
	public List findBigShowClientByContent(
										  String contractNumber,
										  String materialCode,
										  String code,
										  String state,
										  String goodName,
										  String processId,
										  String startTime,
										  String endTime,
										  String materialQuality,
										  String blankSize,
										  String goodSize,
										  String mapNumber) throws Exception {
		BlankFormMap blankFormMap = getFormMap(BlankFormMap.class);

		if(endTime!=null&&!endTime.equals("")){
			endTime=ToolCommon.addDay(endTime,1);
		}
		blankFormMap.set("contractNumber",contractNumber);
		blankFormMap.set("materialCode",materialCode);
		blankFormMap.set("state",state);
		blankFormMap.set("code",code);
		blankFormMap.set("processId",processId);
		blankFormMap.set("startTime",startTime);
		blankFormMap.set("materialQuality",materialQuality);
		blankFormMap.set("blankSize",blankSize);
		blankFormMap.set("goodSize",goodSize);
		blankFormMap.set("endTime",endTime);
		blankFormMap.set("mapNumber",mapNumber);

		blankFormMap.set("goodName",goodName);
		List<BlankFormMap> clientFormMaps=blankMapper.findBigShowClientByContent(blankFormMap);
		return clientFormMaps;
	}

	@ResponseBody
	@RequestMapping("findSmallShow2ShowClientByContent")
	@SystemLog(module="小调度2",methods="获取左侧客户列表内容")
	public List findSmallShow2ShowClientByContent(
			String contractNumber,
			String goodName,
			String startTime,
			String endTime,
			String startTimePick,
			String endTimePick,
			String materialQuality,
			String goodSize,
			String mapNumber,
			String origin) throws Exception {
		HeatTreatFormMap heatTreatFormMap = new HeatTreatFormMap();

		if(endTime!=null&&!endTime.equals("")){
			endTime=ToolCommon.addDay(endTime,1);
		}
		if(endTimePick!=null&&!endTimePick.equals("")){
			endTimePick=ToolCommon.addDay(endTimePick,1);
		}
		heatTreatFormMap.set("contractNumber",contractNumber);
		heatTreatFormMap.set("startTime",startTime);
		heatTreatFormMap.set("endTime",endTime);
		heatTreatFormMap.set("startTimePick",startTimePick);
		heatTreatFormMap.set("endTimePick",endTimePick);
		heatTreatFormMap.set("materialQuality",materialQuality);
		heatTreatFormMap.set("goodSize",goodSize);
		heatTreatFormMap.set("mapNumber",mapNumber);
		heatTreatFormMap.set("goodName",goodName);
		heatTreatFormMap.set("origin",origin);
		List<HeatTreatFormMap> clientFormMaps=heatTreatMapper.findSmallShow2ClientByContent(heatTreatFormMap);
		return clientFormMaps;
	}

	@ResponseBody
	@RequestMapping("findSmallShowOrder2ByContent")
	@SystemLog(module="小调度2",methods="获取左侧订单列表内容")
	public List findSmallShowOrder2ByContent(
			String contractNumber,
			String goodName,
			String startTime,
			String endTime,
			String startTimePick,
			String endTimePick,
			String materialQuality,
			String goodSize,
			String mapNumber,
			String origin,String clientId) throws Exception {
		HeatTreatFormMap heatTreatFormMap = new HeatTreatFormMap();


		if(goodName==null||goodName.equals("")||goodName.equals("不限")){
			goodName="";
		}

		if(materialQuality==null||materialQuality.equals("")||materialQuality.equals("不限")){
			materialQuality="";
		}

		if(goodSize==null||goodSize.equals("")||goodSize.equals("不限")){
			goodSize="";
		}

		if(endTime!=null&&!endTime.equals("")){
			endTime=ToolCommon.addDay(endTime,1);
		}
		if(endTimePick!=null&&!endTimePick.equals("")){
			endTimePick=ToolCommon.addDay(endTimePick,1);
		}
		heatTreatFormMap.set("contractNumber",contractNumber);
		heatTreatFormMap.set("clientId",clientId);
		heatTreatFormMap.set("startTime",startTime);
		heatTreatFormMap.set("endTime",endTime);
		heatTreatFormMap.set("startTimePick",startTimePick);
		heatTreatFormMap.set("endTimePick",endTimePick);
		heatTreatFormMap.set("materialQuality",materialQuality);
		heatTreatFormMap.set("goodSize",goodSize);
		heatTreatFormMap.set("mapNumber",mapNumber);
		heatTreatFormMap.set("goodName",goodName);
		heatTreatFormMap.set("origin",origin);
		List<HeatTreatFormMap> clientFormMaps=heatTreatMapper.findSmallShow2OrderByContent(heatTreatFormMap);
		List<TreeOrderFormMap> treeClientEntities=new ArrayList<>();
		try {
			for (int i = 0; i < clientFormMaps.size(); i++) {
				HeatTreatFormMap clientFormMap = clientFormMaps.get(i);
				if (clientFormMap != null) {
					TreeOrderFormMap treeOrderFormMap = new TreeOrderFormMap();
					treeOrderFormMap.id =  clientFormMap.getStr("id");
					treeOrderFormMap.text = clientFormMap.getStr("text");
					treeOrderFormMap.state = "closed";
					List<TreeOrderDetailsFormMap> treeOrderDetailsFormMaps = new ArrayList<>();
					treeOrderFormMap.children = treeOrderDetailsFormMaps;
					treeClientEntities.add(treeOrderFormMap);
				}
			}
			return treeClientEntities;
		}catch (Exception e){
			return treeClientEntities;
		}
	}


	@ResponseBody
	@RequestMapping("findBigShowOrderByContent")
	@SystemLog(module="调度大屏",methods="获取左侧订单列表内容")
	public List findBigShowOrderByContent(
			String contractNumber,
			String materialCode,
			String state,
			String code,
			String goodName,
			String processId,
			String startTime,
			String endTime,
			String materialQuality,
			String blankSize,
			String goodSize,
			String clientId,
			String mapNumber) throws Exception {
		BlankFormMap blankFormMap = getFormMap(BlankFormMap.class);

		if(state==null||state.equals("")||state.equals("不限")){
			state="";
		}
		if(materialCode==null||materialCode.equals("")||materialCode.equals("不限")){
			materialCode="";
		}
		if(code==null||code.equals("")||code.equals("不限")){
			code="";
		}
		if(goodName==null||goodName.equals("")||goodName.equals("不限")){
			goodName="";
		}
		if(processId==null||processId.equals("")||processId.equals("不限")){
			processId="";
		}
		if(materialQuality==null||materialQuality.equals("")||materialQuality.equals("不限")){
			materialQuality="";
		}
		if(blankSize==null||blankSize.equals("")||blankSize.equals("不限")){
			blankSize="";
		}
		if(goodSize==null||goodSize.equals("")||goodSize.equals("不限")){
			goodSize="";
		}

		if(endTime!=null&&!endTime.equals("")){
			endTime=ToolCommon.addDay(endTime,1);
		}
		blankFormMap.set("materialCode",materialCode);
		blankFormMap.set("code",code);
		blankFormMap.set("contractNumber",contractNumber);
		blankFormMap.set("state",state);
		blankFormMap.set("clientId",clientId);
		blankFormMap.set("processId",processId);
		blankFormMap.set("startTime",startTime);
		blankFormMap.set("materialQuality",materialQuality);
		blankFormMap.set("blankSize",blankSize);
		blankFormMap.set("goodSize",goodSize);
		blankFormMap.set("endTime",endTime);
		blankFormMap.set("mapNumber",mapNumber);

		blankFormMap.set("goodName",goodName);
		List<BlankFormMap> clientFormMaps=blankMapper.findBigShowOrderByContent(blankFormMap);

		List<TreeOrderFormMap> treeClientEntities=new ArrayList<>();
		try {
			for (int i = 0; i < clientFormMaps.size(); i++) {
				BlankFormMap clientFormMap = clientFormMaps.get(i);
				if (clientFormMap != null) {
					String orderId = clientFormMap.get("id") + "";
					TreeOrderFormMap treeOrderFormMap = new TreeOrderFormMap();
					treeOrderFormMap.id = orderId + "订单";
					treeOrderFormMap.text = clientFormMap.getStr("contractNumber");
					treeOrderFormMap.state = "closed";
					List<TreeOrderDetailsFormMap> treeOrderDetailsFormMaps = new ArrayList<>();
					treeOrderFormMap.children = treeOrderDetailsFormMaps;
					treeClientEntities.add(treeOrderFormMap);
				}
			}
			return treeClientEntities;
		}catch (Exception e){
			return treeClientEntities;
		}
	}

	@ResponseBody
	@RequestMapping("findBigShowOrderDetailsByContent")
	@SystemLog(module="调度大屏",methods="获取左侧订单明细列表内容")
	public List findBigShowOrderDetailsByContent(
			String contractNumber,
			String materialCode,
			String code,
			String state,
			String goodName,
			String orderId,
			String processId,
			String startTime,
			String endTime,
			String materialQuality,
			String blankSize,
			String goodSize,
			String mapNumber) throws Exception {
		BlankFormMap blankFormMap = getFormMap(BlankFormMap.class);
		if(state==null||state.equals("")||state.equals("不限")){
			state="";
		}
		if(materialCode==null||materialCode.equals("")||materialCode.equals("不限")){
			materialCode="";
		}
		if(code==null||code.equals("")||code.equals("不限")){
			code="";
		}
		if(goodName==null||goodName.equals("")||goodName.equals("不限")){
			goodName="";
		}
		if(processId==null||processId.equals("")||processId.equals("不限")){
			processId="";
		}
		if(materialQuality==null||materialQuality.equals("")||materialQuality.equals("不限")){
			materialQuality="";
		}
		if(blankSize==null||blankSize.equals("")||blankSize.equals("不限")){
			blankSize="";
		}
		if(goodSize==null||goodSize.equals("")||goodSize.equals("不限")){
			goodSize="";
		}

		if(endTime!=null&&!endTime.equals("")){
			endTime=ToolCommon.addDay(endTime,1);
		}
		blankFormMap.set("materialCode",materialCode);
		blankFormMap.set("contractNumber",contractNumber);
		blankFormMap.set("code",code);
		blankFormMap.set("orderId",orderId);
		blankFormMap.set("state",state);
		blankFormMap.set("processId",processId);
		blankFormMap.set("startTime",startTime);
		blankFormMap.set("materialQuality",materialQuality);
		blankFormMap.set("blankSize",blankSize);
		blankFormMap.set("goodSize",goodSize);
		blankFormMap.set("endTime",endTime);
		blankFormMap.set("mapNumber",mapNumber);
		blankFormMap.set("printTime","不限");

		blankFormMap.set("goodName",goodName);
		List<BlankFormMap> clientFormMaps=blankMapper.findOrderDetailsByAllLike(blankFormMap);
		List<TreeOrderDetailsFormMap> treeClientEntities=new ArrayList<>();
		try{
			for(int i=0;i<clientFormMaps.size();i++){
				BlankFormMap clientFormMap=clientFormMaps.get(i);
				if(clientFormMap!=null){
					String goodId=clientFormMap.get("goodId")+"";
					TreeOrderDetailsFormMap treeOrderFormMap=new TreeOrderDetailsFormMap();
					treeOrderFormMap.id=goodId+"明细";
					treeOrderFormMap.text=clientFormMap.getStr("mapNumber");
					treeClientEntities.add(treeOrderFormMap);
				}
			}
//			TreeOrderDetailsFormMap treeClientEntity=new TreeOrderDetailsFormMap();
//			treeClientEntity.id="不限";
//			treeClientEntity.text="不限";
//			treeClientEntities.add(treeClientEntity);
			return treeClientEntities;
		}catch (Exception e){
			System.out.print(e);
			return treeClientEntities;
		}

	}

	@ResponseBody
	@RequestMapping("findSmallShowOrderDetails2ByContent")
	@SystemLog(module="小调度2",methods="获取左侧订单明细列表内容")
	public List findSmallShowOrderDetails2ByContent(
			String contractNumber,
			String goodName,
			String startTime,
			String endTime,
			String startTimePick,
			String endTimePick,
			String materialQuality,
			String goodSize,
			String mapNumber,
			String origin,String clientId) throws Exception {
		HeatTreatFormMap heatTreatFormMap = new HeatTreatFormMap();


		if(goodName==null||goodName.equals("")||goodName.equals("不限")){
			goodName="";
		}

		if(materialQuality==null||materialQuality.equals("")||materialQuality.equals("不限")){
			materialQuality="";
		}

		if(goodSize==null||goodSize.equals("")||goodSize.equals("不限")){
			goodSize="";
		}

		if(endTime!=null&&!endTime.equals("")){
			endTime=ToolCommon.addDay(endTime,1);
		}
		if(endTimePick!=null&&!endTimePick.equals("")){
			endTimePick=ToolCommon.addDay(endTimePick,1);
		}
		heatTreatFormMap.set("contractNumber",contractNumber);
		heatTreatFormMap.set("clientId",clientId);
		heatTreatFormMap.set("startTime",startTime);
		heatTreatFormMap.set("endTime",endTime);
		heatTreatFormMap.set("startTimePick",startTimePick);
		heatTreatFormMap.set("endTimePick",endTimePick);
		heatTreatFormMap.set("materialQuality",materialQuality);
		heatTreatFormMap.set("goodSize",goodSize);
		heatTreatFormMap.set("mapNumber",mapNumber);
		heatTreatFormMap.set("goodName",goodName);
		heatTreatFormMap.set("origin",origin);
		List<HeatTreatFormMap> clientFormMaps=heatTreatMapper.findSmallShow2OrderDetailsByContent(heatTreatFormMap);
			return clientFormMaps;
	}


	@RequestMapping(value = "exportBigShow")
	@ResponseBody
	@SystemLog(module="调度大屏",methods="导出表格")//凡需要处理业务逻辑的.都需要记录操作日志
	public void exportBigShow(HttpServletResponse response,String entity) throws Exception {
		String processId=ToolCommon.json2Object(entity).getString("processId");
		String contractNumber=ToolCommon.json2Object(entity).getString("contractNumber");
		String code=ToolCommon.json2Object(entity).getString("code");
		String materialCode=ToolCommon.json2Object(entity).getString("materialCode");
		String mapNumber=ToolCommon.json2Object(entity).getString("mapNumber");
		String startTime=ToolCommon.json2Object(entity).getString("startTime");
		String endTime=ToolCommon.json2Object(entity).getString("endTime");
		String state=ToolCommon.json2Object(entity).getString("state");
		String goodName=ToolCommon.json2Object(entity).getString("goodName");
		String clientId=ToolCommon.json2Object(entity).getString("clientId");
		String materialQuality=ToolCommon.json2Object(entity).getString("materialQuality");
		String blankSize=ToolCommon.json2Object(entity).getString("blankSize");
		String goodSize=ToolCommon.json2Object(entity).getString("goodSize");
		String goodId=ToolCommon.json2Object(entity).getString("goodId");
		String orderId=ToolCommon.json2Object(entity).getString("orderId");
		BlankFormMap blankFormMap=new BlankFormMap();
		if(state==null||state.equals("")||state.equals("不限")){
			state="";
		}
		if(materialCode==null||materialCode.equals("")||materialCode.equals("不限")){
			materialCode="";
		}
		if(code==null||code.equals("")||code.equals("不限")){
			code="";
		}
		if(goodName==null||goodName.equals("")||goodName.equals("不限")){
			goodName="";
		}
		if(clientId==null||clientId.equals("")||clientId.equals("不限")){
			clientId="";
		}

		if(processId==null||processId.equals("")||processId.equals("不限")){
			processId="";
		}
		if(materialQuality==null||materialQuality.equals("")||materialQuality.equals("不限")){
			materialQuality="";
		}
		if(blankSize==null||blankSize.equals("")||blankSize.equals("不限")){
			blankSize="";
		}
		if(goodSize==null||goodSize.equals("")||goodSize.equals("不限")){
			goodSize="";
		}
		if(endTime!=null&&!endTime.equals("")){
			endTime=ToolCommon.addDay(endTime,1);
		}

		blankFormMap.set("clientId",clientId);
		blankFormMap.set("materialCode",materialCode);
		blankFormMap.set("code",code);
		blankFormMap.set("goodId",goodId);
		blankFormMap.set("orderId",orderId);
		blankFormMap.set("contractNumber",contractNumber);
		blankFormMap.set("state",state);
		blankFormMap.set("processId",processId);
		blankFormMap.set("startTime",startTime);
		blankFormMap.set("endTime",endTime);
		blankFormMap.set("mapNumber",mapNumber);
		blankFormMap.set("materialQuality",materialQuality);
		blankFormMap.set("blankSize",blankSize);
		blankFormMap.set("goodSize",goodSize);
		blankFormMap.set("goodName",goodName);
		List<BlankFormMap> blankFormMaps=blankMapper.findBigShowByContent(blankFormMap);
		//excel标题
		String[] title = {"客户", "订单编号","图号","产品名称","成品尺寸","下料尺寸","材质","交货日期","工序号","工序","数量","预计用时","完成数量","废品数量","状态"};

		//excel文件名
		String fileName = "调度信息" + System.currentTimeMillis() + ".xls";

		//sheet名
		String sheetName = "sheet1";

		String [][] content = new String[blankFormMaps.size()][title.length];

		for (int i = 0; i < blankFormMaps.size(); i++) {
			content[i] = new String[title.length];
			BlankFormMap obj = blankFormMaps.get(i);
			content[i][0] = obj.get("clientFullName")+"";
			content[i][1] = obj.get("contractNumber")+"";
			content[i][2] = obj.get("mapNumber")+"";
			content[i][3] = obj.get("goodName")+"";
			content[i][4] = obj.get("goodSize")+"";
			content[i][5] = obj.get("blankSize")+"";
			content[i][6] = obj.get("materialQualityName")+"";
			content[i][7] = obj.get("deliveryTime")+"";
			content[i][8] = obj.get("number")+"";
			content[i][9] = obj.get("processName")+"";
			content[i][10] = obj.get("amount")+"";
			content[i][11] = obj.get("planneedDay")+"";
			content[i][12] = obj.get("completeAmount")+"";
			content[i][13] = obj.get("badAmount")+"";
			String state1=obj.get("state")+"";
			if(state1.equals("已完成")){
				content[i][14] ="已完成:"+obj.get("completeAmount")+"";
			}else{
				content[i][14] = obj.get("state")+"";
			}
		}

		//创建HSSFWorkbook
		HSSFWorkbook wb = ExcelUtils.getHSSFWorkbook("时间紧，任务重，加快生产！",sheetName, title, content, null);

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
	@RequestMapping(value = "exportSmall2Show")
	@ResponseBody
	@SystemLog(module="小调度2渗碳",methods="导出表格")//凡需要处理业务逻辑的.都需要记录操作日志
	public void exportSmall2Show(HttpServletResponse response,String entity) throws Exception {
		String clientId=ToolCommon.json2Object(entity).getString("clientId");
		String contractNumber=ToolCommon.json2Object(entity).getString("contractNumber");
		String goodName=ToolCommon.json2Object(entity).getString("goodName");
		String startTime=ToolCommon.json2Object(entity).getString("startTime");
		String endTime=ToolCommon.json2Object(entity).getString("endTime");
		String startTimePick=ToolCommon.json2Object(entity).getString("startTimePick");
		String endTimePick=ToolCommon.json2Object(entity).getString("endTimePick");
		String materialQuality=ToolCommon.json2Object(entity).getString("materialQuality");
		String goodSize=ToolCommon.json2Object(entity).getString("goodSize");
		String mapNumber=ToolCommon.json2Object(entity).getString("mapNumber");
		String origin=ToolCommon.json2Object(entity).getString("origin");

		HeatTreatFormMap heatTreatFormMap =new HeatTreatFormMap();
		heatTreatFormMap.set("clientId",clientId);
		heatTreatFormMap.set("contractNumber",contractNumber);
		heatTreatFormMap.set("startTime",startTime);
		if(!ToolCommon.isNull(endTime)){
			endTime=ToolCommon.addDay(endTime,1);
		}
		heatTreatFormMap.set("endTime",endTime);
		heatTreatFormMap.set("startTimePick",startTimePick);
		if(!ToolCommon.isNull(endTimePick)){
			endTimePick=ToolCommon.addDay(endTimePick,1);
		}
		heatTreatFormMap.set("endTimePick",endTimePick);
		heatTreatFormMap.set("mapNumber",mapNumber);
		heatTreatFormMap.set("materialQuality",materialQuality);
		heatTreatFormMap.set("goodSize",goodSize);
		heatTreatFormMap.set("goodName",goodName);
		heatTreatFormMap.set("origin",origin);
		List<HeatTreatFormMap> departmentFormMapList=heatTreatMapper.findDispatchContentByDispatch(heatTreatFormMap);
		List<FixedProcessFormMap> fixedProcessFormMaps=fixedProcessMapper.findByOrigin(origin);
		List<HeatTreatFormMap> blankFormMaps=getSmallBlankListProgressChinese(departmentFormMapList,fixedProcessFormMaps,origin);
		//excel标题
		int len=0;
		if(origin.equals("正火调质")){
			len=19;
		}else{
			len=21;
		}
		String[] title=new String[len];
		if(origin.equals("正火调质")){
			title[0]="领料日期";
			title[1]="客户";
			title[2]="订单编号";
			title[3]="图号";
			title[4]="产品名称";
			title[5]="成品尺寸";
			title[6]="材质";
			title[7]="交货日期";
			title[8]="数量";
			title[9]="车";
			title[10]="火前";
			title[11]="铣槽";
			title[12]="油槽";
			title[13]="钳";
			title[14]="铣";
			title[15]="氮前";
			title[16]="热渗氮";
			title[17]="氮后";
			title[18]="线切割";

		}else{
			title[0]="领料日期";
			title[1]="客户";
			title[2]="订单编号";
			title[3]="图号";
			title[4]="产品名称";
			title[5]="成品尺寸";
			title[6]="材质";
			title[7]="交货日期";
			title[8]="数量";
			title[9]="平磨";
			title[10]="外圆粗";
			title[11]="消差";
			title[12]="磨边";
			title[13]="统一尺寸";
			title[14]="内孔粗";
			title[15]="内孔精";
			title[16]="外圆精";
			title[17]="外磨";
			title[18]="内磨";
			title[19]="端面";
			title[20]="线切割";
		}

		//excel文件名
		String fileName = "调度信息" +origin+System.currentTimeMillis() + ".xls";

		//sheet名
		String sheetName = "sheet1";

		String [][] content = new String[blankFormMaps.size()][title.length];
		if(origin.equals("正火调质")){
			for (int i = 0; i < blankFormMaps.size(); i++) {
				content[i] = new String[title.length];
				HeatTreatFormMap obj = blankFormMaps.get(i);
				content[i][0] = obj.get("pickTime")+"";
				content[i][1] = obj.get("clientFullName")+"";
				content[i][2] = obj.get("contractNumber")+"";
				content[i][3] = obj.get("mapNumber")+"";
				content[i][4] = obj.get("goodName")+"";
				content[i][5] = obj.get("goodSize")+"";
				content[i][6] = obj.get("materialQualityName")+"";
				content[i][7] = obj.get("deliveryTime")+"";
				content[i][8] = obj.get("amount")+"";
				content[i][9] = obj.get("process1")+"";
				content[i][10] = obj.get("process2")+"";
				content[i][11] = obj.get("process3")+"";
				content[i][12] = obj.get("process4")+"";
				content[i][13] = obj.get("process5")+"";
				content[i][14] = obj.get("process6")+"";
				content[i][15] = obj.get("process7")+"";
				content[i][16] = obj.get("process8")+"";
				content[i][17] = obj.get("process9")+"";
				content[i][18] = obj.get("process10")+"";
			}
		}else{
			for (int i = 0; i < blankFormMaps.size(); i++) {
				content[i] = new String[title.length];
				HeatTreatFormMap obj = blankFormMaps.get(i);
				content[i][0] = obj.get("pickTime")+"";
				content[i][1] = obj.get("clientFullName")+"";
				content[i][2] = obj.get("contractNumber")+"";
				content[i][3] = obj.get("mapNumber")+"";
				content[i][4] = obj.get("goodName")+"";
				content[i][5] = obj.get("goodSize")+"";
				content[i][6] = obj.get("materialQualityName")+"";
				content[i][7] = obj.get("deliveryTime")+"";
				content[i][8] = obj.get("amount")+"";
				content[i][9] = obj.get("process1")+"";
				content[i][10] = obj.get("process2")+"";
				content[i][11] = obj.get("process3")+"";
				content[i][12] = obj.get("process4")+"";
				content[i][13] = obj.get("process5")+"";
				content[i][14] = obj.get("process6")+"";
				content[i][15] = obj.get("process7")+"";
				content[i][16] = obj.get("process8")+"";
				content[i][17] = obj.get("process9")+"";
				content[i][18] = obj.get("process10")+"";
				content[i][19] = obj.get("process11")+"";
				content[i][20] = obj.get("process12")+"";
			}
		}


		//创建HSSFWorkbook
		HSSFWorkbook wb = ExcelUtils.getHSSFWorkbook("时间紧，任务重，加快生产！",sheetName, title, content, null);

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

	@RequestMapping(value = "exportGetlist")
	@ResponseBody
	@SystemLog(module="生产管理",methods="已接收列表-导出表格")//凡需要处理业务逻辑的.都需要记录操作日志
	public void exportGetlist(HttpServletResponse response,String entity) throws Exception {
		String user=ToolCommon.json2Object(entity).getString("user");
		String contentS=ToolCommon.json2Object(entity).getString("content");
		String startTime=ToolCommon.json2Object(entity).getString("startTime");
		String endTime=ToolCommon.json2Object(entity).getString("endTime");
		WorkersubmitFormMap workersubmitFormMap=new WorkersubmitFormMap();
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
		workersubmitFormMap.set("content",contentS);
		workersubmitFormMap.set("state","已接收未完成");
		workersubmitFormMap.set("user",user);
		workersubmitFormMap.set("startTime",startTime);
		workersubmitFormMap.set("endTime",endTime);
		List<WorkersubmitFormMap> workersubmitFormMaps=workersubmitMapper.findByWorkerSubmitIdByStartTimeAndEndTimeAndContentAndUser(workersubmitFormMap);

		//excel标题
		String[] title = {"订单编号",
				"图号",
				"产品名称",
				"产品尺寸",
				"下料尺寸",
				"材质",
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
			WorkersubmitFormMap obj = workersubmitFormMaps.get(i);
			content[i][0] = obj.get("contractNumber")+"";
			content[i][1] = obj.get("mapNumber")+"";
			content[i][2] = obj.get("goodName")+"";
			content[i][3] = obj.get("goodSize")+"";
			content[i][4] = obj.get("blankSize")+"";
			content[i][5] = obj.get("materialqualityName")+"";
			content[i][6] = obj.get("processName")+"";
			content[i][7] = obj.get("accountShow")+"";
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

	@RequestMapping(value = "progressSearchDownload")
	@ResponseBody
	@SystemLog(module="进度查询",methods="导出表格")//凡需要处理业务逻辑的.都需要记录操作日志
	public void progressSearchDownload(HttpServletResponse response,String entity) throws Exception {
		String origin=ToolCommon.json2Object(entity).getString("origin");
		String contentS=ToolCommon.json2Object(entity).getString("content");
		String goodName=ToolCommon.json2Object(entity).getString("goodName");
		String clientId=ToolCommon.json2Object(entity).getString("clientId");
		String startTime=ToolCommon.json2Object(entity).getString("startTime");
		String endTime=ToolCommon.json2Object(entity).getString("endTime");
		ProgressSearchFormMap progressSearchFormMapS=new ProgressSearchFormMap();
		if(origin.equals("检索")){
			progressSearchFormMapS.set("origin",origin);
			progressSearchFormMapS.set("goodName",goodName);
			progressSearchFormMapS.set("content",contentS);
			progressSearchFormMapS.set("clientId",clientId);
			progressSearchFormMapS.set("goodName",goodName);
			progressSearchFormMapS.set("startTime",startTime);
			if(!ToolCommon.isNull(endTime)){
				endTime=ToolCommon.addDay(endTime,1);
			}
			progressSearchFormMapS.set("endTime",endTime);
		}
		List<BlankFormMap> blankFormMaps=new ArrayList<>();
		List<ProgressSearchFormMap> progressSearchFormMaps=progressSearchMapper.findByAllLike(progressSearchFormMapS);
		for(int i=0;i<progressSearchFormMaps.size();i++){
			ProgressSearchFormMap progressSearchFormMap=progressSearchFormMaps.get(i);
			String id=progressSearchFormMap.get("id")+"";
			String contractNumber=progressSearchFormMap.get("contractNumber")+"";
			String mapNumber=progressSearchFormMap.get("mapNumber")+"";
			String deliveryTime=progressSearchFormMap.get("deliveryTime")+"";
			List<BlankFormMap> blankFormMaps1=blankMapper.findByProgressSearchId(id);
			if(blankFormMaps1.size()==0){
				if(origin.equals("全部")){
					String progressSearchId=progressSearchFormMap.get("id")+"";
					BlankFormMap blankFormMap1=new BlankFormMap();
					blankFormMap1.set("progressSearchId",progressSearchId);
					blankFormMap1.set("contractNumber",contractNumber);
					blankFormMap1.set("mapNumber",mapNumber);
					blankFormMap1.set("deliveryTime",deliveryTime);
					blankFormMaps1.add(blankFormMap1);
					blankFormMaps.addAll(blankFormMaps1);
				}
			}else{
				List<BlankFormMap> blankFormMaps2=getBlankListContainProgressNoColor(blankFormMaps1);
				blankFormMaps.addAll(blankFormMaps2);
			}
		}

		//excel标题
		String[] title = {"客户",
				"订单号",
				"图号",
				"产品名称",
				"产品尺寸",
				"下料尺寸",
				"材质",
				"交货日期",
				"数量",
				"当前分配数量",
				"进度"};

		//excel文件名
		String fileName = "进度查询" + System.currentTimeMillis() + ".xls";

		//sheet名
		String sheetName = "sheet1";

		String [][] content = new String[blankFormMaps.size()][title.length];

		for (int i = 0; i < blankFormMaps.size(); i++) {
			content[i] = new String[title.length];
			BlankFormMap obj = blankFormMaps.get(i);
			content[i][0] = obj.get("clientName")+"";
			content[i][1] = obj.get("contractNumber")+"";
			content[i][2] = obj.get("mapNumber")+"";
			content[i][3] = obj.get("goodName")+"";
			content[i][4] = obj.get("goodSize")+"";
			content[i][5] = obj.get("blankSize")+"";
			content[i][6] = obj.get("materialQuality")+"";
			content[i][7] = obj.get("deliveryTime")+"";
			content[i][8] = obj.get("waitDistributionAmount")+"";
			content[i][9] = obj.get("nowDistributionAmount")+"";
			content[i][10] = obj.get("progress")+"";
		}

		//创建HSSFWorkbook
		HSSFWorkbook wb = ExcelUtils.getHSSFWorkbook("进度查询",sheetName, title, content, null);

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

	@RequestMapping(value = "exportCompletelist")
	@ResponseBody
	@SystemLog(module="生产管理",methods="已完成列表-导出表格")//凡需要处理业务逻辑的.都需要记录操作日志
	public void exportCompletelist(HttpServletResponse response,String entity) throws Exception {
		String user=ToolCommon.json2Object(entity).getString("user");
		String contentS=ToolCommon.json2Object(entity).getString("content");
		String startTime=ToolCommon.json2Object(entity).getString("startTime");
		String endTime=ToolCommon.json2Object(entity).getString("endTime");
		WorkersubmitFormMap workersubmitFormMap=new WorkersubmitFormMap();
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
		workersubmitFormMap.set("content",contentS);
		workersubmitFormMap.set("state","已完成");
		workersubmitFormMap.set("user",user);
		workersubmitFormMap.set("startTime",startTime);
		workersubmitFormMap.set("endTime",endTime);
		List<WorkersubmitFormMap> workersubmitFormMaps=workersubmitMapper.findByWorkerSubmitIdByStartTimeAndEndTimeAndContentAndUser(workersubmitFormMap);

		//excel标题
		String[] title = {"订单编号",
				"图号",
				"产品名称",
				"产品尺寸",
				"下料尺寸",
				"材质",
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
			WorkersubmitFormMap obj = workersubmitFormMaps.get(i);
			content[i][0] = obj.get("contractNumber")+"";
			content[i][1] = obj.get("mapNumber")+"";
			content[i][2] = obj.get("goodName")+"";
			content[i][3] = obj.get("goodSize")+"";
			content[i][4] = obj.get("blankSize")+"";
			content[i][5] = obj.get("materialqualityName")+"";
			content[i][6] = obj.get("processName")+"";
			content[i][7] = obj.get("accountShow")+"";
			content[i][8] = obj.get("completeAmount")+"";
			content[i][9] = obj.get("badAmount")+"";
			content[i][10] = obj.get("completeTime")+"";
			content[i][11] = obj.get("wages")+"";
			content[i][12] = obj.get("deductWages")+"";
			content[i][13] = obj.get("trueWages")+"";
			content[i][14] = obj.get("startTime")+"";

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
	@RequestMapping("findBigShowAll")
	public BlankFormMap findBigShowAll(String clientId,
										  String contractNumber,
										  String state,
										  String processId,
										  String startTime,
										  String endTime,
										  String mapNumber) throws Exception {
		BlankFormMap blankFormMap = getFormMap(BlankFormMap.class);
		String origin=getPara("origin");
		List<BlankFormMap> blankFormMaps=new ArrayList<>();
		if(origin==null){
		}else{
			if(state==null||state.equals("")||state.equals("不限")){
				state="";
			}
			if(clientId==null||clientId.equals("")||clientId.equals("不限")){
				clientId="";
			}

			if(processId==null||processId.equals("")||processId.equals("不限")){
				processId="";
			}
			if(endTime!=null&&!endTime.equals("")){
				endTime=ToolCommon.addDay(endTime,1);
			}

			blankFormMap.set("clientId",clientId);
			blankFormMap.set("contractNumber",contractNumber);
			blankFormMap.set("state",state);
			blankFormMap.set("processId",processId);
			blankFormMap.set("startTime",startTime);
			blankFormMap.set("endTime",endTime);
			blankFormMap.set("mapNumber",mapNumber);
			blankFormMaps=blankMapper.findBigShowByContent(blankFormMap);
		}

		blankFormMap.set("rows",blankFormMaps);
		return blankFormMap;
	}

	@ResponseBody
	@RequestMapping("findByOriginAndOrderDetailsId")
	public BlankFormMap findByOriginAndOrderDetailsId(String origin,String orderdetailsId	) throws Exception {
		BlankFormMap blankFormMap = getFormMap(BlankFormMap.class);
		blankFormMap.set("origin",origin);
		blankFormMap.set("orderdetailsId",orderdetailsId);
		List<BlankFormMap> departmentFormMapList=blankMapper.findByOriginAndOrderdetailsId(blankFormMap);
		blankFormMap.set("rows",departmentFormMapList);
		return blankFormMap;
	}

	@ResponseBody
	@RequestMapping("findFeedByPage")
	public BlankFormMap findFeedByPage(String content,
								   String printTime,
								   String materialQuality,
								   String blankSize,
								   String clientId,
								   String makeTime,
								   String deliveryTime,
								   String state) throws Exception {
		String page=getPara("page");
		String rows=getPara("rows");
		String sort=getPara("sort");
		String order=getPara("order");
		if(sort==null){
//			sort="deliveryTime";
//			order="desc";
		}
		BlankFormMap blankFormMap = getFormMap(BlankFormMap.class);
		if(content==null){
			content="";
		}
		if(state==null||state.equals("")||state.equals("不限")){
			state="";
		}
		if(clientId==null||clientId.equals("")){
			clientId="不限";
		}
		if(makeTime==null||makeTime.equals("")){
			makeTime="不限";
		}
		if(deliveryTime==null||deliveryTime.equals("")){
			deliveryTime="不限";
		}
		if(blankSize==null||blankSize.equals("")){
			blankSize="不限";
		}
		if(printTime==null||printTime.equals("")){
			printTime="不限";
		}
		if(materialQuality==null||materialQuality.equals("")){
			materialQuality="";
		}
		blankFormMap.set("blankSize",blankSize);
		blankFormMap.set("content",content);
		blankFormMap.set("origin","订单");
		blankFormMap.set("printTime",printTime);
		blankFormMap.set("state",state);
		blankFormMap.set("materialQuality",materialQuality);
		blankFormMap.set("clientId",clientId);
		blankFormMap.set("makeTime",makeTime);
		blankFormMap.set("deliveryTime",deliveryTime);
		blankFormMap.set("origin","补料");
		if(page.equals("1")){
			total=blankMapper.findCountByAllLike(blankFormMap);
		}
		blankFormMap.set("sort",sort);
		blankFormMap.set("order",order);
		blankFormMap=toFormMap(blankFormMap, page, rows,blankFormMap.getStr("orderby"));
		List<BlankFormMap> departmentFormMapList=blankMapper.findByAllLike(blankFormMap);
//		if(order!=null){
//			if(order.equals("desc")){
//				departmentFormMapList=desc(departmentFormMapList);
//			}else{
//				departmentFormMapList=asc(departmentFormMapList);
//			}
//		}
		blankFormMap.set("rows",departmentFormMapList);
		blankFormMap.set("total",total);
		return blankFormMap;
	}



	@RequestMapping("scrapOrderPrintShowUI")
	public String scrapOrderPrintShowUI(Model model,String orderdetailsId,String workersubmitId) throws Exception {
		OrderDetailsFormMap orderDetailsFormMap=orderDetailsMapper.getOrderAndGoodByOrderDetailsId(orderdetailsId);
		WorkersubmitFormMap workersubmitFormMap=workersubmitMapper.findById(workersubmitId);
		ScrapFormMap scrapFormMap=scrapMapper.getByWorkersubmitId(workersubmitId);
		model.addAttribute("scrapFormMap",scrapFormMap);
		model.addAttribute("orderdetails",orderDetailsFormMap);
		model.addAttribute("workersubmitFormMap",workersubmitFormMap);
		UserFormMap userFormMap=getNowUserMessage();
		model.addAttribute("userFormMap",userFormMap);
		model.addAttribute("nowDay",ToolCommon.getNowDay());
		return Common.BACKGROUND_PATH + "/system/blank/scrapOrderPrintShow";
	}


	@ResponseBody
	@RequestMapping("findPrintByPage")
	public BlankFormMap findPrintByPage(String ids) throws Exception {
		ids=ids.substring(0,ids.length()-1);
		BlankFormMap blankFormMap=new BlankFormMap();
		List<BlankFormMap> departmentFormMapList=blankMapper.findByIds(ids);
		blankFormMap.set("rows",departmentFormMapList);
		return blankFormMap;
	}






	@ResponseBody
	@RequestMapping("editEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="组织架构",methods="员工管理-新增")//凡需要处理业务逻辑的.都需要记录操作日志
	public String editEntity(String entity) throws Exception {
		boolean isUpdate=false;
		List<BlankFormMap> list = (List) ToolCommon.json2ObjectList(entity, BlankFormMap.class);
		String nowtime=ToolCommon.getNowTime();
		UserFormMap userFormMap=getNowUserMessage();
		String userId=String.valueOf(userFormMap.getInt("id"));
		String amount="";
		float amountF=0;
		for(int ii=0;ii<list.size();ii++){
			BlankFormMap blankFormMap11=list.get(ii);
			String id=blankFormMap11.get("id")+"";
			String orderdetailsId=blankFormMap11.get("orderdetailsId")+"";
			blankFormMap11.set("modifytime",nowtime);
			blankFormMap11.set("userId",userId);
			if(id==null||id.equals("")||id.equals("null")){
				blankFormMap11.remove("id");
				blankMapper.addEntity(blankFormMap11);
			}else{
				BlankFormMap blankFormMap22=blankMapper.findById(id);
				String amount22=blankFormMap22.get("amount")+"";
				float amountF22=ToolCommon.StringToFloat(amount22);
				amount=blankFormMap11.get("amount")+"";
				amountF=ToolCommon.StringToFloat(amount);
				if(amountF22!=amountF){
					isUpdate=true;
					String blankSize=blankFormMap11.get("blankSize")+"";
					String outSize="";
					String length="";
					String inside="";
					try {
						outSize=blankSize.split("\\*")[0];
						outSize=outSize.replace("Φ","");
						inside=blankSize.split("\\*")[1];
						if(ToolCommon.isContain(inside,"Φ")){
							inside=inside.replace("Φ","");
							length=blankSize.split("\\*")[2];
							String REGEX ="[^(0-9).]";
							length= Pattern.compile(REGEX).matcher(length).replaceAll("").trim();
						}else{
							if(blankSize.split("\\*").length==2){
								length=inside;
								inside="0";
							}else{
								length=blankSize.split("\\*")[2];
							}
						}
					}catch (Exception e){
						outSize="0";
						inside="0";
						length="0";
					}
					float outsideF=ToolCommon.StringToFloat(outSize);
					float insideF=ToolCommon.StringToFloat(inside);
					float lengthF=ToolCommon.StringToFloat(length);


					double lengthOrderD=amountF*lengthF/1000;
					lengthOrderD = ToolCommon.Double4(lengthOrderD);
					blankFormMap11.set("length",lengthOrderD+"");
					double weight=outsideF*outsideF*lengthOrderD*(0.00617/1000)-insideF*insideF*lengthOrderD*(0.00617/1000);
					weight = ToolCommon.Double4(weight);
					blankFormMap11.set("weight",weight+"");
				}

				blankMapper.editEntity(blankFormMap11);
			}
			if(isUpdate){
				SystemconfigFormMap systemconfigFormMapWages=systemconfigMapper.findByAttribute("name","dailyWages",SystemconfigFormMap.class).get(0);
				String dailyWages=systemconfigFormMapWages.get("content")+"";
				float dailyWagesF=ToolCommon.StringToFloat(dailyWages);
				BlankFormMap blankFormMap1 = getFormMap(BlankFormMap.class);
				blankFormMap1.set("orderdetailsId",orderdetailsId);
				String blankId=blankFormMap11.get("id")+"";
				blankProcessMapper.deleteByAttribute("blankId",blankId,BlankProcessFormMap.class);
				OrderDetailsFormMap orderDetailsFormMap1=orderDetailsMapper.findById(orderdetailsId);
				String goodId=orderDetailsFormMap1.get("goodId")+"";
				List<GoodProcessFormMap> goodProcessFormMaps=goodProcessMapper.findByGoodId(goodId);
				for(int jj=0;jj<goodProcessFormMaps.size();jj++) {
					GoodProcessFormMap goodProcessFormMap = goodProcessFormMaps.get(jj);
					String goodprocessId = goodProcessFormMap.get("id") + "";
					BlankProcessFormMap blankProcessFormMap = new BlankProcessFormMap();
					blankProcessFormMap.set("amount", amount);
					blankProcessFormMap.set("unreceiveAmount", amount);
					String artificial = goodProcessFormMap.get("artificial") + "";
					float artificialF = ToolCommon.StringToFloat(artificial);
					double planneedDay = artificialF * amountF / dailyWagesF;
					double neesDay = 0;
					int planneedDayInt = (new Double(planneedDay)).intValue();
					if (planneedDay == planneedDayInt) {
						neesDay = planneedDay;
					} else {
						neesDay = (planneedDay) + 1;
					}
					int neesDayInt = (new Double(neesDay)).intValue();
					blankProcessFormMap.set("planneedDay", neesDayInt);
					blankProcessFormMap.set("goodprocessId", goodprocessId);

					blankProcessFormMap.set("blankId", blankId);
					blankProcessFormMap.remove("id");
					blankProcessMapper.addEntity(blankProcessFormMap);
					String startQRCode = blankProcessFormMap.get("id") + "start";
					blankProcessFormMap.set("startQRCode", startQRCode);
					String endQRCode = blankProcessFormMap.get("id") + "end";
					blankProcessFormMap.set("endQRCode", endQRCode);
					blankProcessMapper.editEntity(blankProcessFormMap);
				}
			}
		}
		return "success";
	}
	@ResponseBody
	@RequestMapping("editWagesEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="组织架构",methods="员工管理-新增")//凡需要处理业务逻辑的.都需要记录操作日志
	public String editWagesEntity(String entity) throws Exception {
		List<WorkersubmitFormMap> list = (List) ToolCommon.json2ObjectList(entity, WorkersubmitFormMap.class);
		for(int ii=0;ii<list.size();ii++){
			WorkersubmitFormMap workersubmitFormMap=list.get(ii);
			String id=workersubmitFormMap.get("id")+"";
			String deductWages=workersubmitFormMap.get("deductWages")+"";
			String trueWages=workersubmitFormMap.get("trueWages")+"";
			String wages=workersubmitFormMap.get("wages")+"";
			String deductRate=workersubmitFormMap.get("deductRate")+"";
			WorkersubmitFormMap workersubmitFormMap1=workersubmitMapper.findById(id);
			workersubmitFormMap1.set("deductWages",deductWages);
			workersubmitFormMap1.set("trueWages",trueWages);
			workersubmitFormMap1.set("wages",wages);
			workersubmitFormMap1.set("deductRate",deductRate);
			workersubmitMapper.editEntity(workersubmitFormMap1);
		}
		return "success";
	}
	@ResponseBody
	@RequestMapping("inputReceiveEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="生产管理",methods="接收码打印-分配")//凡需要处理业务逻辑的.都需要记录操作日志
	public String inputReceiveEntity(String entity) throws Exception {
		String userId=getPara("userId");
		List<BlankProcessFormMap> list = (List) ToolCommon.json2ObjectList(entity, BlankProcessFormMap.class);
		for(int ii=0;ii<list.size();ii++){
			BlankProcessFormMap blankProcessFormMap=list.get(ii);
			String id=blankProcessFormMap.get("id")+"";
			String unreceiveAmount=blankProcessFormMap.get("unreceiveAmount")+"";
			String nowreceiveAmount=blankProcessFormMap.get("nowreceiveAmount")+"";
			BlankProcessFormMap blankProcessFormMap1=blankProcessMapper.findbyFrist("id",id,BlankProcessFormMap.class);
			String blankprocessId=blankProcessFormMap1.get("id")+"";
			blankProcessFormMap1.set("nowreceiveAmount",nowreceiveAmount);
			blankProcessFormMap.set("state","已接收未完成");
			blankProcessFormMap.set("unreceiveAmount",ToolCommon.StringToFloat(unreceiveAmount)-ToolCommon.StringToFloat(nowreceiveAmount));
			blankProcessMapper.editEntity(blankProcessFormMap);
			String amount=nowreceiveAmount;
			WorkersubmitFormMap workersubmitFormMap=new WorkersubmitFormMap();
			workersubmitFormMap.set("blankprocessId",blankprocessId);
			workersubmitFormMap.set("submiterId",userId);
			workersubmitFormMap.set("amount",amount);
			workersubmitFormMap.set("startTime",ToolCommon.getNowTime());
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
			workersubmitFormMap.set("planneedDay", neesDayInt);
			double distributionWages=ToolCommon.StringToInteger(amount)*artificialF;
			workersubmitFormMap.set("distributionWages",distributionWages);
			workersubmitMapper.addEntity(workersubmitFormMap);

		}
		return "success";
	}

	@ResponseBody
	@RequestMapping("completeReceiveEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="组织架构",methods="员工管理-新增")//凡需要处理业务逻辑的.都需要记录操作日志
	public String completeReceiveEntity(String entity) throws Exception {
		List<WorkersubmitFormMap> list = (List) ToolCommon.json2ObjectList(entity,WorkersubmitFormMap.class);
		for(int i=0;i<list.size();i++){
			String blankprocessId=list.get(i).getStr("blankprocessId");
			String completeAmount=list.get(i).getStr("completeAmount");
			String badAmount=list.get(i).getStr("badAmount");
			BlankProcessFormMap blankProcessFormMap=blankProcessMapper.findbyFrist("id",blankprocessId,BlankProcessFormMap.class);
			String completeAmountBP=blankProcessFormMap.getStr("completeAmount");
			String badAmountBP=blankProcessFormMap.getStr("badAmount");
			blankProcessFormMap.set("completeAmount",ToolCommon.StringToInteger(completeAmount)+ToolCommon.StringToInteger(completeAmountBP));
			blankProcessFormMap.set("badAmount",ToolCommon.StringToInteger(badAmount)+ToolCommon.StringToInteger(badAmountBP));
			blankProcessFormMap.set("nowreceiveAmount","");
			blankProcessMapper.editEntity(blankProcessFormMap);
			WorkersubmitFormMap workersubmitFormMap=list.get(i);
			String receiveAmount=workersubmitFormMap.get("receiveAmount")+"";
			workersubmitFormMap.set("amount",receiveAmount);
			workersubmitMapper.editEntity(workersubmitFormMap);

		}
		return "success";
	}

	@ResponseBody
	@RequestMapping("uploadBlankSize")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="Excel表格上传",methods="订单上传-上传")//凡需要处理业务逻辑的.都需要记录操作日志
	public String uploadBlankSize(@RequestParam(value = "file", required = false) MultipartFile file,
						 HttpServletRequest request) throws Exception {
		String errorMessage="";
		if(file!=null){
			UserFormMap userFormMap=getNowUserMessage();
			String userId=String.valueOf(userFormMap.getInt("id"));
			FileBean fileBean=ToolCommon.uploadExcelFile(file,request);
			Sheet sheet1= ExcelUtil.readRowsAndColumsSheet1(fileBean.getPath());
			for(int i=1;i<=sheet1.getLastRowNum();i++){
				Row row = null;
				row = sheet1.getRow(i);
				int lastColNum=0;
				if(row!=null){
					lastColNum = row.getLastCellNum();
					BlankSizeFormMap blankSizeFormMap1=new BlankSizeFormMap();
					for(int j=0;j<lastColNum;j++){
						Cell cell=row.getCell(j);
						if(cell!=null){
							String value=ExcelUtil.getCellValue(cell);
							if(value==null){
								value="";
							}
							if(j==0){
								if(value.equals("")){
									j=lastColNum;
								}else if(value.equals("结束")){
									i=sheet1.getLastRowNum()+1;
								}
							}
							value= ToolExcel.replaceBlank(value);
							switch (j){
								case 0:
									List<GoodFormMap> goodFormMaps=goodMapper.findByMapNumber(value);
									if(goodFormMaps.size()>0){
										String goodId=goodFormMaps.get(0).get("id")+"";
										String clientId=goodFormMaps.get(0).get("clientId")+"";
										blankSizeFormMap1.set("goodId",goodId);
										blankSizeFormMap1.set("clientId",clientId);
									}else{
										errorMessage=errorMessage+value+"、";
									}
									break;
								case 1:
									String blankSize=value;
									blankSizeFormMap1.set("blankSize",blankSize);
									try{
										String outSize=blankSize.split("\\*")[0];
										outSize=outSize.replace("Φ","");
										String inside=blankSize.split("\\*")[1];
										String length="";
										if(ToolCommon.isContain(inside,"Φ")){
											inside=inside.replace("Φ","");
											length=blankSize.split("\\*")[2];
											String REGEX ="[^(0-9).]";
											length= Pattern.compile(REGEX).matcher(length).replaceAll("").trim();
										}else{
											if(blankSize.split("\\*").length==2){
												length=inside;
												inside="0";
											}else{
												length=blankSize.split("\\*")[2];
											}
										}
										float outsideF=ToolCommon.StringToFloat(outSize);
										float insideF=ToolCommon.StringToFloat(inside);
										float lengthF=ToolCommon.StringToFloat(length);
										double weight=outsideF*outsideF*lengthF*(0.00617/1000)-insideF*insideF*lengthF*(0.00617/1000);
										String weightStr =Common.formatDouble4(weight);
										blankSizeFormMap1.set("blankWeight",weightStr+"");
									}catch (Exception e){
										blankSizeFormMap1.set("blankWeight","0");
									}

									break;
								case 2:
									blankSizeFormMap1.set("remarks1",value);
									break;
								case 3:
									blankSizeFormMap1.set("remarks2",value);
									break;
								case 4:
									blankSizeFormMap1.set("remarks3",value);
									break;
								case 5:
									blankSizeFormMap1.set("isCheck",value);
									break;
							}
						}

					}
					blankSizeFormMap1.set("modifyTime",ToolCommon.getNowTime());
					blankSizeFormMap1.set("userId",userId);
					blankSizeMapper.addEntity(blankSizeFormMap1);
				}
			}
		}
		if(errorMessage.equals("")){
			return "success";
		}else{
			return errorMessage;
		}

	}

	@ResponseBody
	@RequestMapping("feedEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="报表统计",methods="废品统计-生成补料单")//凡需要处理业务逻辑的.都需要记录操作日志
	public String feedEntity(String entity) throws Exception {
//		SystemconfigFormMap systemconfigFormMapHours=systemconfigMapper.findByAttribute("name","dailyWorkingHours",SystemconfigFormMap.class).get(0);
//		String dailyWorkingHours=systemconfigFormMapHours.get("content")+"";
//		float dailyWorkingHoursF=ToolCommon.StringToFloat(dailyWorkingHours);
		SystemconfigFormMap systemconfigFormMapWages=systemconfigMapper.findByName("dailyWages");
//		SystemconfigFormMap systemconfigFormMapWages=systemconfigMapper.findByAttribute("name","dailyWages",SystemconfigFormMap.class).get(0);
		String dailyWages=systemconfigFormMapWages.get("content")+"";
		float dailyWagesF=ToolCommon.StringToFloat(dailyWages);
		String selectList = ToolCommon.json2Object(entity).getString("selectList");
		List<BlankFormMap> list = (List) ToolCommon.json2ObjectList(selectList, BlankFormMap.class);
		String nowtime=ToolCommon.getNowTime();
		UserFormMap userFormMap=getNowUserMessage();
		String userId=String.valueOf(userFormMap.getInt("id"));
		String amount="";
		for(int i=0;i<list.size();i++){
			BlankFormMap blankFormMapS=list.get(i);
			String materialQuality=blankFormMapS.getStr("materialQuality");
			String contractNumber=blankFormMapS.getStr("contractNumber");
			String deliveryTime=blankFormMapS.getStr("deliveryTime");
			String goodId=blankFormMapS.getStr("goodId");
			String blandprocessId=blankFormMapS.get("blankprocessId")+"";
			String orderdetailsId="";
			String blankId="";
			String processId="";
			if(ToolCommon.isNull(blandprocessId)){
				String workersubmitheattreatId=blankFormMapS.get("id")+"";
				workersubmitHeatTreatMapper.updateStateById("已生成补料单",workersubmitheattreatId);
				orderdetailsId=orderDetailsMapper.findIdByContractNumberAndGoodIdAndDeliveryTime(contractNumber,goodId,deliveryTime);
				blankId=blankMapper.findIdByOrderDetailsId(orderdetailsId);
				processId=blankFormMapS.get("processId")+"";
				GoodProcessFormMap goodProcessFormMap=new GoodProcessFormMap();
				goodProcessFormMap.set("goodId",goodId);
				goodProcessFormMap.set("processId",processId);
				List<GoodProcessFormMap> goodProcessFormMaps=goodProcessMapper.findByGoodIdAndProcessId(goodProcessFormMap);
				if(goodProcessFormMaps!=null){
					GoodProcessFormMap goodProcessFormMap1=goodProcessFormMaps.get(0);
					String goodprocessId=goodProcessFormMap1.get("id")+"";
					blandprocessId=blankProcessMapper.findIdByBlankIdAndGoodProcessId(blankId,goodprocessId);
				}

			}
			if(!ToolCommon.isNull(blandprocessId)){
				BlankProcessFormMap blankProcessFormMapS=blankProcessMapper.findbyFrist("id",blandprocessId,BlankProcessFormMap.class);
				blankProcessFormMapS.set("state","废品已补料");
				blankProcessMapper.editEntity(blankProcessFormMapS);
				if(ToolCommon.isNull(orderdetailsId)){
					orderdetailsId=blankFormMapS.get("orderdetailsId")+"";
				}
				String orderAmount=blankFormMapS.get("badAmount")+"";
				amount=orderAmount;
				float amountF=ToolCommon.StringToFloat(amount);
				if(ToolCommon.isContain(amount,".")){
					amount=ToolCommon.StringToInteger(amount)+"";
				}
				BlankSizeFormMap blankSizeFormMap=blankSizeMapper.findByGoodId(goodId);
				BlankFormMap blankFormMap=new BlankFormMap();
				blankFormMap.set("userId",userId);
				blankFormMap.set("modifytime",nowtime);
				blankFormMap.set("materialQuality",materialQuality);
				blankFormMap.set("goodId",goodId);
				blankFormMap.set("orderdetailsId",orderdetailsId);
				String batchNumber=blankMapper.findFeedBatchNumberByOrderdetailsId(orderdetailsId);
				batchNumber="批次"+batchNumber;
				blankFormMap.set("batchNumber",batchNumber);
				blankFormMap.set("amount",amount);
				blankFormMap.set("origin","补料");
				blankFormMap.remove("id");
				if(blankSizeFormMap!=null){
					String length="";
					String blankSize=blankSizeFormMap.get("blankSize")+"";
					blankFormMap.set("blankSize",blankSize);
					String outSize=blankSize.split("\\*")[0];
					String inside=blankSize.split("\\*")[1];
					if(ToolCommon.isContain(inside,"Φ")){
						length=blankSize.split("\\*")[2];
						String REGEX ="[^(0-9).]";
						length= Pattern.compile(REGEX).matcher(length).replaceAll("").trim();
					}else{
						if(blankSize.split("\\*").length==2){
							length=inside;
							inside="0";
						}else{
							length=blankSize.split("\\*")[2];
						}
					}
					float outsideF=ToolCommon.StringToFloat(outSize);
					float insideF=ToolCommon.StringToFloat(inside);
					float lengthF=ToolCommon.StringToFloat(length);
					double lengthOrderD=amountF*lengthF/1000;
					lengthOrderD = ToolCommon.Double4(lengthOrderD);
					blankFormMap.set("length",lengthOrderD+"");
					double weight=outsideF*outsideF*lengthOrderD*(0.00617/1000)-insideF*insideF*lengthOrderD*(0.00617/1000);
					String weightStr =Common.formatDouble4(weight);
					blankFormMap.set("weight",weightStr+"");
				}
				blankMapper.addEntity(blankFormMap);

				blankId=blankFormMap.get("id")+"";

				List<GoodProcessFormMap> goodProcessFormMaps=goodProcessMapper.findByGoodId(goodId);
				for(int j=0;j<goodProcessFormMaps.size();j++){
					GoodProcessFormMap goodProcessFormMap=goodProcessFormMaps.get(j);
					String goodprocessId=goodProcessFormMap.get("id")+"";
					processId=goodProcessFormMap.get("processId")+"";
					if(j==1){
						System.out.print("aaa");
					}
					String artificial=goodProcessFormMap.get("artificial")+"";
					float artificialF=ToolCommon.StringToFloat(artificial);
					double planneedDay=artificialF*amountF/dailyWagesF;
					double neesDay=(planneedDay)+1;
					int neesDayInt= (new Double(neesDay)).intValue();
					BlankProcessFormMap blankProcessFormMap=new BlankProcessFormMap();
					blankProcessFormMap.set("goodprocessId",goodprocessId);
					blankProcessFormMap.set("planneedDay",neesDayInt);
					blankProcessFormMap.set("amount",amount);
					blankProcessFormMap.set("blankId",blankId);
					blankProcessFormMap.remove("id");
					blankProcessMapper.addEntity(blankProcessFormMap);
					String startQRCode=blankProcessFormMap.get("id")+"start";
					blankProcessFormMap.set("startQRCode",startQRCode);
					String endQRCode=blankProcessFormMap.get("id")+"end";
					blankProcessFormMap.set("endQRCode",endQRCode);
					blankProcessMapper.editEntity(blankProcessFormMap);
				}
			}

		}
		return "success";
	}

	@ResponseBody
	@RequestMapping("editTechnologyEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="生产管理",methods="工艺卡-新增")//凡需要处理业务逻辑的.都需要记录操作日志
	public String editTechnologyEntity(String entity,String goodId) throws Exception {
		String result="";
		UserFormMap userFormMap=getNowUserMessage();
		String userId=userFormMap.get("id")+"";
		String nowTime=ToolCommon.getNowTime();
		String rows = ToolCommon.json2Object(entity).getString("rows");
		goodId = ToolCommon.json2Object(entity).getString("goodId");
		List<BlankProcessFormMap> list = (List) ToolCommon.json2ObjectList(rows, BlankProcessFormMap.class);
		for(int i=0;i<list.size();i++){
			BlankProcessFormMap blankProcessFormMap=list.get(i);
			String blankId=blankProcessFormMap.get("blankId")+"";
			String number=blankProcessFormMap.get("number")+"";
			String processName=blankProcessFormMap.get("processName")+"";
			String processId=blankProcessFormMap.get("processId")+"";
			if(processId.equals("")){
				i=list.size();
				result="请选择工序";
			}else{
				ProcessFormMap processFormMap=processMapper.findbyFrist("id",processId,ProcessFormMap.class);
				if(processFormMap!=null){
					processFormMap.set("name",processName);
					processMapper.editEntity(processFormMap);
				}
				String content=blankProcessFormMap.get("content")+"";
				String goodprocessId=blankProcessFormMap.get("goodprocessId")+"";
				if(goodprocessId==null||goodprocessId.equals("null")||goodprocessId.equals("")){
					GoodProcessFormMap goodProcessFormMap=new GoodProcessFormMap();
					goodProcessFormMap.set("userId",userId);
					goodProcessFormMap.set("modifyTime",nowTime);
					goodProcessFormMap.set("goodId",goodId);
					goodProcessFormMap.set("number",number);
					goodProcessFormMap.set("goodId",goodId);
					goodProcessFormMap.set("processId",processId);
					goodProcessFormMap.set("content",content);
					goodProcessMapper.addEntity(goodProcessFormMap);
					goodprocessId=goodProcessFormMap.get("id")+"";
				}else{
					GoodProcessFormMap goodProcessFormMap=goodProcessMapper.findbyFrist("id",goodprocessId,GoodProcessFormMap.class);
					if(goodProcessFormMap!=null){
						goodProcessFormMap.set("processId",processId);
						goodProcessFormMap.set("content",content);
						goodProcessMapper.editEntity(goodProcessFormMap);
						goodprocessId=goodProcessFormMap.get("id")+"";
					}

				}
				String amount=blankProcessFormMap.get("amount")+"";
				String unreceiveAmount=amount;
				blankProcessFormMap.set("unreceiveAmount",unreceiveAmount);
				String blankProcessId=blankProcessFormMap.get("id")+"";
				if(blankProcessId==null||blankProcessId.equals("")||blankProcessId.equals("null")){
					blankProcessFormMap.remove("id");
					blankProcessFormMap.set("goodprocessId",goodprocessId);
					blankProcessFormMap.set("blankId",blankId);
					blankProcessMapper.addEntity(blankProcessFormMap);
					String startQRCode=blankProcessFormMap.get("id")+"start";
					blankProcessFormMap.set("startQRCode",startQRCode);
					String endQRCode=blankProcessFormMap.get("id")+"end";
					blankProcessFormMap.set("endQRCode",endQRCode);
					blankProcessMapper.editEntity(blankProcessFormMap);
				}else{
					blankProcessMapper.editEntity(blankProcessFormMap);
				}
				BlankProcessFormMap blankProcessFormMap1=new BlankProcessFormMap();
				blankProcessFormMap1.set("number",number);
				blankProcessFormMap1.set("blankId",blankId);
				List<BlankProcessFormMap> blankProcessFormMaps=blankProcessMapper.findByBlankIdAndNumber(blankProcessFormMap1);
				for(int ii=0;ii<blankProcessFormMaps.size();ii++){
					BlankProcessFormMap blankProcessFormMap2=blankProcessFormMaps.get(ii);
					blankProcessFormMap2.set("amount",amount);
					blankProcessFormMap2.set("unreceiveAmount",unreceiveAmount);
					blankProcessMapper.editEntity(blankProcessFormMap2);
				}
			}
		}
		if(result.equals("")){
			return "success";
		}else{
			return "error:"+result;
		}

	}

	@ResponseBody
	@RequestMapping("editWorkerSubmitEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="组织架构",methods="员工管理-新增")//凡需要处理业务逻辑的.都需要记录操作日志
	public String editWorkerSubmitEntity(String entity) throws Exception {
		String rows = ToolCommon.json2Object(entity).getString("rows");
		List<WorkersubmitFormMap> list = (List) ToolCommon.json2ObjectList(rows, WorkersubmitFormMap.class);
		for(int i=0;i<list.size();i++){
			String blankprocessId=list.get(i).getStr("blankprocessId");
			String completeAmount=list.get(i).getStr("completeAmount");
			String badAmount=list.get(i).getStr("badAmount");
			BlankProcessFormMap blankProcessFormMap=blankProcessMapper.findById(blankprocessId);
			String completeAmountBP=blankProcessFormMap.getStr("completeAmount");
			String badAmountBP=blankProcessFormMap.getStr("badAmount");
			blankProcessFormMap.set("completeAmount",ToolCommon.StringToInteger(completeAmount)+ToolCommon.StringToInteger(completeAmountBP));
			blankProcessFormMap.set("badAmount",ToolCommon.StringToInteger(badAmount)+ToolCommon.StringToInteger(badAmountBP));
			blankProcessMapper.editEntity(blankProcessFormMap);
			WorkersubmitFormMap workersubmitFormMap=list.get(i);
			int completeAmountIntWorkersubmit=ToolCommon.StringToInteger(workersubmitFormMap.get("completeAmount")+"");
			int badAmountIntWorkersubmit=ToolCommon.StringToInteger(workersubmitFormMap.get("badAmount")+"");
			float deductRate=ToolCommon.StringToFloat(workersubmitFormMap.get("deductRate")+"");
			float taxPrice = ToolCommon.StringToFloat(blankProcessFormMap.getStr("taxPrice") + "");
			float artificial = ToolCommon.StringToFloat(blankProcessFormMap.get("artificial") + "");
			double wages = completeAmountIntWorkersubmit * artificial;
			double deductWages = taxPrice * deductRate * badAmountIntWorkersubmit;
			String wagesStr = ToolCommon.Double2(wages) + "";
			double trueWages = wages - deductWages;
			String deductWagesStr = ToolCommon.Double2(deductWages) + "";
			String trueWagesStr = ToolCommon.Double2(trueWages) + "";
			workersubmitFormMap.set("wages", wagesStr);
			workersubmitFormMap.set("deductWages", deductWagesStr);
			workersubmitFormMap.set("trueWages", trueWagesStr);
			workersubmitMapper.editEntity(workersubmitFormMap);
		}
		return "success";
	}

	@ResponseBody
	@RequestMapping("deleteBlankProcessEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="系统管理",methods="组管理-删除组")//凡需要处理业务逻辑的.都需要记录操作日志
	public String deleteBlankProcessEntity() throws Exception {
			String id = getPara("id");
			String goodprocessId=getPara("goodprocessId");
			goodProcessMapper.deleteById(goodprocessId);
			blankProcessMapper.deleteByAttribute("id",id,BlankProcessFormMap.class);
			return "success";
	}


	@ResponseBody
	@RequestMapping("deleteEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="生产管理",methods="下料单-删除")//凡需要处理业务逻辑的.都需要记录操作日志
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
				BlankFormMap blankFormMap=blankMapper.findById(id);
				if(blankFormMap!=null){
					String state=blankFormMap.get("state")+"";
					if(state!=null&&state.equals("已订料")){
						String length=blankFormMap.get("length")+"";
						String weight=blankFormMap.get("weight")+"";
						String blankSize=blankFormMap.get("blankSize")+"";
						String deliveryTime=blankFormMap.get("deliveryTime")+"";
						String materialQuality=blankFormMap.get("materialQualityName")+"";
						MaterialBuyOrderFormMap materialBuyOrderFormMap=materialBuyOrderMapper.selectByDeliveryDay(deliveryTime);
						if(materialBuyOrderFormMap!=null){
							String materialbuyorderId=materialBuyOrderFormMap.get("id")+"";
							String outerCircle="";
							String inside="";
							if(blankSize!=null&&!blankSize.equals("null")){
								String outSize=blankSize.split("\\*")[0];
								inside=blankSize.split("\\*")[1];

								if(blankSize.split("\\*").length>2){
									outerCircle=outSize+"*"+inside;
									materialQuality=materialQuality.replace(" ","");
									materialQuality=materialQuality+"管";
								}else{
									outerCircle=outSize;
								}
							}else{

							}
							MaterialBuyOrderDetailsFormMap materialBuyOrderDetailsFormMap1=new MaterialBuyOrderDetailsFormMap();
							materialBuyOrderDetailsFormMap1.set("outerCircle",outerCircle);
							materialBuyOrderDetailsFormMap1.set("materialQuality",materialQuality);
							materialBuyOrderDetailsFormMap1.set("materialbuyorderId",materialbuyorderId);
							MaterialBuyOrderDetailsFormMap materialBuyOrderDetailsFormMap=materialBuyOrderDetailsMapper.selectByMaterialQualityAndOuterCircleAndMaterialbuyorderId(materialBuyOrderDetailsFormMap1);
							if(materialBuyOrderDetailsFormMap!=null){
								String buyLength=materialBuyOrderDetailsFormMap.get("buyLength")+"";
								String buyWeight=materialBuyOrderDetailsFormMap.get("buyWeight")+"";
								String buyLengthN=ToolCommon.FloatTo4Float(ToolCommon.StringToFloat(buyLength)-ToolCommon.StringToFloat(length))+"";
								String buyWeightN=ToolCommon.FloatTo4Float(ToolCommon.StringToFloat(buyWeight)-ToolCommon.StringToFloat(weight))+"";
								materialBuyOrderDetailsFormMap.set("buyLength",buyLengthN);
								materialBuyOrderDetailsFormMap.set("buyWeight",buyWeightN);
								materialBuyOrderDetailsMapper.editEntity(materialBuyOrderDetailsFormMap);
							}
						}
					}
				}

				blankMapper.deleteById(id);
			}
			return "success";
		}else{
			return "删除订单密码错误";
		}

	}


	@ResponseBody
	@RequestMapping("editNowDistributionAmountByIds")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="进度查询",methods="修改数量")//凡需要处理业务逻辑的.都需要记录操作日志
	public String editNowDistributionAmountByIds(String entity) throws Exception {
		List<BlankFormMap> list = (List) ToolCommon.json2ObjectList(entity, BlankFormMap.class);
		for(int i=0;i<list.size();i++){
			BlankFormMap blankFormMap=list.get(i);
			String id=blankFormMap.get("progressSearchId")+"";
			String nowDistributionAmount=blankFormMap.get("nowDistributionAmount")+"";
			String waitDistributionAmount=blankFormMap.get("waitDistributionAmount")+"";
			progressSearchMapper.updateNowDistributionAmountById(waitDistributionAmount,nowDistributionAmount,id);
			HeatTreatFormMap heatTreatFormMap=heatTreatMapper.findByProgressSearchId(id);
			heatTreatFormMap.set("distributionAmount",nowDistributionAmount);
			heatTreatMapper.editEntity(heatTreatFormMap);
		}
		return "success";
	}

	@ResponseBody
	@RequestMapping("makeHeattreatEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="生产管理",methods="下料单-生成热处理情况")//凡需要处理业务逻辑的.都需要记录操作日志
	public String makeHeattreatEntity(String origin) throws Exception {
			String ids = getPara("ids");
			ids=ids.substring(0,ids.length()-1);
			List<BlankFormMap> blankFormMaps=blankMapper.findHeatTreatByIds(ids);
			for(int i=0;i<blankFormMaps.size();i++){
				BlankFormMap blankFormMap=blankFormMaps.get(i);
				String workersubmitId=blankFormMap.get("workersubmitId")+"";
				workersubmitMapper.updateIsHeattreatTrueById(workersubmitId);
				addHeartTreatByBlankFormMap(origin,blankFormMap,getNowUserMessage().get("id")+"");
			}
			return "success";
	}



	@ResponseBody
	@RequestMapping("makeCodeEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="生产管理",methods="下料单-生成编号")//凡需要处理业务逻辑的.都需要记录操作日志
	public String makeCodeEntity() throws Exception {
			String ids = getPara("ids");
			String code=getPara("code");
			ids=ids.substring(0,ids.length()-1);
			BlankFormMap blankFormMap=new BlankFormMap();
			blankFormMap.set("code",code);
			blankFormMap.set("ids",ids);
			blankMapper.updateBlankCodeByIdsAndCode(blankFormMap);
			return "success";
	}
	@ResponseBody
	@RequestMapping("deleteEntityGetList")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="系统管理",methods="组管理-删除组")//凡需要处理业务逻辑的.都需要记录操作日志
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
				workersubmitMapper.deleteById(id);
			}
			return "success";
		}else{
			return "删除订单密码错误";
		}

	}

	@ResponseBody
	@RequestMapping("inputEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="生产管理",methods="工艺卡-入库")//凡需要处理业务逻辑的.都需要记录操作日志
	public String inputEntity(String amount) throws Exception {
		UserFormMap userFormMap=getNowUserMessage();
		String userId=userFormMap.get("id")+"";
		String nowTime=ToolCommon.getNowTime();
			String orderdetailsId = getPara("orderdetailsId");
			OrderDetailsFormMap orderDetailsFormMap=orderDetailsMapper.findById(orderdetailsId);
			String state=orderDetailsFormMap.getStr("state");
			String inputAmountNow=amount;
			if(ToolCommon.isContain(state,"已入库")){
				String inputAmount=state.replace("已入库","");
				inputAmountNow=(ToolCommon.StringToInteger(inputAmount)+ToolCommon.StringToInteger(inputAmountNow))+"";
			}
			orderDetailsFormMap.set("state","已入库"+inputAmountNow);
			orderDetailsMapper.editEntity(orderDetailsFormMap);
			SendInputFormMap sendInputFormMap=new SendInputFormMap();
			sendInputFormMap.set("orderdetailsId",orderdetailsId);
			sendInputFormMap.set("amount",amount);
			sendInputFormMap.set("modifyuserId",userId);
			sendInputFormMap.set("modifyTime",nowTime);
			sendInputFormMap.set("origin","入库");
			sendInputMapper.addEntity(sendInputFormMap);
			addStock(orderDetailsFormMap,amount);
			return "success";
	}

	public void addStock(OrderDetailsFormMap orderDetailsFormMap,String amount){
		try{
			String nowTime=ToolCommon.getNowTime();
			String goodId=orderDetailsFormMap.get("goodId")+"";
			String taxPrice=goodMapper.findTaxPriceById(goodId);
			StockFormMap stockFormMap=stockMapper.findByGoodId(goodId);
			if(stockFormMap==null){
				stockFormMap=new StockFormMap();
				double money=ToolCommon.StringToFloat(taxPrice)*ToolCommon.StringToFloat(amount);
				stockFormMap.set("goodId",goodId);
				stockFormMap.set("amount",amount);
				stockFormMap.set("price",taxPrice);
				stockFormMap.set("money",ToolCommon.Double2(money));
				stockFormMap.set("userId",getNowUserMessage().get("userId"));
				stockFormMap.set("modifyTime",nowTime);
				stockMapper.addEntity(stockFormMap);
			}else{
				String stockAmount=stockFormMap.get("amount")+"";
				int stockAmountNow=ToolCommon.StringToInteger(amount)+ToolCommon.StringToInteger(stockAmount);
				String money=ToolCommon.Double2(stockAmountNow*ToolCommon.StringToFloat(taxPrice))+"";
				stockFormMap.set("amount",stockAmountNow);
				stockFormMap.set("money",money);
				stockFormMap.set("userId",getNowUserMessage().get("userId"));
				stockFormMap.set("modifyTime",nowTime);
				stockMapper.editEntity(stockFormMap);
			}
		}catch (Exception e){

		}

	}
	@ResponseBody
	@RequestMapping("uploadInputStock")
	@SystemLog(module="报表统计",methods="入库统计-导入数据")
	public String uploadInputStock(@RequestParam(value = "file", required = false) MultipartFile file,
									HttpServletRequest request) throws Exception {
		String errorMessage="";
		boolean isAdd=true;
		if(file!=null){
			FileBean fileBean=ToolCommon.uploadExcelFile(file,request);
			Sheet sheet1= ExcelUtil.readRowsAndColumsSheet1(fileBean.getPath());
			for(int i=1;i<=sheet1.getLastRowNum();i++){
				if(i==7){
					System.out.print("111");
				}
				Row row = null;
				row = sheet1.getRow(i);
				int lastColNum=0;
				if(row!=null){
					lastColNum = row.getLastCellNum();
					String contractNumber="";
					String mapNumber="";
					String amount="";
					String price="";
					OrderDetailsFormMap orderDetailsFormMap=null;
					if(lastColNum>0){
						for(int j=1;j<9;j++){
							Cell cell=row.getCell(j);
							if(cell!=null){
								String value=ExcelUtil.getCellValue(cell);
								if(value==null){
									value="";
								}
								if(j==1){
									if(value.equals("")){
										j=lastColNum;
										isAdd=false;
										i=sheet1.getLastRowNum()+1;
									}else if(value.equals("结束")){
										i=sheet1.getLastRowNum()+1;
										isAdd=false;
									}
								}
								value= ToolExcel.replaceBlank(value);
								switch (j){
									case 1:
										contractNumber=value.replace(" ","");
										contractNumber=contractNumber.replace(",","");
										break;
									case 4:
										mapNumber=value.replace(" ","");
										mapNumber=mapNumber.replace(",","");
										if(mapNumber.equals("01.1.5-11A")){
											System.out.print("aaa");
										}
										int count=orderDetailsMapper.getCountByContractNumberAndMapNumber(contractNumber,mapNumber);
										if(count!=1){
											errorMessage=errorMessage+"订单号:"+contractNumber+"图号:"+mapNumber+"不唯一或不存在<br>";
											isAdd=false;
											j=lastColNum;
										}else {
											isAdd=true;
											orderDetailsFormMap=orderDetailsMapper.getGoodIdAndOrderdetailsIdByContractNumberAndMapNumber(contractNumber,mapNumber);
										}
										break;
									case 7:
										amount=value;
										break;
									case 8:
										price=value;
										break;
								}
							}else{
								isAdd=false;
								j=lastColNum;
							}
						}
						if(isAdd){//开发中
							orderDetailsFormMap.set("taxPrice",price);
							addStock(orderDetailsFormMap,amount);
							SendInputFormMap sendInputFormMap=new SendInputFormMap();
							sendInputFormMap.set("amount",amount);
							sendInputFormMap.set("orderdetailsId",orderDetailsFormMap.get("id")+"");
							sendInputFormMap.set("modifyuserId",getNowUserMessage().get("id"));
							sendInputFormMap.set("modifyTime",ToolCommon.getNowTime());
							sendInputFormMap.set("origin","入库");
							sendInputFormMap.remove("id");
							sendInputMapper.addEntity(sendInputFormMap);
						}
					}else{
						i=sheet1.getLastRowNum()+1;
					}
				}
			}
		}
		if(errorMessage.equals("")){
			return "success";
		}else{
			return errorMessage;
		}
	}


	@ResponseBody
	@RequestMapping("completeEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="生产管理",methods="工艺卡-确认完成")//凡需要处理业务逻辑的.都需要记录操作日志
	public String completeEntity(String orderdetailsId,String origin) throws Exception {
		BlankFormMap blankFormMap=new BlankFormMap();
		blankFormMap.set("orderdetailsId",orderdetailsId);
		blankFormMap.set("origin",origin);
		blankMapper.updateBlankStateByIdAndOrigin(blankFormMap);
		BlankFormMap blankFormMap1=blankMapper.findAllCountAndCompleteCountByOrderdetailsId(orderdetailsId);
		String allCount=blankFormMap1.get("allCount")+"";
		String completeCount=blankFormMap1.get("completeCount")+"";
		if(allCount.equals(completeCount)){
			orderDetailsMapper.updateStateById(orderdetailsId);
		}
		return "success";
	}


	@ResponseBody
    @RequestMapping("modifyPrintTime")
    @Transactional(readOnly=false)//需要事务操作必须加入此注解
    @SystemLog(module="系统管理",methods="组管理-删除组")//凡需要处理业务逻辑的.都需要记录操作日志
    public String modifyPrintTime() throws Exception {
            String ids = getPara("ids");
            String[] idsStr=ids.split(",");
            for(int i=0;i<idsStr.length;i++){
                BlankFormMap blankFormMap=blankMapper.findById(idsStr[i]);
                blankFormMap.set("printTime",ToolCommon.getNowDay());
                blankMapper.editEntity(blankFormMap);
            }
            return "success";
    }

	@ResponseBody
	@RequestMapping("deleteWorkerSubmitEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="系统管理",methods="组管理-删除组")//凡需要处理业务逻辑的.都需要记录操作日志
	public String deleteWorkerSubmitEntity() throws Exception {
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
				workersubmitMapper.deleteById(id);
			}
			return "success";
		}else{
			return "删除密码错误";
		}

	}

}