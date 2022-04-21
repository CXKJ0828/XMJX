package com.zhjh.controller.system;

import com.zhjh.annotation.SystemLog;
import com.zhjh.bean.FileBean;
import com.zhjh.controller.index.BaseController;
import com.zhjh.entity.*;
import com.zhjh.mapper.PoMapper;
import com.zhjh.mapper.PodetailsMapper;
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
@RequestMapping("/po/")
public class PoController extends BaseController {
	@Inject
	private PoMapper poMapper;

	@Inject
	private PodetailsMapper podetailsMapper;




	@RequestMapping("list")
	public String listUI(Model model) throws Exception {
		model.addAttribute("res", findByRes());
		return Common.BACKGROUND_PATH + "/system/po/list";
	}

	@ResponseBody
	@RequestMapping("findByPage")
	public PoFormMap findByPage(String content) throws Exception {
		String page=getPara("page");

		String rows=getPara("rows");
		PoFormMap poFormMap = getFormMap(PoFormMap.class);
		if(content==null){
			content="";
		}
		poFormMap.set("content",content);
		if(page.equals("1")){
			total=poMapper.findCountByAllLike(poFormMap);
		}
		poFormMap=toFormMap(poFormMap, page, rows,poFormMap.getStr("orderby"));
		List<PoFormMap> departmentFormMapList=poMapper.findByAllLike(poFormMap);
		poFormMap.set("rows",departmentFormMapList);
		poFormMap.set("total",total);
		return poFormMap;
	}

	@ResponseBody
	@RequestMapping("findpoByPoId")
	public PodetailsFormMap findpoByPoId(String poId) throws Exception {
		String rows=getPara("rows");
		PodetailsFormMap podetailsFormMap = getFormMap(PodetailsFormMap.class);
		podetailsFormMap.set("poId",poId);
		List<PodetailsFormMap> departmentFormMapList=podetailsMapper.findByPoId(podetailsFormMap);
		podetailsFormMap.set("rows",departmentFormMapList);
		return podetailsFormMap;
	}

	@ResponseBody
	@RequestMapping("upload")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="Excel表格上传",methods="订单上传-上传")//凡需要处理业务逻辑的.都需要记录操作日志
	public String upload(@RequestParam(value = "file", required = false) MultipartFile file,
							 HttpServletRequest request) throws Exception {
		String nowtime=ToolCommon.getNowTime();
		UserFormMap userFormMap=getNowUserMessage();
		String userId=String.valueOf(userFormMap.getInt("id"));
		PoFormMap poFormMap=new PoFormMap();
		PodetailsFormMap podetailsFormMap=new PodetailsFormMap();
		boolean isAdd=false;
		String poId="0";
		if(file!=null){
			FileBean fileBean=ToolCommon.uploadExcelFile(file,request);
			Sheet sheet= ExcelUtil.readRowsAndColumsSheet1(fileBean.getPath());
			int lastColNum=0;
			for(int i=1;i<=sheet.getLastRowNum();i++){
				Row row = null;
				row = sheet.getRow(i);
				if(row!=null){
					lastColNum = row.getLastCellNum();
					PodetailsFormMap poFormMap1=new PodetailsFormMap();
					for(int j=0;j<lastColNum;j++){
						Cell cell=row.getCell(j);
						if(cell!=null){
							String value=ExcelUtil.getCellValue(cell);
							if(value==null){
								value="";
							}
							if(j==0){
								if(value.equals("")){
									isAdd=false;
									j=lastColNum;
								}else{
									isAdd=true;
								}
							}
							value= ToolExcel.replaceBlank(value);
							switch (j){
								case 0:
									List<PoFormMap> poFormMaps=poMapper.findByAttribute("poCode",value,PoFormMap.class);
									if(poFormMaps.size()>0){
										poId=poFormMaps.get(0).get("id")+"";
									}else{
										PoFormMap poFormMap2=new PoFormMap();
										poFormMap2.set("poCode",value);
										poFormMap2.set("modifyTime",nowtime);
										poFormMap2.set("userId",userId);
										poMapper.addEntity(poFormMap2);
										poId=poFormMap2.get("id")+"";
									}
									break;
								case 1:
									poFormMap1.clear();
									poFormMap1.set("poId",poId);
									poFormMap1.set("factoryNumber",value);
									break;
								case 2:poFormMap1.set("makeTime",value);break;
								case 3:poFormMap1.set("startPort",value);break;
								case 4:poFormMap1.set("endPort",value);break;
								case 5:poFormMap1.set("sailingTime",value);break;
								case 6:poFormMap1.set("cabinetType",value);break;
								case 7:poFormMap1.set("productFactory ",value);break;
								case 8:poFormMap1.set("cx",value);break;
								case 9:poFormMap1.set("amount",value);break;
							}
						}

					}
					if(isAdd){
						String code=poFormMap1.get("poId")+"";
						if(code!=null&&!code.equals("")&&!code.equals("null")){
							podetailsMapper.addEntity(poFormMap1);
						}else{
							i=sheet.getLastRowNum();
						}

					}
				}
			}
		}
		return "success";
	}



	@RequestMapping("addUI")
	public String addUI(Model model) throws Exception {
		return Common.BACKGROUND_PATH + "/system/po/add";
	}

	@ResponseBody
	@RequestMapping("addEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="组织架构",methods="员工管理-新增")//凡需要处理业务逻辑的.都需要记录操作日志
	public String addEntity(String entity) throws Exception {
		List<PoFormMap> list = (List) ToolCommon.json2ObjectList(entity, PoFormMap.class);
		String nowtime=ToolCommon.getNowTime();
		UserFormMap userFormMap=getNowUserMessage();
		String userId=String.valueOf(userFormMap.getInt("id"));
		for(int i=0;i<list.size();i++){
			PoFormMap poFormMap=list.get(i);
			String id=poFormMap.getStr("id");
			if(id!=null&&!id.equals("")){
				PoFormMap poFormMap1=poMapper.findbyFrist("id",id,PoFormMap.class);
				poFormMap.set("modifyTime",nowtime);
				poFormMap.set("userId",userId);
				if(poFormMap1==null){
					poMapper.addEntity(poFormMap);
				}else{
					poMapper.editEntity(poFormMap);
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
			poMapper.deleteByAttribute("id", ids, PoFormMap.class);
		return "success";
	}

	@RequestMapping("editUI")
	public String editUI(Model model) throws Exception {
		String id = getPara("id");
		if(Common.isNotEmpty(id)){
			model.addAttribute("po", poMapper.findbyFrist("id", id, PoFormMap.class));
		}
		return Common.BACKGROUND_PATH + "/system/po/edit";
	}

	@ResponseBody
	@RequestMapping("editEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="系统管理",methods="组管理-修改组")//凡需要处理业务逻辑的.都需要记录操作日志
	public String editEntity(String entity) throws Exception {
		String po=ToolCommon.json2Object(entity).getString("po");
		PoFormMap poFormMap=(PoFormMap)ToolCommon.json2Object(po,PoFormMap.class);
		String code=poFormMap.get("code")+"";
		if(poFormMap.get("id")==null||poFormMap.get("id").equals("")){
			poFormMap.remove("id");
			List<PoFormMap> poFormMaps=poMapper.findByAttribute("code",
					code,
					PoFormMap.class);
			if(poFormMaps.size()>0){
				return code+"物料编码已存在";
			}else{
				poMapper.addEntity(poFormMap);
				return "success:"+poFormMap.get("id")+"";
			}

		}else{
			poMapper.editEntity(poFormMap);
			return "success:"+poFormMap.get("id")+"";
		}
	}
	

}