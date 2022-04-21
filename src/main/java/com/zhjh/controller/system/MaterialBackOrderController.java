package com.zhjh.controller.system;

import com.zhjh.annotation.SystemLog;
import com.zhjh.bean.FileBean;
import com.zhjh.controller.index.BaseController;
import com.zhjh.entity.*;
import com.zhjh.mapper.*;
import com.zhjh.tool.ToolCommon;
import com.zhjh.tool.ToolExcel;
import com.zhjh.util.Common;
import com.zhjh.util.ExcelUtil;
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
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lanyuan 2014-11-19
 * @Email: mmm333zzz520@163.com
 * @version 3.0v
 */
@Controller
@RequestMapping("/materialbackorder/")
public class MaterialBackOrderController extends BaseController {
	@Inject
	private GoodMapper goodMapper;

	@Inject
	private BlankMapper blankMapper;

	@Inject
	private BlankSizeMapper blankSizeMapper;

	@Inject
	private WorkersubmitMapper workersubmitMapper;

	@Inject
	private SystemconfigMapper systemconfigMapper;

	@Inject
	private MaterialBackOrderMapper materialBackOrderMapper;

	@Inject
	private MaterialQualityTypeMapper materialQualityTypeMapper;

	@Inject
	private MaterialBuyOrderMapper materialBuyOrderMapper;

	@Inject
	private MaterialBuyOrderDetailsMapper materialBuyOrderDetailsMapper;

	@Inject
	private MaterialBackOrderDetailsMapper materialBackOrderDetailsMapper;

	@Inject
	private MaterialBackOrderDetailsDetailsMapper materialBackOrderDetailsDetailsMapper;

	@Inject
	private MaterialweightMapper materialweightMapper;

	@Inject
	private MaterialMapper materialMapper;

	@Inject
	private BlankProcessMapper blankProcessMapper;

	@RequestMapping("list")
	public String listUI(Model model) throws Exception {
		model.addAttribute("res", findByRes());
		return Common.BACKGROUND_PATH + "/system/materialbackorder/list";
	}


	public class TreeMaterialBackOrderEntity {
		public String id;
		public String text;
		public String state;
//		public List<TreeMaterialBackOrderDetailsEntity> children;
	}


	public class TreeMaterialBackOrderDetailsEntity {
		public String id;
		public String text;
	}

	@ResponseBody
	@RequestMapping("findlist")
	public List findTechnologylist(String content) throws Exception {
		List<MaterialBackOrderFormMap> materialBackOrderFormMaps=materialBackOrderMapper.selectTimeSlot();
		List<TreeMaterialBackOrderEntity> treeClientEntities=new ArrayList<>();
		for(int i=0;i<materialBackOrderFormMaps.size();i++){
			MaterialBackOrderFormMap materialBackOrderFormMap=materialBackOrderFormMaps.get(i);
			String id=materialBackOrderFormMap.get("id")+"";
			String timeSlot=materialBackOrderFormMap.getStr("timeSlot");
			TreeMaterialBackOrderEntity treeClientEntity=new TreeMaterialBackOrderEntity();
			treeClientEntity.id=id;
			treeClientEntity.text=timeSlot;
			treeClientEntities.add(treeClientEntity);
		}
		return treeClientEntities;
	}



	@ResponseBody
	@RequestMapping("findByMaterialQualityAndMaterialbackorderId")
	public MaterialBackOrderDetailsFormMap findByMaterialQualityAndMaterialbackorderId(String materialbackorderId) throws Exception {

		MaterialBackOrderDetailsFormMap materialBackOrderDetailsFormMap = getFormMap(MaterialBackOrderDetailsFormMap.class);
		List<MaterialBackOrderDetailsFormMap> materialBackOrderDetailsFormMaps=materialBackOrderDetailsMapper.selectByMaterialbackorderId(materialbackorderId);
		materialBackOrderDetailsFormMap.set("rows",materialBackOrderDetailsFormMaps);
		return materialBackOrderDetailsFormMap;
	}

	@ResponseBody
	@RequestMapping("findByMaterialBackOrderDetailsId")
	public MaterialBackOrderDetailsDetailsFormMap findByMaterialBackOrderDetailsId(String materialBackOrderDetailsId) throws Exception {

		MaterialBackOrderDetailsDetailsFormMap materialBackOrderDetailsFormMap = getFormMap(MaterialBackOrderDetailsDetailsFormMap.class);
		List<MaterialBackOrderDetailsDetailsFormMap> materialBackOrderDetailsFormMaps=materialBackOrderDetailsDetailsMapper.selectByMaterialbackorderdetailsId(materialBackOrderDetailsId);
		materialBackOrderDetailsFormMap.set("rows",materialBackOrderDetailsFormMaps);
		return materialBackOrderDetailsFormMap;
	}

