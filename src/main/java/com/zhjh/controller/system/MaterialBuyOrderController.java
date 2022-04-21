package com.zhjh.controller.system;

import com.zhjh.annotation.SystemLog;
import com.zhjh.bean.ComboboxEntity;
import com.zhjh.controller.index.BaseController;
import com.zhjh.entity.*;
import com.zhjh.mapper.*;
import com.zhjh.tool.ToolCommon;
import com.zhjh.util.Common;
import com.zhjh.util.QrCodeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 *
 * @author lanyuan 2014-11-19
 * @Email: mmm333zzz520@163.com
 * @version 3.0v
 */
@Controller
@RequestMapping("/materialbuyorder/")
public class MaterialBuyOrderController extends BaseController {
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
	private MaterialBuyOrderMapper materialBuyOrderMapper;

	@Inject
	private MaterialBuyOrderDetailsMapper materialBuyOrderDetailsMapper;

	@Inject
	private MaterialBuyExcelMapper materialBuyExcelMapper;
	@Inject
	private MaterialBuyExcelTimeMapper materialBuyExcelTimeMapper;

	@Inject
	private ClientMapper clientMapper;

	@Inject
	private OrderDetailsMapper orderDetailsMapper;

	@Inject
	private MaterialweightMapper materialweightMapper;
	@Inject
	private MaterialbuyorderdetailsBlankMapper materialbuyorderdetailsBlankMapper;

	@Inject
	private BlankProcessMapper blankProcessMapper;

	@RequestMapping("list")
	public String listUI(Model model) throws Exception {
		model.addAttribute("res", findByRes());
		return Common.BACKGROUND_PATH + "/system/materialbuyorder/list";
	}


	public class TreeMaterialBuyOrderEntity {
		public String id;
		public String text;
		public String state;
//		public List<TreeMaterialBuyOrderDetailsEntity> children;
	}


	public class TreeMaterialBuyOrderDetailsEntity {
		public String id;
		public String text;
	}

	@ResponseBody
	@RequestMapping("findlist")
	public List findTechnologylist(String content) throws Exception {
		List<MaterialBuyOrderFormMap> materialBuyOrderFormMaps=materialBuyOrderMapper.selectTimeSlot();
		List<TreeMaterialBuyOrderEntity> treeClientEntities=new ArrayList<>();
		for(int i=0;i<materialBuyOrderFormMaps.size();i++){
			MaterialBuyOrderFormMap materialBuyOrderFormMap=materialBuyOrderFormMaps.get(i);
			String id=materialBuyOrderFormMap.get("id")+"";
			String timeSlot=materialBuyOrderFormMap.getStr("timeSlot");
			TreeMaterialBuyOrderEntity treeClientEntity=new TreeMaterialBuyOrderEntity();
			treeClientEntity.id=id;
			treeClientEntity.text=timeSlot;
//			treeClientEntity.state="closed";
//			List<TreeMaterialBuyOrderDetailsEntity> treeOrderFormMaps=new ArrayList<>();
//			List<MaterialBuyOrderDetailsFormMap> materialBuyOrderDetailsFormMaps=materialBuyOrderDetailsMapper.selectMaterialQualityBymaterialBuyOrderId(id);
//			for(int j=0;j<materialBuyOrderDetailsFormMaps.size();j++){
//				MaterialBuyOrderDetailsFormMap orderFormMap=materialBuyOrderDetailsFormMaps.get(j);
//				String materialQuality=orderFormMap.get("materialQuality")+"";
//				String materialBuyOrderId=orderFormMap.getStr("materialBuyOrderId");
//				TreeMaterialBuyOrderDetailsEntity treeOrderFormMap=new TreeMaterialBuyOrderDetailsEntity();
//				treeOrderFormMap.id=materialQuality+"订料单id"+materialBuyOrderId;
//				treeOrderFormMap.text=materialQuality;
//				treeOrderFormMaps.add(treeOrderFormMap);
//			}
//			treeClientEntity.children=treeOrderFormMaps;
			treeClientEntities.add(treeClientEntity);
		}
		return treeClientEntities;
	}

