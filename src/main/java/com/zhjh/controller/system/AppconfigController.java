package com.zhjh.controller.system;

import com.zhjh.annotation.SystemLog;
import com.zhjh.bean.FileBean;
import com.zhjh.controller.index.BaseController;
import com.zhjh.entity.AppConfigFormMap;
import com.zhjh.mapper.AppConfigMapper;
import com.zhjh.tool.ToolCommon;
import com.zhjh.tool.ToolProject;
import com.zhjh.util.Common;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @author lanyuan 2014-11-19
 * @Email: mmm333zzz520@163.com
 * @version 3.0v
 */
@Controller
@RequestMapping("/appconfig/")
public class AppconfigController extends BaseController {
	@Inject
	private AppConfigMapper appConfigMapper;

	@RequestMapping("showAndroidUI")
	public String showAndroidUI(Model model) throws Exception {
		AppConfigFormMap appConfigFormMap=appConfigMapper.findbyFrist("origin","android",AppConfigFormMap.class);
		// android版本信息
		model.addAttribute("appConfigFormMap", appConfigFormMap);
		return Common.BACKGROUND_PATH + "/system/appconfig/modifyAndroid";
	}

	@ResponseBody
	@RequestMapping(value ="uploadAndoirdAPKM",method = RequestMethod.POST)
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="手机端上传需要完成内容图片",methods="工作流程图片-上传")//凡需要处理业务逻辑的.都需要记录操作日志
	public String uploadAndoirdAPKM(String code, HttpServletRequest request, @RequestParam(value = "file", required = false) MultipartFile file) throws Exception {
			String url= ToolProject.getAndoridAPKUrl(request);
			FileBean fileBean= ToolCommon.uploadFileNotChangeName(url,file);
			AppConfigFormMap appConfigFormMap=appConfigMapper.findbyFrist("origin","android",AppConfigFormMap.class);
			if(appConfigFormMap==null||appConfigFormMap.size()==0){
                appConfigFormMap=new AppConfigFormMap();
				appConfigFormMap.set("origin","android");
				appConfigFormMap.set("path","upload/android/"+fileBean.getName());
				appConfigFormMap.set("code",code);
				appConfigFormMap.set("updateTime",ToolCommon.getNowTime());
				appConfigMapper.addEntity(appConfigFormMap);
			}else{
				appConfigFormMap.set("path","upload/android/"+fileBean.getName());
				appConfigFormMap.set("code",code);
				appConfigFormMap.set("updateTime",ToolCommon.getNowTime());
				appConfigMapper.editEntity(appConfigFormMap);
			}
		return "success";
	}
}