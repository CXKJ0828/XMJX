package com.zhjh.controller.system;

import com.zhjh.annotation.SystemLog;
import com.zhjh.controller.index.BaseController;
import com.zhjh.entity.*;
import com.zhjh.mapper.*;
import com.zhjh.plugin.PageView;
import com.zhjh.tool.ToolCommon;
import com.zhjh.util.Common;
import com.zhjh.util.PasswordHelper;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
@RequestMapping("/employee/")
public class EmployeeController extends BaseController {
	@Inject
	private EmployeeMapper employeeMapper;
	@Inject
	private UserRoleMapper userRoleMapper;

	@Inject
	private UserMapper userMapper;
	@RequestMapping("list")
	public String listUI(Model model) throws Exception {
		model.addAttribute("res", findByRes());
		return Common.BACKGROUND_PATH + "/system/employee/list";
	}

	@ResponseBody
	@RequestMapping("findByPage")
	public EmployeeFormMap findByPage(String content) throws Exception {
		String page=getPara("page");

		String rows=getPara("rows");
		EmployeeFormMap employeeFormMap = getFormMap(EmployeeFormMap.class);
		if(content==null){
			content="";
		}
		employeeFormMap.set("content",content);
		if(page.equals("1")){
			total=employeeMapper.findCountByAllLike(employeeFormMap);
		}
		employeeFormMap=toFormMap(employeeFormMap, page, rows,employeeFormMap.getStr("orderby"));
		List<EmployeeFormMap> departmentFormMapList=employeeMapper.findByAllLike(employeeFormMap);
		employeeFormMap.set("rows",departmentFormMapList);
		employeeFormMap.set("total",total);
		return employeeFormMap;
	}

	@ResponseBody
	@RequestMapping("employeeSelect")
	public PageView employeeSelect() throws Exception {
		pageView.setRecords(employeeMapper.selectNotInUserEntity());
		return pageView;
	}

	@RequestMapping("addUI")
	public String addUI(Model model) throws Exception {
		return Common.BACKGROUND_PATH + "/system/employee/add";
	}

