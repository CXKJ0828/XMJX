package com.zhjh.controller.system;

import com.zhjh.annotation.SystemLog;
import com.zhjh.bean.ComboboxEntity;
import com.zhjh.bean.FileBean;
import com.zhjh.controller.index.BaseController;
import com.zhjh.entity.ProcessFormMap;
import com.zhjh.entity.UserFormMap;
import com.zhjh.mapper.ProcessMapper;
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
@RequestMapping("/process/")
public class ProcessController extends BaseController {
	@Inject
	private ProcessMapper processMapper;

	public int total;
	@RequestMapping("list")
	public String listUI(Model model) throws Exception {
		model.addAttribute("res", findByRes());
		return Common.BACKGROUND_PATH + "/system/process/list";
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
	@RequestMapping("upload")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="Excel表格上传",methods="订单上传-上传")//凡需要处理业务逻辑的.都需要记录操作日志
	public String upload(@RequestParam(value = "file", required = false) MultipartFile file,
						 HttpServletRequest request) throws Exception {
		String nowtime=ToolCommon.getNowTime();
		UserFormMap userFormMap=getNowUserMessage();
		String userId=String.valueOf(userFormMap.getInt("id"));
		ProcessFormMap processFormMap=new ProcessFormMap();
		boolean isAdd=false;
		if(file!=null){
			FileBean fileBean=ToolCommon.uploadExcelFile(file,request);
			Sheet sheet= ExcelUtil.readRowsAndColumsSheet1(fileBean.getPath());
			int lastColNum=0;
			for(int i=1;i<=sheet.getLastRowNum();i++){
				Row row = null;
				row = sheet.getRow(i);
				if(row!=null){
					lastColNum = row.getLastCellNum();
					ProcessFormMap processFormMap1=new ProcessFormMap();
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
							if(j==3){
								System.out.print("aaa");
							}
							switch (j){
								case 0:
									List<ProcessFormMap> processFormMaps=processMapper.findByAttribute("name",value,ProcessFormMap.class);
									if(processFormMaps.size()>0){
										processMapper.deleteByAttribute("name",value,ProcessFormMap.class);
									}
									processFormMap1.set("name",value);
									break;
								case 1:processFormMap1.set("artificial",value);break;
								case 2:processFormMap1.set("remark",value);break;
								case 3:processFormMap1.set("isMust",value);break;
								case 4:processFormMap1.set("multiple",value);break;
							}
						}

					}
					if(isAdd){
						String name=processFormMap1.get("name")+"";
						if(name!=null&&!name.equals("")&&!name.equals("null")){
							processFormMap1.set("modifyTime",nowtime);
							processFormMap1.set("userId",userId);
							processMapper.addEntity(processFormMap1);
						}else{
							i=sheet.getLastRowNum();
						}

					}
				}
			}
		}
		return "success";
	}

	@ResponseBody
	@RequestMapping("findByPage")
	public ProcessFormMap findByPage(String content) throws Exception {
		ProcessFormMap processFormMap = getFormMap(ProcessFormMap.class);
		String page=getPara("page");

		String rows=getPara("rows");
		if(content==null){
			content="";
		}
		processFormMap.set("content",content);
		if(page.equals("1")){
			total=processMapper.findCountByAllLike(processFormMap);
		}

		processFormMap=toFormMap(processFormMap, page, rows,processFormMap.getStr("orderby"));
		List<ProcessFormMap> departmentFormMapList=processMapper.findByAllLike(processFormMap);
		processFormMap.set("rows",departmentFormMapList);
		processFormMap.set("total",total);
		return processFormMap;
	}

	@ResponseBody
	@RequestMapping("processSelect")
	public ProcessFormMap processSelect(String q) throws Exception {
//		String page=getPara("page");
//
//		String rows=getPara("rows");
		ProcessFormMap  processFormMap  = getFormMap(ProcessFormMap .class);
		if(q==null){
			q="";
		}
		processFormMap.set("name",q);
//		if(page.equals("1")){
//			total=processMapper.findCountByNameLike(processFormMap);
//		}
//		processFormMap=toFormMap(processFormMap, page, rows,processFormMap.getStr("orderby"));
		List<ProcessFormMap> departmentFormMapList=processMapper.findByNameLike(processFormMap);
		ProcessFormMap bomFormMap=new ProcessFormMap();
		bomFormMap.set("rows",departmentFormMapList);
//		bomFormMap.set("total",total);
		return bomFormMap;
	}

	@ResponseBody
	@RequestMapping("processSelectTwoContent")
	public List<ComboboxEntity>  processSelectTwoContent(String q) throws Exception {
		if(q==null){
			q="";
		}
		ProcessFormMap processFormMap=new ProcessFormMap();
		processFormMap.set("content",q);
		List<ProcessFormMap> processFormMaps=processMapper.findByNameLike(processFormMap);
		List<ComboboxEntity> comboboxEntityList=new ArrayList<>();
		for(int i=0;i<processFormMaps.size();i++){
			ProcessFormMap roleFormMap1=processFormMaps.get(i);
			if(roleFormMap1!=null){
				ComboboxEntity entity=new ComboboxEntity();
				entity.id=roleFormMap1.get("id")+"";
				entity.text=roleFormMap1.getStr("name");
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
	@RequestMapping("allProcessByRoleIdSelect")
	public ProcessFormMap allProcessByRoleIdSelect(String roleId) throws Exception {
		ProcessFormMap processFormMap = getFormMap(ProcessFormMap.class);
		processFormMap.set("roleId",roleId);
		List<ProcessFormMap> departmentFormMapList=processMapper.findAllProcessByRoleId(processFormMap);
		processFormMap.set("rows",departmentFormMapList);
		return processFormMap;
	}




	@RequestMapping("addUI")
	public String addUI(Model model) throws Exception {
		return Common.BACKGROUND_PATH + "/system/process/add";
	}

	@RequestMapping("detailsUI")
	public String detailsUI(Model model,String id) throws Exception {
		ProcessFormMap processFormMap=new ProcessFormMap();
		if(id.equals("")){
		}else{
			processFormMap=processMapper.findbyFrist("id",id,ProcessFormMap.class);
		}

		model.addAttribute("process",processFormMap);
		return Common.BACKGROUND_PATH + "/system/process/details";
	}



	@ResponseBody
	@RequestMapping("addEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="组织架构",methods="员工管理-新增")//凡需要处理业务逻辑的.都需要记录操作日志
	public String addEntity(String entity) throws Exception {
		List<ProcessFormMap> list = (List) ToolCommon.json2ObjectList(entity, ProcessFormMap.class);
		String nowtime=ToolCommon.getNowTime();
		UserFormMap userFormMap=getNowUserMessage();
		String userId=String.valueOf(userFormMap.getInt("id"));
		for(int i=0;i<list.size();i++){
			ProcessFormMap processFormMap=list.get(i);
			String id=processFormMap.get("id")+"";
			processFormMap.set("modifyTime",nowtime);
			processFormMap.set("userId",userId);
			if(id.equals("null")){

			}else{
				if(id!=null&&!id.equals("")){
					processMapper.editEntity(processFormMap);
				}else{
					processFormMap.remove("id");
					processMapper.addEntity(processFormMap);
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
		processMapper.deleteByAttribute("id", ids, ProcessFormMap.class);
//		processMapper.deleteByAttribute("parentId", ids, ProcessFormMap.class);
		return "success";
	}

	@RequestMapping("editUI")
	public String editUI(Model model) throws Exception {
		String id = getPara("id");
		if(Common.isNotEmpty(id)){
			model.addAttribute("process", processMapper.findbyFrist("id", id, ProcessFormMap.class));
		}
		return Common.BACKGROUND_PATH + "/system/process/edit";
	}
	@ResponseBody
	@RequestMapping("getprocessById")
	public ProcessFormMap getprocessById(@RequestParam(required=true) String id) throws Exception {
		ProcessFormMap processFormMap=new ProcessFormMap();
		ProcessFormMap processFormMap1=processMapper.findbyFrist("id",id,ProcessFormMap.class);
		if(processFormMap1!=null){
			processFormMap.set("id","1");
			processFormMap.set("processId",processFormMap1.getStr("processId"));
		}else{
			processFormMap.set("id","0");
			processFormMap.set("result","所选员工不存在");
		}
		return processFormMap;
	}


	

}