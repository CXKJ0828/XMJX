package com.zhjh.controller.system;

import com.zhjh.annotation.SystemLog;
import com.zhjh.bean.FileBean;
import com.zhjh.controller.index.BaseController;
import com.zhjh.entity.MaterialFormMap;
import com.zhjh.entity.MaterialweightFormMap;
import com.zhjh.entity.UserFormMap;
import com.zhjh.mapper.MaterialMapper;
import com.zhjh.mapper.MaterialweightMapper;
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
@RequestMapping("/material/")
public class MaterialController extends BaseController {

	@Inject
	private MaterialMapper materialMapper;

	@Inject
	private MaterialweightMapper materialweightMapper;
	@RequestMapping("list")
	public String listUI(Model model) throws Exception {
		model.addAttribute("res", findByRes());
		return Common.BACKGROUND_PATH + "/system/material/list";
	}


	@ResponseBody
	@RequestMapping("findByPage")
	public MaterialFormMap findByPage(String content) throws Exception {
		String page=getPara("page");

		String rows=getPara("rows");
		MaterialFormMap materialFormMap = getFormMap(MaterialFormMap.class);
		if(content==null){
			content="";
		}
		materialFormMap.set("content",content);
		if(page.equals("1")){
			total=materialMapper.findCountByAllLike(materialFormMap);
		}
		materialFormMap=toFormMap(materialFormMap, page, rows,materialFormMap.getStr("orderby"));
		List<MaterialFormMap> departmentFormMapList=materialMapper.findByAllLike(materialFormMap);
		materialFormMap.set("rows",departmentFormMapList);
		materialFormMap.set("total",total);
		return materialFormMap;
	}

	@ResponseBody
	@RequestMapping("upload")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="物料管理",methods="物料信息-上传")//凡需要处理业务逻辑的.都需要记录操作日志
	public String upload(@RequestParam(value = "file", required = false) MultipartFile file,
						 HttpServletRequest request) throws Exception {
		String errorMessage="";
		if(file!=null){
			boolean isAdd=false;
			UserFormMap userFormMap=getNowUserMessage();
			FileBean fileBean=ToolCommon.uploadExcelFile(file,request);
			Sheet sheet1= ExcelUtil.readRowsAndColumsSheet1(fileBean.getPath());
			String materialQuality="";
			String outerCircle="";
			String materialId="";
			for(int i=1;i<=sheet1.getLastRowNum();i++){
				Row row = null;
				row = sheet1.getRow(i);
				int lastColNum=0;
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
									materialQuality=value;
									break;
								case 1:
									value=value.replace(" ","");
									outerCircle=value;
									MaterialFormMap materialFormMap=new MaterialFormMap();
									materialFormMap.set("outerCircle",outerCircle);
									materialFormMap.set("materialQuality",materialQuality);
									MaterialFormMap materialFormMap1=materialMapper.findByMaterialQualityAndOuterCircle(materialFormMap);
									if(materialFormMap1==null){
										errorMessage=errorMessage+"物料"+materialQuality+"["+outerCircle+"]不存在;<br>";
										j=lastColNum+1;
										isAdd=false;
									}else{
										materialId=materialFormMap1.get("id")+"";
									}
									break;
								case 2:
									value=value.replace(" ","");
									value=value.replace(",","");
									value=value.replace("，","");
									materialMapper.updateTaxPriceById(value,materialId);
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
	@RequestMapping("editEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="物料管理",methods="物料信息-修改")//凡需要处理业务逻辑的.都需要记录操作日志
	public String editEntity(String entity) throws Exception {
		List<MaterialFormMap> list = (List) ToolCommon.json2ObjectList(entity, MaterialFormMap.class);
		String nowtime=ToolCommon.getNowTime();
		UserFormMap userFormMap=getNowUserMessage();
		String userId=String.valueOf(userFormMap.getInt("id"));
		for(int i=0;i<list.size();i++){
			MaterialFormMap materialFormMap=list.get(i);
			String id=materialFormMap.get("id")+"";
			materialFormMap.set("modifytime",nowtime);
			materialFormMap.set("userId",userId);
			if(id==null||id.equals("")||id.equals("null")){
				materialFormMap.remove("id");
				materialMapper.addEntity(materialFormMap);
			}else{
				materialMapper.editEntity(materialFormMap);
				String taxPrice=materialFormMap.get("taxPrice")+"";
				float taxPriceF=ToolCommon.StringToFloat(taxPrice);
				String materialId=materialFormMap.get("id")+"";
				MaterialweightFormMap materialweightFormMap=materialweightMapper.findbyFrist("materialId",materialId,MaterialweightFormMap.class);
				if(materialweightFormMap!=null){
					String weight=materialweightFormMap.get("weight")+"";
					float weightF=ToolCommon.StringToFloat(weight);
					float money=ToolCommon.FloatToMoney(taxPriceF*weightF);
					materialweightFormMap.set("money",money);
					materialweightMapper.editEntity(materialweightFormMap);
				}

			}
		}
		return "success";
	}

	@ResponseBody
	@RequestMapping("deleteEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="系统管理",methods="组管理-删除组")//凡需要处理业务逻辑的.都需要记录操作日志
	public String deleteEntity() throws Exception {
		String ids = getPara("ids");
		ids=ids.substring(0,ids.length()-1);
		String[] idsStr=ids.split(",");
		for (String id : idsStr) {
			materialMapper.deleteById(id);
		}
		return "success";
	}

}