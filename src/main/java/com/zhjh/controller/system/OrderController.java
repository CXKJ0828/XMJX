package com.zhjh.controller.system;

import com.zhjh.annotation.SystemLog;
import com.zhjh.bean.ComboboxEntity;
import com.zhjh.bean.FileBean;
import com.zhjh.bean.OrderEntityChildsBean;
import com.zhjh.controller.index.BaseController;
import com.zhjh.entity.*;
import com.zhjh.file.ExcelUtils;
import com.zhjh.mapper.*;
import com.zhjh.tool.ToolCommon;
import com.zhjh.tool.ToolExcel;
import com.zhjh.util.*;
import org.apache.commons.beanutils.DynaBean;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.core.annotation.Order;
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
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 
 * @author lanyuan 2014-11-19
 * @Email: mmm333zzz520@163.com
 * @version 3.0v
 */
@Controller
@RequestMapping("/order/")
public class OrderController extends BaseController {
	@Inject
	private OrderMapper orderMapper;

	@Inject
	private ProcessMapper processMapper;

	@Inject
	private SystemconfigMapper systemconfigMapper;

	@Inject
	private MaterialBuyOrderMapper materialBuyOrderMapper;


	@Inject
	private OrderDetailsMapper orderDetailsMapper;

	@Inject
	private MaterialBuyOrderDetailsMapper materialBuyOrderDetailsMapper;

	@Inject
	private MaterialbuyorderdetailsBlankMapper materialbuyorderdetailsBlankMapper;

	@Inject
	private SendInputMapper sendInputMapper;

	@Inject
	private StockMapper stockMapper;

	@Inject
	private BlankSizeMapper blankSizeMapper;

	@Inject
	private BlankMapper blankMapper;

	@Inject
	private GoodProcessMapper goodProcessMapper;

	@Inject
	private BlankProcessMapper blankProcessMapper;

	@Inject
	private OrderGoodCodeMapper orderGoodCodeMapper;

	@Inject
	private OrderDetailsProcessMapper orderDetailsProcessMapper;

	@Inject
	private WorkersubmitMapper workersubmitMapper;

	@Inject
	private ClientMapper clientMapper;

	@Inject
	private GoodMapper goodMapper;



	@RequestMapping("listUI")
	public String listUI(Model model) throws Exception {
		model.addAttribute("res", findByRes());
		return Common.BACKGROUND_PATH + "/system/order/list";
	}
	@RequestMapping("ordertrack")
	@SystemLog(module="???????????????",methods="???????????????-????????????")
	public String ordertrack(Model model) throws Exception {
		model.addAttribute("res", findByRes());
		return Common.BACKGROUND_PATH + "/system/order/ordertrack";
	}

	@ResponseBody
	@RequestMapping("findOrderTrackByPage")
	@SystemLog(module="???????????????",methods="??????????????????")
	public OrderDetailsFormMap findByPage(String clientId,
												 String makeTimestart,
												 String makeTimeend,
												 String deliveryTimestart,
												 String deliveryTimeend,
												 String goodName,
												 String materialQuality,
												 String blankIsFinish,
												 String turnerIsFinish,
												 String carburizationIsFinish,
												 String feedIsFinish,
												 String content,
										  String isWarning
	) throws Exception {
		String page=getPara("page");

		String rows=getPara("rows");
		String origin=getPara("origin");

		String sort=getPara("sort");
		String order=getPara("order");
		if(sort==null){
			sort="deliveryTime";
			order="asc";
		}

		if(!ToolCommon.isNull(deliveryTimeend)){
			deliveryTimeend=ToolCommon.addDay(deliveryTimeend,1);
		}
		if(!ToolCommon.isNull(makeTimeend)){
			makeTimeend=ToolCommon.addDay(makeTimeend,1);
		}
		OrderDetailsFormMap orderDetailsFormMap =new OrderDetailsFormMap();
		orderDetailsFormMap.set("isWarning",isWarning);
		orderDetailsFormMap.set("clientId",clientId);
		orderDetailsFormMap.set("makeTimestart",makeTimestart);
		orderDetailsFormMap.set("makeTimeend",makeTimeend);
		orderDetailsFormMap.set("deliveryTimestart",deliveryTimestart);
		orderDetailsFormMap.set("deliveryTimeend",deliveryTimeend);
		orderDetailsFormMap.set("goodName",goodName);
		orderDetailsFormMap.set("materialQuality",materialQuality);
		orderDetailsFormMap.set("blankIsFinish",blankIsFinish);
		orderDetailsFormMap.set("turnerIsFinish",turnerIsFinish);
		orderDetailsFormMap.set("carburizationIsFinish",carburizationIsFinish);
		orderDetailsFormMap.set("feedIsFinish",feedIsFinish);
		orderDetailsFormMap.set("content",content);
		float amountSum=orderDetailsMapper.getTrackAmountSum(orderDetailsFormMap);
		if(page.equals("1")){
			total=orderDetailsMapper.getTrackCount(orderDetailsFormMap);
		}
		orderDetailsFormMap.set("sort",sort);
		orderDetailsFormMap.set("order",order);
		orderDetailsFormMap=toFormMap(orderDetailsFormMap, page, rows,orderDetailsFormMap.getStr("orderby"));
		List<OrderDetailsFormMap> departmentFormMapList=orderDetailsMapper.getTrackContent(orderDetailsFormMap);
		orderDetailsFormMap.set("rows",departmentFormMapList);
		orderDetailsFormMap.set("total",total);
		orderDetailsFormMap.set("amountSum",amountSum);
		return orderDetailsFormMap;
	}
	@RequestMapping("inputStatistics")
	@SystemLog(module="????????????",methods="????????????-????????????")
	public String inputStatistics(Model model) throws Exception {
		model.addAttribute("res", findByRes());
		return Common.BACKGROUND_PATH + "/system/order/inputStatistics";
	}

	@RequestMapping("expectOrder")
	@SystemLog(module="???????????????",methods="???????????????-????????????")
	public String expectOrder(Model model) throws Exception {
		model.addAttribute("res", findByRes());
			String startTime=ToolCommon.getNowDay();
			String endTime=ToolCommon.addDay(startTime,10);
		model.addAttribute("startTime",startTime);
		model.addAttribute("endTime",endTime);
		String startTimeShow=ToolCommon.getNowDay();
		String endTimeShow=ToolCommon.addDay(startTimeShow,7);
		model.addAttribute("startTimeShow",startTimeShow);
		model.addAttribute("endTimeShow",endTimeShow);
		return Common.BACKGROUND_PATH + "/system/order/expectOrder";
	}

	@RequestMapping("expectClientGood")
	@SystemLog(module="???????????????",methods="???????????????-????????????")
	private String expectClientGood(Model model) throws Exception {
		model.addAttribute("res", findByRes());
		model.addAttribute("month",ToolCommon.getNowMonth());
		return Common.BACKGROUND_PATH + "/system/order/expectClientGood";
	}