	@ResponseBody
	@RequestMapping("upload")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="物料管理",methods="回料单-上传Excel(支数)")//凡需要处理业务逻辑的.都需要记录操作日志
	public String upload(@RequestParam(value = "file", required = false) MultipartFile file,
						 HttpServletRequest request) throws Exception {
//		updateMaterialWeight();
		List<String> materialBackOrderIds=new ArrayList();
		List<String> materialBackOrderDetailsIds=new ArrayList();
		String errorMessage="";
		String  orderId="";
		MaterialBackOrderFormMap materialBackOrderFormMap=new MaterialBackOrderFormMap();
		if(file!=null){
			boolean isAdd=true;
			String nowtime=ToolCommon.getNowTime();
			UserFormMap userFormMap=getNowUserMessage();
			String userId=String.valueOf(userFormMap.getInt("id"));
			FileBean fileBean=ToolCommon.uploadExcelFile(file,request);
			Sheet sheet1= ExcelUtil.readRowsAndColumsSheet1(fileBean.getPath());
			String materialQualityName="";
			String outerCircle="";
			String materialBackOrderId="";
			String materialBuyOrderId="";
			String materialBuyOrderDetailsId="";
			String buyLength="";
			String lackAmount="";
			float length=0;
			double weight=0;
			int amount=0;
			String startTime="";
			String endTime=",";
			for(int i=1;i<=sheet1.getLastRowNum();i++){
				Row row = null;
				row = sheet1.getRow(i);
				int lastColNum=0;
				if(row!=null){
					lastColNum = row.getLastCellNum();
					MaterialBackOrderDetailsDetailsFormMap materialBackOrderDetailsDetailsFormMap=new MaterialBackOrderDetailsDetailsFormMap();
					for(int j=0;j<lastColNum;j++){
						Cell cell=row.getCell(j);
						if(cell!=null){
							String value=ExcelUtil.getCellValue(cell);
							if(value==null){
								value="";
							}
							if(j==0){
								if(value.equals("")){
									i=sheet1.getLastRowNum()+1;
									j=lastColNum;
									isAdd=false;
								}else if(value.equals("结束")){
									i=sheet1.getLastRowNum()+1;
									j=lastColNum;
									isAdd=false;
								}else{
									isAdd=true;
								}
							}
							value= ToolExcel.replaceBlank(value);
							if(isAdd){
								switch (j){
									case 8:
										String[] days=value.split("至");
										startTime=days[0];
										endTime=days[1];
										MaterialBuyOrderFormMap materialBuyOrderFormMap1=new MaterialBuyOrderFormMap();
										materialBuyOrderFormMap1.set("startTime",startTime);
										materialBuyOrderFormMap1.set("endTime",endTime);
										MaterialBuyOrderFormMap materialBuyOrderFormMap=materialBuyOrderMapper.selectByStartAndEndTime(materialBuyOrderFormMap1);
										if(materialBuyOrderFormMap==null){
											isAdd=false;
											j=lastColNum;
											errorMessage=errorMessage+"【"+value+"】时间段不存在;";
										}else{
											materialBuyOrderId=materialBuyOrderFormMap.get("id")+"";
											MaterialBackOrderFormMap materialBackOrderFormMap2=new MaterialBackOrderFormMap();
											materialBackOrderFormMap2.set("startTime",startTime);
											materialBackOrderFormMap2.set("endTime",endTime);
											materialBackOrderFormMap2=materialBackOrderMapper.selectByStartAndEndTime(materialBackOrderFormMap2);
											if(materialBackOrderFormMap2==null){
												materialBackOrderFormMap.set("startTime",startTime);
												materialBackOrderFormMap.set("endTime",endTime);
												materialBackOrderFormMap.remove("id");
												materialBackOrderMapper.addEntity(materialBackOrderFormMap);
												materialBackOrderId=materialBackOrderFormMap.get("id")+"";
												materialBackOrderIds.add(materialBackOrderId);
											}else{
												materialBackOrderId=materialBackOrderFormMap2.get("id")+"";
											}
											materialBackOrderDetailsDetailsFormMap.set("materialBackOrderId",materialBackOrderId);
										}
										MaterialBuyOrderDetailsFormMap materialBuyOrderDetailsFormMap1=new MaterialBuyOrderDetailsFormMap();
										materialBuyOrderDetailsFormMap1.set("outerCircle",outerCircle);
										materialBuyOrderDetailsFormMap1.set("materialQuality",materialQualityName);
										materialBuyOrderDetailsFormMap1.set("materialbuyorderId",materialBuyOrderId);
										MaterialBuyOrderDetailsFormMap materialBuyOrderDetailsFormMap=materialBuyOrderDetailsMapper.selectByMaterialQualityAndOuterCircleAndMaterialbuyorderId(materialBuyOrderDetailsFormMap1);
										if(outerCircle.equals("Ф100")){
											System.out.print("aaa");
										}
										if(materialBuyOrderDetailsFormMap==null){
											isAdd=false;
											j=lastColNum;
											errorMessage=errorMessage+"【"+startTime+"至"+endTime+"】时间段内,材质【"+materialQualityName+"】【"+outerCircle+"】不存在;";
										}else{
											String state=materialBuyOrderDetailsFormMap.get("state")+"";
											buyLength=state.replace("已订料:","");
											materialBuyOrderDetailsId=materialBuyOrderDetailsFormMap.get("id")+"";
											materialBackOrderDetailsDetailsFormMap.set("materialBuyOrderDetailsId",materialBuyOrderDetailsId);
										}
										break;
									case 0:
										materialBackOrderDetailsDetailsFormMap.set("arrivalTime",value);
										break;
									case 1:
										materialQualityName=value;
										break;
									case 2:
										if(i==61){
											System.out.print(value);
										}
										outerCircle=value.replace("Ф","");
										outerCircle=outerCircle.replace("Φ","");
										break;
									case 4:
										amount=ToolCommon.StringToInteger(value);
										materialBackOrderDetailsDetailsFormMap.set("amount",value);
										length=ToolCommon.StringToFloat(value)*6;
										materialBackOrderDetailsDetailsFormMap.set("length",length);
										break;
									case 5:
										weight=ToolCommon.StringToFloat(value);
										weight=ToolCommon.Double4(weight);
										materialBackOrderDetailsDetailsFormMap.set("weight",value);
										break;
									case 6:
										materialBackOrderDetailsDetailsFormMap.set("taxPrice",value);
										break;
									case 7:
										materialBackOrderDetailsDetailsFormMap.set("taxMoney",value);
										break;
								}
							}

						}
					}
					if (isAdd) {
						updateUploadMessage(materialQualityName,outerCircle,materialBuyOrderDetailsId,
								materialBackOrderId,lackAmount,length,materialBackOrderDetailsDetailsFormMap,
								amount,weight);
					}
				}
			}
		}
		if(errorMessage.equals("")){
			return "success";
		}else{
			return errorMessage;
		}
//		return "";
	}



	public double getWeightByLength(String outerCircle1,float length){
		String outside=outerCircle1.split("\\*")[0].replace("Φ","");
		String inside="0";
		if(outerCircle1.split("\\*").length==2){
			inside=outerCircle1.split("\\*")[1];
		}
		if(ToolCommon.isContain(inside,"Φ")){
			inside=inside.replace("Φ","");
		}
		float outsideFloat=ToolCommon.StringToFloat(outside);
		float insideFloat=ToolCommon.StringToFloat(inside);
		double weightD=outsideFloat*outsideFloat*length*(0.00617/1000)-insideFloat*insideFloat*length*(0.00617/1000);//本次余料重量
		return weightD;
	}

	@ResponseBody
	@RequestMapping("uploadMeters")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="物料管理",methods="回料单-上传Excel(米数)")//凡需要处理业务逻辑的.都需要记录操作日志
	public String uploadMeters(@RequestParam(value = "fileMeters", required = false) MultipartFile file,
							   HttpServletRequest request) throws Exception {
		List<String> materialBackOrderIds=new ArrayList();
		List<String> materialBackOrderDetailsIds=new ArrayList();
		String errorMessage="";
		String  orderId="";
		MaterialBackOrderFormMap materialBackOrderFormMap=new MaterialBackOrderFormMap();
		if(file!=null){
			boolean isAdd=true;
			String nowtime=ToolCommon.getNowTime();
			UserFormMap userFormMap=getNowUserMessage();
			String userId=String.valueOf(userFormMap.getInt("id"));
			FileBean fileBean=ToolCommon.uploadExcelFile(file,request);
			Sheet sheet1= ExcelUtil.readRowsAndColumsSheet1(fileBean.getPath());
			String materialQualityName="";
			String outerCircle="";
			String materialBackOrderId="";
			String materialBuyOrderId="";
			String materialBuyOrderDetailsId="";
			String buyLength="";
			String lackAmount="";
			float length=0;
			double weight=0;
			int amount=0;
			String startTime="";
			String endTime=",";
			for(int i=1;i<=sheet1.getLastRowNum();i++){
				Row row = null;
				row = sheet1.getRow(i);
				int lastColNum=0;
				if(row!=null){
					lastColNum = row.getLastCellNum();
					MaterialBackOrderDetailsDetailsFormMap materialBackOrderDetailsDetailsFormMap=new MaterialBackOrderDetailsDetailsFormMap();
					for(int j=0;j<lastColNum;j++){
						Cell cell=row.getCell(j);
						if(cell!=null){
							String value=ExcelUtil.getCellValue(cell);
							if(value==null){
								value="";
							}
							if(j==0){
								if(value.equals("")){
									i=sheet1.getLastRowNum()+1;
									j=lastColNum;
									isAdd=false;
								}else if(value.equals("结束")){
									i=sheet1.getLastRowNum()+1;
									j=lastColNum;
									isAdd=false;
								}else{
									isAdd=true;
								}
							}
							value= ToolExcel.replaceBlank(value);
							if(isAdd){
								switch (j){
									case 8:
										String[] days=value.split("至");
										startTime=days[0];
										endTime=days[1];
										MaterialBuyOrderFormMap materialBuyOrderFormMap1=new MaterialBuyOrderFormMap();
										materialBuyOrderFormMap1.set("startTime",startTime);
										materialBuyOrderFormMap1.set("endTime",endTime);
										MaterialBuyOrderFormMap materialBuyOrderFormMap=materialBuyOrderMapper.selectByStartAndEndTime(materialBuyOrderFormMap1);
										if(materialBuyOrderFormMap==null){
											isAdd=false;
											j=lastColNum;
											errorMessage=errorMessage+"【"+value+"】时间段不存在;";
										}else{
											materialBuyOrderId=materialBuyOrderFormMap.get("id")+"";
											MaterialBackOrderFormMap materialBackOrderFormMap2=new MaterialBackOrderFormMap();
											materialBackOrderFormMap2.set("startTime",startTime);
											materialBackOrderFormMap2.set("endTime",endTime);
											materialBackOrderFormMap2=materialBackOrderMapper.selectByStartAndEndTime(materialBackOrderFormMap2);
											if(materialBackOrderFormMap2==null){
												materialBackOrderFormMap.set("startTime",startTime);
												materialBackOrderFormMap.set("endTime",endTime);
												materialBackOrderFormMap.remove("id");
												materialBackOrderMapper.addEntity(materialBackOrderFormMap);
												materialBackOrderId=materialBackOrderFormMap.get("id")+"";
												materialBackOrderIds.add(materialBackOrderId);
											}else{
												materialBackOrderId=materialBackOrderFormMap2.get("id")+"";
											}
											materialBackOrderDetailsDetailsFormMap.set("materialBackOrderId",materialBackOrderId);
										}
										MaterialBuyOrderDetailsFormMap materialBuyOrderDetailsFormMap1=new MaterialBuyOrderDetailsFormMap();
										materialBuyOrderDetailsFormMap1.set("outerCircle",outerCircle);
										materialBuyOrderDetailsFormMap1.set("materialQuality",materialQualityName);
										materialBuyOrderDetailsFormMap1.set("materialbuyorderId",materialBuyOrderId);
										MaterialBuyOrderDetailsFormMap materialBuyOrderDetailsFormMap=materialBuyOrderDetailsMapper.selectByMaterialQualityAndOuterCircleAndMaterialbuyorderId(materialBuyOrderDetailsFormMap1);
										if(materialBuyOrderDetailsFormMap==null){
											isAdd=false;
											j=lastColNum;
											errorMessage=errorMessage+"【"+startTime+"至"+endTime+"】时间段内,材质【"+materialQualityName+"】【"+outerCircle+"】不存在;";
										}else{
											String state=materialBuyOrderDetailsFormMap.get("state")+"";
											buyLength=state.replace("已订料:","");
											materialBuyOrderDetailsId=materialBuyOrderDetailsFormMap.get("id")+"";
											materialBackOrderDetailsDetailsFormMap.set("materialBuyOrderDetailsId",materialBuyOrderDetailsId);
										}
										break;
									case 0:
										materialBackOrderDetailsDetailsFormMap.set("arrivalTime",value);
										break;
									case 1:
										materialQualityName=value;
										break;
									case 2:
										outerCircle=value.replace("Ф","");
										outerCircle=outerCircle.replace("Φ","");
										break;
									case 4:
										amount=0;
										materialBackOrderDetailsDetailsFormMap.set("amount",amount);
										value=value.replace("米","");
										length=ToolCommon.StringToFloat(value);
										materialBackOrderDetailsDetailsFormMap.set("length",length);
										break;
									case 5:
										weight=ToolCommon.StringToFloat(value);
										weight=ToolCommon.Double4(weight);
										materialBackOrderDetailsDetailsFormMap.set("weight",value);
										break;
									case 6:
										materialBackOrderDetailsDetailsFormMap.set("taxPrice",value);
										break;
									case 7:
										materialBackOrderDetailsDetailsFormMap.set("taxMoney",value);
										break;
								}
							}

						}else{
							i=sheet1.getLastRowNum()+1;
							j=lastColNum;
							isAdd=false;
						}
					}
					if(isAdd){
						updateUploadMessage(materialQualityName,outerCircle,materialBuyOrderDetailsId,
								materialBackOrderId,lackAmount,length,materialBackOrderDetailsDetailsFormMap,
								amount,weight);
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

	public void updateUploadMessage(String materialQualityName,String outerCircle,
									String materialBuyOrderDetailsId,
									String materialBackOrderId,
									String lackAmount,
									float length,
									MaterialBackOrderDetailsDetailsFormMap materialBackOrderDetailsDetailsFormMap,
									int amount,
									double weight){
		try{
			MaterialBackOrderDetailsFormMap materialBackOrderDetailsFormMap1=new MaterialBackOrderDetailsFormMap();
			materialBackOrderDetailsFormMap1.set("materialQuality",materialQualityName);
			materialBackOrderDetailsFormMap1.set("outerCircle",outerCircle);
			materialBackOrderDetailsFormMap1.set("materialBuyOrderDetailsId",materialBuyOrderDetailsId);
			materialBackOrderDetailsFormMap1.set("materialBackOrderId",materialBackOrderId);
			MaterialBackOrderDetailsFormMap materialBackOrderDetailsFormMaps2=materialBackOrderDetailsMapper.selectByMaterialQualityAndAndouterCircleMaterialBuyOrderIdAndMaterialBackOrderId(materialBackOrderDetailsFormMap1);
			String materialBackOrderDetailsId="";
			if(materialBackOrderDetailsFormMaps2!=null){//已存在订料信息
				//已订料米数
				String alBuyLength=materialBackOrderDetailsFormMaps2.getStr("alBuyLength");//已订料数量
				lackAmount=materialBackOrderDetailsFormMaps2.get("lackAmount")+"";//已余料米数
				float lackAmountF=ToolCommon.StringToFloat(lackAmount);//已余料米数
				String length2=materialBackOrderDetailsFormMaps2.getStr("length");//已回料米数
				float length2F=ToolCommon.StringToFloat(length2);//已回料米数
				float nowLengthF=length2F+length;//已回料米数+当前回料米数
				String amount2=materialBackOrderDetailsFormMaps2.getStr("amount");//已回料支数
				String weight2=materialBackOrderDetailsFormMaps2.getStr("weight");//已回料重量
				materialBackOrderDetailsFormMaps2.set("length",nowLengthF);
				materialBackOrderDetailsFormMaps2.set("amount",ToolCommon.StringToFloat(amount2)+amount);
				materialBackOrderDetailsFormMaps2.set("weight",ToolCommon.Double4(ToolCommon.StringToFloat(weight2)+weight));
				float lackAmount3=0;
				if(lackAmountF>0){//已经存在余料
					lackAmount3=lackAmountF+length;//总余料=已经存在余料+本次米数
					//更新库存 长度为length
					lackAmount=length+"";
					MaterialweightFormMap materialweightFormMap=new MaterialweightFormMap();
					materialweightFormMap.set("materialQuality",materialQualityName);
					materialweightFormMap.set("outerCircle",outerCircle);
					MaterialweightFormMap materialweightFormMap1=materialweightMapper.findByMaterialQualityAndOuterCircle(materialweightFormMap);
					String outerCircle1="0";
					String materialId="";
					String taxPrice="";
					float taxPriceF=0;
					if(materialweightFormMap1!=null){
						outerCircle1=materialweightFormMap1.get("outerCircle")+"";
						taxPrice=materialweightFormMap1.get("taxPrice")+"";
					}else{
						MaterialFormMap materialFormMap=new MaterialFormMap();
						materialFormMap.set("materialQuality",materialQualityName);
						materialFormMap.set("outerCircle",outerCircle);
						MaterialFormMap materialFormMap1=materialMapper.findByMaterialQualityAndOuterCircle(materialFormMap);
						if(materialFormMap1!=null){
							materialId=materialFormMap1.get("id")+"";
							taxPrice=materialFormMap1.get("taxPrice")+"";
						}else{
							taxPrice=materialBackOrderDetailsDetailsFormMap.get("taxPrice")+"";
							materialFormMap.set("taxPrice",taxPrice);
							materialMapper.addEntity(materialFormMap);
							materialId=materialFormMap.get("id")+"";
						}
						outerCircle1=outerCircle;
					}

					double weightD=getWeightByLength(outerCircle1,length);//本次余料重量
					taxPriceF=ToolCommon.StringToFloat(taxPrice);
					double money=ToolCommon.FloatToMoney(taxPriceF)*weightD;//本次余料金额
					money=ToolCommon.Double2(money);
					if(materialweightFormMap1!=null) {
						String lengthStock=materialweightFormMap1.get("length")+"";//当前库存米数
						float lengthStockD=ToolCommon.StringToFloat(lengthStock);
						float lengthStockNowD=lengthStockD+length;
						materialweightFormMap1.set("length",ToolCommon.Double4(lengthStockNowD));
						double weightStockNowD=getWeightByLength(outerCircle1,lengthStockNowD);//当前库存重量
						taxPriceF=ToolCommon.StringToFloat(taxPrice);
						money=ToolCommon.FloatToMoney(taxPriceF)*weightStockNowD;//当前库存金额金额
						materialweightFormMap1.set("weight",ToolCommon.Double4(weightStockNowD));
						materialweightFormMap1.set("money",ToolCommon.Double2(money));
						materialweightMapper.editEntity(materialweightFormMap1);
					}else{
						materialweightFormMap1=new MaterialweightFormMap();
						materialweightFormMap1.set("materialId",materialId);
						materialweightFormMap1.set("length",ToolCommon.Double4(length));
						materialweightFormMap1.set("weight",ToolCommon.Double4(weightD));
						materialweightFormMap1.set("money",money);
						materialweightMapper.addEntity(materialweightFormMap1);
					}

				}else{//不存在余料
					String id=materialBackOrderDetailsFormMaps2.get("id")+"";
					MaterialBackOrderDetailsFormMap materialBackOrderDetailsFormMapS=materialBackOrderDetailsMapper.findById(id);
					lackAmount=materialBackOrderDetailsFormMapS.get("lackAmount")+"";
					lackAmountF=ToolCommon.StringToFloat(lackAmount);
					if(lackAmountF==0){
						lackAmount3=length-ToolCommon.StringToFloat(alBuyLength);//未订料 总余料=本次回料-已订料
					}else{
						lackAmount3=lackAmountF+length;//总余料=已经存在余料（负值）+本次米数
					}

					if(lackAmount3>0){
						lackAmount=lackAmount3+"";
						//更新库存 余料长度为lackAmount3
						MaterialweightFormMap materialweightFormMap=new MaterialweightFormMap();
						materialweightFormMap.set("materialQuality",materialQualityName);
						materialweightFormMap.set("outerCircle",outerCircle);
						MaterialweightFormMap materialweightFormMap1=materialweightMapper.findByMaterialQualityAndOuterCircle(materialweightFormMap);;
						String outerCircle1="0";
						String materialId="";
						String taxPrice="";
						float taxPriceF=0;
						if(materialweightFormMap1!=null){
							outerCircle1=materialweightFormMap1.get("outerCircle")+"";
							taxPrice=materialweightFormMap1.get("taxPrice")+"";
						}else{
							MaterialFormMap materialFormMap=new MaterialFormMap();
							materialFormMap.set("materialQuality",materialQualityName);
							materialFormMap.set("outerCircle",outerCircle);
							MaterialFormMap materialFormMap1=materialMapper.findByMaterialQualityAndOuterCircle(materialFormMap);
							if(materialFormMap1!=null){
								materialId=materialFormMap1.get("id")+"";
								taxPrice=materialFormMap1.get("taxPrice")+"";
							}else{
								taxPrice=materialBackOrderDetailsDetailsFormMap.get("taxPrice")+"";
								materialFormMap.set("taxPrice",taxPrice);
								materialMapper.addEntity(materialFormMap);
								materialId=materialFormMap.get("id")+"";
							}
							outerCircle1=outerCircle;
						}

						double weightD=getWeightByLength(outerCircle1,lackAmount3);//本次余料重量
						taxPriceF=ToolCommon.StringToFloat(taxPrice);
						double money=ToolCommon.FloatToMoney(taxPriceF)*weightD;//本次余料金额
						money=ToolCommon.Double2(money);
						if(materialweightFormMap1!=null) {
							String lengthStock=materialweightFormMap1.get("length")+"";//当前库存米数
							float lengthStockD=ToolCommon.StringToFloat(lengthStock);
							float lengthStockNowD=lengthStockD+lackAmount3;
							materialweightFormMap1.set("length",ToolCommon.Double4(lengthStockNowD));
							double weightStockNowD=getWeightByLength(outerCircle1,lengthStockNowD);//当前库存重量
							taxPriceF=ToolCommon.StringToFloat(taxPrice);
							money=ToolCommon.FloatToMoney(taxPriceF)*weightStockNowD;//当前库存金额金额
							materialweightFormMap1.set("weight",ToolCommon.Double4(weightStockNowD));
							materialweightFormMap1.set("money",ToolCommon.Double2(money));
							materialweightMapper.editEntity(materialweightFormMap1);
						}else{
							materialweightFormMap1=new MaterialweightFormMap();
							materialweightFormMap1.set("materialId",materialId);
							materialweightFormMap1.set("length",lackAmount);
							materialweightFormMap1.set("weight",ToolCommon.Double4(weightD));
							materialweightFormMap1.set("money",money);
							materialweightMapper.addEntity(materialweightFormMap1);
						}
					}

				}
				lackAmount3=ToolCommon.FloatTo4Float(lackAmount3);
				String lengthNow=materialBackOrderDetailsFormMaps2.get("length")+"";//现回料米数
				String buyLengthNow=materialBackOrderDetailsFormMaps2.get("buyLength")+"";//现订料米数
				double lackAmountNowF=ToolCommon.StringToDouble(lengthNow)-ToolCommon.StringToDouble(buyLengthNow);
				materialBackOrderDetailsFormMaps2.set("lackAmount",ToolCommon.Double4(lackAmountNowF)+"");
				materialBackOrderDetailsMapper.editEntity(materialBackOrderDetailsFormMaps2);
				String id=materialBackOrderDetailsFormMaps2.get("id")+"";
				materialBackOrderDetailsMapper.updateLackAmountById(id);
			}else{//不存在订料
				MaterialBuyOrderDetailsFormMap materialBuyOrderDetailsFormMap=materialBuyOrderDetailsMapper.findById(materialBuyOrderDetailsId);
				String buyState=materialBuyOrderDetailsFormMap.get("state")+"";
				float alBuyLength=0;
				buyState=materialBuyOrderDetailsFormMap.get("state")+"";
				alBuyLength=ToolCommon.StringToFloat(buyState.replace("已订料:",""));
				float lackAmountF=length-alBuyLength;
				lackAmountF=ToolCommon.FloatTo4Float(lackAmountF);
				materialBackOrderDetailsFormMaps2=new MaterialBackOrderDetailsFormMap();
				materialBackOrderDetailsFormMaps2.set("length",length);
				materialBackOrderDetailsFormMaps2.set("amount",amount);
				materialBackOrderDetailsFormMaps2.set("weight",weight);
				materialBackOrderDetailsFormMaps2.set("materialQuality",materialQualityName);
				materialBackOrderDetailsFormMaps2.set("lackAmount",lackAmountF);
				materialBackOrderDetailsFormMaps2.set("materialBackOrderId",materialBackOrderId);
				materialBackOrderDetailsFormMaps2.set("materialBuyOrderDetailsId",materialBuyOrderDetailsId);
				materialBackOrderDetailsMapper.addEntity(materialBackOrderDetailsFormMaps2);
				String id=materialBackOrderDetailsFormMaps2.get("id")+"";
				if(ToolCommon.isContain(buyState,"已订料")){
					materialBackOrderDetailsMapper.updateLackAmountById(id);
				}
				MaterialBackOrderDetailsFormMap materialBackOrderDetailsFormMapS=materialBackOrderDetailsMapper.findById(id);
				lackAmount=materialBackOrderDetailsFormMapS.get("lackAmount")+"";
				if(lackAmountF>0){//存在余料
					MaterialweightFormMap materialweightFormMap=new MaterialweightFormMap();
					materialweightFormMap.set("materialQuality",materialQualityName);
					materialweightFormMap.set("outerCircle",outerCircle);
					MaterialweightFormMap materialweightFormMap1=materialweightMapper.findByMaterialQualityAndOuterCircle(materialweightFormMap);;
					String outerCircle1="0";
					String materialId="";
					String taxPrice="";
					float taxPriceF=0;
					if(materialweightFormMap1!=null){//物料有库存
						outerCircle1=materialweightFormMap1.get("outerCircle")+"";
						taxPrice=materialweightFormMap1.get("taxPrice")+"";
					}else{
						MaterialFormMap materialFormMap=new MaterialFormMap();
						materialFormMap.set("materialQuality",materialQualityName);
						materialFormMap.set("outerCircle",outerCircle);
						MaterialFormMap materialFormMap1=materialMapper.findByMaterialQualityAndOuterCircle(materialFormMap);
						if(materialFormMap1!=null){
							materialId=materialFormMap1.get("id")+"";
							taxPrice=materialFormMap1.get("taxPrice")+"";
						}else{
							taxPrice=materialBackOrderDetailsDetailsFormMap.get("taxPrice")+"";
							materialFormMap.set("taxPrice",taxPrice);
							materialMapper.addEntity(materialFormMap);
							materialId=materialFormMap.get("id")+"";
						}
						outerCircle1=outerCircle;
					}

					double nowweightD=getWeightByLength(outerCircle1,lackAmountF);//本次余料重量
					String lengthStr=lackAmountF+"";//本次余料米数
					taxPriceF=ToolCommon.StringToFloat(taxPrice);
					double money=ToolCommon.FloatToMoney(taxPriceF)*nowweightD;
					money=ToolCommon.Double2(money);
					if(materialweightFormMap1!=null) {
						String lengthStock=materialweightFormMap1.get("length")+"";//当前库存米数
						float lengthStockD=ToolCommon.StringToFloat(lengthStock);
						float lengthStockNowD=lengthStockD+lackAmountF;
						materialweightFormMap1.set("length",ToolCommon.Double4(lengthStockNowD));
						double weightStockNowD=getWeightByLength(outerCircle1,lengthStockNowD);//当前库存重量
						taxPriceF=ToolCommon.StringToFloat(taxPrice);
						money=ToolCommon.FloatToMoney(taxPriceF)*weightStockNowD;//当前库存金额金额
						materialweightFormMap1.set("weight",ToolCommon.Double4(weightStockNowD));
						materialweightFormMap1.set("money",ToolCommon.Double2(money));
						materialweightMapper.editEntity(materialweightFormMap1);
					}else{
						materialweightFormMap1=new MaterialweightFormMap();
						materialweightFormMap1.set("materialId",materialId);
						materialweightFormMap1.set("length",lengthStr);
						materialweightFormMap1.set("weight",ToolCommon.Double4(nowweightD));
						materialweightFormMap1.set("money",money);
						materialweightMapper.addEntity(materialweightFormMap1);
					}
				}
			}
			materialBackOrderDetailsId=materialBackOrderDetailsFormMaps2.get("id")+"";
			materialBackOrderDetailsDetailsFormMap.set("materialBackOrderDetailsId",materialBackOrderDetailsId);
			materialBackOrderDetailsDetailsFormMap.set("lackAmount",lackAmount);
			materialBackOrderDetailsDetailsFormMap.remove("id");
			materialBackOrderDetailsDetailsMapper.addEntity(materialBackOrderDetailsDetailsFormMap);
		}catch (Exception e){

		}
	}

	public void updateMaterialWeight(){
		try{
			List<MaterialBackOrderDetailsFormMap> materialBackOrderDetailsFormMaps=materialBackOrderDetailsMapper.selectReduceLengthMessageByMaterialBackOrderId("55");
			for(int i=0;i<materialBackOrderDetailsFormMaps.size();i++){
				MaterialBackOrderDetailsFormMap materialBackOrderDetailsFormMap=materialBackOrderDetailsFormMaps.get(i);
				String materialId=materialBackOrderDetailsFormMap.get("materialId")+"";
				String outerCircle1=materialBackOrderDetailsFormMap.get("outerCircle")+"";
				String materialQuality=materialBackOrderDetailsFormMap.get("materialQuality")+"";
				if(outerCircle1.equals("28")&&materialQuality.equals("20CrMnTiH")){
					System.out.print("aaa");
				}
				MaterialweightFormMap materialweightFormMap1=materialweightMapper.findByMaterialId(materialId);
				String taxPrice=materialweightFormMap1.get("taxPrice")+"";
				String lengthStr=materialBackOrderDetailsFormMap.get("length")+"";
				float length=-ToolCommon.StringToFloat(lengthStr);
				String lengthStock=materialweightFormMap1.get("length")+"";//当前库存米数
				float lengthStockD=ToolCommon.StringToFloat(lengthStock);
				float lengthStockNowD=lengthStockD+length;
				materialweightFormMap1.set("length",ToolCommon.Double4(lengthStockNowD));
				double weightStockNowD=getWeightByLength(outerCircle1,lengthStockNowD);//当前库存重量
				float taxPriceF=ToolCommon.StringToFloat(taxPrice);
				double money=ToolCommon.FloatToMoney(taxPriceF)*weightStockNowD;//当前库存金额金额
				materialweightFormMap1.set("weight",ToolCommon.Double4(weightStockNowD));
				materialweightFormMap1.set("money",ToolCommon.Double2(money));
				materialweightMapper.editEntity(materialweightFormMap1);
			}
		}catch (Exception e){

		}

	}

	@ResponseBody
	@RequestMapping("deleteDetailsEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="物料管理",methods="回料单-回料明细删除")//凡需要处理业务逻辑的.都需要记录操作日志
	public String deleteDetailsEntity(String materialBackOrderId) throws Exception {
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
				materialBackOrderDetailsDetailsMapper.findByAttribute("materialBackOrderDetailsId",id,MaterialBackOrderDetailsDetailsFormMap.class);
				materialBackOrderDetailsMapper.deleteByAttribute("id",id,MaterialBackOrderDetailsFormMap.class);
			}
			List<MaterialBackOrderDetailsFormMap> materialBackOrderDetailsFormMaps=materialBackOrderDetailsMapper.selectByMaterialbackorderId(materialBackOrderId);
			if(materialBackOrderDetailsFormMaps.size()==0){
				materialBackOrderMapper.deleteByAttribute("id",materialBackOrderId,MaterialBackOrderFormMap.class);
			}
			return "success";
		}else{
			return "删除订单密码错误";
		}

	}

	@RequestMapping("downOrderDemo")
	public void downOrderDemo(Model model,String url,HttpServletRequest request,HttpServletResponse response) throws Exception {
		String path = request.getRealPath("");//当前运行文件在服务器上的绝对路径.
//		url=url.replace("/","\\");
		String fileUrl=path+url;
		System.out.print(fileUrl);
		File file=new File(fileUrl);
		String name=file.getName();
		ToolCommon.exportFile(file,name,response);
	}
	@ResponseBody
	@RequestMapping("editEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="组织架构",methods="员工管理-新增")//凡需要处理业务逻辑的.都需要记录操作日志
	public String editEntity(String entity) throws Exception {
		String rows = ToolCommon.json2Object(entity).getString("rows");
		List<MaterialBackOrderDetailsFormMap> list = (List) ToolCommon.json2ObjectList(rows, MaterialBackOrderDetailsFormMap.class);
		for(int i=0;i<list.size();i++){
			MaterialBackOrderDetailsFormMap blankProcessFormMap=list.get(i);
			materialBackOrderDetailsMapper.editEntity(blankProcessFormMap);
			String stockWeight=blankProcessFormMap.get("stockWeight")+"";

			if(stockWeight!=null&&!stockWeight.equals("")){
				String outerCircle=blankProcessFormMap.get("outerCircle")+"";
				String materialQuality=blankProcessFormMap.get("materialQuality")+"";
				MaterialweightFormMap materialweightFormMap=new MaterialweightFormMap();
				materialweightFormMap.set("materialQuality",materialQuality);
				materialweightFormMap.set("outerCircle",outerCircle);
				MaterialweightFormMap materialweightFormMapResult=materialweightMapper.findByMaterialQualityAndOuterCircle(materialweightFormMap);
				if(materialweightFormMapResult!=null){
					materialweightFormMapResult.set("weight",stockWeight);
					materialweightMapper.editEntity(materialweightFormMapResult);
				}
			}
		}
		return "success";
	}


	@ResponseBody
	@RequestMapping("sureBackEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="系统管理",methods="组管理-删除组")//凡需要处理业务逻辑的.都需要记录操作日志
	public String sureBackEntity() throws Exception {
		String ids = getPara("ids");
		ids=ids.substring(0,ids.length()-1);
		String[] idsStr=ids.split(",");
		for(int i=0;i<idsStr.length;i++){
			String id=idsStr[i];
			MaterialBackOrderDetailsFormMap materialBackOrderDetailsFormMap=materialBackOrderDetailsMapper.findById(id);
			materialBackOrderDetailsFormMap.set("state","已订料");
			materialBackOrderDetailsMapper.editEntity(materialBackOrderDetailsFormMap);
		}
		return "success";
	}

}