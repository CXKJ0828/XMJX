package com.zhjh.controller.system;

import com.zhjh.annotation.SystemLog;
import com.zhjh.controller.index.BaseController;
import com.zhjh.entity.WorkoverwageFormMap;
import com.zhjh.entity.SystemconfigFormMap;
import com.zhjh.mapper.WorkoverwageMapper;
import com.zhjh.mapper.SystemconfigMapper;
import com.zhjh.tool.ToolCommon;
import com.zhjh.util.Common;
import com.zhjh.util.QrCodeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.util.List;

/**
 *
 * @author lanyuan 2014-11-19
 * @Email: mmm333zzz520@163.com
 * @version 3.0v
 */
@Controller
@RequestMapping("/workoverwage/")
public class WorkoverwageController extends BaseController {
	@Inject
	private WorkoverwageMapper workoverwageMapper;

	@Inject
	private SystemconfigMapper systemconfigMapper;



	@RequestMapping("list")
	public String list(Model model) throws Exception {
		model.addAttribute("res", findByRes());
		return Common.BACKGROUND_PATH + "/system/workoverwage/list";
	}

	@ResponseBody
	@RequestMapping("deleteEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="系统管理",methods="组管理-删除组")//凡需要处理业务逻辑的.都需要记录操作日志
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
				workoverwageMapper.deleteById(id);
			}
			return "success";
		}else{
			return "删除密码错误";
		}

	}

	@ResponseBody
	@RequestMapping("findList")
	@SystemLog(module="考勤管理",methods="加班管理-获取列表内容")//凡需要处理业务逻辑的.都需要记录操作日志
	public WorkoverwageFormMap findList(String user,String startTime,String endTime) throws Exception {
		WorkoverwageFormMap workoverwageFormMap = getFormMap(WorkoverwageFormMap.class);
		String page=getPara("page");
		String rows=getPara("rows");
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
		workoverwageFormMap.set("userId",user);
		workoverwageFormMap.set("startTime",startTime);
		workoverwageFormMap.set("endTime",endTime);
		WorkoverwageFormMap workoverwageFormMapSum=workoverwageMapper.findAllSumByAllLike(workoverwageFormMap);
		if(page.equals("1")){
			total=workoverwageMapper.findCountByAllLike(workoverwageFormMap);
		}
		workoverwageFormMap=toFormMap(workoverwageFormMap, page, rows,workoverwageFormMap.getStr("orderby"));
		List<WorkoverwageFormMap> workersubmitFormMaps=workoverwageMapper.findByAllLike(workoverwageFormMap);
		workoverwageFormMap.set("rows",workersubmitFormMaps);
		workoverwageFormMap.set("total",total);
		workoverwageFormMap.set("workoverwageFormMapSum",workoverwageFormMapSum);
		return workoverwageFormMap;
	}

	@ResponseBody
	@RequestMapping("findListAll")
	public WorkoverwageFormMap findListAll(String user,String startTime,String endTime) throws Exception {
		WorkoverwageFormMap workoverwageFormMap = getFormMap(WorkoverwageFormMap.class);
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
		workoverwageFormMap.set("user",user);
		workoverwageFormMap.set("startTime",startTime);
		workoverwageFormMap.set("endTime",endTime);
		List<WorkoverwageFormMap> workersubmitFormMaps=workoverwageMapper.findByAllLike(workoverwageFormMap);
		workoverwageFormMap.set("rows",workersubmitFormMaps);
		workoverwageFormMap.set("total",total);
		return workoverwageFormMap;
	}

	@ResponseBody
	@RequestMapping("changeshowCode")
	public WorkoverwageFormMap changeshowCode() throws Exception {
		WorkoverwageFormMap workoverwageFormMap=new WorkoverwageFormMap();
		String day=getPara("day");
		if(day==null){
			day=ToolCommon.getNowDay();
		}
		String binaryStart = QrCodeUtils.creatRrCode("start:"+day, 200,200);
		workoverwageFormMap.set("imgStart",binaryStart);
		String binaryEnd = QrCodeUtils.creatRrCode("end:"+day, 200,200);
		workoverwageFormMap.set("imgEnd",binaryEnd);
		return workoverwageFormMap;
	}

	@RequestMapping("showCode")
	public String showCode(Model model) throws Exception {
		return Common.BACKGROUND_PATH + "/system/workoverwage/codeShow";
	}

}