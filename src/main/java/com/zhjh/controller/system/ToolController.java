package com.zhjh.controller.system;

import com.zhjh.annotation.SystemLog;
import com.zhjh.controller.index.BaseController;
import com.zhjh.entity.SystemconfigFormMap;
import com.zhjh.entity.ToolFormMap;
import com.zhjh.entity.UserFormMap;
import com.zhjh.mapper.SystemconfigMapper;
import com.zhjh.mapper.ToolMapper;
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
@RequestMapping("/tool/")
public class ToolController extends BaseController {
	@Inject
	private ToolMapper toolMapper;

	@Inject
	private SystemconfigMapper systemconfigMapper;

	@RequestMapping("list")
	public String list(Model model) throws Exception {
		model.addAttribute("res", findByRes());
		return Common.BACKGROUND_PATH + "/system/tool/list";
	}

	@ResponseBody
	@RequestMapping("findByPage")
	public ToolFormMap findByPage(String content) throws Exception {
		String toolTypeId=getPara("toolTypeId");
		String page=getPara("page");

		String rows=getPara("rows");
		ToolFormMap toolFormMap=new ToolFormMap();
		if(content==null){
			content="";
		}
		toolFormMap.set("content",content);
		toolFormMap.set("toolTypeId",toolTypeId);
		if(page.equals("1")){
			total=toolMapper.findCountByAllLike(toolFormMap);
		}
		toolFormMap=toFormMap(toolFormMap, page, rows,toolFormMap.getStr("orderby"));
		List<ToolFormMap> toolFormMaps=toolMapper.findByAllLike(toolFormMap);
		toolFormMap.set("rows",toolFormMaps);
		toolFormMap.set("total",total);
		return toolFormMap;
	}

	@ResponseBody
	@RequestMapping("toolSelect")
	public List<ToolFormMap> toolSelect(String q) throws Exception {
		q=getPara("q");
		if(q==null){
			q="";
		}
		ToolFormMap toolFormMap=new ToolFormMap();
		toolFormMap.set("content",q);
		List<ToolFormMap> toolFormMaps=toolMapper.findByAllLike(toolFormMap);
		return toolFormMaps;
	}

	@ResponseBody
	@RequestMapping("editEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="组织架构",methods="员工管理-新增")//凡需要处理业务逻辑的.都需要记录操作日志
	public String editEntity(String entity) throws Exception {
		String errorMessage="";
		List<ToolFormMap> list = (List) ToolCommon.json2ObjectList(entity, ToolFormMap.class);
		String nowtime=ToolCommon.getNowTime();
		UserFormMap userFormMap=getNowUserMessage();
		String userId=String.valueOf(userFormMap.getInt("id"));
		for(int i=0;i<list.size();i++){
			ToolFormMap toolFormMap=list.get(i);
			String id=toolFormMap.get("id")+"";
			toolFormMap.set("modifytime",nowtime);
			toolFormMap.set("userId",userId);
			if(id==null||id.equals("")||id.equals("null")){
				String name=toolFormMap.get("name")+"";
				name=name.replace(" ","");
				toolFormMap.set("name",name);
				ToolFormMap toolFormMap1=toolMapper.findByName(name);
				if(toolFormMap1==null){
					toolFormMap.remove("id");
					toolMapper.addEntity(toolFormMap);
				}else{
					errorMessage=errorMessage+"["+name+"]品名已存在;";
				}

			}else{
				toolMapper.editEntity(toolFormMap);
			}
		}
		if(ToolCommon.isNull(errorMessage)){
			return "success";
		}else{
			return errorMessage;
		}

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
				toolMapper.deleteById(id);
			}
			return "success";
		}else{
			return "删除订单密码错误";
		}

	}

}