	@RequestMapping("productStatistics")
	@SystemLog(module="????????????",methods="????????????-????????????")
	private String productStatistics(Model model) throws Exception {

		model.addAttribute("res", findByRes());
		model.addAttribute("month",ToolCommon.getNowMonth());
		return Common.BACKGROUND_PATH + "/system/order/productStatistics";
	}
	@RequestMapping(value = "exportProductStatisticsAll")
	@ResponseBody
	@SystemLog(module="????????????",methods="????????????-????????????")
	public void exportProductStatisticsAll(HttpServletResponse response,String entity) throws Exception {
		String clientId=ToolCommon.json2Object(entity).getString("clientId");
		String clientName=ToolCommon.json2Object(entity).getString("clientName");
		String month=ToolCommon.json2Object(entity).getString("month");
		String clientDefault=getPara("clientDefault");
		if(ToolCommon.isNull(clientId)){
			clientId=clientDefault;
		}
		if(ToolCommon.isNull(month)){
			month=ToolCommon.getNowMonth();
		}
		List<String> processList=new ArrayList<>();
		String[] namesXL = {"??????"};
		String processIdsXL = processMapper.findIdsByNames(namesXL);//??????
		processList.add(processIdsXL);

		String[] namesReCl = {"?????????","?????????"};
		String processIdsReCl = processMapper.findIdsByNames(namesReCl);//?????????
		processList.add(processIdsReCl);


		String[] namesC = {"???","??????","??????","??????"};
		String processIdsC = processMapper.findIdsByNames(namesC);//???
		processList.add(processIdsC);

		String[] namesX = {"???","???????????????","???????????????"};
		String processIdsX = processMapper.findIdsByNames(namesX);//???
		processList.add(processIdsX);

		String[] namesQ = {"???"};
		String processIdsQ = processMapper.findIdsByNames(namesQ);//???
		processList.add(processIdsQ);

		String[] namesM = {"?????????","?????????","?????????","?????????","??????","??????","??????","??????"};
		String processIdsM = processMapper.findIdsByNames(namesM);//???
		processList.add(processIdsM);

		String[] namesXQG = {"?????????","??????","??????"};
		String processIdsXQG= processMapper.findIdsByNames(namesXQG);//?????????
		processList.add(processIdsXQG);

		String[] namesYC= {"??????"};
		String processIdsYC= processMapper.findIdsByNames(namesYC);//??????
		processList.add(processIdsYC);

		List<WorkersubmitFormMap> workersubmitFormMaps=new ArrayList<>();
		for(int i=0;i<processList.size();i++){
			String processIds=processList.get(i);
			String process="";
			switch (i){
				case 0:process="??????";break;
				case 1:process="?????????";break;
				case 2:process="???";break;
				case 3:process="???";break;
				case 4:process="???";break;
				case 5:process="???";break;
				case 6:process="?????????";break;
				case 7:process="??????";break;
			}
			WorkersubmitFormMap workersubmitFormMap=new WorkersubmitFormMap();
			workersubmitFormMap.set("process",process);
			for(int j=1;j<32;j++){
				String day="";
				if(j<10){
					day="0"+j;
				}else{
					day=j+"";
				}
				String  showAmount=workersubmitMapper.findProductAmountByClinetAndProcessIdsAndDeliveryTime(processIds,month+"-"+day,clientId);
				workersubmitFormMap.set("amount"+j,showAmount);
			}
			String  showAmount=workersubmitMapper.findProductAmountByClinetAndProcessIdsAndDeliveryTime(processIds,month,clientId);
			workersubmitFormMap.set("amountSum",showAmount);
			workersubmitFormMaps.add(workersubmitFormMap);
		}
		String[] title = new String[33];
		title[0]="??????";
		for(int i=1;i<32;i++){
			title[i]=i+"???";
		}
		title[32]="??????";

		//excel?????????
		String fileName = clientName+"-????????????" + System.currentTimeMillis() + ".xls";

		//sheet???
		String sheetName = "sheet1";

		String [][] content = new String[workersubmitFormMaps.size()][title.length];

		for (int i = 0; i < workersubmitFormMaps.size(); i++) {
			content[i] = new String[title.length];
			WorkersubmitFormMap obj = workersubmitFormMaps.get(i);
			content[i][0] = obj.get("process")+"";
			for(int j=0;j<31;j++){
				content[i][j+1] = obj.get("amount"+(j+1))+"";
			}
			content[i][32] = obj.get("amountSum")+"";
		}

		//??????HSSFWorkbook
		HSSFWorkbook wb = ExcelUtils.getHSSFWorkbook(clientName+"-????????????",sheetName, title, content, null);

		//??????????????????
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

	@RequestMapping(value = "exportClientGoodAll")
	@ResponseBody
	@SystemLog(module="???????????????",methods="???????????????-????????????")
	public void exportClientGoodAll(HttpServletResponse response,String entity) throws Exception {
		String clientId=ToolCommon.json2Object(entity).getString("clientId");
		String mapNumber=ToolCommon.json2Object(entity).getString("mapNumber");
		String month=ToolCommon.json2Object(entity).getString("month");
		if(ToolCommon.isNull(month)){
			month=ToolCommon.getNowMonth();
		}
		OrderDetailsFormMap orderDetailsFormMap1=new OrderDetailsFormMap();
		orderDetailsFormMap1.set("clientId",clientId);
		orderDetailsFormMap1.set("mapNumber",mapNumber);
		if(ToolCommon.isNull(month)){
			month=ToolCommon.getNowMonth();
		}
		orderDetailsFormMap1.set("month",month);
		List<OrderDetailsFormMap> orderDetailsFormMaps=orderDetailsMapper.getExpectClientGood(orderDetailsFormMap1);
		String[] title = new String[36];
		title[0]="??????";
		title[1]="??????";
		title[2]="????????????";
		title[3]="????????????";
		for(int i=1;i<32;i++){
			title[i+3]=i+"???";
		}
		title[35]="??????";

		//excel?????????
		String fileName = "???????????????" + System.currentTimeMillis() + ".xls";

		//sheet???
		String sheetName = "sheet1";

		String [][] content = new String[orderDetailsFormMaps.size()][title.length];

		for (int i = 0; i < orderDetailsFormMaps.size(); i++) {
			content[i] = new String[title.length];
			OrderDetailsFormMap obj = orderDetailsFormMaps.get(i);
			content[i][0] = obj.get("fullName")+"";
			content[i][1] = obj.get("mapNumber")+"";
			content[i][2] = obj.get("goodName")+"";
			content[i][3] = obj.get("goodSize")+"";
			for(int j=0;j<31;j++){
				content[i][j+4] = obj.get("amount"+(j+1))+"";
			}
			content[i][35] = obj.get("amountSum")+"";
		}

		//??????HSSFWorkbook
		HSSFWorkbook wb = ExcelUtils.getHSSFWorkbook("???????????????",sheetName, title, content, null);

		//??????????????????
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
	@RequestMapping("findInputStatisticsByPage")
	@SystemLog(module="????????????",methods="????????????-????????????")
	public SendInputFormMap findBadgoodStatisticsByPage( String clientId,
														String contractNumber,
														String mapNumber,
														String modifyTime) throws Exception {

		String page=getPara("page");
		String rows=getPara("rows");
		SendInputFormMap sendInputFormMap = getFormMap(SendInputFormMap.class);
		if(clientId!=null&&clientId.equals("??????")){
			clientId="";
		}
		sendInputFormMap.set("clientId",clientId);
		sendInputFormMap.set("contractNumber",contractNumber);
		sendInputFormMap.set("mapNumber",mapNumber);
		sendInputFormMap.set("modifyTime",modifyTime);
		sendInputFormMap.set("origin","??????");
		if(page.equals("1")){
			total=sendInputMapper.findCountByLike(sendInputFormMap);
		}
		sendInputFormMap=toFormMap(sendInputFormMap, page, rows,sendInputFormMap.getStr("orderby"));
		List<SendInputFormMap> sendInputFormMaps=sendInputMapper.findByLike(sendInputFormMap);
		sendInputFormMap.set("rows",sendInputFormMaps);
		sendInputFormMap.set("total",total);
		return sendInputFormMap;
	}

	@ResponseBody
	@RequestMapping("findExpectOrderByPage")
	@SystemLog(module="???????????????",methods="???????????????-??????????????????")
	public OrderDetailsFormMap findExpectOrderByPage(String clientId,
														String startTime,
														String endTime) throws Exception {
		String page=getPara("page");
		String rows=getPara("rows");
		String goodName=getPara("goodName");
		System.out.print(goodName);
		OrderDetailsFormMap orderDetailsFormMap = getFormMap(OrderDetailsFormMap.class);
		OrderDetailsFormMap orderDetailsFormMap1=new OrderDetailsFormMap();
		orderDetailsFormMap1.set("clientId",clientId);
		orderDetailsFormMap1.set("goodName",goodName);
		if(endTime==null){
			startTime=ToolCommon.getNowDay();
			endTime=ToolCommon.addDay(startTime,10);
		}
		endTime=ToolCommon.addDay(endTime,1);
		orderDetailsFormMap1.set("endTime",endTime);
		orderDetailsFormMap1.set("startTime",startTime);
		if(page.equals("1")){
			total=orderDetailsMapper.getOrderDetailsCountByClientId(orderDetailsFormMap1);
		}
		OrderDetailsFormMap sumAmount=orderDetailsMapper.getSumAmountOrderDetailsByClientId(orderDetailsFormMap1);
		orderDetailsFormMap1=toFormMap(orderDetailsFormMap1, page, rows,orderDetailsFormMap1.getStr("orderby"));
		List<OrderDetailsFormMap> orderDetailsFormMaps=orderDetailsMapper.getOrderDetailsByClientId(orderDetailsFormMap1);
		orderDetailsFormMap.set("sum",sumAmount);
		orderDetailsFormMap.set("rows",orderDetailsFormMaps);
		orderDetailsFormMap.set("total",total);
		return orderDetailsFormMap;
	}

	@ResponseBody
	@RequestMapping("findClientGoodByPage")
	@SystemLog(module="???????????????",methods="???????????????-??????????????????")
	public OrderDetailsFormMap findClientGoodByPage(String clientId,
													 String mapNumber,
													 String month) throws Exception {
		String page=getPara("page");
		String rows=getPara("rows");
		String goodName=getPara("goodName");
		String clientDefault=getPara("clientDefault");
		System.out.print(clientDefault);
		OrderDetailsFormMap orderDetailsFormMap = getFormMap(OrderDetailsFormMap.class);
		OrderDetailsFormMap orderDetailsFormMap1=new OrderDetailsFormMap();
		if(ToolCommon.isNull(clientId)){
			clientId=clientDefault;
		}
		orderDetailsFormMap1.set("goodName",goodName);
		orderDetailsFormMap1.set("clientId",clientId);
		orderDetailsFormMap1.set("mapNumber",mapNumber);
		if(ToolCommon.isNull(month)){
			month=ToolCommon.getNowMonth();
		}
		orderDetailsFormMap1.set("month",month);
		OrderDetailsFormMap orderDetailsFormMap2=orderDetailsMapper.getExpectClientGoodAmountSum(orderDetailsFormMap1);
		if(page.equals("1")){
			total=orderDetailsMapper.getExpectClientGoodCount(orderDetailsFormMap1);
//			total=0;
		}
		orderDetailsFormMap1=toFormMap(orderDetailsFormMap1, page, rows,orderDetailsFormMap1.getStr("orderby"));
		List<OrderDetailsFormMap> orderDetailsFormMaps=orderDetailsMapper.getExpectClientGood(orderDetailsFormMap1);
//		List<OrderDetailsFormMap> orderDetailsFormMaps=new ArrayList<>();
		orderDetailsFormMap.set("rows",orderDetailsFormMaps);
		orderDetailsFormMap.set("total",total);
		orderDetailsFormMap.set("sumEntity",orderDetailsFormMap2);
		return orderDetailsFormMap;
	}

	@ResponseBody
	@RequestMapping("findProductStatisticsByPage")
	@SystemLog(module="????????????",methods="????????????-??????????????????")
	public List<WorkersubmitFormMap> findProductStatisticsByPage(String clientId,
													String month) throws Exception {
		String clientDefault=getPara("clientDefault");
		if(ToolCommon.isNull(clientId)){
			clientId=clientDefault;
		}
		if(ToolCommon.isNull(month)){
			month=ToolCommon.getNowMonth();
		}
		List<String> processList=new ArrayList<>();
		String[] namesXL = {"??????"};
		String processIdsXL = processMapper.findIdsByNames(namesXL);//??????
		processList.add(processIdsXL);

		String[] namesReCl = {"?????????","?????????"};
		String processIdsReCl = processMapper.findIdsByNames(namesReCl);//?????????
		processList.add(processIdsReCl);


		String[] namesC = {"???","??????","??????","??????"};
		String processIdsC = processMapper.findIdsByNames(namesC);//???
		processList.add(processIdsC);

		String[] namesX = {"???","???????????????","???????????????"};
		String processIdsX = processMapper.findIdsByNames(namesX);//???
		processList.add(processIdsX);

		String[] namesQ = {"???"};
		String processIdsQ = processMapper.findIdsByNames(namesQ);//???
		processList.add(processIdsQ);

		String[] namesM = {"?????????","?????????","?????????","?????????","??????","??????","??????","??????"};
		String processIdsM = processMapper.findIdsByNames(namesM);//???
		processList.add(processIdsM);

		String[] namesXQG = {"?????????","??????","??????"};
		String processIdsXQG= processMapper.findIdsByNames(namesXQG);//?????????
		processList.add(processIdsXQG);

		String[] namesYC= {"??????"};
		String processIdsYC= processMapper.findIdsByNames(namesYC);//??????
		processList.add(processIdsYC);

		List<WorkersubmitFormMap> workersubmitFormMaps=new ArrayList<>();
		for(int i=0;i<processList.size();i++){
			String processIds=processList.get(i);
			String process="";
			switch (i){
				case 0:process="??????";break;
				case 1:process="?????????";break;
				case 2:process="???";break;
				case 3:process="???";break;
				case 4:process="???";break;
				case 5:process="???";break;
				case 6:process="?????????";break;
				case 7:process="??????";break;
			}
			WorkersubmitFormMap workersubmitFormMap=new WorkersubmitFormMap();
			workersubmitFormMap.set("process",process);
			for(int j=1;j<32;j++){
				String day="";
				if(j<10){
					day="0"+j;
				}else{
					day=j+"";
				}
				String  showAmount=workersubmitMapper.findProductAmountByClinetAndProcessIdsAndDeliveryTime(processIds,month+"-"+day,clientId);
				workersubmitFormMap.set("amount"+j,showAmount);
			}
			String  showAmount=workersubmitMapper.findProductAmountByClinetAndProcessIdsAndDeliveryTime(processIds,month,clientId);
			workersubmitFormMap.set("amountSum",showAmount);
			workersubmitFormMaps.add(workersubmitFormMap);
		}
		System.out.print(workersubmitFormMaps);
		return workersubmitFormMaps;
	}

	@ResponseBody
	@RequestMapping("findClientGoodDetailsByPage")
	@SystemLog(module="???????????????",methods="???????????????-??????")
	public OrderDetailsFormMap findClientGoodDetailsByPage(String clientId,
													String goodId,
													String month) throws Exception {
		OrderDetailsFormMap orderDetailsFormMap = getFormMap(OrderDetailsFormMap.class);
		OrderDetailsFormMap orderDetailsFormMap1=new OrderDetailsFormMap();
		orderDetailsFormMap1.set("clientId",clientId);
		orderDetailsFormMap1.set("goodId",goodId);
		if(ToolCommon.isNull(month)){
			month=ToolCommon.getNowMonth();
		}
		orderDetailsFormMap1.set("month",month);
		List<OrderDetailsFormMap> orderDetailsFormMaps=orderDetailsMapper.getExpectClientGoodDetails(orderDetailsFormMap1);
		orderDetailsFormMap.set("rows",orderDetailsFormMaps);
		return orderDetailsFormMap;
	}



	@RequestMapping("sendStatistics")
	@SystemLog(module="????????????",methods="????????????-????????????")
	public String sendStatistics(Model model) throws Exception {
		model.addAttribute("res", findByRes());
		model.addAttribute("starttime",ToolCommon.getNowMonth()+"-01");
		model.addAttribute("endtime",ToolCommon.getNowDay());
		return Common.BACKGROUND_PATH + "/system/order/sendStatistics";
	}

	@ResponseBody
	@RequestMapping("findSendStatisticsByPage")
	@SystemLog(module="????????????",methods="????????????-??????????????????")
	public SendInputFormMap findSendStatisticsByPage(String clientId,
														String contractNumber,
														String mapNumber,
														String goodName,
														String starttime,
														String endtime) throws Exception {

		String page=getPara("page");
		String rows=getPara("rows");
		SendInputFormMap sendInputFormMap = getFormMap(SendInputFormMap.class);
		if(clientId!=null&&clientId.equals("??????")){
			clientId="";
		}
				sendInputFormMap.set("clientId",clientId);
		sendInputFormMap.set("contractNumber",contractNumber);
		sendInputFormMap.set("goodName",goodName);
		sendInputFormMap.set("mapNumber",mapNumber);
		sendInputFormMap.set("startTime",starttime);
		if(!ToolCommon.isNull(endtime)){
			endtime=ToolCommon.addDay(endtime,1);
		}
		sendInputFormMap.set("endTime",endtime);
		sendInputFormMap.set("origin","??????");
		if(page.equals("1")){
			total=sendInputMapper.findCountByLike(sendInputFormMap);
		}
		sendInputFormMap=toFormMap(sendInputFormMap, page, rows,sendInputFormMap.getStr("orderby"));
		List<SendInputFormMap> sendInputFormMaps=sendInputMapper.findByLike(sendInputFormMap);
		sendInputFormMap.set("rows",sendInputFormMaps);
		sendInputFormMap.set("total",total);
		return sendInputFormMap;
	}

	@ResponseBody
	@RequestMapping("workersubmitShow")
	public List<WorkersubmitFormMap>  workersubmitShow(String orderdetailsprocessId) throws Exception {
		WorkersubmitFormMap roleProcessFormMap=new WorkersubmitFormMap();
		roleProcessFormMap.set("orderdetailsprocessId",orderdetailsprocessId);
		List<WorkersubmitFormMap> roleProcessFormMaps=workersubmitMapper.findByOrderdetailsprocessId(roleProcessFormMap);
		return roleProcessFormMaps;
	}

	@ResponseBody
	@RequestMapping("employeeShow")
	public OrderDetailsProcessFormMap  employeeShow(String starttime,String endtime,String name) throws Exception {
		OrderDetailsProcessFormMap orderDetailsProcessFormMap = getFormMap(OrderDetailsProcessFormMap.class);
		OrderDetailsProcessFormMap orderDetailsProcessFormMap1=new OrderDetailsProcessFormMap();
		String page=getPara("page");

		String rows=getPara("rows");

		if(starttime==null){
			starttime="";
		}
		if(endtime==null){
			endtime="";
		}else{
			endtime=ToolCommon.addDay(endtime,1);
		}
		orderDetailsProcessFormMap1.set("name",name);
		orderDetailsProcessFormMap1.set("starttime",starttime);
		orderDetailsProcessFormMap1.set("endtime",endtime);
		if(page.equals("1")){
			total=orderDetailsProcessMapper.findCountByCompleteUserNameAndStartTimeAndEndTime(orderDetailsProcessFormMap1);
		}
		orderDetailsProcessFormMap1=toFormMap(orderDetailsProcessFormMap1, page, rows,orderDetailsProcessFormMap1.getStr("orderby"));
		List<OrderDetailsProcessFormMap> orderDetailsProcessFormMaps=orderDetailsProcessMapper.findAllUseTimeAndAllAmountByCompleteUserNameAndStartTimeAndEndTime(orderDetailsProcessFormMap1);
		orderDetailsProcessFormMap.set("rows",orderDetailsProcessFormMaps);
		orderDetailsProcessFormMap.set("total",total);
		return orderDetailsProcessFormMap;
	}


	@RequestMapping("workersubmitShowUI")
	public String processRoleShowUI(Model model,String orderdetailsprocessId) throws Exception {
		model.addAttribute("orderdetailsprocessId",orderdetailsprocessId);
		return Common.BACKGROUND_PATH + "/system/order/workersubmitShow";
	}

	@RequestMapping("employeeShowUI")
	public String employeeShowUI(Model model) throws Exception {
		return Common.BACKGROUND_PATH + "/system/order/employeelist";
	}
	@RequestMapping("employeeExport")
	public String employeeExport(Model model) throws Exception {
		model.addAttribute("res", findByRes());
		return Common.BACKGROUND_PATH + "/system/order/employeeexport";
	}

    @RequestMapping("orderExport")
    public String orderExport(Model model) throws Exception {
        model.addAttribute("res", findByRes());
        return Common.BACKGROUND_PATH + "/system/order/orderexport";
    }

	@RequestMapping("productionUI")
	public String productionUI(Model model) throws Exception {
		model.addAttribute("res", findByRes());
		return Common.BACKGROUND_PATH + "/system/order/productionlist";
	}

	@RequestMapping("worktimeUI")
	public String worktimeUI(Model model) throws Exception {
		model.addAttribute("res", findByRes());
		return Common.BACKGROUND_PATH + "/system/order/worktimelist";
	}
	@RequestMapping("orderSituation")
	@SystemLog(module="????????????",methods="????????????-????????????")
	public String orderSituation(Model model) throws Exception {
		model.addAttribute("res", findByRes());
		String startTime=ToolCommon.getNowDay();
		String endTime=ToolCommon.addDay(startTime,10);
		model.addAttribute("startTime",startTime);
		model.addAttribute("endTime",endTime);

		String startTimeShow=ToolCommon.getNowDay();
		String endTimeShow=ToolCommon.addDay(startTimeShow,7);
		model.addAttribute("startTimeShow",startTimeShow);
		model.addAttribute("endTimeShow",endTimeShow);

		return Common.BACKGROUND_PATH + "/system/order/orderSituation";
	}

	@RequestMapping("sendlist")
	@SystemLog(module="???????????????",methods="????????????-????????????")
	public String sendlist(Model model) throws Exception {
		model.addAttribute("res", findByRes());
		String startTime=ToolCommon.getNowDay();
		String endTime=ToolCommon.addDay(startTime,7);
		model.addAttribute("startTime",startTime);
		model.addAttribute("endTime",endTime);
		return Common.BACKGROUND_PATH + "/system/order/sendlist";
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
	}

	@ResponseBody
	@RequestMapping("findsendlist")
	public List findsendlist(String content) throws Exception {
		List<ClientFormMap> clientFormMaps=clientMapper.findUnSendOrderClientGroupByClientId();
		List<TreeClientEntity> treeClientEntities=new ArrayList<>();
		for(int i=0;i<clientFormMaps.size();i++){
			ClientFormMap clientFormMap=clientFormMaps.get(i);
			String clientId=clientFormMap.get("id")+"";
			String fullName=clientFormMap.getStr("fullName");
			TreeClientEntity treeClientEntity=new TreeClientEntity();
			treeClientEntity.id=clientId;
			treeClientEntity.text=fullName;
			treeClientEntities.add(treeClientEntity);
		}
		return treeClientEntities;
	}

	@ResponseBody
	@RequestMapping("findorderSituation")
	public List findorderSituation(String content) throws Exception {
		List<ClientFormMap> clientFormMaps=clientMapper.findUnSendOrderClientGroupByClientId();
		List<TreeClientEntity> treeClientEntities=new ArrayList<>();
		for(int i=0;i<clientFormMaps.size();i++){
			ClientFormMap clientFormMap=clientFormMaps.get(i);
			String clientId=clientFormMap.get("id")+"";
			String fullName=clientFormMap.getStr("fullName");
			TreeClientEntity treeClientEntity=new TreeClientEntity();
			treeClientEntity.id=clientId;
			treeClientEntity.text=fullName;
			treeClientEntities.add(treeClientEntity);
		}
		return treeClientEntities;
	}

	@ResponseBody
	@RequestMapping("sureSendEntity")
	@Transactional(readOnly=false)//???????????????????????????????????????
	@SystemLog(module="???????????????",methods="????????????-??????")//??????????????????????????????.???????????????????????????
	public String sureSendEntity(String entity) throws Exception {
		UserFormMap userFormMap=getNowUserMessage();
		String userId=userFormMap.get("id")+"";
		String nowTime=ToolCommon.getNowTime();
		String orderdetails = ToolCommon.json2Object(entity).getString("rows");
		List<OrderDetailsFormMap> list = (List) ToolCommon.json2ObjectList(orderdetails, OrderDetailsFormMap.class);
		for (int i = 0; i < list.size(); i++) {
			OrderDetailsFormMap orderDetailsFormMap = list.get(i);
			sendGood(orderDetailsFormMap);
		}
		return "success";
	}

	public void sendGood(OrderDetailsFormMap orderDetailsFormMap){
		String userId=getNowUserMessage().get("id")+"";
		String nowTime=ToolCommon.getNowTime();
		try {
			String goodId = orderDetailsFormMap.get("goodId") + "";
			String taxPrice=goodMapper.findTaxPriceById(goodId);
			float taxPriceFloat=ToolCommon.StringToFloat(taxPrice);
			String sendAmount = orderDetailsFormMap.get("sendAmount") + "";
			float sendAmountF=ToolCommon.StringToFloat(sendAmount);
			StockFormMap stockFormMap=stockMapper.findByGoodId(goodId);
			if(stockFormMap==null){
				stockFormMap=new StockFormMap();
				stockFormMap.set("goodId",goodId);
				float money=taxPriceFloat*sendAmountF;
				money=ToolCommon.FloatToMoney(money);
				stockFormMap.set("amount",-sendAmountF);
				stockFormMap.set("money",-money);
				stockFormMap.set("price",taxPriceFloat);
				stockFormMap.set("userId",userId);
				stockFormMap.set("modifyTime",ToolCommon.getNowTime());
				stockMapper.addEntity(stockFormMap);
			}else{
				String stockAmountLast=stockFormMap.get("amount")+"";
				float stockAmountLastF=ToolCommon.StringToFloat(stockAmountLast);
				float money=taxPriceFloat*(stockAmountLastF-sendAmountF);
				money=ToolCommon.FloatToMoney(money);
				stockFormMap.set("money",money);
				stockFormMap.set("price",taxPriceFloat);
				stockFormMap.set("amount",stockAmountLastF-sendAmountF);
				stockMapper.editEntity(stockFormMap);
			}
			String unsendAmount = orderDetailsFormMap.get("unsendAmount") + "";
			float unsendAmountF=ToolCommon.StringToFloat(unsendAmount);

			String alreadysendAmount = orderDetailsFormMap.get("alreadysendAmount") + "";
			float alreadysendAmountF=ToolCommon.StringToFloat(alreadysendAmount);
			String alreadysendAmount1=(alreadysendAmountF+sendAmountF)+"";
			String unsendAmount1=(unsendAmountF-sendAmountF)+"";
			orderDetailsFormMap.set("unsendAmount",unsendAmount1);
			orderDetailsFormMap.set("alreadysendAmount",alreadysendAmount1);
			if(ToolCommon.StringToFloat(unsendAmount1)<=0){
				orderDetailsFormMap.set("state","???????????????");
			}else{
				orderDetailsFormMap.set("state","?????????"+alreadysendAmount1);
			}
			orderDetailsFormMap.set("sendTime",ToolCommon.getNowTime());
			orderDetailsFormMap.set("sendState","");
			orderDetailsFormMap.set("boxId","");
			orderDetailsFormMap.set("sendAmount","");
			orderDetailsFormMap.set("sendRemarks","");
			orderDetailsMapper.editEntity(orderDetailsFormMap);
			String orderId=orderDetailsFormMap.get("orderId")+"";
			int stateCount=orderDetailsMapper.findStateCountByOrderId(orderId);
			if(stateCount==1){
				OrderFormMap orderFormMap=orderMapper.findById(orderId);
				orderFormMap.set("state","???????????????");
				orderMapper.editEntity(orderFormMap);
			}
			SendInputFormMap sendInputFormMap=new SendInputFormMap();
			sendInputFormMap.set("orderdetailsId",orderDetailsFormMap.get("id")+"");
			sendInputFormMap.set("amount",sendAmount);
			sendInputFormMap.set("modifyuserId",userId);
			sendInputFormMap.set("modifyTime",nowTime);
			sendInputFormMap.set("origin","??????");
			sendInputMapper.addEntity(sendInputFormMap);
		}catch (Exception e){

		}
	}

	@ResponseBody
	@RequestMapping("uploadSendStock")
	@SystemLog(module="????????????",methods="????????????-????????????")
	public String uploadSendStock(@RequestParam(value = "file", required = false) MultipartFile file,
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
									}else if(value.equals("??????")){
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
										int count=orderDetailsMapper.getCountByContractNumberAndMapNumber(contractNumber,mapNumber);
										if(count!=1){
											errorMessage=errorMessage+"?????????:"+contractNumber+"??????:"+mapNumber+"?????????????????????<br>";
											isAdd=false;
											j=lastColNum;
										}else {
											isAdd=true;
											orderDetailsFormMap=orderDetailsMapper.getAllByContractNumberAndMapNumber(contractNumber,mapNumber);
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
						if(isAdd){//?????????
							orderDetailsFormMap.set("taxPrice",price);
							orderDetailsFormMap.set("sendAmount",amount);
							sendGood(orderDetailsFormMap);
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
	@RequestMapping("saveSendEntity")
	@Transactional(readOnly=false)//???????????????????????????????????????
	@SystemLog(module="???????????????",methods="????????????-??????")//??????????????????????????????.???????????????????????????
	public String saveSendEntity(String entity) throws Exception {
		UserFormMap userFormMap=getNowUserMessage();
		String userId=userFormMap.get("id")+"";
		String orderdetails = ToolCommon.json2Object(entity).getString("rows");
		List<OrderDetailsFormMap> list = (List) ToolCommon.json2ObjectList(orderdetails, OrderDetailsFormMap.class);
		for (int i = 0; i < list.size(); i++) {
			OrderDetailsFormMap orderDetailsFormMap = list.get(i);
			String boxId = orderDetailsFormMap.get("boxId") + "";
			String sendAmount = orderDetailsFormMap.get("sendAmount") + "";
			String sendRemarks = orderDetailsFormMap.get("sendRemarks") + "";
			orderDetailsFormMap.set("sendState","?????????");
			orderDetailsFormMap.set("boxId",boxId);
			orderDetailsFormMap.set("sendAmount",sendAmount);
			orderDetailsFormMap.set("sendRemarks",sendRemarks);
			orderDetailsMapper.editEntity(orderDetailsFormMap);

		}
		return "success";
	}

	@RequestMapping("sendorderPrintUI")
	public String sendorderPrintUI(Model model,String ids) throws Exception {
		String fullName=getPara("fullName");
		String origin=getPara("origin");
		List<OrderDetailsFormMap> list=new ArrayList<>();
		ids=ids.substring(0,ids.length()-1);
		String[] idsStr=ids.split(",");
		int boxCount=0;
		String boxId="";
		for(int i=0;i<idsStr.length;i++){
			String id=idsStr[i];
			OrderDetailsFormMap entity = orderDetailsMapper.findById(id);
			String entityBoxId=entity.getStr("boxId");
			if(!boxId.equals(entityBoxId)){
				boxCount=boxCount+1;
				boxId=entityBoxId;
			}
			list.add(entity);
		}
		model.addAttribute("boxCount",boxCount);
		model.addAttribute("fullName",fullName);
		model.addAttribute("ids",ids);
		model.addAttribute("origin",origin);
		model.addAttribute("rows",list);
		model.addAttribute("companyName",getCompanyName());
		model.addAttribute("nowDay",ToolCommon.getNowDay());
		return Common.BACKGROUND_PATH + "/system/order/sendOrderPrintShow";
	}

	@ResponseBody
	@RequestMapping("findSendByClientId")
	public OrderDetailsFormMap findSendByClientId(String clientId,String mapNumber,String contractNumber) throws Exception {
		OrderDetailsFormMap orderDetailsFormMap = getFormMap(OrderDetailsFormMap.class);
		OrderDetailsFormMap orderDetailsFormMap1=new OrderDetailsFormMap();
		orderDetailsFormMap1.set("clientId",clientId);
		orderDetailsFormMap1.set("contractNumber",contractNumber);
		orderDetailsFormMap1.set("mapNumber",mapNumber);
		List<OrderDetailsFormMap> orderDetailsFormMaps=orderDetailsMapper.getUnsendOrderDetailsNoSitationByClientId(orderDetailsFormMap1);
		orderDetailsFormMap.set("rows",orderDetailsFormMaps);
		return orderDetailsFormMap;
	}

	@ResponseBody
	@RequestMapping("findUnSendByClientId")
	@SystemLog(module="????????????",methods="????????????-??????????????????")
	public OrderDetailsFormMap findUnSendByClientId(String clientId,String contractNumber,String mapNumber,String startTime,String endTime) throws Exception {
		String page=getPara("page");

		String rows=getPara("rows");
		OrderFormMap orderFormMap = getFormMap(OrderFormMap.class);
		OrderDetailsFormMap orderDetailsFormMap = getFormMap(OrderDetailsFormMap.class);
		OrderDetailsFormMap orderDetailsFormMap1=new OrderDetailsFormMap();
		orderDetailsFormMap1.set("clientId",clientId);
		orderDetailsFormMap1.set("contractNumber",contractNumber);
		orderDetailsFormMap1.set("mapNumber",mapNumber);
		if(page.equals("1")){
			total=orderDetailsMapper.getCountUnsendOrderDetailsByClientId(orderDetailsFormMap1);
		}
		orderDetailsFormMap1=toFormMap(orderDetailsFormMap1, page, rows,orderDetailsFormMap1.getStr("orderby"));
		List<OrderDetailsFormMap> orderDetailsFormMaps=orderDetailsMapper.getUnsendOrderDetailsByClientId(orderDetailsFormMap1);
		orderDetailsFormMap.set("total",total);
		orderDetailsFormMap.set("rows",orderDetailsFormMaps);
		return orderDetailsFormMap;
	}

	@ResponseBody
	@RequestMapping("findUnProductByClientId")
	public OrderDetailsFormMap findUnProductByClientId(String orderId,
													   String orderdetailsId,
													   String startTime,
													   String endTime,
													   String mapNumber,
													   String contractNumber) throws Exception {
		if(endTime!=null&&!endTime.equals("")){
			endTime=ToolCommon.addDay(endTime,1);
		}
		String page=getPara("page");

		String rows=getPara("rows");
//		rows="2";
		OrderDetailsFormMap orderDetailsFormMap = getFormMap(OrderDetailsFormMap.class);
		OrderDetailsFormMap orderDetailsFormMap1=new OrderDetailsFormMap();
		orderDetailsFormMap1.set("mapNumber",mapNumber);
		orderDetailsFormMap1.set("contractNumber",contractNumber);
		orderDetailsFormMap1.set("orderId",orderId);
		orderDetailsFormMap1.set("startTime",startTime);
		orderDetailsFormMap1.set("endTime",endTime);
		orderDetailsFormMap1.set("orderdetailsId",orderdetailsId);
		if(page.equals("1")){
			total=orderDetailsMapper.getCountUnproductOrderDetailsByClientId(orderDetailsFormMap1);
		}
		orderDetailsFormMap1=toFormMap(orderDetailsFormMap1, page, rows,orderDetailsFormMap1.getStr("orderby"));
		List<OrderDetailsFormMap> orderDetailsFormMaps=orderDetailsMapper.getUnproductOrderDetailsByClientId(orderDetailsFormMap1);
		orderDetailsFormMap.set("total",total);
		orderDetailsFormMap.set("rows",orderDetailsFormMaps);
		return orderDetailsFormMap;
	}

	@ResponseBody
	@RequestMapping("findOrderSituation")
	public BlankFormMap findOrderSituation(String orderdetailsId) throws Exception {
		String origin=getPara("origin");
		BlankFormMap blankFormMap = getFormMap(BlankFormMap.class);
		blankFormMap.set("orderdetailsId",orderdetailsId);
		blankFormMap.set("origin",origin);
		List<BlankFormMap> departmentFormMapList=blankMapper.findByByOrderdetailsId(blankFormMap);
		blankFormMap.set("rows",departmentFormMapList);
		return blankFormMap;
	}

	@ResponseBody
	@RequestMapping("completeSendEntity")
	@Transactional(readOnly=false)//???????????????????????????????????????
	@SystemLog(module="????????????",methods="?????????-?????????")//??????????????????????????????.???????????????????????????
	public String completeSendEntity() throws Exception {
		String orderId = getPara("orderId");
		orderId=orderId.replace("??????id","");
		OrderFormMap orderFormMap=orderMapper.findById(orderId);
		orderFormMap.set("state","?????????");
		orderMapper.editEntity(orderFormMap);
		return "success";
	}

	@ResponseBody
	@RequestMapping("findproductionByPage")
	public OrderFormMap findproductionByPage(String content) throws Exception {
		String page=getPara("page");

		String rows=getPara("rows");
		OrderFormMap orderFormMap = getFormMap(OrderFormMap.class);
		if(content==null){
			content="";
		}
		orderFormMap.set("content",content);
		if(page.equals("1")){
			total=orderMapper.findCountByAllLike(orderFormMap);
		}
		orderFormMap=toFormMap(orderFormMap, page, rows,orderFormMap.getStr("orderby"));
		List<OrderFormMap> orderFormMaps=orderMapper.findByAllLike(orderFormMap);
		orderFormMap.set("rows",orderFormMaps);
		orderFormMap.set("total",total);
		return orderFormMap;
	}

	@ResponseBody
	@RequestMapping("findByPage")
	public OrderFormMap findByPage(String content,String startTime,String endTime,String clientId) throws Exception {
		String page=getPara("page");
		String sort=getPara("sort");
		String order=getPara("order");
		if(sort==null){
			sort="makeTime";
			order="desc";
		}
		String rows=getPara("rows");
		OrderFormMap orderFormMap = getFormMap(OrderFormMap.class);
		if(content==null){
			content="";
		}
		if(endTime!=null&&!endTime.equals("")){
			endTime=ToolCommon.addDay(endTime,1);
		}
		if(clientId!=null&&clientId.equals("??????")){
			clientId="";
		}
		orderFormMap.set("content",content);
		orderFormMap.set("startTime",startTime);
		orderFormMap.set("endTime",endTime);
		orderFormMap.set("clientId",clientId);
		String money=orderMapper.findAllMoneyByContentLike(orderFormMap);
		if(page.equals("1")){
			total=orderMapper.findCountByAllLike(orderFormMap);
		}
		orderFormMap.set("sort",sort);
		orderFormMap.set("order",order);
		orderFormMap=toFormMap(orderFormMap, page, rows,orderFormMap.getStr("orderby"));
		List<OrderFormMap> orderFormMaps=orderMapper.findByAllLike(orderFormMap);
		orderFormMap.set("money",money);
		orderFormMap.set("rows",orderFormMaps);
		orderFormMap.set("total",total);
		return orderFormMap;
	}




	@ResponseBody
	@RequestMapping("addEntity")
	@Transactional(readOnly=false)//???????????????????????????????????????
	@SystemLog(module="????????????",methods="????????????-??????")//??????????????????????????????.???????????????????????????
	public String addEntity(String entity) throws Exception {
		List<OrderFormMap> list = (List) ToolCommon.json2ObjectList(entity, OrderFormMap.class);
		String nowtime=ToolCommon.getNowTime();
		UserFormMap userFormMap=getNowUserMessage();
		String userId=String.valueOf(userFormMap.getInt("id"));
		for(int i=0;i<list.size();i++){
			OrderFormMap orderFormMap=list.get(i);
			String id=orderFormMap.getStr("id");
			if(id!=null&&!id.equals("")){
				OrderFormMap orderFormMap1=orderMapper.findbyFrist("id",id,OrderFormMap.class);
				orderFormMap.set("modifyTime",nowtime);
				orderFormMap.set("userId",userId);
				if(orderFormMap1==null){
					orderMapper.addEntity(orderFormMap);
				}else{
					orderMapper.editEntity(orderFormMap);
				}
			}

		}
		return "success";
	}

	@ResponseBody
	@RequestMapping("clientSimpleNameSelectTwoContentTrack")
	public List<ComboboxEntity>  clientSimpleNameSelectTwoContentTrack(String makeTimestart,
																	   String makeTimeend,
																	   String deliveryTimestart,
																	   String deliveryTimeend,
																	   String goodName,
																	   String materialQuality,
																	   String blankIsFinish,
																	   String turnerIsFinish,
																	   String carburizationIsFinish,
																	   String feedIsFinish,
																	   String content,
																	   String isWarning) throws Exception {
		if(!ToolCommon.isNull(deliveryTimeend)){
			deliveryTimeend=ToolCommon.addDay(deliveryTimeend,1);
		}
		if(!ToolCommon.isNull(makeTimeend)){
			makeTimeend=ToolCommon.addDay(makeTimeend,1);
		}
		OrderDetailsFormMap orderDetailsFormMap =new OrderDetailsFormMap();
		orderDetailsFormMap.set("isWarning",isWarning);
		orderDetailsFormMap.set("makeTimestart",makeTimestart);
		orderDetailsFormMap.set("makeTimeend",makeTimeend);
		orderDetailsFormMap.set("deliveryTimestart",deliveryTimestart);
		orderDetailsFormMap.set("deliveryTimeend",deliveryTimeend);
		orderDetailsFormMap.set("goodName",goodName);
		orderDetailsFormMap.set("materialQuality",materialQuality);
		orderDetailsFormMap.set("blankIsFinish",blankIsFinish);
		orderDetailsFormMap.set("turnerIsFinish",turnerIsFinish);
		orderDetailsFormMap.set("carburizationIsFinish",carburizationIsFinish);
		orderDetailsFormMap.set("feedIsFinish",feedIsFinish);
		orderDetailsFormMap.set("content",content);
		List<ComboboxEntity> clientFormMaps=orderDetailsMapper.getClientSimpleNameTrack(orderDetailsFormMap);
		ComboboxEntity entity=new ComboboxEntity();
		entity.id="??????";
		entity.text="??????";
		clientFormMaps.add(entity);
		return clientFormMaps;
	}

	@RequestMapping(value = "exportTrackAll")
	@ResponseBody
	@SystemLog(module="???????????????",methods="????????????")
	public void exportAll(HttpServletResponse response, String entity) throws Exception {

		String makeTimestart=ToolCommon.json2Object(entity).getString("makeTimestart");
		String makeTimeend=ToolCommon.json2Object(entity).getString("makeTimeend");
		String deliveryTimestart=ToolCommon.json2Object(entity).getString("deliveryTimestart");
		String deliveryTimeend=ToolCommon.json2Object(entity).getString("deliveryTimeend");
		String goodName=ToolCommon.json2Object(entity).getString("goodName");
		String materialQuality=ToolCommon.json2Object(entity).getString("materialQuality");
		String blankIsFinish=ToolCommon.json2Object(entity).getString("blankIsFinish");
		String turnerIsFinish=ToolCommon.json2Object(entity).getString("turnerIsFinish");
		String carburizationIsFinish=ToolCommon.json2Object(entity).getString("carburizationIsFinish");
		String feedIsFinish=ToolCommon.json2Object(entity).getString("feedIsFinish");
		String contentWeb=ToolCommon.json2Object(entity).getString("content");
		String isWarning=ToolCommon.json2Object(entity).getString("isWarning");
		if(!ToolCommon.isNull(deliveryTimeend)){
			deliveryTimeend=ToolCommon.addDay(deliveryTimeend,1);
		}
		if(!ToolCommon.isNull(makeTimeend)){
			makeTimeend=ToolCommon.addDay(makeTimeend,1);
		}
		OrderDetailsFormMap orderDetailsFormMap =new OrderDetailsFormMap();
		orderDetailsFormMap.set("isWarning",isWarning);
		orderDetailsFormMap.set("makeTimestart",makeTimestart);
		orderDetailsFormMap.set("makeTimeend",makeTimeend);
		orderDetailsFormMap.set("deliveryTimestart",deliveryTimestart);
		orderDetailsFormMap.set("deliveryTimeend",deliveryTimeend);
		orderDetailsFormMap.set("goodName",goodName);
		orderDetailsFormMap.set("materialQuality",materialQuality);
		orderDetailsFormMap.set("blankIsFinish",blankIsFinish);
		orderDetailsFormMap.set("turnerIsFinish",turnerIsFinish);
		orderDetailsFormMap.set("carburizationIsFinish",carburizationIsFinish);
		orderDetailsFormMap.set("feedIsFinish",feedIsFinish);
		orderDetailsFormMap.set("content",contentWeb);
		List<OrderDetailsFormMap> orderDetailsFormMaps=orderDetailsMapper.getTrackContent(orderDetailsFormMap);
		//excel??????
		String[] title = {"??????",
				"????????????",
				"??????",
				"????????????",
				"????????????",
				"????????????",
				"??????",
				"????????????",
				"????????????",
				"??????",
				"??????????????????",
				"??????????????????",
				"??????????????????",
				"??????????????????",
				"??????????????????"};

		//excel?????????
		String fileName ="??????????????????" + System.currentTimeMillis() + ".xls";

		//sheet???
		String sheetName = "sheet1";

		String [][] content = new String[orderDetailsFormMaps.size()][title.length];

		for (int i = 0; i < orderDetailsFormMaps.size(); i++) {
			content[i] = new String[title.length];
			OrderDetailsFormMap obj = orderDetailsFormMaps.get(i);
			content[i][0] = obj.get("clientSimpleName")+"";
			content[i][1] = obj.get("contractNumber")+"";
			content[i][2] = obj.get("mapNumber")+"";
			content[i][3] = obj.get("name")+"";
			content[i][4] = obj.get("goodSize")+"";
			content[i][5] = obj.get("blankSize")+"";
			content[i][6] = obj.get("materialQualityName")+"";
			content[i][7] = obj.get("makeTime")+"";
			content[i][8] = obj.get("deliveryTime")+"";
			content[i][9] = obj.get("amount")+"";
			content[i][10] = obj.get("blankIsFinish")+"";
			content[i][11] = obj.get("turnerIsFinish")+"";
			content[i][12] = obj.get("turnerCompleteTime")+"";
			content[i][13] = obj.get("carburizationIsFinish")+"";
			content[i][14] = obj.get("feedIsFinish")+"";
		}

		//??????HSSFWorkbook
		HSSFWorkbook wb = ExcelUtils.getHSSFWorkbook("????????????",sheetName, title, content, null);

		//??????????????????
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
	@RequestMapping("deleteDetailsEntity")
	@Transactional(readOnly=false)//???????????????????????????????????????
	@SystemLog(module="????????????",methods="?????????-?????????")//??????????????????????????????.???????????????????????????
	public String deleteDetailsEntity() throws Exception {
		String id=getPara("id");
		BlankFormMap blankFormMap1 = getFormMap(BlankFormMap.class);
		blankFormMap1.set("orderdetailsId",id);
		List<BlankFormMap> blankFormMaps=blankMapper.findByByOrderdetailsId(blankFormMap1);
		for(int j=0;j<blankFormMaps.size();j++){
			String blankId=blankFormMaps.get(j).getStr("blankId");
			blankProcessMapper.deleteByAttribute("blankId",blankId,BlankProcessFormMap.class);
		}
		blankMapper.deleteByOrderDetailsId(id);

		orderDetailsMapper.deleteByAttribute("id",id,OrderDetailsFormMap.class);
		return "success";
	}
	@ResponseBody
	@RequestMapping("deleteTrackEntity")
	@Transactional(readOnly=false)//???????????????????????????????????????
	@SystemLog(module="???????????????",methods="??????")//??????????????????????????????.???????????????????????????
	public String deleteTrackEntity() throws Exception {
		SystemconfigFormMap systemconfigFormMap=new SystemconfigFormMap();
		systemconfigFormMap.set("name","deletepassword");
		String content=getPara("password");
		systemconfigFormMap.set("content",content);
		List<SystemconfigFormMap> systemconfigFormMaps=systemconfigMapper.findByNames(systemconfigFormMap);
		if(systemconfigFormMaps.size()>0){
			String ids = getPara("ids");
			ids=ids.substring(0,ids.length()-1);
			orderDetailsMapper.updateTrackIsShowByIdsAndState("???",ids);
			return "success";
		}else{
			return "????????????????????????";
		}
	}
	@ResponseBody
	@RequestMapping("deleteSendInputEntity")
	@Transactional(readOnly=false)//???????????????????????????????????????
	@SystemLog(module="????????????",methods="?????????-?????????")//??????????????????????????????.???????????????????????????
	public String deleteSendInputEntity() throws Exception {
		SystemconfigFormMap systemconfigFormMap=new SystemconfigFormMap();
		systemconfigFormMap.set("name","deletepassword");
		String content=getPara("password");
		systemconfigFormMap.set("content",content);
		List<SystemconfigFormMap> systemconfigFormMaps=systemconfigMapper.findByNames(systemconfigFormMap);
		if(systemconfigFormMaps.size()>0){
			String ids = getPara("ids");
			ids=ids.substring(0,ids.length()-1);
			String[] idsStr=ids.split(",");
			for(int i=0;i<idsStr.length;i++){
				String id=idsStr[i];
				sendInputMapper.deleteByAttribute("id",id,SendInputFormMap.class);
			}
			return "success";
		}else{
			return "????????????????????????";
		}

	}
	@ResponseBody
	@RequestMapping("deleteEntity")
	@Transactional(readOnly=false)//???????????????????????????????????????
	@SystemLog(module="????????????",methods="?????????-?????????")//??????????????????????????????.???????????????????????????
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
			for(int i=0;i<idsStr.length;i++){
				String id=idsStr[i];
				materialbuyorderdetailsBlankMapper.deleteByOrderId(id);
				orderGoodCodeMapper.deleteByAttribute("orderId",id,OrderGoodCodeFormMap.class);
				orderDetailsProcessMapper.deleteByOrderId(id);
				blankProcessMapper.deleteByOrderId(id);
				blankMapper.deleteByOrderId(id);
				orderDetailsMapper.deleteByOrderId(id);
				orderMapper.deleteByAttribute("id", id, OrderFormMap.class);
			}
			return "success";
		}else{
			return "????????????????????????";
		}

	}

	@ResponseBody
	@RequestMapping("hideOrderDetailsEntity")
	@Transactional(readOnly=false)//???????????????????????????????????????
	@SystemLog(module="???????????????",methods="????????????-?????????")//??????????????????????????????.???????????????????????????
	public String hideOrderDetailsEntity() throws Exception {
		SystemconfigFormMap systemconfigFormMap=new SystemconfigFormMap();
		systemconfigFormMap.set("name","deletepassword");
		String content=getPara("password");
		systemconfigFormMap.set("content",content);
		List<SystemconfigFormMap> systemconfigFormMaps=systemconfigMapper.findByNames(systemconfigFormMap);
		if(systemconfigFormMaps.size()>0){
			String ids = getPara("ids");
			ids=ids.substring(0,ids.length()-1);
			orderDetailsMapper.updateHideByIds(ids);
			return "success";
		}else{
			return "????????????????????????";
		}

	}

	@ResponseBody
	@RequestMapping("blankEntity")
	@Transactional(readOnly=false)//???????????????????????????????????????
	@SystemLog(module="????????????",methods="????????????-??????")//??????????????????????????????.???????????????????????????
	public String blankEntity(String id) throws Exception {



		SystemconfigFormMap systemconfigFormMapHours=systemconfigMapper.findByName("blankAmount");
		String blankAmount=systemconfigFormMapHours.get("content")+"";
		float blankAmountF=ToolCommon.StringToFloat(blankAmount);
//		float dailyWorkingHoursF=ToolCommon.StringToFloat(dailyWorkingHours);
		SystemconfigFormMap systemconfigFormMapWages=systemconfigMapper.findByName("dailyWages");
		String dailyWages=systemconfigFormMapWages.get("content")+"";
		float dailyWagesF=ToolCommon.StringToFloat(dailyWages);


		String ids = id;
		ids=ids.substring(0,ids.length()-1);
		String[] idsStr=ids.split(",");
		for(int ii=0;ii<idsStr.length;ii++){
			id=idsStr[ii];
			OrderDetailsFormMap orderDetailsFormMap=new OrderDetailsFormMap();
			orderDetailsFormMap.set("orderId",id);
			OrderFormMap orderFormMap=orderMapper.findbyFrist("id",id,OrderFormMap.class);
			orderFormMap.set("state","??????????????????");
			orderMapper.editEntity(orderFormMap);
			String nowtime=ToolCommon.getNowTime();
			UserFormMap userFormMap=getNowUserMessage();
			String userId=String.valueOf(userFormMap.getInt("id"));
			List<OrderDetailsFormMap> orderDetailsFormMaps=orderDetailsMapper.getOrderDetailsByOrderId(orderDetailsFormMap);
			for(int i=0;i<orderDetailsFormMaps.size();i++){
				OrderDetailsFormMap orderDetailsFormMap1=orderDetailsFormMaps.get(i);
				String materialQuality=orderDetailsFormMap1.get("materialQuality")+"";
				String orderdetailsId=orderDetailsFormMap1.get("id")+"";
				String goodId=orderDetailsFormMap1.get("goodId")+"";
				String orderAmount=orderDetailsFormMap1.get("amount")+"";
				String amount="";
				float amountF=0;
				BlankSizeFormMap blankSizeFormMap=blankSizeMapper.findByGoodId(goodId);
				BlankFormMap blankFormMap=new BlankFormMap();
				blankFormMap.set("userId",userId);
				blankFormMap.set("modifytime",nowtime);
				blankFormMap.set("goodId",goodId);
				blankFormMap.set("orderdetailsId",orderdetailsId);
				blankFormMap.set("origin","??????");
				blankFormMap.set("materialQuality",materialQuality);
				blankFormMap.remove("id");
				if(blankSizeFormMap!=null){
					String blankSize=blankSizeFormMap.get("blankSize")+"";
					blankFormMap.set("blankSize",blankSize);
					String outSize=blankSize.split("\\*")[0];
					outSize=outSize.replace("??","");
					String inside=blankSize.split("\\*")[1];
					String length="";
					if(ToolCommon.isContain(inside,"??")){
						inside=inside.replace("??","");
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
//					StockFormMap stockFormMap=stockMapper.findByGoodId(goodId);
//					String stockAmount="0";
//					if(stockFormMap!=null){
//						stockAmount=stockFormMap.get("amount")+"";
//					}
//					float stockAmountF=ToolCommon.StringToFloat(stockAmount);
					float orderAmountF=ToolCommon.StringToFloat(orderAmount);
//					if(outsideF>100){
//						amount=(orderAmountF+2-stockAmountF)+"";
//					}else{
//						amount=(orderAmountF+5-stockAmountF)+"";
//					}
					amount=(orderAmountF+blankAmountF)+"";
					if(ToolCommon.isContain(amount,".")){
						amount=ToolCommon.StringToInteger(amount)+"";
					}
					blankFormMap.set("amount",amount);
					double lengthOrderD=amountF*lengthF/1000;
					lengthOrderD = ToolCommon.Double4(lengthOrderD);
					blankFormMap.set("length",lengthOrderD+"");
					double weight=outsideF*outsideF*lengthOrderD*(0.00617/1000)-insideF*insideF*lengthOrderD*(0.00617/1000);
					weight = ToolCommon.Double4(weight);
					blankFormMap.set("weight",weight+"");
				}
				blankMapper.addEntity(blankFormMap);

				String blankId=blankFormMap.get("id")+"";

				List<GoodProcessFormMap> goodProcessFormMaps=goodProcessMapper.findByGoodId(goodId);
				for(int j=0;j<goodProcessFormMaps.size();j++){
					GoodProcessFormMap goodProcessFormMap=goodProcessFormMaps.get(j);
					String goodprocessId=goodProcessFormMap.get("id")+"";
					String processId=goodProcessFormMap.get("processId")+"";

					BlankProcessFormMap blankProcessFormMap=new BlankProcessFormMap();
					blankProcessFormMap.set("amount",amount);
					blankProcessFormMap.set("unreceiveAmount",amount);
					String artificial=goodProcessFormMap.get("artificial")+"";
					float artificialF=ToolCommon.StringToFloat(artificial);
					double planneedDay=artificialF*amountF/dailyWagesF;
					double neesDay=0;
					int planneedDayInt=(new Double(planneedDay)).intValue();
					if(planneedDay==planneedDayInt){
						neesDay=planneedDay;
					}else{
						neesDay=(planneedDay)+1;
					}
					int neesDayInt= (new Double(neesDay)).intValue();
					blankProcessFormMap.set("planneedDay",neesDayInt);
					blankProcessFormMap.set("goodprocessId",goodprocessId);

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

	@RequestMapping("detailsUI")
	public String detailsUI(Model model,String orderId) throws Exception {
		OrderFormMap orderFormMap=new OrderFormMap();
		if(orderId.equals("")){
			orderFormMap.set("orderId","");
			orderFormMap.set("id","0");
		}else{
			orderFormMap=orderMapper.findById(orderId);
		}

		model.addAttribute("order",orderFormMap);
		return Common.BACKGROUND_PATH + "/system/order/details";
	}

	@ResponseBody
	@RequestMapping("deleteOrderGoodCodeEntity")
	@Transactional(readOnly=false)//???????????????????????????????????????
	@SystemLog(module="????????????",methods="?????????-?????????")//??????????????????????????????.???????????????????????????
	public String deleteOrderGoodCodeEntity(String ordergoodcodeId) throws Exception {
		orderGoodCodeMapper.deleteByAttribute("id", ordergoodcodeId, OrderGoodCodeFormMap.class);
		return "success";
	}

	@ResponseBody
	@RequestMapping("findSituationByOrderDetailsId")
	@Transactional(readOnly=false)//???????????????????????????????????????
	@SystemLog(module="????????????",methods="?????????-?????????")//??????????????????????????????.???????????????????????????
	public String findSituationByOrderDetailsId(String orderdetailsId) throws Exception {
		BlankProcessFormMap blankProcessFormMap=blankProcessMapper.findMaxProcessByOrderDetailsId(orderdetailsId);
		if(blankProcessFormMap==null){
			String planneedDay=blankMapper.findSumPlanneedDayByOrderDetailsId(orderdetailsId);
			return "?????????,??????"+planneedDay+"???";
		}else{
			String number=blankProcessFormMap.get("number")+"";
			String maxNumber=blankProcessFormMap.get("maxNumber")+"";
			String state=blankProcessFormMap.get("state")+"";
			String name=blankProcessFormMap.get("name")+"";
			String blankId=blankProcessFormMap.get("blankId")+"";
			if(state.equals("?????????")){
				if(number.equals(maxNumber)){
					return "?????????";
				}else{
					number=StringUtil.addOne(number);
					BlankProcessFormMap blankProcessFormMap1=new BlankProcessFormMap();
					blankProcessFormMap1.set("number",number);
					blankProcessFormMap1.set("blankId",blankId);
					name=blankProcessMapper.findProcessNameByBlankIdAndNumber(blankProcessFormMap1);
					if(name==null){
						name="";
					}
							BlankProcessFormMap blankProcessFormMap11=new BlankProcessFormMap();
						blankProcessFormMap11.set("number",number);
						blankProcessFormMap11.set("blankId",blankId);
						String planneedDay=blankProcessMapper.findSumPlanneedDayByBlankIdAndNumber(blankProcessFormMap11);
						return "?????????"+name+"?????????"+planneedDay+"???";
				}

			}else{
				BlankProcessFormMap blankProcessFormMap1=new BlankProcessFormMap();
				blankProcessFormMap1.set("number",number);
				blankProcessFormMap1.set("blankId",blankId);
				String planneedDay=blankProcessMapper.findSumPlanneedDayByBlankIdAndNumber(blankProcessFormMap1);
				return "?????????"+name+"?????????"+planneedDay+"???";
			}

		}

	}

	@ResponseBody
	@RequestMapping("upload")
	@Transactional(readOnly=false)//???????????????????????????????????????
	@SystemLog(module="Excel????????????",methods="????????????-??????")//??????????????????????????????.???????????????????????????
	public String upload(@RequestParam(value = "file", required = false) MultipartFile file,
						 HttpServletRequest request) throws Exception {
		String errorMessage="";
		String  orderId="";
		String materialQuality="";
		SystemconfigFormMap systemconfigFormMapHours=systemconfigMapper.findByName("blankAmount");
		String blankAmount=systemconfigFormMapHours.get("content")+"";
		float blankAmountF=ToolCommon.StringToFloat(blankAmount);
		OrderDetailsFormMap orderDetailsFormMap=new OrderDetailsFormMap();
		if(file!=null){
			SystemconfigFormMap systemconfigFormMapWages=systemconfigMapper.findByName("dailyWages");
			String dailyWages=systemconfigFormMapWages.get("content")+"";
			float dailyWagesF=ToolCommon.StringToFloat(dailyWages);
			boolean isAdd=false;
			String nowtime=ToolCommon.getNowTime();
			UserFormMap userFormMap=getNowUserMessage();
			String userId=String.valueOf(userFormMap.getInt("id"));
			FileBean fileBean=ToolCommon.uploadExcelFile(file,request);
			Sheet sheet1= ExcelUtil.readRowsAndColumsSheet1(fileBean.getPath());
			String deliveryTime="";
			for(int i=2;i<=sheet1.getLastRowNum();i++){
				if(i==3){
					System.out.print("aaa");
				}
				Row row = null;
				row = sheet1.getRow(i);
				int lastColNum=0;
				if(row!=null){
					lastColNum = row.getLastCellNum();
					OrderFormMap orderFormMap=new OrderFormMap();
					for(int j=0;j<lastColNum;j++){
						Cell cell=row.getCell(j);
						if(cell!=null){
							if(j==2){
								System.out.print("aaa");
							}
							String value=ExcelUtil.getCellValue(cell);
							if(value==null){
								value="";
							}
							if(j==0){
								if(value.equals("")){
									j=lastColNum+1;
									i=sheet1.getLastRowNum()+1;
								}else if(value.equals("??????")){
									i=sheet1.getLastRowNum()+1;
									j=lastColNum+1;
								}
							}
							value= ToolExcel.replaceBlank(value);
							switch (j){
								case 0:
									orderFormMap.set("makeTime",value);
									break;
								case 1:
									value=value.replace(" ","");
									List<ClientFormMap> clientFormMaps=clientMapper.findByFullName(value);
									if(clientFormMaps.size()==0){
										isAdd=false;
										j=lastColNum;
										errorMessage=errorMessage+"?????????"+value+"????????????;<br>";
									}else{
										if(clientFormMaps.size()>1){
											isAdd=false;
											j=lastColNum;
											errorMessage=errorMessage+"?????????"+value+"?????????;<br>";
										}else{
											String clientId=clientFormMaps.get(0).get("id")+"";
											orderFormMap.set("clientId",clientId);break;
										}
									}
									break;
								case 2:
									value=value.replace(",","");
									List<OrderFormMap> orderFormMapList=orderMapper.findByContractNumber(value);
									System.out.print(value);
									if(orderFormMapList.size()==0){
										orderFormMap.set("modifytime",nowtime);
										orderFormMap.set("userId",userId);
										orderFormMap.set("contractNumber",value);
										orderFormMap.set("state","??????????????????");
										orderMapper.addEntity(orderFormMap);
										orderId=orderFormMap.get("id")+"";
									}else{
										orderId=orderFormMapList.get(0).get("id")+"";
									}
									break;
								case 3:
									value=value.replace(" ","");
									value=value.replace(",","");
									orderDetailsFormMap.remove("id");
									orderDetailsFormMap.set("orderId",orderId);
									List<GoodFormMap> goodFormMaps=goodMapper.findByMapNumber(value);
									if(goodFormMaps.size()==0){
										isAdd=false;
										j=lastColNum;
										errorMessage=errorMessage+"???????????????"+value+"????????????";
									}else{
										if(goodFormMaps.size()>1){
											isAdd=false;
											j=lastColNum;
											errorMessage=errorMessage+"???????????????"+value+"?????????";
										}else{
											String goodId=goodFormMaps.get(0).get("id")+"";
											String taxPrice=goodFormMaps.get(0).get("taxPrice")+"";
											String nottaxPrice=goodFormMaps.get(0).get("nottaxPrice")+"";
											materialQuality=goodFormMaps.get(0).get("materialQuality")+"";
											orderDetailsFormMap.set("goodId",goodId);
											orderDetailsFormMap.set("ordertaxPrice",taxPrice);
											orderDetailsFormMap.set("ordernottaxPrice",nottaxPrice);
											break;
										}
									}
									break;
								case 4:
									orderDetailsFormMap.set("amount",value);
									orderDetailsFormMap.set("unsendAmount",value);
								break;
								case 5:
									deliveryTime=value;
									deliveryTime=deliveryTime.replace("//","-");
									deliveryTime=deliveryTime.replace("/","-");
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
									String f = sdf.format(sdf.parse(deliveryTime));
									orderDetailsFormMap.set("deliveryTime",f);
									break;
								case 6:
									orderDetailsFormMap.set("modifytime",nowtime);
									orderDetailsFormMap.set("userId",userId);
									orderDetailsFormMap.set("remakrs",value);
									String orderId1=orderDetailsFormMap.get("orderId")+"";
									if(orderId1!=null&&!orderId1.equals("")){
										String amount=orderDetailsFormMap.getStr("amount")+"";
										float amountF=0;
										String ordertaxPrice=orderDetailsFormMap.getStr("ordertaxPrice")+"";
										float money=ToolCommon.StringToFloat(amount)*ToolCommon.StringToFloat(ordertaxPrice);
										money=ToolCommon.FloatToMoney(money);
										orderDetailsFormMap.set("money",money);
										orderDetailsMapper.addEntity(orderDetailsFormMap);
										OrderDetailsFormMap orderDetailsFormMap1=orderDetailsFormMap;
										String orderdetailsId=orderDetailsFormMap1.get("id")+"";
										String goodId=orderDetailsFormMap1.get("goodId")+"";
										String orderAmount=orderDetailsFormMap1.get("amount")+"";
										if(ToolCommon.isContain(amount,".")){
											amount=ToolCommon.StringToInteger(amount)+"";
										}
										BlankSizeFormMap blankSizeFormMap=blankSizeMapper.findByGoodId(goodId);
										BlankFormMap blankFormMap=new BlankFormMap();
										blankFormMap.set("userId",userId);
										blankFormMap.set("modifytime",nowtime);
										blankFormMap.set("goodId",goodId);
										blankFormMap.set("orderdetailsId",orderdetailsId);
										blankFormMap.set("origin","??????");
										blankFormMap.set("materialQuality",materialQuality);
										blankFormMap.remove("id");
										String blankSize="";
										if(blankSizeFormMap!=null){
											blankSize=blankSizeFormMap.get("blankSize")+"";
											blankFormMap.set("blankSize",blankSize);
											String outSize="";
											String length="";
											String inside="";
											try {
												outSize=blankSize.split("\\*")[0];
												outSize=outSize.replace("??","");
												inside=blankSize.split("\\*")[1];
												if(ToolCommon.isContain(inside,"??")){
													inside=inside.replace("??","");
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
											float orderAmountF=ToolCommon.StringToFloat(orderAmount);
//											StockFormMap stockFormMap=stockMapper.findByGoodId(goodId);
//											String stockAmount="0";
//											if(stockFormMap!=null){
//												stockAmount=stockFormMap.get("amount")+"";
//											}
//											float stockAmountF=ToolCommon.StringToFloat(stockAmount);
//											if(outsideF>100){
//												amount=(orderAmountF+2-stockAmountF)+"";
//											}else{
//												amount=(orderAmountF+5-stockAmountF)+"";
//											}
											amount=(orderAmountF+blankAmountF)+"";
											if(ToolCommon.isContain(amount,".")){
												amount=ToolCommon.StringToInteger(amount)+"";
											}
											blankFormMap.set("amount",amount);
											amountF=ToolCommon.StringToFloat(amount);
											double lengthOrderD=amountF*lengthF/1000;
											lengthOrderD = ToolCommon.Double4(lengthOrderD);
											blankFormMap.set("length",lengthOrderD+"");
											double weight=outsideF*outsideF*lengthOrderD*(0.00617/1000)-insideF*insideF*lengthOrderD*(0.00617/1000);
											weight = ToolCommon.Double4(weight);
											blankFormMap.set("weight",weight+"");
										}
										blankMapper.addEntity(blankFormMap);

										String blankId=blankFormMap.get("id")+"";

										List<GoodProcessFormMap> goodProcessFormMaps=goodProcessMapper.findByGoodId(goodId);
										for(int jj=0;jj<goodProcessFormMaps.size();jj++) {
											GoodProcessFormMap goodProcessFormMap = goodProcessFormMaps.get(jj);
											String goodprocessId = goodProcessFormMap.get("id") + "";
											String processId = goodProcessFormMap.get("processId") + "";

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
								break;
							}
						}else{
								i=sheet1.getLastRowNum()+1;
								j=lastColNum+1;
						}

					}
				}else{
					i=sheet1.getLastRowNum()+1;
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
	@RequestMapping("upload2")
	@Transactional(readOnly=false)//???????????????????????????????????????
	@SystemLog(module="Excel????????????",methods="????????????-??????")//??????????????????????????????.???????????????????????????
	public String upload2(@RequestParam(value = "file2", required = false) MultipartFile file,
						 HttpServletRequest request) throws Exception {
		String errorMessage="";
		String  orderId="";
		String materialQuality="";
		SystemconfigFormMap systemconfigFormMapHours=systemconfigMapper.findByName("blankAmount");
		String blankAmount=systemconfigFormMapHours.get("content")+"";
		float blankAmountF=ToolCommon.StringToFloat(blankAmount);
		OrderDetailsFormMap orderDetailsFormMap=new OrderDetailsFormMap();
		if(file!=null){
			SystemconfigFormMap systemconfigFormMapWages=systemconfigMapper.findByName("dailyWages");
			String dailyWages=systemconfigFormMapWages.get("content")+"";
			float dailyWagesF=ToolCommon.StringToFloat(dailyWages);
			boolean isAdd=false;
			String nowtime=ToolCommon.getNowTime();
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
					OrderFormMap orderFormMap=new OrderFormMap();
					for(int j=0;j<lastColNum;j++){
						Cell cell=row.getCell(j);
						if(cell!=null){
							String value=ExcelUtil.getCellValue(cell);
							if(value==null){
								value="";
							}
							if(j==0||j==1){
								value=value.replace(" ","");
								if(value.equals("")){
									j=lastColNum+1;
									i=sheet1.getLastRowNum()+1;
								}else if(value.equals("??????")){
									i=sheet1.getLastRowNum()+1;
									j=lastColNum+1;
								}
							}
							value= ToolExcel.replaceBlank(value);
							switch (j){
								case 0:
									orderFormMap.set("makeTime",value);
									break;
								case 1:
									value=value.replace(" ","");
									List<ClientFormMap> clientFormMaps=clientMapper.findByFullName(value);
									if(clientFormMaps.size()==0){
										isAdd=false;
										j=lastColNum;
										errorMessage=errorMessage+"?????????"+value+"????????????;<br>";
									}else{
										if(clientFormMaps.size()>1){
											isAdd=false;
											j=lastColNum;
											errorMessage=errorMessage+"?????????"+value+"?????????;<br>";
										}else{
											String clientId=clientFormMaps.get(0).get("id")+"";
											orderFormMap.set("clientId",clientId);break;
										}
									}
									break;
								case 2:
									value=value.replace(" ","");
									value=value.replace(",","");
									List<OrderFormMap> orderFormMapList=orderMapper.findByContractNumber(value);
									System.out.print(value);
									if(orderFormMapList.size()==0){
										orderFormMap.set("modifytime",nowtime);
										orderFormMap.set("userId",userId);
										orderFormMap.set("contractNumber",value);
										orderFormMap.set("state","??????????????????");
										orderMapper.addEntity(orderFormMap);
										orderId=orderFormMap.get("id")+"";
									}else{
										orderId=orderFormMapList.get(0).get("id")+"";
									}
									break;
								case 3:
									String content=value;
									String[] contentStr=content.split(",");
									String mapNumber=contentStr[contentStr.length-1];
									orderDetailsFormMap.remove("id");
									orderDetailsFormMap.set("orderId",orderId);
									mapNumber=mapNumber.replace(" ","");
									mapNumber=mapNumber.replace(",","");
									List<GoodFormMap> goodFormMaps=goodMapper.findByMapNumber(mapNumber);
									if(goodFormMaps.size()==0){
										isAdd=false;
										j=lastColNum;
										errorMessage=errorMessage+"?????????"+value+"????????????";
									}else{
										if(goodFormMaps.size()>1){
											isAdd=false;
											j=lastColNum;
											errorMessage=errorMessage+"???????????????"+value+"?????????";
										}else{
											String goodId=goodFormMaps.get(0).get("id")+"";
											String taxPrice=goodFormMaps.get(0).get("taxPrice")+"";
											String nottaxPrice=goodFormMaps.get(0).get("nottaxPrice")+"";
											materialQuality=goodFormMaps.get(0).get("materialQuality")+"";
											orderDetailsFormMap.set("goodId",goodId);
											orderDetailsFormMap.set("ordertaxPrice",taxPrice);
											orderDetailsFormMap.set("ordernottaxPrice",nottaxPrice);
											break;
										}
									}
									break;
								case 4:
									orderDetailsFormMap.set("amount",value);
									orderDetailsFormMap.set("unsendAmount",value);break;
								case 5:
									String deliveryTime=value;
									deliveryTime=deliveryTime.replace("//","-");
									deliveryTime=deliveryTime.replace("/","-");
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
									String f = sdf.format(sdf.parse(deliveryTime));
									orderDetailsFormMap.set("deliveryTime",f);

									orderDetailsFormMap.set("modifytime",nowtime);
									orderDetailsFormMap.set("userId",userId);
									String orderId1=orderDetailsFormMap.get("orderId")+"";
									if(orderId1!=null&&!orderId1.equals("")){
										String amount=orderDetailsFormMap.getStr("amount")+"";
										String ordertaxPrice=orderDetailsFormMap.getStr("ordertaxPrice")+"";
										float money=ToolCommon.StringToFloat(amount)*ToolCommon.StringToFloat(ordertaxPrice);
										money=ToolCommon.FloatToMoney(money);
										orderDetailsFormMap.set("money",money);
										orderDetailsMapper.addEntity(orderDetailsFormMap);
										OrderDetailsFormMap orderDetailsFormMap1=orderDetailsFormMap;
										String orderdetailsId=orderDetailsFormMap1.get("id")+"";
										String goodId=orderDetailsFormMap1.get("goodId")+"";
										String orderAmount=orderDetailsFormMap1.get("amount")+"";
										float amountF=0;
										if(ToolCommon.isContain(amount,".")){
											amount=ToolCommon.StringToInteger(amount)+"";
										}
										BlankSizeFormMap blankSizeFormMap=blankSizeMapper.findByGoodId(goodId);
										BlankFormMap blankFormMap=new BlankFormMap();
										blankFormMap.set("userId",userId);
										blankFormMap.set("modifytime",nowtime);
										blankFormMap.set("goodId",goodId);
										blankFormMap.set("orderdetailsId",orderdetailsId);
										blankFormMap.set("origin","??????");
										blankFormMap.set("materialQuality",materialQuality);
										blankFormMap.remove("id");
										String blankSize="";
										if(blankSizeFormMap!=null){
											blankSize=blankSizeFormMap.get("blankSize")+"";
											blankFormMap.set("blankSize",blankSize);
											String outSize=blankSize.split("\\*")[0];
											outSize=outSize.replace("??","");
											String inside=blankSize.split("\\*")[1];
											String length="";
											if(ToolCommon.isContain(inside,"??")){
												inside=inside.replace("??","");
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
											float orderAmountF=ToolCommon.StringToFloat(orderAmount);
											//??????????????????100
//											StockFormMap stockFormMap=stockMapper.findByGoodId(goodId);
//											String stockAmount="0";
//											if(stockFormMap!=null){
//												stockAmount=stockFormMap.get("amount")+"";
//											}
//											float stockAmountF=ToolCommon.StringToFloat(stockAmount);
//											if(outsideF>100){
//												amount=(orderAmountF+2-stockAmountF)+"";
//											}else{
//												amount=(orderAmountF+5-stockAmountF)+"";
//											}

											amount=(orderAmountF+blankAmountF)+"";
											if(ToolCommon.isContain(amount,".")){
												amount=ToolCommon.StringToInteger(amount)+"";
											}
											blankFormMap.set("amount",amount);
											amountF=ToolCommon.StringToFloat(amount);
											double lengthOrderD=amountF*lengthF/1000;
											lengthOrderD = ToolCommon.Double4(lengthOrderD);
											blankFormMap.set("length",lengthOrderD+"");
											double weight=outsideF*outsideF*lengthOrderD*(0.00617/1000)-insideF*insideF*lengthOrderD*(0.00617/1000);
											weight = ToolCommon.Double4(weight);
											blankFormMap.set("weight",weight+"");
										}
										blankMapper.addEntity(blankFormMap);

										String blankId=blankFormMap.get("id")+"";

										List<GoodProcessFormMap> goodProcessFormMaps=goodProcessMapper.findByGoodId(goodId);
										for(int jj=0;jj<goodProcessFormMaps.size();jj++) {
											GoodProcessFormMap goodProcessFormMap = goodProcessFormMaps.get(jj);
											String goodprocessId = goodProcessFormMap.get("id") + "";
											String processId = goodProcessFormMap.get("processId") + "";

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
								break;

							}
						}else{
							i=sheet1.getLastRowNum()+1;
							j=lastColNum+1;
						}

					}
				}else{
					i=sheet1.getLastRowNum()+1;
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
	@RequestMapping("refreshProductionByOrderDetailsId")
	@Transactional(readOnly=false)//???????????????????????????????????????
	@SystemLog(module="????????????",methods="?????????-??????????????????")//??????????????????????????????.???????????????????????????
	public String refreshProductionByOrderDetailsId(String orderdetailsId) throws Exception {
		String origin=getPara("origin");
		UserFormMap userFormMap=getNowUserMessage();
		String userId=userFormMap.get("id")+"";
		String nowtime=ToolCommon.getNowTime();
		SystemconfigFormMap systemconfigFormMapWages=systemconfigMapper.findByName("dailyWages");
		String dailyWages=systemconfigFormMapWages.get("content")+"";
		float dailyWagesF=ToolCommon.StringToFloat(dailyWages);
		BlankFormMap blankFormMap1 = getFormMap(BlankFormMap.class);
		blankFormMap1.set("orderdetailsId",orderdetailsId);
		blankFormMap1.set("origin",origin);
		List<BlankFormMap> blankFormMaps=blankMapper.findByOrderdetailsIdAndOrigin(blankFormMap1);
		for(int ii=0;ii<blankFormMaps.size();ii++){
			BlankFormMap blankFormMap=blankFormMaps.get(ii);
			String batchNumber="";
			if(ii==0){
				batchNumber=blankFormMap.get("batchNumber")+"";
			}
			String amount="";
			float amountF=0;
			String goodId="";
			String blankId="";
			amount=blankFormMap.get("amount")+"";
			amountF=ToolCommon.StringToFloat(amount);
			goodId=blankFormMap.get("goodId")+"";
			blankId=blankFormMap.get("id")+"";
			List<BlankProcessFormMap> blankProcessFormMaps=blankProcessMapper.findByBlankId(blankId);
			for(int i=0;i<blankProcessFormMaps.size();i++){
				BlankProcessFormMap blankProcessFormMap=blankProcessFormMaps.get(i);
				String blankprocessId=blankProcessFormMap.get("id")+"";
				int count=workersubmitMapper.findCountByBlankprocessId(blankprocessId);
				if(count==0){
					blankProcessMapper.deleteByAttribute("id",blankprocessId,BlankProcessFormMap.class);
				}
			}

			List<GoodProcessFormMap> goodProcessFormMaps=goodProcessMapper.findByGoodId(goodId);
			for(int jj=0;jj<goodProcessFormMaps.size();jj++) {
				GoodProcessFormMap goodProcessFormMap = goodProcessFormMaps.get(jj);
				String goodprocessId = goodProcessFormMap.get("id") + "";
				BlankProcessFormMap blankProcessFormMap11=new BlankProcessFormMap();
				blankProcessFormMap11.set("blankId",blankId);
				blankProcessFormMap11.set("goodprocessId",goodprocessId);
				int count=blankProcessMapper.findCountByBlankprocessId(blankProcessFormMap11);
				if(count==0){
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
					if(origin.equals("??????")){
						blankProcessFormMap.set("batchNumber", batchNumber);
					}

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
	@RequestMapping("deleteOrderDetailsEntity")
	@Transactional(readOnly=false)//???????????????????????????????????????
	@SystemLog(module="????????????",methods="?????????-?????????")//??????????????????????????????.???????????????????????????
	public String deleteOrderDetailsEntity() throws Exception {
		String orderdetailsId=getPara("orderdetailsId");
		String orderId=getPara("orderId");
		OrderDetailsFormMap orderDetailsFormMap=new OrderDetailsFormMap();
		orderDetailsFormMap.set("orderId",orderId);
		orderDetailsFormMap.set("id",orderdetailsId);
		orderDetailsMapper.deleteByNames(orderDetailsFormMap);
		return "success";
	}
	@RequestMapping("downOrderDemo")
	public void downOrderDemo(Model model,String url,HttpServletRequest request,HttpServletResponse response) throws Exception {
		String path = request.getRealPath("");//????????????????????????????????????????????????.
//		url=url.replace("/","\\");
		String fileUrl=path+url;
		System.out.print(fileUrl);
		File file=new File(fileUrl);
		String name=file.getName();
		ToolCommon.exportFile(file,name,response);
	}
	@ResponseBody
	@RequestMapping("getOrderDetailsByOrderId")
	public OrderDetailsFormMap getOrderDetailsByOrderId(@RequestParam(required=true) String id) throws Exception {
		OrderDetailsFormMap orderDetailsFormMap1 = getFormMap(OrderDetailsFormMap.class);
		if(id.equals("")){
			List<OrderDetailsFormMap> orderDetailsFormMaps= new ArrayList<>();
			orderDetailsFormMap1.set("rows",orderDetailsFormMaps);
		}else{
			orderDetailsFormMap1.set("orderId",id);
			List<OrderDetailsFormMap> workingcenterProcessFormMapList= orderDetailsMapper.getOrderDetailsByOrderId(orderDetailsFormMap1);
			orderDetailsFormMap1.set("rows",workingcenterProcessFormMapList);
		}
		return orderDetailsFormMap1;
	}

	@ResponseBody
	@RequestMapping("getOrderGodeCodeByOrderId")
	public OrderGoodCodeFormMap getOrderGodeCodeByOrderId(@RequestParam(required=true) String id) throws Exception {
		OrderGoodCodeFormMap orderGoodCodeFormMap1 = getFormMap(OrderGoodCodeFormMap.class);
		if(id.equals("")){
			List<OrderDetailsFormMap> orderDetailsFormMaps= new ArrayList<>();
			orderGoodCodeFormMap1.set("rows",orderDetailsFormMaps);
		}else{
			List<OrderGoodCodeFormMap> workingcenterProcessFormMapList= orderGoodCodeMapper.findByAttribute("orderId",id,OrderGoodCodeFormMap.class);
			orderGoodCodeFormMap1.set("rows",workingcenterProcessFormMapList);
		}
		return orderGoodCodeFormMap1;
	}

	@ResponseBody
	@RequestMapping("getProcessByOrderDetailsId")
	public OrderDetailsProcessFormMap getProcessByOrderDetailsId(@RequestParam(required=true) String id) throws Exception {
		OrderDetailsProcessFormMap orderDetailsFormMap1 = getFormMap(OrderDetailsProcessFormMap.class);
		if(id.equals("")){
			List<OrderDetailsProcessFormMap> orderDetailsFormMaps= new ArrayList<>();
			orderDetailsFormMap1.set("rows",orderDetailsFormMaps);
		}else{
			orderDetailsFormMap1.set("orderdetailsId",id);
			List<OrderDetailsProcessFormMap> workingcenterProcessFormMapList= orderDetailsProcessMapper.getDetailsByOrderDetailsId(orderDetailsFormMap1);
			orderDetailsFormMap1.set("rows",workingcenterProcessFormMapList);
		}
		return orderDetailsFormMap1;
	}
	@ResponseBody
	@RequestMapping("getUseTimeByOrderId")
	public OrderDetailsProcessFormMap getSumUseTimeByOrderId(@RequestParam(required=true) String id) throws Exception {
		OrderDetailsProcessFormMap orderDetailsFormMap1 = getFormMap(OrderDetailsProcessFormMap.class);
		if(id.equals("")){
			List<OrderDetailsProcessFormMap> orderDetailsFormMaps= new ArrayList<>();
			orderDetailsFormMap1.set("rows",orderDetailsFormMaps);
		}else{
			orderDetailsFormMap1.set("orderId",id);
			List<OrderDetailsProcessFormMap> workingcenterProcessFormMapList= orderDetailsProcessMapper.getSumUseTimesByOrderId(orderDetailsFormMap1);
			orderDetailsFormMap1.set("rows",workingcenterProcessFormMapList);
		}
		return orderDetailsFormMap1;
	}

	@ResponseBody
	@RequestMapping("getDetailsWorkersumitByUserId")
	public OrderDetailsProcessFormMap getDetailsWorkersumitByUserId(String userId,String starttime,String endtime) throws Exception {
		OrderDetailsProcessFormMap orderDetailsFormMap1 = getFormMap(OrderDetailsProcessFormMap.class);
		if(userId.equals("")){
			List<OrderDetailsProcessFormMap> orderDetailsFormMaps= new ArrayList<>();
			orderDetailsFormMap1.set("rows",orderDetailsFormMaps);
		}else{
			orderDetailsFormMap1.set("completeUserId",userId);
			orderDetailsFormMap1.set("startTime",starttime);
			orderDetailsFormMap1.set("endTime",endtime);
			List<OrderDetailsProcessFormMap> workingcenterProcessFormMapList= orderDetailsProcessMapper.findByCompleteUserIdAndStartTimeAndEndTime(orderDetailsFormMap1);
			orderDetailsFormMap1.set("rows",workingcenterProcessFormMapList);
		}
		return orderDetailsFormMap1;
	}

	@ResponseBody
	@RequestMapping("getDetailsWorkersumitByUserName")
	public OrderDetailsProcessFormMap getDetailsWorkersumitByUserName(String userName,String starttime,String endtime) throws Exception {
		OrderDetailsProcessFormMap orderDetailsFormMap1 = getFormMap(OrderDetailsProcessFormMap.class);
		if(userName.equals("")){
			List<OrderDetailsProcessFormMap> orderDetailsFormMaps= new ArrayList<>();
			orderDetailsFormMap1.set("rows",orderDetailsFormMaps);
		}else{
			endtime=ToolCommon.addDay(endtime,1);
			orderDetailsFormMap1.set("userName",userName);
			orderDetailsFormMap1.set("startTime",starttime);
			orderDetailsFormMap1.set("endTime",endtime);
			List<OrderDetailsProcessFormMap> workingcenterProcessFormMapList= orderDetailsProcessMapper.findByCompleteUserNameAndStartTimeAndEndTime(orderDetailsFormMap1);
			orderDetailsFormMap1.set("rows",workingcenterProcessFormMapList);
		}
		return orderDetailsFormMap1;
	}

    @ResponseBody
    @RequestMapping("getProcessStateAndProcessByOrderId")
    public OrderDetailsProcessFormMap getProcessStateAndProcessByOrderId(String id) throws Exception {
        OrderDetailsProcessFormMap orderDetailsFormMap1 = getFormMap(OrderDetailsProcessFormMap.class);
        if(id.equals("")){
            List<OrderDetailsProcessFormMap> orderDetailsFormMaps= new ArrayList<>();
            orderDetailsFormMap1.set("rows",orderDetailsFormMaps);
        }else{
            orderDetailsFormMap1.set("id",id);
            List<OrderDetailsProcessFormMap> orderDetailsProcessFormMaps= orderDetailsProcessMapper.findProcessStateAndProcessByOrderId(orderDetailsFormMap1);
			List<OrderDetailsProcessFormMap> orderDetailsProcessFormMapsAll=new ArrayList<>();
			List<OrderDetailsProcessFormMap> orderDetailsProcessFormMapsAllShow=new ArrayList<>();
            for(int i=0;i<orderDetailsProcessFormMaps.size();i++){
				orderDetailsProcessFormMapsAll.clear();
				OrderDetailsProcessFormMap orderDetailsProcessFormMapj=orderDetailsProcessFormMaps.get(i);
            	OrderDetailsProcessFormMap orderDetailsProcessFormMap=orderDetailsProcessFormMaps.get(i);
				List<WorkersubmitFormMap> workersubmitFormMaps1=new ArrayList<>();
            	String orderdetailsprocessId1=orderDetailsProcessFormMap.get("orderdetailsprocessId1")+"";
            	if(orderdetailsprocessId1!=null&&!orderdetailsprocessId1.equals("")){
					workersubmitFormMaps1=workersubmitMapper.findUserNameAndGoodCodeByOrderdetailsprocessId(orderdetailsprocessId1);
				}

				List<WorkersubmitFormMap> workersubmitFormMaps2=new ArrayList<>();
				String orderdetailsprocessId2=orderDetailsProcessFormMap.get("orderdetailsprocessId2")+"";
				if(orderdetailsprocessId2!=null&&!orderdetailsprocessId2.equals("")){
					workersubmitFormMaps2=workersubmitMapper.findUserNameAndGoodCodeByOrderdetailsprocessId(orderdetailsprocessId2);
				}

				List<WorkersubmitFormMap> workersubmitFormMaps3=new ArrayList<>();
				String orderdetailsprocessId3=orderDetailsProcessFormMap.get("orderdetailsprocessId3")+"";
				if(orderdetailsprocessId3!=null&&!orderdetailsprocessId3.equals("")){
					workersubmitFormMaps3=workersubmitMapper.findUserNameAndGoodCodeByOrderdetailsprocessId(orderdetailsprocessId3);
				}

				List<WorkersubmitFormMap> workersubmitFormMaps4=new ArrayList<>();
				String orderdetailsprocessId4=orderDetailsProcessFormMap.get("orderdetailsprocessId4")+"";
				if(i==8){
					System.out.print("aa");
				}
				if(orderdetailsprocessId4!=null&&!orderdetailsprocessId4.equals("")){
					workersubmitFormMaps4=workersubmitMapper.findUserNameAndGoodCodeByOrderdetailsprocessId(orderdetailsprocessId4);
				}

				List<WorkersubmitFormMap> workersubmitFormMaps5=new ArrayList<>();
				String orderdetailsprocessId5=orderDetailsProcessFormMap.get("orderdetailsprocessId5")+"";
				if(orderdetailsprocessId5!=null&&!orderdetailsprocessId5.equals("")){
					workersubmitFormMaps5=workersubmitMapper.findUserNameAndGoodCodeByOrderdetailsprocessId(orderdetailsprocessId5);
				}

				int allCount=0;
				String completeer1S="";
				String code1S="";
				String id1S="";

				String completeer2S="";
				String code2S="";
				String id2S="";

				String completeer3S="";
				String code3S="";
				String id3S="";

				String completeer4S="";
				String code4S="";
				String id4S="";

				String completeer5S="";
				String code5S="";
				String id5S="";

            		if(workersubmitFormMaps1.size()>0){
            			for(int j=0;j<workersubmitFormMaps1.size();j++){
							OrderDetailsProcessFormMap orderDetailsProcessFormMap1=new OrderDetailsProcessFormMap();
							WorkersubmitFormMap workersubmitFormMap=workersubmitFormMaps1.get(j);
							String userName=workersubmitFormMap.getStr("userName");
							String goodCode=workersubmitFormMap.getStr("goodCode");
							String idCode=workersubmitFormMap.get("id")+"";
							id1S=id1S+idCode+";";
							orderDetailsProcessFormMap1.set("oprateCompleter1",userName);
							orderDetailsProcessFormMap1.set("ordergoodcode1",goodCode);
							completeer1S=completeer1S+userName+";";
							code1S=code1S+goodCode+";";
							if(j==0){
								orderDetailsProcessFormMap1.set("goodId",orderDetailsProcessFormMapj.get("goodId")+"");
								orderDetailsProcessFormMap1.set("goodName",orderDetailsProcessFormMapj.get("goodName")+"");
								orderDetailsProcessFormMap1.set("amount",orderDetailsProcessFormMapj.get("amount")+"");
								orderDetailsProcessFormMap1.set("processName1",orderDetailsProcessFormMapj.get("processName1")+"");
								orderDetailsProcessFormMap1.set("state1",orderDetailsProcessFormMapj.get("state1")+"");
								orderDetailsProcessFormMap1.set("processName2",orderDetailsProcessFormMapj.get("processName2")+"");
								orderDetailsProcessFormMap1.set("state2",orderDetailsProcessFormMapj.get("state2")+"");
								orderDetailsProcessFormMap1.set("processName3",orderDetailsProcessFormMapj.get("processName3")+"");
								orderDetailsProcessFormMap1.set("state3",orderDetailsProcessFormMapj.get("state3")+"");
								orderDetailsProcessFormMap1.set("processName4",orderDetailsProcessFormMapj.get("processName4")+"");
								orderDetailsProcessFormMap1.set("state4",orderDetailsProcessFormMapj.get("state4")+"");
								orderDetailsProcessFormMap1.set("processName5",orderDetailsProcessFormMapj.get("processName5")+"");
								orderDetailsProcessFormMap1.set("state5",orderDetailsProcessFormMapj.get("state5")+"");
							}


							orderDetailsProcessFormMapsAll.add(orderDetailsProcessFormMap1);
						}
					}else{
						orderDetailsProcessFormMapsAll.add(orderDetailsProcessFormMap);
					}
					String goodId=orderDetailsProcessFormMapsAll.get(0).getStr("goodId");
					String amount=orderDetailsProcessFormMapsAll.get(0).getStr("amount");
					String goodName=orderDetailsProcessFormMapsAll.get(0).getStr("goodName");

					String processName1=orderDetailsProcessFormMapsAll.get(0).getStr("processName1");
					String state1=orderDetailsProcessFormMapsAll.get(0).getStr("state1");

				String processName2=orderDetailsProcessFormMapsAll.get(0).getStr("processName2");
				String state2=orderDetailsProcessFormMapsAll.get(0).getStr("state2");

				String processName3=orderDetailsProcessFormMapsAll.get(0).getStr("processName3");
				String state3=orderDetailsProcessFormMapsAll.get(0).getStr("state3");

				String processName4=orderDetailsProcessFormMapsAll.get(0).getStr("processName4");
				String state4=orderDetailsProcessFormMapsAll.get(0).getStr("state4");

				String processName5=orderDetailsProcessFormMapsAll.get(0).getStr("processName5");
				String state5=orderDetailsProcessFormMapsAll.get(0).getStr("state5");
					allCount=orderDetailsProcessFormMapsAll.size();
					int completeer1Count=completeer1S.split(";").length;
					if(workersubmitFormMaps2.size()>0){
						if(i<allCount){

						}
						for(int n=0;n<allCount;n++){
							for(int j=0;j<workersubmitFormMaps2.size();j++){
								String oprateCompleter1="";
								String ordergoodcode1="";
								if(j<completeer1Count){
									oprateCompleter1=completeer1S.split(";")[j];
									ordergoodcode1=code1S.split(";")[j];
								}

								OrderDetailsProcessFormMap orderDetailsProcessFormMap1=new OrderDetailsProcessFormMap();
								WorkersubmitFormMap workersubmitFormMap=workersubmitFormMaps2.get(j);
								String userName=workersubmitFormMap.getStr("userName");
								String goodCode=workersubmitFormMap.getStr("goodCode");
								String idCode=workersubmitFormMap.get("id")+"";
								boolean flag = Arrays.asList(id2S.split(";")).contains(idCode);
								if(flag){
									n++;
								}else {
									id2S = id2S + idCode + ";";
									completeer2S = completeer2S + userName + ";";
									code2S = code2S + goodCode + ";";
									orderDetailsProcessFormMap1.set("oprateCompleter2", userName);
									orderDetailsProcessFormMap1.set("ordergoodcode2", goodCode);
									orderDetailsProcessFormMap1.set("oprateCompleter1", oprateCompleter1);
									orderDetailsProcessFormMap1.set("ordergoodcode1", ordergoodcode1);
									if (j == 0) {
										orderDetailsProcessFormMap1.set("goodId", goodId);
										orderDetailsProcessFormMap1.set("goodName", goodName);
										orderDetailsProcessFormMap1.set("amount", amount);
										orderDetailsProcessFormMap1.set("state1", state1);
										orderDetailsProcessFormMap1.set("processName1", processName1);
										orderDetailsProcessFormMap1.set("processName2", processName2);
										orderDetailsProcessFormMap1.set("state2", state2);
										orderDetailsProcessFormMap1.set("processName3", processName3);
										orderDetailsProcessFormMap1.set("state3", state3);
										orderDetailsProcessFormMap1.set("processName4", processName4);
										orderDetailsProcessFormMap1.set("state4", state4);
										orderDetailsProcessFormMap1.set("processName5", processName5);
										orderDetailsProcessFormMap1.set("state5", state5);
									}

									if (j < allCount) {
										orderDetailsProcessFormMapsAll.set(j, orderDetailsProcessFormMap1);
									} else {
										orderDetailsProcessFormMapsAll.add(orderDetailsProcessFormMap1);
									}
								}
							}
						}
					}

				int completeer2Count=completeer2S.split(";").length;
				allCount=orderDetailsProcessFormMapsAll.size();
				if(goodId.equals("???4-1")){
					System.out.print("aaa");
				}
				if(workersubmitFormMaps3.size()>0){
					for(int n=0;n<allCount;n++){
						for(int j=0;j<workersubmitFormMaps3.size();j++){
							String oprateCompleter1="";
							String ordergoodcode1="";
							if(j<completeer1Count){
								oprateCompleter1=completeer1S.split(";")[j];
								ordergoodcode1=code1S.split(";")[j];
							}

							String oprateCompleter2="";
							String ordergoodcode2="";
							if(j<completeer2Count){
								oprateCompleter2=completeer2S.split(";")[j];
								ordergoodcode2=code2S.split(";")[j];
							}
							OrderDetailsProcessFormMap orderDetailsProcessFormMap1=new OrderDetailsProcessFormMap();
							WorkersubmitFormMap workersubmitFormMap=workersubmitFormMaps3.get(j);
							String userName=workersubmitFormMap.getStr("userName");
							String goodCode=workersubmitFormMap.getStr("goodCode");
							String idCode=workersubmitFormMap.get("id")+"";
							boolean flag = Arrays.asList(id3S.split(";")).contains(idCode);
							if(flag){
								n++;
							}else {
								id3S = id3S + idCode + ";";
								completeer3S = completeer3S + userName + ";";
								code3S = code3S + goodCode + ";";
								orderDetailsProcessFormMap1.set("oprateCompleter3", userName);
								orderDetailsProcessFormMap1.set("ordergoodcode3", goodCode);
								orderDetailsProcessFormMap1.set("oprateCompleter1", ordergoodcode1);
								orderDetailsProcessFormMap1.set("ordergoodcode1", oprateCompleter1);
								orderDetailsProcessFormMap1.set("oprateCompleter2", oprateCompleter2);
								orderDetailsProcessFormMap1.set("ordergoodcode2", ordergoodcode2);
								if (j == 0) {
									orderDetailsProcessFormMap1.set("goodId", goodId);
									orderDetailsProcessFormMap1.set("goodName", goodName);
									orderDetailsProcessFormMap1.set("amount", amount);
									orderDetailsProcessFormMap1.set("state1", state1);
									orderDetailsProcessFormMap1.set("processName1", processName1);
									orderDetailsProcessFormMap1.set("state2", state2);
									orderDetailsProcessFormMap1.set("processName2", processName2);
									orderDetailsProcessFormMap1.set("processName3", processName3);
									orderDetailsProcessFormMap1.set("state3", state3);
									orderDetailsProcessFormMap1.set("processName4", processName4);
									orderDetailsProcessFormMap1.set("state4", state4);
									orderDetailsProcessFormMap1.set("processName5", processName5);
									orderDetailsProcessFormMap1.set("state5", state5);
								}

								if (j < allCount) {
									orderDetailsProcessFormMapsAll.set(j, orderDetailsProcessFormMap1);
								} else {
									orderDetailsProcessFormMapsAll.add(orderDetailsProcessFormMap1);
								}
							}
						}
					}
				}


				int completeer3Count=completeer3S.split(";").length;
				allCount=orderDetailsProcessFormMapsAll.size();
				if(workersubmitFormMaps4.size()>0){
					for(int n=0;n<allCount;n++){
						for(int j=0;j<workersubmitFormMaps4.size();j++){
							String oprateCompleter1="";
							String ordergoodcode1="";
							if(j<completeer1Count){
								oprateCompleter1=completeer1S.split(";")[j];
								ordergoodcode1=code1S.split(";")[j];
							}

							String oprateCompleter2="";
							String ordergoodcode2="";
							if(j<completeer2Count){
								oprateCompleter2=completeer2S.split(";")[j];
								ordergoodcode2=code2S.split(";")[j];
							}

							String oprateCompleter3="";
							String ordergoodcode3="";
							if(j<completeer3Count){
								oprateCompleter3=completeer3S.split(";")[j];
								ordergoodcode3=code3S.split(";")[j];
							}

							OrderDetailsProcessFormMap orderDetailsProcessFormMap1=new OrderDetailsProcessFormMap();
							WorkersubmitFormMap workersubmitFormMap=workersubmitFormMaps4.get(j);
							String userName=workersubmitFormMap.getStr("userName");
							String goodCode=workersubmitFormMap.getStr("goodCode");
							String idCode=workersubmitFormMap.get("id")+"";
							boolean flag = Arrays.asList(id4S.split(";")).contains(idCode);
							if(flag){
								n++;
							}else{
								id4S=id4S+idCode+";";
								completeer4S=completeer4S+userName+";";
								code4S=code4S+goodCode+";";
								orderDetailsProcessFormMap1.set("oprateCompleter4",userName);
								orderDetailsProcessFormMap1.set("ordergoodcode4",goodCode);
								orderDetailsProcessFormMap1.set("oprateCompleter1",ordergoodcode1);
								orderDetailsProcessFormMap1.set("ordergoodcode1",oprateCompleter1);
								orderDetailsProcessFormMap1.set("oprateCompleter2",oprateCompleter2);
								orderDetailsProcessFormMap1.set("ordergoodcode2",ordergoodcode2);
								orderDetailsProcessFormMap1.set("oprateCompleter3",oprateCompleter3);
								orderDetailsProcessFormMap1.set("ordergoodcode3",ordergoodcode3);
								if(j==0){
									orderDetailsProcessFormMap1.set("goodId",goodId);
									orderDetailsProcessFormMap1.set("goodName",goodName);
									orderDetailsProcessFormMap1.set("amount",amount);
									orderDetailsProcessFormMap1.set("state1",state1);
									orderDetailsProcessFormMap1.set("processName1",processName1);
									orderDetailsProcessFormMap1.set("state2",state2);
									orderDetailsProcessFormMap1.set("processName2",processName2);
									orderDetailsProcessFormMap1.set("processName3",processName3);
									orderDetailsProcessFormMap1.set("state3",state3);
									orderDetailsProcessFormMap1.set("processName4",processName4);
									orderDetailsProcessFormMap1.set("state4",state4);
									orderDetailsProcessFormMap1.set("processName5",processName5);
									orderDetailsProcessFormMap1.set("state5",state5);
								}

								if(j<allCount){
									orderDetailsProcessFormMapsAll.set(j,orderDetailsProcessFormMap1);
								}else{
									orderDetailsProcessFormMapsAll.add(orderDetailsProcessFormMap1);
								}
							}


						}
					}
				}

				int completeer4Count=completeer4S.split(";").length;
				allCount=orderDetailsProcessFormMapsAll.size();
				if(workersubmitFormMaps5.size()>0){
					for(int n=0;n<allCount;n++){
						for(int j=0;j<workersubmitFormMaps5.size();j++){
							String oprateCompleter1="";
							String ordergoodcode1="";
							if(j<completeer1Count){
								oprateCompleter1=completeer1S.split(";")[j];
								ordergoodcode1=code1S.split(";")[j];
							}

							String oprateCompleter2="";
							String ordergoodcode2="";
							if(j<completeer2Count){
								oprateCompleter2=completeer2S.split(";")[j];
								ordergoodcode2=code2S.split(";")[j];
							}

							String oprateCompleter3="";
							String ordergoodcode3="";
							if(j<completeer3Count){
								oprateCompleter3=completeer3S.split(";")[j];
								ordergoodcode3=code3S.split(";")[j];
							}

							String oprateCompleter4="";
							String ordergoodcode4="";
							if(j<completeer4Count){
								oprateCompleter4=completeer4S.split(";")[j];
								ordergoodcode4=code4S.split(";")[j];
							}

							OrderDetailsProcessFormMap orderDetailsProcessFormMap1=new OrderDetailsProcessFormMap();
							WorkersubmitFormMap workersubmitFormMap=workersubmitFormMaps5.get(j);
							String userName=workersubmitFormMap.getStr("userName");
							String goodCode=workersubmitFormMap.getStr("goodCode");
							String idCode=workersubmitFormMap.get("id")+"";
							boolean flag = Arrays.asList(id5S.split(";")).contains(idCode);
							if(flag){
								n++;
							}else{
								id5S=id5S+idCode+";";
								completeer5S=completeer5S+userName+";";
								code5S=code5S+goodCode+";";
								orderDetailsProcessFormMap1.set("oprateCompleter5",userName);
								orderDetailsProcessFormMap1.set("ordergoodcode5",goodCode);
								orderDetailsProcessFormMap1.set("oprateCompleter4",oprateCompleter4);
								orderDetailsProcessFormMap1.set("ordergoodcode4",ordergoodcode4);
								orderDetailsProcessFormMap1.set("oprateCompleter1",ordergoodcode1);
								orderDetailsProcessFormMap1.set("ordergoodcode1",oprateCompleter1);
								orderDetailsProcessFormMap1.set("oprateCompleter2",oprateCompleter2);
								orderDetailsProcessFormMap1.set("ordergoodcode2",ordergoodcode2);
								orderDetailsProcessFormMap1.set("oprateCompleter3",oprateCompleter3);
								orderDetailsProcessFormMap1.set("ordergoodcode3",ordergoodcode3);
								orderDetailsProcessFormMap1.set("oprateCompleter3",oprateCompleter3);
								orderDetailsProcessFormMap1.set("ordergoodcode3",ordergoodcode3);
								if(j==0){
									orderDetailsProcessFormMap1.set("goodId",goodId);
									orderDetailsProcessFormMap1.set("goodName",goodName);
									orderDetailsProcessFormMap1.set("amount",amount);
									orderDetailsProcessFormMap1.set("state1",state1);
									orderDetailsProcessFormMap1.set("processName1",processName1);
									orderDetailsProcessFormMap1.set("state2",state2);
									orderDetailsProcessFormMap1.set("processName2",processName2);
									orderDetailsProcessFormMap1.set("processName3",processName3);
									orderDetailsProcessFormMap1.set("state3",state3);
									orderDetailsProcessFormMap1.set("processName4",processName4);
									orderDetailsProcessFormMap1.set("state4",state4);
									orderDetailsProcessFormMap1.set("processName5",processName5);
									orderDetailsProcessFormMap1.set("state5",state5);
								}

								if(j<allCount){
									orderDetailsProcessFormMapsAll.set(j,orderDetailsProcessFormMap1);
								}else{
									orderDetailsProcessFormMapsAll.add(orderDetailsProcessFormMap1);
								}
							}


						}
					}
				}
				orderDetailsProcessFormMapsAllShow.addAll(orderDetailsProcessFormMapsAll);
            }
            orderDetailsFormMap1.set("rows",orderDetailsProcessFormMapsAllShow);
        }
        return orderDetailsFormMap1;
    }

	@RequestMapping("/exportOrderStatistics")
	public void exportOrderStatistics(HttpServletRequest request, HttpServletResponse response,@RequestParam(required=true) String id,@RequestParam(required=true) String title,@RequestParam(required=true) String fileName,@RequestParam(required=true) String sheetName) throws IOException {
		String exportData =title;// ????????????json?????????
		List<Map<String, Object>> listMap = JsonUtils.parseJSONList(exportData);
		OrderDetailsProcessFormMap orderDetailsFormMap1=new OrderDetailsProcessFormMap();
		List<OrderDetailsProcessFormMap> orderDetailsFormMaps= new ArrayList<>();
		if(id.equals("")){
		}else{
			orderDetailsFormMap1.set("id",id);
			orderDetailsFormMaps= orderDetailsProcessMapper.findProcessStateAndProcessByOrderId(orderDetailsFormMap1);
		}
		POIUtils.exportToExcel(response, listMap, orderDetailsFormMaps, fileName,sheetName);
	}


	@ResponseBody
	@RequestMapping("orderSelect")
	public OrderFormMap orderSelect(String p) throws Exception {
		OrderFormMap orderFormMap= getFormMap(OrderFormMap.class);
		String page=getPara("page");
		String rows=getPara("rows");
		String q=getPara("q");
		if(q==null){
			q="";
		}
		orderFormMap.set("content",q);
		if(page.equals("1")){
			total=orderMapper.findCountByAllLike(orderFormMap);
		}
		orderFormMap=toFormMap(orderFormMap, page, rows,orderFormMap.getStr("orderby"));
		List<OrderFormMap> departmentFormMapList=orderMapper.findByAllLike(orderFormMap);
		orderFormMap.set("rows",departmentFormMapList);
		orderFormMap.set("total",total);
		return orderFormMap;
	}

	@ResponseBody
	@RequestMapping("getProcessByOrderId")
	public OrderDetailsProcessFormMap getProcessByOrderId(@RequestParam(required=true) String id) throws Exception {
		OrderDetailsProcessFormMap orderDetailsFormMap1 = getFormMap(OrderDetailsProcessFormMap.class);
		if(id.equals("")){
			List<OrderDetailsProcessFormMap> orderDetailsFormMaps= new ArrayList<>();
			orderDetailsFormMap1.set("rows",orderDetailsFormMaps);
		}else{
			orderDetailsFormMap1.set("orderId",id);
			List<OrderDetailsProcessFormMap> workingcenterProcessFormMapList= orderDetailsProcessMapper.getProcessDetailsByOrderId(orderDetailsFormMap1);
			orderDetailsFormMap1.set("rows",workingcenterProcessFormMapList);
		}
		return orderDetailsFormMap1;
	}


	@RequestMapping("editUI")
	public String editUI(Model model) throws Exception {
		String id = getPara("id");
		if(Common.isNotEmpty(id)){
			model.addAttribute("order", orderMapper.findbyFrist("id", id, OrderFormMap.class));
		}else{

		}
		return Common.BACKGROUND_PATH + "/system/order/edit";
	}
	@ResponseBody
	@RequestMapping("getorderById")
	public OrderDetailsFormMap getorderById(@RequestParam(required=true) String id) throws Exception {
		OrderDetailsFormMap orderDetailsFormMap = getFormMap(OrderDetailsFormMap.class);
		if(id.equals("")){
			List<OrderDetailsFormMap> orderDetailsFormMapList= new ArrayList<>();
			orderDetailsFormMap.set("rows",orderDetailsFormMapList);
		}else{
			orderDetailsFormMap.set("orderId",id);
			List<OrderDetailsFormMap> orderDetailsFormMapList= orderDetailsMapper.findByWhere(orderDetailsFormMap);
			orderDetailsFormMap.set("rows",orderDetailsFormMapList);
		}
		return orderDetailsFormMap;
	}

	@ResponseBody
	@RequestMapping("checkOrderById")
	public String checkOrderById(@RequestParam(required=true) String id,@RequestParam(required=true) String remarks) throws Exception {
		OrderFormMap orderFormMap=orderMapper.findbyFrist("id",id,OrderFormMap.class);
		if(orderFormMap!=null){
			String nowtime=ToolCommon.getNowTime();
			UserFormMap userFormMap=getNowUserMessage();
			String userId=String.valueOf(userFormMap.getInt("id"));
			orderFormMap.set("examineTime",nowtime);
			orderFormMap.set("examineRemarks",remarks);
			orderFormMap.set("examineUserId",userId);
			orderFormMap.set("state","?????????");
			orderMapper.editEntity(orderFormMap);
		}

		return "success";
	}
	@ResponseBody
	@RequestMapping("editEntity")
	@Transactional(readOnly=false)//???????????????????????????????????????
	@SystemLog(module="????????????",methods="????????????-????????????")//??????????????????????????????.???????????????????????????
	public String editEntity(String entity) throws Exception {
		SystemconfigFormMap systemconfigFormMapWages=systemconfigMapper.findByName("dailyWages");
		String dailyWages=systemconfigFormMapWages.get("content")+"";
		float dailyWagesF=ToolCommon.StringToFloat(dailyWages);
		String order = ToolCommon.json2Object(entity).getString("order");
		String orderdetails = ToolCommon.json2Object(entity).getString("orderdetails");
		OrderFormMap offerFormMap = (OrderFormMap) ToolCommon.json2Object(order, OrderFormMap.class);
		String id = offerFormMap.getStr("id");
		String nowtime = ToolCommon.getNowTime();
		UserFormMap userFormMap = getNowUserMessage();
		String userId = String.valueOf(userFormMap.getInt("id"));
		if (id != null && !id.equals("")) {
			OrderFormMap offerFormMap1 = orderMapper.findbyFrist("id", id, OrderFormMap.class);
			offerFormMap.set("state", "??????????????????");
			if (offerFormMap1 == null) {
				offerFormMap.set("modifytime", nowtime);
				offerFormMap.set("userId", userId);
				orderMapper.addEntity(offerFormMap);
			} else {
				offerFormMap.set("modifytime", nowtime);
				offerFormMap.set("userId", userId);
				orderMapper.editEntity(offerFormMap);
			}
		}
		String orderId = offerFormMap.getStr("id");
		List<OrderDetailsFormMap> list = (List) ToolCommon.json2ObjectList(orderdetails, OrderDetailsFormMap.class);
		for (int i = 0; i < list.size(); i++) {
			OrderDetailsFormMap orderDetailsFormMap = list.get(i);
			String orderdetailsId = orderDetailsFormMap.get("id") + "";
			orderDetailsFormMap.set("modifytime", nowtime);
			orderDetailsFormMap.set("userId", userId);
			if (orderdetailsId==null||orderdetailsId.equals("")||orderdetailsId.equals("null")) {
				orderDetailsFormMap.remove("id");
				orderDetailsFormMap.set("orderId",orderId);
				orderDetailsFormMap.set("unsendAmount",orderDetailsFormMap.get("amount")+"");
				orderDetailsMapper.addEntity(orderDetailsFormMap);
				orderdetailsId=orderDetailsFormMap.get("id")+"";
				OrderDetailsFormMap orderDetailsFormMap1=orderDetailsFormMap;
				String goodId=orderDetailsFormMap1.get("goodId")+"";
				String materialQuality=orderDetailsFormMap1.get("materialQuality")+"";
				String orderAmount=orderDetailsFormMap1.get("amount")+"";
				String amount="";
				float amountF=0;
				if(ToolCommon.isContain(amount,".")){
					amount=ToolCommon.StringToInteger(amount)+"";
				}
				BlankSizeFormMap blankSizeFormMap=blankSizeMapper.findByGoodId(goodId);
				BlankFormMap blankFormMap=new BlankFormMap();
				blankFormMap.set("userId",userId);
				blankFormMap.set("modifytime",nowtime);
				blankFormMap.set("goodId",goodId);
				blankFormMap.set("orderdetailsId",orderdetailsId);
				blankFormMap.set("origin","??????");
				blankFormMap.set("materialQuality",materialQuality);
				blankFormMap.remove("id");
				String blankSize="";
				if(blankSizeFormMap!=null){
					blankSize=blankSizeFormMap.get("blankSize")+"";
					blankFormMap.set("blankSize",blankSize);
					String outSize=blankSize.split("\\*")[0];
					outSize=outSize.replace("??","");
					String inside=blankSize.split("\\*")[1];
					String length="";
					if(ToolCommon.isContain(inside,"??")){
						inside=inside.replace("??","");
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
//					StockFormMap stockFormMap=stockMapper.findByGoodId(goodId);
//					String stockAmount="0";
//					if(stockFormMap!=null){
//						stockAmount=stockFormMap.get("amount")+"";
//					}
//					float stockAmountF=ToolCommon.StringToFloat(stockAmount);
					float orderAmountF=ToolCommon.StringToFloat(orderAmount);
//					if(outsideF>100){
//						amount=(orderAmountF+2-stockAmountF)+"";
//					}else{
//						amount=(orderAmountF+5-stockAmountF)+"";
//					}
					amount=(orderAmountF+2)+"";
					if(ToolCommon.isContain(amount,".")){
						amount=ToolCommon.StringToInteger(amount)+"";
					}
					blankFormMap.set("amount",amount);
					double lengthOrderD=amountF*lengthF/1000;
					lengthOrderD = ToolCommon.Double4(lengthOrderD);
					blankFormMap.set("length",lengthOrderD+"");
					double weight=outsideF*outsideF*lengthOrderD*(0.00617/1000)-insideF*insideF*lengthOrderD*(0.00617/1000);
					weight = ToolCommon.Double4(weight);
					blankFormMap.set("weight",weight+"");
				}
				blankMapper.addEntity(blankFormMap);

				String blankId=blankFormMap.get("id")+"";

				List<GoodProcessFormMap> goodProcessFormMaps=goodProcessMapper.findByGoodId(goodId);
				for(int jj=0;jj<goodProcessFormMaps.size();jj++) {
					GoodProcessFormMap goodProcessFormMap = goodProcessFormMaps.get(jj);
					String goodprocessId = goodProcessFormMap.get("id") + "";
					String processId = goodProcessFormMap.get("processId") + "";

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

			} else {
				orderDetailsMapper.editEntity(orderDetailsFormMap);
				orderdetailsId=orderDetailsFormMap.get("id")+"";
				BlankFormMap blankFormMap1 = getFormMap(BlankFormMap.class);
				blankFormMap1.set("orderdetailsId",orderdetailsId);
				List<BlankFormMap> blankFormMaps=blankMapper.findByByOrderdetailsId(blankFormMap1);
				for(int j=0;j<blankFormMaps.size();j++){
					String blankId=blankFormMaps.get(j).getStr("blankId");
					blankProcessMapper.deleteByAttribute("blankId",blankId,BlankProcessFormMap.class);
				}
				blankMapper.deleteByOrderDetailsId(orderdetailsId);
				OrderDetailsFormMap orderDetailsFormMap1=orderDetailsFormMap;
				String goodId=orderDetailsFormMap1.get("goodId")+"";
				String materialQuality=orderDetailsFormMap1.get("materialQuality")+"";
				String orderAmount=orderDetailsFormMap1.get("amount")+"";
				String amount="";
				float amountF=0;
				if(ToolCommon.isContain(amount,".")){
					amount=ToolCommon.StringToInteger(amount)+"";
				}
				BlankSizeFormMap blankSizeFormMap=blankSizeMapper.findByGoodId(goodId);
				BlankFormMap blankFormMap=new BlankFormMap();
				blankFormMap.set("userId",userId);
				blankFormMap.set("modifytime",nowtime);
				blankFormMap.set("goodId",goodId);
				blankFormMap.set("orderdetailsId",orderdetailsId);
				blankFormMap.set("origin","??????");
				blankFormMap.set("materialQuality",materialQuality);
				blankFormMap.remove("id");
				String blankSize="";
				if(blankSizeFormMap!=null){
					blankSize=blankSizeFormMap.get("blankSize")+"";
					blankFormMap.set("blankSize",blankSize);
					String outSize=blankSize.split("\\*")[0];
					outSize=outSize.replace("??","");
					String inside=blankSize.split("\\*")[1];
					String length="";
					if(ToolCommon.isContain(inside,"??")){
						inside=inside.replace("??","");
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

//					StockFormMap stockFormMap=stockMapper.findByGoodId(goodId);
//					String stockAmount="0";
//					if(stockFormMap!=null){
//						stockAmount=stockFormMap.get("amount")+"";
//					}
//					float stockAmountF=ToolCommon.StringToFloat(stockAmount);
					float orderAmountF=ToolCommon.StringToFloat(orderAmount);
//					if(outsideF>100){
//						amount=(orderAmountF+2-stockAmountF)+"";
//					}else{
//						amount=(orderAmountF+5-stockAmountF)+"";
//					}
					amount=(orderAmountF+2)+"";
					if(ToolCommon.isContain(amount,".")){
						amount=ToolCommon.StringToInteger(amount)+"";
					}
					blankFormMap.set("amount",amount);
					amountF=ToolCommon.StringToFloat(amount+"");
					double lengthOrderD=amountF*lengthF/1000;
					lengthOrderD = ToolCommon.Double4(lengthOrderD);
					blankFormMap.set("length",lengthOrderD+"");
					double weight=outsideF*outsideF*lengthOrderD*(0.00617/1000)-insideF*insideF*lengthOrderD*(0.00617/1000);
					weight = ToolCommon.Double4(weight);
					blankFormMap.set("weight",weight+"");
				}
				blankMapper.addEntity(blankFormMap);

				String blankId=blankFormMap.get("id")+"";

				List<GoodProcessFormMap> goodProcessFormMaps=goodProcessMapper.findByGoodId(goodId);
				for(int jj=0;jj<goodProcessFormMaps.size();jj++) {
					GoodProcessFormMap goodProcessFormMap = goodProcessFormMaps.get(jj);
					String goodprocessId = goodProcessFormMap.get("id") + "";
					String processId = goodProcessFormMap.get("processId") + "";

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
		return "success:" + offerFormMap.get("id");
	}
}