	@ResponseBody
	@RequestMapping("findblankBymaterialBuyOrderDetailsId")
	public MaterialbuyorderdetailsBlankFormMap findblankBymaterialBuyOrderDetailsId(String materialBuyOrderDetailsId,
																			   String clientId,
																			   String starttime,
																			   String endtime) throws Exception {
		MaterialbuyorderdetailsBlankFormMap materialBuyOrderDetailsFormMap=new MaterialbuyorderdetailsBlankFormMap();
		if(clientId==null||clientId.equals("")){
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
		materialBuyOrderDetailsFormMap.set("clientId",clientId);
		materialBuyOrderDetailsFormMap.set("startTime",starttime);
		endtime=ToolCommon.addDay(endtime,1);
		materialBuyOrderDetailsFormMap.set("endTime",endtime);
		materialBuyOrderDetailsFormMap.set("materialBuyOrderDetailsId",materialBuyOrderDetailsId);
		List<MaterialbuyorderdetailsBlankFormMap> materialBuyOrderDetailsFormMaps=materialbuyorderdetailsBlankMapper.findBankByMaterialBuyOrderDetailsId(materialBuyOrderDetailsFormMap);
		materialBuyOrderDetailsFormMap.set("rows",materialBuyOrderDetailsFormMaps);
		return materialBuyOrderDetailsFormMap;
	}
	@ResponseBody
	@RequestMapping("materialQualitySelect")
	public List<ComboboxEntity>  materialQualitySelect() throws Exception {
		List<MaterialQualityTypeFormMap> materialQualityTypeFormMaps=materialBuyOrderDetailsMapper.selectDistinctMaterialQuality();
		List<ComboboxEntity> comboboxEntityList=new ArrayList<>();
		for(int i=0;i<materialQualityTypeFormMaps.size();i++){
			MaterialQualityTypeFormMap roleFormMap1=materialQualityTypeFormMaps.get(i);
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
	@RequestMapping("findBlankLengthWeightBymaterialBuyOrderDetailsId")
	public MaterialbuyorderdetailsBlankFormMap findBlankLengthWeightBymaterialBuyOrderDetailsId(String materialBuyOrderDetailsId,
																					String clientId,
																					String starttime,
																					String endtime) throws Exception {
		MaterialbuyorderdetailsBlankFormMap materialBuyOrderDetailsFormMap=new MaterialbuyorderdetailsBlankFormMap();
		materialBuyOrderDetailsFormMap.set("clientId",clientId);
		materialBuyOrderDetailsFormMap.set("startTime",starttime);
		endtime=ToolCommon.addDay(endtime,1);
		materialBuyOrderDetailsFormMap.set("endTime",endtime);
		materialBuyOrderDetailsFormMap.set("materialBuyOrderDetailsId",materialBuyOrderDetailsId);
		MaterialbuyorderdetailsBlankFormMap materialbuyorderdetailsBlankFormMap=materialbuyorderdetailsBlankMapper.findBankLengthAndWeightByMaterialBuyOrderDetailsId(materialBuyOrderDetailsFormMap);
		return materialbuyorderdetailsBlankFormMap;
	}

	@ResponseBody
	@RequestMapping("findByMaterialQualityAndMaterialbuyorderId")
	@SystemLog(module="物料管理",methods="订料单-订料明细列表")
	public MaterialBuyOrderDetailsFormMap findByMaterialQualityAndMaterialbuyorderId(String materialbuyorderId,
																					 String materialQualityName,
																					 String isLack,
																					 String clientId,
																					 String starttime,
																					 String endtime) throws Exception {
		OrderDetailsFormMap orderDetailsFormMap=new OrderDetailsFormMap();
		orderDetailsFormMap.set("startTime",starttime);
		if(!ToolCommon.isNull(endtime)){
			endtime=ToolCommon.addDay(endtime,1);
		}
		orderDetailsFormMap.set("endTime",endtime);
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
		orderDetailsFormMap.set("clientId",clientId);
		String orderdetailsId=orderDetailsMapper.getOrderDetailsIdsByClientIdAndDeliveryTime(orderDetailsFormMap);
		MaterialBuyOrderDetailsFormMap materialBuyOrderDetailsFormMap=new MaterialBuyOrderDetailsFormMap();
		materialBuyOrderDetailsFormMap.set("clientId",clientId);
		materialBuyOrderDetailsFormMap.set("blankIds",orderdetailsId);
		materialBuyOrderDetailsFormMap.set("isLack",isLack);
		materialBuyOrderDetailsFormMap.set("materialQualityName",materialQualityName);
		materialBuyOrderDetailsFormMap.set("materialbuyorderId",materialbuyorderId);
		materialBuyOrderDetailsFormMap.set("startTime",starttime);
		materialBuyOrderDetailsFormMap.set("endTime",endtime);
		List<MaterialBuyOrderDetailsFormMap> materialBuyOrderDetailsFormMaps=materialBuyOrderDetailsMapper.selectByMaterialbuyorderIdAndSearchAndBlankIds(materialBuyOrderDetailsFormMap);
		String materialQualityPrevious="";
		String statePrevious="";
		float alreadyLengthF=0;
		float weightF=0;
		float lengthF=0;
		float buyLengthF=0;
		float buyWeightF=0;
		float moneyF=0;
		float allmoneyF=0;
		float backAmountF=0;
		float lackAmountF=0;
		List<MaterialBuyOrderDetailsFormMap> materialBuyOrderDetailsFormMapsSum=new ArrayList<>();
		for(int i=0;i<materialBuyOrderDetailsFormMaps.size();i++){
			MaterialBuyOrderDetailsFormMap materialBuyOrderDetailsFormMap1=materialBuyOrderDetailsFormMaps.get(i);

//			MaterialbuyorderdetailsBlankFormMap materialBuyOrderDetailsFormMap2=new MaterialbuyorderdetailsBlankFormMap();
//
//			materialBuyOrderDetailsFormMap2.set("clientId",clientId);
//			materialBuyOrderDetailsFormMap2.set("startTime",starttime);
//			materialBuyOrderDetailsFormMap2.set("endTime",endtime);
//			materialBuyOrderDetailsFormMap2.set("materialBuyOrderDetailsId",materialBuyOrderDetailsFormMap1.get("id")+"");
//			List<MaterialbuyorderdetailsBlankFormMap> materialBuyOrderDetailsFormMaps=materialbuyorderdetailsBlankMapper.findBankByMaterialBuyOrderDetailsId(materialBuyOrderDetailsFormMap2);


			float weight=ToolCommon.StringToFloat(materialBuyOrderDetailsFormMap1.get("weight")+"");
			float length=ToolCommon.StringToFloat(materialBuyOrderDetailsFormMap1.get("length")+"");
			float buyWeight=ToolCommon.StringToFloat(materialBuyOrderDetailsFormMap1.get("buyWeight")+"");
			float buyLength=ToolCommon.StringToFloat(materialBuyOrderDetailsFormMap1.get("buyLength")+"");
			float taxPrice=ToolCommon.StringToFloat(materialBuyOrderDetailsFormMap1.get("taxPrice")+"");
			float backAmount=ToolCommon.StringToFloat(materialBuyOrderDetailsFormMap1.get("backAmount")+"");
			float lackAmount=ToolCommon.StringToFloat(materialBuyOrderDetailsFormMap1.get("lackAmount")+"");
			float money=taxPrice*buyWeight;
			float allmoney=taxPrice*weight;
			String materialQuality=materialBuyOrderDetailsFormMap1.get("materialQuality")+"";
			String state=materialBuyOrderDetailsFormMap1.get("state")+"";
			float stateF=0;
			if(ToolCommon.isContain(state,"已订料:")){
				state=state.replace("已订料:","");
				stateF=ToolCommon.StringToFloat(state);
			}
			if(i==0){
				materialQualityPrevious=materialQuality;
				statePrevious=state;
			}
			if(materialQuality.equals(materialQualityPrevious)){
				alreadyLengthF=alreadyLengthF+stateF;
				lengthF=lengthF+length;
				weightF=weightF+weight;
				buyLengthF=buyLengthF+buyLength;
				buyWeightF=buyWeightF+buyWeight;
				moneyF=moneyF+money;
				allmoneyF=allmoneyF+allmoney;
				lackAmountF=lackAmountF+lackAmount;
				backAmountF=backAmountF+backAmount;
			}else{
				if(lengthF!=0){
					moneyF=ToolCommon.FloatToMoney(moneyF);
					allmoneyF=ToolCommon.FloatToMoney(allmoneyF);
					lengthF=ToolCommon.FloatTo4Float(lengthF);
					weightF=ToolCommon.FloatTo4Float(weightF);
					buyLengthF=ToolCommon.FloatTo4Float(buyLengthF);
					buyWeightF=ToolCommon.FloatTo4Float(buyWeightF);
					backAmountF=ToolCommon.FloatTo4Float(backAmountF);
					lackAmountF=ToolCommon.FloatTo4Float(lackAmountF);
					MaterialBuyOrderDetailsFormMap materialBuyOrderDetailsFormMap2=new MaterialBuyOrderDetailsFormMap();
					materialBuyOrderDetailsFormMap2.set("materialQuality",materialQualityPrevious);
					materialBuyOrderDetailsFormMap2.set("length",lengthF);
					materialBuyOrderDetailsFormMap2.set("weight",weightF);
					materialBuyOrderDetailsFormMap2.set("buyLength",buyLengthF);
					materialBuyOrderDetailsFormMap2.set("buyWeight",buyWeightF);
					materialBuyOrderDetailsFormMap2.set("buyMoney",moneyF);
					materialBuyOrderDetailsFormMap2.set("money",allmoneyF);
					materialBuyOrderDetailsFormMap2.set("backAmount",backAmountF);
					materialBuyOrderDetailsFormMap2.set("lackAmount",lackAmountF);
					String stateSum="";
					if(alreadyLengthF>0){
						stateSum="已订料"+alreadyLengthF;
					}else{
						stateSum="未订料";
					}
					materialBuyOrderDetailsFormMap2.set("state",stateSum);
					materialBuyOrderDetailsFormMapsSum.add(materialBuyOrderDetailsFormMap2);
					lengthF=0;
					weightF=0;
					buyLengthF=0;
					buyWeightF=0;
					moneyF=0;
					allmoneyF=0;
					alreadyLengthF=0;
					lackAmountF=0;
					backAmountF=0;
				}
				materialQualityPrevious=materialQuality;
				lengthF=lengthF+length;
				weightF=weightF+weight;
				buyLengthF=buyLengthF+buyLength;
				buyWeightF=buyWeightF+buyWeight;
				moneyF=moneyF+money;
				allmoneyF=allmoneyF+allmoney;
				alreadyLengthF=alreadyLengthF+stateF;
				lackAmountF=lackAmountF+lackAmount;
				backAmountF=backAmountF+backAmount;
			}
			if(i==materialBuyOrderDetailsFormMaps.size()-1){
				moneyF=ToolCommon.FloatToMoney(moneyF);
				allmoneyF=ToolCommon.FloatToMoney(allmoneyF);
				lengthF=ToolCommon.FloatTo4Float(lengthF);
				weightF=ToolCommon.FloatTo4Float(weightF);
				buyLengthF=ToolCommon.FloatTo4Float(buyLengthF);
				buyWeightF=ToolCommon.FloatTo4Float(buyWeightF);
				backAmountF=ToolCommon.FloatTo4Float(backAmountF);
				lackAmountF=ToolCommon.FloatTo4Float(lackAmountF);
				MaterialBuyOrderDetailsFormMap materialBuyOrderDetailsFormMap2=new MaterialBuyOrderDetailsFormMap();
				materialBuyOrderDetailsFormMap2.set("materialQuality",materialQualityPrevious);
				materialBuyOrderDetailsFormMap2.set("length",lengthF);
				materialBuyOrderDetailsFormMap2.set("weight",weightF);
				materialBuyOrderDetailsFormMap2.set("buyLength",buyLengthF);
				materialBuyOrderDetailsFormMap2.set("buyWeight",buyWeightF);
				materialBuyOrderDetailsFormMap2.set("buyMoney",moneyF);
				materialBuyOrderDetailsFormMap2.set("money",allmoneyF);
				materialBuyOrderDetailsFormMap2.set("backAmount",backAmountF);
				materialBuyOrderDetailsFormMap2.set("lackAmount",lackAmountF);
				String stateSum="";
				if(alreadyLengthF>0){
					stateSum="已订料"+alreadyLengthF;
				}else{
					stateSum="未订料";
				}
				materialBuyOrderDetailsFormMap2.set("state",stateSum);
				materialBuyOrderDetailsFormMapsSum.add(materialBuyOrderDetailsFormMap2);
			}
		}
		materialBuyOrderDetailsFormMap.set("rows",materialBuyOrderDetailsFormMaps);
		materialBuyOrderDetailsFormMap.set("rowsSum",materialBuyOrderDetailsFormMapsSum);
		return materialBuyOrderDetailsFormMap;
	}

	@ResponseBody
	@RequestMapping("deleteDetailsEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="物料管理",methods="订料单-订料明细删除")//凡需要处理业务逻辑的.都需要记录操作日志
	public String deleteDetailsEntity(String materialBuyOrderId) throws Exception {
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
				materialbuyorderdetailsBlankMapper.deleteByAttribute("materialBuyOrderDetailsId",id,MaterialbuyorderdetailsBlankFormMap.class);
				materialBuyOrderDetailsMapper.deleteByAttribute("id",id,MaterialBuyOrderDetailsFormMap.class);
			}
			List<MaterialBuyOrderDetailsFormMap> materialBuyOrderDetailsFormMaps=materialBuyOrderDetailsMapper.selectByMaterialbuyorderId(materialBuyOrderId);
			if(materialBuyOrderDetailsFormMaps.size()==0){
				materialBuyOrderMapper.deleteByAttribute("id",materialBuyOrderId,MaterialBuyOrderFormMap.class);
			}
			return "success";
		}else{
			return "删除订单密码错误";
		}

	}

	@ResponseBody
	@RequestMapping("editEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="组织架构",methods="员工管理-新增")//凡需要处理业务逻辑的.都需要记录操作日志
	public String editEntity(String entity) throws Exception {
		String rows = ToolCommon.json2Object(entity).getString("rows");
		List<MaterialBuyOrderDetailsFormMap> list = (List) ToolCommon.json2ObjectList(rows, MaterialBuyOrderDetailsFormMap.class);
		for(int i=0;i<list.size();i++){
			MaterialBuyOrderDetailsFormMap blankProcessFormMap=list.get(i);
			String length=blankProcessFormMap.get("lengthAll")+"";
			String weight=blankProcessFormMap.get("weightAll")+"";
			blankProcessFormMap.set("length",length);
			blankProcessFormMap.set("weight",weight);
			materialBuyOrderDetailsMapper.editEntity(blankProcessFormMap);
			String stockLength=blankProcessFormMap.get("stockLength")+"";

			if(stockLength!=null&&!stockLength.equals("")){
				String outerCircle=blankProcessFormMap.get("outerCircle")+"";
				String materialQuality=blankProcessFormMap.get("materialQuality")+"";
				MaterialweightFormMap materialweightFormMap=new MaterialweightFormMap();
				materialweightFormMap.set("materialQuality",materialQuality);
				materialweightFormMap.set("outerCircle",outerCircle);
				MaterialweightFormMap materialweightFormMapResult=materialweightMapper.findByMaterialQualityAndOuterCircle(materialweightFormMap);
				if(materialweightFormMapResult!=null){
					String taxPrice=materialweightFormMapResult.get("taxPrice")+"";
					float lengthF=ToolCommon.StringToFloat(stockLength);
					materialweightFormMapResult.set("length",lengthF);
					materialweightMapper.editEntity(materialweightFormMapResult);
				}
			}
		}
		return "success";
	}

	@ResponseBody
	@RequestMapping("buyMaterial")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="生产管理",methods="下料单-生成订料单")//凡需要处理业务逻辑的.都需要记录操作日志
	public String buyMaterial(String entity) throws Exception {
		String rows = ToolCommon.json2Object(entity).getString("rows");
		List<MaterialBuyOrderDetailsFormMap> list = (List) ToolCommon.json2ObjectList(rows, MaterialBuyOrderDetailsFormMap.class);
		for(int i=0;i<list.size();i++){
			MaterialBuyOrderDetailsFormMap blankProcessFormMap=list.get(i);
			String id=blankProcessFormMap.get("id")+"";
			String blankId=id;
			BlankFormMap blankFormMap=blankMapper.findById(id);
			blankFormMap.set("state","已订料");
			blankMapper.editEntity(blankFormMap);
			String length=blankProcessFormMap.get("length")+"";
			String weight=blankProcessFormMap.get("weight")+"";
			String deliveryTime=blankProcessFormMap.get("deliveryTime")+"";
			String materialQuality=blankProcessFormMap.get("materialQualityName")+"";
			materialQuality=materialQuality.replace(" ","");
			String blankSize=blankProcessFormMap.get("blankSize")+"";
			String outerCircle="";
			String inside="";
			if(blankSize!=null&&!blankSize.equals("null")&&!blankSize.equals("")){
				String outSize=blankSize.split("\\*")[0];
				inside=blankSize.split("\\*")[1];

				if(blankSize.split("\\*").length>2){
					outerCircle=outSize+"*"+inside;
					materialQuality=materialQuality+"管";
				}else{
					outerCircle=outSize;
				}
			}else{

			}

			MaterialBuyOrderFormMap materialBuyOrderFormMap=materialBuyOrderMapper.selectByDeliveryDay(deliveryTime);
			String materialbuyorderId="";
			if(materialBuyOrderFormMap==null){
				String startTime="";
				String endTime="";
					startTime=deliveryTime.substring(0,7)+"-01";
					endTime=deliveryTime.substring(0,7)+"-31";
				MaterialBuyOrderFormMap materialBuyOrderFormMap1=new MaterialBuyOrderFormMap();
				materialBuyOrderFormMap1.set("startTime",startTime);
				materialBuyOrderFormMap1.set("endTime",endTime);
				materialBuyOrderMapper.addEntity(materialBuyOrderFormMap1);
				materialbuyorderId=materialBuyOrderFormMap1.get("id")+"";
			}else{
				materialbuyorderId=materialBuyOrderFormMap.get("id")+"";
			}
			MaterialBuyOrderDetailsFormMap materialBuyOrderDetailsFormMap1=new MaterialBuyOrderDetailsFormMap();
			materialBuyOrderDetailsFormMap1.set("outerCircle",outerCircle);
			materialBuyOrderDetailsFormMap1.set("materialQuality",materialQuality);
			materialBuyOrderDetailsFormMap1.set("materialbuyorderId",materialbuyorderId);
			MaterialBuyOrderDetailsFormMap materialBuyOrderDetailsFormMap=materialBuyOrderDetailsMapper.selectByMaterialQualityAndOuterCircleAndMaterialbuyorderId(materialBuyOrderDetailsFormMap1);
			String materialBuyOrderDetailsId="";

			String buyWeight="";


			if(materialBuyOrderDetailsFormMap==null){
				materialBuyOrderDetailsFormMap1.set("length",length);
				if(weight.equals("")){
					materialBuyOrderDetailsFormMap1.set("weight","0");
				}else{
					materialBuyOrderDetailsFormMap1.set("weight",ToolCommon.Double4(Double.parseDouble(weight)));
				}
				materialBuyOrderDetailsFormMap1.set("buyLength",ToolCommon.Double4(Double.parseDouble(length)));
				float unbuyLengthF=ToolCommon.StringToFloat(length);
				String outside=outerCircle.split("\\*")[0].replace("Φ","");
				inside="0";
				if(outerCircle.split("\\*").length==2){
					inside=outerCircle.split("\\*")[1];
				}
				if(ToolCommon.isContain(inside,"Φ")){
					inside=inside.replace("Φ","");
				}
				float outsideFloat=ToolCommon.StringToFloat(outside);
				float insideFloat=ToolCommon.StringToFloat(inside);
				double  unWeight=outsideFloat*outsideFloat*unbuyLengthF*(0.00617/1000)-insideFloat*insideFloat*unbuyLengthF*(0.00617/1000);
				String unWeightStr=Common.formatDouble4(unWeight);
				materialBuyOrderDetailsFormMap1.set("buyWeight",ToolCommon.Double4(Double.parseDouble(unWeightStr)));
				materialBuyOrderDetailsFormMap1.remove("id");
				materialBuyOrderDetailsMapper.addEntity(materialBuyOrderDetailsFormMap1);
				materialBuyOrderDetailsId=materialBuyOrderDetailsFormMap1.get("id")+"";
			}else{
					float lengthF=ToolCommon.StringToFloat(materialBuyOrderDetailsFormMap.getStr("length"));
					float weightF=ToolCommon.StringToFloat(materialBuyOrderDetailsFormMap.getStr("weight"));
					lengthF=lengthF+ToolCommon.StringToFloat(length);
					weightF=weightF+ToolCommon.StringToFloat(weight);
					String state=materialBuyOrderDetailsFormMap.getStr("state");
					String buyedLength="0";
					if(ToolCommon.isContain(state,"已订料")){
						buyedLength=state.replace("已订料:","");
					}
					double buyLengthFloat=ToolCommon.StringToFloat(buyedLength);
					buyLengthFloat=ToolCommon.Double4(buyLengthFloat);
					double unbuyLength=lengthF-buyLengthFloat;
					String unbuyLengthStr=Common.formatDouble4(unbuyLength);
					materialBuyOrderDetailsFormMap.set("length",lengthF);
					materialBuyOrderDetailsFormMap.set("weight",ToolCommon.Double4(weightF));
					materialBuyOrderDetailsFormMap.set("buyLength",unbuyLengthStr);
				float unbuyLengthF=ToolCommon.StringToFloat(unbuyLengthStr);
				String outside=outerCircle.split("\\*")[0].replace("Φ","");
				inside="0";
				if(outerCircle.split("\\*").length==2){
					inside=outerCircle.split("\\*")[1];
				}
				if(ToolCommon.isContain(inside,"Φ")){
					inside=inside.replace("Φ","");
				}
				float outsideFloat=ToolCommon.StringToFloat(outside);
				float insideFloat=ToolCommon.StringToFloat(inside);
				double  unWeight=outsideFloat*outsideFloat*unbuyLengthF*(0.00617/1000)-insideFloat*insideFloat*unbuyLengthF*(0.00617/1000);
				String unWeightStr=Common.formatDouble4(unWeight);
				materialBuyOrderDetailsFormMap.set("buyWeight",unWeightStr);
					materialBuyOrderDetailsMapper.editEntity(materialBuyOrderDetailsFormMap);
					materialBuyOrderDetailsId=materialBuyOrderDetailsFormMap.get("id")+"";
			}
			MaterialbuyorderdetailsBlankFormMap materialbuyorderdetailsBlankFormMap=new MaterialbuyorderdetailsBlankFormMap();
			materialbuyorderdetailsBlankFormMap.set("blankId",blankId);
			materialbuyorderdetailsBlankFormMap.set("materialBuyOrderDetailsId",materialBuyOrderDetailsId);
			materialbuyorderdetailsBlankMapper.addEntity(materialbuyorderdetailsBlankFormMap);
		}
		return "success";
	}


	@ResponseBody
	@RequestMapping("sureBuyEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="物料管理",methods="订料管理-确认订料")//凡需要处理业务逻辑的.都需要记录操作日志
	public String sureBuyEntity() throws Exception {
			String ids = getPara("ids");
			ids=ids.substring(0,ids.length()-1);
			String[] idsStr=ids.split(",");
			for(int i=0;i<idsStr.length;i++){
				String id=idsStr[i];
				MaterialBuyOrderDetailsFormMap materialBuyOrderDetailsFormMap=materialBuyOrderDetailsMapper.findById(id);
				String buyWeight=materialBuyOrderDetailsFormMap.get("buyWeight")+"";
				String outerCircle=materialBuyOrderDetailsFormMap.get("outerCircle")+"";
				String buyLength=materialBuyOrderDetailsFormMap.get("buyLength")+"";
				String length=materialBuyOrderDetailsFormMap.get("length")+"";
				String state=materialBuyOrderDetailsFormMap.get("state")+"";
				String buyedLength="0";
				if(ToolCommon.isContain(state,"已订料")){
					buyedLength=state.replace("已订料:","");
				}
				double buyLengthFloat=ToolCommon.StringToFloat(buyedLength)+ToolCommon.StringToFloat(buyLength);
				buyLengthFloat=ToolCommon.Double4(buyLengthFloat);
				double unbuyLength=ToolCommon.StringToDouble(length)-buyLengthFloat;

				String outside=outerCircle.split("\\*")[0].replace("Φ","");
				String inside="0";
				if(outerCircle.split("\\*").length==2){
					inside=outerCircle.split("\\*")[1];
				}
				if(ToolCommon.isContain(inside,"Φ")){
					inside=inside.replace("Φ","");
				}
				float outsideFloat=ToolCommon.StringToFloat(outside);
				float insideFloat=ToolCommon.StringToFloat(inside);
				double  unWeight=outsideFloat*outsideFloat*unbuyLength*(0.00617/1000)-insideFloat*insideFloat*unbuyLength*(0.00617/1000);
				String unWeightStr=Common.formatDouble4(unWeight);
				String unbuyLengthStr=Common.formatDouble4(unbuyLength);
				materialBuyOrderDetailsFormMap.set("state","已订料:"+buyLengthFloat);
				materialBuyOrderDetailsFormMap.set("buyTime",ToolCommon.getNowTime());
				materialBuyOrderDetailsFormMap.set("buyLength",unbuyLengthStr);
				materialBuyOrderDetailsFormMap.set("buyWeight",unWeightStr);
				materialBuyOrderDetailsMapper.editEntity(materialBuyOrderDetailsFormMap);
			}
		return "success";
	}

	@ResponseBody
	@RequestMapping("sureBuy2Entity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="物料管理",methods="订料管理-生成到订料单2")//凡需要处理业务逻辑的.都需要记录操作日志
	public String sureBuy2Entity() throws Exception {
		String startTime = getPara("startTime");
		String endTime = getPara("endTime");
		if(!ToolCommon.isNull(endTime)){
			endTime=ToolCommon.addDay(endTime,1);
		}
		String clientId = getPara("clientId");
		OrderDetailsFormMap orderDetailsFormMap=new OrderDetailsFormMap();
		orderDetailsFormMap.set("startTime",startTime);
		orderDetailsFormMap.set("endTime",endTime);
		orderDetailsFormMap.set("clientId",clientId);
		String orderdetailsId=orderDetailsMapper.getOrderDetailsIdsByClientIdAndDeliveryTime(orderDetailsFormMap);
		MaterialBuyOrderDetailsFormMap materialBuyOrderDetailsFormMap=new MaterialBuyOrderDetailsFormMap();
		materialBuyOrderDetailsFormMap.set("blankIds",orderdetailsId);
		materialBuyOrderDetailsFormMap.set("clientId",clientId);
		materialBuyOrderDetailsFormMap.set("startTime",startTime);
		materialBuyOrderDetailsFormMap.set("endTime",endTime);
		List<MaterialBuyOrderDetailsFormMap> materialBuyOrderDetailsFormMaps=materialBuyOrderDetailsMapper.selectBuyExcelByMaterialbuyorderIdAndSearchAndBlankIds(materialBuyOrderDetailsFormMap);
		String remarks=startTime+"至"+ToolCommon.addDay(endTime,-1);
		boolean isFirst=true;
		for(int i=0;i<materialBuyOrderDetailsFormMaps.size();i++){
			if(i==0){
				isFirst=true;
			}else{
				isFirst=false;
			}
			addMaterialBuyExcel(materialBuyOrderDetailsFormMaps.get(i),remarks,clientId,isFirst);
		}
		return "success";
	}
	public void addMaterialBuyExcel(MaterialBuyOrderDetailsFormMap materialBuyOrderDetailsFormMap1,
									String remarks,String clientId,boolean isFirst
									){
		try {

			MaterialBuyExcelFormMap materialBuyExcelFormMap=new MaterialBuyExcelFormMap();
			String materialQuality=materialBuyOrderDetailsFormMap1.get("materialQuality")+"";
			String size=materialBuyOrderDetailsFormMap1.get("size")+"";
			String length=materialBuyOrderDetailsFormMap1.get("length")+"";
			String weight=materialBuyOrderDetailsFormMap1.get("weight")+"";
			materialBuyExcelFormMap.set("remarks",remarks);
			materialBuyExcelFormMap.set("size",size);
			materialBuyExcelFormMap.set("materialQuality",materialQuality);
			materialBuyExcelFormMap.set("length",length);
			materialBuyExcelFormMap.set("weight",weight);
			String isExist=materialBuyExcelMapper.findIsExistBySizeAndMaterialQuality(materialBuyExcelFormMap);
			if(isExist.equals("false")){//不存在 新增
				materialBuyExcelFormMap.set("lackLength",-ToolCommon.StringToFloat(materialBuyExcelFormMap.get("length")+""));
				materialBuyExcelFormMap.remove("id");
				materialBuyExcelMapper.addEntity(materialBuyExcelFormMap);
			}else{//存在 累加
				materialBuyExcelFormMap.set("id",isExist);
				materialBuyExcelMapper.updateAmountByEntity(materialBuyExcelFormMap);
			}
			MaterialBuyExcelTimeFormMap materialBuyExcelTimeFormMap=new MaterialBuyExcelTimeFormMap();
			materialBuyExcelTimeFormMap.set("time",remarks);
			List<MaterialBuyExcelTimeFormMap> materialBuyExcelTimeFormMaps=materialBuyExcelTimeMapper.findByNames(materialBuyExcelTimeFormMap);
			String clientIds="";
			if(isFirst){
				if(materialBuyExcelTimeFormMaps!=null&&materialBuyExcelTimeFormMaps.size()>0){
					MaterialBuyExcelTimeFormMap materialBuyExcelTimeFormMap1=materialBuyExcelTimeFormMaps.get(0);
					clientIds=materialBuyExcelTimeFormMap1.get("clientIds")+"";
					String clientNames=clientMapper.findSimpleNameByIds(clientId);
					clientIds=clientIds+","+clientNames;
					materialBuyExcelTimeFormMap1.set("clientIds",clientIds);
					materialBuyExcelTimeMapper.editEntity(materialBuyExcelTimeFormMap1);
				}else{
					String clientNames=clientMapper.findSimpleNameByIds(clientId);
					materialBuyExcelTimeFormMap.set("clientIds",clientNames);
					materialBuyExcelTimeMapper.addEntity(materialBuyExcelTimeFormMap);
				}
			}


		}catch (Exception e){

		}

	}
}