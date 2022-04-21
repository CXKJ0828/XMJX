package com.zhjh.controller.system;

import com.zhjh.annotation.SystemLog;
import com.zhjh.bean.FileBean;
import com.zhjh.controller.index.BaseController;
import com.zhjh.entity.*;
import com.zhjh.mapper.MaterialBackExcelMapper;
import com.zhjh.mapper.MaterialBuyExcelMapper;
import com.zhjh.mapper.MaterialBuyExcelTimeMapper;
import com.zhjh.mapper.MaterialQualityTypeMapper;
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
import java.util.List;

/**
 *
 * @author lanyuan 2014-11-19
 * @Email: mmm333zzz520@163.com
 * @version 3.0v
 */
@Controller
@RequestMapping("/materialbuyexcel/")
public class MaterialBuyExcelController extends BaseController {


	@Inject
	private MaterialBuyExcelMapper materialBuyExcelMapper;

	@Inject
	private MaterialQualityTypeMapper materialQualityTypeMapper;

	@Inject
	private MaterialBuyExcelTimeMapper materialBuyExcelTimeMapper;

	@Inject
	private MaterialBackExcelMapper materialBackExcelMapper;


	@RequestMapping("list")
	public String listUI(Model model) throws Exception {
		model.addAttribute("res", findByRes());
		return Common.BACKGROUND_PATH + "/system/materialbuyexcel/list";
	}
	@ResponseBody
	@RequestMapping("findlist")
	public List findlist(String materialQuality) throws Exception {
		List<MaterialBuyExcelTimeFormMap> materialBuyOrderFormMaps=materialBuyExcelTimeMapper.findByMaterialQuality(materialQuality);
		return materialBuyOrderFormMaps;
	}

	@ResponseBody
	@RequestMapping("findByMaterialQuality")
	public MaterialBuyExcelFormMap findByMaterialQuality(String materialQuality) throws Exception {
		MaterialBuyExcelFormMap materialBuyOrderDetailsFormMap =new MaterialBuyExcelFormMap();
		List<MaterialBuyExcelFormMap> materialBuyOrderDetailsFormMaps=materialBuyExcelMapper.findByMaterialQuality(materialQuality);
		List<MaterialBuyExcelFormMap> materialBuyOrderDetailsFormMapsSum=materialBuyExcelMapper.findSumByMaterialQuality(materialQuality);
		materialBuyOrderDetailsFormMap.set("rowsSum",materialBuyOrderDetailsFormMapsSum);
		materialBuyOrderDetailsFormMap.set("rows",materialBuyOrderDetailsFormMaps);
		return materialBuyOrderDetailsFormMap;
	}

	@ResponseBody
	@RequestMapping("editEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="物料管理",methods="订料单2-编辑")//凡需要处理业务逻辑的.都需要记录操作日志
	public String editEntity(String entity) throws Exception {
		List<MaterialBuyExcelFormMap> list = (List) ToolCommon.json2ObjectList(entity, MaterialBuyExcelFormMap.class);
		for(int i=0;i<list.size();i++){
			MaterialBuyExcelFormMap materialBuyExcelFormMap=list.get(i);
			String id=materialBuyExcelFormMap.get("id")+"";
			if(ToolCommon.isNull(id)){
				materialBuyExcelFormMap.remove("id");
				materialBuyExcelMapper.addEntity(materialBuyExcelFormMap);
			}else{
				materialBuyExcelMapper.editEntity(materialBuyExcelFormMap);
			}
		}
		return "success";
	}
	@ResponseBody
	@RequestMapping("deleteEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="物料管理",methods="订料单2-订料明细删除")//凡需要处理业务逻辑的.都需要记录操作日志
	public String deleteDetailsEntity() throws Exception {
		SystemconfigFormMap systemconfigFormMap=new SystemconfigFormMap();
		systemconfigFormMap.set("name","deletepassword");
		String content=getPara("password");
		systemconfigFormMap.set("content",content);
		List<SystemconfigFormMap> systemconfigFormMaps=systemconfigMapper.findByNames(systemconfigFormMap);
		if(systemconfigFormMaps.size()>0){
			String ids = getPara("ids");
			ids=ids.substring(0,ids.length()-1);
			materialBuyExcelTimeMapper.deleteByMaterialbuyexcelIds(ids);
			materialBuyExcelMapper.deleteByIds(ids);
			return "success";
		}else{
			return "删除订单密码错误";
		}
	}
	@ResponseBody
	@RequestMapping("deleteAllEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="物料管理",methods="订料单2-清空")//凡需要处理业务逻辑的.都需要记录操作日志
	public String deleteAllEntity() throws Exception {
		SystemconfigFormMap systemconfigFormMap=new SystemconfigFormMap();
		systemconfigFormMap.set("name","deletepassword");
		String content=getPara("password");
		systemconfigFormMap.set("content",content);
		List<SystemconfigFormMap> systemconfigFormMaps=systemconfigMapper.findByNames(systemconfigFormMap);
		if(systemconfigFormMaps.size()>0){
			materialBuyExcelMapper.deleteAll();
			materialBuyExcelTimeMapper.deleteAll();
			return "success";
		}else{
			return "删除订单密码错误";
		}
	}
	@ResponseBody
	@RequestMapping("back2Entity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="物料管理",methods="订料单2-生成回料单2")//凡需要处理业务逻辑的.都需要记录操作日志
	public String back2Entity() throws Exception {
		String materialQuality=getPara("materialQuality");
		String materialQualityName="";
		if(!ToolCommon.isNull(materialQuality)){
			MaterialQualityTypeFormMap materialQualityTypeFormMap=materialQualityTypeMapper.findbyFrist("id",materialQuality,MaterialQualityTypeFormMap.class);
			materialQualityName=materialQualityTypeFormMap.get("name")+"";
		}
		String remarks=getPara("remarks");
		List<MaterialBuyExcelFormMap> materialBuyExcelFormMaps=materialBuyExcelMapper.findByMaterialQuality(materialQualityName);
		for(int i=0;i<materialBuyExcelFormMaps.size();i++){
			MaterialBuyExcelFormMap materialBuyExcelFormMap=materialBuyExcelFormMaps.get(i);
			String materialQualityBuy=materialBuyExcelFormMap.get("materialQuality")+"";
			String size=materialBuyExcelFormMap.get("size")+"";
			String length=materialBuyExcelFormMap.get("length")+"";
			String weight=materialBuyExcelFormMap.get("weight")+"";
			MaterialBackExcelFormMap materialBackExcelFormMap=new MaterialBackExcelFormMap();
			materialBackExcelFormMap.set("materialQuality",materialQualityBuy);
			materialBackExcelFormMap.set("size",size);
			materialBackExcelFormMap.set("length",length);
			materialBackExcelFormMap.set("weight",weight);
			materialBackExcelFormMap.set("remarks",remarks);
			String isExist=materialBackExcelMapper.findIsExistByEntity(materialBackExcelFormMap);
			if(isExist.equals("false")){//不存在 新增
				materialBackExcelFormMap.set("lackLength",-ToolCommon.StringToFloat(materialBackExcelFormMap.get("length")+""));
				materialBackExcelFormMap.remove("id");
				materialBackExcelMapper.addEntity(materialBackExcelFormMap);
			}else{//存在 累加
				materialBackExcelFormMap.set("id",isExist);
				materialBackExcelMapper.updateAmountByEntity(materialBackExcelFormMap);
			}

		}
		return "success";

	}
}