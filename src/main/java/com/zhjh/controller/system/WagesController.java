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
@RequestMapping("/wages/")
public class WagesController extends BaseController {
	@Inject
	private WagesMapper wagesMapper;


	@Inject
	private SystemconfigMapper systemConfigMapper;


	@RequestMapping("list")
	public String listUI(Model model) throws Exception {

		SystemconfigFormMap systemconfigFormMap=systemConfigMapper.findByAttribute("name","taxRate",SystemconfigFormMap.class).get(0);
		model.addAttribute("taxRate",systemconfigFormMap.get("content")+"");
		model.addAttribute("res", findByRes());
		return Common.BACKGROUND_PATH + "/system/wages/list";
	}


	@ResponseBody
	@RequestMapping("findByPage")
	@SystemLog(module="生产管理",methods="二类工资-获取二类工资列表")//凡需要处理业务逻辑的.都需要记录操作日志
	public WagesFormMap findByPage(String user,String content,String startTime,String endTime) throws Exception {
		String page=getPara("page");

		String rows=getPara("rows");
		WagesFormMap wagesFormMap =new WagesFormMap();
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
		endTime=ToolCommon.addDay(endTime,1);
		wagesFormMap.set("user",user);
		wagesFormMap.set("content",content);
		wagesFormMap.set("startTime",startTime);
		wagesFormMap.set("endTime",endTime);
		String wages=wagesMapper.findAllMoneyByStartTimeAndEndTimeAndContentAndUser(wagesFormMap);
		if(page.equals("1")){
			total=wagesMapper.findCountByAllLike(wagesFormMap);
		}
		wagesFormMap=toFormMap(wagesFormMap, page, rows,wagesFormMap.getStr("orderby"));
		List<WagesFormMap> departmentFormMapList=wagesMapper.findByAllLike(wagesFormMap);
		wagesFormMap.set("rows",departmentFormMapList);
		wagesFormMap.set("wages",wages);
		wagesFormMap.set("total",total);
		return wagesFormMap;
	}

	@ResponseBody
	@RequestMapping("findByAll")
	public WagesFormMap findByAll(String user,String content,String startTime,String endTime) throws Exception {
		WagesFormMap wagesFormMap =new WagesFormMap();
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
		endTime=ToolCommon.addDay(endTime,1);
		wagesFormMap.set("user",user);
		wagesFormMap.set("content",content);
		wagesFormMap.set("startTime",startTime);
		wagesFormMap.set("endTime",endTime);
		List<WagesFormMap> departmentFormMapList=wagesMapper.findByAllLike(wagesFormMap);
		wagesFormMap.set("rows",departmentFormMapList);
		wagesFormMap.set("total",total);
		return wagesFormMap;
	}



	@ResponseBody
	@RequestMapping("editEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="组织架构",methods="员工管理-新增")//凡需要处理业务逻辑的.都需要记录操作日志
	public String editEntity(String entity) throws Exception {
		List<WagesFormMap> list = (List) ToolCommon.json2ObjectList(entity, WagesFormMap.class);
		String nowtime=ToolCommon.getNowTime();
		for(int i=0;i<list.size();i++){
			WagesFormMap wagesFormMap=list.get(i);
			String id=wagesFormMap.get("id")+"";
			wagesFormMap.set("modifytime",nowtime);
			if(id==null||id.equals("")||id.equals("null")){
						wagesFormMap.remove("id");
						wagesMapper.addEntity(wagesFormMap);
			}else{
					wagesMapper.editEntity(wagesFormMap);
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
			wagesMapper.deleteById(id);
		}
		return "success";
	}


}