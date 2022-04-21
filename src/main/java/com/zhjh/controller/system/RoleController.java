package com.zhjh.controller.system;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.zhjh.bean.ComboboxEntity;
import com.zhjh.entity.*;
import com.zhjh.mapper.RoleProcessMapper;
import com.zhjh.mapper.TeamMapper;
import com.zhjh.tool.ToolCommon;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhjh.annotation.SystemLog;
import com.zhjh.controller.index.BaseController;
import com.zhjh.mapper.RoleMapper;
import com.zhjh.util.Common;

/**
 * 
 * @author lanyuan 2014-11-19
 * @Email: mmm333zzz520@163.com
 * @version 3.0v
 */
@Controller
@RequestMapping("/role/")
public class RoleController extends BaseController {
	@Inject
	private RoleMapper roleMapper;

	@Inject
	private TeamMapper teamMapper;

	@Inject
	private RoleProcessMapper roleProcessMapper;

	private int listSize;

	@RequestMapping("list")
	public String listUI(Model model) throws Exception {
		RoleFormMap roleFormMap = getFormMap(RoleFormMap.class);
		List<ResFormMap> res=findByRes();
		model.addAttribute("res",res);
		listSize=roleMapper.findByWhere(roleFormMap).size();
		return Common.BACKGROUND_PATH + "/system/role/list";
	}


	@ResponseBody
	@RequestMapping("findByPage")
	public RoleFormMap findByPage() throws Exception {
		String page=getPara("page");

		String rows=getPara("rows");
		RoleFormMap roleFormMap = getFormMap(RoleFormMap.class);
		if(page.equals("1")){
			total=roleMapper.seletAllCount();
		}
		roleFormMap=toFormMap(roleFormMap, page, rows,roleFormMap.getStr("orderby"));
        List<RoleFormMap> roleFormMaps=roleMapper.findByPage(roleFormMap);
		roleFormMap.set("rows",roleFormMaps);
		roleFormMap.set("total",total);
		return roleFormMap;
	}

