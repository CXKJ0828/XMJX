package com.zhjh.controller.system;

import com.zhjh.annotation.SystemLog;
import com.zhjh.controller.index.BaseController;
import com.zhjh.entity.LogFormMap;
import com.zhjh.entity.SystemconfigFormMap;
import com.zhjh.entity.UserFormMap;
import com.zhjh.mapper.LogMapper;
import com.zhjh.mapper.SystemconfigMapper;
import com.zhjh.plugin.PageView;
import com.zhjh.tool.ToolCommon;
import com.zhjh.util.Common;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @author lanyuan 2014-11-19
 * @Email: mmm333zzz520@163.com
 * @version 3.0v
 */
@Controller
@RequestMapping("/systemconfig/")
public class SystemConfigController extends BaseController {
	@Inject
	private SystemconfigMapper systemconfigMapper;




	@RequestMapping("taxRatemodify")
	public String taxRatemodify(Model model) throws Exception {
		SystemconfigFormMap systemconfigFormMap=systemconfigMapper.findByAttribute("name","taxRate",SystemconfigFormMap.class).get(0);
		model.addAttribute("systemconfigFormMap", systemconfigFormMap);
		return Common.BACKGROUND_PATH + "/system/config/taxRatemodify";
	}


	@ResponseBody
	@RequestMapping("edittaxRateEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="系统管理",methods="用户管理-修改用户")//凡需要处理业务逻辑的.都需要记录操作日志
	public String edittaxRateEntity(String entity) throws Exception {
		String taxRate= ToolCommon.json2Object(entity).getString("taxRate");
		SystemconfigFormMap systemconfigFormMap=systemconfigMapper.findByAttribute("name","taxRate",SystemconfigFormMap.class).get(0);
		systemconfigFormMap.set("content",taxRate);
		systemconfigMapper.editEntity(systemconfigFormMap);
		return "success";
	}


	@RequestMapping("blankAmountmodify")
	public String blankAmountmodify(Model model) throws Exception {
		SystemconfigFormMap systemconfigFormMap=systemconfigMapper.findByAttribute("name","blankAmount",SystemconfigFormMap.class).get(0);
		model.addAttribute("systemconfigFormMap", systemconfigFormMap);
		return Common.BACKGROUND_PATH + "/system/config/blankAmountmodify";
	}


	@ResponseBody
	@RequestMapping("editblankAmountEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="系统管理",methods="用户管理-修改用户")//凡需要处理业务逻辑的.都需要记录操作日志
	public String editblankAmountEntity(String entity) throws Exception {
		String blankAmount= ToolCommon.json2Object(entity).getString("blankAmount");
		SystemconfigFormMap systemconfigFormMap=systemconfigMapper.findByAttribute("name","blankAmount",SystemconfigFormMap.class).get(0);
		systemconfigFormMap.set("content",blankAmount);
		systemconfigMapper.editEntity(systemconfigFormMap);
		return "success";
	}

	@RequestMapping("timeoutHourmodify")
	public String timeoutHourmodify(Model model) throws Exception {
		SystemconfigFormMap systemconfigFormMap=systemconfigMapper.findByAttribute("name","timeoutHour",SystemconfigFormMap.class).get(0);
		model.addAttribute("systemconfigFormMap", systemconfigFormMap);
		return Common.BACKGROUND_PATH + "/system/config/timeoutHourmodify";
	}

	@ResponseBody
	@RequestMapping("edittimeoutHourEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="系统管理",methods="超时设定值")//凡需要处理业务逻辑的.都需要记录操作日志
	public String edittimeoutHourEntity(String entity) throws Exception {
		String timeoutHour= ToolCommon.json2Object(entity).getString("timeoutHour");
		SystemconfigFormMap systemconfigFormMap=systemconfigMapper.findByAttribute("name","timeoutHour",SystemconfigFormMap.class).get(0);
		systemconfigFormMap.set("content",timeoutHour);
		systemconfigMapper.editEntity(systemconfigFormMap);
		return "success";
	}


	@RequestMapping("deductionBaseValuemodify")
	public String deductionBaseValuemodify(Model model) throws Exception {
		SystemconfigFormMap systemconfigFormMap=systemconfigMapper.findByAttribute("name","deductionBaseValue",SystemconfigFormMap.class).get(0);
		model.addAttribute("systemconfigFormMap", systemconfigFormMap);
		return Common.BACKGROUND_PATH + "/system/config/deductionBaseValuemodify";
	}

	@ResponseBody
	@RequestMapping("editdeductionBaseValueEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="系统管理",methods="扣款基础值")//凡需要处理业务逻辑的.都需要记录操作日志
	public String editdeductionBaseValueEntity(String entity) throws Exception {
		String deductionBaseValue= ToolCommon.json2Object(entity).getString("deductionBaseValue");
		SystemconfigFormMap systemconfigFormMap=systemconfigMapper.findByAttribute("name","deductionBaseValue",SystemconfigFormMap.class).get(0);
		systemconfigFormMap.set("content",deductionBaseValue);
		systemconfigMapper.editEntity(systemconfigFormMap);
		return "success";
	}

	@RequestMapping("timeoutHourCategorymodify")
	public String timeoutHourCategorymodify(Model model) throws Exception {
		SystemconfigFormMap systemconfigFormMap=systemconfigMapper.findByAttribute("name","timeoutHourCategory",SystemconfigFormMap.class).get(0);
		model.addAttribute("systemconfigFormMap", systemconfigFormMap);
		return Common.BACKGROUND_PATH + "/system/config/timeoutHourCategorymodify";
	}

	@ResponseBody
	@RequestMapping("edittimeoutHourCategoryEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="系统管理",methods="超时设定值")//凡需要处理业务逻辑的.都需要记录操作日志
	public String edittimeoutHourCategoryEntity(String entity) throws Exception {
		String timeoutHourCategory= ToolCommon.json2Object(entity).getString("timeoutHourCategory");
		SystemconfigFormMap systemconfigFormMap=systemconfigMapper.findByAttribute("name","timeoutHourCategory",SystemconfigFormMap.class).get(0);
		systemconfigFormMap.set("content",timeoutHourCategory);
		systemconfigMapper.editEntity(systemconfigFormMap);
		return "success";
	}


	@RequestMapping("deductionBaseValueCategorymodify")
	public String deductionBaseValueCategorymodify(Model model) throws Exception {
		SystemconfigFormMap systemconfigFormMap=systemconfigMapper.findByAttribute("name","deductionBaseValueCategory",SystemconfigFormMap.class).get(0);
		model.addAttribute("systemconfigFormMap", systemconfigFormMap);
		return Common.BACKGROUND_PATH + "/system/config/deductionBaseValueCategorymodify";
	}

	@ResponseBody
	@RequestMapping("editdeductionBaseValueCategoryEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="系统管理",methods="扣款基础值")//凡需要处理业务逻辑的.都需要记录操作日志
	public String editdeductionBaseValueCategoryEntity(String entity) throws Exception {
		String deductionBaseValueCategory= ToolCommon.json2Object(entity).getString("deductionBaseValueCategory");
		SystemconfigFormMap systemconfigFormMap=systemconfigMapper.findByAttribute("name","deductionBaseValueCategory",SystemconfigFormMap.class).get(0);
		systemconfigFormMap.set("content",deductionBaseValueCategory);
		systemconfigMapper.editEntity(systemconfigFormMap);
		return "success";
	}

	@RequestMapping("wagesworkhourmodify")
	@SystemLog(module="系统设置",methods="工资工时")
	public String wagesworkhourmodify(Model model) throws Exception {
		SystemconfigFormMap systemconfigFormMapHours=systemconfigMapper.findByAttribute("name","dailyWorkingHours",SystemconfigFormMap.class).get(0);
		SystemconfigFormMap systemconfigFormMapWages=systemconfigMapper.findByAttribute("name","dailyWages",SystemconfigFormMap.class).get(0);
		model.addAttribute("systemconfigFormMapHours", systemconfigFormMapHours);
		model.addAttribute("systemconfigFormMapWages", systemconfigFormMapWages);
		return Common.BACKGROUND_PATH + "/system/config/wagesworkhourmodify";
	}

	@RequestMapping("workoverwage")
	@SystemLog(module="系统设置",methods="加班工资")
	public String workoverwage(Model model) throws Exception {
		SystemconfigFormMap systemconfigFormMapWages=systemconfigMapper.findByAttribute("name","workoverwage",SystemconfigFormMap.class).get(0);
		model.addAttribute("systemconfigFormMapWages", systemconfigFormMapWages);
		return Common.BACKGROUND_PATH + "/system/config/workoverwage";
	}


	@ResponseBody
	@RequestMapping("editwagesworkhourEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="系统设置",methods="工资工时-修改")//凡需要处理业务逻辑的.都需要记录操作日志
	public String editwagesworkhourEntity(String entity) throws Exception {
		String dailyWorkingHours= ToolCommon.json2Object(entity).getString("dailyWorkingHours");
		SystemconfigFormMap systemconfigFormMap=systemconfigMapper.findByAttribute("name","dailyWorkingHours",SystemconfigFormMap.class).get(0);
		systemconfigFormMap.set("content",dailyWorkingHours);
		systemconfigMapper.editEntity(systemconfigFormMap);

		String dailyWages= ToolCommon.json2Object(entity).getString("dailyWages");
		SystemconfigFormMap systemconfigFormMap1=systemconfigMapper.findByAttribute("name","dailyWages",SystemconfigFormMap.class).get(0);
		systemconfigFormMap1.set("content",dailyWages);
		systemconfigMapper.editEntity(systemconfigFormMap1);
		return "success";
	}

	@ResponseBody
	@RequestMapping("editworkoverWageEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="系统设置",methods="加班工资-修改")//凡需要处理业务逻辑的.都需要记录操作日志
	public String editworkoverWageEntity(String entity) throws Exception {
		String workoverwage= ToolCommon.json2Object(entity).getString("workoverwage");
		SystemconfigFormMap systemconfigFormMap=systemconfigMapper.findByAttribute("name","workoverwage",SystemconfigFormMap.class).get(0);
		systemconfigFormMap.set("content",workoverwage);
		systemconfigMapper.editEntity(systemconfigFormMap);
		return "success";
	}
}