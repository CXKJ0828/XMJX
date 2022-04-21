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
@RequestMapping("/materialbackexcel/")
public class MaterialBackExcelController extends BaseController {


	@Inject
	private MaterialBackExcelMapper materialBackExcelMapper;

	@Inject
	private MaterialBackExcelDetailsMapper materialBackExcelDetailsMapper;

	@RequestMapping("list")
	public String listUI(Model model) throws Exception {
		model.addAttribute("res", findByRes());
		return Common.BACKGROUND_PATH + "/system/materialbackexcel/list";
	}
	@ResponseBody
	@RequestMapping("findlist")
	public List findTechnologylist() throws Exception {
		List<MaterialBackExcelFormMap> materialBackOrderFormMaps=materialBackExcelMapper.findDifferentRemarksTreeShow();
		return materialBackOrderFormMaps;
	}

	@ResponseBody
	@RequestMapping("findByRemarks")
	public MaterialBackExcelFormMap findByRemarks(String remarks,String state) throws Exception {
		MaterialBackExcelFormMap materialBackOrderDetailsFormMap =new MaterialBackExcelFormMap();
		List<MaterialBackExcelFormMap> materialBackOrderDetailsFormMaps=materialBackExcelMapper.findByRemarks(remarks,state);
		List<MaterialBackExcelFormMap> materialBuyOrderDetailsFormMapsSum=materialBackExcelMapper.findSumMaterialQualityByRemarks(remarks,state);
		materialBackOrderDetailsFormMap.set("rowsSum",materialBuyOrderDetailsFormMapsSum);
		materialBackOrderDetailsFormMap.set("rows",materialBackOrderDetailsFormMaps);
		return materialBackOrderDetailsFormMap;
	}

	@ResponseBody
	@RequestMapping("findByMaterialBackExcelId")
	public MaterialBackExcelDetailsFormMap findByMaterialBackExcelId(String materialbackexcelId) throws Exception {

		MaterialBackExcelDetailsFormMap materialBackExcelDetailsFormMap =new MaterialBackExcelDetailsFormMap();
		List<MaterialBackExcelDetailsFormMap> materialBackExcelDetailsFormMaps=materialBackExcelDetailsMapper.findByMaterialbackExcelId(materialbackexcelId);
		materialBackExcelDetailsFormMap.set("rows",materialBackExcelDetailsFormMaps);
		return materialBackExcelDetailsFormMap;
	}

	@RequestMapping("backUI")
	@SystemLog(module="物料管理",methods="回料单2-回料界面")
	public String backUI(Model model) {
		String id=getPara("id");
		model.addAttribute("id",id);
		return Common.BACKGROUND_PATH + "/system/materialbackexcel/back";
	}

