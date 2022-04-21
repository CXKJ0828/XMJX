package com.zhjh.controller.system;

import com.zhjh.annotation.SystemLog;
import com.zhjh.controller.index.BaseController;
import com.zhjh.entity.ToolTypeFormMap;
import com.zhjh.entity.SystemconfigFormMap;
import com.zhjh.entity.UserFormMap;
import com.zhjh.mapper.ToolTypeMapper;
import com.zhjh.mapper.SystemconfigMapper;
import com.zhjh.tool.ToolCommon;
import com.zhjh.util.Common;
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
@RequestMapping("/toolType/")
public class ToolTypeController extends BaseController {
	@Inject
	private ToolTypeMapper toolTypeMapper;

	@Inject
	private SystemconfigMapper systemconfigMapper;

	@RequestMapping("list")
	public String list(Model model) throws Exception {
		model.addAttribute("res", findByRes());
		return Common.BACKGROUND_PATH + "/system/toolType/list";
	}

	@ResponseBody
	@RequestMapping("findByPage")
	public ToolTypeFormMap findByPage(String content) throws Exception {
		if(content==null){
			content="";
		}
		ToolTypeFormMap toolTypeFormMap=new ToolTypeFormMap();
		List<ToolTypeFormMap> toolTypeFormMaps=toolTypeMapper.findByAllLike(content);
		toolTypeFormMap.set("rows",toolTypeFormMaps);
		return toolTypeFormMap;
	}

	@ResponseBody
	@RequestMapping("toolTypeSelect")
	public List<ToolTypeFormMap> toolTypeSelect(String q) throws Exception {
		q=getPara("q");
		String content=getPara("content");
		if(q==null){
			q="";
		}
		List<ToolTypeFormMap> toolTypeFormMaps=toolTypeMapper.findByAllLike(q);
		return toolTypeFormMaps;
	}

	@ResponseBody
	@RequestMapping("editEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="组织架构",methods="员工管理-新增")//凡需要处理业务逻辑的.都需要记录操作日志
	public String editEntity(String entity) throws Exception {
		List<ToolTypeFormMap> list = (List) ToolCommon.json2ObjectList(entity, ToolTypeFormMap.class);
		String nowtime=ToolCommon.getNowTime();
		UserFormMap userFormMap=getNowUserMessage();
		String userId=String.valueOf(userFormMap.getInt("id"));
		for(int i=0;i<list.size();i++){
			ToolTypeFormMap toolTypeFormMap=list.get(i);
			String id=toolTypeFormMap.get("id")+"";
			toolTypeFormMap.set("modifytime",nowtime);
			toolTypeFormMap.set("userId",userId);
			if(id==null||id.equals("")||id.equals("null")){
				toolTypeFormMap.remove("id");
				toolTypeMapper.addEntity(toolTypeFormMap);
			}else{
				toolTypeMapper.editEntity(toolTypeFormMap);
			}
		}
		return "success";
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
				toolTypeMapper.deleteById(id);
			}
			return "success";
		}else{
			return "删除订单密码错误";
		}

	}

}