	@ResponseBody
	@RequestMapping("addEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="组织架构",methods="员工管理-新增")//凡需要处理业务逻辑的.都需要记录操作日志
	public String addEntity(String entity) throws Exception {
		String errorId="";
		List<EmployeeFormMap> list = (List) ToolCommon.json2ObjectList(entity, EmployeeFormMap.class);
		String nowtime=ToolCommon.getNowTime();
		UserFormMap userFormMap=getNowUserMessage();
		String userId=String.valueOf(userFormMap.getInt("id"));
		String roleId="";
		for(int i=0;i<list.size();i++){
			EmployeeFormMap departmentFormMap=list.get(i);
			roleId=departmentFormMap.get("roleId")+"";
			String id=departmentFormMap.getStr("id");
			if(id!=null&&!id.equals("")){
				EmployeeFormMap employeeFormMap1=new EmployeeFormMap();
				employeeFormMap1.set("id",id);
				List<EmployeeFormMap> employeeFormMaps=employeeMapper.findByNames(employeeFormMap1);
				departmentFormMap.set("modifytime",nowtime);
				departmentFormMap.set("userId",userId);
				if(employeeFormMaps==null||employeeFormMaps.size()==0){//
					employeeMapper.addEntity(departmentFormMap);
					String name=departmentFormMap.get("name")+"";
					String userName=departmentFormMap.get("id")+"";
					UserFormMap userFormMap1 = new UserFormMap();
					userFormMap1.set("userName",name);
					userFormMap1.set("accountName",userName);
					userFormMap1.set("createTime",nowtime);
					PasswordHelper passwordHelper = new PasswordHelper();
					userFormMap1.set("password","123456789");
					passwordHelper.encryptPassword(userFormMap1);
					userMapper.addEntity(userFormMap1);//新增后返回新增信息
					String userId1=userFormMap1.get("id")+"";
					UserRoleFormMap userRoleFormMap=new UserRoleFormMap();
					userRoleFormMap.set("userId",userId1);
					userRoleFormMap.set("roleId",roleId);
					userRoleMapper.addEntity(userRoleFormMap);
				}else if(employeeFormMaps.size()==1){
					String name=departmentFormMap.getStr("name");
					List<UserFormMap> userFormMaps=userMapper.findByAttribute("accountName",id,UserFormMap.class);
					if(userFormMaps.size()>0){
						for(int j=0;j<userFormMaps.size();j++){
							UserFormMap userFormMap1=userFormMaps.get(j);
							if(j==userFormMaps.size()-1){
								if(userFormMap1!=null){
									userFormMap1.set("userName",name);
									userMapper.editEntity(userFormMap1);
									String userId1=userFormMap1.get("id")+"";
									UserRoleFormMap userRoleFormMap=userRoleMapper.findbyFrist("userId",userId1,UserRoleFormMap.class);
									if(userRoleFormMap!=null){
										userRoleFormMap.set("roleId",roleId);
										userRoleMapper.updateRoleIdByUserId(userRoleFormMap);
									}
								}
							}else{
								userMapper.deleteByAttribute("id",userFormMap1.get("id")+"",UserFormMap.class);
							}

						}
					}


					String from=departmentFormMap.getStr("from");
					if(from!=null&&from.equals("新增")){
						errorId=id+";"+errorId;
					}else{
						employeeMapper.editEntity(departmentFormMap);
					}

				}else{
					errorId=errorId+";"+id;
				}
			}

		}
		if(errorId.equals("")){
			return "success";
		}else{
			return "success:"+errorId;
		}

	}
	/**
	 * 验证资源是否存在
	 *
	 * @author lanyuan Email：mmm333zzz520@163.com date：2014-2-19
	 * @param name
	 * @return
	 */
	@RequestMapping("employeeId_isExist")
	@ResponseBody
	public boolean employeeIdIsExist(String name) {

		EmployeeFormMap employeeFormMap = new EmployeeFormMap();
		employeeFormMap.put("employeeId", name);
		List<EmployeeFormMap> list = baseMapper.findByNames(employeeFormMap);
		if (list == null || list.size()<=0) {
			return true;
		} else {
			return false;
		}
	}
	@ResponseBody
	@RequestMapping("deleteEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="系统管理",methods="组管理-删除组")//凡需要处理业务逻辑的.都需要记录操作日志
	public String deleteEntity() throws Exception {
		String ids = getPara("ids");
		ids=ids.substring(0,ids.length()-1);
		String[] idsStr=ids.split(",");
		for(int i=0;i<idsStr.length;i++){
			String id=idsStr[i];
			userRoleMapper.deleteByUserAccountName(id);
			employeeMapper.deleteById(id);
			userMapper.deleteByAccountName(id);
		}
		return "success";
	}

	@ResponseBody
	@RequestMapping("resetPasswordEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="系统管理",methods="组管理-删除组")//凡需要处理业务逻辑的.都需要记录操作日志
	public String resetPasswordEntity() throws Exception {
		String ids = getPara("ids");
		ids=ids.substring(0,ids.length()-1);
		String[] idsStr=ids.split(",");
		for(int i=0;i<idsStr.length;i++){
			String id=idsStr[i];
			UserFormMap userFormMap1 = userMapper.findByAttribute("accountName",id,UserFormMap.class).get(0);
			PasswordHelper passwordHelper = new PasswordHelper();
			userFormMap1.set("password","123456789");
			passwordHelper.encryptPassword(userFormMap1);
			userMapper.editEntity(userFormMap1);//新增后返回新增信息
		}
		return "success";
	}

	@ResponseBody
	@RequestMapping("createAccountEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="系统管理",methods="组管理-删除组")//凡需要处理业务逻辑的.都需要记录操作日志
	public String createAccountEntity() throws Exception {
		String nowtime=ToolCommon.getNowTime();
		String ids = getPara("ids");
		ids=ids.substring(0,ids.length()-1);
		String[] idsStr=ids.split(",");
		for(int i=0;i<idsStr.length;i++){
			String id=idsStr[i];
			EmployeeFormMap departmentFormMap=employeeMapper.findbyFrist("id",id,EmployeeFormMap.class);
				String roleId="14";
				String name=departmentFormMap.get("name")+"";
				String userName=departmentFormMap.get("id")+"";
				UserFormMap userFormMap1 = new UserFormMap();
				userFormMap1.set("userName",name);
				userFormMap1.set("accountName",userName);
				userFormMap1.set("createTime",nowtime);
				PasswordHelper passwordHelper = new PasswordHelper();
				userFormMap1.set("password","123456789");
				passwordHelper.encryptPassword(userFormMap1);
				userMapper.addEntity(userFormMap1);//新增后返回新增信息
				String userId1=userFormMap1.get("id")+"";
				UserRoleFormMap userRoleFormMap=new UserRoleFormMap();
				userRoleFormMap.set("userId",userId1);
				userRoleFormMap.set("roleId",roleId);
				userRoleMapper.addEntity(userRoleFormMap);

		}
		return "success";
	}

	@RequestMapping("editUI")
	public String editUI(Model model) throws Exception {
		String id = getPara("id");
		if(Common.isNotEmpty(id)){
			model.addAttribute("employee", employeeMapper.findbyFrist("id", id, EmployeeFormMap.class));
		}
		return Common.BACKGROUND_PATH + "/system/employee/edit";
	}
	@ResponseBody
	@RequestMapping("getEmployeeById")
	public EmployeeFormMap getEmployeeById(@RequestParam(required=true) String id) throws Exception {
		EmployeeFormMap employeeFormMap=new EmployeeFormMap();
		EmployeeFormMap employeeFormMap1=employeeMapper.findbyFrist("id",id,EmployeeFormMap.class);
		if(employeeFormMap1!=null){
			employeeFormMap.set("id","1");
			employeeFormMap.set("employeeId",employeeFormMap1.getStr("employeeId"));
		}else{
			employeeFormMap.set("id","0");
			employeeFormMap.set("result","所选员工不存在");
		}
		return employeeFormMap;
	}

	@ResponseBody
	@RequestMapping("editEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="系统管理",methods="组管理-修改组")//凡需要处理业务逻辑的.都需要记录操作日志
	public String editEntity() throws Exception {
		EmployeeFormMap employeeFormMap = getFormMap(EmployeeFormMap.class);
		employeeMapper.editEntity(employeeFormMap);
		return "success";
	}
	

}