	@ResponseBody
	@RequestMapping("backEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="物料管理",methods="回料单2-回料操作")//凡需要处理业务逻辑的.都需要记录操作日志
	public String backEntity() throws Exception {
		MaterialBackExcelDetailsFormMap materialBackExcelDetailsFormMap=getFormMap(MaterialBackExcelDetailsFormMap.class);
		materialBackExcelDetailsFormMap.set("time",ToolCommon.getNowTime());
		materialBackExcelDetailsFormMap.set("userId",getNowUserMessage().get("id")+"");
		materialBackExcelDetailsFormMap.remove("id");
		materialBackExcelDetailsMapper.addEntity(materialBackExcelDetailsFormMap);
		materialBackExcelMapper.updateAmountByBackEntity(materialBackExcelDetailsFormMap);
		return "success";

	}

	@ResponseBody
	@RequestMapping("deleteEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="物料管理",methods="回料单2-回料明细删除")//凡需要处理业务逻辑的.都需要记录操作日志
	public String deleteDetailsEntity() throws Exception {
		SystemconfigFormMap systemconfigFormMap=new SystemconfigFormMap();
		systemconfigFormMap.set("name","deletepassword");
		String content=getPara("password");
		systemconfigFormMap.set("content",content);
		List<SystemconfigFormMap> systemconfigFormMaps=systemconfigMapper.findByNames(systemconfigFormMap);
		if(systemconfigFormMaps.size()>0){
			String ids = getPara("ids");
			ids=ids.substring(0,ids.length()-1);
			materialBackExcelDetailsMapper.deleteByMaterialbackExcelIds(ids);
			materialBackExcelMapper.deleteByIds(ids);
			return "success";
		}else{
			return "删除订单密码错误";
		}
	}

	@ResponseBody
	@RequestMapping("upload")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="物料管理",methods="回料单2-上传Excel")//凡需要处理业务逻辑的.都需要记录操作日志
	public String upload(@RequestParam(value = "file", required = false) MultipartFile file,
						 HttpServletRequest request) throws Exception {
		String errorMessage="";
		if(file!=null){
			FileBean fileBean=ToolCommon.uploadExcelFile(file,request);
			Sheet sheet1= ExcelUtil.readRowsAndColumsSheet1(fileBean.getPath());
			for(int i=1;i<=sheet1.getLastRowNum();i++){
				Row row = null;
				row = sheet1.getRow(i);
				int lastColNum=0;
				MaterialBackExcelFormMap materialBackExcelFormMap=new MaterialBackExcelFormMap();
				if(row!=null){
					lastColNum = row.getLastCellNum();
					for(int j=0;j<lastColNum;j++){
						Cell cell=row.getCell(j);
						if(cell!=null){
							String value= ExcelUtil.getCellValue(cell);
							if(value==null){
								value="";
							}
							if(j==0){
								if(value.equals("")){
									j=lastColNum+1;
									i=sheet1.getLastRowNum()+1;
								}else if(value.equals("结束")){
									i=sheet1.getLastRowNum()+1;
									j=lastColNum+1;
								}
							}
							value= ToolExcel.replaceBlank(value);
							switch (j){
								case 0:
									value=value.replace(" ","");
									materialBackExcelFormMap.set("materialQuality",value);
									break;
								case 1:
									value=value.replace(" ","");
									value=value.replace("Φ","");
									materialBackExcelFormMap.set("size",value);
									break;
								case 2:
									value=value.replace(" ","");
									value=value.replace(",","");
									if(ToolCommon.isNull(value)){
										value="0";
									}
									materialBackExcelFormMap.set("length",value);
									break;
								case 3:
									value=value.replace(" ","");
									value=value.replace(",","");
									if(ToolCommon.isNull(value)){
										value="0";
									}
									materialBackExcelFormMap.set("weight",value);
									break;
								case 4:
									value=value.replace(" ","");
									materialBackExcelFormMap.set("remarks",value);
									String isExist=materialBackExcelMapper.findIsExistByEntity(materialBackExcelFormMap);
									if(isExist.equals("false")){//不存在 新增
										materialBackExcelFormMap.set("lackLength",-ToolCommon.StringToFloat(materialBackExcelFormMap.get("length")+""));
										materialBackExcelFormMap.remove("id");
										materialBackExcelMapper.addEntity(materialBackExcelFormMap);
									}else{//存在 累加
										materialBackExcelFormMap.set("id",isExist);
										materialBackExcelMapper.updateAmountByEntity(materialBackExcelFormMap);
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
	@RequestMapping("uploadBack")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="物料管理",methods="回料单2-回料上传Excel")//凡需要处理业务逻辑的.都需要记录操作日志
	public String uploadBack(@RequestParam(value = "fileBack", required = false) MultipartFile fileBack,
						 HttpServletRequest request) throws Exception {
		String errorMessage="";
		if(fileBack!=null){
			FileBean fileBean=ToolCommon.uploadExcelFile(fileBack,request);
			Sheet sheet1= ExcelUtil.readRowsAndColumsSheet1(fileBean.getPath());
			for(int i=1;i<=sheet1.getLastRowNum();i++){
				Row row = null;
				row = sheet1.getRow(i);
				int lastColNum=0;
				MaterialBackExcelFormMap materialBackExcelFormMap=new MaterialBackExcelFormMap();
				if(row!=null){
					lastColNum = row.getLastCellNum();
					for(int j=0;j<lastColNum;j++){
						Cell cell=row.getCell(j);
						if(cell!=null){
							String value= ExcelUtil.getCellValue(cell);
							if(value==null){
								value="";
							}
							if(j==0){
								if(value.equals("")){
									j=lastColNum+1;
									i=sheet1.getLastRowNum()+1;
								}else if(value.equals("结束")){
									i=sheet1.getLastRowNum()+1;
									j=lastColNum+1;
								}
							}
							value= ToolExcel.replaceBlank(value);
							switch (j){
								case 0:
									value=value.replace(" ","");
									materialBackExcelFormMap.set("materialQuality",value);
									break;
								case 1:
									value=value.replace(" ","");
									value=value.replace("Φ","");
									materialBackExcelFormMap.set("size",value);
									break;
								case 2:
									value=value.replace(" ","");
									value=value.replace(",","");
									if(ToolCommon.isNull(value)){
										value="0";
									}
									materialBackExcelFormMap.set("length",value);
									break;
								case 3:
									value=value.replace(" ","");
									value=value.replace(",","");
									if(ToolCommon.isNull(value)){
										value="0";
									}
									materialBackExcelFormMap.set("weight",value);
									break;
								case 4:
									value=value.replace(" ","");
									materialBackExcelFormMap.set("remarks",value);
									String isExist=materialBackExcelMapper.findIsExistByEntity(materialBackExcelFormMap);
									if(isExist.equals("false")){//不存在 新增
										errorMessage=errorMessage+value+":"+materialBackExcelFormMap.get("materialQuality")+"["+materialBackExcelFormMap.get("size")+"]不存在<br>";
									}else{//存在 累加
										MaterialBackExcelDetailsFormMap materialBackExcelDetailsFormMap=getFormMap(MaterialBackExcelDetailsFormMap.class);
										materialBackExcelDetailsFormMap.set("time",ToolCommon.getNowTime());
										materialBackExcelDetailsFormMap.set("userId",getNowUserMessage().get("id")+"");
										materialBackExcelDetailsFormMap.set("length",materialBackExcelFormMap.get("length")+"");
										materialBackExcelDetailsFormMap.set("weight",materialBackExcelFormMap.get("weight")+"");
										materialBackExcelDetailsFormMap.set("materialbackExcelId",isExist);
										materialBackExcelDetailsFormMap.remove("id");
										materialBackExcelDetailsMapper.addEntity(materialBackExcelDetailsFormMap);
										materialBackExcelMapper.updateAmountByBackEntity(materialBackExcelDetailsFormMap);
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


}