	@ResponseBody
	@RequestMapping("processRoleEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="组织架构",methods="员工管理-新增")//凡需要处理业务逻辑的.都需要记录操作日志
	public String processRoleEntity(String entity) throws Exception {
		String nowtime=ToolCommon.getNowTime();
		String processSelect=ToolCommon.json2Object(entity).getString("processSelect");
		String roleId=ToolCommon.json2Object(entity).getString("roleId");
		roleProcessMapper.deleteByAttribute("roleId",roleId,RoleProcessFormMap.class);
		List<ProcessFormMap> processFormMapList = (List) ToolCommon.json2ObjectList(processSelect, ProcessFormMap.class);
		for(int i=0;i<processFormMapList.size();i++){
			ProcessFormMap processFormMap=processFormMapList.get(i);
			RoleProcessFormMap roleProcessFormMap=new RoleProcessFormMap();
			roleProcessFormMap.set("roleId",roleId);
			roleProcessFormMap.set("processId",processFormMap.get("id")+"");
			roleProcessFormMap.set("modifyTime",nowtime);
			roleProcessMapper.addEntity(roleProcessFormMap);
		}

		return "success";
	}

	@ResponseBody
	@RequestMapping("teamRoleEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="组织架构",methods="员工管理-新增")//凡需要处理业务逻辑的.都需要记录操作日志
	public String teamRoleEntity(String entity) throws Exception {
		String userIds=ToolCommon.json2Object(entity).getString("userIds");
		userIds=userIds.replace("[","");
		userIds=userIds.replace("]","");
		userIds=userIds.replace("\"","");
		String roleId=ToolCommon.json2Object(entity).getString("roleId");
		TeamFormMap teamFormMap=teamMapper.findbyFrist("roleId",roleId,TeamFormMap.class);
		if(teamFormMap==null){
			teamFormMap=new TeamFormMap();
			teamFormMap.set("roleId",roleId);
			teamFormMap.set("userIds",userIds);
			teamMapper.addEntity(teamFormMap);
		}else{
			teamFormMap.set("userIds",userIds);
			teamMapper.editEntity(teamFormMap);
		}

		return "success";
	}

	@RequestMapping("processRoleUI")
	public String processRoleUI(Model model,String roleId) throws Exception {
		model.addAttribute("roleId",roleId);
		return Common.BACKGROUND_PATH + "/system/role/processRole";
	}

	@RequestMapping("processRoleShowUI")
	public String processRoleShowUI(Model model,String roleId) throws Exception {
		model.addAttribute("roleId",roleId);
		return Common.BACKGROUND_PATH + "/system/role/processRoleShow";
	}

	@RequestMapping("teamRoleShowUI")
	public String teamRoleShowUI(Model model,String roleId) throws Exception {
		model.addAttribute("roleId",roleId);
		TeamFormMap teamFormMap=teamMapper.findbyFrist("roleId",roleId,TeamFormMap.class);
		if(teamFormMap==null){
			teamFormMap=new TeamFormMap();
		}
		model.addAttribute("teamFormMap",teamFormMap);
		return Common.BACKGROUND_PATH + "/system/role/teamRoleShow";
	}

	@RequestMapping("addUI")
	public String addUI(Model model) throws Exception {
		return Common.BACKGROUND_PATH + "/system/role/add";
	}
	@ResponseBody
	@RequestMapping("processShow")
	public List<RoleProcessFormMap>  processShow(String roleId) throws Exception {
		RoleProcessFormMap roleProcessFormMap=new RoleProcessFormMap();
		roleProcessFormMap.set("roleId",roleId);
		List<RoleProcessFormMap> roleProcessFormMaps=roleProcessMapper.findByRoleId(roleProcessFormMap);
		return roleProcessFormMaps;
	}
	@ResponseBody
	@RequestMapping("getSelectList")
	public List<ComboboxEntity>  getSelectList() throws Exception {
		RoleFormMap roleFormMap = getFormMap(RoleFormMap.class);
		List<RoleFormMap> roleFormMapList=roleMapper.findByWhere(roleFormMap);
		List<ComboboxEntity> comboboxEntityList=new ArrayList<>();
		for(int i=0;i<roleFormMapList.size();i++){
			RoleFormMap roleFormMap1=roleFormMapList.get(i);
			ComboboxEntity entity=new ComboboxEntity();
			entity.id=roleFormMap1.get("id")+"";
			entity.text=roleFormMap1.getStr("name");
			comboboxEntityList.add(entity);
		}
		return comboboxEntityList;
	}

	@ResponseBody
	@RequestMapping("addEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="系统管理",methods="组管理-新增组")//凡需要处理业务逻辑的.都需要记录操作日志
	public String addEntity(String entity) throws Exception {
		List<RoleFormMap> list = (List) ToolCommon.json2ObjectList(entity, RoleFormMap.class);
		for(int i=0;i<list.size();i++){
			RoleFormMap roleFormMap=list.get(i);
			String id=roleFormMap.get("id")+"";
			RoleFormMap roleFormMap1=roleMapper.findbyFrist("id",id,RoleFormMap.class);
			if(roleFormMap1==null){
				roleMapper.addEntity(roleFormMap);
			}else{
				roleMapper.editEntity(roleFormMap);
			}

		}
//		RoleFormMap roleFormMap = getFormMap(RoleFormMap.class);
//
		System.out.print(list);
		return "success";
	}

	@ResponseBody
	@RequestMapping("deleteEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="系统管理",methods="组管理-删除组")//凡需要处理业务逻辑的.都需要记录操作日志
	public String deleteEntity() throws Exception {
		String ids = getPara("ids");
		ids=ids.substring(0,ids.length()-1);
			roleMapper.deleteByAttribute("id", ids, RoleFormMap.class);
		return "success";
	}

	@ResponseBody
	@RequestMapping("saveTeamEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="系统管理",methods="组管理-删除组")//凡需要处理业务逻辑的.都需要记录操作日志
	public String saveTeamEntity() throws Exception {

		return "success";
	}

	@RequestMapping("editUI")
	public String editUI(Model model) throws Exception {
		String id = getPara("id");
		if(Common.isNotEmpty(id)){
			model.addAttribute("role", roleMapper.findbyFrist("id", id, RoleFormMap.class));
		}
		return Common.BACKGROUND_PATH + "/system/role/edit";
	}

	@ResponseBody
	@RequestMapping("editEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="系统管理",methods="组管理-修改组")//凡需要处理业务逻辑的.都需要记录操作日志
	public String editEntity() throws Exception {
		RoleFormMap roleFormMap = getFormMap(RoleFormMap.class);
		roleMapper.editEntity(roleFormMap);
		return "success";
	}
	
	
	@RequestMapping("selRole")
	public String seletRole(Model model) throws Exception {
		RoleFormMap roleFormMap = getFormMap(RoleFormMap.class);
		Object userId = roleFormMap.get("userId");
		if(null!=userId){
			List<RoleFormMap> list = roleMapper.seletUserRole(roleFormMap);
			String ugid = "";
			for (RoleFormMap ml : list) {
				ugid += ml.get("id")+",";
			}
			ugid = Common.trimComma(ugid);
			model.addAttribute("txtRoleSelect", ugid);
			model.addAttribute("userRole", list);
			if(StringUtils.isNotBlank(ugid)){
				roleFormMap.put("where", " where id not in ("+ugid+")");
			}
		}
		List<RoleFormMap> roles = roleMapper.findByWhere(roleFormMap);
		model.addAttribute("role", roles);
		return Common.BACKGROUND_PATH + "/system/user/roleSelect";
	}

}